package com.ml2wf;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ml2wf.generation.InstanceFactoryImpl;
import com.ml2wf.merge.AbstractMerger;
import com.ml2wf.merge.base.BaseMergerImpl;
import com.ml2wf.util.FileHandler;
import com.ml2wf.util.XMLManager;

/**
 * This class contains all required field and method to efficiently test
 * instances of the {@code XMLManager} class.
 *
 * @author Nicolas Lacroix
 *
 * @since 1.0.0
 *
 * @see XMLManager
 */
public abstract class AbstractXMLTest {

	/**
	 * Instance of the tested class.
	 *
	 * @see XMLManager
	 */
	protected XMLManager testedClass;
	/**
	 * XML source document.
	 *
	 * @see Document
	 */
	protected Document sourceDocument;
	/**
	 * XML result document.
	 *
	 * @see Document
	 */
	protected Document resultDocument;
	/**
	 * {@code ClassLoader}'s instance used to get resources.
	 *
	 * @see ClassLoader
	 */
	protected static ClassLoader classLoader = AbstractXMLTest.class.getClassLoader();
	/**
	 * Meta workflows' directory.
	 */
	protected static final String META_DIRECTORY = "./wf_meta/";
	/**
	 * Instance workflows' directory.
	 */
	protected static final String INSTANCES_DIRECTORY = "./wf_instances/";

	/**
	 * Returns a {@code Stream} containing all meta-workflows located under the
	 * {@link #META_DIRECTORY} directory.
	 *
	 * @return a {@code Stream} containing all meta-workflows located under the
	 *         {@link #META_DIRECTORY} directory
	 *
	 * @throws URISyntaxException
	 *
	 * @see FileUtils
	 */
	protected static Stream<File> metaFiles() throws URISyntaxException {
		File instanceDir = new File(
				Objects.requireNonNull(classLoader.getResource(META_DIRECTORY)).toURI());
		return new HashSet<>(FileUtils.listFiles(instanceDir, FileHandler.getWfExtensions(), true)).stream();
	}

	/**
	 * Returns a {@code Stream} containing all instance-workflows located under the
	 * {@link #INSTANCES_DIRECTORY} directory.
	 *
	 * @return a {@code Stream} containing all instance-workflows located under the
	 *         {@link #INSTANCES_DIRECTORY} directory
	 *
	 * @throws URISyntaxException
	 *
	 * @see FileUtils
	 */
	protected static Stream<File> instanceFiles() throws URISyntaxException {
		File instanceDir = new File(Objects.requireNonNull(classLoader.getResource(INSTANCES_DIRECTORY)).toURI());
		return new HashSet<>(FileUtils.listFiles(instanceDir, FileHandler.getWfExtensions(), true)).stream();
	}

	/**
	 * Retruns all references for the given {@code nodes}.
	 *
	 * @param nodes nodes to retrieve references
	 *
	 * @return all references for the given {@code nodes}
	 *
	 * @see Node
	 */
	protected static List<String> getReferences(List<Node> nodes) {
		return nodes.stream().map(Element.class::cast)
				.map(XMLManager::getAllBPMNDocContent)
				.map(XMLManager::getReferredTask)
				.map(r -> r.orElse(BaseMergerImpl.UNMANAGED))
				.collect(Collectors.toList());
	}

	/**
	 * Returns all names for the given {@code nodes}.
	 *
	 * @param nodes          nodes to retrieve names
	 * @param tagName        the tag name used to retrieve the nodes' names
	 * @param considerNested whether the result should contains the flatten nested
	 *                       nodes or not
	 * @return all names for the given {@code nodes}
	 *
	 * @see Node
	 */
	protected static List<String> getNames(List<Node> nodes, String tagName, boolean considerNested) {
		Stream<Node> nodesStream = (considerNested)
				? nodes.stream().flatMap(n -> AbstractMerger.getNestedNodes(n).stream())
				: nodes.stream();
		Stream<String> namesStream = nodesStream.map(Node::getAttributes)
				.map(a -> a.getNamedItem(tagName)).map(Node::getNodeValue);
		if (!considerNested) {
			namesStream = namesStream.map(InstanceFactoryImpl::getLastReference);
		}
		return namesStream.map(XMLManager::sanitizeName).collect(Collectors.toList());
	}
}
