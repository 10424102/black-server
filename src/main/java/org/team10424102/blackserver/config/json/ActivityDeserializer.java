package org.team10424102.blackserver.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team10424102.blackserver.models.Activity;
import org.team10424102.blackserver.game.Game;
import org.team10424102.blackserver.models.UserGroup;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityDeserializer extends JsonDeserializer<Activity> {

    private static final Logger logger = LoggerFactory.getLogger(ActivityDeserializer.class);

    @Override
    public Activity deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        String title = root.get("title").asText();
        String startTime = root.get("startTime").asText();
        String registrationDeadline = root.get("registrationDeadline").asText();
        String endTime = root.get("endTime").asText();
        String location = root.get("location").asText();
        String content = root.get("content").asText();
        String type = root.get("type").asText();
        String status = root.get("status").asText();
        String gameString = root.get("game").asText();
        Long groupId = root.get("group").asLong();
        Date startTimeDate = null;
        Date endTimeDate = null;
        Date registrationDeadlineDate = null;
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
            startTimeDate = format.parse(startTime);
            endTimeDate = format.parse(endTime);
            registrationDeadlineDate = format.parse(registrationDeadline);
            logger.debug("startTime " + startTimeDate);
        } catch (ParseException e) {
            logger.warn("parse from [Date] error");
        }
        Activity activity = new Activity();
//        switch (type) {
//            case "MATCH":
//                activity.setType(Activity.Type.MATCH);
//                break;
//            case "BLACK":
//                activity.setType(Activity.Type.BLACK);
//                break;
//            default:
//                throw new IOException("unknown activity type of " + type);
//        }
//        switch (status) {
//            case "RUNNING":
//                activity.setStatus(Activity.Status.RUNNING);
//                break;
//            case "READY":
//                activity.setStatus(Activity.Status.READY);
//                break;
//            case "STOPPED":
//                activity.setStatus(Activity.Status.STOPPED);
//                break;
//            default:
//                throw new IOException("unknown activity status of " + status);
//        }

        Game game = new Game();
        game.setIdentifier(gameString);

        UserGroup group = new UserGroup();
        group.setId(groupId);

        activity.setGame(game);
        activity.setGroup(group);

        activity.setTitle(title);
        activity.setContent(content);
        activity.setLocation(location);
        activity.setStartTime(startTimeDate);
        activity.setEndTime(endTimeDate);
        activity.setRegistrationDeadline(registrationDeadlineDate);

        return activity;
    }
}
