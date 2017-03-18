package roles;

import dmoj.DMOJ;
import dmoj.DMOJProblem;
import dmoj.DMOJUser;
import sx.blah.discord.handle.obj.IMessage;

public class DMOJRole extends BotRole{
    public DMOJRole(){
        super();
        super.roleName = "DMOJRole";
        super.commandPrefix = ".dmoj";
        super.usage = new String[][] {
                { ".dmoj user <username>", "Query information about the user."},
                { ".dmoj problem <probid>", "Query information about the problem."}
        };
    }

    @Override
    public void processCommand(IMessage message) {
        try {
            String[] tokens = message.getContent().split(" ");

            if (tokens.length == 3 && tokens[1].equals("user")){
                DMOJUser user = DMOJ.getUserInfo(tokens[2]);
                message.getChannel().sendMessage(String.format("```%s```", user));
            } else if (tokens.length == 3 && tokens[1].equals("problem")){
                DMOJProblem problem = DMOJ.getProblemById(tokens[2]);
                message.getChannel().sendMessage(String.format("```%s```", problem));
            } else {
                message.getChannel().sendMessage("Invalid DMOJ Query.");
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
