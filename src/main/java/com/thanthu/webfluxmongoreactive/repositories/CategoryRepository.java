package com.thanthu.webfluxmongoreactive.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.thanthu.webfluxmongoreactive.domain.Category;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {

}
