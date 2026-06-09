package gr.medialab.medialabproject.model;

import gr.medialab.medialabproject.service.DataService;
import gr.medialab.medialabproject.service.JsonDataType;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

public class Admin extends Author{
    public Admin(String user_name, String user_password, String name, String surname, List<Category> categories) {
        super(user_name, user_password, name, surname, categories);
        role=Roles.ADMIN;
    }

    public void createUser(String user_name, String user_password, String name, String surname, List<Category> categories, Roles role ) {
        switch(role){
            case ADMIN:
                DataService.createUser(new Admin(user_name, user_password, name, surname, categories));
                break;
            case AUTHOR:
                DataService.createUser(new Author(user_name, user_password, name, surname, categories));
                break;
            case SIMPLEUSER:
                DataService.createUser(new SimpleUser(user_name, user_password, name, surname, categories));
                break;
        }
    }

    public List<Category> getCategories() {
        List<FileSystemData> temp = DataService.getList(JsonDataType.CATEGORIES);
        List<Category> categories = new ArrayList<>();
        for (FileSystemData item : temp) {
            if(item instanceof Category c)
                categories.add(c);
        }
        return categories;
    }

    public void deleteUser(String user_name) {
        DataService.deleteUser(user_name);
    }

    public void createCategory(String category_name){
        DataService.createCategory(new Category (category_name));
    }

    public void deleteCategory(String category_name){
        DataService.deleteCategory(category_name);
    }

    public void renameCategory(String old_name, String new_name){
        DataService.renameCategory(old_name, new_name);
    }
}
