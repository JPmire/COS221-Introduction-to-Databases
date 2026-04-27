package org.example.model;

public class MediaType {

    private int mediaTypeId;
    private String name;

    public MediaType() {}

    public MediaType(int mediaTypeId, String name) {
        this.mediaTypeId = mediaTypeId;
        this.name        = name;
    }

    public int getMediaTypeId()                  { return mediaTypeId; }
    public void setMediaTypeId(int mediaTypeId)  { this.mediaTypeId = mediaTypeId; }

    public String getName()                      { return name; }
    public void setName(String name)             { this.name = name; }

    @Override
    public String toString() { return name; }
}
