package com.alexwendland.schoolloop.api;

import com.alexwendland.schoolloop.api.helpers.LoginException;
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
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by awendland on 5/19/14.
 */
public class SchoolLoopClient {
    public static final String BASE_DOMAIN = "cdm.schoolloop.com";
    public static final String BASE_URL_SECURE = "https://" + BASE_DOMAIN;
    public static final String BASE_URL = "http://" + BASE_DOMAIN;

    private HttpClient http;
    private BasicCookieStore cookieStore;

    private String sessionId;

    public SchoolLoopClient() {
        cookieStore = new BasicCookieStore();
        http = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
    }

    /**
     * Create a new object for retrieving School Loop data. Logs in using user and pass and handles JSESSIONID retrieval.
     * @param user Username to login with
     * @param pass Password to login with
     * @throws java.io.IOException Network errors
     */
    public SchoolLoopClient(String user, String pass) throws IOException {
        this();

        if (!login(user, pass))
            throw new LoginException("Invalid Login data");
    }

    /**
     * Create a new object for retrieving School Loop data.
     * @param sessionId JSESSIONID to use. Should be a valid session.
     */
    public SchoolLoopClient(String sessionId) {
        this();

        setSessionId(sessionId);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
        BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", sessionId);
        cookie.setDomain(BASE_DOMAIN);
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
    }

    private static final String R_FORM_DATA_ID = "<input\\b[^>]+\\bname=\"form_data_id\"[^>]+\\bvalue=\"([0-9]*)\"";

    public boolean login(String username, String password) throws ClientProtocolException, IOException {
        HttpGet request = new HttpGet(BASE_URL + "/portal/login");
        HttpResponse response = http.execute(request);

        String entity = EntityUtils.toString(response.getEntity());
        Pattern p = Pattern.compile(R_FORM_DATA_ID);
        Matcher m = p.matcher(entity);
        if (m.find()) {
            String formDataIDString = m.group(1);

            HttpPost loginRequest = new HttpPost(BASE_URL + "/portal/login?etarget=login_form");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("login_name", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("form_data_id", formDataIDString));
            nameValuePairs.add(new BasicNameValuePair("event_override", "login"));
            String[] blankFields = {
                    "reverse", "sort", "login_form_reverse", "login_form_page_index",
                    "login_form_page_item_count", "login_form_sort", "return_url",
                    "forward", "redirect", "login_form_letter", "login_form_filter"
            };
            populateNVListWithBlank(nameValuePairs, blankFields);
            loginRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse loginResponse = http.execute(loginRequest);
            if (!EntityUtils.toString(loginResponse.getEntity()).contains("form_data_id")) {
                sessionId = getSessionID(cookieStore);
                return true;
            }
        }
        return false;
    }

    private static void populateNVListWithBlank(List<NameValuePair> list, String[] array) {
        for (String name : array) {
            list.add(new BasicNameValuePair(name, ""));
        }
    }

    public boolean isValidSession() throws IOException {
        HttpGet loginRequest = new HttpGet(BASE_URL + "/portal/login?etarget=login_form");
        // Execute HTTP Post Request
        HttpResponse loginResponse = http.execute(loginRequest);
        return !EntityUtils.toString(loginResponse.getEntity()).contains("form_data_id");
    }

    public static String getSessionID(CookieStore cs) {
        for (Cookie c : cs.getCookies()) {
            if (c.getName().equals("JSESSIONID")) {
                return c.getValue();
            }
        }
        return null;
    }

    public String getPage(String path) throws IOException {
        HttpGet request = new HttpGet(BASE_URL + path);
        HttpResponse response = http.execute(request);

        return EntityUtils.toString(response.getEntity());
    }
}
