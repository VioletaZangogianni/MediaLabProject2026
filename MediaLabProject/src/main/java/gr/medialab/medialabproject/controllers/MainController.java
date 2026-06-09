package gr.medialab.medialabproject.controllers;

import gr.medialab.medialabproject.model.*;
import gr.medialab.medialabproject.service.DataService;
import gr.medialab.medialabproject.service.JsonDataType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MainController {

    @FXML private Label totalCategoriesLabel;
    @FXML private Label totalDocumentsLabel;
    @FXML private Label followedDocumentsLabel;
    @FXML private StackPane contentArea;
    @FXML private Button categoriesBtn;
    @FXML private Button usersBtn;

    public static MainController instance;
    private User currentUser;

    public void initUser(User user) {
        this.currentUser = user;
        instance = this;

        boolean isAdmin = user.getRole() == Roles.ADMIN;
        categoriesBtn.setVisible(isAdmin);
        usersBtn.setVisible(isAdmin);


        refreshStats();
        showDocuments();
    }

    public User getCurrentUser() { return currentUser; }

    public void refreshStats() {
        int cats = DataService.getList(JsonDataType.CATEGORIES).size();
        int docs = DataService.getList(JsonDataType.DOCUMENTS).size();
        int foll = currentUser.getFollowingDocs().size();
        totalDocumentsLabel.setText("Documents: " + docs);
        totalCategoriesLabel.setText("Categories: " + cats);
        followedDocumentsLabel.setText("Following: " + foll);
    }

    @FXML private void showDocuments() { loadView("documents.fxml"); }
    @FXML private void showSearch()    { loadView("search.fxml"); }
    @FXML private void showFollowed()   { loadView("followed.fxml"); }
    @FXML private void showCategories(){ loadView("categories.fxml"); }
    @FXML private void showUsers()     { loadView("users.fxml"); }

    public void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/gr/medialab/medialabproject/" + fxmlFile));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
