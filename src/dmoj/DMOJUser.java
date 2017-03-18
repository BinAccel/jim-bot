package dmoj;

import java.util.ArrayList;
import java.util.Arrays;

public class DMOJUser {
    public boolean error;
    public String errorMessage;

    public String userName;
    public String displayName;
    public double points;
    public ArrayList<String> organizations;
    public ArrayList<String> solvedProblems;

    public DMOJUser(){
        organizations = new ArrayList<>();
        solvedProblems = new ArrayList<>();
    }

    public DMOJUser(boolean e, String msg){
        this.error = e;
        this.errorMessage = msg;
    }

    public String toString(){
        if (this.error){
            return String.format("An error has occurred: " + this.errorMessage);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Username: %s\n", this.userName));
        sb.append(String.format("Display Name: %s\n", this.displayName));
        sb.append(String.format("Total Points: %.2f\n", this.points));
        sb.append(String.format("Organizations: %s\n", Arrays.toString(organizations.toArray())));

        return sb.toString();
    }
}
