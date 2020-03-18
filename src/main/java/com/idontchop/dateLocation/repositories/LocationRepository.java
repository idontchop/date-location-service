package com.idontchop.dateLocation.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.idontchop.dateLocation.entities.Location;

public interface LocationRepository extends MongoRepository<Location, String> {

}
