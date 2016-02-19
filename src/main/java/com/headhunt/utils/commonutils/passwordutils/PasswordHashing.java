package com.headhunt.utils.commonutils.passwordutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Random;

/**
 *
 * @author shekhar2010us
 */

public class PasswordHashing {
  
  private static final Logger logger = LoggerFactory.getLogger(PasswordHashing.class);

  private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZaebcdefghijklmnopqrstuvwxyz";
  private static Random rnd = new Random();

  public static String generateOTP( int len ) {
    StringBuilder sb = new StringBuilder( len );
    for( int i = 0; i < len; i++ )
      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
    return sb.toString();
  }

  public static String hashPassword(String passwordToHash) {
    logger.debug("hashPassword {}");
    
    String generatedPassword = null;
    try {
      // Create MessageDigest instance for MD5
      MessageDigest md = MessageDigest.getInstance("MD5");
      //Add password bytes to digest
      md.update(passwordToHash.getBytes());
      //Get the hash's bytes
      byte[] bytes = md.digest();
      //This bytes[] has bytes in decimal format;
      //Convert it to hexadecimal format
      StringBuilder sb = new StringBuilder();
      for( int i=0; i < bytes.length; i++ ) {
        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
      }
      //Get complete hashed password in hex format
      generatedPassword = sb.toString();
    }
    catch (Exception e) {
      logger.error("problem in hashing the password: " , e);
    }
    
    return generatedPassword;
  }
  
}