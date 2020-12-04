package com.example.hellospringboot.DAO;

import com.example.hellospringboot.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUsername(String username);
}