package gr.medialab.medialabproject.service;

import gr.medialab.medialabproject.model.FileSystemData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataList {
    private List<FileSystemData> list = new ArrayList<>();

    public int addtolist(FileSystemData item) {
        if (getItem(item.getName())!= null) {
            return -1;
        }
        list.add(item);
        return 1;
    }

    public void deleteItem(String name){
        for (FileSystemData item:list){
            if  (Objects.equals(item.getName(), name)) {
                list.remove(item);
                return;
            }
        }
    }

    public int renameItem(String name,String new_Name){
        if (getItem(new_Name)!=null) {
            return -1;
        }
        for (FileSystemData item:list){
            if (Objects.equals(item.getName(), name)) {
                item.setName(new_Name);
            }
        }
        return 1;
    }

    public List<FileSystemData> getList(){
        return list;
    }

    public FileSystemData getItem(String name){
        for (FileSystemData item:list){
            if (Objects.equals(item.getName(), name)) {
                return item;
            }
        }
        return null;
    }

}
