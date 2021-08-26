package com.thanthu.webfluxmongoreactive.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class Vendor {
	
	@Id
	private String id;
	
	private String firstname;
	private String lastname;

}
