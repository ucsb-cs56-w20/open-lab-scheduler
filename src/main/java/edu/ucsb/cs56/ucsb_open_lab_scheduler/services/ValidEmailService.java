package edu.ucsb.cs56.ucsb_open_lab_scheduler.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;

public class ValidEmailService {
  public static boolean validEmail(final String email) {
    if (email == null) {
      return false;
    }
    final String regex = "^(.+)@(.+)$";

    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }
  
  public static boolean inDomain(final String email, final String myDomain) {
    if (email == null) {
      return false;
    }
    
    return email.substring(email.indexOf("@") + 1).equals(myDomain);

  }
}