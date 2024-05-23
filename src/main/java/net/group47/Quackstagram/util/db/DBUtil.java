package net.group47.Quackstagram.util.db;

import net.group47.Quackstagram.Handler;

import java.sql.Connection;
import java.sql.SQLException;

public class DBUtil {

    public DBUtil() {

    }


    public void runQuery(String... query) {

        try(Connection connection = Handler.getDataManager().getConnection()) {
            for(String str : query) {
                connection.prepareStatement(str).execute();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
