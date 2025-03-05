

package fr.eni.ecole.projet.encheres.bll;

import java.util.List;

import fr.eni.ecole.projet.encheres.bo.Adresse;
import fr.eni.ecole.projet.encheres.bo.Utilisateur;

public interface UtilisateurService {

		void add(String pseudo, String nom, String prenom, String email, String telephone, String motDePasse,
			int credit, boolean admin, Adresse adresse);
	
		List<Utilisateur> getUtilisateurs();

		void add(Utilisateur utilisateur);
		
		Utilisateur findByEmail(String emailUtilisateur);

		Utilisateur findByPseudo(String pseudo);

		void update(Utilisateur utilisateur);
		
		int uniqueEmail(String email);

		int uniquePseudo(String pseudo);

		
	}
