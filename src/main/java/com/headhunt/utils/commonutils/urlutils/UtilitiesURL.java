package com.headhunt.utils.commonutils.urlutils;

import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by sagraw001c on 1/31/15.
 */
public class UtilitiesURL {

    public String readStringFromUrl(String url) {
        InputStream is = null;
        String text = null;
        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            text = readAll(rd);
        } catch (IOException ex) {
          System.out.println(ex);
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException ex) {
              System.out.println(ex);
            }
        }
        return text;
    }

    public JSONObject readJsonFromUrl(String url) {
        InputStream is = null;
        JSONObject json = null;
        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            json = new JSONObject(jsonText);
        } catch (Exception ex) {
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException ex) {
            }
        }
        return json;
    }

    private String readAll(Reader rd) {
        StringBuilder sb = new StringBuilder();
        try {
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
        } catch (IOException ex) {

        }
        return sb.toString();
    }

}
