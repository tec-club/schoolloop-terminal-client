package com.alexwendland.schoolloop.api;

import com.alexwendland.schoolloop.api.helpers.Regex;
import com.alexwendland.schoolloop.api.objects.GradeBookEntry;
import com.alexwendland.schoolloop.utils.FileUtils;
import junit.framework.TestCase;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * Created by awendland on 5/17/14.
 */
public class SchoolLoopTest extends TestCase {

    private String htmlString;

    public SchoolLoopTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();

        URL url = this.getClass().getResource("/school_loop_print_page_demo.html");
        File file = new File(url.getFile());

        htmlString = FileUtils.readFileToString(file.getPath());
    }

    public void testParseGrades() {
        List<GradeBookEntry> grades = SchoolLoop.getGrades(htmlString);
        assertEquals(8, grades.size());
    }

}
