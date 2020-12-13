package com.example.hellospringboot.DAO;

import com.example.hellospringboot.model.NewestFeed;
import com.example.hellospringboot.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NewestFeedRepository extends MongoRepository<NewestFeed, ObjectId> {
    NewestFeed[] findByUrl(String url);
}
