package Service;

import JıraAPI.JiraApi;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONMethodsImpl implements JSONMethods {
    private final static Logger LOGGER = Logger.getLogger(JiraApi.class.getName());

    @Override
    public JSONObject getJSON(String stringToParse) {
        try {
            return new JSONObject(stringToParse);
        } catch (JSONException err){
            LOGGER.log(Level.parse("Error"), err.toString());
        }
        return null;
    }
}
