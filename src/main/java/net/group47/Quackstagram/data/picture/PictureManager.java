package net.group47.Quackstagram.data.picture;

import net.group47.Quackstagram.Handler;
import net.group47.Quackstagram.data.user.User;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PictureManager {


    private HashMap<Integer, Picture> pictures;

    public PictureManager(){
        this.pictures = new HashMap<>();

        load();
    }

    public Picture postPicture(User user, String caption, File selectedFile){
        try(Connection connection = Handler.getDataManager().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO posts(user_id, caption) VALUES (?, ?)");
            ps.setInt(1, user.getId());
            ps.setString(2, caption);

            ps.executeUpdate();

            PreparedStatement ps2 = connection.prepareStatement("SELECT MAX(post_id) FROM posts");
            ResultSet rs = ps2.executeQuery();
            if(rs.next()){
                int id = rs.getInt(1);
                Picture picture = new Picture(id + 1, user.getId(), caption);
                picture.uploadImage(selectedFile);
                pictures.put(picture.getId(), picture);

                return picture;
            }

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public Picture getById(int id){
        return pictures.get(id);
    }

    public List<Picture> getAsList(){
        return new ArrayList<>(pictures.values());
    }


    private void load(){
        try(Connection connection = Handler.getDataManager().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT post_id, user_id, caption FROM posts");

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("post_id");
                int userId = rs.getInt("user_id");
                String caption = rs.getString("caption");

                Picture picture = new Picture(id, userId, caption);
                pictures.put(picture.getId(), picture);
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
