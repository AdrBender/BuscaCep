package com.adriano.buscacep;

import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
 
public class HttpConnection {
 
    public HttpConnection() {
		
    }
 
    public String getHttpData(String reqUrl) {
        String res = null;
		
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            res = convertStreamToString(in);
			
        } catch (MalformedURLException e) {
			e.printStackTrace();
        } catch (ProtocolException e) {
            e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.getMessage();
        }
        return res;
    }
 
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
