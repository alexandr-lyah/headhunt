package com.headhunt.impl;

import com.headhunt.ingest.location.LinkedinJobLocation;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sagraw200
 */
public class ImplJobLocation {
    
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1); list.add(2); list.add(3); list.add(4);
        
        LinkedinJobLocation xyz = new LinkedinJobLocation();
        xyz.getLinkedinJobLocation(list);
        
    }
    
}
