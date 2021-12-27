package com.obss.file.db.controller;

import com.obss.file.db.domain.DbObject;
import com.obss.file.db.repository.DbRepository;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URL;
import java.util.Comparator;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.*;

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
        return ok(dbRepository.getDbObjects()
                .stream()
                .sorted(Comparator.comparing(DbObject::getModified))
                .collect(Collectors.toList()));
    }

    @GetMapping(value = "/id/{id}")
    public ResponseEntity getObject(@PathVariable("id") String id) {
        return ok(dbRepository.getObjectById(id));
    }

    @GetMapping(value = "/value/{value}")
    public ResponseEntity searchObjectByValue(@PathVariable("value") String value,
                                              @RequestParam(value = "from", required = false) Long fromMillis,
                                              @RequestParam(value = "to", required = false) Long toMillis) {
        return ok(dbRepository.getObjectsByValue(value, fromMillis, toMillis)
                .stream()
                .sorted(Comparator.comparing(DbObject::getModified))
                .collect(Collectors.toList()));
    }

    @SneakyThrows
    @PostMapping(value = "/")
    public ResponseEntity addObject(@RequestBody DbObject dbObject) {
        dbRepository.saveObject(dbObject);
        return created(new URL("http://localhost:9393/id/" + dbObject.getId()).toURI()).build();
    }

    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity deleteObject(@PathVariable("id") String id) {
        dbRepository.deleteObject(dbRepository.getObjectById(id));
        return noContent().build();
    }

    @PutMapping(value = "/id/{id}")
    public ResponseEntity updateObject(@PathVariable("id") String id, @RequestBody DbObject dbObject) {

        if (!id.equals(dbObject.getId())) return ResponseEntity.badRequest().build();

        dbRepository.saveObject(dbObject);
        return noContent().build();
    }
}
