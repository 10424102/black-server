package org.projw.blackserver.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.projw.blackserver.models.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserDeserializer extends JsonDeserializer<User> {

    private static final Logger logger = LoggerFactory.getLogger(UserDeserializer.class);

    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        String phone = root.get("phone").asText();
        String realName = root.get("realName").asText();
        String username = root.get("username").asText();
        String email = root.get("email").asText();
        String birthday = root.get("birthday").asText();
        Date date = null;
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            date = format.parse(birthday);
        } catch (ParseException e) {
            logger.warn("parse from [birthday] error");
        }
        String collegeName = root.get("college").asText();
        String academyName = root.get("academy").asText();
        String grade = root.get("grade").asText();
        String signature = root.get("signature").asText();
        String hometown = root.get("hometown").asText();
        String highschool = root.get("highschool").asText();
        String gender = root.get("gender").asText();

        User user = new User();
        user.setUsername(phone);
        user.setEmail(email);
//
//        Profile profile = new Profile();
//        profile.setPhone(username);
//        profile.setBirthday(date);
//        profile.setRealName(realName);
//
//        College college = new College();
//        college.setName(collegeName);
//        profile.setCollege(college);
//
//        Academy academy = new Academy();
//        academy.setName(academyName);
//        profile.setAcademy(academy);
//
//        switch (gender) {
//            case "MALE":
//                profile.setGender(Gender.MALE);
//                break;
//            case "FEMALE":
//                profile.setGender(Gender.FEMALE);
//                break;
//            case "SECRET":
//                profile.setGender(Gender.SECRET);
//                break;
//            default:
//                throw new IOException("unknown gender type of " + gender);
//        }
//
//        profile.setGrade(grade);
//        profile.setSignature(signature);
//        profile.setHometown(hometown);
//        profile.setHighschool(highschool);
//        user.setProfile(profile);

        return user;
    }
}
