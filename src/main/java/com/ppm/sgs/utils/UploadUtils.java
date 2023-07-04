package com.ppm.sgs.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UploadUtils {

    public static String dummyUpload(byte[] data, String contentType) throws IOException {
        return "this_is_url";
    }

    public static String upload(byte[] data, String contentType) throws IOException {
        URL url = new URL("https://api.upload.io/v2/accounts/FW25bU3/uploads/binary");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Authorization", "Bearer " + System.getenv("UPLOAD_TOKEN"));
        httpConn.setRequestProperty("Content-Type", contentType);
        httpConn.setDoOutput(true);

        try (DataOutputStream wr = new DataOutputStream(httpConn.getOutputStream())) {
            wr.write(data);
        }

        String response = "";
        if (httpConn.getResponseCode() / 100 == 2) {
            Scanner s = new Scanner(httpConn.getInputStream());
            response = s.hasNext() ? s.next() : "";
        }

        Map<String, String> resMap = new ObjectMapper().readValue(response, HashMap.class);

        return resMap.get("fileUrl");
    }

}
