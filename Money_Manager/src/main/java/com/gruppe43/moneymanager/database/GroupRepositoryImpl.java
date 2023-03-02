package com.gruppe43.moneymanager.database;

import com.gruppe43.moneymanager.domain.Group;
import com.gruppe43.moneymanager.service.GroupRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GroupRepositoryImpl implements GroupRepository {

    @Override
    public List<Group> finaAll() {
        return null;
    }

    @Override
    public Optional<Group> findById(int id) {
        return Optional.empty();
    }

    @Override
    public Group save(Group group) {
        return null;
    }
}
