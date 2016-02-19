package com.headhunt.impl;

import com.headhunt.ingest.skills.IndeedSkills;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sagraw200
 */
public class ImplGetIndeedSkills {
    
    public static void main(String[] args) {
        
        List<Integer> list = new ArrayList<>();
        list.add(1); list.add(2); list.add(3); list.add(4);
        
        IndeedSkills getSkills = new IndeedSkills();
        getSkills.getIndeedSkills(list);
        
    }
    
}
