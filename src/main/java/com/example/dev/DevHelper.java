package com.example.dev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

public class DevHelper {
    private static final Logger logger = LoggerFactory.getLogger(DevHelper.class);

    public static void initDb(String[] args) {
        initDbWithScript(args, "/dev/init.sql");
    }

    public static void initDbWithoutTables(String[] args) {
        initDbWithScript(args, "/dev/recreate_database.sql");
    }

    private static void initDbWithScript(String[] args, String script) {
        try {
            String username, password, url, driver;
            InputStream is = DevHelper.class.getResourceAsStream("/application.properties");
            Properties props = new Properties();
            props.load(is);

            // find from application.properties
            username = props.getProperty("spring.datasource.username");
            password = props.getProperty("spring.datasource.password");
            url = props.getProperty("spring.datasource.url");
            driver = props.getProperty("spring.datasource.driverClassName");

            // find form system env
            String usernameEnv = System.getenv("spring.datasource.username");
            String passwordEnv = System.getenv("spring.datasource.password");
            String urlEnv = System.getenv("spring.datasource.url");
            String driverEnv = System.getenv("spring.datasource.driverClassName");
            if (usernameEnv != null) {
                username = usernameEnv;
            }
            if (passwordEnv != null) {
                password = passwordEnv;
            }
            if (urlEnv != null) {
                url = urlEnv;
            }
            if (driverEnv != null) {
                driver = driverEnv;
            }

            // find from command line arguments
            Optional<String> _username = Arrays.stream(args)
                    .filter(e -> e.contains("spring.datasource.username")).findFirst();
            if (_username.isPresent()) {
                username = _username.get().split("=")[1];
            }
            Optional<String> _password = Arrays.stream(args)
                    .filter(e -> e.contains("spring.datasource.password")).findFirst();
            if (_password.isPresent()) {
                password = _password.get().split("=")[1];
            }
            Optional<String> _url = Arrays.stream(args)
                    .filter(e -> e.contains("spring.datasource.url")).findFirst();
            if (_url.isPresent()) {
                url = _url.get().split("=")[1];
            }
            Optional<String> _driver = Arrays.stream(args)
                    .filter(e -> e.contains("spring.datasource.driverClassName")).findFirst();
            if (_driver.isPresent()) {
                driver = _driver.get().split("=")[1];
            }

            // connect to datebase
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, username, password);


            // run init.sql
            ScriptRunner runner = new ScriptRunner(conn, false, true);
            //runner.setLogWriter(new LogPrintWriter(logger));
            runner.setLogWriter(null); // 不现实建表语句
            runner.setErrorLogWriter(new ErrorLogPrintWriter(logger));
            runner.runScript(new BufferedReader(new InputStreamReader(
                    DevHelper.class.getResourceAsStream(script)
            )));

            //检查系统是否windows，若是则去掉盘符前面的反斜杠
            boolean isWindows;
            isWindows = System.getProperty("os.name").startsWith("Windows");

            Statement stat = conn.createStatement();
            String sql = "INSERT INTO T_IMAGE(data) VALUES (LOAD_FILE('%s'))";
            String wnmAvatar = DevHelper.class.getResource("/dev/wnm_avatar.png").getPath();
            String wnmBg = DevHelper.class.getResource("/dev/wnm_bg.png").getPath();
            if (isWindows) {
                wnmAvatar = wnmAvatar.substring(1);
            }
            stat.execute(String.format(sql, wnmAvatar));

            if (isWindows) {
                wnmBg = wnmBg.substring(1);
            }
            stat.execute(String.format(sql, wnmBg));
            String sql2 = "UPDATE T_USER SET avatar_id=1 WHERE id=1";
            stat.execute(sql2);
            String sql3 = "UPDATE T_USER SET background_image_id=2 WHERE id=1";
            stat.execute(sql3);

            System.out.println("=~> 已经插入王尼玛的头像和背景");

            String gameCS = DevHelper.class.getResource("/dev/game_cs.png").getPath();
            if (isWindows) {
                gameCS = gameCS.substring(1);
            }
            stat.execute(String.format(sql, gameCS));
            String gameDota2 = DevHelper.class.getResource("/dev/game_dota2.png").getPath();
            if (isWindows) {
                gameDota2 = gameDota2.substring(1);
            }
            stat.execute(String.format(sql, gameDota2));
            String gameHeartStone = DevHelper.class.getResource("/dev/game_hearthstone.png").getPath();
            if (isWindows) {
                gameHeartStone = gameHeartStone.substring(1);
            }
            stat.execute(String.format(sql, gameDota2));
            String gameLOL = DevHelper.class.getResource("/dev/game_lol.png").getPath();
            if (isWindows) {
                gameLOL = gameLOL.substring(1);
            }
            stat.execute(String.format(sql, gameLOL));
            String gameMineCraft = DevHelper.class.getResource("/dev/game_minecraft.png").getPath();
            if (isWindows) {
                gameMineCraft = gameMineCraft.substring(1);
            }
            stat.execute(String.format(sql, gameMineCraft));
            String gameStarCraft = DevHelper.class.getResource("/dev/game_starcraft.png").getPath();
            if (isWindows) {
                gameStarCraft = gameStarCraft.substring(1);
            }
            stat.execute(String.format(sql, gameStarCraft));
            String gameWarCraft = DevHelper.class.getResource("/dev/game_warcraft.png").getPath();
            if (isWindows) {
                gameWarCraft = gameWarCraft.substring(1);
            }
            stat.execute(String.format(sql, gameWarCraft));

            int imageid;
            for (int id = 1; id < 8; id++) {
                imageid = id + 2;
                String sql4 = "UPDATE T_GAME SET logo_id=" + imageid + " WHERE id=" + id;
                stat.execute(sql4);
            }

            for (int id = 1; id < 11; id++) {
                String sql4 = "UPDATE T_ACTIVITY SET cover_image_id=2, game_id=" + (id % 6 + 1) + " WHERE id=" + id;
                stat.execute(sql4);
            }

            stat.close();
            conn.close();

            System.out.println("=~> 测试数据库初始化完毕 \n");

        } catch (Exception e) {
            logger.debug("数据库初始化出错，服务器被迫终止", e);
            System.exit(0);
        }
    }

}
