package gr.medialab.medialabproject.controllers;

import gr.medialab.medialabproject.model.*;
import gr.medialab.medialabproject.service.DataService;
import gr.medialab.medialabproject.service.JsonDataType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.stream.Collectors;

public class SearchController {

    @FXML private TextField searchTitle;
    @FXML private TextField searchAuthor;
    @FXML private ComboBox<String> searchCategory;
    @FXML private TableView<Document> resultsTable;
    @FXML private TableColumn<Document, String> rTitleCol;
    @FXML private TableColumn<Document, String> rAuthorCol;
    @FXML private TableColumn<Document, String> rCategoryCol;
    @FXML private TableColumn<Document, String> rDateCol;
    @FXML private TableColumn<Document, Integer> rVersionCol;

    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = MainController.instance.getCurrentUser();

        rTitleCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        rAuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        rCategoryCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCategory().getName()));
        rDateCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDate().toString()));
        rVersionCol.setCellValueFactory(new PropertyValueFactory<>("version"));

        List<String> userCats = currentUser.getCategories()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toList());
        userCats.add(0, "Any");
        searchCategory.setItems(FXCollections.observableArrayList(userCats));
        searchCategory.setValue("Any");

        handleSearch();
    }

    @FXML
    private void handleSearch() {
        String title  = searchTitle.getText().toLowerCase().trim();
        String author = searchAuthor.getText().toLowerCase().trim();
        String cat    = searchCategory.getValue();

        List<Document> results = DataService.getList(JsonDataType.DOCUMENTS)
                .stream()
                .filter(d -> d instanceof Document)
                .map(d -> (Document) d)

                .filter(d -> currentUser.getCategories() == null ||
                        currentUser.getCategories().contains(d.getCategory()))

                .filter(d -> d.getName().toLowerCase().contains(title))
                .filter(d -> d.getAuthor().toLowerCase().contains(author))
                .filter(d -> "Any".equals(cat) ||
                        d.getCategory().getName().equals(cat))
                .collect(Collectors.toList());

        resultsTable.setItems(FXCollections.observableArrayList(results));
    }
}
