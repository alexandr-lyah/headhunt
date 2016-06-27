package com.headhunt.utils.specutils;

import com.headhunt.utils.commonutils.fileutils.UtilitiesFile;
import com.headhunt.utils.commonutils.tokenizerutils.TokenizerActionSelector;
import com.headhunt.utils.commonutils.tokenizerutils.UtilitiesTokenizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by sagraw200 on 3/19/16.
 */
public class JDSkillsMatch {

    private static final String skillsMaster = "src/main/resources/data/ingest/skill_master.txt";

    private static UtilitiesFile utilitiesFile;
    private static UtilitiesTokenizer utilitiesTokenizer;
    private static Set<String> foundSkills = new HashSet<>();
    private static List<String> skills;

    public JDSkillsMatch() {
        utilitiesFile = new UtilitiesFile();
        utilitiesTokenizer = new UtilitiesTokenizer();
        skills = new ArrayList<>();
        skills = utilitiesFile.readFileInList(skillsMaster);
        for (int i=0; i<skills.size(); ++i) {
            skills.set(i, skills.get(i).toLowerCase().trim());
        }
    }

    public static void main(String[] args) {
        JDSkillsMatch jdSkillsMatch = new JDSkillsMatch();

        String jd = jdSkillsMatch.getJD().toLowerCase();
        String jdClean = jd.replaceAll("\\."," ").replaceAll("\n"," ").replaceAll(","," ").replaceAll("\\\\", " ").replaceAll("/", " ");

        for (int i=4; i>0; --i) {
            List<String> jdtokens = jdSkillsMatch.getNgram(jdClean, i);
            jdClean = jdSkillsMatch.cleanText(jdtokens, jdClean);
//            System.out.println("jd clean: " + jdClean.length());
//            System.out.println("skill size: " + foundSkills.size());
        }

        for (String s : foundSkills) {
            System.out.println(s);
        }
    }

    private String cleanText(List<String> jdtokens, String jdClean) {
        for (int i=0; i<jdtokens.size(); ++i) {
            String token = jdtokens.get(i).toLowerCase().trim();
            if (skills.contains(token)) {
                foundSkills.add(token);
//                System.out.println(token);
                jdClean = jdClean.replaceAll(token," ");
            }
        }
        return jdClean;
    }

    private List<String> getNgram(String jdClean, int gram) {
        int[] grams = {gram};
        TokenizerActionSelector tokenizerActionSelector = new TokenizerActionSelector();
        tokenizerActionSelector.setWhichGrams(grams);
        List<String> jdtokens = utilitiesTokenizer.Tokenize(jdClean, tokenizerActionSelector);
        return jdtokens;
    }

    public String getJD() {
        return "We are seeking several Software Engineer, C++, Java for our customer in the Philadelphia and Moorestown NJ. area. The Software Engineer, C++, Java must be a US Citizen and have a current DOD Secret Security Clearance, or be able to obtain one. The S Software Engineer will work as part of a team and will be responsible for designing or modifying new and existing software applications - both external and internal to our organization. The Software Engineer, C++, Java will also be tasked to interact with other personnel from the Software Development Team, as well as Software Test and Cybersecurity Team members to design, develop, document, and test applications and software.\n" +
                "\n" +
                "Software Engineer, C++, Java Responsibilities\n" +
                "Design, development and of time delivery of applications, components and/or systems\n" +
                "Participate in design reviews to provide input on functional requirements, product designs, schedules, or potential problems\n" +
                "Recommend, design, and implement application enhancements and architectural improvements for existing products\n" +
                "Create system design specifications and other technical documents as required\n" +
                "Modify and execute test plans, scenarios, scripts, and procedures. Document test procedures to ensure repeatability and compliance with standards\n" +
                "Design and implement front end web interfaces\n" +
                "Maintain and enhance various web solutions\n" +
                "Document software defects using a bug tracking system, and report defects to software lead\n" +
                "Maintain source code in a version control system\n" +
                "Understand and follow secure coding standards\n" +
                "Work productively with other team members on small to large scale projects\n" +
                "Minimum Security Clearance\n" +
                "Must be eligible and pass security screening to obtain DoD Secret\n" +
                "\n" +
                "Software Engineer, C++, Java Required Qualifications and Skills\n" +
                "Proficiency in software design, development and testing\n" +
                "History with some or all of the following programming languages: Java, C#, C/C++, Objective-C, Python, Ruby, JavaScript, MySQL\n" +
                "Ability to develop applications following secure coding standards\n" +
                "Source control solutions such as Git, Mercurial, or SVN\n" +
                "\n" +
                "Software Engineer, C++, Java Additional Skills\n" +
                "Experience with Virtualization, VMWare preferred\n" +
                "Ability to identify, define and resolve problems, collect data, establish facts and draw valid conclusions\n" +
                "Proficient in Microsoft Office Suite\n" +
                "Excellent organizational skills\n" +
                "\n" +
                "Software Engineer, C++, Java Education\n" +
                "BA/BS degree in Computer Science, Software Engineering, Mathematics or equivalent.\n" +
                "Years of Experience\n" +
                "2-12"
                ;
    }

}
