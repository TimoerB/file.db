package com.obss.file.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class CommandLineTaskExecutor implements CommandLineRunner {

    private final TaskService taskService;

    @Autowired
    public CommandLineTaskExecutor(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void run(String... args) throws Exception {
        taskService.execute("keep application running");
    }
}
