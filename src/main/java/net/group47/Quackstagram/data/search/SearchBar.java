package net.group47.Quackstagram.data.search;

import net.group47.Quackstagram.Handler;
import net.group47.Quackstagram.data.user.User;

import java.util.ArrayList;
import java.util.List;

public class SearchBar {
    List<User> results;

    public List<User> search(User currentUser, String query){
        //search for users
        results = new ArrayList<>();

        List <User> users = Handler.getDataManager().forUsers().getAsList();

        for (User user : users) {
            if (currentUser.getUsername().equals(user.getUsername()))
                continue;
            if (user.getUsername().startsWith(query)) {
                results.add(user);
            }
        }

        return results;
    }
}
