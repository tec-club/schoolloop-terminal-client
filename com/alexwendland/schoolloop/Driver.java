package com.alexwendland.schoolloop;

import java.util.List;

/**
 * Implements the School Loop API
 * 
 * @author Alex Wendland
 * @version 20140503
 */
public class Driver
{
    public static void main(String[] args) {
        try {
            String user = "awendland";
            String pass = "xela10";
            
            /* String sessionID = SchoolLoopAPI.getSessionID(SchoolLoopAPI.loginToSchoolloop(user, pass, true));
            System.out.println(sessionID); */
            
            SchoolLoop sl = new SchoolLoop(user, pass);
            System.out.println(sl.getSessionId());
            
            List<GradeBookEntry> grades = sl.getGrades();
            
            for (GradeBookEntry g : grades) {
                System.out.format("%4s%24s%12s%12s", g.period, g.className, g.percent, g.grade);
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
