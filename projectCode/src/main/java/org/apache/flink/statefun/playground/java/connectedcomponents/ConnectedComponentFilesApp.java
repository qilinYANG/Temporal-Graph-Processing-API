package org.apache.flink.statefun.playground.java.connectedcomponents;

import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ConnectedComponentFilesApp {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file names in ./data folder: ");
        File fileName = new File("./data/" + scanner.nextLine().trim());
        Scanner scFiles = new Scanner(fileName);

        while(scFiles.hasNextLine()) {
            //test with ConnectedComponents http request format
            String[] inputStr = scFiles.nextLine().trim().split(" ");

            //create connection to undertow server
            String protocol = String.format("http://localhost:8090/connected-components.fns/vertex/%1$s", inputStr[0]);

            URL appServerUrl = new URL(protocol);
            HttpURLConnection con = (HttpURLConnection) appServerUrl.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/vnd.connected-components.types/vertex");
            con.setDoOutput(true);

            String jsonString = String.format("{\"src\": \"%1$s\", \"dst\": \"%2$s\", \"t\": \"%3$s\"}", inputStr[0], inputStr[1], inputStr[2]);
            System.out.println(jsonString);

            //write output to undertow web server
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println(responseCode + "\n");
        }
    }

}
