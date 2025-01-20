package models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Task  implements Serializable {

    private String id;
    private String title;
    private String description;
    private Map<String, Date> dateTime;
    private Priority priority;
    private Status status;
    private String categoryId;

    public Task() {
        this.dateTime = new HashMap<>();
    }

    public Task(String id, String title, String description, Map<String, Date> dateTime, Priority priority, Status status, String categoryId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.priority = priority;
        this.status = status;
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dateTime=" + dateTime +
                ", priority=" + priority +
                ", status=" + status +
                ", categoryId='" + categoryId + '\'' +
                '}';
    }

    public boolean isEqual(Task task) {
        return this.id.equals(task.id);
    }

    public void markAsComplete() {
        this.status = Status.COMPLETE;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Date> getDateTime() {
        return dateTime;
    }

    public void setDateTime(Map<String, Date> dateTime) {
        this.dateTime = dateTime;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCategoryId() {
        return categoryId;
    }


    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }



}