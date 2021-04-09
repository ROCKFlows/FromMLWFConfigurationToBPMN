package com.ml2wf.merge.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.conventions.enums.bpmn.BPMNNames;
import com.ml2wf.conventions.enums.fm.FMAttributes;
import com.ml2wf.conventions.enums.fm.FMNames;
import com.ml2wf.merge.AbstractMerger;
import com.ml2wf.merge.concretes.WFMetaMerger;
import com.ml2wf.merge.exceptions.MergeException;
import com.ml2wf.tasks.base.Task;
import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.tasks.concretes.BPMNTask;
import com.ml2wf.tasks.concretes.FMTask;
import com.ml2wf.tasks.factory.TaskFactory;
import com.ml2wf.tasks.manager.TasksManager;

import com.ml2wf.tasks.specs.FMTaskSpecs;
import com.ml2wf.util.FileHandler;
import com.ml2wf.util.Pair;
import com.ml2wf.util.XMLManager;

@Log4j2
public abstract class BaseMergerImpl extends AbstractMerger implements BaseMerger {

    /**
     * Unmanaged parent's name.
     *
     * <p>
     *
     * Unmanaged nodes will be placed under a parent with this name.
     */
    public static final String UNMANAGED = "Unmanaged";
    /**
     * Unmanaged tasks parent's name.
     *
     * <p>
     *
     * Unmanaged task nodes will be placed under a parent with this name.
     */
    public static final String UNMANAGED_TASKS = "Unmanaged_Tasks";
    /**
     * Unmanaged features parent's name.
     *
     * <p>
     *
     * Unmanaged feature nodes will be placed under a parent with this name.
     */
    public static final String UNMANAGED_FEATURES = "Unmanaged_Features";
    /**
     * The default root name.
     */
    public static final String DEFAULT_ROOT_NAME = "Root";
    /**
     * The deeper default root name.
     */
    public static final String DEEPER_DEFAULT_ROOT_NAME = "AD";
    /**
     * {@code HashMap} that contains all global unmanaged tasks.
     */
    protected static Map<String, FMTask> unmanagedGlobalTasks = new HashMap<>();
    /**
     * The {@code FMTask} corresponding to the merged workflow's created task.
     *
     * @see FMTask
     */
    protected FMTask createdWFTask;

    /**
     * {@code BaseMergerImpl}'s default constructor.
     *
     * @param file  the FeatureModel {@code File}
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    protected BaseMergerImpl(File file) throws ParserConfigurationException, SAXException, IOException {
        super(file);
    }

    /**
     * Returns the unmanaged global task with the given {@code name}.
     *
     * @param name  name of the requested unmanaged global task
     *
     * @return the {@link #unmanagedGlobalTasks} corresponding value (according to the given name key)
     */
    public static FMTask getUnmanagedGlobalTask(String name) {
        return unmanagedGlobalTasks.get(name);
    }

    @Override
    public void mergeWithWF(boolean backUp, boolean completeMerge, File wfFile) throws Exception {
        if (backUp) {
            FileHandler.backUp(getPath(), getDocument());
        }
        List<Node> annotations = new ArrayList<>();
        Set<File> files = getFiles(wfFile);
        // create existing FM tasks + unmanagedTask
        createFMTasks();
        // process files
        Set<Node> nodes;
        List<WFTask<?>> tasks = new ArrayList<>();
        for (File file : files) {
            // retrieving document information
            Pair<String, Document> wfInfo = getWFDocInfoFromFile(file);
            if (wfInfo.isEmpty()) {
                continue;
            }
            Document wfDocument = wfInfo.getValue();
            // retrieving all WF's nodes
            nodes = getTasksList(wfDocument, BPMNNames.SELECTOR).stream().map(AbstractMerger::getNestedNodes)
                    .flatMap(Collection::stream).collect(Collectors.toSet());
            // creating associated tasks
            for (Node node : nodes) {
                try {
                    tasks.add(getTaskFactory().createTask(node));
                } catch (UnresolvedConflict e) {
                    log.warn("Skipping node {}...", getNodeName(node));
                }
            }

            // saving annotations
            annotations.addAll(getAnnotations(wfDocument));
            if (completeMerge) {
                processCompleteMerge(wfInfo.getKey(), tasks);
                processSpecificNeeds(wfInfo);
            }
            // clearing to free memory
            tasks.clear();
        }
        // process created tasks
        for (WFTask<?> wfTask : TasksManager.getWFTasks()) {
            processTask(wfTask);
        }
        TasksManager.updateFMParents(TasksManager.getFMTasks());
        TasksManager.updateStatus(TasksManager.getWFTasks());
        processAnnotations(annotations);
        endProcessUnmanagedNodes();
        TasksManager.clearWFTasks();

    }

