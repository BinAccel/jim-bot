import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

public class DMOJ {
    public static String getResponse(String request) throws Exception{
        System.out.println("Processing request: " + request);

        URL url = new URL(request);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String response = "";
        while(true){
            String cur = reader.readLine();
            if (cur == null) break;
            response += cur;
        }

        return response;
    }

    public static DMOJProblem getProblemById(String probid){
        probid = probid.toLowerCase().trim();
        for (char c : probid.toCharArray()){
            if (!Character.isDigit(c) && !Character.isLetter(c)){
                return new DMOJProblem(true, "Problem ID can only contain alphanumeric characters");
            }
        }

        try{
            JSONObject obj = new JSONObject(getResponse(String.format("https://dmoj.ca/api/problem/info/%s", probid)));

            DMOJProblem problem = new DMOJProblem();
            problem.problemID = probid;
            problem.problemName = obj.getString("name");
            problem.timeLimit = obj.getDouble("time_limit");
            problem.memLimit = obj.getDouble("memory_limit");
            problem.pointValue = obj.getDouble("points");
            problem.partial = obj.getBoolean("partial");

            return problem;
        } catch (Exception ex){
            return new DMOJProblem(true, "Error fetching problem details.");
        }
    }

    public static DMOJUser getUserInfo(String username){
        username = username.trim();
        for (char c : username.toCharArray()){
            if (!Character.isDigit(c) && !Character.isLetter(c)){
                return new DMOJUser(true, "Username can only contain alphanumeric characters");
            }
        }

        try {
            JSONObject obj = new JSONObject(getResponse(String.format("https://dmoj.ca/api/user/info/%s", username)));
            DMOJUser user = new DMOJUser();
            user.userName = username;
            user.displayName = obj.getString("display_name");
            user.points = obj.getDouble("points");

            JSONArray solved = obj.getJSONArray("solved_problems");
            for (int i = 0; i < solved.length(); i++){
                user.solvedProblems.add((String) solved.get(i));
            }

            JSONArray orgs = obj.getJSONArray("organizations");
            for (int i = 0; i < orgs.length(); i++){
                user.organizations.add((String) orgs.get(i));
            }

            return user;
        } catch (Exception ex){
            return new DMOJUser(true, "Error fetching user details.");
        }
    }

    public static String[] props = {
            "os.name",
            "os.version",
            "os.arch",
            "java.runtime.name",
            "java.vm.version",
            "java.vm.name",
            "java.runtime.version",
    };

    public static String getInfo(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("jim-bot\n"));
        for (String pp : props){
            sb.append(String.format("%s: %s\n", pp, System.getProperty(pp)));
        }
        return sb.toString();
    }
}
