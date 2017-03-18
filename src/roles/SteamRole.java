package roles;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import sx.blah.discord.handle.obj.IMessage;

public class SteamRole extends BotRole {

    public SteamRole() {
        super();
        super.roleName = "SteamRole";
        super.commandPrefix = ".steam";
        super.usage = new String[][]{
                {".steam game <user>", "Query the current steam game played by user."}
        };
    }

    @Override
    public void processCommand(IMessage message) {
        try {
            String[] tokens = message.getContent().toLowerCase().split("\\s+");
            if (tokens.length != 3) {
                message.getChannel().sendMessage("Steam query must have 3 tokens.");
                return;
            }

            String username = tokens[2];
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();

            String id_url = "http://steamcommunity.com/id/" + username + "/?xml=1";
            Document id_doc = b.parse(id_url);

            Element id = id_doc.getDocumentElement();
            String id64 = getString("steamID64", id);
            if (username.contains("765611980")) {
                id64 = username;
            }

            String pf_url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=46B20A9C3979D5E751CCA7E7AC5F09CF&steamids="
                    + id64 + "&format=xml";
            Document pf_doc = b.parse(pf_url);
            Element pf = pf_doc.getDocumentElement();

            String game = getString("gameextrainfo", pf);
            String user = getString("personaname", pf);

            if (user == null){
                message.getChannel().sendMessage("The username specified is invalid.");
                return;
            }

            if (game == null){
                message.getChannel().sendMessage("The user specified is not playing a game.");
                return;
            }

            message.getChannel().sendMessage(String.format("`%s` is playing `%s`.", user, game));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getString(String tagName, Element element) {
        NodeList list = element.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0) {
            NodeList subList = list.item(0).getChildNodes();

            if (subList != null && subList.getLength() > 0) {
                return subList.item(0).getNodeValue();
            }
        }

        return null;
    }
}