package com.alexwendland.schoolloop.api.helpers;

import junit.framework.TestCase;

/**
 * Created by awendland on 5/17/14.
 */
public class RegexTest extends TestCase {

    private String htmlString;

    public RegexTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();

        htmlString = "<tbody><tr>" +
                        "<td class=\"period\">2</td>" +
                        "<td class=\"course\">AP Chemistry 1B</td>" +
                        "<td class=\"grade\">A-</td>" +
                        "<td class=\"percent\">91.3%</td>" +
                        "<td class=\"zeros\"><span>Zeros: </span>0</td>" +
                    "</tr></tbody>";
    }

    public void testReturnGroup() {
        Regex r = new Regex("<td class=\"period\">(.+?)</");
        assertEquals("2", r.firstGroup(htmlString));
    }

    public void testNoGroupFound() {
        Regex r = new Regex("<td class=\"column\">(.+?)</td");
        assertNull(r.firstGroup(htmlString));
    }

}
