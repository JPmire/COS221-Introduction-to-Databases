package org.example.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.dao.CustomerDAO;
import org.example.dao.EmployeeDAO;
import org.example.dao.GenreDAO;
import org.example.dao.TrackDAO;
import org.example.dao.InvoiceDAO;
import org.example.model.Customer;
import org.example.model.Employee;
import org.example.model.GenreRevenue;
import org.example.model.Track;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class MainController {

    // 1. Link the FXML elements (Make sure these perfectly match your fx:id's in Scene Builder)
    @FXML private TextField txtEmployeeFilter;
    @FXML private TableView<Employee> tblEmployee;
    @FXML private TableColumn<Employee, String> colEmpFirstName;
    @FXML private TableColumn<Employee, String> colEmpLastName;
    @FXML private TableColumn<Employee, String> colEmpTitle;
    @FXML private TableColumn<Employee, String> colEmpCity;
    @FXML private TableColumn<Employee, String> colEmpCountry;
    @FXML private TableColumn<Employee, String> colEmpPhone;
    @FXML private TableColumn<Employee, String> colEmpReportsTo;
    @FXML private TableColumn<Employee, String> colEmpActive;
    
    @FXML private Button btnAddTrack;

    @FXML private TableView<Track> tblTracks;
    @FXML private TableColumn<Track, String> colTrackName;
    @FXML private TableColumn<Track, Integer> colTrackAlbum;
    @FXML private TableColumn<Track, Integer> colTrackGenre;
    @FXML private TableColumn<Track, Integer> colTrackMediaType;
    @FXML private TableColumn<Track, String> colTrackComposer;
    @FXML private TableColumn<Track, BigDecimal> colTrackUnitPrice;

    @FXML private Tab tabReport;
    @FXML private TableView<GenreRevenue> tblGenreRevenue;
    @FXML private TableColumn<GenreRevenue, String> colGenreName;
    @FXML private TableColumn<GenreRevenue, BigDecimal> colGenreRevenue;

    // Customer CRUD elements
    @FXML private TextField txtCustFirstName;
    @FXML private TextField txtCustLastName;
    @FXML private TextField txtCustEmail;
    @FXML private TextField txtCustPhone;
    @FXML private TextField txtCustCountry;
    @FXML private Button btnCustCreate;
    @FXML private Button btnCustUpdate;
    @FXML private Button btnCustDelete;
    @FXML private Button btnCustClear;
    @FXML private TableView<Customer> tblCustomers;
    @FXML private TableColumn<Customer, String> colCustFirstName;
    @FXML private TableColumn<Customer, String> colCustLastName;
    @FXML private TableColumn<Customer, String> colCustEmail;
    @FXML private TableColumn<Customer, String> colCustPhone;
    @FXML private TableColumn<Customer, String> colCustCountry;

    // Inactive Customers elements
    @FXML private TextField txtInactiveCustFilter;
    @FXML private TableView<Customer> tblInactiveCustomers;
    @FXML private TableColumn<Customer, String> colInactCustFirstName;
    @FXML private TableColumn<Customer, String> colInactCustLastName;
    @FXML private TableColumn<Customer, String> colInactCustEmail;
    @FXML private TableColumn<Customer, String> colInactCustPhone;

    // Customer Recommendations elements
    @FXML private ComboBox<Customer> cmbCustomerSelect;
    @FXML private Label lblTotalSpent;
    @FXML private Label lblTotalPurchases;
    @FXML private Label lblRecentPurchase;
    @FXML private Label lblFavGenre;
    @FXML private TableView<Track> tblRecommendations;
    @FXML private TableColumn<Track, String> colRecTrackName;
    @FXML private TableColumn<Track, Integer> colRecAlbum;
    @FXML private TableColumn<Track, Integer> colRecGenre;

    // 2. Instantiate your DAO
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private TrackDAO trackDAO = new TrackDAO();
    private GenreDAO genreDAO = new GenreDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    
    private Customer selectedCustomer;

    // 3. The initialize() method runs automatically when the app starts
    @FXML
    public void initialize() {
        // Tell each column exactly which variable to look for in the Employee.java class
        colEmpFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colEmpLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmpTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colEmpCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colEmpCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        colEmpPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmpReportsTo.setCellValueFactory(cellData -> {
            Employee emp = cellData.getValue();
            if (emp.getReportsTo() == null || emp.getReportsTo() == 0) {
                return new SimpleStringProperty("");
            }
            String supervisorName = emp.getSupervisorName();
            if (supervisorName == null || supervisorName.isEmpty()) {
                return new SimpleStringProperty(String.valueOf(emp.getReportsTo()));
            }
            return new SimpleStringProperty(emp.getReportsTo() + " (" + supervisorName + ")");
        });
        colEmpActive.setCellValueFactory(new PropertyValueFactory<>("active"));

        colTrackName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colTrackAlbum.setCellValueFactory(new PropertyValueFactory<>("albumId"));
        colTrackGenre.setCellValueFactory(new PropertyValueFactory<>("genreId"));
        colTrackMediaType.setCellValueFactory(new PropertyValueFactory<>("mediaTypeId"));
        colTrackComposer.setCellValueFactory(new PropertyValueFactory<>("composer"));
        colTrackUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        colGenreName.setCellValueFactory(new PropertyValueFactory<>("genreName"));
        colGenreRevenue.setCellValueFactory(new PropertyValueFactory<>("totalRevenue"));

        // Setup Customer columns
        colCustFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colCustLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colCustEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCustPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colCustCountry.setCellValueFactory(new PropertyValueFactory<>("country"));

        // Setup Inactive Customer columns
        colInactCustFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colInactCustLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colInactCustEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colInactCustPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Setup Customer Recommendations columns
        colRecTrackName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colRecAlbum.setCellValueFactory(new PropertyValueFactory<>("albumId"));
        colRecGenre.setCellValueFactory(new PropertyValueFactory<>("genreId"));

        // Setup Customer ComboBox listener
        if (cmbCustomerSelect != null) {
            cmbCustomerSelect.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    updateCustomerInsights(newVal);
                } else {
                    clearCustomerInsights();
                }
            });
        }

        // Load the data into the table
        refreshEmployeeTable();
        refreshTracksTable();
        refreshCustomerTable();
        refreshInactiveCustomerTable();
        
        // Task 4.3: Add New Track button click event
        btnAddTrack.setOnAction(event -> openAddTrackPopup());

        // Task 4.4: Genre Revenue Report Tab event
        if (tabReport != null) {
            tabReport.setOnSelectionChanged(event -> {
                if (tabReport.isSelected()) {
                    refreshGenreRevenueTable();
                }
            });
        }

        // Setup Customer CRUD actions
        btnCustCreate.setOnAction(e -> createCustomer());
        btnCustUpdate.setOnAction(e -> updateCustomer());
        btnCustDelete.setOnAction(e -> deleteCustomer());
        btnCustClear.setOnAction(e -> clearCustomerForm());

        tblCustomers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedCustomer = newSelection;
                txtCustFirstName.setText(selectedCustomer.getFirstName());
                txtCustLastName.setText(selectedCustomer.getLastName());
                txtCustEmail.setText(selectedCustomer.getEmail());
                txtCustPhone.setText(selectedCustomer.getPhone());
                txtCustCountry.setText(selectedCustomer.getCountry());
            }
        });
    }

    private void updateCustomerInsights(Customer customer) {
        int customerId = customer.getCustomerId();
        
        // 1. Total amount spent
        BigDecimal totalSpent = invoiceDAO.getTotalSpentByCustomer(customerId);
        lblTotalSpent.setText(totalSpent != null ? "$" + String.format("%.2f", totalSpent) : "$0.00");
        
        // 2. Total purchases
        int totalPurchases = invoiceDAO.getPurchaseCountByCustomer(customerId);
        lblTotalPurchases.setText(String.valueOf(totalPurchases));
        
        // 3. Most recent purchase
        LocalDateTime recentPurchase = invoiceDAO.getLastPurchaseDateByCustomer(customerId);
        if (recentPurchase != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            lblRecentPurchase.setText(recentPurchase.format(formatter));
        } else {
            lblRecentPurchase.setText("N/A");
        }
        
        // 4. Favourite Genre
        String favGenre = invoiceDAO.getFavouriteGenreByCustomer(customerId);
        lblFavGenre.setText(favGenre != null ? favGenre : "None");
        
        // 5. Track Recommendations
        List<Track> recommendations = trackDAO.getRecommendationsForCustomer(customerId, 10);
        tblRecommendations.setItems(FXCollections.observableArrayList(recommendations));
    }

    private void clearCustomerInsights() {
        if (lblTotalSpent != null) lblTotalSpent.setText("$0.00");
        if (lblTotalPurchases != null) lblTotalPurchases.setText("0");
        if (lblRecentPurchase != null) lblRecentPurchase.setText("N/A");
        if (lblFavGenre != null) lblFavGenre.setText("None");
        if (tblRecommendations != null) tblRecommendations.setItems(FXCollections.observableArrayList());
    }

    private void openAddTrackPopup() {
        try {
            // Load the FXML for the popup. This will AUTOMATICALLY instantiate your AddTrackController!
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/AddTrackPopup.fxml"));
            Parent root = loader.load();

            // Create a new Stage (window) for the popup
            Stage popupStage = new Stage();
            popupStage.setTitle("Add New Track");
            popupStage.setScene(new Scene(root));
            
            // Set modality so the user can't click the main window while the popup is open
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Show the popup and pause the main thread until the popup is closed
            popupStage.showAndWait();

            // When the code reaches here, the popup was just closed.
            // You should refresh your tracks table here!
            refreshTracksTable(); 

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 4. The method that actually fetches data from the DAO and puts it in the table
    public void refreshEmployeeTable() {
        // Get the standard Java List from your DAO
        List<Employee> employeeList = employeeDAO.getAll();

        // Convert it to a JavaFX ObservableList (which is required for TableViews)
        ObservableList<Employee> observableData = FXCollections.observableArrayList(employeeList);

        // Wrap the ObservableList in a FilteredList (initially display all data)
        FilteredList<Employee> filteredData = new FilteredList<>(observableData, b -> true);

        // Set the filter Predicate whenever the filter changes
        txtEmployeeFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(employee -> {
                // If filter text is empty, display all employees.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name, last name and city of every employee with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (employee.getFirstName() != null && employee.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (employee.getLastName() != null && employee.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (employee.getCity() != null && employee.getCity().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches city.
                }
                
                return false; // Does not match.
            });
        });

        // Wrap the FilteredList in a SortedList
        SortedList<Employee> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList comparator to the TableView comparator
        // Otherwise, sorting the TableView would have no effect
        sortedData.comparatorProperty().bind(tblEmployee.comparatorProperty());

        // Add sorted (and filtered) data to the table
        tblEmployee.setItems(sortedData);
    }

    public void refreshTracksTable() {
        List<Track> trackList = trackDAO.getAll();
        ObservableList<Track> observableData = FXCollections.observableArrayList(trackList);
        tblTracks.setItems(observableData);
    }

    public void refreshGenreRevenueTable() {
        List<GenreRevenue> list = genreDAO.getGenreRevenueReport();
        ObservableList<GenreRevenue> observableData = FXCollections.observableArrayList(list);
        tblGenreRevenue.setItems(observableData);
    }

    public void refreshCustomerTable() {
        List<Customer> customers = customerDAO.getAll();
        tblCustomers.setItems(FXCollections.observableArrayList(customers));
        
        // Also update the combobox in the recommendations tab
        if (cmbCustomerSelect != null) {
            Customer selected = cmbCustomerSelect.getValue();
            cmbCustomerSelect.setItems(FXCollections.observableArrayList(customers));
            if (selected != null) {
                cmbCustomerSelect.getItems().stream()
                        .filter(c -> c.getCustomerId() == selected.getCustomerId())
                        .findFirst()
                        .ifPresent(c -> cmbCustomerSelect.getSelectionModel().select(c));
            }
        }
    }
    
    public void refreshInactiveCustomerTable() {
        List<Customer> inactiveCustomers = customerDAO.getInactiveCustomers();
        ObservableList<Customer> observableData = FXCollections.observableArrayList(inactiveCustomers);
        FilteredList<Customer> filteredData = new FilteredList<>(observableData, b -> true);

        txtInactiveCustFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (customer.getFirstName() != null && customer.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (customer.getLastName() != null && customer.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (customer.getEmail() != null && customer.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                
                return false;
            });
        });

        SortedList<Customer> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblInactiveCustomers.comparatorProperty());
        tblInactiveCustomers.setItems(sortedData);
    }

    private void clearCustomerForm() {
        selectedCustomer = null;
        txtCustFirstName.clear();
        txtCustLastName.clear();
        txtCustEmail.clear();
        txtCustPhone.clear();
        txtCustCountry.clear();
        tblCustomers.getSelectionModel().clearSelection();
    }

    private void createCustomer() {
        if (!validateCustomerForm()) return;
        
        Customer c = new Customer();
        c.setCustomerId(customerDAO.getNextId());
        c.setFirstName(txtCustFirstName.getText());
        c.setLastName(txtCustLastName.getText());
        c.setEmail(txtCustEmail.getText());
        c.setPhone(txtCustPhone.getText());
        c.setCountry(txtCustCountry.getText());
        
        if (customerDAO.insert(c)) {
            refreshCustomerTable();
            refreshInactiveCustomerTable();
            clearCustomerForm();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Customer created successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to create customer.");
        }
    }

    private void updateCustomer() {
        if (selectedCustomer == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a customer to update.");
            return;
        }
        if (!validateCustomerForm()) return;

        selectedCustomer.setFirstName(txtCustFirstName.getText());
        selectedCustomer.setLastName(txtCustLastName.getText());
        selectedCustomer.setEmail(txtCustEmail.getText());
        selectedCustomer.setPhone(txtCustPhone.getText());
        selectedCustomer.setCountry(txtCustCountry.getText());

        if (customerDAO.update(selectedCustomer)) {
            refreshCustomerTable();
            refreshInactiveCustomerTable();
            clearCustomerForm();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Customer updated successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update customer.");
        }
    }

    private void deleteCustomer() {
        if (selectedCustomer == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a customer to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Confirmation");
        confirm.setHeaderText("Delete Customer");
        confirm.setContentText("Are you sure you want to delete customer: " + selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName() + "?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (customerDAO.delete(selectedCustomer.getCustomerId())) {
                refreshCustomerTable();
                refreshInactiveCustomerTable();
                clearCustomerForm();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer deleted successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete customer. Ensure they have no dependent records.");
            }
        }
    }

    private boolean validateCustomerForm() {
        if (txtCustFirstName.getText().trim().isEmpty() || txtCustLastName.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "First Name and Last Name are required.");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
