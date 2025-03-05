package fr.eni.ecole.projet.encheres.bo;

import java.io.Serializable;
import java.util.Objects;

public class Categorie implements Serializable {
	
	private long id;
	private String libelle;
	
	public Categorie() {
	}
	
	public Categorie(String libelle) {
		this.libelle = libelle;
	}
	
	public Categorie(long id, String libelle) {
		this(libelle);
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, libelle);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categorie other = (Categorie) obj;
		return id == other.id && Objects.equals(libelle, other.libelle);
	}

	@Override
	public String toString() {
		return "Categorie [id=" + id + ", libelle=" + libelle + "]";
	}

}
