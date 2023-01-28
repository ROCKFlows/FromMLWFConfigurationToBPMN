package com.ml2wf.xml.factory;

import com.ctc.wstx.stax.WstxInputFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.ml2wf.contract.mapper.IObjectMapperFactory;
import com.ml2wf.core.configurations.RawConfiguration;
import com.ml2wf.core.configurations.ConfigurationFeature;
import com.ml2wf.core.constraints.operands.impl.VariableOperand;
import com.ml2wf.core.constraints.operators.AbstractOperator;
import com.ml2wf.core.tree.custom.featuremodel.FeatureModel;
import com.ml2wf.core.tree.custom.featuremodel.FeatureModelRule;
import com.ml2wf.core.tree.custom.featuremodel.FeatureModelStructure;
import com.ml2wf.core.tree.custom.featuremodel.FeatureModelTask;
import com.ml2wf.core.workflow.custom.bpmn.BPMNProcess;
import com.ml2wf.core.workflow.custom.bpmn.BPMNWorkflow;
import com.ml2wf.core.workflow.custom.bpmn.BPMNWorkflowTask;
import com.ml2wf.xml.mixins.configurations.ConfigurationFeatureMixin;
import com.ml2wf.xml.mixins.configurations.ConfigurationFeatureStatusMixin;
import com.ml2wf.xml.mixins.configurations.ConfigurationMixin;
import com.ml2wf.xml.mixins.constraints.operands.VariableOperandMixin;
import com.ml2wf.xml.mixins.constraints.operands.serializer.VariableOperandSerializer;
import com.ml2wf.xml.mixins.constraints.operators.AbstractOperatorMixin;
import com.ml2wf.xml.mixins.constraints.operators.serializer.AbstractOperatorSerializer;
import com.ml2wf.xml.mixins.tree.custom.featuremodel.FMMixin;
import com.ml2wf.xml.mixins.tree.custom.featuremodel.FMRuleMixin;
import com.ml2wf.xml.mixins.tree.custom.featuremodel.FMStructureMixin;
import com.ml2wf.xml.mixins.tree.custom.featuremodel.FMTaskMixin;
import com.ml2wf.xml.mixins.tree.custom.featuremodel.serializers.FeatureModelRuleSerializer;
import com.ml2wf.xml.mixins.tree.custom.featuremodel.serializers.FeatureModelTaskSerializer;
import com.ml2wf.xml.mixins.workflow.custom.bpmn.BPMNWFMixin;
import com.ml2wf.xml.mixins.workflow.custom.bpmn.BPMNWFDocumentationMixin;
import com.ml2wf.xml.mixins.workflow.custom.bpmn.BPMNWFTaskMixin;
import com.ml2wf.xml.mixins.workflow.custom.bpmn.BPMNProcessMixin;
import com.ml2wf.xml.mixins.workflow.custom.bpmn.BPMNProcessSequenceFlowMixin;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLInputFactory;

@Component
public class XMLObjectMapperFactory implements IObjectMapperFactory {

    private final XMLInputFactory xmlInputFactory;
    private final JacksonXmlModule xmlModule;

    private XMLObjectMapperFactory() {
        xmlInputFactory  = createXMLInputFactory();
        xmlModule = createXmlModule();
    }

    private XMLInputFactory createXMLInputFactory() {
        XMLInputFactory newXmlInputFactory = new WstxInputFactory();
        newXmlInputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.FALSE);
        return newXmlInputFactory;
    }

    private JacksonXmlModule createXmlModule() {
        JacksonXmlModule newXmlModule = new JacksonXmlModule();
        newXmlModule.addSerializer(FeatureModelTask.class, new FeatureModelTaskSerializer());
        newXmlModule.addSerializer(FeatureModelRule.class, new FeatureModelRuleSerializer());
        newXmlModule.addSerializer(AbstractOperator.class, new AbstractOperatorSerializer());
        newXmlModule.addSerializer(VariableOperand.class, new VariableOperandSerializer());
        return newXmlModule;
    }

    public ObjectMapper createNewObjectMapper() {
        return new XmlMapper(xmlInputFactory)
                .setDefaultMergeable(true)
                .setDefaultSetterInfo(JsonSetter.Value.forContentNulls(Nulls.AS_EMPTY))
                .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .registerModule(xmlModule) // TODO: check why not working
                // FeatureModel related mixins
                .addMixIn(FeatureModel.class, FMMixin.class)
                .addMixIn(FeatureModelStructure.class, FMStructureMixin.class)
                .addMixIn(FeatureModelTask.class, FMTaskMixin.class)
                // .addMixIn(FeatureModelTask.Description.class, FMTaskDescriptionMixin.class)
                .addMixIn(FeatureModelRule.class, FMRuleMixin.class)
                // Workflow related mixins
                .addMixIn(BPMNWorkflow.class, BPMNWFMixin.class)
                .addMixIn(BPMNWorkflowTask.class, BPMNWFTaskMixin.class)
                .addMixIn(BPMNWorkflowTask.Documentation.class, BPMNWFDocumentationMixin.class)
                .addMixIn(BPMNProcess.class, BPMNProcessMixin.class)
                .addMixIn(BPMNProcess.SequenceFlow.class, BPMNProcessSequenceFlowMixin.class)
                // Operands related mixins
                .addMixIn(AbstractOperator.class, AbstractOperatorMixin.class)
                .addMixIn(VariableOperand.class, VariableOperandMixin.class)
                // Configurations related mixins
                .addMixIn(RawConfiguration.class, ConfigurationMixin.class)
                .addMixIn(ConfigurationFeature.class, ConfigurationFeatureMixin.class)
                .addMixIn(ConfigurationFeature.Status.class, ConfigurationFeatureStatusMixin.class);
    }
}
