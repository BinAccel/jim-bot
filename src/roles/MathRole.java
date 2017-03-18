package roles;

import com.wolfram.alpha.*;
import sx.blah.discord.handle.obj.IMessage;

public class MathRole extends BotRole{

    private WAEngine engine;

    public MathRole(String appid){
        super();
        super.roleName = "MathRole";
        super.commandPrefix = ".evaluate";
        super.usage = new String[][] {
                { ".evaluate <expression>", "Evaluates the given expression using WolframAlpha." }
        };

        engine = new WAEngine();
        engine.setAppID(appid);
        engine.addFormat("plaintext");
    }

    @Override
    public void processCommand(IMessage message) {
        try {
            String expression = message.getContent().substring(message.getContent().indexOf(" ") + 1);

            try {
                WAQuery query = engine.createQuery();
                query.setInput(expression);
                WAQueryResult result = engine.performQuery(query);

                StringBuilder sb = new StringBuilder();
                if (result.isSuccess()){
                    for (WAPod pod : result.getPods()) {
                        if (!pod.isError()) {
                            if (pod.getTitle().equals("Input") ||
                                    pod.getTitle().equals("Result") ||
                                    pod.getTitle().equals("Input interpretation") ||
                                    pod.getTitle().equals("Definitions")) {
                                sb.append(pod.getTitle() + ": ");
                                for (WASubpod subpod : pod.getSubpods()) {
                                    for (Object element : subpod.getContents()) {
                                        if (element instanceof WAPlainText) {
                                            sb.append(((WAPlainText) element).getText() + "\n");
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    sb.append("Query was not understood. No results available.");
                }

                message.getChannel().sendMessage(String.format("```\n%s\n```", sb.toString()));
            } catch (WAException ex){
                message.getChannel().sendMessage(String.format("Error during evaluation: " + ex.getLocalizedMessage()));
            }
        } catch (Exception ex){

        }
    }
}
