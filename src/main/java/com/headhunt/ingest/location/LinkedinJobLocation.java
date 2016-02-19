package com.headhunt.ingest.location;

import com.headhunt.utils.specutils.General;
import com.headhunt.utils.commonutils.fileutils.UtilitiesFile;
import com.headhunt.utils.commonutils.urlutils.UtilitiesURL;

import java.io.File;
import java.util.List;
import java.util.TreeSet;

/**
 *
 * @author sagraw200
 */
public class LinkedinJobLocation {
    
    private static final String baseurl = "https://www.linkedin.com/ta/geo?query=";
    private static final String outputFile = "src/main/resources/data/ingest/linkedin.joblocation.tmp";
    private static final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    
    private static UtilitiesURL uUrl;
    private static UtilitiesFile uFile;
    
    public LinkedinJobLocation() {
        uUrl = new UtilitiesURL();
        uFile = new UtilitiesFile();
    }
    
    /**
     * 
     * @param nums 
     */
    public void getLinkedinJobLocation(List<Integer> nums) {
        try {
            File f = new File(outputFile);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
        } catch (Exception ex) {
            System.out.println("Error in deleting/creating file: " + ex);
        }
        
        TreeSet<String> set = new TreeSet<>();
        TreeSet<String> tmp = new TreeSet<>();
        
        if (nums.contains(1)) {
            tmp = General.getOneChar(alphabet, baseurl, uUrl);
            set.addAll(tmp); tmp.clear();
        }
        uFile.appendToFile(outputFile, General.setToBuilder(set)); set.clear();
        
        if (nums.contains(2)) {
            tmp = General.getTwoChar(alphabet, baseurl, uUrl);
            set.addAll(tmp); tmp.clear();
        }
        uFile.appendToFile(outputFile, General.setToBuilder(set)); set.clear();
        
        if (nums.contains(3)) {
            tmp = General.getThreeChar(alphabet, baseurl, uUrl);
            set.addAll(tmp); tmp.clear();
        }
        uFile.appendToFile(outputFile, General.setToBuilder(set)); set.clear();
        
        if (nums.contains(4)) {
            tmp = General.getFourChar(alphabet, baseurl, uUrl);
            set.addAll(tmp); tmp.clear();
        }
        uFile.appendToFile(outputFile, General.setToBuilder(set)); set.clear();
        
    }
    
    
}
