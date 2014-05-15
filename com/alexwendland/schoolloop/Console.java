package com.alexwendland.schoolloop;

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
        String user = args.get("user");
        if (user == null)
            return "Missing username. Use '--user blahblah'.";
        String pass = args.get("pass");
        if (pass == null)
            return "Missing password. Use '--pass blahblah'.";
        
        System.out.println("Logging in as '" + user + "'.....");
        SchoolLoop sl = null;
        try {
            sl = new SchoolLoop(user, pass);
        } catch (LoginException e) {
            return "Invalid login credentials";
        }
        
        System.out.println("Session ID: " + sl.getSessionId());
        
        if (args.getInput() != null) {
        
            if (args.getInput().equals("grades")) {
                System.out.println();
                System.out.format("%4s%24s%12s%12s", "P", "Class", "%", "Grade");
                System.out.println("\n----------------------------------------------------");
                List<GradeBookEntry> grades = sl.getGrades();
                for (GradeBookEntry g : grades) {
                    System.out.format("%4s%24s%12s%12s", g.period, g.className, g.percent, g.grade);
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
                        options.put(lastOption, arg);
                    else
                        input = arg;
                    lastOption = null;
                }
            }
        }
        
        String get(String key) {
            return options.get(key);
        }
        
        String getInput() {
            return input;
        }
    }
    
}
