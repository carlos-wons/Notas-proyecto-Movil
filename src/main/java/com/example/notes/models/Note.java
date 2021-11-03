package com.example.notes.models;

import java.util.ArrayList;

public class Note {
    int id;
    String title;
    String content;
    int isReminder;
    ArrayList<String[]> images;
    ArrayList<String[]> video;
    ArrayList<String[]> audio;

    public Note() {

    }

    public Note(int id, String title, String content, int isReminder, ArrayList<String[]> images, ArrayList<String[]> video, ArrayList<String[]> audio) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isReminder = isReminder;
        this.images = images;
        this.video = video;
        this.audio = audio;
    }

    public Note(int id, String title, String content, int isReminder) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isReminder = isReminder;
    }

    public Note(String title, String content, int isReminder) {
        this.title = title;
        this.content = content;
        this.isReminder = isReminder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int isReminder() {
        return isReminder;
    }

    public void setReminder(int reminder) {
        isReminder = reminder;
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

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", isReminder=" + isReminder +
                ", images=" + images +
                ", video=" + video +
                ", audio=" + audio +
                '}';
    }
}
