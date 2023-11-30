package models;

import javafx.beans.property.SimpleStringProperty;

public class TaskData {
    private final SimpleStringProperty taskName;
    private final SimpleStringProperty formattedTime;

    public TaskData(String taskName, String formattedTime) {
        this.taskName = new SimpleStringProperty(taskName);
        this.formattedTime = new SimpleStringProperty(formattedTime);
    }

    public String getTaskName() {
        return taskName.get();
    }

    public SimpleStringProperty taskNameProperty() {
        return taskName;
    }

    public String getFormattedTime() {
        return formattedTime.get();
    }

    public SimpleStringProperty formattedTimeProperty() {
        return formattedTime;
    }
}
