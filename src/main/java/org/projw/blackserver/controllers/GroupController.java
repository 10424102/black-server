package org.projw.blackserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.projw.blackserver.config.json.Views;
import org.projw.blackserver.models.UserRepo;
import org.projw.blackserver.models.GroupRepo;
import org.projw.blackserver.models.UserGroup;

@RestController
public class GroupController {
    @Autowired GroupRepo groupRepo;

    @Autowired UserRepo userRepo;

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
