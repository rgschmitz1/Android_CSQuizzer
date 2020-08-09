package edu.tacoma.uw.csquizzer.helper;

import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * The ServiceHandler is a helper class. The purpose of this class is to get json data.
 *
 * @author  Phuc Pham N
 * @version 1.0
 * @since   2020-08-05
 */
public class ServiceHandler {

    static InputStream is = null;
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;

    public ServiceHandler() {}

    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
    public String makeServiceCall(String url, int method, Map<String, String> mapConditions) {
        // http client
        StringBuilder response = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try {
            // Checking http request method type
            if (method == POST) {
                URL urlObject = new URL(url);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                JSONObject mUserJSON = new JSONObject();
                for (Map.Entry<String,String> entry : mapConditions.entrySet()){
                    mUserJSON.put(entry.getKey(),entry.getValue());
                }
                OutputStreamWriter wr =
                        new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(mUserJSON.toString());
                wr.flush();
                wr.close();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s;
                while ((s = buffer.readLine()) != null) {
                    response.append(s);
                }
                buffer.close();
            } else if (method == GET) {
                if(mapConditions != null) {
                    for (Map.Entry<String,String> entry : mapConditions.entrySet()){
                        url = Uri.parse(url)
                                .buildUpon()
                                .appendQueryParameter(entry.getKey(), entry.getValue())
                                .build().toString();
                    }
                }
                URL urlObject = new URL(url);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s;
                while ((s = buffer.readLine()) != null) {
                    response.append(s);
                }
                buffer.close();
            }
        } catch (Exception e) {
            response.append("Authentication exception occurred: ");
            response.append(e.getMessage());
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return response.toString();

    }

}
