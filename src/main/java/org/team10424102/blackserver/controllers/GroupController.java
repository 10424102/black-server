package org.team10424102.blackserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.team10424102.blackserver.config.json.Views;
import org.team10424102.blackserver.daos.UserRepo;
import org.team10424102.blackserver.services.UserService;
import org.team10424102.blackserver.daos.GroupRepo;
import org.team10424102.blackserver.models.UserGroup;

@RestController
public class GroupController {
    @Autowired
    GroupRepo groupRepo;

    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    @RequestMapping(value = "/api/groups/{id}", method = RequestMethod.GET)
    @JsonView(Views.Group.class)
    public ResponseEntity<?> getGroupDetails(@PathVariable("id") Long id) {
        UserGroup group = groupRepo.findOne(id);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(group, HttpStatus.OK);
    }
}
