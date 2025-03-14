package fr.eni.ecole.projet.encheres.bll;

import java.util.List;

import fr.eni.ecole.projet.encheres.bo.Adresse;
import fr.eni.ecole.projet.encheres.bo.Utilisateur;
import fr.eni.ecole.projet.encheres.exceptions.BusinessException;

public interface UtilisateurService {

		void add(String pseudo, String nom, String prenom, String email, String telephone, String motDePasse,
			int credit, boolean admin, Adresse adresse);
	
		List<Utilisateur> getUtilisateurs();

		void add(Utilisateur utilisateur);
		
		Utilisateur findByPseudo(String pseudo);

		long verifierEtAffecterAdresse(Adresse adresse);

		String getTelephoneDeLUtilisateurConnecte(String pseudo);

		int getCreditDeLUtilisateurConnecte(String pseudo);

		Adresse getAdresseDeLUtilisateurConnecte(String pseudo);

		boolean supprimerUtilisateur(String pseudo);

		void mettreAjourUtilisateur(Utilisateur utilisateur);

		boolean verifierMotDePasse(String motDePasseSaisi, Utilisateur utilisateur);

		void mettreAjourMotDePasse(String motDePasseNew, Utilisateur utilisateur) throws BusinessException;

		boolean validerPseudo(String pseudo, BusinessException be);

		Utilisateur findByPseudoMDP(String pseudo);

		int uniquePseudo(String pseudo);

		int uniqueEmail(String email);

		

		


	}
