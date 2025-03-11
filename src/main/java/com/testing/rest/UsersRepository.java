package com.testing.rest;

import org.bson.types.ObjectId;

import jakarta.data.repository.BasicRepository;
import jakarta.data.repository.Repository;

@Repository
public interface UsersRepository extends BasicRepository<Users, ObjectId> {
    Users findByEmail(String email);
}