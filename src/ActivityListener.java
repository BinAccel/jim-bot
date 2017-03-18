import roles.*;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

import java.util.ArrayList;

public class ActivityListener {

    public ArrayList<BotRole> roles;

    public ActivityListener(){
        roles = new ArrayList<>();
        roles.add(new PingRole());
        roles.add(new UsageRole(this.roles));
        roles.add(new InfoRole());
        roles.add(new DMOJRole());
        roles.add(new MathRole(BotConstants.WOLFRAM_KEY));
        roles.add(new SteamRole(BotConstants.STEAM_KEY));
        roles.add(new OsuRole(BotConstants.OSU_KEY));
    }

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        IMessage message = event.getMessage();

        for (BotRole role : roles){
            if (message.getContent().startsWith(role.commandPrefix)){
                role.processCommand(message);
            }
        }
    }
}