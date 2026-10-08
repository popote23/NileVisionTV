package com.nilevision.tv;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class M3uParser {
    public static List<Channel> load(String url, String filter) throws Exception {
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        c.setConnectTimeout(12000); c.setReadTimeout(20000);
        List<Channel> out = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream(), "UTF-8"))) {
            String line, name = null, group = "";
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#EXTINF")) {
                    group = attr(line, "group-title");
                    int i = line.lastIndexOf(',');
                    name = i >= 0 ? line.substring(i + 1).trim() : "Chaîne";
                } else if (!line.isEmpty() && !line.startsWith("#") && name != null) {
                    if (filter == null || group.toLowerCase().contains(filter)) out.add(new Channel(name, line, group));
                    name = null;
                }
            }
        } finally { c.disconnect(); }
        return out;
    }
    private static String attr(String l, String k) {
        int i = l.indexOf(k + "=\"");
        if (i < 0) return "";
        i += k.length() + 2;
        int e = l.indexOf('"', i);
        return e < 0 ? "" : l.substring(i, e);
    }
}
