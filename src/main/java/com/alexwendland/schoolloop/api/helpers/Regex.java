package com.alexwendland.schoolloop.api.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Write a description of class Regex here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Regex
{
    
    private Pattern pattern;
    
    public Regex(String p)
    {
        pattern = Pattern.compile(p);
    }
    
    public String firstGroup(String string) {
        Matcher m = pattern.matcher(string);
        if (m.find()) {
            return m.group(1);
        } else
            return null;
    }
}
