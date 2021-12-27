package com.obss.file.db.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obss.file.db.FileDbApplicationTests;
import com.obss.file.db.domain.DbObject;
import com.obss.file.db.repository.DbRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import java.time.Instant;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

class WebInterface_RestDocsTests extends FileDbApplicationTests {

    @Autowired
    private DbRepository dbRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ResponseFieldsSnippet responseFieldsSnippet = relaxedResponseFields(
            fieldWithPath("id").description("The identifier of the database object."),
            fieldWithPath("modified").description("The timestamp when the object was modified."),
            fieldWithPath("value").description("The actual data/value of the object.")
    );

    private RequestFieldsSnippet requestFieldsSnippet = relaxedRequestFields(
            fieldWithPath("id").description("The identifier of the database object."),
            fieldWithPath("modified").description("The timestamp when the object was modified."),
            fieldWithPath("value").description("The actual data/value of the object.")
    );

    private ResponseFieldsSnippet responseFieldsSnippetList = relaxedResponseFields(
            fieldWithPath("[].id").description("The identifier of the database object."),
            fieldWithPath("[].modified").description("The timestamp when the object was modified."),
            fieldWithPath("[].value").description("The actual data/value of the object.")
    );

    @Test
    void givenDbObject_getAll() {
        dbRepository.saveObject(new DbObject("s7PNsRlexy", Instant.now().toEpochMilli(), "Some data in here"));
        dbRepository.saveObject(new DbObject("gwXAnR2oK2", Instant.now().toEpochMilli(), "Another piece of data"));

        given()
                .when()
                .filter(document("getall",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFieldsSnippetList
                ))
                .get("/")
                .then()
                .statusCode(200);
    }

    @Test
    void givenDbObject_getById() {
        String id = "gwXAnR23v7";
        dbRepository.saveObject(new DbObject(id, Instant.now().toEpochMilli(), "More data"));

        given()
                .when()
                .filter(document("getbyid",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFieldsSnippet,
                        pathParameters(
                                parameterWithName("id").description("The id of the object.")
                        )
                ))
                .get("http://localhost:" + localPort + "/id/{id}", id)
                .then()
                .statusCode(200);
    }

    @SneakyThrows
    @Test
    void givenDbObject_add() {
        String id = "gwXAnR23v8";
        DbObject dbObject = new DbObject(id, Instant.now().toEpochMilli(), "Add new data");

        given()
                .when()
                .contentType(JSON)
                .filter(document("post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFieldsSnippet
                ))
                .body(objectMapper.writeValueAsString(dbObject))
                .post("http://localhost:" + localPort + "/")
                .then()
                .statusCode(201);
    }

    @Test
    void givenDbObject_delById() {
        String id = "gwXAnR11v7";
        dbRepository.saveObject(new DbObject(id, Instant.now().toEpochMilli(), "Data to be deleted"));

        given()
                .when()
                .filter(document("del",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("The id of the object.")
                        )
                ))
                .delete("http://localhost:" + localPort + "/id/{id}", id)
                .then()
                .statusCode(204);
    }

    @SneakyThrows
    @Test
    void givenDbObject_putById() {
        String id = "drXAnR11v7";
        DbObject dbObject = new DbObject(id, Instant.now().toEpochMilli(), "Data to be updated");
        dbRepository.saveObject(dbObject);

        dbObject.setValue("Different data");

        given()
                .when()
                .contentType(JSON)
                .filter(document("put",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFieldsSnippet,
                        pathParameters(
                                parameterWithName("id").description("The id of the object.")
                        )
                ))
                .body(objectMapper.writeValueAsString(dbObject))
                .put("http://localhost:" + localPort + "/id/{id}", id)
                .then()
                .statusCode(204);
    }

    @Test
    void givenDbObject_getByValue() {
        String id = "sqXAnR23v1";
        dbRepository.saveObject(new DbObject(id, Instant.now().toEpochMilli(), "Data with value 1"));
        dbRepository.saveObject(new DbObject("B9XAnZ800A", Instant.now().toEpochMilli(), "More data with different value"));

        given()
                .when()
                .filter(document("getbyval",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFieldsSnippetList,
                        pathParameters(
                                parameterWithName("value").description("Part of the value of the object (case-insensitive).")
                        ),
                        requestParameters(
                                parameterWithName("from").description("The timestamp of data objects should be larger than this timestamp."),
                                parameterWithName("to").description("The timestamp of data objects should be smaller than this timestamp.")
                        )
                ))
                .queryParam("from", Instant.now().minusSeconds(10).toEpochMilli())
                .queryParam("to", Instant.now().plusSeconds(3).toEpochMilli())
                .get("http://localhost:" + localPort + "/value/{value}", "data with")
                .then()
                .statusCode(200);
    }
}
