package models;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Board implements Serializable {

    private String id;
    private Set<Task> tasks ;
    private Set<Notification> notifications;


    class TitleFilter implements models.Filter , Serializable{

        String title;

        public TitleFilter(String title){
            this.title = title;
        }
        @Override
        public  boolean filter(Task task){
            if(task == null)
                return  false ;
            return task.getTitle().equals(title);
        }
    }
    class IdFilter implements models.Filter , Serializable{

        String id;

        public  IdFilter(String id){
            this.id = id;
        }
        @Override
        public boolean filter(Task task){
            if(task == null)
                return  false ;
            return task.getId().equals(id);
        }
    }
    class StatusFilter implements models.Filter , Serializable{

        Status status;

        public StatusFilter(Status status){
            this.status = status;
        }
        @Override
        public  boolean filter(Task task){
            if(task == null)
                return  false ;
            return task.getStatus().equals(status);
        }
    }
    class PriorityFilter implements models.Filter , Serializable{

        Priority priotity;

        public PriorityFilter(Priority priotity){
            this.priotity = priotity;
        }
        @Override
        public  boolean filter(Task task){
            if(task == null)
                return  false ;
            return task.getPriority().equals(priotity);
        }
    }
    class CategoryFilter implements models.Filter , Serializable{

        String categoryId;

        public CategoryFilter(String categoryId){
            this.categoryId = categoryId;
        }
        @Override
        public  boolean filter(Task task){
            if(task == null)
                return  false ;
            return task.getCategoryId().equals(categoryId);
        }
    }


    private StatusFilter inProgressFilter = new StatusFilter(Status.IN_PROGRESS);
    private StatusFilter completeFilter = new StatusFilter(Status.COMPLETE);
    private StatusFilter notStartedFilter = new StatusFilter(Status.NOT_STARTED);

    private PriorityFilter lowPriorityFilter = new PriorityFilter(Priority.LOW);
    private PriorityFilter mediumPriorityFilter = new PriorityFilter(Priority.MEDIUM);
    private PriorityFilter HighPriorityFilter = new PriorityFilter(Priority.HIGH);


    public Board() {
        tasks = new HashSet<Task>();
        notifications = new HashSet<Notification>();
    }


    public Board(String id) {
        this();
        this.id = id;
    }

    public boolean addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        return tasks.add(task);
    }

    public boolean removeTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        return tasks.remove(task);
    }

    public boolean updateTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        if (tasks.remove(task)) {
            tasks.add(task);
            return true;
        }
        return false;
    }

    public Set<Task> getTasks() {
        return tasks;
    }
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Task> getTasksByCategory(String categoryId) {
        CategoryFilter categoryFilter = new CategoryFilter(categoryId);
        return tasks.stream().filter(categoryFilter::filter).collect(Collectors.toSet());
    }

    public Set<Task> getTasksByTitle(String title) {
        TitleFilter titleFilter = new TitleFilter(title);
        return tasks.stream().filter(titleFilter::filter).collect(Collectors.toSet());
    }




    public Set<Task> getNotStartedTasks() {
        return tasks.stream().filter(notStartedFilter::filter).collect(Collectors.toSet());
    }
    public Set<Task> getIinProgressTasks() {
        return tasks.stream().filter(inProgressFilter::filter).collect(Collectors.toSet());
    }
    public Set<Task> getCompleteTasks() {
        return tasks.stream().filter(completeFilter::filter).collect(Collectors.toSet());
    }



    public Set<Task> getHighPriorityTasks() {
        return tasks.stream().filter(HighPriorityFilter::filter).collect(Collectors.toSet());
    }
    public Set<Task> getMediumPriorityTasks() {
        return tasks.stream().filter(mediumPriorityFilter::filter).collect(Collectors.toSet());
    }
    public Set<Task> getLowPriorityTasks() {
        return tasks.stream().filter(lowPriorityFilter::filter).collect(Collectors.toSet());
    }

    public String toString() {
        return "Board{" +
                "id='" + id + '\n' +
                ", tasks= \n \t" + tasks.stream().map(Task::toString).collect(Collectors.joining(", \n \t")) +
                ",\n notifications= \n \t" + notifications.stream().map(Notification::toString).collect(Collectors.joining(",  \n \t")) +
                '}';
    }
}