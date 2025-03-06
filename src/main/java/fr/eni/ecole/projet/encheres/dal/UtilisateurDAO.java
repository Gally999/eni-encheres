
package fr.eni.ecole.projet.encheres.dal;

import java.util.List;

import fr.eni.ecole.projet.encheres.bo.Utilisateur;

public interface UtilisateurDAO {
		
	void create(Utilisateur utilisateur);

	Utilisateur read(String emailUtilisateur);

	Utilisateur readByPseudo(String pseudo);

	void update(Utilisateur utilisateur);

	List<Utilisateur> findAll();

	int uniqueEmail(String email);

	int uniquePseudo(String pseudo);

	
	
}


