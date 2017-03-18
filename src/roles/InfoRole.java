package roles;

import sx.blah.discord.handle.obj.IMessage;

public class InfoRole extends BotRole{
    public InfoRole(){
        super();
        super.roleName = "InfoRole";
        super.commandPrefix = ".info";
        super.usage = new String[][] {
                { ".info",                  "Gets basic information for bot host." },
                { ".info <p1> <p2> ...",    "Gets list of properties for bot host."}
        };
    }

    @Override
    public void processCommand(IMessage message) {
        try {
            String[] tokens = message.getContent().toLowerCase().split("\\s+");
            if (tokens.length < 1){
                message.getChannel().sendMessage("Info command must have at least 1 token.");
                return;
            }

            String[] properties = null;
            if (tokens.length == 1){
                properties = new String[] {
                        "os.name",
                        "os.version",
                        "os.arch",
                        "java.runtime.name",
                        "java.vm.version",
                        "java.vm.name",
                        "java.runtime.version",
                };
            } else {
                properties = new String[tokens.length - 1];
                for (int i = 1; i < tokens.length; i++)
                    properties[i - 1] = tokens[i];
            }

            StringBuilder sb = new StringBuilder();
            sb.append("jim-bot info:\n");
            for (String prop : properties){
                String pp = System.getProperty(prop);
                sb.append(String.format("%s: %s\n", prop, pp == null ? "Error" : pp));
            }

            message.getChannel().sendMessage(String.format("```%s```", sb.toString()));
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
