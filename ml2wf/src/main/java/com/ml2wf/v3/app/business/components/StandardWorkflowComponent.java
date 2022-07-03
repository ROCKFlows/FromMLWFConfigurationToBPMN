package com.ml2wf.v3.app.business.components;

//@Component
public class StandardWorkflowComponent {

    private static final String ROOT_CONSTRAINT_NODE_NAME = "__ROOT_CONSTRAINT";

    /*private final ArangoStandardKnowledgeComponent standardKnowledgeComponent;
    private final ConstraintsRepository constraintsRepository;
    private final IArangoConstraintsConverter constraintsConverter;

    public StandardWorkflowComponent(@Autowired ArangoStandardKnowledgeComponent standardKnowledgeComponent,
                                     @Autowired ConstraintsRepository constraintsRepository,
                                     @Autowired IArangoConstraintsConverter constraintsConverter) {
        this.standardKnowledgeComponent = standardKnowledgeComponent;
        this.constraintsRepository = constraintsRepository;
        this.constraintsConverter = constraintsConverter;
    }

    public boolean importStandardWorkflow(String versionName, StandardWorkflow standardWorkflow) {
        var standardKnowledgeTree = standardKnowledgeComponent.getStandardKnowledgeTree(versionName);
        // TODO: import + merge
        return true;
    }

    public boolean isStandardWorkflowConsistent(String versionName, StandardWorkflow standardWorkflow) {
        var operands = constraintsRepository.findAllByTypeEqualsAndVersion_Name(
                ROOT_CONSTRAINT_NODE_NAME, versionName).get(0).getOperands();
        return operands.stream()
                .map(constraintsConverter::toConstraintTree)
                .allMatch(c -> c.isWorkflowConsistent(standardWorkflow));
    }*/
}
