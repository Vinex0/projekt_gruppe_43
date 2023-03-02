package com.gruppe43.moneymanager.service;

import com.gruppe43.moneymanager.domain.GroupActionController;
import com.gruppe43.moneymanager.domain.Person;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    private final GroupRepository groupRepository;


    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public GroupActionController createGroup(String title, Person username) {

        return new GroupActionController(title, username);
    }


}
