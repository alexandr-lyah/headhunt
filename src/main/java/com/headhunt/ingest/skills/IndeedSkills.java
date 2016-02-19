package com.headhunt.ingest.skills;

import com.headhunt.utils.commonutils.urlutils.UtilitiesURL;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.TreeSet;

/**
 *
 * @author sagraw200
 */
public class IndeedSkills {
    
    private static final String baseurl = "https://my.indeed.com/resume/auto/skill?q=";
    private static final String outputFile = "src/main/resources/data/ingest/indeed.skill.tmp";
    private static final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    
    private static UtilitiesURL uUrl;
    
    public IndeedSkills() {
        uUrl = new UtilitiesURL();
    }
    
    /**
     * 
     * @param nums 
     */
    public void getIndeedSkills(List<Integer> nums) {
        TreeSet<String> set = new TreeSet<>();
        TreeSet<String> tmp = new TreeSet<>();
        
        if (nums.contains(1)) {
            tmp = getOneChar();
            set.addAll(tmp); tmp.clear();
        }
        
        if (nums.contains(2)) {
            tmp = getTwoChar();
            set.addAll(tmp); tmp.clear();
        }
        
        if (nums.contains(3)) {
            tmp = getThreeChar();
            set.addAll(tmp); tmp.clear();
        }
        
        if (nums.contains(4)) {
            tmp = getFourChar();
            set.addAll(tmp); tmp.clear();
        }
        
        File f = new File(outputFile);
        if (f.exists()) {
            f.delete();
        }
        
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
            StringBuilder builder = new StringBuilder();
            for (String s : set) {
                builder.append(s).append("\n");
            }
            bw.write(builder.toString());
            bw.close();
        } catch (Exception ex) {
            System.out.println("Error in writing the output file: " + ex);
        }
        
    }
    
    private TreeSet<String> getOneChar() {
        TreeSet<String> tmp = new TreeSet<>();
        
        for (int i = 0 ; i < alphabet.length; ++i) {
            String uri = baseurl + alphabet[i];
            pr(uri);
            String res = uUrl.readStringFromUrl(uri);
            if (res != null && !res.isEmpty()) {
                res = res.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "").trim();
                if (!res.isEmpty()) {
                    String [] split = res.split(",");
                    for (String ii : split) {
                        tmp.add(ii.trim().toLowerCase());
                    }
                }
            }
        }
        return tmp;
    }
    
    private TreeSet<String> getTwoChar() {
        TreeSet<String> tmp = new TreeSet<>();
        
        for (int i = 0 ; i < alphabet.length; ++i) {
            for (int j = 0 ; j < alphabet.length; ++j) {
                String uri = baseurl + alphabet[i] + alphabet[j];
                pr(uri);
                String res = uUrl.readStringFromUrl(uri);
                if (res != null && !res.isEmpty()) {
                    res = res.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "").trim();
                    if (!res.isEmpty()) {
                        String [] split = res.split(",");
                        for (String ii : split) {
                            tmp.add(ii.trim().toLowerCase());
                        }
                    }
                }
            }
        }
        return tmp;
    }
    
    private TreeSet<String> getThreeChar() {
        TreeSet<String> tmp = new TreeSet<>();
        
        for (int i = 0 ; i < alphabet.length; ++i) {
            for (int j = 0 ; j < alphabet.length; ++j) {
                for (int k = 0 ; k < alphabet.length; ++k) {
                    String uri = baseurl + alphabet[i] + alphabet[j] + alphabet[k];
                    pr(uri);
                    String res = uUrl.readStringFromUrl(uri);
                    if (res != null && !res.isEmpty()) {
                        res = res.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "").trim();
                        if (!res.isEmpty()) {
                            String [] split = res.split(",");
                            for (String ii : split) {
                                tmp.add(ii.trim().toLowerCase());
                            }
                        }
                    }
                }
            }
        }
        return tmp;
    }
    
    private TreeSet<String> getFourChar() {
        TreeSet<String> tmp = new TreeSet<>();
        
        for (int i = 0 ; i < alphabet.length; ++i) {
            for (int j = 0 ; j < alphabet.length; ++j) {
                for (int k = 0 ; k < alphabet.length; ++k) {
                    for (int l = 0 ; l < alphabet.length; ++l) {
                        String uri = baseurl + alphabet[i] + alphabet[j] + alphabet[k] + alphabet[l];
                        pr(uri);
                        String res = uUrl.readStringFromUrl(uri);
                        if (res != null && !res.isEmpty()) {
                            res = res.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "").trim();
                            if (!res.isEmpty()) {
                                String [] split = res.split(",");
                                for (String ii : split) {
                                    tmp.add(ii.trim().toLowerCase());
                                }
                            }
                        }
                    }
                }
            }
        }
        return tmp;
    }
    
    private void pr(Object o) {
        System.out.println(o);
    }
    
}
