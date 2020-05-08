package no.tytraman.newguilds;

import com.google.gson.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class GsonManager {
    private Gson gson;

    public GsonManager() {
        this.gson = createGsonInstance();
    }

    private Gson createGsonInstance() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
    }

    public String getUuidFromUsername(String username) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            try {
                return json.get("id").getAsString();
            }catch (NullPointerException e){}
        }catch(IOException e) {}
        return null;
    }

    public String getUsernameFromUuid(String uuid) {
        try {
            URL url = new URL("https://api.mojang.com/user/profiles/" + uuid.replace("-", "") + "/names");
            InputStreamReader reader = new InputStreamReader(url.openStream());
            try {
                JsonArray array = (JsonArray) new JsonParser().parse(reader);
                String username = ((JsonObject)array.get(array.size() - 1)).get("name").getAsString();
                return username;
            }catch(NullPointerException e) {
            }catch(ClassCastException e) {}
        }catch(IOException e) {}
        return null;
    }
}
