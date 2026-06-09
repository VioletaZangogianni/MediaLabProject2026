package gr.medialab.medialabproject.service;
import gr.medialab.medialabproject.model.*;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;

import javax.json.*;

public class JsonHandler {
    private String path;
    private JsonDataType data_Type;

    public JsonHandler(String path, JsonDataType data_Type){
        this.path = path;
        this.data_Type = data_Type;
    }

    public void read_Json () {
         try (InputStream is = new FileInputStream(path);
              JsonReader rdr = Json.createReader(is)) {

             //JsonObject obj = rdr.readObject();
             JsonObject obj;
             JsonArray results;
             switch (data_Type) {
                 case CATEGORIES:
                     results = rdr.readArray();
                     for (JsonString result : results.getValuesAs(JsonString.class)) {
                         DataService.createCategory(new Category(result.getString()));
                     }
                     break;
                 case USERS:
                     obj = rdr.readObject();
                     results = obj.getJsonArray("users");
                     for (JsonObject result : results.getValuesAs(JsonObject.class)) {
                         String user_Name=result.getString("user_name");
                         String user_Password=result.getString("user_password");
                         String name=result.getString("name");
                         String surname=result.getString("surname");
                         String role=result.getString("role");
                         JsonArray categories=result.getJsonArray("category");
                         List<Category> categories_list=new ArrayList<>();
                         for (JsonString category : categories.getValuesAs(JsonString.class)) {
                             if(DataService.getCategory(category.getString())!=null){
                                 categories_list.add(DataService.getCategory(category.getString()));
                             }
                         }
                         switch (role) {
                             case "Admin":
                                 DataService.createUser(new Admin(user_Name, user_Password, name, surname, categories_list));
                                 break;
                             case "Author":
                                 DataService.createUser(new Author(user_Name, user_Password, name, surname, categories_list));
                                 break;
                             case "SimpleUser":
                                 DataService.createUser(new SimpleUser(user_Name, user_Password, name, surname, categories_list));
                                 break;
                         }
                         User user=DataService.getUser(user_Name);
                         JsonArray following=result.getJsonArray("following");
                         for (JsonObject f : following.getValuesAs(JsonObject.class)) {
                             String title=f.getString("followingTitle");
                             int version=f.getInt("followingVersion");
                             if(user != null)
                                user.followDoc(title, version);
                         }
                     }
                     break;
                 case DOCUMENTS:
                     obj = rdr.readObject();
                     results = obj.getJsonArray("documents");
                     for (JsonObject result : results.getValuesAs(JsonObject.class)) {
                         String title = result.getString("title");
                         String author = result.getString("author");
                         String category = result.getString("category");
                         LocalDate date = LocalDate.parse(result.getString("date_created"));
                         int version = result.getInt("version");

                         if(DataService.getCategory(category) == null)
                             System.out.println("fatal");

                         DataService.createDocument(new Document(title, author, DataService.getCategory(category), date, version));
                     }
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

    public void write_Json() {
        try (OutputStream os = new FileOutputStream(path);
             JsonWriter writer = Json.createWriter(os)) {

            switch (data_Type) {
                case CATEGORIES:
                    JsonArrayBuilder categoriesBuilder = Json.createArrayBuilder();
                    List<FileSystemData> list_categories = DataService.getList(JsonDataType.CATEGORIES);
                    if(list_categories!=null){
                        for (FileSystemData category : list_categories) {
                            if (category instanceof Category c) {
                                categoriesBuilder.add(c.getName());
                            }
                        }
                        writer.writeArray(categoriesBuilder.build());
                    }
                    break;

                case USERS:
                    JsonArrayBuilder usersArrayBuilder = Json.createArrayBuilder();

                    List<FileSystemData> users =  DataService.getList(JsonDataType.USERS);
                    if(users == null)
                    {
                        System.out.println("users empty!");
                        return;
                    }
                    for (FileSystemData user : users) {
                        JsonArrayBuilder userCategoriesBuilder = Json.createArrayBuilder();
                        if (user instanceof User u) {
                            if(u.getCategories() != null){
                                for (Category cat : u.getCategories()) {
                                    if(cat != null) {
                                        userCategoriesBuilder.add(cat.getName());
                                    }
                                }
                            }

                            String role = "";
                            switch (u.getRole()) {
                                case Roles.ADMIN:
                                    role = "Admin";
                                    break;
                                case Roles.AUTHOR:
                                    role = "Author";
                                    break;
                                case Roles.SIMPLEUSER:
                                    role = "SimpleUser";
                                    break;
                            }
                            JsonArrayBuilder userFollowingBuilder = Json.createArrayBuilder();
                            for(User.FollowDocs d : u.getFollowing()){
                                JsonObject following = Json.createObjectBuilder()
                                        .add("followingTitle", d.getTitle())
                                        .add("followingVersion", d.getFollowingVersion())
                                        .build();

                                userFollowingBuilder.add(following);
                            }


                            JsonObject userJson = Json.createObjectBuilder()
                                    .add("user_name", u.getName())
                                    .add("user_password", u.getPassword())
                                    .add("name", u.getRealName())
                                    .add("surname", u.getSurname())
                                    .add("role", role)
                                    .add("category", userCategoriesBuilder)
                                    .add("following", userFollowingBuilder)
                                    .build();

                            usersArrayBuilder.add(userJson);
                        }
                    }

                    JsonObject usersRoot = Json.createObjectBuilder()
                            .add("users", usersArrayBuilder)
                            .build();

                    writer.writeObject(usersRoot);
                    break;

                case DOCUMENTS:
                    JsonArrayBuilder docsArrayBuilder = Json.createArrayBuilder();

                    List<FileSystemData> list_documents = DataService.getList(JsonDataType.DOCUMENTS);
                    if(list_documents!=null){
                        for (FileSystemData doc : list_documents) {
                            if (doc instanceof Document d) {

                                JsonObject docJson = Json.createObjectBuilder()
                                        .add("title", d.getName())
                                        .add("author", d.getAuthor())
                                        .add("category", d.getCategory().getName())
                                        .add("date_created", d.getDate().toString())
                                        .add("version", d.getVersion())
                                        .build();
                                docsArrayBuilder.add(docJson);
                            }
                        }
                    }

                    JsonObject docsRoot = Json.createObjectBuilder()
                            .add("documents", docsArrayBuilder)
                            .build();

                    writer.writeObject(docsRoot);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
