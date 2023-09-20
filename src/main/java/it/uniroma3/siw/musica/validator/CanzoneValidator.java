package it.uniroma3.siw.musica.validator;

import it.uniroma3.siw.musica.model.Canzone;
import it.uniroma3.siw.musica.repository.CanzoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;



@Component
public class CanzoneValidator implements Validator {

	@Autowired
	public CanzoneRepository prodottoRepository;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Canzone.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
       Canzone canzone = (Canzone)target;
		if(canzone.getTitolo()!=null && prodottoRepository.existsByTitolo(canzone.getTitolo()))
			errors.reject("prodotto.duplicate");


	}

}
