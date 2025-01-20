package models;

import java.time.LocalDateTime;

public class Notification {

    private String id;
    private String message;
    private LocalDateTime time;
    private int taskId;

    public Notification() {
    }

    public Notification(String id, String message, LocalDateTime time, int taskId) {
        this.id = id;
        this.message = message;
        this.time = time;
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                ", taskId=" + taskId +
                '}';
    }

    public void sendNotification() {
        // TODO implement here
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}