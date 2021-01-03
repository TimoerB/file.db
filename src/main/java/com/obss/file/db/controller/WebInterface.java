package com.obss.file.db.controller;

import com.obss.file.db.domain.DbObject;
import com.obss.file.db.repository.DbRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class WebInterface {

    @Inject
    private DbRepository dbRepository;

    @GetMapping(value = "/")
    public List<DbObject> getObjects() {
        return dbRepository.getDbObjects();
    }

    @GetMapping(value = "/id/{id}")
    public DbObject getObject(@PathVariable("id") String id) {
        return dbRepository.getObjectById(id);
    }

    @PostMapping(value = "/")
    public ResponseEntity addObject(@RequestBody DbObject dbObject) {
        dbRepository.saveObject(dbObject);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity deleteObject(@PathVariable("id") String id) {
        dbRepository.deleteObject(dbRepository.getObjectById(id));
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/id/{id}")
    public ResponseEntity updateObject(@PathVariable("id") String id, @RequestBody DbObject dbObject) {

        if (!id.equals(dbObject.getId())) return ResponseEntity.badRequest().build();

        dbRepository.saveObject(dbObject);
        return ResponseEntity.ok().build();
    }
}
