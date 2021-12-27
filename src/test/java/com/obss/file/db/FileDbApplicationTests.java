package com.obss.file.db;

import com.obss.file.db.repository.DbRepository;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.requestSpecification;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@SpringBootTest(classes = FileDbApplication.class, webEnvironment = RANDOM_PORT)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs
public abstract class FileDbApplicationTests {

    @LocalServerPort
    protected int localPort;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private DbRepository dbRepository;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        requestSpecification = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
        RestAssured.port = localPort;

        dbRepository.deleteAllObjects();
    }

    @AfterEach
    public void afterEach() {
        requestSpecification = null;
    }
}
