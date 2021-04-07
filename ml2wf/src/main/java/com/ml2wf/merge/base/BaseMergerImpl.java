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

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

public abstract class BaseMergerImpl extends AbstractMerger implements BaseMerger {

	/**
	 * The {@code FMTask} corresponding to the merged workflow's created task.
	 *
	 * @see FMTask
	 */
	protected FMTask createdWFTask;
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
	 * {@code HashMap} that contains all global unmanaged tasks.
	 */
	protected static Map<String, FMTask> unmanagedGlobalTasks = new HashMap<>();
	/**
	 * The default root name.
	 */
	public static final String DEFAULT_ROOT_NAME = "Root";
	/**
	 * The deeper default root name.
	 */
	public static final String DEEPER_DEFAULT_ROOT_NAME = "AD";
	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(BaseMergerImpl.class);

	/**
	 * {@code BaseMergerImpl}'s default constructor.
	 *
	 * @param filePath the FeatureModel {@code File}
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public BaseMergerImpl(File file) throws ParserConfigurationException, SAXException, IOException {
		super(file);
	}

	/**
	 * Returns the unmanaged global task with the given {@code name}.
	 *
	 * @param name name of the requested unmanaged global task
	 * @return
	 */
	public static FMTask getUnmanagedGlobalTask(String name) {
		return unmanagedGlobalTasks.get(name);
	}

	@Override
	public void mergeWithWF(boolean backUp, boolean completeMerge, File wfFile) throws Exception {
		if (backUp) {
			FileHandler.backUp(this.getPath(), getDocument());
		}
		List<Node> annotations = new ArrayList<>();
		Set<File> files = this.getFiles(wfFile);
		// create existing FM tasks + unmanagedTask
		this.createFMTasks();
		// process files
		Set<Node> nodes;
		List<WFTask<?>> tasks = new ArrayList<>();
		for (File file : files) {
			// retrieving document informations
			Pair<String, Document> wfInfo = this.getWFDocInfoFromFile(file);
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
					logger.warn("Skipping node {}...", getNodeName(node));
				}
			}

