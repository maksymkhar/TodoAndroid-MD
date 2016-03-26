package com.iesebre.dam2.max.todosandroidmd.models;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by max on 13/11/15.
 */
public class TodoItem implements Serializable{

    private String name;
    private boolean done;
    private int priority;
    private String description;

    public TodoItem() {
        this.name = "-";
        this.done = false;
        this.priority = 1;
        this.description = "-";
    }

    public TodoItem(String name, boolean done, int priority, String description) {
        this.name = name;
        this.done = done;
        this.priority = priority;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "{\"name\": " + name + ",\"description\": \"" + description + "\", \"done\": " + done + ", \"priority\": " + priority + "}";
    }

    public String serialize()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
