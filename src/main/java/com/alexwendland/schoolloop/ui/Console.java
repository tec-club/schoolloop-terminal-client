package com.alexwendland.schoolloop.ui;

import com.alexwendland.schoolloop.api.SchoolLoop;
import com.alexwendland.schoolloop.api.helpers.LoginException;
import com.alexwendland.schoolloop.api.objects.GradeBookEntry;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.io.IOException;

/**
 * Provides console access to the SchoolLoop object
 * 
 * @author Alex Wendland
 * @version 20140503
 */
public class Console
{
    public static void main(String[] args) {
        try {
            System.out.println(execute(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String execute(String[] argsArr) throws IOException {
        Args args = new Args(argsArr);
        SchoolLoop sl = null;

        if (args.get("id") != null) {
            try {
                sl = new SchoolLoop(args.get("id"));
            } catch (LoginException e) {
                return "Invalid login credentials";
            }
            if (!sl.isValidSession())
                return "This session is invalid";
            System.out.println("Loading.....");
        } else {
            String user = args.get("user");
            if (user == null)
                return "Missing username. Use '--user blahblah'.";
            String pass = args.get("pass");
            if (pass == null)
                return "Missing password. Use '--pass blahblah'.";

            System.out.println("Logging in as '" + user + "'.....");
            try {
                sl = new SchoolLoop(user, pass);
            } catch (LoginException e) {
                return "Invalid login credentials";
            }

            System.out.println("Session ID: " + sl.getSessionId());
        }
        
        if (args.getInput() != null) {
        
            if (args.getInput().equals("grades")) {
                System.out.println();
                System.out.format("%4s%30s%12s%10s%10s", "P", "Class", "%", "Grade", "Zeros");
                System.out.println("\n------------------------------------------------------------------");
                List<GradeBookEntry> grades = sl.getGrades();
                for (GradeBookEntry g : grades) {
                    System.out.format("%4s%30s%12s%10s%10s", g.period, g.className, g.percent, g.grade, g.zeros);
                    System.out.println();
                }
            }
            
        }
        
        return "";
    }
    
    public static class Args {
        
        Map<String, String> options;
        String input;
        
        public Args(String[] args) {
            options = new HashMap<String, String>();
            
            String lastOption = null;
            for (String arg : args) {
                if (arg.contains("--"))
                    lastOption = arg.replace("--", "");
                else {
                    if (lastOption != null)
                        options.put(lastOption.toLowerCase(), arg.toLowerCase());
                    else
                        input = arg.toLowerCase();
                    lastOption = null;
                }
            }
        }
        
        String get(String key) {
            return options.get(key.toLowerCase());
        }
        
        String getInput() {
            return input;
        }
    }
    
}
