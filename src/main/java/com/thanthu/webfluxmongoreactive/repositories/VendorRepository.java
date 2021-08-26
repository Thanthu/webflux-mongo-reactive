package com.thanthu.webfluxmongoreactive.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.thanthu.webfluxmongoreactive.domain.Vendor;

public interface VendorRepository extends ReactiveMongoRepository<Vendor, String> {

}
