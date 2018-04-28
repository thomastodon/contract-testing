package thomastodon.io.sinkapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.http.MediaType.parseMediaType;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
class EggControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {

        EggController eggController = new EggController();

        mockMvc = MockMvcBuilders
            .standaloneSetup(eggController)
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    @Test
    @DisplayName("count the eggs")
    void test() throws Exception {

        mockMvc.perform(get("/eggs/count"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(parseMediaType("application/json;charset=UTF-8")))
            .andExpect(content().json("{\"count\": 4}"))
            .andDo(document("index"));
    }
}