    @Override
    public void mergeWithWF(boolean backUp, boolean completeMerge, File... wfFiles) throws Exception {
        for (File wfFile : wfFiles) {
            mergeWithWF(backUp, completeMerge, wfFile);
        }
    }

    @Override
    protected void normalizeDocument() {
        getDocument().getDocumentElement().normalize();
        List<Node> taskNodes = XMLManager.getTasksList(getDocument(), FMNames.SELECTOR);
        taskNodes.stream()
                .map(t -> t.getAttributes().getNamedItem(FMAttributes.NAME.getName()))
                .filter(Objects::nonNull)
                .forEach(t -> t.setNodeValue(t.getNodeValue().trim().replace(" ", "_")));
    }

    /**
     * Returns a {@code List<File>} from the given {@code File}.
     *
     * @param file  the file to retrieve contained files
     *
     * @return a {@code List<File>} from the given {@code File}
     *
     * @throws IOException
     *
     * @see File
     */
    private Set<File> getFiles(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException(String.format("The given workflow file does not exist (%s)", file));
        }
        Set<File> files;
        try (Stream<Path> stream = Files.walk(file.toPath())) {
            files = stream.parallel().map(Path::toFile).filter(File::isFile)
                    .filter(p -> FilenameUtils.isExtension(p.getName(), FileHandler.getWfExtensions()))
                    .collect(Collectors.toSet());
        }
        if (files.isEmpty()) {
            // wfFile is a regular file (not a directory)
            files.add(file);
        }
        return files;
    }

    /**
     * Calls the {@link #getTaskFactory()} to create {@code FMTask} instances from
     * the FM {@link #getDocument()} and the {@link #unmanagedGlobalTasks}.
     *
     * <p>
     *
     * It finally updates the created FM tasks to update the parents informations
     * using the {@link TasksManager#updateFMParents(Set)} method.
     *
     * @throws MergeException
     * @throws UnresolvedConflict
     *
     * @see TaskFactory
     * @see FMTask
     */
    private void createFMTasks() throws MergeException, UnresolvedConflict {
        if (TasksManager.getFMTasks().isEmpty()) {
            List<Node> fmTasksList = getTasksList(getDocument(), FMNames.SELECTOR);
            // create fm tasks foreach task node
            for (Node taskNode : fmTasksList) {
                getTaskFactory().createTask(taskNode);
            }
        }
        // creating required unmanaged parent nodes
        startProcessUnmanagedNodes();
        // update created tasks' parents
        TasksManager.updateFMParents(TasksManager.getFMTasks());
    }

    // unmanaged tasks part

    /**
     * Creates the {@link #unmanagedGlobalTasks} and its children.
     *
     * <p>
     *
     * This allow the user to retrieve unmanaged tasks and features under these
     * global tasks.
     *
     * @throws MergeException
     * @throws UnresolvedConflict
     */
    private void startProcessUnmanagedNodes() throws MergeException, UnresolvedConflict {
        // get the unmanaged global task
        FMTask unmanagedTask = getGlobalFMTask(UNMANAGED);
        unmanagedGlobalTasks.put(UNMANAGED, unmanagedTask);
        // creating the unmanaged tasks global task
        unmanagedGlobalTasks.put(UNMANAGED_TASKS, unmanagedTask.appendChild(getUnmanaged(UNMANAGED_TASKS)));
        // creating the unmanaged features global task
        unmanagedGlobalTasks.put(UNMANAGED_FEATURES, unmanagedTask.appendChild(getUnmanaged(UNMANAGED_FEATURES)));
    }

    /**
     * Retrieves the unmanaged node with the given {@code name} or creates it if
     * missing.
     *
     * @param name  name of the wished unmanaged node
     *
     * @return the unmanaged node with the given {@code name}
     *
     * @throws UnresolvedConflict
     */
    private static FMTask getUnmanaged(String name) throws UnresolvedConflict {
        Optional<FMTask> opt = TasksManager.getFMTaskWithName(name);
        if (opt.isPresent()) {
            return opt.get();
        }
        Element unmanagedTasksElement = createFeatureWithAbstract(name, true);
        return getTaskFactory().createTask(unmanagedTasksElement);
    }

    /**
     * Removes the "unmanaged tasks" from the task list and their nodes from the
     * current {@link BaseMergerImpl#getDocument()} if they have no child using the
     * {@link #removeUnusedGlobalTask(FMTask)} (FMTask)} method.
     *
     * <p>
     *
     * This allow the user to keep an unmanagedTask-free FeatureModel if it is not
     * required.
     */
    private static void endProcessUnmanagedNodes() {
        // processing children first
        FMTask unmanagedTask = unmanagedGlobalTasks.remove(UNMANAGED);
        unmanagedGlobalTasks.values().forEach(BaseMergerImpl::removeUnusedGlobalTask);
        // processing the parent at the end
        removeUnusedGlobalTask(unmanagedTask);
    }

    /**
     * Removes the given {@code globalTask} if it is not used.
     *
     * @param globalTask    the global task to remove
     */
    private static void removeUnusedGlobalTask(FMTask globalTask) {
        globalTask.setNode(cleanChildren(globalTask.getNode()));
        if (!globalTask.getNode().hasChildNodes()) {
            // removing the unmanaged node if not needed
            Optional<Task<FMTaskSpecs>> opt = globalTask.getParent().removeChild(globalTask);
            opt.ifPresent(TasksManager::removeTask);
        }
    }

    /**
     * Processes the complete merge of the workflow describe by the
     * {@code wfInfo Pair}'s instance.
     *
     * <p>
     *
     * More precisely,
     *
     * <p>
     *
     * <ul>
     * <li>processes the association of constraints involving the given workflow
     * using the {@link #processAssocConstraints(String, List)} method,</li>
     * <li>retrieves the root parent node using the {@link #getRootParentNode()}
     * method,</li>
     * <li>inserts the workflow's corresponding task under the root parent
     * node.</li>
     * </ul>
     *
     * @param wfName    the workflow's name
     * @param tasks     the {@link List}<{@link WFTask}> to insert
     *
     * @throws InvalidConstraintException
     * @throws MergeException
     * @throws UnresolvedConflict
     *
     * @see Pair
     */
    private void processCompleteMerge(String wfName, List<WFTask<?>> tasks)
            throws InvalidConstraintException, MergeException, UnresolvedConflict {
        createdWFTask = createFMTaskWithName(wfName, this instanceof WFMetaMerger);
        FMTask root = getRootParentNode();
        createdWFTask = insertNewTask(root, createdWFTask);
        processAssocConstraints(wfName, tasks);
    }

    /**
     * Processes the given {@code task} converting it from {@link BPMNTask} to
     * {@link FMTask}.
     *
     * <p>
     *
     * More precisely, this method :
     *
     * <p>
     *
     * <ul>
     * <li>removes the corresponding {@code FMTask} from the {@link #unmanagedGlobalTasks}
     * if it is contained by this one,</li>
     * <li>retrieves a suitable parent for the given {@code task} using the
     * {@link #getSuitableParent(WFTask)} method,</li>
     * <li>inserts the given {@code task} under the retrieved {@code parentTask}
     * using the {@link #insertNewTask(FMTask, Task)} method.</li>
     * </ul>
     *
     * @param task  task to process
     *
     * @throws MergeException
     * @throws UnresolvedConflict
     *
     * @see BPMNTask
     * @see FMTask
     */
    protected void processTask(WFTask<?> task) throws MergeException, UnresolvedConflict {
        String taskName = task.getName();

        // Begin new Code --- MIREILLE
        if (TasksManager.existsInFM(taskName) && !processDuplicatedTask(task)) {
            log.debug("{} already exists in FM, nothing to do", task);
            return;
        }

        // retrieving a suitable parent
        FMTask parentTask = getSuitableParent(task);
        log.debug("------> ### processTask {} with suitable Parent {}", task.getName(), parentTask);
        TasksManager.addTask(parentTask);
        // inserting the new task
        insertNewTaskUnlessConflict(parentTask, task);
        log.debug("--END----> ###  processTask  {} with suitable Parent {} has now for reference {}",
                task.getName(), parentTask, task.getReference());

    }

    /**
     * Processes the given {@code task} and returns whether we have more to do with this task or not.
     *
     * <p>
     *
     * More precisely, this method :
     *
     * <p>
     *
     * <ul>
     * <li> checks if the task is already known as unmanaged (return true)</li>
     * </ul>
     *
     * @param task  the {@link WFTask} to process
     *
     * @return whether we have more to do with this task or not
     *
     * @throws MergeException
     * @throws UnresolvedConflict
     */
    private boolean processDuplicatedTask(WFTask<?> task) throws MergeException, UnresolvedConflict {
        FMTask unmanagedTask = unmanagedGlobalTasks.get(UNMANAGED_TASKS);
        Optional<FMTask> optFMTask = unmanagedTask.getChildWithName(task.getName());
        String ref = task.getReference();
        if (ref == null || ref.equals(UNMANAGED_TASKS)) {
            log.debug("The new task is already unmanaged- ");
            return false;
        } else {
            // We must remember the new parent
            dealWithUnmanagedTask(unmanagedTask, optFMTask, task);
            return true;
        }
    }

    /**
     * Processes the given {@code task} and the corresponding FMTask if exists {@code optFMTask}
     * and return whether this task is modified or not
     *
     * <p>
     *
     * More precisely, this method :
     *
     * <p>
     *
     * @param unmanagedTask
     * @param optFMTask
     * @param task
     *
     * @return whether this task is modified or not
     *
     * @throws MergeException
     * @throws UnresolvedConflict
     */
    private boolean dealWithUnmanagedTask(FMTask unmanagedTask, Optional<FMTask> optFMTask, WFTask<?> task)
            throws MergeException, UnresolvedConflict {
        if (optFMTask.isEmpty()) {
            // if it is not under the unmanaged_tasks node (
            log.debug("{} is not under the unmanaged_tasks node", task);
            return false;
        }
        log.debug("{} is under the unmanaged_tasks node and should go to {}", task, getSuitableParent(task));
        unmanagedTask.removeChild(optFMTask.get());
        optFMTask.get().setParent(null);
        return true;
    }

    /**
     * Processes the given {@code taskName} as a duplicated {@code Task}'s name.
     *
     * <p>
     *
     * A duplicated task is a task already contained in the FeatureModel.
     *
     * <p>
     *
     * The following operations can be applied to a duplicated task depending of the
     * situation :
     *
     * <p>
     *
     * <ul>
     * <li>removing the duplicated task from the {@link #unmanagedGlobalTasks},</li>
     * <li>merging the original task's node with the duplicated's one.</li>
     * </ul>
     *
     * @param taskName  the duplicated task's name
     *
     * @return whether further operations are needed or not
     */
    private boolean processDuplicatedTask(String taskName) {
        FMTask unmanagedTask = unmanagedGlobalTasks.get(UNMANAGED_TASKS);
        Optional<FMTask> optFMTask = unmanagedTask.getChildWithName(taskName);
        if (optFMTask.isEmpty()) {
            // if it is not under the unmanaged_tasks node
            return false;
        }
        unmanagedTask.removeChild(optFMTask.get());
        return true;
    }

    /**
     * Returns the <b>global</b> {@code FMTask} with the given
     * {@code globalNodeName}.
     *
     * <p>
     *
     * <b>Note</b> that this method create it if not exist using the
     * {@link #createGlobalFMTask(String)} method.
     *
     * <p>
     *
     * @param globalNodeName    the <b>global</b> {@code Task} with the given
     *                          {@code globalNodeName}
     *
     * @return the global {@code Task}
     *
     * @throws MergeException
     * @throws UnresolvedConflict
     *
     * @see Task
     */
    protected FMTask getGlobalFMTask(String globalNodeName) throws MergeException, UnresolvedConflict {
        Optional<FMTask> optGlobalTask = TasksManager.getFMTaskWithName(globalNodeName);
        if (optGlobalTask.isEmpty()) {
            return createGlobalFMTask(globalNodeName);
        }
        return optGlobalTask.get();
    }

    /**
     * Creates the global {@code FMTask} instance corresponding to the given
     * {@code globalNodeName}.
     *
     * @param globalNodeName    the global node name
     *
     * @return the created global {@code FMTask} instance
     *
     * @throws MergeException
     * @throws UnresolvedConflict
     *
     * @see FMTask
     */
    protected FMTask createGlobalFMTask(String globalNodeName) throws MergeException, UnresolvedConflict {
        // create the node element
        Element globalElement = createFeatureWithAbstract(globalNodeName, true);
        // create the global task
        FMTask globalTask = getTaskFactory().createTask(globalElement);
        // get the root node
        Optional<Node> optRoot = getFeatureNodeAtLevel(getDocument(), 2);
        Node rootNode = optRoot.orElseThrow(() -> new MergeException("Invalid FeatureModel structure."));
        // get the corresponding root task
        String rootNodeName = XMLManager.getNodeName(rootNode);
        Optional<FMTask> optRootTask = TasksManager.getFMTaskWithName(rootNodeName);
        return optRootTask
                .orElseThrow(() -> new MergeException("Can't retrieve the global task with name : " + rootNodeName))
                .appendChild(globalTask);
    }

    /**
     * Returns the given {@code task}'s referred {@code FMTask} or the given
     * {@code defaultTask} if no valid reference was found.
     *
     * @param task          {@code BPMNTask} containing the reference
     * @param defaultTask   the default {@code FMTask}
     *
     * @return the given {@code task}'s referred {@code FMTask} or the given
     *         {@code defaultTask} if no valid reference was found
     *
     * @throws MergeException
     * @throws UnresolvedConflict
     *
     * @see BPMNTask
     * @see FMTask
     */
    protected FMTask getReferredFMTask(WFTask<?> task, FMTask defaultTask) throws MergeException, UnresolvedConflict {
        String reference = task.getReference();
        if (!reference.isBlank()) {
            // if contains a documentation node that can refer to a generic task
            Optional<FMTask> optRef = TasksManager.getFMTaskWithName(reference);
            if (optRef.isEmpty()) {
                return createReferredFMTask(task);
            } else if ((optRef.get().getParent() != null) &&
                    optRef.get().getParent().getName().equals(UNMANAGED_TASKS)) {
                // removing the reference from the unmanaged node if it is present
                processDuplicatedTask(reference);
            }
            return optRef.get();
        }
        return defaultTask;
    }

    /**
     * Creates and returns a {@code FMTask} specified by the given {@code task}'s
     * reference.
     *
     * @param task  task containing the reference
     *
     * @return a {@code FMTask} specified by the given {@code task}'s reference
     *
     * @throws MergeException
     * @throws UnresolvedConflict
     *
     * @see BPMNTask
     * @see FMTask
     */
    protected FMTask createReferredFMTask(WFTask<?> task) throws MergeException, UnresolvedConflict {
        String reference = task.getReference();
        log.warn("The referenced task [{}] is missing in the FeatureModel.", reference);
        log.warn("Creating the referenced task : {}", reference);
        Optional<WFTask<?>> opt = TasksManager.getWFTaskWithName(reference);
        FMTask newParent = createFMTaskWithName(reference, opt.isEmpty() || opt.get().isAbstract());
        opt = TasksManager.getWFTaskWithName(newParent.getName());
        FMTask globalTask = (opt.isEmpty()) ? getGlobalFMTask(WFMetaMerger.STEP_TASK) : getSuitableParent(opt.get());
        return insertNewTask(globalTask, newParent);
    }

    // Mireille
    public FMTask insertNewTaskUnlessConflict(FMTask newParent, Task<?> task) throws UnresolvedConflict {
        log.debug("******* Inserting task unless conflict : {}  with parent {} ", task.getName(), newParent);
        Optional<FMTask> optNewParent = TasksManager.getFMTaskWithName(newParent.getName());
        Optional<FMTask> optFeature = TasksManager.getFMTaskWithName(task.getName());
        //TODO Remove test on Parent
        if (optNewParent.isPresent()) {
            if (optFeature.isPresent()) {
                return bothFeaturesExist(optFeature.get(), optNewParent.get());
            } else {
                // task is a new feature
                log.debug("only add feature {}}", task);
                return addFeature(task, newParent);
            }
        }
        return newParent;

    }

    private static FMTask addFeature(Task<?> task, FMTask newParent) throws UnresolvedConflict {
        FMTask childTask = (task instanceof FMTask) ? (FMTask) task : taskFactory.convertWFtoFMTask((WFTask<?>) task);
        return newParent.appendChild(childTask);
    }

    public FMTask bothFeaturesExist(FMTask child, FMTask newParent) throws UnresolvedConflict {
        // TO IMPROVE !!.
        FMTask currentParent = child.getParent();
        if (Objects.equals(currentParent, newParent)) {
            log.debug("We do nothing, It'OK : {} has for parent {} -- and refers to {}", child, newParent,
                    child.getParent());
            return newParent;
        }
        if ((currentParent == null)
                || currentParent.getName().equals(UNMANAGED)
                || currentParent.getName().equals(UNMANAGED_TASKS)
                || currentParent.getName().equals(UNMANAGED_FEATURES)) {
            return moveChildToNewParent(currentParent, child, newParent);
        }

        String childName = child.getName();
        if ((childName.equals(DEFAULT_ROOT_NAME)) || (childName.equals(WFMetaMerger.STEP_TASK))) {
            return child;
        }

        if (isChildOf(newParent, child)) {
            // newParent#child is valid => newParent#currentParent or
            // currentParent#newParent is valid
            if (isChildOf(newParent, currentParent)) {
                // newParent#child and newParent#currentParent#child is valid. Nothing to do
                log.debug("We do nothing, we assume {} has for parent {} -- and refers to {}",
                        child, newParent, child.getParent());
                return newParent;
            } else {
                //// newParent#child and currentParent#child is valid but
                //// newParent#currentParent is invalid
                log.warn("Something strange ...  it's a child of newParent but not from cu .. {} has moved " +
                        "from {} to parent -- and refers to {}", child, currentParent, newParent);
                return moveChildToNewParent(currentParent, child, newParent);
            }
        } else {
            log.debug("{} is not child of {}, it's child of {}", child, newParent, currentParent);
            if (isChildOf(currentParent, newParent)) {
                // currentParent#child, currentParent#newParent, newParent#child =>
                // currentParent#newParent#child : we can move
                log.debug("{} moves under {}, it's child of {}", child, newParent, currentParent);
                return moveChildToNewParent(currentParent, child, newParent);
            } else {
                // currentParent#child, newParent#child => but nor currentParent#newParent nor
                // newParent#currentParent we have a conflict
                // parent#newparent is non valid, we can’t move
                throw new UnresolvedConflict("Add an order relationship between %s and %s", currentParent, newParent);
            }
        }
    }

    private FMTask moveChildToNewParent(FMTask currentParent, FMTask child, FMTask newParent) {
        log.warn("{} has to move from {} to parent {} and refers to {}", child, currentParent, newParent,
                child.getParent());
        if (currentParent == null) {
            setParent(child, newParent);
            return newParent;
        }
        Optional<FMTask> optCurrentParent = TasksManager.getFMTaskWithName(currentParent.getName());
        setParent(child, newParent);
        optCurrentParent.ifPresent(fmTask -> fmTask.removeChild(child));
        return newParent;
    }

    private static void setParent(FMTask child, FMTask newParent) {
        child.setParent(newParent);
        newParent.appendChild(child);
    }

    private static boolean isChildOf(FMTask parent, FMTask child) {
        return TasksManager.getFMTaskWithParent(parent).stream()
                .anyMatch(t -> t.getName().equals(child.getName()) || isChildOf(t, child));
    }
}
