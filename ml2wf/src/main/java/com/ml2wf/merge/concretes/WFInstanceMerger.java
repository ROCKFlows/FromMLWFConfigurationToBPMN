package com.ml2wf.merge.concretes;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import lombok.extern.log4j.Log4j2;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.conventions.Notation;
import com.ml2wf.conventions.enums.fm.FMNames;
import com.ml2wf.merge.base.BaseMergerImpl;
import com.ml2wf.merge.exceptions.MergeException;
import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.tasks.concretes.FMTask;
import com.ml2wf.util.Pair;
import com.ml2wf.util.XMLManager;

/**
 * This class merges a given Workflow's instance into a FeatureModel xml file.
 *
 * <p>
 *
 * It is an extension of the {@link BaseMergerImpl} base class.
 *
 * @author Nicolas Lacroix
 *
 * @see BaseMergerImpl
 *
 * @since 1.0.0
 */
@Log4j2
public final class WFInstanceMerger extends BaseMergerImpl {

    /**
     * Instances default task tag name.
     */
    private static final String INSTANCES_TASK = "Instances";

    /**
     * {@code WFInstanceMerger}'s default constructor.
     *
     * @param file the XML {@code File}
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public WFInstanceMerger(File file) throws ParserConfigurationException, SAXException, IOException {
        super(file);
    }

    @Override
    public FMTask getSuitableParent(WFTask<?> task) throws MergeException, UnresolvedConflict {
        return getReferredFMTask(task, unmanagedGlobalTasks.get(UNMANAGED_TASKS));
    }

    @Override
    public FMTask getRootParentNode() throws MergeException, UnresolvedConflict {
        return getGlobalFMTask(INSTANCES_TASK);
    }

    @Override
    public void processSpecificNeeds(Pair<String, Document> wfInfo)
            throws InvalidConstraintException, UnresolvedConflict {
        Document wfDocument = wfInfo.getValue();
        log.debug("Specific need : meta reference.");
        String metaReference = getMetaReferenced(wfDocument);
        if (metaReference == null) {
            log.error("No referenced meta-workflow.");
            log.info("Make sure to use instance-Workflows using the generate command before merging/building.");
            log.error("Skipping...");
            return;
        }
        String associationConstraint = getConstraintFactory()
                .getAssociationConstraint(wfInfo.getKey(), Collections.singletonList(metaReference));
        adoptRules(getConstraintFactory().getRuleNodes(associationConstraint));
        addReferences(wfDocument);
    }

    /**
     * Returns the referenced metaworkflow's name.
     *
     * @param wfDocument document containing the reference
     *
     * @return the referenced metaworkflow's name
     */
    private static String getMetaReferenced(Document wfDocument) {
        List<String> docContent = XMLManager.getAllBPMNDocContent(wfDocument.getDocumentElement());
        return getReferredTask(docContent).orElse(UNMANAGED);
    }

    /**
     * Adds a description {@code Node} to the current {@code createdWFNode}
     * containing all references (meta-workflow, dataset, author/articleà
     *
     * @param wfDocument document containing the references
     */
    private void addReferences(Document wfDocument) {
        log.debug("Adding references...");
        // getting global annotation content
        Node globalAnnotation = getGlobalAnnotationNode(wfDocument);
        String references = globalAnnotation.getTextContent();
        // removing WF's name
        // and references delimiters
        // TODO: remove WF's name part
        references = references.replace(Notation.REFERENCES_DELIMITER_LEFT, "");
        references = references.replace(Notation.REFERENCES_DELIMITER_RIGHT, "");
        // getting/creating the createdWFNode description
        Node descNode = createTag(createdWFTask, FMNames.DESCRIPTION);
        // merging content with description
        mergeNodesTextContent(descNode, references);
    }
}
