package com.rangers.demo.service;

import com.rangers.demo.dto.UserDto;
import org.springframework.data.crossstore.ChangeSetPersister;

public interface UserService {
    UserDto getUserById(String id) throws ChangeSetPersister.NotFoundException;
    UserDto getUserByEmail(String email) throws ChangeSetPersister.NotFoundException;
    String addUser(UserDto user);
}
