package net.group47.Quackstagram;

import lombok.Getter;
import net.group47.Quackstagram.data.DataManager;
import net.group47.Quackstagram.data.user.User;
import net.group47.Quackstagram.ui.UIManager;
import net.group47.Quackstagram.ui.UIUtil;
import net.group47.Quackstagram.ui.type.SignInUI;
import net.group47.Quackstagram.util.Hash;

import javax.swing.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class Handler {

    @Getter
    private static String path;

    @Getter
    private static DataManager dataManager;
    @Getter
    private static UIManager uiManager;
    @Getter
    private static Hash hash;

    public static void main(String[] args) {
        try {
            File file = new File(
                    Handler.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            path = file.getParentFile().getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        dataManager = new DataManager();
        uiManager = new UIManager();
        hash = new Hash();

        uiManager.startApp();
    }

}
