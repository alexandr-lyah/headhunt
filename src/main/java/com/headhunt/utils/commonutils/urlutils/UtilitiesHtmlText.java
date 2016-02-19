package com.headhunt.utils.commonutils.urlutils;

import java.util.regex.Pattern;

/**
 *
 * @author shekhar2010us
 */
public class UtilitiesHtmlText {
  
  /**
   * clean the HTML content of the text. Pass 1 or nothing for Regex and anything else for Jsoup
   * @param htmlText
   * @param method
   * @return 
   */
  public static String htmlCleanser(String htmlText, int ... method) {
    
    if ( method.length == 0 ) {
      return regexHTMLCleanser(htmlText);
    } else if (method[0] == 1) {
      return regexHTMLCleanser(htmlText);
    } else {
      return libraryHTMLCleanser(htmlText);
    }
    
  }
  
  private static String regexHTMLCleanser(String strHTML) {
    
    Pattern pattern = null;
    String regex = "<script type=\"text/javascript\">(.*?)</script>";
    pattern = Pattern.compile(regex, Pattern.DOTALL);
    strHTML = pattern.matcher(strHTML).replaceAll(" ");
    
    pattern = null;
    regex = "<script>(.*?)</script>";
    pattern = Pattern.compile(regex);
    strHTML = pattern.matcher(strHTML).replaceAll(" ");
    
    pattern = null;
    regex = "<xml>(.*?)</xml>";
    pattern = Pattern.compile(regex);
    strHTML = pattern.matcher(strHTML).replaceAll(" ");
    
    pattern = null;
    regex = "<[^>]*>";
    pattern = Pattern.compile(regex);
    strHTML = pattern.matcher(strHTML).replaceAll(" ");
    
    strHTML = strHTML.replaceAll("\\s+", " ");
    
    return strHTML;
    
  }
  
  private static String libraryHTMLCleanser(String htmlText) {
    
      // clean the message - 1) unescapehtml
     //return StringEscapeUtils.unescapeHtml4( DOMUtility.TextConverter( DOMUtility.Parser(htmlText) ) );
    
    // clean the message - 2) parse with jsoup and get only plain text
    return UtilitiesHtmlParser.TextConverter( UtilitiesHtmlParser.Parser(htmlText) );
    
  }
  
}
