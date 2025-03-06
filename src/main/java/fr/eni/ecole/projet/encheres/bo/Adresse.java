package fr.eni.ecole.projet.encheres.bo;

import java.io.Serializable;
import java.util.Objects;

public class Adresse implements Serializable {
	
	private long id;
	private String rue;
	private String codePostal;
	private String ville;
	private int noAdresse; 
	
	public Adresse() {
	}
	
	public Adresse(String rue, String codePostal, String ville) {
		this.rue = rue;
		this.codePostal = codePostal;
		this.ville = ville;
	}

	public Adresse(long id, String rue, String codePostal, String ville) {
		this(rue, codePostal, ville);
		this.id = id;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codePostal, id, rue, ville);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Adresse other = (Adresse) obj;
		return Objects.equals(codePostal, other.codePostal) && id == other.id && Objects.equals(rue, other.rue)
				&& Objects.equals(ville, other.ville);
	}

	@Override
	public String toString() {
		return "Adresse [id=" + id + ", rue=" + rue + ", codePostal=" + codePostal + ", ville=" + ville + "]";
	}

	public void setNoAdresse(int noAdresse) {
		this.noAdresse = noAdresse;		
	}

	public int getNoAdresse() {
		return noAdresse;
	}

	
}
