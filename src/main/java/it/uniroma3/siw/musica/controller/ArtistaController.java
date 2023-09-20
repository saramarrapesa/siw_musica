package it.uniroma3.siw.musica.controller;

import it.uniroma3.siw.musica.model.Artista;
import it.uniroma3.siw.musica.service.ArtistaService;
import it.uniroma3.siw.musica.service.CanzoneService;
import it.uniroma3.siw.musica.validator.ArtistaValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
public class ArtistaController {
	
	@Autowired
	private ArtistaService artistaService;
	
	@Autowired
	private ArtistaValidator artistaValidator;

	@Autowired
	private CanzoneService canzoneService;


	//metodoto che visualizza tutti i fornitori nella dashboard dell'admin
	@GetMapping(value="/admin/manageArtisti")
	public String manageArtista(Model model) {
		model.addAttribute("artisti", this.artistaService.getAllArtisti());
		return "admin/manageArtisti";
	}


	@GetMapping(value="/admin/formNewArtista")
	public String formNewArtista(Model model) {
		model.addAttribute("artista", new Artista());
		return "formNewArtista";
	}

	@PostMapping("/admin/artista/add")
	public String postArtistaAdd(@Valid @ModelAttribute("artista") Artista artista , BindingResult bindingResult , Model model ,@ModelAttribute("artistaImage") MultipartFile immagine) throws IOException {
		this.artistaValidator.validate(artista, bindingResult);
		if(!bindingResult.hasErrors()) {
			model.addAttribute("artista", this.artistaService.createFornitore(artista,immagine));
			return "redirect:/admin/manageArtisti";
		}else {
			model.addAttribute("messaggioErrore", "Questo fornitore esiste già");
			return "artistaError";

		}

	}

	//metodo per eliminare un prodotto
	@GetMapping(value="/admin/deleteArtista/{artistaId}")
	public String RemoveArtista(@PathVariable("artistaId") Long artistaId) {
		this.artistaService.deleteArtista(artistaId);
		return "redirect:/admin/manageArtisti";
	}

	//metodo per modificare un prodotto
	@GetMapping(value="/admin/formUpdateArtista/{artistaId}")
	public String formUpdateFornitore(@PathVariable("artistaId") Long artistaId, Model model) {
		Artista artista = this.artistaService.findArtistaById(artistaId);
		if(artista !=null) {
			model.addAttribute("artista", artista);
			return "admin/formUpdateArtista";

		}else {
			return "artistaError";
		}
	}

	//metodo che modifica un prodotto
	@PostMapping("/admin/manageArtisti/{artistaId}")
	public String updateArtista(@PathVariable("artistaId") Long artistaId, @ModelAttribute("artista") Artista artista){
		//get prodotto from database by id
		Artista exisistinArtista = new Artista();
		exisistinArtista = this.artistaService.findArtistaById(artistaId);
		exisistinArtista.setNome(artista.getNome());
		exisistinArtista.setCognome(artista.getCognome());
		exisistinArtista.setNomeDarte(artista.getNomeDarte());
		exisistinArtista.setImage(artista.getImage());
		//save updated product Object
		this.artistaService.saveArtista(exisistinArtista);
		return "redirect:/admin/manageArtisti";
	}

	@GetMapping(value="/admin/elencoCanzoniArtista/{artistaId}")
	public String formElencoCanzoni(@PathVariable("artistaId") Long artistaId, Model model) {
		Artista artista = this.artistaService.findArtistaById(artistaId);
		if(artista !=null) {
			model.addAttribute("artista", artista);
			model.addAttribute("canzoni", canzoneService.findAllCanzoni());
			return "admin/elencoCanzoniArtista";

		}else {
			return "artistaError";
		}
	}


	//metodo per visualizzare i fornitori
	@GetMapping("/artisti")
	public String getArtisti(Model model) {
		model.addAttribute("artisti", this.artistaService.getAllArtisti());
		return "artisti";
	}

	//metodo per visualizzare un particolare fornitore in base a id
	@GetMapping("/artista/{artistaId}")
	public String getArtista(@PathVariable("artistaId") Long artistaId, Model model) {
		Artista artista = this.artistaService.findArtistaById(artistaId);
		if(artista !=null) {
			model.addAttribute("artista", artista);
			return "artista";
		}
		else {
			return "artistaError";
		}
	}


	@GetMapping("/admin/addCanzone/{artistaId}/{canzoneId}")
	public String addProdottoToFornitore (@PathVariable("artistaId") Long artistaId, @PathVariable("canzoneId") Long canzoneId,Model model) {


		this.canzoneService.addArtistaToCanzone(canzoneId, artistaId);

		model.addAttribute("fornitore", this.artistaService.addCanzoneToArtista(artistaId, canzoneId));
		model.addAttribute("prodotti", this.canzoneService.findAllCanzoni());
		return "redirect:/admin/elencoProdottiFornitore/{fornitoreId}";
	}

	@GetMapping("/admin/removeCanzone/{artistaId}/{canzoneId}")
	public String removeProdotto (@PathVariable("artistaId") Long fornitoreId, @PathVariable("canzoneId") Long prodottoId,Model model) {

		this.canzoneService.deleteArtistaFromCanzone(prodottoId, fornitoreId);

		model.addAttribute("artista", this.artistaService.removeCanzoneFromArtista(fornitoreId,prodottoId));
		model.addAttribute("canzoni", this.canzoneService.findAllCanzoni());

		return "redirect:/admin/elencoCanzoniArtista//{fornitoreId}";
	}


}
