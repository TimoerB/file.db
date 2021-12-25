package com.obss.file.db.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskService {

    public void execute(String task) {
        log.info("Executing " + task);
    }
}