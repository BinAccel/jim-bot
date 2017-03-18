package roles;

import sx.blah.discord.handle.obj.IMessage;

public abstract class BotRole {
    public String roleName = null;
    public String commandPrefix = null;
    public String[][] usage;
    public abstract void processCommand(IMessage message);
}
