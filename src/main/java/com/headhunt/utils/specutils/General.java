package com.headhunt.utils.specutils;

import com.headhunt.utils.commonutils.urlutils.UtilitiesURL;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.TreeSet;

/**
 *
 * @author sagraw200
 */
public class General {
    
    public static void pr(Object o) {
        System.out.println(o);
    }

    public static String setToBuilder(TreeSet<String> set) {
        StringBuilder builder = new StringBuilder();
        for (String s : set) {
            builder.append(s).append("\n");
        }
        return builder.toString();
    } 
    
    
    public static TreeSet<String> getOneChar(char[] alphabet, String baseurl, UtilitiesURL uUrl) {
        TreeSet<String> tmp = new TreeSet<>();
        
        for (int i = 0 ; i < alphabet.length; ++i) {
            String uri = baseurl + alphabet[i];
            pr(uri);
            String res = uUrl.readStringFromUrl(uri);
            if (res != null && !res.isEmpty()) {
                try {
                    JSONObject json = new JSONObject(res);
                    if (json.has("resultList")) {
                        JSONArray jArr = json.getJSONArray("resultList");
                        if (jArr != null && jArr.length() > 0) {
                            for (int x = 0 ; x < jArr.length(); ++x) {
                                JSONObject item = jArr.getJSONObject(x);
                                if (item.has("displayName") && item.get("displayName") != null) {
                                    tmp.add(item.getString("displayName").toLowerCase());
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Error in parsing url json: " + ex);
                }
            }
        }
        return tmp;
    }
    
    public static TreeSet<String> getTwoChar(char[] alphabet, String baseurl, UtilitiesURL uUrl) {
        TreeSet<String> tmp = new TreeSet<>();
        
        for (int i = 0 ; i < alphabet.length; ++i) {
            for (int j = 0 ; j < alphabet.length; ++j) {
                String uri = baseurl + alphabet[i] + alphabet[j];
                pr(uri);
                String res = uUrl.readStringFromUrl(uri);
                if (res != null && !res.isEmpty()) {
                    try {
                        JSONObject json = new JSONObject(res);
                        if (json.has("resultList")) {
                            JSONArray jArr = json.getJSONArray("resultList");
                            if (jArr != null && jArr.length() > 0) {
                                for (int x = 0 ; x < jArr.length(); ++x) {
                                    JSONObject item = jArr.getJSONObject(x);
                                    if (item.has("displayName") && item.get("displayName") != null) {
                                        tmp.add(item.getString("displayName").toLowerCase());
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println("Error in parsing url json: " + ex);
                    }
                }
            }
        }
        return tmp;
    }
    
    public static TreeSet<String> getThreeChar(char[] alphabet, String baseurl, UtilitiesURL uUrl) {
        TreeSet<String> tmp = new TreeSet<>();
        
        for (int i = 0 ; i < alphabet.length; ++i) {
            for (int j = 0 ; j < alphabet.length; ++j) {
                for (int k = 0 ; k < alphabet.length; ++k) {
                    String uri = baseurl + alphabet[i] + alphabet[j] + alphabet[k];
                    pr(uri);
                    String res = uUrl.readStringFromUrl(uri);
                    if (res != null && !res.isEmpty()) {
                        try {
                            JSONObject json = new JSONObject(res);
                            if (json.has("resultList")) {
                                JSONArray jArr = json.getJSONArray("resultList");
                                if (jArr != null && jArr.length() > 0) {
                                    for (int x = 0 ; x < jArr.length(); ++x) {
                                        JSONObject item = jArr.getJSONObject(x);
                                        if (item.has("displayName") && item.get("displayName") != null) {
                                            tmp.add(item.getString("displayName").toLowerCase());
                                        }
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            System.out.println("Error in parsing url json: " + ex);
                        }
                    }
                }
            }
        }
        return tmp;
    }
    
    public static TreeSet<String> getFourChar(char[] alphabet, String baseurl, UtilitiesURL uUrl) {
        TreeSet<String> tmp = new TreeSet<>();
        
        for (int i = 0 ; i < alphabet.length; ++i) {
            for (int j = 0 ; j < alphabet.length; ++j) {
                for (int k = 0 ; k < alphabet.length; ++k) {
                    for (int y = 0 ; y < alphabet.length; ++y) {
                        String uri = baseurl + alphabet[i] + alphabet[j] + alphabet[k] + alphabet[y];
                        pr(uri);
                        String res = uUrl.readStringFromUrl(uri);
                        if (res != null && !res.isEmpty()) {
                            try {
                                JSONObject json = new JSONObject(res);
                                if (json.has("resultList")) {
                                    JSONArray jArr = json.getJSONArray("resultList");
                                    if (jArr != null && jArr.length() > 0) {
                                        for (int x = 0 ; x < jArr.length(); ++x) {
                                            JSONObject item = jArr.getJSONObject(x);
                                            if (item.has("displayName") && item.get("displayName") != null) {
                                                tmp.add(item.getString("displayName").toLowerCase());
                                            }
                                        }
                                    }
                                }
                            } catch (Exception ex) {
                                System.out.println("Error in parsing url json: " + ex);
                            }
                        }
                    }
                }
            }
        }
        return tmp;
    }
    
    
}
