package it.uniroma3.siw.musica.controller;


import it.uniroma3.siw.musica.model.Canzone;
import it.uniroma3.siw.musica.model.Review;
import it.uniroma3.siw.musica.service.CanzoneService;
import it.uniroma3.siw.musica.service.ReviewService;
import it.uniroma3.siw.musica.validator.ReviewValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import jakarta.validation.Valid;

@Controller
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private ReviewValidator reviewValidator;

	@Autowired
	private CanzoneService canzoneService;

	@Autowired
	private GlobalController globalController;

	@PostMapping("/user/uploadReview/{canzoneId}")
	public String newReview(@Valid @ModelAttribute("review") Review review, BindingResult bindingResult, Model model, @PathVariable("canzoneId") Long canzoneId) {
		this.reviewValidator.validate(review, bindingResult);
		if (!bindingResult.hasErrors()) {
			Canzone canzone = this.canzoneService.findCanzoneById(canzoneId);
			if (this.globalController.getUser() != null && !canzone.getReviews().contains(review)) {
				review.setUsername(this.globalController.getUser().getUsername());
				this.reviewService.saveReview(review);
				canzone.getReviews().add(review);
			}
			this.canzoneService.saveCanzone(canzone);
			this.canzoneService.function(model, canzone, this.globalController.getUser().getUsername());
			return "redirect:/canzone/{canzoneId}";
		} else {
			return "canzoneError";
		}
	}

	@GetMapping("/user/deleteReview/{canzoneId}/{reviewId}")
	public String removeReview(Model model, @PathVariable("canzoneId") Long canzoneId, @PathVariable("reviewId") Long reviewId) {
		Canzone canzone = this.canzoneService.findCanzoneById(canzoneId);
		Review review = this.reviewService.findById(reviewId);

		canzone.getReviews().remove(review);
		this.reviewService.deleteReview(review);
		this.canzoneService.saveCanzone(canzone);
		this.canzoneService.function(model, canzone, this.globalController.getUser().getUsername());
		return "redirect:/canzone/{canzoneId}";
	}


}
