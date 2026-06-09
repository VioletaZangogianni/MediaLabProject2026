package gr.medialab.medialabproject;

import gr.medialab.medialabproject.model.User;
import gr.medialab.medialabproject.service.DataService;
import gr.medialab.medialabproject.service.JsonHandler;
import gr.medialab.medialabproject.service.JsonDataType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(
                getClass().getResource("/gr/medialab/medialabproject/login.fxml")
        );
        primaryStage.setTitle("MediaLab Documents - Login");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        DataService.loadData();
        File DocumentDir = new File("medialab/Doc");
        if (!DocumentDir.exists())
            DocumentDir.mkdirs();
    }

    @Override
    public void stop() throws Exception {
        DataService.saveData();
    }

    public static void main(String[] args) {
        launch(args);
    }
}