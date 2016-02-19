package com.headhunt.impl;

import com.headhunt.ingest.jobtitle.LinkedinJobTitle;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sagraw200
 */
public class ImplJobTitle {
    
     public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1); list.add(2); list.add(3); list.add(4);
        
        LinkedinJobTitle xyz = new LinkedinJobTitle();
        xyz.getLinkedinJobTitle(list);
        
    }
    
}
