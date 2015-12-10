package org.team10424102.blackserver.dev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class DevHelper {
    public static final String INIT_SCRIPT = "/dev/init.sql";
    public static final String CONFIGURATION_FILE = "/src/test/application.properties";
    public static final String JDBC_USERNAME = "spring.datasource.username";
    public static final String JDBC_PASSWORD = "spring.datasource.password";
    public static final String JDBC_URL = "spring.datasource.url";
    public static final String JDBC_DRIVER = "spring.datasource.driverClassName";
    private static final Logger logger = LoggerFactory.getLogger(DevHelper.class);
    private static String username;
    private static String password;
    private static String url;
    private static String driver;

    private static void loadPropertiesFromConfigurationFile() throws IOException {
        InputStream is = DevHelper.class.getResourceAsStream(CONFIGURATION_FILE);
        Properties props = new Properties();
        props.load(is);

        // find from application.properties
        username = props.getProperty(JDBC_USERNAME);
        password = props.getProperty(JDBC_PASSWORD);
        url = props.getProperty(JDBC_URL);
        driver = props.getProperty(JDBC_DRIVER);
    }

    private static void loadPropertiesFromSystemEnvironment() {
        String usernameEnv = System.getenv(JDBC_USERNAME);
        String passwordEnv = System.getenv(JDBC_PASSWORD);
        String urlEnv = System.getenv(JDBC_URL);
        String driverEnv = System.getenv(JDBC_DRIVER);
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
    }

    private static void loadPropertiesFromCommandLineArguments(String[] args) {
        if (args == null) return;
        Optional<String> _username = Arrays.stream(args)
                .filter(e -> e.contains(JDBC_USERNAME)).findFirst();
        if (_username.isPresent()) {
            username = _username.get().split("=")[1];
        }
        Optional<String> _password = Arrays.stream(args)
                .filter(e -> e.contains(JDBC_PASSWORD)).findFirst();
        if (_password.isPresent()) {
            password = _password.get().split("=")[1];
        }
        Optional<String> _url = Arrays.stream(args)
                .filter(e -> e.contains(JDBC_URL)).findFirst();
        if (_url.isPresent()) {
            url = _url.get().split("=")[1];
        }
        Optional<String> _driver = Arrays.stream(args)
                .filter(e -> e.contains(JDBC_DRIVER)).findFirst();
        if (_driver.isPresent()) {
            driver = _driver.get().split("=")[1];
        }
    }

    public static void initDb(String[] args) {
        try {
            loadPropertiesFromConfigurationFile();
            loadPropertiesFromSystemEnvironment();
            loadPropertiesFromCommandLineArguments(args);

            // connect to datebase
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, username, password);

            ScriptRunner runner = new ScriptRunner(conn, false, true);
            //runner.setLogWriter(new LogPrintWriter(logger));
            runner.setLogWriter(null); // 不显示建表语句
            runner.setErrorLogWriter(new ErrorLogPrintWriter(logger));
            runner.runScript(new BufferedReader(new InputStreamReader(DevHelper.class.getResourceAsStream(INIT_SCRIPT))));

//            //检查系统是否windows，若是则去掉盘符前面的反斜杠
//            boolean isWindows;
//            isWindows = System.getProperty("os.name").startsWith("Windows");
//
//            Statement stat = conn.createStatement();
//            String sql = "INSERT INTO T_IMAGE(data) VALUES (LOAD_FILE('%s'))";
//            String wnmAvatar = DevHelper.class.getResource("/dev/data/wnm_avatar.png").getPath();
//            String wnmBg = DevHelper.class.getResource("/dev/data/wnm_bg.png").getPath();
//            if (isWindows) {
//                wnmAvatar = wnmAvatar.substring(1);
//            }
//            stat.execute(String.format(sql, wnmAvatar));
//
//            if (isWindows) {
//                wnmBg = wnmBg.substring(1);
//            }
//            stat.execute(String.format(sql, wnmBg));
//            String sql2 = "UPDATE T_USER SET avatar_id=1 WHERE id=1";
//            stat.execute(sql2);
//            String sql3 = "UPDATE T_USER SET background_image_id=2 WHERE id=1";
//            stat.execute(sql3);
//
//            String[] gameImages = new String[]{
//                    "/dev/game_cs.png",
//                    "/dev/game_dota2.png",
//                    "/dev/game_hearthstone.png",
//                    "/dev/game_lol.png",
//                    "/dev/game_minecraft.png",
//                    "/dev/game_starcraft.png",
//                    "/dev/game_warcraft.png",
//            };
//            for (String image : gameImages) {
//                String path = DevHelper.class.getResource(image).getPath();
//                if (isWindows)
//                    path = path.substring(1);
//                stat.execute(String.format(sql, path));
//            }
//
//            int imageid;
//            for (int id = 1; id < 8; id++) {
//                imageid = id + 2;
//                String sql4 = "UPDATE T_GAME SET logo_id=" + imageid + " WHERE id=" + id;
//                stat.execute(sql4);
//            }
//
//            for (int id = 1; id < 11; id++) {
//                String sql4 = "UPDATE T_ACTIVITY SET cover_image_id=2, game_id=" + (id % 6 + 1) + " WHERE id=" + id;
//                stat.execute(sql4);
//            }

//            stat.close();
            conn.close();

            logger.debug("测试数据库初始化完毕");

        } catch (Exception e) {
            logger.debug("数据库初始化出错，服务器被迫终止", e);
            System.exit(0);
        }
    }

    private static String get(Map map, String filed) {
        Object value = map.get(filed);
        if (value != null) return value.toString();
        return null;
    }

