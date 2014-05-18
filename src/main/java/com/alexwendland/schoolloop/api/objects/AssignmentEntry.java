package com.alexwendland.schoolloop.api.objects;

public class AssignmentEntry {
    public final String className;
    public final String assignment;
    public final String date;
    public final String category;

    public AssignmentEntry(String cn, String a, String d, String c) {
        className = cn;
        assignment = a;
        date = d;
        category = c;
    }

    public String toString() {
        return date + " -- " + assignment + " (" + className + " / " + category + ")";
    }

}