package com.ml2wf.merge.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.conventions.enums.bpmn.BPMNNames;
import com.ml2wf.conventions.enums.fm.FMAttributes;
import com.ml2wf.conventions.enums.fm.FMNames;
import com.ml2wf.merge.AbstractMerger;
import com.ml2wf.tasks.BPMNTask;
import com.ml2wf.tasks.FMTask;
import com.ml2wf.tasks.Task;
import com.ml2wf.tasks.manager.TasksManager;
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
	private static String UNMANAGED_PARENT_NAME = "Unmanaged";

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

	protected static void setUnmanagedTask(FMTask unmanagedTask) {
		BaseMergerImpl.unmanagedTask = unmanagedTask;
	}

	@Override
	public void mergeWithWF(boolean backUp, boolean completeMerge, File wfFile) throws Exception {
		if (backUp) {
			super.backUp();
		}
		Set<File> files = this.getFiles(wfFile);
		setUnmanagedTask(this.getGlobalFMTask(UNMANAGED_PARENT_NAME));
		for (File file : files) {
			System.out.println("\nFile " + file.getAbsolutePath());
			Pair<String, Document> wfInfo = this.getWFDocInfoFromFile(file);
			if (wfInfo.isEmpty()) {
				// TODO: add logs
				return;
			}
			Document wfDocument = wfInfo.getValue();
			List<Node> tasksNodes = getTasksList(wfDocument, BPMNNames.SELECTOR);
			for (ListIterator<Node> iter = tasksNodes.listIterator(); iter.hasNext();) {
				for (Task task : this.getTaskFactory().createTasks(iter.next())) {
					this.processTask((BPMNTask) task);
				}
				iter.remove(); // removing from list to free memory
			}
			this.processAnnotations(wfDocument);
			if (completeMerge) {
				this.processCompleteMerge(wfInfo);
				this.processSpecificNeeds(wfInfo);
			}
		}
		System.out.println("EOF");
	}

	private Set<File> getFiles(File file) throws IOException {
		Set<File> files;
		try (Stream<Path> stream = Files.walk(file.toPath())) {
			// TODO: factorize endsWith filter in a dedicated method (add extension in
			// notation and use the apache-io api
			files = stream.parallel().map(Path::toFile).filter(File::isFile)
					.filter(p -> p.getName().endsWith(".bpmn") || p.getName().endsWith(".bpmn2"))
					.collect(Collectors.toSet());
		}
		if (files.isEmpty()) {
			// wfFile is a regular file (not a directory)
			files.add(file);
		}
		return files;
	}

	@Override
	public void mergeWithWF(boolean backUp, boolean completeMerge, File... wfFiles) throws Exception {
		for (File wfFile : wfFiles) {
			this.mergeWithWF(backUp, completeMerge, wfFile);
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
	private void processCompleteMerge(Pair<String, Document> wfInfo) throws InvalidConstraintException {
		String wfName = wfInfo.getKey();
		// TODO: check order execution (node creation before assocConstraints)
		this.processAssocConstraints(wfInfo.getValue(), wfName);
		this.createdWFTask = this.createFeatureWithName(wfName);
		FMTask root = this.getRootParentNode();
		this.createdWFTask = this.insertNewTask(root, this.createdWFTask);
	}

	protected void processTask(BPMNTask task) {
		String taskName = task.getName();
		Optional<FMTask> opt;
		if (TasksManager.exists(taskName)) {
			// if task is already in the FM
			opt = unmanagedTask.getChildWithName(taskName);
			if (opt.isEmpty()) {
				// if it is not under the unmanaged node
				return;
			}
			opt = unmanagedTask.removeChild(opt.get());
			if (opt.isEmpty()) {
				return; // TODO: throw error
			}
			Task duplicatedTask = opt.get();
			// task = this.mergeNodes(task, duplicatedTask); // TODO: to modify
		}
		// retrieving a suitable parent
		FMTask parentTask = this.getSuitableParent(task);
		// inserting the new task
		this.insertNewTask(parentTask, task);

	}

	protected Task mergeNodes(Task taskA, Task taskB) {
		// TODO: to change according to recent changes in task OOC
		// TODO: improve considering conflicts (e.g same child & different levels)
		/*-NodeList nodeBChildren = nodeB.getChildNodes();
		for (int i = 0; i < nodeBChildren.getLength(); i++) {
			nodeA.appendChild(nodeBChildren.item(i));
		}
		return nodeA;*/
		return null;
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
		System.out.println("LOOKED FOR : -" + globalNodeName + "-");
		TasksManager.getFMTasks().forEach(t -> System.out.println(t + " -" + t.getName() + "-"));
		System.out.println("Result : " + optGlobalTask);
		return optGlobalTask.orElseGet(() -> this.createGlobalFMTask(globalNodeName));
	}

	protected FMTask createGlobalFMTask(String globalNodeName) {
		Element globalElement = getDocument().createElement(FMNames.AND.getName());
		globalElement.setAttribute(FMAttributes.ABSTRACT.getName(), String.valueOf(true));
		globalElement.setAttribute(FMAttributes.NAME.getName(), globalNodeName);
		Node globalNode = getDocument().getElementsByTagName(FMNames.AND.getName()).item(1)
				.appendChild(globalElement); // TODO: use TasksManager
		return (FMTask) this.getTaskFactory().createTasks(globalNode).stream().findFirst().orElse(null);
	}

}
