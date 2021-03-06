package com.obss.file.db.repository;

import com.obss.file.db.domain.DbObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.inject.Named;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Named
@ConfigurationProperties("db")
public class DbRepository extends FileRepository<DbObject> {

    @Autowired
    public DbRepository(@Value("${server.port}") int port, @Value("${db.path}") String dbPath) {
        super(cleanPath(dbPath) + "/" + port + ".db");
    }

    private static String cleanPath(String s) {
        if (s == null) return "data";
        if (s.substring(0, 1).equals("/")) s = s.substring(1);
        if (s.substring(s.length()-1).equals("/")) s = s.substring(0, s.length()-1);
        File f = new File("../"+s);
        if (!f.exists()) f.mkdirs();
        return "../"+s;
    }

    public List<DbObject> getDbObjects() {
        List<DbObject> objects = new ArrayList<>();
        this.getObjects().forEach((aLong, dbObject) -> objects.add(dbObject));
        return objects;
    }

    public DbObject getObjectById(String id) {
        for (DbObject dbObject : getObjects().values()) {
            if (dbObject.getId().equals(id)) return dbObject;
        }
        return null;
    }

    public void saveObject(DbObject dbObject) {
        System.out.println("saving object " + generateId(dbObject) + " - " + dbObject.getId() + ", " + dbObject.getModified() + ", " + dbObject.getValue());
        this.getObjects().put(generateId(dbObject), dbObject);
        save();
    }

    public void deleteObject(DbObject dbObject) {
        this.getObjects().remove(generateId(dbObject));
        save();
    }

    @Override
    String objectToString(DbObject dbObject) {
        return dbObject.getId() + DB_DELIMITER + dbObject.getModified() + DB_DELIMITER + dbObject.getValue();
    }

    @Override
    Long generateId(DbObject o) {
        return (long) (o.getId() + o.getModified() + o.getValue()).hashCode();
    }

    @Override
    DbObject createObjectInstance(String[] values) {
        return new DbObject(values[0], values[1], values[2]);
    }
}
