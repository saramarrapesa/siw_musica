package it.uniroma3.siw.musica.service;

import java.io.IOException;
import java.util.*;


import it.uniroma3.siw.musica.model.Artista;
import it.uniroma3.siw.musica.model.Canzone;
import it.uniroma3.siw.musica.model.Image;
import it.uniroma3.siw.musica.model.Review;
import it.uniroma3.siw.musica.repository.ArtistaRepository;
import it.uniroma3.siw.musica.repository.CanzoneRepository;
import it.uniroma3.siw.musica.repository.ImageRepository;
import it.uniroma3.siw.musica.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CanzoneService {
	
	@Autowired
	private CanzoneRepository canzoneRepository;
	
	@Autowired
	private ArtistaRepository artistaRepository;

	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private ReviewRepository reviewRepository;

	@Transactional
	public Canzone createNewCanzone(Canzone canzone, MultipartFile multipartFile) throws IOException {
		Image imageCanzone = new Image(multipartFile.getBytes());
		this.imageRepository.save(imageCanzone);
		canzone.setImage(imageCanzone);
		return this.canzoneRepository.save(canzone);
	}
	@Transactional
	public void deleteCanzone(Long canzoneId) {
		this.canzoneRepository.deleteById(canzoneId);
	}

	@Transactional
	public Canzone findCanzoneById(Long canzoneId) {
		return this.canzoneRepository.findById(canzoneId).orElse(null);
	}
	
	public void saveCanzone(Canzone canzone) {
		this.canzoneRepository.save(canzone);
	}

	@Transactional
	public List<Canzone> findCanzoniByTitolo(String titolo) { return this.canzoneRepository.findByTitolo(titolo);}

	@Transactional
	public Iterable<Canzone> findAllCanzoni(){
		return this.canzoneRepository.findAll();
	}
	
	//metodo per aggiungere un fornitore alla lista dei fornitori di un prodotto 
	public Canzone addArtistaToCanzone(Long canzoneId, Long artistaId) {
		Artista artista = this.artistaRepository.findById(artistaId).orElse(null);
		Canzone canzone = this.canzoneRepository.findById(canzoneId).orElse(null);
		if(canzone !=null&& artista !=null) {
			Set<Artista> artisti = canzone.getArtisti();
			artisti.add(artista);
			canzone.setArtisti(artisti);
		}
		return this.canzoneRepository.save(canzone);
		
	}

	
	//metodo per rimuovere un fornitore dalla lista dei fornitori di un prodotto
	public Canzone deleteArtistaFromCanzone(Long canzoneId, Long artistaId) {
		Canzone canzone = this.canzoneRepository.findById(canzoneId).orElse(null);
		Artista artista = this.artistaRepository.findById(artistaId).orElse(null);
		if(canzone !=null && artista !=null) {
			Set<Artista> artisti = canzone.getArtisti();
			artisti.remove(artista);
			canzone.setArtisti(artisti);
		}
		return this.canzoneRepository.save(canzone);
	}


	//ogni utente può scrivere una recensione sul prodotto
	
	public void function (Model model , Canzone canzone, String username) {
		Set<Artista> fornitoriProdotto = new HashSet<>();
		if(canzone.getArtisti()!=null)
			fornitoriProdotto.addAll(canzone.getArtisti());
		fornitoriProdotto.remove(null);
		model.addAttribute("fonitoriProdotto", fornitoriProdotto);
		model.addAttribute("prodotto", canzone);
		if(username!=null && this.alreadyReviewed(canzone.getReviews(), username))
			model.addAttribute("hasNotAlreadyCommented", false);
		else
			model.addAttribute("hasNotAlreadyCommented", true);
		model.addAttribute("review", new Review());
		model.addAttribute("reviews", canzone.getReviews());
		model.addAttribute("hasReviews", !canzone.getReviews().isEmpty());

		}
	



	@Transactional
	public boolean alreadyReviewed(Set<Review> reviews , String username){
		if(reviews != null) {
			for(Review rev : reviews) {
				if(rev.getUsername().equals(username))
					return true;
			}
		}
		return false;
	}
}





