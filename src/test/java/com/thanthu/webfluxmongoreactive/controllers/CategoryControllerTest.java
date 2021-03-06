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

import com.thanthu.webfluxmongoreactive.domain.Category;
import com.thanthu.webfluxmongoreactive.repositories.CategoryRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

	@Mock
	CategoryRepository categoryRepository;

	@InjectMocks
	CategoryController categoryController;

	WebTestClient webTestClient;

	@BeforeEach
	void setUp() throws Exception {
		webTestClient = WebTestClient.bindToController(categoryController).build();
	}

	@Test
	public void list() {
		BDDMockito.given(categoryRepository.findAll()).willReturn(Flux
				.just(Category.builder().description("Cat1").build(), Category.builder().description("Cat2").build()));

		webTestClient.get().uri("/api/v1/categories/").exchange().expectBodyList(Category.class).hasSize(2);
	}

	@Test
	public void getById() {
		BDDMockito.given(categoryRepository.findById("someid"))
				.willReturn(Mono.just(Category.builder().description("Cat").build()));

		webTestClient.get().uri("/api/v1/categories/someid").exchange().expectBody(Category.class);

	}

	@Test
	public void testCreateCateogry() {
		BDDMockito.given(categoryRepository.saveAll(any(Mono.class)))
				.willReturn(Flux.just(Category.builder().description("descrp").build()));

		Mono<Category> catToSaveMono = Mono.just(Category.builder().description("Some Cat").build());

		webTestClient.post().uri("/api/v1/categories").body(catToSaveMono, Category.class).exchange().expectStatus()
				.isCreated();
	}

	@Test
	public void testUpdateCateogry() {
		BDDMockito.given(categoryRepository.save(any(Category.class)))
				.willReturn(Mono.just(Category.builder().build()));

		Mono<Category> catToUpdateMono = Mono.just(Category.builder().description("Some Cat").build());

		webTestClient.put().uri("/api/v1/categories/asdfasdf").body(catToUpdateMono, Category.class).exchange()
				.expectStatus().isOk();
	}

	@Test
	public void testPatchWithChanges() {
		BDDMockito.given(categoryRepository.findById(anyString())).willReturn(Mono.just(Category.builder().build()));

		BDDMockito.given(categoryRepository.save(any(Category.class))).willReturn(Mono.just(Category.builder().build()));

		Mono<Category> catToUpdateMono = Mono.just(Category.builder().description("New Description").build());

		webTestClient.patch().uri("/api/v1/categories/asdfasdf").body(catToUpdateMono, Category.class).exchange()
				.expectStatus().isOk();

		BDDMockito.verify(categoryRepository).save(any());
	}

	@Test
	public void testPatchNoChanges() {
		BDDMockito.given(categoryRepository.findById(anyString())).willReturn(Mono.just(Category.builder().build()));

		Mono<Category> catToUpdateMono = Mono.just(Category.builder().build());

		webTestClient.patch().uri("/api/v1/categories/asdfasdf").body(catToUpdateMono, Category.class).exchange()
				.expectStatus().isOk();

		BDDMockito.verify(categoryRepository, never()).save(any());
	}

}
