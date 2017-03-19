package roles;

import sx.blah.discord.handle.obj.IMessage;

public class PingRole extends BotRole{
    public PingRole(){
        super();
        super.roleName = "PingRole";
        super.commandPrefix = ".ping";
        super.usage = new String[][] {
                { ".ping", "Pings the bot." }
        };
    }

    @Override
    public void processCommand(IMessage message) {
        try {
            message.getChannel().sendMessage("Pong.");
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
