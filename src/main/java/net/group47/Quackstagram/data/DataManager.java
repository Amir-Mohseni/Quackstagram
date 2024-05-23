package net.group47.Quackstagram.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import net.group47.Quackstagram.data.picture.PictureManager;
import net.group47.Quackstagram.data.user.UserManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataManager {

    private PictureManager pictureManager;
    private UserManager userManager;

    @Getter
    private HikariDataSource dataSource;

    public DataManager() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://localhost:3306");
        config.setUsername("root");
        config.setPassword("alex");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);

        try(Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS quackstagram");
            ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        config.setJdbcUrl("jdbc:mysql://localhost:3306/quackstagram");
        dataSource = new HikariDataSource(config);

    }

    public void initOthers() {
        this.pictureManager = new PictureManager();
        this.userManager = new UserManager();
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PictureManager forPictures(){
        return pictureManager;
    }

    public UserManager forUsers(){
        return userManager;
    }
}
