package com.gruppe43.moneymanager.database;

import com.gruppe43.moneymanager.domain.Group;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SpringDataGroupRepository extends CrudRepository<Group, Integer> {
    List<Group> findAll();
}
