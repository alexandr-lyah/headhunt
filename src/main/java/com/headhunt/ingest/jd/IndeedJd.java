package com.headhunt.ingest.jd;

import com.headhunt.utils.commonutils.urlutils.UtilitiesURL;

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
    private final String startjobId = "626efb4b88d6838b";

    private UtilitiesURL uurl;

    private List<String> allJobIds = new ArrayList<>();
    private List<String> readJobIds = new ArrayList<>();

    public IndeedJd() {
        uurl = new UtilitiesURL();
    }

    public static void main(String[] args) {
        IndeedJd indeedJd = new IndeedJd();
        indeedJd.impl();
    }

    private void impl() {
        allJobIds.add(startjobId);
        String urlText = readJDUrl(startURL + startjobId);
        String jdText = parseJD(urlText);
        List<String> recJobs = getRecommendedJobIds(urlText);
        allJobIds.addAll(recJobs);
        readJobIds.add(startjobId);

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
            recJobs = getRecommendedJobIds(urlText);
            allJobIds.addAll(recJobs);
            readJobIds.add(jobId);
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
        // summary : <span id="job_summary" class="summary">
        // job header : <div id="job_header" data-tn-component="jobHeader">
            // <b class="jobtitle"> : job title
            // <span class="company"> : company
            // <span class="location"> : location

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
