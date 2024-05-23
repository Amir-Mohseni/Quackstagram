package net.group47.Quackstagram;

import lombok.Getter;
import net.group47.Quackstagram.data.DataManager;
import net.group47.Quackstagram.ui.UIManager;
import net.group47.Quackstagram.util.Util;
import net.group47.Quackstagram.util.db.DBUtil;

public class Handler {

    @Getter
    private static DataManager dataManager;
    @Getter
    private static UIManager uiManager;
    @Getter
    private static Util util;
    @Getter
    private static DBUtil dbUtil;

    public static void main(String[] args) {
        dataManager = new DataManager();
        dataManager.initOthers();

        uiManager = new UIManager();
        util = new Util();
        dbUtil = new DBUtil();


        uiManager.startApp();
    }

}