//    private static Image insertImage(ImageRepo imageRepo, String path, String tags, int uid) throws Exception {
//        System.out.println(path);
//        InputStream is = DevHelper.class.getResourceAsStream(path);
//        byte[] data = IOUtils.toByteArray(is);
//        try {
//            MessageDigest m = MessageDigest.getInstance("MD5");
//            m.reset();
//            m.update(data);
//            m.update("salt".getBytes());
//            byte[] digest = m.digest();
//            Base64.Encoder encoder = Base64.getEncoder();
//            String hash = encoder.encodeToString(digest);
//
//            Image image = new Image();
//            image.setData(data);
//            image.setFlags(0);
//            image.setUid(uid);
//            image.setGid((long) UserGroup.GID_EMPTY);
//            image.setHash(hash);
//            image.setUsed(0);
//            image.setTags(tags);
//
//            Image savedImage = imageRepo.save(image);
//
//            if (savedImage == null) {
//                throw new PersistEntityException(Image.class);
//            }
//            return savedImage;
//        } catch (NoSuchAlgorithmException e) {
//            logger.error("系统没有 MD5 摘要算法", e);
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static Image randomeImage(Random rand, Image[] images) {
//        int length = images.length;
//        int index = rand.nextInt(length + 1);
//        if (index == length) {
//            return null;
//        }
//        return images[index];
//    }
//
//
//    public static void initDb(ApplicationContext context) {
//        try {
//            // 先插入默认图片
//            ImageRepo imageRepo = context.getBean(ImageRepo.class);
//            insertImage(imageRepo, "/dev/data/default/default_avatar.png", App.DEFAULT_AVATAR_TAG, User.UID_SYSTEM);
//            insertImage(imageRepo, "/dev/data/default/default_background.png", App.DEFAULT_BACKGROUND_TAG, User.UID_SYSTEM);
//            insertImage(imageRepo, "/dev/data/default/default_cover.png", App.DEFAULT_COVER_TAG, User.UID_SYSTEM);
//            DefaultImage defaultImageService = context.getBean(DefaultImage.class);
//            ((DefaultImageServiceImpl) defaultImageService).loadDefaultImages(imageRepo);
//
//            // 插入背景图片
//            Image[] backgrounds = new Image[6];
//            for (int i = 0; i < 6; i++) {
//                backgrounds[i] = insertImage(imageRepo, "/dev/data/backgrounds/bg" + (i+1) + ".png", "用户背景", User.UID_SYSTEM);
//            }
//
//            // 插入封面
//            Image[] covers = new Image[4];
//            for (int i = 0; i < 4; i++) {
//                covers[i] = insertImage(imageRepo, "/dev/data/covers/cover" + (i+1) + ".png", "活动封面", User.UID_SYSTEM);
//            }
//
//            Random rand = new Random();
//
//            UserRepo userRepo = context.getBean(UserRepo.class);
//            YamlReader reader = new YamlReader(new InputStreamReader(
//                    DevHelper.class.getResourceAsStream("/dev/data/test_data.yaml")));
//            Map doc = (Map) reader.read();
//            List users = (List) doc.get("users");
//
//            for (Object user : users) {
//                Map m = (Map) user;
//                String name = get(m, "name");
//                String username = get(m, "username");
//                String signature = get(m, "signature");
//                String gender = get(m, "gender");
//                String realname = get(m, "realname");
//                String email = get(m, "email");
//                String phone = get(m, "phone");
//
//                User u = new User();
//                u.setUsername(username);
//                u.getProfile().setNickname(name);
//                u.getProfile().setPhone(phone);
//                u.setEmail(email);
//                u.getProfile().setRealName(realname);
//                u.setEnabled(true);
//                if (gender != null)
//                    u.getProfile().setGender(Gender.valueOf(gender.toUpperCase()));
//                else
//                    u.getProfile().setGender(Gender.SECRET);
//                u.getProfile().setSignature(signature);
//
//                Image avatar = insertImage(imageRepo, "/dev/data/avatars/" + username + ".png", null, User.UID_SYSTEM);
//                Image background = randomeImage(rand, backgrounds);
//
//                if (avatar == null) avatar = defaultImageService.avatar();
//                if (background == null) background = defaultImageService.background();
//
//                u.getProfile().setAvatar(avatar);
//                u.getProfile().setBackgroundImage(background);
//
//                userRepo.save(u);
//
//            }
//        } catch (Exception e) {
//            logger.error("插入测试数据失败", e);
//            throw new RuntimeException("插入测试数据失败");
//        }
//
//    }

}
