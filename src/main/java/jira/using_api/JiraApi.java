package jira.using_api;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;
import org.apache.tomcat.util.json.ParseException;

import java.io.IOException;
import java.util.HashMap;

import java.io.*;
import java.util.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JiraApi {
    private static String email;
    private static String api_token;
    private static String jira_link;
    private final static Logger LOGGER = Logger.getLogger(JiraApi.class.getName());

    private static void getCredentials() throws IOException {
        Properties prop = new Properties();

        prop.load(new FileInputStream("src/main/resources/credentials.properties"));
        email = prop.getProperty("email");
        api_token = prop.getProperty("api_token");
        jira_link = prop.getProperty("jira_link");
    }

    private static JSONObject getJSON(int newStoryPoint) {
        String stringToParse = String.format("{\n" +
                "    \"fields\": {\n" +
                "        \"customfield_10026\": %d\n" +
                "    }\n" +
                "}", newStoryPoint);
        try {
            System.out.println(new JSONObject(stringToParse));
            return new JSONObject(stringToParse);
        } catch (JSONException err){
            LOGGER.log(Level.parse("Error"), err.toString());
        }
        return null;
    }

    public static int getStoryPointsFromPbi(String attribute_name, String pbiNo) throws IOException {
        getCredentials();
        HttpResponse<String> response = Unirest.get(jira_link +"/rest/api/latest/issue/"+pbiNo)
                .basicAuth(email, api_token)
                .header("Accept", "application/json")
                .asString();
        return Integer.parseInt(response.getBody().split(attribute_name)[1].substring(2).split("\\.")[0]);
    }

    private static void compareStoryPoints() throws IOException {
        HashMap<String, Integer> map = new ReadExcel().getPbiMap();
        HashMap<String, Integer> mapMismatch = new HashMap<>();

        for (String pbiName: map.keySet()) {
            int storyPointOnJira = getStoryPointsFromPbi("customfield_10026", pbiName);
            int plannedStoryPoint = map.get(pbiName);
            if (storyPointOnJira != plannedStoryPoint){
                System.out.printf("Mismatch: Story Point of %s is planned %d but entered %d on Jira.%n",
                        pbiName, plannedStoryPoint, storyPointOnJira);
                mapMismatch.put(pbiName, plannedStoryPoint);
            }
        }

        Scanner input = new Scanner(System.in);
        if (mapMismatch.size() != 0) {
            System.out.printf("Edit Story Points of %d PBI(s) on Jira according to the Sprint Planning Excel sheet? (y/n)",
                    mapMismatch.size());
            if (input.next().toLowerCase().equals("y")){
                for (String pbiName: mapMismatch.keySet()) {
                    editStoryPointOnJira(pbiName, mapMismatch.get(pbiName));
                    System.out.printf("Story Point of %s has been changed to %d%n", pbiName, mapMismatch.get(pbiName));
                }
            }
        } else {
            System.out.println("No mismatch found between the Excel sheet and Jira.");
        }
    }

    private static void editStoryPointOnJira(String pbiName, int newStoryPoint) throws IOException {
        getCredentials();
        HttpResponse<String> response = Unirest.put(jira_link +"/rest/api/2/issue/"+pbiName)
                .basicAuth(email, api_token)
                .header("Content-Type", "application/json")
                .body(getJSON(newStoryPoint))
                .asString();
    }

    public static void main(String[] args) throws IOException, ParseException {
        LOGGER.setLevel(Level.INFO);
        compareStoryPoints();
    }
}
