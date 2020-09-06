package SlackAPI;

import Service.JSONMethods;
import Service.JSONMethodsImpl;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SlackApi {
    private JSONMethods jsonMethods;

    private String webHookURL;

    private void init() throws IOException {
        getCredentials();
        jsonMethods = new JSONMethodsImpl();
    }

    private void getCredentials() throws IOException {
        Properties prop = new Properties();

        prop.load(new FileInputStream("src/main/resources/credentials.properties"));
        webHookURL = prop.getProperty("webHookURL");
    }

    private String getBodyString(String channelId, String text) {
        return "{\n" +
                "  \"channel\": \""+channelId+"\",\n" +
                "  \"text\": \""+text+"\"\n" +
                "}";
    }

    public void sendMessage(String channelId, String text) throws IOException {
        init();
        HttpResponse<String> response = Unirest.post(webHookURL)
                .header("Content-type", "application/json")
                .body(jsonMethods.getJSON(getBodyString(channelId, text)))
                .asString();
    }
}