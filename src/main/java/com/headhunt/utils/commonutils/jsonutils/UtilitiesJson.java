package com.headhunt.utils.commonutils.jsonutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.headhunt.utils.commonutils.fileutils.UtilitiesFile;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sagraw001c on 11/13/14.
 */
public class UtilitiesJson {

    public static JSONObject readFileFromJson(String file) {
        UtilitiesFile uf = new UtilitiesFile();
        String fileS = uf.readFileInString(file);
        JSONObject json = null;
        try {
            json = new JSONObject(fileS);
        } catch (Exception ex) {
            System.out.println("Unable to read file to jsonobject: " + ex);
        }
        return json;
    }

    public static JSONObject stringToJSON(String text) {
        JSONObject result = null;
        try {
            if (text != null && !text.isEmpty()) {
                result = new JSONObject(text);
            }
        } catch (Exception ex) {
            System.out.println("Error in parsing string to json: " + ex);
            return null;
        }
        return result;
    }

    public static Map<String,Object> changeJsonToMap(JSONObject json) {
        Map<String,Object> map = null;
        try {
            map = new ObjectMapper().readValue(String.valueOf(json), HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static List<JSONObject> getJSONList(JSONObject json, String key) {
        try {
            List<JSONObject> jsonList = null;
            if (json.getJSONArray(key) != null) {
                JSONArray arr = json.getJSONArray(key);
                if (arr.length() > 0) {
                    jsonList = new ArrayList<>();
                    for (int i = 0; i < arr.length(); ++i) {
                        jsonList.add(arr.getJSONObject(i));
                    }
                }
            }
            return jsonList;
        } catch (Exception ex) {
            System.out.println("Error in getJSONList(): " + ex);
        }
        return null;
    }

    public static String prettyPrint(JSONObject json) {

        return null;
    }

}
