package com.ppm.sgs.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class UploadUtils {
	
	public static String dummyUpload(byte[] data, String contentType) throws IOException{
		return "this_is_url";
	}
	
	public static String upload(byte[] data, String contentType) throws IOException {
		URL url = new URL("https://api.upload.io/v2/accounts/kW15bPc/uploads/binary");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Authorization", "Bearer public_kW15bPcCURDDavMCCYghuArVGKbf");
        httpConn.setRequestProperty("Content-Type", contentType);
        httpConn.setDoOutput(true);
        
        try(DataOutputStream wr = new DataOutputStream(httpConn.getOutputStream())) {
            wr.write(data);
        }

        String response = "";
        if(httpConn.getResponseCode() / 100 == 2) {
            Scanner s = new Scanner(httpConn.getInputStream());
            response = s.hasNext() ? s.next() : "";
        }
        
        return response;
	}
}

