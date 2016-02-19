package com.headhunt.utils.commonutils.mixed;

/**
 *
 * @author sagraw001c
 */
public class DelimiterFactory {
  
  private static final String COMMADELIMTER = ",";
  private static final String TABDELIMTER = "\t";
  private static final String PIPEDELIMTER = "\\|";
  private static final String SEMICOLONDELIMTER = ";";

  /**
   * @return the COMMADELIMTER
   */
  public static String getCOMMADELIMTER() {
    return COMMADELIMTER;
  }

  /**
   * @return the TABDELIMTER
   */
  public static String getTABDELIMTER() {
    return TABDELIMTER;
  }

  /**
   * @return the PIPEDELIMTER
   */
  public static String getPIPEDELIMTER() {
    return PIPEDELIMTER;
  }

  /**
   * @return the SEMICOLONDELIMTER
   */
  public static String getSEMICOLONDELIMTER() {
    return SEMICOLONDELIMTER;
  }
  
}
