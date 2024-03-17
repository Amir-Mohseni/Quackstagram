package net.group47.Quackstagram;

import lombok.Getter;
import net.group47.Quackstagram.data.DataManager;
import net.group47.Quackstagram.ui.UIManager;
import net.group47.Quackstagram.util.Util;

public class Handler {

    @Getter
    private static DataManager dataManager;
    @Getter
    private static UIManager uiManager;
    @Getter
    private static Util util;

    public static void main(String[] args) {
        dataManager = new DataManager();
        uiManager = new UIManager();
        util = new Util();

        uiManager.startApp();
    }

}
