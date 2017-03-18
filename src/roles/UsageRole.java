package roles;

import sx.blah.discord.handle.obj.IMessage;

import java.util.ArrayList;

public class UsageRole extends BotRole{

    private ArrayList<BotRole> roles;

    public UsageRole(ArrayList<BotRole> _roles){
        super();
        this.roles = _roles;
        super.roleName = "roles.UsageRole";
        super.commandPrefix = ".help";
        super.usage = new String[][] {
                { ".help", "Prints this help message." }
        };
    }

    @Override
    public void processCommand(IMessage message) {
        try {
            int maxWidth = 0;
            for (BotRole role : this.roles){
                for (String[] cmd : role.usage){
                    maxWidth = Math.max(maxWidth, cmd[0].length());
                }
            }
            maxWidth ++;

            StringBuilder sb = new StringBuilder();
            for (BotRole role : this.roles){
                sb.append("Commands for role \"" + role.roleName + "\"\n");
                for (String[] cmd : role.usage){
                    sb.append(String.format("%s\t%s\n",
                            String.format("%" + maxWidth + "." + maxWidth + "s", cmd[0]),
                            cmd[1]
                    ));
                }
                sb.append("\n");
            }

            message.getChannel().sendMessage(String.format("```%s```", sb.toString()));
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
