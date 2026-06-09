package gr.medialab.medialabproject.model;

import gr.medialab.medialabproject.service.DataService;

public class Category extends FileSystemData{
    private String name;

    public Category(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
