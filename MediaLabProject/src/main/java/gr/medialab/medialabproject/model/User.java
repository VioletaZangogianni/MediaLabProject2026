package gr.medialab.medialabproject.model;

import gr.medialab.medialabproject.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends FileSystemData{
    private String user_name;
    private String user_password;
    protected String name;
    protected String surname;
    protected List<Category> categories;
    protected Roles role;
    private List<FollowDocs> following;

    public User(String user_name, String user_password, String name, String surname, List<Category> categories) {
        this.user_name = user_name;
        this.user_password = user_password;
        this.name = name;
        this.surname = surname;
        this.categories = categories;
        this.following=new ArrayList<>();
    }

    public List<Document> getDocuments() {
        List<FileSystemData> list = DataService.getList(JsonDataType.DOCUMENTS);
        List<Document> documentList = new ArrayList<>();

        if(list == null)
            return null;

        if(!(this instanceof Admin a)) {
            for (FileSystemData item : list) {
                if(item instanceof Document d) {
                    if(categories.contains(d.getCategory()))
                        documentList.add(d);
                }
            }
        } else {
            for (FileSystemData item : list) {
                if(item instanceof Document d)
                    documentList.add(d);
            }
        }

        return documentList;
    }

    public String getName() {
        return user_name;
    }

    public void setName(String name) {
        this.user_name = name;
    }

    public Roles getRole() {
        return role;
    }

    public String getSurname() {
        return surname;
    }

    public String getRealName() {
        return name;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public String getPassword() {
        return user_password;
    }

    public void removeCategory(String name) {
        categories.removeIf(c -> c.getName().equals(name));
    }

    public int followDoc(Document d) {
        for (FollowDocs fd : following) {
            if (Objects.equals(d.getName(), fd.getTitle())){
                return -1;
            }
        }
        following.add(new FollowDocs(d));
        return 1;
    }

    public int followDoc(String title, int version) {
        for (FollowDocs fd : following) {
            if (Objects.equals(title, fd.getTitle())){
                return -1;
            }
        }
        following.add(new FollowDocs(title, version));
        return 1;
    }

    public void removeFollowDoc(Document d) {
        following.removeIf(item -> Objects.equals(d.getName(), item.getTitle()));
    }

    public class FollowDocs {
        private String title;
        private int followingVersion;

        public FollowDocs(Document d) {
            title = d.getName();
            followingVersion = d.getVersion();
        }

        public FollowDocs(String title, int followingVersion) {
            this.title = title;
            this.followingVersion = followingVersion;
        }

        public String getTitle() {
            return title;
        }

        public int getFollowingVersion() {
            return followingVersion;
        }

        public boolean needsUpdate (Document d) {
            if ((d != null) && (d.getVersion() != followingVersion)) {
                followingVersion = d.getVersion();
                return true;
            }
            return false;
        }
    }

    public List<String> deletedFollowingDocs() {
        List<String> list = new ArrayList<>();
        for (FollowDocs fd : following) {
            if(DataService.getDocument(fd.getTitle()) == null) {
                list.add(fd.getTitle());
            }
        }
        following.removeIf(item -> list.contains(item.getTitle()));
        return list;
    }

    public List<String> updatedFollowingDocs() {
        List<String> list = new ArrayList<>();
        for (FollowDocs fd : following) {
            if(DataService.getDocument(fd.getTitle()) != null ) {
                if(fd.needsUpdate(DataService.getDocument(fd.getTitle()))) {
                    list.add(fd.getTitle());
                }
            }
        }
        return list;
    }

    public List<Document> getFollowingDocs() {
        List<Document> list = new ArrayList<>();
        for (FollowDocs fd : following) {
            if(DataService.getDocument(fd.getTitle()) != null) {
                list.add(DataService.getDocument(fd.getTitle()));
            }
        }
        return list;
    }

    public List<FollowDocs> getFollowing() {
        return following;
    }

}

