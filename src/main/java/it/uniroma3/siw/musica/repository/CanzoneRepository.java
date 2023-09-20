package it.uniroma3.siw.musica.repository;

import it.uniroma3.siw.musica.model.Canzone;
import org.springframework.data.repository.CrudRepository;

import java.util.List;



public interface CanzoneRepository extends CrudRepository<Canzone, Long> {

	boolean existsByTitolo(String nome);

    List<Canzone> findByTitolo(String nome);
}
