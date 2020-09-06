package JıraAPI;

import Objects.PBI;
import Service.JSONMethods;
import Service.JSONMethodsImpl;
import SlackAPI.SlackApi;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.IOException;

import java.io.*;
import java.util.*;

public class JiraApi {
    private String email;
    private String api_token;
    private String jira_link;
    private JSONMethods jsonMethods;
    private final Properties prop = new Properties();

    public void checkJira() throws IOException {
        getCredentials();
        jsonMethods = new JSONMethodsImpl();
        compareStoryPoints();
    }

    private void getCredentials() throws IOException {
        prop.load(new FileInputStream("src/main/resources/credentials.properties"));
        email = prop.getProperty("email");
        api_token = prop.getProperty("api_token");
        jira_link = prop.getProperty("jira_link");
    }

    private String getBodyString(int newStoryPoint) {
        return "{\n" +
                "    \"fields\": {\n" +
                "        \"customfield_10026\": "+newStoryPoint+"\n" +
                "    }\n" +
                "}";
    }

    private String getChannelIdFromAssignee(String assignee) {
        return prop.getProperty("directId"+assignee);
    }

    private int getStoryPointsFromJira(String attribute_name, String pbiNo) {
        HttpResponse<String> response = Unirest.get(jira_link +"/rest/api/latest/issue/"+pbiNo)
                .basicAuth(email, api_token)
                .header("Accept", "application/json")
                .asString();
        return Integer.parseInt(response.getBody().split(attribute_name)[1].substring(2).split("\\.")[0]);
    }

    private ArrayList<PBI> getStoryPointsFromExcel() throws IOException {
        return new ReadExcel().getPbiFromExcel();
    }

    private void compareStoryPoints() throws IOException {
        ArrayList<PBI> pbiList = getStoryPointsFromExcel();
        ArrayList<PBI> listMismatch = new ArrayList<>();

        for (PBI pbi : pbiList) {
            String pbiName = pbi.getPbiName();
            int storyPointOnJira = getStoryPointsFromJira("customfield_10026", pbiName);
            int plannedStoryPoint = pbi.getStoryPoint();
            if (storyPointOnJira != plannedStoryPoint) {
                System.out.printf("Mismatch: Story Point of %s is planned %d but entered %d on Jira.%n",
                        pbiName, plannedStoryPoint, storyPointOnJira);
                listMismatch.add(pbi);
            }
        }

        Scanner input = new Scanner(System.in);
        if (listMismatch.size() != 0) {
            System.out.printf("Edit Story Points of %d PBI(s) on Jira according to the Sprint Planning Excel sheet? (y/n)",
                    listMismatch.size());
            if (input.next().toLowerCase().equals("y")) {
                System.out.print("Would you like to notify the user(s) about the recent change on Jira? (y/n)");
                boolean willNotify = input.next().toLowerCase().equals("y");
                for (PBI pbi : listMismatch) {
                    String pbiName = pbi.getPbiName();
                    if (willNotify) {
                        notifyUser(getChannelIdFromAssignee(pbi.getAssignee()),
                                "Your story point of task '" +pbi.getPbiName()+ "' changed from " +
                                        getStoryPointsFromJira("customfield_10026", pbi.getPbiName()) +
                                        " to " + pbi.getStoryPoint());
                    }
                    System.out.print("Story Point of "+pbi.getPbiName()+" has been changed from " +
                            getStoryPointsFromJira("customfield_10026", pbi.getPbiName()) +
                            " to " + pbi.getStoryPoint());
                    editStoryPointOnJira(pbiName, pbi.getStoryPoint());
                }
                }
            } else {
                System.out.println("No mismatch found between the Excel sheet and Jira.");
            }
        }

        private void editStoryPointOnJira(String pbiName,int newStoryPoint) throws IOException {
            getCredentials();
            HttpResponse<String> response = Unirest.put(jira_link + "/rest/api/2/issue/" + pbiName)
                    .basicAuth(email, api_token)
                    .header("Content-Type", "application/json")
                    .body(jsonMethods.getJSON(getBodyString(newStoryPoint)))
                    .asString();
        }

        private void notifyUser(String username, String message) throws IOException {
            SlackApi slackApi = new SlackApi();
            slackApi.sendMessage(username, message);
        }
}
