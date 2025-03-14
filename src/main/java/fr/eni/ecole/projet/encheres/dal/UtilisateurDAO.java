
package fr.eni.ecole.projet.encheres.dal;

import java.util.List;

import fr.eni.ecole.projet.encheres.bo.Adresse;
import fr.eni.ecole.projet.encheres.bo.Utilisateur;

public interface UtilisateurDAO {
		
	void create(Utilisateur utilisateur);

	Utilisateur read(String emailUtilisateur);

	Utilisateur readByPseudo(String pseudo);

	void update(Utilisateur utilisateur);

	List<Utilisateur> findAll();

	int uniqueEmail(String email);

	int uniquePseudo(String pseudo);

	String getTelephoneByPseudo(String pseudo);

	int getCreditByPseudo(String pseudo);

	Adresse getAdresseByNoAdresse(int noAdresse);

	void deleteByPseudo(String pseudo);

	void deleteAdresseByNoAdresse(int noAdresse);

	int getNoAdresseByPseudo(String pseudo);

	int countUsersByNoAdresse(int noAdresse);

	Utilisateur ReadByPseudo(String pseudo);

	void updateMotDePasse(Utilisateur utilisateur);

	void deleteEnchereByPseudoe(String pseudo);

	void deleteArticleByPseudo(String pseudo);

	
}


