package it.uniroma3.siw.musica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;
import java.util.Set;

@Entity
public class Artista {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String nome;
	private String nomeDarte;
	@NotBlank
	private String cognome;


	@OneToOne
	private Image image;
	@ManyToMany(mappedBy = "artisti")
	private Set<Canzone> canzoni;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeDarte() {
		return nomeDarte;
	}

	public void setNomeDarte(String nomeDarte) {
		this.nomeDarte = nomeDarte;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Set<Canzone> getCanzoni() {
		return canzoni;
	}

	public void setCanzoni(Set<Canzone> canzoni) {
		this.canzoni = canzoni;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cognome, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artista other = (Artista) obj;
		return Objects.equals(cognome, other.cognome) && Objects.equals(id, other.id);
	}


	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}


}
