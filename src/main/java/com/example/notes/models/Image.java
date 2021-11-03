package com.example.notes.models;

public class Image {
    private long id;
    private long idNotes;
    private String srcImage;

    public Image(long id, long idNotes, String srcImage) {
        this.id = id;
        this.idNotes = idNotes;
        this.srcImage = srcImage;
    }

    public Image(long idNotes, String srcImage) {
        this.idNotes = idNotes;
        this.srcImage = srcImage;
    }

    public Image(String srcImage) {
        this.srcImage = srcImage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdNotes() {
        return idNotes;
    }

    public void setIdNotes(long idNotes) {
        this.idNotes = idNotes;
    }

    public String getSrcImage() {
        return srcImage;
    }

    public void setSrcImage(String srcImage) {
        this.srcImage = srcImage;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", idNotes=" + idNotes +
                ", srcImage='" + srcImage + '\'' +
                '}';
    }
}
