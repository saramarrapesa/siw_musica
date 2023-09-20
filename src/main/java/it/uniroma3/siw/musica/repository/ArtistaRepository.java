package it.uniroma3.siw.musica.repository;

import it.uniroma3.siw.musica.model.Artista;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;




public interface ArtistaRepository extends CrudRepository<Artista, Long> {
	
			/*@Query(value="select * "
			+ "from fornitore f "
			+ "where f.id not in "
			+ "(select fornitori_id "
			+ "from fornitori_prodotti "
			+ "where fornitori_prodotti.starred_prodotti_id = :prodottoId)", nativeQuery=true)

	 Iterable<Artista> findFornitoriNotInProdotto(@Param("prodottoId") Long prodottoId);*/
	 boolean existsByNomeAndAndNomeDarte(String nome, String nomeDarte);

}
