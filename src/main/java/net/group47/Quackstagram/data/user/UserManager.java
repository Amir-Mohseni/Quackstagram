package net.group47.Quackstagram.data.user;


import lombok.Getter;
import lombok.Setter;
import net.group47.Quackstagram.Handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserManager {

    private HashMap<Integer, User> users;

    @Getter
    @Setter
    private User currentUser;


    public UserManager(){
        this.users = new HashMap<>();

        load();
    }

    public User auth(String username, String password){
        User authenticatedUser = users.values().stream()
                .filter(user -> password.equals(user.getPassword()) && user.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);

        setCurrentUser(authenticatedUser);
        return authenticatedUser;
    }

    public boolean exists(String username){
        return users.values().stream()
                .anyMatch(user -> user.getUsername().equalsIgnoreCase(username));
    }

    public User registerUser(String username, String password, String bio){

        try(Connection connection = Handler.getDataManager().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO users(username, password, bio) VALUES (?, ?, ?)");

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, bio);

            ps.executeUpdate();

            PreparedStatement ps2 = connection.prepareStatement("SELECT MAX(user_id) FROM users");
            ResultSet rs = ps2.executeQuery();
            if(rs.next()){

                User user = new User(rs.getInt(1) + 1, username, password, bio, 0, 0, 0);
                users.put(user.getId(), user);

                return user;

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public User getById(int id){
        return users.get(id);
    }

    public List<User> getAsList(){
        return new ArrayList<>(users.values());
    }

    private void load(){
        try(Connection connection = Handler.getDataManager().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM users");

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("user_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String bio = rs.getString("bio");
                int numFollowers = rs.getInt("num_followers");
                int numFollowing = rs.getInt("num_following");
                int numPosts = rs.getInt("num_posts");

                User user = new User(id, username, password, bio, numFollowers, numFollowing, numPosts);

                users.put(user.getId(), user);
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
