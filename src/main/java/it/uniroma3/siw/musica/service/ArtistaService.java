package it.uniroma3.siw.musica.service;


import it.uniroma3.siw.musica.model.Artista;
import it.uniroma3.siw.musica.model.Canzone;
import it.uniroma3.siw.musica.model.Image;
import it.uniroma3.siw.musica.repository.ArtistaRepository;
import it.uniroma3.siw.musica.repository.CanzoneRepository;
import it.uniroma3.siw.musica.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ArtistaService {
	
	@Autowired
	public ArtistaRepository artistaRepository;
	@Autowired
	public CanzoneRepository canzoneRepository;
	@Autowired
	private ImageRepository imageRepository;
	
	@Transactional
	public Artista createFornitore(Artista artista,MultipartFile multipartFile) throws IOException {
		Image imageArtista = new Image(multipartFile.getBytes());
		this.imageRepository.save(imageArtista);
		artista.setImage(imageArtista);
		this.artistaRepository.save(artista);
		return artista;
	}


	public Iterable<Artista> getAllArtisti(){
		return this.artistaRepository.findAll();
	}
	
	/*@Transactional
	public List<Artista> findFornitoriNotInProdotto(Long prodottoId){
		List<Artista> fornitoriToAdd = new ArrayList<>();
		for(Artista f : this.artistaRepository.findFornitoriNotInProdotto(prodottoId)) {
			fornitoriToAdd.add(f);
		}
		return fornitoriToAdd;
	}*/

	//metodo per aggiornare un prodotto
	@Transactional
	public void saveArtista(Artista artista) {
		this.artistaRepository.save(artista);
	}
	//metodo per rimuovere un prodotto
	@Transactional
	public void deleteArtista(Long artistaId) {
		this.artistaRepository.deleteById(artistaId);
	}
	
	public Artista findArtistaById(Long artistaId) {
		return this.artistaRepository.findById(artistaId).orElse(null);
	}

	@Transactional
	public Artista addCanzoneToArtista (Long artistaId, Long canzoneId){

		Artista artista = this.artistaRepository.findById(artistaId).get();
		Canzone canzone = this.canzoneRepository.findById(canzoneId).get();

		Set<Canzone> canzoni = artista.getCanzoni();
		canzoni.add(canzone);
		artista.setCanzoni(canzoni);
		return this.artistaRepository.save(artista);
	}

	@Transactional
	public Artista removeCanzoneFromArtista (Long artistaId, Long canzoneId){

		Artista artista = this.artistaRepository.findById(artistaId).get();
		Canzone canzone = this.canzoneRepository.findById(canzoneId).get();

		Set <Canzone> canzoni = artista.getCanzoni();
		canzoni.remove (canzone);
		artista.setCanzoni(canzoni);

		return this.artistaRepository.save(artista);
	}

}
