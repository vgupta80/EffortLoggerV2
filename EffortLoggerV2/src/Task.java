
public class Task {
    private String taskName;
    private int timeInMinutes;

    public Task(String taskName, int timeInMinutes) {
        this.taskName = taskName;
        this.timeInMinutes = timeInMinutes;
    }

    public String getTaskName() {
        return taskName;
    }

    public int getTimeInMinutes() {
        return timeInMinutes;
    }

    public void setTimeInMinutes(int timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", timeInMinutes=" + timeInMinutes +
                '}';
    }
}
