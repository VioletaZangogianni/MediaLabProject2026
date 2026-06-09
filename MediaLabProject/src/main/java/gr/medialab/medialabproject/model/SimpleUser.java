package gr.medialab.medialabproject.model;

import gr.medialab.medialabproject.service.DataList;

import java.util.ArrayList;
import java.util.List;

public class SimpleUser extends User{
    public SimpleUser(String user_name, String user_password, String name, String surname, List<Category> categories) {
        super(user_name, user_password, name, surname, categories);
        role=Roles.SIMPLEUSER;
    }

    /*private List<FollowDocs> followedDocs = new ArrayList<>();

    public List<FollowDocs> getFollowedDocs() {
        return followedDocs;
    }

    public boolean addFollow(Document doc) {
        for (FollowDocs f : followedDocs) {
            if (f.getTitle().equals(doc.getName())) {
                return false;
            }
        }
        followedDocs.add(new FollowDoc(doc.getName(), doc.getVersion()));
        return true;
    }

    public boolean removeFollow(String title) {
        return followedDocs.removeIf(f -> f.getTitle().equals(title));
    }

    public boolean isFollowing(String title) {
        return followedDocs.stream().anyMatch(f -> f.getTitle().equals(title));
    }

    public FollowDocs getFollow(String title) {
        return followedDocs.stream()
                .filter(f -> f.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }*/
}
