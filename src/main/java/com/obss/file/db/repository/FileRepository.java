package com.obss.file.db.repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public abstract class FileRepository<T> {

    private Log log = LogFactory.getLog(FileRepository.class);

    private String DATA_FILE = "filedata";
    static final String DB_DELIMITER = ":::";
    private HashMap<Long, T> objects = new HashMap<>();
    private static final long UPDATE_TIME = 15 * 1000;
    private long lastUpdated = new Date().getTime();

    public FileRepository() {
        load(true);
    }

    public FileRepository(String dataFile) {
        DATA_FILE = dataFile;
        load(true);
    }

    HashMap<Long, T> getObjects() {
        return objects;
    }

    public void setObjects(HashMap<Long, T> objects) {
        this.objects = objects;
    }

    boolean save() {
        try {
            FileWriter myWriter = new FileWriter(DATA_FILE);
            objects.forEach((id, o) -> {
                try {
                    myWriter.write(objectToString(o) + "\n");
                } catch (IOException e) {
                    log.error("An error occurred when writing to prop file: " + e.getMessage());
                }
            });
            if (objects.size() == 0)
                myWriter.write("");
            myWriter.close();
            log.info("Saved " + objects.size() + " objects to " + DATA_FILE);
        } catch (IOException e) {
            log.error("An error occurred when opening data file " + DATA_FILE + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    abstract String objectToString(T t);

    public void load() {
        load(false);
    }

    private void load(boolean force) {
        if (!force) {
            long nowTime = new Date().getTime();
            if (nowTime - lastUpdated < UPDATE_TIME) return;
            lastUpdated = nowTime;
        }
        objects.clear();
        try {
            File myObj = new File(DATA_FILE);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] d;
                if (data.contains(DB_DELIMITER)) d = data.split(DB_DELIMITER);
                else d = new String[]{data};
                T o = createObjectInstance(d);
                objects.put(generateId(o), o);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            log.error("An error occurred when reading data file: " + e.getMessage());
        }
    }

    abstract Long generateId(T o);

    abstract T createObjectInstance(String[] values);
}
