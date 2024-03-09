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
    @Setter
    private String bio;

    @Getter
    private String hashedPassword;
    @Getter
    @Setter
    private int postsCount;
    @Getter
    private List<String> rawFollowers;
    @Getter
    private List<String> rawFollowing;
    @Getter
    private List<String> rawPictures;
    @Getter
    private UUID uuid;



    public User(UUID uuid, String username, String hashedPassword, List<String> rawFollowers, List<String> rawFollowing, List<String> rawPictures, String bio, int postsCount) {
        this.uuid = uuid;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.rawFollowers = rawFollowers;
        this.rawFollowing = rawFollowing;
        this.rawPictures = rawPictures;
        this.bio = bio;
        this.postsCount = postsCount;
    }

    public void addFollowing(UUID userUuid){
        getRawFollowers().add(userUuid.toString());
    }

    public void follow(User user){
        getRawFollowing().add(user.getUuid().toString());
        user.getRawFollowers().add(this.getUuid().toString());
        Handler.getDataManager().forUsers().save();
    }

    public void addPicture(UUID pictureUuid) {
        rawPictures.add(pictureUuid.toString());
        postsCount++;
    }

    public List<Picture> getPostedPictures(){
        return rawPictures.stream()
                .map(pictureUuid -> Handler.getDataManager().forPictures().getByUUID(UUID.fromString(pictureUuid)))
                .collect(Collectors.toList());
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
        return rawFollowing.stream()
                .map(followingUuid -> Handler.getDataManager().forUsers().getByUUID(UUID.fromString(followingUuid)))
                .collect(Collectors.toList());
    }

    public List<User> getFollowers(){
        return rawFollowers.stream()
                .map(followingUuid -> Handler.getDataManager().forUsers().getByUUID(UUID.fromString(followingUuid)))
                .collect(Collectors.toList());
    }

    public ImageIcon getProfilePicture(int width, int height){
        return new ImageIcon(
                new ImageIcon("img/storage/profile/" + username + ".png")
                        .getImage()
                        .getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    public void setProfilePicture(File file, String username) {
        try {
            BufferedImage image = ImageIO.read(file);
            File outputFile = new File("img/storage/profile/" + username + ".png");
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}