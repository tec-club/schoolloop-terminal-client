package com.alexwendland.schoolloop.api.objects;

public class GradeBookEntry {
    public final String className;
    public final int period;
    public final Double percent;
    public final String grade;
    public final Integer zeros;

    public GradeBookEntry(String cn, int p, Double pe, String g, Integer z) {
        className = cn;
        period = p;
        percent = pe;
        grade = g;
        zeros = z;
    }

    public String toString() {
        return period + " " + className + " -- " + grade + "/" + percent + ", " + zeros + " zeros";
    }

}