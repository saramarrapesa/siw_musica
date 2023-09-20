package it.uniroma3.siw.musica.model;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
public class Canzone {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	

	@NotBlank
	private String titolo;
	
	
	private String genere;

	@NotNull
	private float prezzo;

	@OneToOne
    private Image image;

	
	@ManyToMany
	private Set<Artista> artisti;
	
	@OneToMany
    private Set<Review> reviews;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getGenere() {
		return genere;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	public float getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(float prezzo) {
		this.prezzo = prezzo;
	}

	public Set<Artista> getArtisti() {
		return artisti;
	}

	public void setArtisti(Set<Artista> artisti) {
		this.artisti = artisti;
	}
	
	public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }


	@Override
	public int hashCode() {
		return Objects.hash(id, titolo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Canzone other = (Canzone) obj;
		return Objects.equals(id, other.id) && Objects.equals(titolo, other.titolo);
	}


	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	


}
