package model;

import android.support.annotation.NonNull;

/**
 * Created by pritshah on 8/10/17.
 */

public class Task implements Comparable {
    private String item;
    private int priority;

    public Task() {

    }

    public Task(String s, int p) {
        item = s;
        priority = p;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Task temp = (Task) o;
        if (this.getPriority() > temp.getPriority()) {
            return 1;
        } else {
            return -1;
        }
    }

    public void setItem(String s) {
        item = s;
    }

    public void setPriority(int p) {
        priority = p;
    }

    public String getItem() {
        return item;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return priority + ": " + item;
    }
}
