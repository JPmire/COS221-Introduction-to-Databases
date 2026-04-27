package org.example.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import org.example.dao.TrackDAO;
import org.example.model.Track;
import org.example.dao.AlbumDAO;
import org.example.dao.GenreDAO;
import org.example.dao.MediaTypeDAO;
import org.example.model.Album;
import org.example.model.Genre;
import org.example.model.MediaType;

public class AddTrackController {
    @FXML private TextField txtTrackName;
    @FXML private TextField txtComposer;
    @FXML private TextField txtMilliseconds;
    @FXML private TextField txtBytes;
    @FXML private TextField txtUnitPrice;
    @FXML private ComboBox<Album> cmbAlbum;
    @FXML private ComboBox<Genre> cmbGenre;
    @FXML private ComboBox<MediaType> cmbMediaType;

    @FXML Button btnSaveTrack;
    @FXML Button btnCancel;

    @FXML
    private void initialize() {
        // Load data from DAOs
        AlbumDAO albumDAO = new AlbumDAO();
        GenreDAO genreDAO = new GenreDAO();
        MediaTypeDAO mediaTypeDAO = new MediaTypeDAO();

        cmbAlbum.setItems(FXCollections.observableArrayList(albumDAO.getAll()));
        cmbGenre.setItems(FXCollections.observableArrayList(genreDAO.getAll()));
        cmbMediaType.setItems(FXCollections.observableArrayList(mediaTypeDAO.getAll()));

        // Set actions
        btnSaveTrack.setOnAction(event -> addTrack());
        btnCancel.setOnAction(event -> closeWindow());
    }

    private void addTrack() {
        if (txtTrackName.getText().isEmpty() || txtMilliseconds.getText().isEmpty() || txtBytes.getText().isEmpty() || txtUnitPrice.getText().isEmpty() || cmbAlbum.getValue() == null || cmbGenre.getValue() == null || cmbMediaType.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all required fields.");
            return;
        }

        try {
            TrackDAO trackDAO = new TrackDAO();
            Track track = new Track();

            track.setTrackId(trackDAO.getNextId());
            track.setName(txtTrackName.getText());
            track.setComposer(txtComposer.getText().isEmpty() ? null : txtComposer.getText());
            track.setMilliseconds(Integer.parseInt(txtMilliseconds.getText()));
            track.setBytes(Integer.parseInt(txtBytes.getText()));
            track.setUnitPrice(new BigDecimal(txtUnitPrice.getText()));
            
            track.setAlbumId(cmbAlbum.getValue().getAlbumId());
            track.setGenreId(cmbGenre.getValue().getGenreId());
            track.setMediaTypeId(cmbMediaType.getValue().getMediaTypeId());

            boolean success = trackDAO.insert(track);
            if (success) {
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add track to the database.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Milliseconds, Bytes, and Unit Price must be valid numbers.");
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
