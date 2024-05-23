package net.group47.Quackstagram.data.user;

import lombok.Getter;
import lombok.Setter;
import net.group47.Quackstagram.Handler;
import net.group47.Quackstagram.data.picture.Picture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

// Represents a user on Quackstagram
public class User  {
    // Getter methods for user details
    @Getter
    private String username;
    @Getter
    private String bio;
    @Getter
    private String password;
    @Getter
    private int postsCount, followersCount, followingCount;
    @Getter
    private int id;

    public User(int id, String username, String password, String bio, int followersCount, int followingCount, int postsCount) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.postsCount = postsCount;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
    }

    public void follow(User user){
        try(Connection connection = Handler.getDataManager().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO follows(follower_id, followed_id) VALUES (?, ?)");
            ps.setInt(1, getId());
            ps.setInt(2, user.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Picture> getPostedPictures(){
        List<Picture> pictures = new ArrayList<>();

        try(Connection connection = Handler.getDataManager().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT post_id FROM posts WHERE user_id = ?");
            ps.setInt(1, getId());

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int postId = rs.getInt("post_id");
                pictures.add(Handler.getDataManager().forPictures().getById(postId));
            }

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return pictures;
    }

    public HashMap<User, LocalDateTime> getNotificationsSorted(){
        List<Map.Entry<User, LocalDateTime>> entryList = new ArrayList<>();

        for(Picture picture : getPostedPictures()){
            entryList.addAll(picture.getLikesData().entrySet());
        }

        entryList.sort((e1, e2) -> {
            return e2.getValue().compareTo(e1.getValue()); //reverse order from the latest to earliest
        });

        HashMap<User, LocalDateTime> map = new HashMap<>();
        for(Map.Entry<User, LocalDateTime> entry : entryList)
            map.put(entry.getKey(), entry.getValue());

        return map;
    }

    public List<User> getFollowing(){
        List<User> users = new ArrayList<>();

        try(Connection connection = Handler.getDataManager().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT followed_id FROM follows WHERE follower_id = ?");
            ps.setInt(1, getId());

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int followingId = rs.getInt("followed_id");
                users.add(Handler.getDataManager().forUsers().getById(followingId));
            }

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    public List<User> getFollowers(){
        List<User> users = new ArrayList<>();

        try(Connection connection = Handler.getDataManager().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT follower_id FROM follows WHERE followed_id = ?");
            ps.setInt(1, getId());

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int followingId = rs.getInt("follower_id");
                users.add(Handler.getDataManager().forUsers().getById(followingId));
            }

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    public ImageIcon getProfilePicture(int width, int height){
        return new ImageIcon(
                new ImageIcon(Paths.get("img", "storage", "profile",  username + ".png").toString())
                        .getImage()
                        .getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    public void setProfilePicture(File file) {
        if(!Handler.getUtil().isPhoto(file))
            return;

        try {
            BufferedImage image = ImageIO.read(file);
            File outputFile = Paths.get("img", "storage", "profile",  username + ".png").toFile();
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof User)) return false;
        User other = (User) obj;

        return other.id == id;
    }
}