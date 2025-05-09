
package fr.eni.ecole.projet.encheres.dal;

import java.util.List;

import fr.eni.ecole.projet.encheres.bo.Adresse;
import fr.eni.ecole.projet.encheres.bo.Utilisateur;

public interface UtilisateurDAO {
		
	void create(Utilisateur utilisateur);

	Utilisateur read(String emailUtilisateur);

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

	void updateMotDePasse(Utilisateur utilisateur);

	void deleteEnchereByPseudoe(String pseudo);

	void deleteArticleByPseudo(String pseudo);

	void debiter(int montantEnchere, String pseudo);

	void recrediterPrecedentEncherisseur(int montant, String pseudo);

	Utilisateur readByPseudo(String pseudo);

	int countPassword(String motDePasseNewCrypte);

}


