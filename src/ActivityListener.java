import roles.BotRole;
import roles.PingRole;
import roles.UsageRole;
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