package com.alexwendland.schoolloop.api;

import com.alexwendland.schoolloop.api.helpers.LoginException;
import com.alexwendland.schoolloop.api.helpers.Regex;
import com.alexwendland.schoolloop.api.objects.AssignmentEntries;
import com.alexwendland.schoolloop.api.objects.GradeBookEntry;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SchoolLoop {

    private SchoolLoopCachedClient cachedClient;

    public SchoolLoop(String user, String pass) throws IOException {
        cachedClient = new SchoolLoopCachedClient(user, pass);
    }

    public SchoolLoop(String sessionId) {
        cachedClient = new SchoolLoopCachedClient(sessionId);
    }

    private static final String PRINT_PAGE = "/portal/student_home?template=print";

    // Grades

    private static final Pattern R_ENTRY = Pattern.compile("<table .+? class=\"row\">(<tr>.+?course.+?</tr>)</table>");
    private static final Regex R_PERIOD = new Regex("<td class=\"period\">(.+?)</");
    private static final Regex R_CLASS = new Regex("<td class=\"course\">(.+?)</");
    private static final Regex R_GRADE = new Regex("<td class=\"grade\">(.+?)</");
    private static final Regex R_PERCENT = new Regex("<td class=\"percent\">(.+?)%</");
    private static final Regex R_ZEROS = new Regex("<span>Zeros:.*?(\\d)+<\\/");
    
    public List<GradeBookEntry> getGrades() throws IOException {
        String entity = cachedClient.getPage(PRINT_PAGE);
        return getGrades(entity);
    }

    public static List<GradeBookEntry> getGrades(String html) {
        List<GradeBookEntry> grades = new ArrayList<GradeBookEntry>();

        Matcher m = R_ENTRY.matcher(html.replace("\n",""));
        while (m.find()) {
            String entry = m.group();
            String className = R_CLASS.firstGroup(entry);
            int period = Integer.parseInt(R_PERIOD.firstGroup(entry));
            String grade = R_GRADE.firstGroup(entry);
            Double percent = null;
            try {
                percent = Double.parseDouble(R_PERCENT.firstGroup(entry));
            } catch (Exception e){}
            Integer zeros = null;
            try {
                zeros = Integer.parseInt(R_ZEROS.firstGroup(entry));
            } catch (Exception e){}
            GradeBookEntry gradeEntry = new GradeBookEntry(className, period, percent, grade, zeros);
            grades.add(gradeEntry);
        }
        return grades;
    }

    // Assignments

    public List<AssignmentEntries> getAssignments() throws IOException {
        String entity = cachedClient.getPage(PRINT_PAGE);
        return getAssignments(entity);
    }

    public static List<AssignmentEntries> getAssignments(String html) {
        List<AssignmentEntries> assignments = new ArrayList<AssignmentEntries>();

        Document doc = Jsoup.parse(html);
        Element select = doc.select("#cal_label_day_has").get(0).parent().parent();
        while (m.find()) {
            String entry = m.group();
            String className = R_CLASS.firstGroup(entry);
            int period = Integer.parseInt(R_PERIOD.firstGroup(entry));
            String grade = R_GRADE.firstGroup(entry);
            Double percent = null;
            try {
                percent = Double.parseDouble(R_PERCENT.firstGroup(entry));
            } catch (Exception e){}
            Integer zeros = null;
            try {
                zeros = Integer.parseInt(R_ZEROS.firstGroup(entry));
            } catch (Exception e){}
            GradeBookEntry gradeEntry = new GradeBookEntry(className, period, percent, grade, zeros);
            grades.add(gradeEntry);
        }
        return grades;
    }
    
}