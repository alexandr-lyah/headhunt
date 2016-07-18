package com.headhunt.ingest.jd;

import com.headhunt.utils.commonutils.fileutils.UtilitiesFile;
import com.headhunt.utils.commonutils.urlutils.UtilitiesURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sagraw200 on 09/06/16.
 */
public class IndeedJd {

    // search_url = "http://www.indeed.com/jobs?q=software+engineer&l=Philadelphia%2C+PA";
    private final String startURL = "http://www.indeed.com/viewjob?jk=";
    private final String startjobId = "7bfe49f2643e1b2e";

    private UtilitiesURL uurl;

    private List<String> allJobIds = new ArrayList<>();
    private List<String> readJobIds = new ArrayList<>();

    public IndeedJd() {
        uurl = new UtilitiesURL();
    }

    public static void main(String[] args) {
        IndeedJd indeedJd = new IndeedJd();

        UtilitiesFile utilitiesFile = new UtilitiesFile();
        indeedJd.parseJD(utilitiesFile.readFileInString("/Users/sagraw200/Documents/dev/team-personal/headhunt/src/main/java/com/headhunt/ingest/jd/sampleindeed.html"));
//        indeedJd.impl();
    }

    private void impl() {

        allJobIds.add(startjobId);

        String urlText = readJDUrl(startURL + startjobId);
        String jdText = parseJD(urlText);
        readJobIds.add(startjobId);

        List<String> recJobs = getRecommendedJobIds(urlText);
        for (String jid : recJobs) {
            if (!allJobIds.contains(jid)) {
                allJobIds.add(jid);
            }
        }

        int N = 1;
        while (true) {
            String jobId = null;
            while ( true ) {
                jobId = allJobIds.get(N);
                N++;
                if (!readJobIds.contains(jobId)) {
                    break;
                }
            }

            urlText = readJDUrl(startURL + jobId);
            jdText = parseJD(urlText);
            readJobIds.add(startjobId);

            recJobs = getRecommendedJobIds(urlText);
            for (String jid : recJobs) {
                if (!allJobIds.contains(jid)) {
                    allJobIds.add(jid);
                }
            }

        }

    }

    private List<String> getRecommendedJobIds(String urlText) {
        // recommended jobs : <div class="recJobs">
        // jk=e28fa4eeaf95b1f4 inside href

        List<String> result = new ArrayList<>();

        Pattern pattern = null;
        String regex = "<div class=\"recJobs\">(.*)</div>";
        pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher m = pattern.matcher(urlText);
        if (m.find()) {
            String recText = m.group();
            pattern = null;
            regex = "jk=(.*?)&from=recjobs";
            pattern = Pattern.compile(regex, Pattern.DOTALL);
            m = pattern.matcher(recText);
            while (m.find()) {
                String recjobid = m.group();
                result.add(recjobid.replaceAll("jk=", "").replaceAll("&from=recjobs", "").trim());
            }
        }

        return result;
    }

    private String parseJD(String urlText) {
        System.out.println("Start JD Parsing");
        Document doc = Jsoup.parse(urlText);

        StringBuilder general = new StringBuilder();
        List<String> ll = new ArrayList();
        Elements elems = doc.select("span[class=summary]");
        for (Element elem : elems) {
            Element idElem = elem.getElementById("job_summary");
            List<Node> childnodes = idElem.childNodes();

            for (Node childnode : childnodes) {
                boolean isList = false;
                Node childParent = childnode.parentNode().parentNode();
                if (childParent instanceof Element) {
                    String ee = ((Element)childParent).tagName();
                    if (ee.contains("li")) {
                        isList = true;
                    }
                }

                String nodeText = "";
                String nodeTag = "";

                if (childnode instanceof TextNode) {
                    nodeText = ((TextNode) childnode).text().replaceAll("\n", " ").trim();
                }
                else if (childnode instanceof Element) {
                    nodeText = ((Element)childnode).text().replaceAll("\n", " ").trim();
                    String tag = ((Element)childnode).tagName();
                    if (tag != null && tag.equalsIgnoreCase("b")) {
                        nodeTag = "b";
                    }
                }

                // print
                if (!isList && ll.isEmpty()) {
                    general.append(nodeTag).append(".").append(nodeText).append(".");
                }
                else if (!isList && !ll.isEmpty()) {
                    general.append(ll).append(" ");
                    ll = new ArrayList<>();
                } else {
                    ll.add(nodeText);
                }

                if (isList) {
                    System.out.print(isList + " ");
                }
                if (!nodeTag.isEmpty()) {
                    System.out.print(nodeTag + " ");
                }
                if (!nodeText.isEmpty()) {
                    System.out.println(nodeText + " ");
                }

            }
        }

        // summary : <span id="job_summary" class="summary">
        // job header : <div id="job_header" data-tn-component="jobHeader">
        // <b class="jobtitle"> : job title
        // <span class="company"> : company
        // <span class="location"> : location

//        System.out.println(general.toString().replaceAll("[.]+", ".").replaceAll("( )+", " "));
        System.out.println("End JD Parsing");
        return null;
    }

    private String readJDUrl(String url) {
        String str = uurl.readStringFromUrl(url);

        Pattern pattern = null;
        String regex = "<script type=\"text/javascript\">(.*?)</script>";
        pattern = Pattern.compile(regex, Pattern.DOTALL);
        str = pattern.matcher(str).replaceAll("\n");

        pattern = null;
        regex = "<script(.*?)</script>";
        pattern = Pattern.compile(regex, Pattern.DOTALL);
        str = pattern.matcher(str).replaceAll("\n");

        pattern = null;
        regex = "<noscript(.*?)</noscript>";
        pattern = Pattern.compile(regex, Pattern.DOTALL);
        str = pattern.matcher(str).replaceAll("\n");

        pattern = null;
        regex = "<style(.*?)</style>";
        pattern = Pattern.compile(regex, Pattern.DOTALL);
        str = pattern.matcher(str).replaceAll("\n");

        str = str.replaceAll("[\\n]+", "\n");

        return str;
    }

}
