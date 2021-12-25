# FileDb

FileDb is a simple database based on writing to files. It is lightweight and easy to set up and is a perfect fit for deer (https://deercore.org). The default port for FileDb is `9393` but can be changed in the configuration of course. 

A database object consists of only three basic fields: `id, modified, value`. Please read further to see how to access these objects/fields.

## Using deer

It can be set up by installing deer (https://deercore.org):

```
wget -q "https://deer.obss.be/install.tar.gz" -O deer.tar.gz
tar -xf deer.tar.gz
chmod +x run.sh
sudo bash run.sh
rm run.sh
```
 and running: `deer file-db`
 
## Manually the hard way

To be hardcore, build and run the jar file:

Build with skipped tests:
```$xslt
mvn clean install -DskipTests=true
```

Run jar:
```$xslt
java -jar *.jar
```

## Connect to the database

REST calls are used to communicate with fileDb.

### Get all objects

Simply calling GET to `/` will return a json list of all the objects.

### Get one object

Issue a GET call to `/id/{objectId}` will return one object only.

### Add an object

POST to `/` with the object in json format will suffice.

### Update object

PUT to `/id/{objectId}` with the json object as body will update the specified object.

### Delete object

Likewise to delete an object, simply issue DELETE to `/id/{objectId}` and the object will be no more.
