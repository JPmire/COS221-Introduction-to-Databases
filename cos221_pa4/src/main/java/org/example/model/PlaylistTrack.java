package org.example.model;

public class PlaylistTrack {

    private int playlistId;
    private int trackId;

    public PlaylistTrack() {}

    public PlaylistTrack(int playlistId, int trackId) {
        this.playlistId = playlistId;
        this.trackId    = trackId;
    }

    public int getPlaylistId()               { return playlistId; }
    public void setPlaylistId(int playlistId){ this.playlistId = playlistId; }

    public int getTrackId()                  { return trackId; }
    public void setTrackId(int trackId)      { this.trackId = trackId; }
}
