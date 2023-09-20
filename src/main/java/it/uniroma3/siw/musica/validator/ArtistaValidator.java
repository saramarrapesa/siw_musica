package it.uniroma3.siw.musica.validator;

import it.uniroma3.siw.musica.model.Artista;
import it.uniroma3.siw.musica.repository.ArtistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;



@Component
public class ArtistaValidator implements Validator {

	@Autowired
	private ArtistaRepository artistaRepository;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Artista.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		Artista artista = (Artista) target;
		if(artista.getNome()!=null && artista.getNomeDarte()!=null && artistaRepository.existsByNomeAndAndNomeDarte(artista.getNome(), artista.getNomeDarte()))
			errors.reject("artista.duplicate");
		}

}
