package gr.medialab.medialabproject.service;

import gr.medialab.medialabproject.model.Author;
import gr.medialab.medialabproject.model.User;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler {

    public static String openFile(String name, int version, boolean showPrevious) {
        String path = "medialab/Doc/" + name + "_" + version + ".txt";
        String content = null;

        try {
            content = Files.readString(Paths.get(path));

            if(showPrevious) {
                Path pathPrevious = Paths.get("medialab/Doc/" + name + "_" + (version-1) + ".txt");
                Path pathPrevious2 = Paths.get("medialab/Doc/" + name + "_" + (version-2) + ".txt");
                String contentPrevious = null, contentPrevious2 = null;

                if(Files.exists(pathPrevious)) {
                    contentPrevious = Files.readString(pathPrevious);
                } else {
                    contentPrevious = "No previous version!";
                }

                if(Files.exists(pathPrevious2)) {
                    contentPrevious2 = Files.readString(pathPrevious);
                } else {
                    contentPrevious2 = "No previous version!";
                }

                content = content + '\n' +
                        "\nPrevious Document:\n" + contentPrevious + '\n' +
                        "\nSecond Previous Document:\n" + contentPrevious2;
            }
        } catch (Exception e) {
            System.out.println("Failed to read document " + name);
        }

        return content;
    }

    public static void createFile(String name, int version, String content) {
        String path = "medialab/Doc/" + name + "_" + version + ".txt";

        try {
            Files.writeString(Paths.get(path), content);
        } catch (Exception e) {
            System.out.println("Failed to create document " + name);
        }
    }

    public static void deleteFile(String name, int version) {
        String path = "medialab/Doc/" + name + "_" + version + ".txt";

        try {
            Files.delete(Paths.get(path));
        } catch (Exception e) {
            System.out.println("Failed to delete document " + name);
        }
    }
}
