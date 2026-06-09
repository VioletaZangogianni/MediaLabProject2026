package gr.medialab.medialabproject.model;

import gr.medialab.medialabproject.service.DataService;

import java.time.LocalDate;
import java.util.List;

public class Author extends SimpleUser{

    public Author(String user_name, String user_password, String name, String surname, List<Category> categories) {
        super(user_name, user_password, name, surname, categories);
        role=Roles.AUTHOR;
    }

    public void createDocument(String title, Category category, String content) {
        if(role == Roles.ADMIN || categories.contains(category)) {
            Document new_Document = new Document(title, (this.surname + " " + this.name), category, LocalDate.now(), 1);
            Document doc = DataService.getDocument(title);
            if(doc == null) {
                new_Document.create(content);
                DataService.createDocument(new_Document);
            }

        }
        else {
            System.out.println("You Do Not Have Access");
        }
    }

    public void editDocument(Document document, String content) {
        document.update(content);
    }

    public void deleteDocument(String title) {
        DataService.deleteDocument(title);
    }

}
