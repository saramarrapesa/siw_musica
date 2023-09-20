package it.uniroma3.siw.musica.controller;

import it.uniroma3.siw.musica.model.Canzone;
import it.uniroma3.siw.musica.model.Review;
import it.uniroma3.siw.musica.service.ArtistaService;
import it.uniroma3.siw.musica.service.CanzoneService;
import it.uniroma3.siw.musica.validator.CanzoneValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class CanzoneController {
	
	@Autowired
	public CanzoneService canzoneService;
	
	@Autowired
	public ArtistaService artistaService;
	
	@Autowired
	public CanzoneValidator canzoneValidator;

	@Autowired
	public GlobalController globalController;



	//metodo per visualizzare tutti i prodotti

	@GetMapping("/canzoni")
	public  String getCanzoni(Model model, String titolo){
		if(titolo !=null){
			model.addAttribute("canzoni", this.canzoneService.findCanzoniByTitolo(titolo));
		}
		else{
		model.addAttribute("canzoni", this.canzoneService.findAllCanzoni());
		}
		return "canzoni";
	}

	//metodo per visualizzare un particolare prodotto in base all'id

	@GetMapping("/canzone/{canzoneId}")
	public String getCanzone(@PathVariable("canzoneId") Long canzoneId, Model model) {
		Canzone canzone = this.canzoneService.findCanzoneById(canzoneId);
		if(canzone !=null) {
			model.addAttribute("canzone", canzone);
			model.addAttribute("user",this.globalController.getUser());
			model.addAttribute("review", new Review());
			model.addAttribute("reviews", canzone.getReviews());
			model.addAttribute("hasReviews", !canzone.getReviews().isEmpty());

			if(this.globalController.getUser()!=null&& this.globalController.getUser().getUsername()!=null && this.canzoneService.alreadyReviewed(canzone.getReviews(),this.globalController.getUser().getUsername()))
				model.addAttribute("hasNotAlreadyCommented", false);
			else
				model.addAttribute("hasNotAlreadyCommented", true);
			return "canzone";
		}else {
			return "canzoneError";
		}
	}

	@PostMapping("/canzone")
	public String getCanzone(@Valid @ModelAttribute("canzone") Canzone canzone, BindingResult bindingResult, Model model, @ModelAttribute("canzoneImage") MultipartFile immagine) throws IOException {
		this.canzoneValidator.validate(canzone, bindingResult);
		if (!bindingResult.hasErrors()) {
			model.addAttribute("canzone", this.canzoneService.createNewCanzone(canzone,immagine));
			model.addAttribute("hasReviews", !canzone.getReviews().isEmpty());
			model.addAttribute("user", this.globalController.getUser());
			model.addAttribute("review", new Review());
			return "canzone";
		}else{
			model.addAttribute("messaggio", "Attenzione questo prodotto e' già presente nel sistema");
			return "canzoneError";
		}
	}


	//COMPITI DELL'ADMIN


	//metodoto che visualizza tutti i prodotti nella dashboard dell'admin
	@GetMapping(value="/admin/manageCanzoni")
	public String manageCanzone(Model model) {
		model.addAttribute("canzoni", this.canzoneService.findAllCanzoni());
		model.addAttribute("artisti", this.artistaService.getAllArtisti());
		return "admin/manageCanzoni";
	}

	//metodo che crea un nuovo prodotto
	@PostMapping("/admin/canzone/add")
	public String newCanzone(@Valid @ModelAttribute("canzone") Canzone canzone, BindingResult bindingResult , @RequestParam("canzoneImage") MultipartFile multipartFile, Model model) throws IOException {
		this.canzoneValidator.validate(canzone, bindingResult);
		if(!bindingResult.hasErrors()) {
			model.addAttribute("canzone", this.canzoneService.createNewCanzone(canzone, multipartFile));
			return "redirect:/admin/manageCanzoni";
		}else {
			model.addAttribute("messaggioErrore", "Questa canzone esiste già");
			return "admin/formNewCanzone";
		}

	}

	//metodo per creare un nuovo prodotto
	@GetMapping(value="/admin/formNewCanzone")
	public String formNewCanzone(Model model) {
		model.addAttribute("canzone", new Canzone());
		return "admin/formNewCanzone";
	}

	//metodo per modificare un prodotto
	@GetMapping(value="/admin/formUpdateCanzone/{canzoneId}")
	public String formUpdateCanzone(@PathVariable("canzoneId") Long canzoneId, Model model) {
		Canzone canzone = this.canzoneService.findCanzoneById(canzoneId);
		if(canzone !=null) {
			model.addAttribute("canzone", canzone);
			return "admin/formUpdateCanzone";
		
		}else {
			return "canzoneError";
		}
	}

	//metodo che modifica un prodotto
	@PostMapping("/admin/manageCanzoni/{canzoneId}")
	public String updateCanzone(@PathVariable("canzoneId") Long canzoneId, @ModelAttribute("canzone") Canzone canzone){
		//get prodotto from database by id
		Canzone exisistinCanzone = new Canzone();
		exisistinCanzone = this.canzoneService.findCanzoneById(canzoneId);
		exisistinCanzone.setId(canzoneId);
		exisistinCanzone.setTitolo(canzone.getTitolo());
		exisistinCanzone.setGenere(canzone.getGenere());
		exisistinCanzone.setPrezzo(canzone.getPrezzo());
		exisistinCanzone.setImage(canzone.getImage());

		//save updated product Object
		this.canzoneService.saveCanzone(exisistinCanzone);
		return "redirect:/admin/manageCanzoni";
	}

	//metodo per eliminare un prodotto
	@GetMapping(value="/admin/deleteCanzone/{canzoneId}")
	public String RemoveCanzone(@PathVariable("canzoneId") Long canzoneId) {
		this.canzoneService.deleteCanzone(canzoneId);
		return  "redirect:/admin/manageCanzoni";
	}

	@GetMapping(value="/admin/elencoArtistiCanzone/{canzoneId}")
	public String formElencoArtisti(@PathVariable("canzoneId") Long canzoneId, Model model) {
		Canzone canzone = this.canzoneService.findCanzoneById(canzoneId);
		if(canzone !=null) {
			model.addAttribute("canzone", canzone);
			model.addAttribute("artisti", artistaService.getAllArtisti());
			return "admin/elencoArtistiCanzone";

		}else {
			return "canzoneError";
		}
	}
	@GetMapping("/admin/addArtista/{canzoneId}/{artistaId}")
	public String addArtistaToCanzone (@PathVariable("canzoneId") Long canzoneId, @PathVariable("artistaId") Long artistaId, Model model) {

		this.artistaService.addCanzoneToArtista(artistaId, canzoneId);

		model.addAttribute("prodotto", this.canzoneService.addArtistaToCanzone(canzoneId, artistaId));
		model.addAttribute("fornitori", this.artistaService.getAllArtisti());

		return "redirect:/admin/elencoArtistiCanzone/{canzoneId}";
	}

	@GetMapping(value="/admin/removeArtista/{canzoneId}/{artistaId}")
	public String removeArtistaFromCanzone (@PathVariable("canzoneId") Long canzoneId, @PathVariable("artistaId") Long artistaId, Model model) {

		this.artistaService.removeCanzoneFromArtista(artistaId, canzoneId);

		model.addAttribute("prodotto", this.canzoneService.deleteArtistaFromCanzone(canzoneId, artistaId));
		model.addAttribute("fornitori", this.artistaService.getAllArtisti());

		return "redirect:/admin/elencoArtistiCanzone/{canzoneId}";
	}


}
