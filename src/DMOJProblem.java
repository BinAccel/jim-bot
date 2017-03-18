public class DMOJProblem implements Comparable<DMOJProblem> {
    public boolean error;
    public String errorMessage;

    public String problemID;
    public String problemName;
    public double pointValue;
    public double timeLimit;
    public double memLimit;
    public boolean partial;

    public DMOJProblem(){}

    public DMOJProblem(boolean e, String msg){
        this.error = e;
        this.errorMessage = msg;
    }

    public DMOJProblem(String id, String name, double value) {
        this.problemID = id;
        this.problemName = name;
        this.pointValue = value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DMOJProblem)) return false;
        DMOJProblem prob = (DMOJProblem) o;
        return this.problemID.trim().equals(prob.problemID.trim());
    }

    @Override
    public int compareTo(DMOJProblem prob) {
        return this.problemID.compareTo(prob.problemID);
    }

    public String toString() {
        if (this.error)
            return String.format("An error has occurred: " + this.errorMessage);

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Problem ID: %s\n", this.problemID));
        sb.append(String.format("Problem Name: %s\n", this.problemName));
        sb.append(String.format("Time Limit (seconds): %.2f\n", this.timeLimit));
        sb.append(String.format("Memory Limit (KB): %.2f\n", this.memLimit));
        sb.append(String.format("Point Value: %s\n", this.pointValue));
        sb.append(String.format("Partial Points: %s\n", this.partial ? "Yes" : "No"));

        return sb.toString();
    }
}