package com.tornyak.noon.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tornyak.noon.model.Directory;

@RestController
@RequestMapping("/acme")
public class DirectoryController {
	
	@Autowired
	private Directory directory;
	
	@GetMapping
    public Directory getDirectory() {
		return directory;
	}

}
