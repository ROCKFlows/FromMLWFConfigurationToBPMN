package com.ml2wf.app.controllers

import com.ml2wf.contract.business.IVersionsComponent
import com.ml2wf.core.tree.StandardKnowledgeVersion
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Ignore
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
@ActiveProfiles("neo4j")
class FeatureModelControllerTest extends Specification {

    @Autowired
    private MockMvc mvc
    @Autowired
    IVersionsComponent versionsComponent;

    @Ignore
    def "[MOCK] when get all versions then the response has status 200"() {
        given:
        versionsComponent.versions >> [new StandardKnowledgeVersion(0, 0, 0, "mock")]

        expect: "Status is 200"
        mvc.perform(get("/versions/all"))
            .andExpect(status().isOk())
            .andExpect(content().string('"[StandardKnowledgeVersion()]"'))
    }
}
