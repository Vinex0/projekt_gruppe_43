package com.gruppe43.moneymanager.service;

import com.gruppe43.moneymanager.domain.Group;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface GroupRepository {
    List<Group> finaAll();

    Optional<Group> findById(int id);

    Group save(Group group);
}
