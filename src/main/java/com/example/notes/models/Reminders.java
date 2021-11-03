package com.example.notes.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Reminders {
    int id;
    String title;
    String content;
    int isReminder;
    ArrayList<String[]> images;
    ArrayList<String[]> video;
    ArrayList<String[]> audio;
    ArrayList<String> dateReminders;
    String finishDate;

    public Reminders() {
    }

    public Reminders(String title, String content, int isReminder, String finishDate) {
        this.title = title;
        this.content = content;
        this.isReminder = isReminder;
        this.finishDate = finishDate;
    }

    public Reminders(int id, String title, String content, int isReminder, String finishDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isReminder = isReminder;
        this.finishDate = finishDate;
    }

    public Reminders(int id, String title, String content, ArrayList<String> dateReminders, String finishDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateReminders = dateReminders;
        this.finishDate = finishDate;
    }

    public Reminders(int id, String title, String content, ArrayList<String[]> images, ArrayList<String[]> video, ArrayList<String[]> audio, ArrayList<String> dateReminders, String finishDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.images = images;
        this.video = video;
        this.audio = audio;
        this.dateReminders = dateReminders;
        this.finishDate = finishDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int isReminder() {
        return isReminder;
    }

    public void setReminder(int reminder) {
        isReminder = reminder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String[]> getImages() {
        return images;
    }

    public void setImages(ArrayList<String[]> images) {
        this.images = images;
    }

    public ArrayList<String[]> getVideo() {
        return video;
    }

    public void setVideo(ArrayList<String[]> video) {
        this.video = video;
    }

    public ArrayList<String[]> getAudio() {
        return audio;
    }

    public void setAudio(ArrayList<String[]> audio) {
        this.audio = audio;
    }

    public ArrayList<String> getDateReminders() {
        return dateReminders;
    }

    public void setDateReminders(ArrayList<String> dateReminders) {
        this.dateReminders = dateReminders;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    @Override
    public String toString() {
        return "Reminders{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", isReminder=" + isReminder +
                ", images=" + images +
                ", video=" + video +
                ", audio=" + audio +
                ", dateReminders=" + dateReminders +
                ", finishDate='" + finishDate + '\'' +
                '}';
    }
}
