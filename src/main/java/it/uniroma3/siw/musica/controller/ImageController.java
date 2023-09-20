package it.uniroma3.siw.musica.controller;

import it.uniroma3.siw.musica.model.Image;
import it.uniroma3.siw.musica.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class ImageController {
	
	@Autowired
    private ImageService imageService;



	@GetMapping("/image/{id}")
	public ResponseEntity<byte[]> displayItemImage(@PathVariable("id") Long id) {
		Image img = this.imageService.findImmagineById(id);
		byte[] image = img.getBytes();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<>(image, headers, HttpStatus.OK);
	}
}
