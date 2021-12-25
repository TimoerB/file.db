package com.obss.file.db.controller;

import com.obss.file.db.domain.DbObject;
import com.obss.file.db.repository.DbRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@CrossOrigin(origins = "*")
public class WebInterface {

    private final DbRepository dbRepository;

    @Inject
    public WebInterface(DbRepository dbRepository) {
        this.dbRepository = dbRepository;
    }

    @GetMapping(value = "/")
    public ResponseEntity getObjects() {
        return ok(dbRepository.getDbObjects());
    }

    @GetMapping(value = "/id/{id}")
    public ResponseEntity getObject(@PathVariable("id") String id) {
        return ok(dbRepository.getObjectById(id));
    }

    @GetMapping(value = "/value/{value}")
    public ResponseEntity searchObjectByValue(@PathVariable("value") String value) {
        return ok(dbRepository.getObjectsByValue(value));
    }

    @PostMapping(value = "/")
    public ResponseEntity addObject(@RequestBody DbObject dbObject) {
        dbRepository.saveObject(dbObject);
        return ok().build();
    }

    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity deleteObject(@PathVariable("id") String id) {
        dbRepository.deleteObject(dbRepository.getObjectById(id));
        return ok().build();
    }

    @PutMapping(value = "/id/{id}")
    public ResponseEntity updateObject(@PathVariable("id") String id, @RequestBody DbObject dbObject) {

        if (!id.equals(dbObject.getId())) return ResponseEntity.badRequest().build();

        dbRepository.saveObject(dbObject);
        return ok().build();
    }
}
