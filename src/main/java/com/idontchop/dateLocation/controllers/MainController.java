package com.idontchop.dateLocation.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
	
	@RequestMapping ("/helloWorld")
	public String helloWorld () {
		return "{\n\"message\": \"Hello from Location Service\"\n}";
	}

}
