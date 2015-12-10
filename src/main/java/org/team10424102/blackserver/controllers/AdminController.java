package org.team10424102.blackserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team10424102.blackserver.daos.ImageRepo;
import org.team10424102.blackserver.daos.UserRepo;
import org.team10424102.blackserver.models.Image;
import org.team10424102.blackserver.models.User;
import org.team10424102.blackserver.services.UserService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
public class AdminController {
    @Autowired UserRepo userRepo;

    @Autowired ImageRepo imageRepo;

    @Autowired UserService userService;

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
            result.put(u.getProfile().getPhone(), userService.generateToken(u));
        }
        return result;
    }
}
