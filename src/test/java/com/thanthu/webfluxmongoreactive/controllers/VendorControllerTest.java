package com.thanthu.webfluxmongoreactive.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;

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

	@Test
	public void testCreateVendor() {
		BDDMockito.given(vendorRepository.saveAll(any(Mono.class))).willReturn(Flux.just(Vendor.builder().build()));

		Mono<Vendor> vendorToSaveMono = Mono
				.just(Vendor.builder().firstName("First Name").lastName("Last Name").build());

		webTestClient.post().uri("/api/v1/vendors").body(vendorToSaveMono, Vendor.class).exchange().expectStatus()
				.isCreated();
	}

	@Test
	public void testUpdateVendor() {

		BDDMockito.given(vendorRepository.save(any(Vendor.class))).willReturn(Mono.just(Vendor.builder().build()));

		Mono<Vendor> vendorMonoToUpdate = Mono.just(Vendor.builder().build());

		webTestClient.put().uri("/api/v1/vendors/someid").body(vendorMonoToUpdate, Vendor.class).exchange()
				.expectStatus().isOk();

	}

	@Test
	public void testPatchVendorWithChanges() {

		BDDMockito.given(vendorRepository.findById(anyString()))
				.willReturn(Mono.just(Vendor.builder().firstName("Jimmy").build()));

		BDDMockito.given(vendorRepository.save(any(Vendor.class))).willReturn(Mono.just(Vendor.builder().build()));

		Mono<Vendor> vendorMonoToUpdate = Mono.just(Vendor.builder().firstName("Jim").build());

		webTestClient.patch().uri("/api/v1/vendors/someid").body(vendorMonoToUpdate, Vendor.class).exchange()
				.expectStatus().isOk();

		BDDMockito.verify(vendorRepository).save(any());
	}

	@Test
	public void testPatchVendorWithoutChanges() {

		BDDMockito.given(vendorRepository.findById(anyString()))
				.willReturn(Mono.just(Vendor.builder().firstName("Jimmy").build()));

		Mono<Vendor> vendorMonoToUpdate = Mono.just(Vendor.builder().firstName("Jimmy").build());

		webTestClient.patch().uri("/api/v1/vendors/someid").body(vendorMonoToUpdate, Vendor.class).exchange()
				.expectStatus().isOk();

		BDDMockito.verify(vendorRepository, never()).save(any());
	}
}
