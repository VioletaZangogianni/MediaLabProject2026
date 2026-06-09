package gr.medialab.medialabproject.service;

import gr.medialab.medialabproject.model.*;

import java.awt.desktop.UserSessionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataService {
    private static DataList users = new DataList();
    private static DataList documents = new DataList();
    private static DataList categories = new DataList();

    private static JsonHandler usersHandler = new JsonHandler("medialab/data/users.json", JsonDataType.USERS);
    private static JsonHandler documentsHandler = new JsonHandler("medialab/data/documents.json", JsonDataType.DOCUMENTS);
    private static JsonHandler categoriesHandler = new JsonHandler("medialab/data/categories.json", JsonDataType.CATEGORIES);

    private DataService() {}

    public static void loadData()
    {
        categoriesHandler.read_Json();
        documentsHandler.read_Json();
        usersHandler.read_Json();
    }

    public static void saveData()
    {
        categoriesHandler.write_Json();
        documentsHandler.write_Json();
        usersHandler.write_Json();
    }

    public static void createUser(User user){
        users.addtolist(user);
    }

    public static void deleteUser(String user_name){
        users.deleteItem(user_name);
    }

    public static void createDocument(Document document){
        documents.addtolist(document);
    }

    public static void deleteDocument(String title){
        FileSystemData item = documents.getItem(title);
        if (item instanceof Document d)
            d.delete();

        documents.deleteItem(title);
    }

    public static void createCategory(Category category){
        categories.addtolist(category);
    }

    public static void deleteCategory(String name){
        List<FileSystemData> temporary_Document_List = documents.getList();
        for (FileSystemData f : temporary_Document_List) {
            if (f instanceof Document d && Objects.equals(d.getCategory().getName(), name)) {
                int version = d.getVersion();
                if (version > 1) {
                    FileHandler.deleteFile(d.getName(), version - 1);
                    if (version > 2) {
                        FileHandler.deleteFile(d.getName(), version - 2);
                    }
                }
                FileHandler.deleteFile(d.getName(), version);
            }
        }

        categories.deleteItem(name);

        temporary_Document_List.removeIf(item -> item instanceof Document d
                && Objects.equals(d.getCategory().getName(), name));

        List<FileSystemData> temp_users = users.getList();
        for(FileSystemData f : temp_users){
            if(f instanceof User u){
                u.removeCategory(name);
            }
        }
    }

    public static int renameCategory(String old_name, String new_name) {
        return categories.renameItem(old_name, new_name);
    }

    public static User getUser(String name){
        FileSystemData item = users.getItem(name);
        if (item instanceof User user) {
            return user;
        }
        return null;
    }

    public static Document getDocument(String title){
        FileSystemData item = documents.getItem(title);
        if (item instanceof Document document) {
            return document;
        }
        return null;
    }

    public static Category getCategory(String name){
        FileSystemData item = categories.getItem(name);
        if (item instanceof Category category) {
            return category;
        }
        return null;
    }

    public static List<FileSystemData> getList(JsonDataType type){
        switch (type){
            case USERS:
                return users.getList();
            case DOCUMENTS:
                return documents.getList();
            case CATEGORIES:
                return categories.getList();
        }
        return null;
    }

}
