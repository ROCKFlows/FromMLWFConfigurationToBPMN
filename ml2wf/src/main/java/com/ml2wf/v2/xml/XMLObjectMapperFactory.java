package com.ml2wf.v2.xml;

import com.ctc.wstx.stax.WstxInputFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.ml2wf.v2.constraints.operands.impl.VariableOperand;
import com.ml2wf.v2.constraints.operators.AbstractOperator;
import com.ml2wf.v2.tree.fm.FeatureModel;
import com.ml2wf.v2.tree.fm.FeatureModelRule;
import com.ml2wf.v2.tree.fm.FeatureModelStructure;
import com.ml2wf.v2.tree.fm.FeatureModelTask;
import com.ml2wf.v2.tree.wf.Process;
import com.ml2wf.v2.tree.wf.Workflow;
import com.ml2wf.v2.tree.wf.WorkflowTask;
import com.ml2wf.v2.xml.constraints.operands.mixins.VariableOperandMixin;
import com.ml2wf.v2.xml.constraints.operands.serializer.VariableOperandSerializer;
import com.ml2wf.v2.xml.constraints.operators.mixins.AbstractOperatorMixin;
import com.ml2wf.v2.xml.constraints.operators.serializer.AbstractOperatorSerializer;
import com.ml2wf.v2.xml.fm.mixins.FMMixin;
import com.ml2wf.v2.xml.fm.mixins.FMRuleMixin;
import com.ml2wf.v2.xml.fm.mixins.FMStructureMixin;
import com.ml2wf.v2.xml.fm.mixins.FMTaskMixin;
import com.ml2wf.v2.xml.fm.serializers.FeatureModelRuleSerializer;
import com.ml2wf.v2.xml.fm.serializers.FeatureModelTaskSerializer;
import com.ml2wf.v2.xml.wf.mixins.*;

import javax.xml.stream.XMLInputFactory;

public class XMLObjectMapperFactory {

    private static XMLObjectMapperFactory INSTANCE;
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

    public static XMLObjectMapperFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new XMLObjectMapperFactory();
        }
        return INSTANCE;
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
                .addMixIn(Workflow.class, WFMixin.class)
                .addMixIn(WorkflowTask.class, WFTaskMixin.class)
                .addMixIn(WorkflowTask.Documentation.class, WFDocumentationMixin.class)
                .addMixIn(Process.class, ProcessMixin.class)
                .addMixIn(Process.SequenceFlow.class, ProcessSequenceFlowMixin.class)
                // Operands related mixins
                .addMixIn(AbstractOperator.class, AbstractOperatorMixin.class)
                .addMixIn(VariableOperand.class, VariableOperandMixin.class);
    }
}
