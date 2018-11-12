package com.ucast.screen_program.xutlsEvents;

/**
 * Created by pj on 2018/10/31.
 */
public class RunTaskEvent {
    public Runnable task = null;
    public String msg ;

    public RunTaskEvent(Runnable task) {
        this.task = task;
    }

    public RunTaskEvent(Runnable task, String msg) {
        this.task = task;
        this.msg = msg;
    }

    public Runnable getTask() {
        return task;
    }

    public void setTask(Runnable task) {
        this.task = task;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