			// saving annotations
			annotations.addAll(this.getAnnotations(wfDocument));
			if (completeMerge) {
				this.processCompleteMerge(wfInfo.getKey(), tasks);
				this.processSpecificNeeds(wfInfo);
			}
			tasks.clear(); // clearing to free memory
		}
		// process created tasks
		for (WFTask<?> wfTask : TasksManager.getWFTasks()) {
			this.processTask(wfTask);
		}
		TasksManager.updateFMParents(TasksManager.getFMTasks());
		TasksManager.updateStatus(TasksManager.getWFTasks());
		this.processAnnotations(annotations);
		endProcessUnmanagedNodes();
		TasksManager.clearWFTasks();

	}

	@Override
	public void mergeWithWF(boolean backUp, boolean completeMerge, File... wfFiles) throws Exception {
		for (File wfFile : wfFiles) {
			this.mergeWithWF(backUp, completeMerge, wfFile);
		}
	}

	@Override
	protected void normalizeDocument() {
		getDocument().getDocumentElement().normalize();
		List<Node> taskNodes = XMLManager.getTasksList(getDocument(), FMNames.SELECTOR);
		taskNodes.stream().map(t -> t.getAttributes().getNamedItem(FMAttributes.NAME.getName()))
		.filter(Objects::nonNull).forEach(t -> t.setNodeValue(t.getNodeValue().trim().replace(" ", "_")));
	}

	/**
	 * Returns a {@code List<File>} from the given {@code File}.
	 *
	 * @param file the file to retrieve contained files
	 * @return a {@code List<File>} from the given {@code File}
	 * @throws IOException
	 *
	 * @since 1.0
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
	 * the FM {@link #getDocument()} and the {@link #unmanagedTask}.
	 *
	 * <p>
	 *
	 * It finally updates the created FM tasks to update the parents informations
	 * using the {@link TasksManager#updateFMParents(Set)} method.
	 *
	 * @throws MergeException
	 * @throws UnresolvedConflict
	 *
	 * @since 1.0
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
		this.startProcessUnmanagedNodes();
		// update created tasks' parents
		TasksManager.updateFMParents(TasksManager.getFMTasks());
	}

	// unmanaged tasks part

	/**
	 * Creates the {@link #unmanagedTask} and its children.
	 *
	 * <p>
	 *
	 * This allow the user to retrieve unmanaged tasks and features under these
	 * global tasks.
	 *
	 * @throws UnresolvedConflict
	 *
	 * @since 1.0
	 */
	private void startProcessUnmanagedNodes() throws MergeException, UnresolvedConflict {
		// get the unmanaged global task
		FMTask unmanagedTask = this.getGlobalFMTask(UNMANAGED);
		unmanagedGlobalTasks.put(UNMANAGED, unmanagedTask);
		// creating the unmanaged tasks global task
		unmanagedGlobalTasks.put(UNMANAGED_TASKS, unmanagedTask.appendChild(this.getUnmanaged(UNMANAGED_TASKS)));
		// creating the unmanaged features global task
		unmanagedGlobalTasks.put(UNMANAGED_FEATURES, unmanagedTask.appendChild(this.getUnmanaged(UNMANAGED_FEATURES)));
	}

	/**
	 * Retrieves the unmanaged node with the given {@code name} or creates it if
	 * missing.
	 *
	 * @param name name of the wished unmanaged node
	 * @return the unmanaged node with the given {@code name}
	 * @throws UnresolvedConflict
	 *
	 * @since 1.0
	 */
	private FMTask getUnmanaged(String name) throws UnresolvedConflict {
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
	 * {@link #removeUnmanagedTask(FMTask)} method.
	 *
	 * <p>
	 *
	 * This allow the user to keep an unmanagedTask-free FeatureModel if it is not
	 * required.
	 *
	 * @since 1.0
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
	 * @param globalTask the global task to remove
	 *
	 * @since 1.0
	 */
	private static void removeUnusedGlobalTask(FMTask globalTask) {
		globalTask.setNode(cleanChildren(globalTask.getNode()));
		if (!globalTask.getNode().hasChildNodes()) {
			// removing the unmanaged node if not needed
			Optional<Task<FMTaskSpecs>> opt = globalTask.getParent().removeChild(globalTask);
			if (opt.isPresent()) {
				TasksManager.removeTask(opt.get());
			}
		}
	}

	// ---

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
	 * using the {@link #processAssocConstraints(Document, String)} method,</li>
	 * <li>retrieves the root parent node using the {@link #getRootParentNode()}
	 * method,</li>
	 * <li>inserts the workflow's corresponding task under the root parent
	 * node.</li>
	 * </ul>
	 *
	 * @param wfInfo workflow's informations
	 * @throws InvalidConstraintException
	 * @throws MergeException
	 * @throws UnresolvedConflict
	 *
	 * @see Pair
	 */
	private void processCompleteMerge(String wfName, List<WFTask<?>> tasks)
			throws InvalidConstraintException, MergeException, UnresolvedConflict {
		this.createdWFTask = createFMTaskWithName(wfName, this instanceof WFMetaMerger);
		FMTask root = this.getRootParentNode();
		this.createdWFTask = insertNewTask(root, this.createdWFTask);
		this.processAssocConstraints(wfName, tasks);
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
	 * <li>removes the corresponding {@code FMTask} from the {@link #unmanagedTask}
	 * if it is contained by this one,</li>
	 * <li>retrieves a suitable parent for the given {@code task} using the
	 * {@link #getSuitableParent(WFTask)} method,</li>
	 * <li>inserts the given {@code task} under the retrieved {@code parentTask}
	 * using the {@link #insertNewTask(FMTask, Task)} method.</li>
	 * </ul>
	 *
	 * @param task task to process
	 * @throws MergeException
	 * @throws UnresolvedConflict
	 *
	 * @since 1.0
	 * @see BPMNTask
	 * @see FMTask
	 */
	protected void processTask(WFTask<?> task) throws MergeException, UnresolvedConflict {
		String taskName = task.getName();


		// Begin new Code --- MIREILLE
		if (TasksManager.existsinFM(taskName) && !this.processDuplicatedTask(task)) {
			logger.debug("%s already exists in FM, nothing to do", task);
			return;
		}

		// retrieving a suitable parent
		FMTask parentTask = this.getSuitableParent(task);
		logger.debug("------> ### processTask %s with suitable Parent %s", task.getName(), parentTask);
		TasksManager.addTask(parentTask);
		// inserting the new task
		insertNewTaskUnlessConflict(parentTask, task);
		logger.debug("--END----> ###  processTask  %s with suitable Parent %s has now for reference %s", task.getName(), parentTask, task.getReference());

	}

	/**
	 * Processes the given {@code task} 
	 * and returns true  if we have  more to do with this task
	 *
	 * <p>
	 *
	 * More precisely, this method :
	 *
	 * <p>
	 * <ul>
	 * <li> checks if the task is already known as unmanaged (return true)</li>
	 * </ul>
	 * @param task
	 * @return
	 * @throws MergeException
	 * @throws UnresolvedConflict
	 */
	private boolean processDuplicatedTask(WFTask<?> task) throws MergeException, UnresolvedConflict {
		FMTask unmanagedTask = unmanagedGlobalTasks.get(UNMANAGED_TASKS);
		Optional<FMTask> optFMTask = unmanagedTask.getChildWithName(task.getName());
		String ref = task.getReference();
		
		if ((ref == null) || (ref.equals(UNMANAGED_TASKS))) {
			logger.debug("The new task is already unmanaged- ");
			return false;
		} else {// We must remember the new parent
			dealWithunmanagedTask(unmanagedTask, optFMTask, task);
			return true;
		}

	}

	/**
	 * Processes the given {@code task} and the corresponding FMTask if exists {@code optFMTask} 
	 * and  return true if this task is modified
	 *
	 * <p>
	 *
	 * More precisely, this method :
	 *
	 * <p>
	 * @param unmanagedTask
	 * @param optFMTask
	 * @param task
	 * @return
	 * @throws MergeException
	 * @throws UnresolvedConflict
	 */
	private boolean dealWithunmanagedTask(FMTask unmanagedTask, Optional<FMTask> optFMTask, WFTask<?> task)
			throws MergeException, UnresolvedConflict {
		if (optFMTask.isEmpty()) {
			// if it is not under the unmanaged_tasks node (
			logger.debug("%s is not under the unmanaged_tasks node", task);
			return false;
		} 
		logger.debug("%s is under the unmanaged_tasks node and should go to %s ", task, this.getSuitableParent(task));
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
	 * <li>removing the duplicated task from the {@link #unmanagedTask},</li>
	 * <li>merging the original task's node with the duplicated's one.</li>
	 * </ul>
	 *
	 *
	 *
	 * @param taskName the duplicated task's name
	 * @return whether further operations are needed or not
	 *
	 * @since 1.0
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
	 * {@link #createGlobalTask(String)} method.
	 *
	 * <p>
	 *
	 * @param globalNodeName the <b>global</b> {@code Task} with the given
	 *                       {@code globalNodeName}
	 * @return the global {@code Task}
	 * @throws MergeException
	 * @throws UnresolvedConflict
	 *
	 * @since 1.0
	 * @see Task
	 */
	protected FMTask getGlobalFMTask(String globalNodeName) throws MergeException, UnresolvedConflict {
		Optional<FMTask> optGlobalTask = TasksManager.getFMTaskWithName(globalNodeName);
		if (optGlobalTask.isEmpty()) {
			return this.createGlobalFMTask(globalNodeName);
		}
		return optGlobalTask.get();
	}

	/**
	 * Creates the global {@code FMTask} instance corresponding to the given
	 * {@code globalNodeName}.
	 *
	 * @param globalNodeName the global node name
	 * @return the created global {@code FMTask} instance
	 * @throws MergeException
	 * @throws UnresolvedConflict
	 *
	 * @since 1.0
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
	 * @param task        {@code BPMNTask} containing the reference
	 * @param defaultTask the default {@code FMTask}
	 * @return the given {@code task}'s referred {@code FMTask} or the given
	 *         {@code defaultTask} if no valid reference was found
	 * @throws MergeException
	 * @throws UnresolvedConflict
	 *
	 * @since 1.0
	 * @see BPMNTask
	 * @see FMTask
	 */
	protected FMTask getReferredFMTask(WFTask<?> task, FMTask defaultTask) throws MergeException, UnresolvedConflict {
		String reference = task.getReference();
		if (!reference.isBlank()) {
			// if contains a documentation node that can refer to a generic task
			Optional<FMTask> optRef = TasksManager.getFMTaskWithName(reference);
			if (optRef.isEmpty()) {
				return this.createReferredFMTask(task);
			} else if ((optRef.get().getParent() != null)
					&& optRef.get().getParent().getName().equals(UNMANAGED_TASKS)) {
				// removing the reference from the unmanaged node if it is present
				this.processDuplicatedTask(reference);
			}
			return optRef.get();
		}
		return defaultTask;
	}

	/**
	 * Creates and returns a {@code FMTask} specified by the given {@code task}'s
	 * reference.
	 *
	 * @param task task containing the reference
	 * @return a {@code FMTask} specified by the given {@code task}'s reference
	 * @throws MergeException
	 * @throws UnresolvedConflict
	 *
	 * @since 1.0
	 * @see BPMNTask
	 * @see FMTask
	 */
	protected FMTask createReferredFMTask(WFTask<?> task) throws MergeException, UnresolvedConflict {
		String reference = task.getReference();
		logger.warn("The referenced task [{}] is missing in the FeatureModel.", reference);
		logger.warn("Creating the referenced task : {}", reference);
		Optional<WFTask<?>> opt = TasksManager.getWFTaskWithName(reference);
		FMTask newParent = createFMTaskWithName(reference, (opt.isPresent()) ? opt.get().isAbstract() : opt.isEmpty());

		opt = TasksManager.getWFTaskWithName(newParent.getName());
		FMTask globalTask = (opt.isEmpty()) ? this.getGlobalFMTask(WFMetaMerger.STEP_TASK)
				: this.getSuitableParent(opt.get());
		return insertNewTask(globalTask, newParent);
	}

	// Mireille
	public <T extends Task<?>> FMTask insertNewTaskUnlessConflict(FMTask newParent, T task) throws UnresolvedConflict {
		logger.debug("******* Inserting task unless conflict : %s  with parent %s ", task.getName(), newParent);
		Optional<FMTask> optNewParent = TasksManager.getFMTaskWithName(newParent.getName());
		Optional<FMTask> optFeature = TasksManager.getFMTaskWithName(task.getName());

		//TODO Remove test on Parent
		if (optNewParent.isPresent()) {
			if (optFeature.isPresent()) {
				return bothFeaturesExist(optFeature.get(), optNewParent.get());
			} else {// task is a new feature
				logger.debug("only add feature %s", task);
				return addFeature(task, newParent);
			}
		}
		return newParent;

	}

	private <T extends Task<?>> FMTask addFeature(T task, FMTask newParent) throws UnresolvedConflict {
		FMTask childTask = (task instanceof FMTask) ? (FMTask) task : taskFactory.convertWFtoFMTask((WFTask<?>) task);
		return newParent.appendChild(childTask);
	}

	public FMTask bothFeaturesExist(FMTask child, FMTask newParent) throws UnresolvedConflict {

		// TO IMPROVE !!.
		String logMsg;
		
		FMTask currentParent = child.getParent();
			


		if (currentParent == newParent) {
			logger.debug("We do nothing, It'OK : %s has for parent %s -- and refers to %s ", child, newParent,
					child.getParent());
			return newParent;
		}

		if ( (currentParent == null) 
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
				logMsg = String.format("We do nothing, we assume %s has for parent %s -- and refers to %s ",
						child, newParent, child.getParent());
				logger.debug(logMsg);
				return newParent;
			} else {
				//// newParent#child and currentParent#child is valid but
				//// newParent#currentParent is invalid
				logger.warn(
						"Something strange ...  it's a child of newParent butnot from cu .. %s has move from %s to parent -- and refers to %s ",
						child, currentParent, newParent);
				return moveChildToNewParent(currentParent, child, newParent);
			}
		} else {
			//
			logMsg = String.format(" %s is not child of  %s, it's child of %s ", child, newParent, currentParent);
			logger.debug(logMsg);

			if (isChildOf(currentParent, newParent)) {
				// currentParrent#child, currentParent#newParent, newParent#child =>
				// currentParent#newParent#child : we can move
				logMsg = String.format(" %s moves under  %s, it's child of %s ", child, newParent, currentParent);
				logger.debug(logMsg);
				return moveChildToNewParent(currentParent, child, newParent);
			} else {
				// currentParrent#child, newParent#child => but nor currentParent#newParent nor
				// newParent#currentParent we have a conflict
				// parent#newparent is non valid, we can’t move
				throw new UnresolvedConflict(
						"add an order relationship between " + currentParent + " and " + newParent);
			}

		}

	}

	private FMTask moveChildToNewParent(FMTask currentParent, FMTask child, FMTask newParent) {
		String logMsg = String.format("%s has to move from %s to parent %s  and refers to %s ", child, currentParent, newParent,
				child.getParent());
		logger.warn(logMsg);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>"+logMsg);
		if (currentParent == null) {
			setParent(child, newParent);
			return newParent;
		}
		Optional<FMTask> optCurrentParent = TasksManager.getFMTaskWithName(currentParent.getName());
		setParent(child, newParent);
		if (optCurrentParent.isPresent()) {
			optCurrentParent.get().removeChild(child);
		}
		return newParent;
	}

	private void setParent(FMTask child, FMTask newParent) {
		child.setParent(newParent);
		newParent.appendChild(child);
	}

	private boolean isChildOf(FMTask parent, FMTask child) {

		for (FMTask task : TasksManager.getFMTaskWithParent(parent)) {
			if (task.getName().equals(child.getName())) {
				return true;
			} else {
				boolean isChild = isChildOf(task, child);
				if (isChild)
					return true;
			}
		}
		return false;
	}


}
