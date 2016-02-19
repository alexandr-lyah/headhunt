package com.headhunt.utils.commonutils.dbutils;

import com.mongodb.*;
import com.mongodb.util.JSON;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sagraw001c on 11/13/14.
 */

public class UtilitiesMongo {

    private static MongoClient _mongoClient;
    private static MongoClientOptions _mongoClientOptions;
    private Map<Integer, DBCollection> map = new HashMap<>();
    private int identifier = 0;

    private static void mongoClientOptions() {
        _mongoClientOptions = MongoClientOptions.builder()
                .autoConnectRetry(true)
                .connectTimeout(1000)
                .build();
    }

    public UtilitiesMongo(String host, int port) {
        mongoClientOptions();
        try {
            List<ServerAddress> serverAddressList = new ArrayList<ServerAddress>();
            ServerAddress serverAddress = null;
            serverAddress = new ServerAddress( host, port );
            serverAddressList.add(serverAddress);
            _mongoClient = new MongoClient( serverAddressList, _mongoClientOptions );
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public int initDB(String _db, String _coll) {
        identifier++;
        DB db = _mongoClient.getDB(_db);
        DBCollection coll = db.getCollection(_coll);
        map.put(identifier, coll);
        return identifier;
    }

    public boolean insert(int id, List<JSONObject> jsonList) {
        boolean result = false;
        try {
            List<DBObject> dbObjs = new ArrayList<>();
            for (JSONObject json : jsonList) {
                dbObjs.add((DBObject) JSON.parse(json.toString()));
            }
            DBCollection coll = map.get(id);
            if (coll != null) {
                coll.insert(dbObjs);
                result = true;
            }
        } catch (Exception ex) {
            System.out.println("Exception caused in inserting rows to Mongo: " + ex);
        }
        return result;
    }

    public boolean insert(int id, JSONObject json) {
        boolean result = false;
        try {
            DBObject dbObj = new BasicDBObject();
            dbObj = (DBObject) JSON.parse(json.toString());
            DBCollection coll = map.get(id);
            if (coll != null) {
                coll.insert(dbObj);
                result = true;
            }
        } catch (Exception ex) {
            System.out.println("Exception caused in inserting rows to Mongo: " + ex);
        }
        return result;
    }

    public boolean insert(int id, BasicDBObject doc) {
        boolean result = false;
        try {
            DBCollection coll = map.get(id);
            if (coll != null) {
                coll.insert(doc);
                result = true;
            }
        } catch (Exception ex) {
            System.out.println("Exception caused in inserting rows to Mongo: " + ex);
        }
        return result;
    }


    public List<DBObject> find(int id, BasicDBObject query, BasicDBObject... fields) {
        List<DBObject> result = null;
        try {
            DBCollection coll = map.get(id);
            if (coll != null) {
                if ( fields != null && fields.length > 0 ) {
                    result = coll.find(query, fields[0]).toArray();
                } else {
                    result = coll.find(query).toArray();
                }
            } else {
                System.out.println("coll object is null..");
            }
        } catch (Exception ex) {
            System.out.println("Exception caused in find query in Mongo: " + ex);
        }
        return result;
    }



}
