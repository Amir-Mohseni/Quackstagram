package net.group47.Quackstagram.data.picture;

import lombok.Getter;
import lombok.Setter;
import net.group47.Quackstagram.Handler;
import net.group47.Quackstagram.data.user.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

// Represents a picture on Quackstagram
public class Picture{

    @Getter
    private int id, postedById;
    @Getter
    private String caption;


    public Picture(int id, int postedById, String caption){
        this.id = id;
        this.postedById = postedById;
        this.caption = caption;
    }

    public Picture uploadImage(File file){
        if(!Handler.getUtil().isPhoto(file))
            return null;

        try {
            Files.copy(file.toPath(), Paths.get("img", "uploaded", id + ".png"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public ImageIcon getImage(int width, int height){
        File path = Paths.get("img", "uploaded", id + ".png").toFile();
        ImageIcon imageIcon;
        try {
            BufferedImage originalImage = ImageIO.read(path);
            Image croppedImage = originalImage
                    .getScaledInstance(
                            Math.min(originalImage.getWidth(), width),
                            Math.min(originalImage.getHeight(), height),
                            Image.SCALE_DEFAULT);
            imageIcon = new ImageIcon(croppedImage);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return imageIcon;
    }

    public List<User> getLikes(){
        List<User> users = new ArrayList<>();

        try(Connection connection = Handler.getDataManager().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT user_id FROM likes WHERE post_id = ?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("user_id");

                users.add(Handler.getDataManager().forUsers().getById(userId));
            }


        }catch (SQLException e){
            throw new RuntimeException(e);
        }


        return users;
    }

    public HashMap<User, LocalDateTime> getLikesData(){
        HashMap<User, LocalDateTime> map = new HashMap<>();

        try(Connection connection = Handler.getDataManager().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT user_id, date FROM likes WHERE post_id = ?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("user_id");

                map.put(
                        Handler.getDataManager().forUsers().getById(userId),
                        rs.getTimestamp("date").toLocalDateTime());
            }


        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return map;
    }


    public LocalDateTime getWhenPosted(){
        try(Connection connection = Handler.getDataManager().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT date FROM posts WHERE post_id = ?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return rs.getTimestamp("date").toLocalDateTime();

        }catch (SQLException e){
            throw new RuntimeException(e);
         }

        return null;
    }

    public User getPostedBy(){
        try(Connection connection = Handler.getDataManager().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT user_id FROM posts WHERE post_id = ?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return Handler.getDataManager().forUsers().getById(rs.getInt("user_id"));

        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return null;
    }

    public void addLike(User user) {
        try(Connection connection = Handler.getDataManager().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO likes(post_id, user_id) VALUES (?, ?)");
            ps.setInt(1, id);
            ps.setInt(2, user.getId());

            ps.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}
