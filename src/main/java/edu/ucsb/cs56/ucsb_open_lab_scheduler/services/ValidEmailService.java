package edu.ucsb.cs56.ucsb_open_lab_scheduler.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidEmailService {
  public static boolean validEmail(String email) {
    if (email == null) {
      return false;
    }
    String regex = "^(.+)@(.+)$";

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }
}