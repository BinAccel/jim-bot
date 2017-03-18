package roles;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONObject;
import sx.blah.discord.handle.obj.IMessage;

public class OsuRole extends BotRole {

    private String osuKey;

    public OsuRole(String _key) {
        super();
        super.roleName = "OsuRole";
        super.commandPrefix = ".osu";
        super.usage = new String[][]{
                {".osu user <username>", "Query information about the osu! player."}
        };

        this.osuKey = _key;
    }

    public String getUserJSON(String username) {
        try {
            URL url = new URL(
                    "https://osu.ppy.sh/api/get_user?k=" + this.osuKey + "&u=" + username);

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder str = new StringBuilder();
            String usr_json;

            while ((usr_json = in.readLine()) != null) {
                str.append(usr_json);
            }

            return str.toString();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void processCommand(IMessage message) {
        try {
            String[] tokens = message.getContent().toLowerCase().split("\\s+");
            if (tokens.length != 3) {
                message.getChannel().sendMessage("osu! query must have 3 tokens.");
                return;
            }

            String username = tokens[2];

            String response = getUserJSON(username);
            if (response.length() > 2) {
                JSONObject user = new JSONObject(response.substring(1, response.length() - 1));
                String level = user.getString("level");
                String accuracy = String.format("%.2f", user.getDouble("accuracy")) + "%";
                String playcount = user.getString("playcount");
                String pp = user.getString("pp_raw");

                message.getChannel().sendMessage(String.format(
                                "Here are the osu stats for %s:\n```Level: %s\nAccuracy: %s\nPlaycount: %s\nPP: %s```",
                                username, level, accuracy, playcount, pp));
            } else {
                message.getChannel().sendMessage("Error: The username specified is not found or invalid.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
};