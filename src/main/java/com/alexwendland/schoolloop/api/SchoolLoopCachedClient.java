package com.alexwendland.schoolloop.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by awendland on 5/19/14.
 */
public class SchoolLoopCachedClient extends SchoolLoopClient {

    Map<String, String> cache = new HashMap<String, String>();

    public SchoolLoopCachedClient(String user, String pass) throws IOException {
        super(user, pass);
    }

    public SchoolLoopCachedClient(String sessionId) {
        super(sessionId);
    }

    @Override
    public String getPage(String path) throws IOException {
        if (cache.containsKey(path))
            return cache.get(path);
        else {
            String resp = super.getPage(path);
            cache.put(path, resp);
            return resp;
        }
    }
}
