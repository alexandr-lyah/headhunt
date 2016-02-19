/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.headhunt.utils.commonutils.urlutils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author shekhar2010us
 */
public class UtilitiesHtmlParser {
  
  /**
   * Parser method that takes String and returns Jsoup document
   * @param message
   * @return 
   */
  public static Document Parser(String message) {
    return Jsoup.parse(message);
  }
  
  /**
   * Convert a html string into plain text using Jsoup
   * @param html
   * @return 
   */
  public static String TextConverter(Document html) {
    return html.text();
  }
  
}
