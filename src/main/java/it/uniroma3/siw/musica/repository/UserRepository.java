package it.uniroma3.siw.musica.repository;

import it.uniroma3.siw.musica.model.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Long> {

}
