package net.group47.Quackstagram.data;

import lombok.Getter;
import net.group47.Quackstagram.data.picture.PictureManager;
import net.group47.Quackstagram.data.user.UserManager;

public class DataManager {

    private PictureManager pictureManager;
    private UserManager userManager;
    public DataManager(){
        this.pictureManager = new PictureManager();
        this.userManager = new UserManager();

        Runtime.getRuntime().addShutdownHook(new Thread(this::saveAll));
    }

    public void saveAll(){
        pictureManager.save();
        userManager.save();
    }

    public PictureManager forPictures(){
        return pictureManager;
    }

    public UserManager forUsers(){
        return userManager;
    }
}
