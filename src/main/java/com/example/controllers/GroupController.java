package com.example.controllers;

import com.example.config.jsonviews.GroupView;
import com.example.daos.GroupRepo;
import com.example.daos.UserRepo;
import com.example.models.UserGroup;
import com.example.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupController {
    @Autowired
    GroupRepo groupRepo;

    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    @RequestMapping(value = "/api/groups/{id}", method = RequestMethod.GET)
    @JsonView(GroupView.GroupSummary.class)
    public ResponseEntity<?> getGroupDetails(@PathVariable("id") Long id) {
        UserGroup group = groupRepo.findOne(id);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(group, HttpStatus.OK);
    }
}
