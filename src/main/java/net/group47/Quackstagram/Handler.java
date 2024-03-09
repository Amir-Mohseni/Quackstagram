package net.group47.Quackstagram;

import lombok.Getter;
import net.group47.Quackstagram.data.DataManager;
import net.group47.Quackstagram.data.user.User;
import net.group47.Quackstagram.ui.UIManager;
import net.group47.Quackstagram.ui.UIUtil;
import net.group47.Quackstagram.ui.type.SignInUI;
import net.group47.Quackstagram.util.Hash;

import javax.swing.*;

public class Handler {

    @Getter
    private static DataManager dataManager;
    @Getter
    private static UIManager uiManager;
    @Getter
    private static Hash hash;

    public static void main(String[] args) {
        dataManager = new DataManager();
        uiManager = new UIManager();
        hash = new Hash();

        uiManager.startApp();
    }

}
