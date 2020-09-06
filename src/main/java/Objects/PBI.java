package Objects;

public class PBI {
    private String epicName;
    private int priority;
    private String pbiName;
    private String pbiDefinition;
    private String sprintNo;
    private String assignee;
    private int storyPoint;

    public PBI(String epicName, int priority, String pbiName, String pbiDefinition, String sprintNo, String assignee,
               int storyPoint) {
        this.epicName = epicName;
        this.priority = priority;
        this.pbiName = pbiName;
        this.pbiDefinition = pbiDefinition;
        this.sprintNo = sprintNo;
        this.assignee = assignee;
        this.storyPoint = storyPoint;
    }

    public String getEpicName() {
        return epicName;
    }

    public void setEpicName(String epicName) {
        this.epicName = epicName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getPbiName() {
        return pbiName;
    }

    public void setPbiName(String pbiName) {
        this.pbiName = pbiName;
    }

    public String getPbiDefinition() {
        return pbiDefinition;
    }

    public void setPbiDefinition(String pbiDefinition) {
        this.pbiDefinition = pbiDefinition;
    }

    public String getSprintNo() {
        return sprintNo;
    }

    public void setSprintNo(String sprintNo) {
        this.sprintNo = sprintNo;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public int getStoryPoint() {
        return storyPoint;
    }

    public void setStoryPoint(int storyPoint) {
        this.storyPoint = storyPoint;
    }
}
