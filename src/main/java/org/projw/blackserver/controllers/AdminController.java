package org.projw.blackserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.projw.blackserver.models.ActivityRepo;
import org.projw.blackserver.models.ImageRepo;
import org.projw.blackserver.models.UserRepo;
import org.projw.blackserver.models.Activity;
import org.projw.blackserver.models.Image;
import org.projw.blackserver.models.User;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
public class AdminController {
    @Autowired UserRepo userRepo;

    @Autowired ImageRepo imageRepo;

    @Autowired ActivityRepo activityRepo;

    @RequestMapping("/admin/users")
    public Set<User> getAllUsers() {
        Iterator<User> i = userRepo.findAll().iterator();
        Stream<User> s = StreamSupport.stream(Spliterators.spliteratorUnknownSize(i, Spliterator.ORDERED), false);
        Set<User> users = s.collect(Collectors.toSet());
        return users;
    }

    @RequestMapping("/admin/images")
    public Set<Image> getAllImages() {
        Iterator<Image> i = imageRepo.findAll().iterator();
        Stream<Image> s = StreamSupport.stream(Spliterators.spliteratorUnknownSize(i, Spliterator.ORDERED), false);
        Set<Image> images = s.collect(Collectors.toSet());
        return images;
    }

    @RequestMapping("/admin/tokens")
    public Map<String, String> getAllTokens() {
        Iterator<User> i = userRepo.findAll().iterator();
        Map<String, String> result = new HashMap<>();
        while (i.hasNext()) {
            User u = i.next();
            result.put(u.getPhone(), "尚未实现"); // TODO 获取特定用户的 token, 是根据 value 来查找 key
        }
        return result;
    }

    @RequestMapping("/admin/activities")
    public Set<Activity> getAllActivities() {
        Iterator<Activity> i = activityRepo.findAll().iterator();
        Stream<Activity> s = StreamSupport.stream(Spliterators.spliteratorUnknownSize(i, Spliterator.ORDERED), false);
        Set<Activity> activities = s.collect(Collectors.toSet());
        return activities;
    }
}
