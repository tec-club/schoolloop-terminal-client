package com.alexwendland.schoolloop;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class SchoolLoopAPI {
    public static final String BASE_URL_SECURE = "https://cdm.schoolloop.com";
    public static final String BASE_URL = "http://cdm.schoolloop.com";

    public static CookieStore loginToSchoolloop(String pUserName, String pPassword, boolean checkLogin)
            throws ClientProtocolException, IOException {
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
        
        HttpResponse schoolloopLoginGetResponse = null;
        HttpGet schoolloopLoginHttpGet = new HttpGet(BASE_URL + "/portal/login");
        schoolloopLoginGetResponse = httpclient.execute(schoolloopLoginHttpGet);

        String schoolLoopString = EntityUtils.toString(schoolloopLoginGetResponse.getEntity());
        Pattern p = Pattern.compile("<input\\b[^>]+\\bname=\"form_data_id\"[^>]+\\bvalue=\"([0-9]*)\"");
        Matcher m = p.matcher(schoolLoopString);
        if (m.find()) {
            String formDataIDString = m.group(1);

            HttpPost schoolloopLoginHttpPost = new HttpPost(BASE_URL + "/portal/login?etarget=login_form");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("login_name", pUserName));
            nameValuePairs.add(new BasicNameValuePair("password", pPassword));
            nameValuePairs.add(new BasicNameValuePair("form_data_id", formDataIDString));
            nameValuePairs.add(new BasicNameValuePair("event_override", "login"));
            String[] blankFields = { "reverse", "sort", "login_form_reverse", "login_form_page_index", "login_form_page_item_count", "login_form_sort",
                    "return_url", "forward", "redirect", "login_form_letter", "login_form_filter" };
            populateNVListWithBlank(nameValuePairs, blankFields);
            schoolloopLoginHttpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse schoolloopLoginPostResponse = httpclient.execute(schoolloopLoginHttpPost);
            if (checkLogin) {
                if (EntityUtils.toString(schoolloopLoginPostResponse.getEntity()).contains("form_data_id")) {
                    return null;
                } else {
                    return cookieStore;
                }
            } else {
                return cookieStore;
            }
        } else {
            return null;
        }
    }

    private static void populateNVListWithBlank(List<NameValuePair> list, String[] array) {
        for (String name : array) {
            list.add(new BasicNameValuePair(name, ""));
        }
    }
    
    public static String getSessionID(CookieStore cs) {
        for (Cookie c : cs.getCookies()) {
            if (c.getName().equals("JSESSIONID")) {
                return c.getValue();
            }
        }
        return null;
    }
    private static final String PRINT_PAGE = "/portal/student_home?template=print";
    
    private static final Pattern R_ENTRY = Pattern.compile("<table .+ class=\"row\">(.+course.+)</table>");
    private static final Pattern R_PERIOD = Pattern.compile("<td class=\"period\">(\\d)</");
    private static final Pattern R_CLASS = Pattern.compile("<td class=\"course\">(.+)</");
    private static final Pattern R_GRADE = Pattern.compile("<td class=\"grade\">(.+)</");
    private static final Pattern R_PERCENT = Pattern.compile("<td class=\"percent\">(.+)%</");
    
    public static List<GradeBookEntry> getMainPagePrint(String sessionID) throws IOException {
        /*BasicCookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", sessionID);
        cookie.setDomain("cdm.schoolloop.com");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);*/
        CloseableHttpClient httpclient = HttpClientBuilder.create()/*.setDefaultCookieStore(cookieStore)*/.build();
        
        HttpResponse response = null;
        HttpGet request = new HttpGet(BASE_URL + PRINT_PAGE);
        System.out.println(BASE_URL + PRINT_PAGE);
        request.addHeader("Set-Cookie", "JSESSIONID="+sessionID);
        System.out.println(request.getFirstHeader("Set-Cookie"));
        response = httpclient.execute(request);
        
        List<GradeBookEntry> grades = new ArrayList<GradeBookEntry>();

        String resp = EntityUtils.toString(response.getEntity()).replace("\n","");
        System.out.println(resp);
        Matcher m = R_ENTRY.matcher(resp);
        while (m.find()) {
            String entry = m.group();
            String className = R_CLASS.matcher(entry).group();
            int period = Integer.parseInt(R_PERIOD.matcher(entry).group());
            String grade = R_GRADE.matcher(entry).group();
            double percent = Double.parseDouble(R_PERCENT.matcher(entry).group());
            grades.add(new GradeBookEntry(className, period, percent, grade));
        }
        return grades;
    }
    
}
