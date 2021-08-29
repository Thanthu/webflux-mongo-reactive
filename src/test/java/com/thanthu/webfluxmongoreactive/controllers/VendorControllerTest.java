package com.thanthu.webfluxmongoreactive.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.thanthu.webfluxmongoreactive.domain.Vendor;
import com.thanthu.webfluxmongoreactive.repositories.VendorRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class VendorControllerTest {

	WebTestClient webTestClient;

	@Mock
	VendorRepository vendorRepository;

	@InjectMocks
	VendorController controller;

	@BeforeEach
	void setUp() throws Exception {
		webTestClient = WebTestClient.bindToController(controller).build();
	}

	@Test
	public void list() {

		BDDMockito.given(vendorRepository.findAll())
				.willReturn(Flux.just(Vendor.builder().firstName("Fred").lastName("Flintstone").build(),
						Vendor.builder().firstName("Barney").lastName("Rubble").build()));

		webTestClient.get().uri("/api/v1/vendors").exchange().expectBodyList(Vendor.class).hasSize(2);
	}

	@Test
	public void getById() {
		BDDMockito.given(vendorRepository.findById("someid"))
				.willReturn(Mono.just(Vendor.builder().firstName("Jimmy").lastName("Johns").build()));

		webTestClient.get().uri("/api/v1/vendors/someid").exchange().expectBody(Vendor.class);
	}
}
