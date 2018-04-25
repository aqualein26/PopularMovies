

package com.aqualein.fancymovies.Utilities;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/** Utility to query the network and get the data.
 * Created by sandy on 28-Jun-17.
 */

public class NetworkUtility {

    /*public static boolean isOnline() {
        try {
            int timeoutMs = 1500;

            Socket sock = new Socket();

            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) { return false; }
    }*/

    public static boolean isOnline() throws InterruptedException, IOException
    {
        String command = "ping -c 1 google.com";

        return (Runtime.getRuntime().exec (command).waitFor() == 0);
    }

    public static String makeHttpRequest(String website) throws IOException {

        HttpsURLConnection urlConnection = null;
        InputStream in = null;
        String response = null;
        try {
            URL url = getURL(website);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.connect();
            in = urlConnection.getInputStream();
            response = getInputStream(in);


        } catch (Exception e) {

            e.printStackTrace();
        }
        finally {

            if(in != null) {

                urlConnection.disconnect();
            }
            if(in != null) {


                in.close();
            }

        }
        return response;
    }

    private static String getInputStream(InputStream in) throws Exception {

        StringBuilder output = new StringBuilder();
        InputStreamReader inputReader = new InputStreamReader(in, "UTF-8");
        BufferedReader reader = new BufferedReader(inputReader);
        String result = reader.readLine();
        while (result != null) {

            output.append(result);
            result = reader.readLine();
        }
        return output.toString();

    }

    private static URL getURL(String website) throws Exception {

        return new URL(website);

    }



}
