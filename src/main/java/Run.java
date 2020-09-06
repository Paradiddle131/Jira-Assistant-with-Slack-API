import JıraAPI.JiraApi;
import SlackAPI.SlackApi;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Run {
    private static String channelIdJiraCheck;
    private static String directIdAnna;
    private static String directIdBenedict;
    private static String directIdSam;
    private static String directIdHerald;

    private static void getCredentials() throws IOException {
        Properties prop = new Properties();

        prop.load(new FileInputStream("src/main/resources/credentials.properties"));

        channelIdJiraCheck = prop.getProperty("channelIdJiraCheck");
        directIdAnna = prop.getProperty("directIdAnna");
        directIdBenedict = prop.getProperty("directIdBenedict");
        directIdSam = prop.getProperty("directIdSam");
        directIdHerald = prop.getProperty("directIdHerald");
    }

    public static void main(String[] args) throws IOException {
        getCredentials();

        JiraApi jiraApi = new JiraApi();
        SlackApi slackApi = new SlackApi();

        jiraApi.checkJira();
//        slackApi.sendMessage(directIdAnna, "Run");
    }
}
