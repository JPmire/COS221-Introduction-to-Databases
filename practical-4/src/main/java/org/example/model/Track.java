package org.example.model;

import java.math.BigDecimal;

public class Track {

    private int trackId;
    private String name;
    private Integer albumId;
    private int mediaTypeId;
    private Integer genreId;
    private String composer;
    private int milliseconds;
    private Integer bytes;
    private BigDecimal unitPrice;

    public Track() {}

    public Track(int trackId, String name, Integer albumId, int mediaTypeId, Integer genreId,
                 String composer, int milliseconds, Integer bytes, BigDecimal unitPrice) {
        this.trackId      = trackId;
        this.name         = name;
        this.albumId      = albumId;
        this.mediaTypeId  = mediaTypeId;
        this.genreId      = genreId;
        this.composer     = composer;
        this.milliseconds = milliseconds;
        this.bytes        = bytes;
        this.unitPrice    = unitPrice;
    }

    public int getTrackId()                    { return trackId; }
    public void setTrackId(int trackId)        { this.trackId = trackId; }

    public String getName()                    { return name; }
    public void setName(String name)           { this.name = name; }

    public Integer getAlbumId()                { return albumId; }
    public void setAlbumId(Integer albumId)    { this.albumId = albumId; }

    public int getMediaTypeId()                { return mediaTypeId; }
    public void setMediaTypeId(int mediaTypeId){ this.mediaTypeId = mediaTypeId; }

    public Integer getGenreId()                { return genreId; }
    public void setGenreId(Integer genreId)    { this.genreId = genreId; }

    public String getComposer()                { return composer; }
    public void setComposer(String composer)   { this.composer = composer; }

    public int getMilliseconds()               { return milliseconds; }
    public void setMilliseconds(int ms)        { this.milliseconds = ms; }

    public Integer getBytes()                  { return bytes; }
    public void setBytes(Integer bytes)        { this.bytes = bytes; }

    public BigDecimal getUnitPrice()           { return unitPrice; }
    public void setUnitPrice(BigDecimal price) { this.unitPrice = price; }
}
