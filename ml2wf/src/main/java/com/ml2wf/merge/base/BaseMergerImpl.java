package com.ml2wf.merge.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.conventions.enums.bpmn.BPMNNames;
import com.ml2wf.conventions.enums.fm.FMAttributes;
import com.ml2wf.conventions.enums.fm.FMNames;
import com.ml2wf.merge.AbstractMerger;
import com.ml2wf.merge.concretes.WFMetaMerger;
import com.ml2wf.tasks.base.Task;
import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.tasks.concretes.BPMNTask;
import com.ml2wf.tasks.concretes.FMTask;
import com.ml2wf.tasks.factory.TaskFactory;
import com.ml2wf.tasks.manager.TasksManager;
import com.ml2wf.tasks.specs.FMTaskSpecs;
import com.ml2wf.util.FileHandler;
import com.ml2wf.util.Pair;

public abstract class BaseMergerImpl extends AbstractMerger implements BaseMerger {

	/**
	 * The {@code FMTask} corresponding to the merged workflow's created task.
	 *
	 * @see FMTask
	 */
	protected FMTask createdWFTask;
	/**
	 * The {@code Task} corresponding of the <b>global unmanaged</b> created
	 * {@code Task}.
	 *
	 * @see Task
	 */
	protected static FMTask unmanagedTask;
	/**
	 * Unmanaged parent's name.
	 *
	 * <p>
	 *
	 * Unmanaged nodes will be placed under a parent with this name.
	 */
	public static final String UNMANAGED = "Unmanaged";
	/**
	 * Root's parent name.
	 *
	 * <p>
	 *
	 * Global nodes (e.g. Meta, Steps, Unmanaged...) will be placed under this one.
	 */
	private static final String ROOT = "AD";

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
	 * Sets the {@link BaseMergerImpl#unmanagedTask} value.
	 *
	 * @param unmanagedTask the new unmanaged {@code FMTask}
	 *
	 * @since 1.0
	 */
	protected static void setUnmanagedTask(FMTask unmanagedTask) {
		BaseMergerImpl.unmanagedTask = unmanagedTask;
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
		Set<WFTask<?>> tasks;
		for (File file : files) {
			Pair<String, Document> wfInfo = this.getWFDocInfoFromFile(file);
			if (wfInfo.isEmpty()) {
				// TODO: add logs
				return;
			}
			Document wfDocument = wfInfo.getValue();
			// create associated tasks
			tasks = getTasksList(wfDocument, BPMNNames.SELECTOR).stream()
					.map(this.getTaskFactory()::createTasks).flatMap(Collection::stream).map(t -> (WFTask<?>) t)
					.collect(Collectors.toSet());
			annotations.addAll(this.getAnnotations(wfDocument));
			if (completeMerge) {
				this.processCompleteMerge(wfInfo.getKey(), tasks);
				this.processSpecificNeeds(wfInfo);
			}
			tasks.clear(); // clearing to free memory
		}
		// process created tasks
		TasksManager.getWFTasks().stream().forEach(this::processTask);
		this.processAnnotations(annotations);
		this.endProcessUnmanagedNode();
	}

	@Override
	public void mergeWithWF(boolean backUp, boolean completeMerge, File... wfFiles) throws Exception {
		for (File wfFile : wfFiles) {
			this.mergeWithWF(backUp, completeMerge, wfFile);
		}
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
	 * @since 1.0
	 * @see TaskFactory
	 * @see FMTask
	 */
	private void createFMTasks() {
		List<Node> fmTasksList = getTasksList(getDocument(), FMNames.SELECTOR);
		// create fm tasks foreach task node
		fmTasksList.stream().forEach(this.getTaskFactory()::createTasks);
		// get the unmanaged global task
		setUnmanagedTask(this.getGlobalFMTask(UNMANAGED));
		// update created tasks' parents
		TasksManager.updateFMParents(TasksManager.getFMTasks());
	}

	/**
	 * Removes the {@link #unmanagedTask} from the task list and its node from the
	 * current {@link BaseMergerImpl#getDocument()} if it has no child.
	 *
	 * <p>
	 *
	 * This allow the user to keep an unmanagedTask-free FeatureModel if it is not
	 * required.
	 */
	private void endProcessUnmanagedNode() {
		// cleaning the unmanaged node to eliminate useless children
		unmanagedTask.setNode(cleanChildren(unmanagedTask.getNode()));
		if (!unmanagedTask.getNode().hasChildNodes()) {
			// removing the unmanaged node if not needed
			Optional<Task<FMTaskSpecs>> opt = unmanagedTask.getParent().removeChild(unmanagedTask);
			if (opt.isPresent()) {
				TasksManager.removeTask(opt.get());
			}
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
	 * using the {@link #processAssocConstraints(Document, String)} method,</li>
	 * <li>retrieves the root parent node using the {@link #getRootParentNode()}
	 * method,</li>
	 * <li>inserts the workflow's corresponding task under the root parent
	 * node.</li>
	 * </ul>
	 *
	 * @param wfInfo workflow's informations
	 * @throws InvalidConstraintException
	 *
	 * @see Pair
	 */
	private void processCompleteMerge(String wfName, Set<WFTask<?>> tasks) throws InvalidConstraintException {
		this.createdWFTask = this.createFeatureWithName(wfName, this instanceof WFMetaMerger);
		FMTask root = this.getRootParentNode();
		this.createdWFTask = this.insertNewTask(root, this.createdWFTask);
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
	 *
	 * @since 1.0
	 * @see BPMNTask
	 * @see FMTask
	 */
	protected void processTask(WFTask<?> task) {
		String taskName = task.getName();
		Optional<FMTask> optFMTask;
		Optional<?> optTask;
		if (TasksManager.existsinFM(taskName)) {
			// if task is already in the FM
			optFMTask = unmanagedTask.getChildWithName(taskName);
			if (optFMTask.isEmpty()) {
				// if it is not under the unmanaged node
				return;
			}
			optTask = unmanagedTask.removeChild(optFMTask.get());
			if (optTask.isEmpty()) {
				return; // TODO: throw error
			}
			FMTask duplicatedTask = (FMTask) optTask.get();
			// task = this.mergeNodes(task, duplicatedTask); // TODO: to modify
		}
		// retrieving a suitable parent
		FMTask parentTask = this.getSuitableParent(task);
		// inserting the new task
		this.insertNewTask(parentTask, task);

	}

	// TODO
	protected <T extends Task<?>> T mergeNodes(T taskA, T taskB) {
		// TODO: to change according to recent changes in task OOC
		// TODO: improve considering conflicts (e.g same child & different levels)
		/*-NodeList nodeBChildren = nodeB.getChildNodes();
		for (int i = 0; i < nodeBChildren.getLength(); i++) {
			nodeA.appendChild(nodeBChildren.item(i));
		}
		return nodeA;*/

		// TODO: STEPS :
		// foreach child of taskB, get child's FMTask
		// append child to taskA
		return null;
	}

	protected Node mergeNodes(Node nodeA, Node nodeB) {
		// TODO: improve considering conflicts (e.g same child & different levels)
		NodeList nodeBChildren = nodeB.getChildNodes();
		for (int i = 0; i < nodeBChildren.getLength(); i++) {
			nodeA.appendChild(nodeBChildren.item(i));
		}
		return nodeA;
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
	 *
	 * @since 1.0
	 * @see Task
	 */
	protected FMTask getGlobalFMTask(String globalNodeName) {
		Optional<FMTask> optGlobalTask = TasksManager.getFMTaskWithName(globalNodeName);
		return optGlobalTask.orElseGet(() -> this.createGlobalFMTask(globalNodeName));
	}

	/**
	 * Creates the global {@code FMTask} instance corresponding to the given
	 * {@code globalNodeName}.
	 *
	 * @param globalNodeName the global node name
	 * @return the created global {@code FMTask} instance
	 *
	 * @since 1.0
	 * @see FMTask
	 */
	protected FMTask createGlobalFMTask(String globalNodeName) {
		// create the node element
		Element globalElement = getDocument().createElement(FMNames.FEATURE.getName());
		globalElement.setAttribute(FMAttributes.ABSTRACT.getName(), String.valueOf(true));
		globalElement.setAttribute(FMAttributes.NAME.getName(), globalNodeName);
		// create the global task
		Optional<Task<?>> optGlobalTask = this.getTaskFactory().createTasks(globalElement).stream().findFirst();
		if (optGlobalTask.isPresent()) {
			FMTask globalTask = (FMTask) optGlobalTask.get();
			Optional<FMTask> optRoot = TasksManager.getFMTaskWithName(ROOT); // get the root
			if (optRoot.isPresent()) {
				return optRoot.get().appendChild(globalTask);
			}
		}
		return null;
	}

	/**
	 * Returns the given {@code task}'s referred {@code FMTask} or the given
	 * {@code defaultTask} if no valid reference was found.
	 *
	 * @param task        {@code BPMNTask} containing the reference
	 * @param defaultTask the default {@code FMTask}
	 * @return the given {@code task}'s referred {@code FMTask} or the given
	 *         {@code defaultTask} if no valid reference was found
	 *
	 * @since 1.0
	 * @see BPMNTask
	 * @see FMTask
	 */
	protected FMTask getReferredFMTask(WFTask<?> task, FMTask defaultTask) {
		String reference = task.getReference();
		if (!reference.isBlank()) {
			// if contains a documentation node that can refer to a generic task
			Optional<FMTask> optRef = TasksManager.getFMTaskWithName(reference);
			return optRef.orElseGet(() -> this.createReferredFMTask(task));
		}
		return defaultTask;
	}

	/**
	 * Creates and returns a {@code FMTask} specified by the given
	 * {@code task}'s reference.
	 *
	 * @param task task containing the reference
	 * @return a {@code FMTask} specified by the given
	 *         {@code task}'s reference
	 *
	 * @since 1.0
	 * @see BPMNTask
	 * @see FMTask
	 */
	protected FMTask createReferredFMTask(WFTask<?> task) {
		FMTask newParent = this.createFeatureWithName(task.getReference(), task.isAbstract());
		Optional<WFTask<?>> opt = TasksManager.getWFTaskWithName(newParent.getName());
		if (opt.isEmpty()) {
			FMTask globalTask = this.getGlobalFMTask(WFMetaMerger.STEP_TASK);
			newParent.setAbstract(globalTask.isAbstract());
			return globalTask.appendChild(newParent); // TODO: check 132
		}
		return this.getSuitableParent(opt.get()).appendChild(newParent);
	}
}
