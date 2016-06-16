package com.headhunt.ingest.jd;

import com.headhunt.utils.commonutils.fileutils.UtilitiesFile;
import com.headhunt.utils.specutils.General;
import com.headhunt.utils.commonutils.urlutils.UtilitiesURL;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.File;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author sagraw200
 */
public class LinkedinJD {

    private static final String basejoburl = "https://www.linkedin.com/vsearch/j?orig=JSHP&keywords=Software+Engineer&distance=50&locationType=I&countryCode=us&trk=two_box_geo_fill";
    private static final String baseurl = "https://www.linkedin.com/jobs2/view/";
    private static final String outputFile = "src/resources/data/ingest/linkedin.jd.tmp2";

    // taken first 50 job ids
    private static int [] startJobIds = {132301592, 141733728};
    private static String rareDelim = "\u2764\u0191\u01DC\u0108\u0137\u2764"+"wowitsucks";

    private UtilitiesURL uUrl;
    private UtilitiesFile uFile;

    private static TreeSet<String> tmpJobId = new TreeSet<>();

    public LinkedinJD() {
        uUrl = new UtilitiesURL();
        uFile = new UtilitiesFile();
    }

    public static void main (String [] args) {
        LinkedinJD jd = new LinkedinJD();
//        jd.getJobIdsFromJobSearchPage();

        // read for each start job ids and then try for +1 -1 and save for valid html
        jd.parseLinkedinJdPage(baseurl);

    }

    private void parseLinkedinJdPage(String page) {

        // TODO: get position, company, location
        StringBuilder builder = new StringBuilder();
        File f = new File(outputFile);
        try {
            if (!f.exists())
                f.createNewFile();
        } catch (Exception ex) {
            General.pr("Error in creating file.. " + ex);
        }
        /**
         // this is the sequence to read
         html
         body
         div#application-body
         div.job-desc
         div.main
         div.description-module.container
         div.content
         div.description-section
         div.rich-text
         */

        int N = 1000;
        int count = 0;
        for (int i = 0 ; i < N; ++i) {

            for (int id : startJobIds) {

                String url1 = page + (id + i);
                String url2 = page + (id - i);

                if (!tmpJobId.contains(url1)) {
                    count++;
                    tmpJobId.add(url1);
                    General.pr(count + ": " + url1);
                    String co = readHtmlContent(url1);
                    builder.append(co).append(rareDelim).append("\n\n");
                }

                if (!tmpJobId.contains(url2)) {
                    count++;
                    tmpJobId.add(url2);
                    General.pr(count + ": " + url2);
                    String co = readHtmlContent(url2);
                    builder.append(co);
                }

                if (count >= 100) {
                    uFile.appendToFile(outputFile, builder.toString());
                    builder = new StringBuilder();
                    count = 0;
                }

                try {
                    Thread.sleep(100);
                } catch (Exception e2x) {
                    General.pr("error in sleeping: " + e2x);
                }
            }

        }

    }

    public String readHtmlContent(String url) {
        StringBuilder builder = new StringBuilder();
        String content = null;

        try {
            content = uUrl.readStringFromUrl(url);
        } catch (Exception ex) {
            General.pr("Error in getting html for page: " + url + "\n" + ex);
        }

        if (content != null) {
            String[] jd = getJdFromCodeComment(content);
            builder.append("Url: " + url).append("\n");
            builder.append("Job Description: " + jd[0]).append("\n");
            builder.append("Roles & responsibilities: " + jd[1]).append("\n");
            builder.append("Skills: " + jd[2]).append("\n");
        }

//        if (content != null) {
//            String jd = getJdFromJsoup(content);
//        }

        return builder.toString();
    }

    private String getJdFromJsoup(String content) {
        StringBuilder builder = new StringBuilder();
        String html = null;
        try {
            Document doc = Jsoup.parse(content, "UTF-8");
            if (doc != null) {
                Element elem = doc.select("div.rich-text").first();
                if (elem != null) {
                    html = elem.html();
                    if (html != null) {
                        Document tidy = Jsoup.parseBodyFragment(html);
                        builder.append(tidy.toString()).append("\n\n");
                        builder.append(rareDelim).append("\n\n");
                    }
                }
            }
        } catch (Exception e1x) {
            General.pr("Error in parsing html for page: " + content + "\n" + e1x);
        }
        return builder.toString();
    }
    private String[] getJdFromMeta(String content) {
        String [] result = new String[2];

        String pattern = "<meta property=\"og:description\" content=";
        String regex = pattern+".*?(>)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        if (m.find()) {
            String c = m.group();
            c = c.replaceAll(pattern, "").replaceAll(">","");
            if (c.trim().length() > 50) {
                String[] parts = c.split("Role and Responsibility");
                if (parts != null && parts.length >= 2) {
                    result[0] = parts[0].trim();
                    result[1] = parts[1].trim();
                }
            }
            return result;
        }
        return null;
    }
    private String[] getJdFromCodeComment(String content) {
        String [] result = new String[3];

        String startpattern = "<code id=\"jobDescriptionModule\">";
        String endpattern = "</code>";
        String regex = startpattern + ".*?" + endpattern;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        if (m.find()) {
            String c = m.group();
            c = c.replaceAll(startpattern, "").replaceAll(endpattern,"").replaceAll("<!--","").replaceAll("-->","").trim();
            c = c.replaceAll("<p>","").replaceAll("</p>","").replaceAll("<li>","").replaceAll("<ul>","").replaceAll("</ul>","").replaceAll("<strong>","").replaceAll("</strong>","");
            c = c.replaceAll("</li>","..").replaceAll("<br>","..").replaceAll("<u>","");

            if (c.trim().length() > 50) {
                try {
                    JSONObject json = new JSONObject(c);
                    if (json.has("description")) {
                        String desc = json.getString("description");
                        if (desc != null && desc.length() > 10) {
                            String[] parts = desc.split("Role and Responsibility");
                            result[0] = parts[0].trim();
                            if (parts.length >= 2) {
                                result[1] = parts[1].trim();
                            }
                        }
                    }
                    if (json.has("skillsDescription")) {
                        String skills = json.getString("skillsDescription");
                        if (skills != null) {
                            result[2] = skills.trim();
                        }
                    }
                } catch (JSONException ex) {
                    System.err.println("Error in parsing json jd: " + ex);
                }
            }
            return result;
        }
        return null;
    }

    private void getJobIdsFromJobSearchPage() {
        HashSet<String> set = new HashSet<>();

        String folder = "/Users/sagraw200/Documents/dev/team-personal/hr/src/main/resources/data/ingest/full_jd_html";
        File f = new File(folder);
        if (f.exists()) {
            File[] files = f.listFiles();
            for (File file : files) {
                String text = uFile.readFileInString(file.getAbsolutePath());
                String regex = "(\"id\":).*?(,)";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(text);
                while (m.find()) {
                    String ss = m.group().replaceAll("\"","").replaceAll(":", "").replaceAll("," , "").replaceAll("id","").trim();
                    set.add(ss);
                }
            }
        }

        General.pr(set.size());
        for (String s : set) {
            General.pr(s);
        }

    }
    private void pr(Object o) {
        System.out.println(o);
    }

}
