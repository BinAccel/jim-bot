package roles;

import dmoj.DMOJ;
import dmoj.DMOJProblem;
import dmoj.DMOJUser;
import sx.blah.discord.handle.obj.IMessage;

import java.util.*;

public class DMOJRole extends BotRole {

    private ArrayList<DMOJProblem> problems;
    private long lastReloadTime = 0L;

    public DMOJRole() {
        super();
        super.roleName = "DMOJRole";
        super.commandPrefix = ".dmoj";
        super.usage = new String[][]{
                {".dmoj user <username>", "Query information about the user."},
                {".dmoj problem <probid>", "Query information about the problem."},
                {".dmoj reload-list", "Reloads local DMOJ problem list"},
                {".dmoj list-types", "Lists problem types."},
                {".dmoj query [+points low high] [+type type]", "Query 10 problems based on requirements in random order."}
        };

        problems = new ArrayList<>();
    }

    @Override
    public void processCommand(IMessage message) {
        try {
            String[] tokens = message.getContent().split(" ");

            if (tokens.length == 3 && tokens[1].equals("user")) {
                DMOJUser user = DMOJ.getUserInfo(tokens[2]);
                message.getChannel().sendMessage(String.format("```%s```", user));
            } else if (tokens.length == 3 && tokens[1].equals("problem")) {
                DMOJProblem problem = DMOJ.getProblemById(tokens[2]);
                message.getChannel().sendMessage(String.format("```%s```", problem));
            } else if (tokens[1].equals("list-types")) {
                StringBuilder sb = new StringBuilder();
                for (String t : DMOJ.problemTypes) sb.append(t.toLowerCase() + "\n");
                message.getChannel().sendMessage("```" + sb.toString() + "```");
            } else if (tokens[1].equals("reload-list")) {
                long interval = System.currentTimeMillis() - lastReloadTime;
                if (interval <= 1800000) {
                    message.getChannel().sendMessage(String.format("Wait for %d seconds before reloading again.",
                            (1800000 - interval) / 1000));
                    return;
                }
                message.getChannel().sendMessage("Reloading local DB ....");

                lastReloadTime = System.currentTimeMillis();
                problems.clear();

                ArrayList<String> ids = DMOJ.getProblemIDList();
                for (String cid : ids) {
                    DMOJProblem cprob = DMOJ.getProblemById(cid);
                    if (cprob != null && !cprob.error)
                        problems.add(cprob);
                }

                message.getChannel().sendMessage(
                        String.format("Done reloading DB. %d problems found. ", problems.size()));
            } else if (tokens[1].equals("query")) {
                Set<DMOJProblem> finalSet = new HashSet<>(this.problems);
                for (int i = 2; i < tokens.length; i++){
                    if (tokens[i].charAt(0) == '+'){
                        Set<DMOJProblem> tempSet = new HashSet<>();
                        if (tokens[i].startsWith("+points") && i + 2 < tokens.length){
                            int low = Integer.parseInt(tokens[i + 1]);
                            int high = Integer.parseInt(tokens[i + 2]);

                            for (DMOJProblem cp : finalSet){
                                if (cp.pointValue >= low && cp.pointValue <= high){
                                    tempSet.add(cp);
                                }
                            }
                        } else if (tokens[i].startsWith("+type") && i + 1 < tokens.length){
                            String type = tokens[i + 1];

                            for (DMOJProblem cp : finalSet){
                                if (cp.types.contains(type.toLowerCase()))
                                    tempSet.add(cp);
                            }
                        }

                        finalSet = tempSet;
                    }
                }

                ArrayList<DMOJProblem> probs = new ArrayList<>(finalSet);
                Collections.shuffle(probs, new Random(System.currentTimeMillis()));

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < Math.min(probs.size(), 10); i++){
                    sb.append(String.format("[%s] %s Points = %.2f, Types = %s\n",
                            probs.get(i).problemID, probs.get(i).problemName,
                            probs.get(i).pointValue,
                            Arrays.toString(probs.get(i).types.toArray())));
                }

                message.getChannel().sendMessage("```" + sb.toString() + "```");
            } else {
                message.getChannel().sendMessage("Invalid DMOJ Query.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
