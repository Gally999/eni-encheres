package fr.eni.ecole.projet.encheres.bll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import fr.eni.ecole.projet.encheres.bo.Adresse;
import fr.eni.ecole.projet.encheres.bo.Utilisateur;
import fr.eni.ecole.projet.encheres.dal.AdresseDAO;
import fr.eni.ecole.projet.encheres.dal.UtilisateurDAO;
import fr.eni.ecole.projet.encheres.exceptions.BusinessCode;
import fr.eni.ecole.projet.encheres.exceptions.BusinessException;

@Service
public class UtilisateurServiceImpl implements UtilisateurService{

	@Autowired
	private UtilisateurDAO utilisateurDAO;
	@Autowired
	private AdresseDAO adresseDAO;

	@Autowired
	private PasswordEncoder passwordEncoder;
	public UtilisateurServiceImpl utilisateurRepository;



	public UtilisateurServiceImpl() {
	}

	public UtilisateurServiceImpl(UtilisateurDAO utilisateurDAO) {
		this.utilisateurDAO = utilisateurDAO;
	}

	public UtilisateurServiceImpl(UtilisateurDAO utilisateurDAO, AdresseDAO adresseDAO) {
		this.utilisateurDAO = utilisateurDAO;
		this.adresseDAO = adresseDAO;
	}

	@Override
	public List<Utilisateur> getUtilisateurs() {
		return utilisateurDAO.findAll();
	}



	// MODIFIER MOT DE PASSE	
	@Override
	public void mettreAjourMotDePasse(String motDePasseNew, Utilisateur utilisateur) throws BusinessException {
		System.out.println("La méthode mettreAjourMotDePasse est appelée.");

		BusinessException be = new BusinessException();
		boolean isValid = true;

		// Validation des données de l'utilisateur
		System.out.println("Validation des informations de l'utilisateur...");
		isValid &= validerMotDePasse(motDePasseNew, be);


		if (isValid) {
			try {


				// Crypter le nouveau mot de passe
				System.out.println("Cryptage du nouveau mot de passe...");
				PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();


				String motDePasseCrypte = passwordEncoder.encode(motDePasseNew);  

				// Mettre à jour le mot de passe de l'utilisateur
				utilisateur.setMotDePasse(motDePasseCrypte);

				// Mise à jour du mot de passe dans la base de données
				System.out.println("Mise à jour du mot de passe dans la base de données...");
				utilisateurDAO.updateMotDePasse(utilisateur);  

			} catch (DataAccessException e) {
				System.out.println("Erreur lors de la mise à jour du mot de passe de l'utilisateur : " + e.getMessage());
				be.add(BusinessCode.BLL_UTILISATEURS_UPDATE_ERREUR);
				throw be;
			}
		} else {
			System.out.println("Le mot de passe de l'utilisateur n'est pas validé.");
			throw be;
		}
	}


	@Override
	public boolean verifierMotDePasse(String motDePasseSaisi, Utilisateur utilisateur) {
		System.out.println("La méthode VerifierMotDePasse est appelée.");

		// Vérification si l'utilisateur est null
		if (utilisateur == null) {
			System.out.println("Utilisateur null, échec de la vérification.");
			return false;
		}

		// Récupération du mot de passe stocké (crypté) dans la base de données
		String motDePasseStocke = utilisateur.getMotDePasse(); 
		System.out.println("Mot de passe stocké (crypté) : " + motDePasseStocke);

		// Création du PasswordEncoder
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

		// Crypter le mot de passe saisi par l'utilisateur
		String motDePasseSaisiCrypte = passwordEncoder.encode(motDePasseSaisi); 
		System.out.println("Mot de passe saisi crypté : " + motDePasseSaisiCrypte);

		// Comparaison du mot de passe saisi crypté avec le mot de passe stocké dans la base de données
		boolean matches = passwordEncoder.matches(motDePasseSaisi, motDePasseStocke);
		System.out.println("Le mot de passe saisi (crypté) correspond-il au mot de passe stocké ? " + matches);

		return matches;
	}

	public void save(Utilisateur utilisateur) {
		utilisateurRepository.save(utilisateur);  
	}



	// SUPPRIMER MON PROFIL
	@Override
	public boolean supprimerUtilisateur(String pseudo) {
		try {
			// Récupérer le no_adresse de l'utilisateur
			System.out.println("Récupération du no_adresse pour l'utilisateur: " + pseudo);
			int noAdresse = utilisateurDAO.getNoAdresseByPseudo(pseudo);
			System.out.println("no_adresse trouvé: " + noAdresse);

			// Vérifier combien d'utilisateurs partagent le même no_adresse
			System.out.println("Vérification du nombre d'utilisateurs avec le même no_adresse...");
			int count = utilisateurDAO.countUsersByNoAdresse(noAdresse);
			System.out.println("Nombre d'utilisateurs avec ce no_adresse: " + count);

			// Si seul cet utilisateur utilise ce no_adresse, supprimer l'adresse
			if (count == 1) {

				// Supprimer l'utilisateur
				utilisateurDAO.deleteEnchereByPseudoe(pseudo);
				System.out.println("Suppression des encheres: " + pseudo);
				// Supprimer les articles à vendre
				utilisateurDAO.deleteArticleByPseudo(pseudo);
				System.out.println("Suppression des articles à vendre: " + pseudo);
				utilisateurDAO.deleteByPseudo(pseudo);
				System.out.println("Suppression de l'utilisateur: " + pseudo);

				System.out.println("Cet utilisateur est le seul à utiliser cette adresse. Suppression de l'adresse...");
				utilisateurDAO.deleteAdresseByNoAdresse(noAdresse);
				System.out.println("Adresse supprimée avec succès.");
			} else {
				System.out.println("L'adresse est partagée par d'autres utilisateurs. L'adresse ne sera pas supprimée.");
				// Supprimer les encheres
				utilisateurDAO.deleteEnchereByPseudoe(pseudo);
				System.out.println("Suppression des encheres: " + pseudo);
				// Supprimer les articles à vendre
				utilisateurDAO.deleteArticleByPseudo(pseudo);
				System.out.println("Suppression des articles à vendre: " + pseudo);
				// Supprimer l'utilisateur
				utilisateurDAO.deleteByPseudo(pseudo);
				System.out.println("Suppression de l'utilisateur: " + pseudo);
			}


			System.out.println("Utilisateur supprimé avec succès.");

			// Si tout se passe bien, retourner true
			return true;

		} catch (Exception e) {
			// Si une exception survient, afficher l'erreur et retourner false
			System.out.println("Erreur lors de la suppression de l'utilisateur: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}







	//PAGE MODIFIER MON PROFIL 
	@Override
	@Transactional
	public void mettreAjourUtilisateur(Utilisateur utilisateur) {
		// Vérifier si cette méthode est appelée
		System.out.println("La méthode update est appelée.");

		BusinessException be = new BusinessException();
		boolean isValid = true;
		System.out.println(utilisateur);


		// Validation des données de l'utilisateur
		System.out.println("Validation des informations de l'utilisateur...");

		isValid &= validerUtilisateur(utilisateur, be);
		isValid &= validerNom(utilisateur.getNom(), be);
		isValid &= validerPrenom(utilisateur.getPrenom(), be);
		isValid &= validerEmail(utilisateur.getEmail(), be);
		isValid &= validerUniqueEmail(utilisateur.getEmail(), be);
		isValid &= validerEmailExiste(utilisateur.getEmail(), be);
		isValid &= validerTelephone(utilisateur.getTelephone(), be);
		isValid &= validerAdresse(utilisateur.getAdresse(), be);

		if (isValid) {
			System.out.println("Les informations de l'utilisateur sont valides.");
			try {
				System.out.println("Mise à jour de l'utilisateur avec pseudo : " + utilisateur.getPseudo());

				utilisateur.getAdresse().setNoAdresse((int) verifierEtAffecterAdresse(utilisateur.getAdresse()));
				utilisateurDAO.update(utilisateur);
				System.out.println("no_adresse de l'utilisateur  " + utilisateur.getAdresse().getNoAdresse());

				System.out.println("Mise à jour réussie.");
			} catch (DataAccessException e) {
				System.out.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
				be.add(BusinessCode.BLL_UTILISATEURS_UPDATE_ERREUR);
				throw be;
			}
		} else {
			System.out.println("Les informations de l'utilisateur ne sont pas valides.");
			throw be;
		}
	}


	@Override
	// Méthode pour récupérer l'adresse complète de l'utilisateur connecté
	public Adresse getAdresseDeLUtilisateurConnecte(String pseudo) {
		// Récupérer le no_adresse de l'utilisateur via son pseudo
		int noAdresse = utilisateurDAO.getNoAdresseByPseudo(pseudo);

		// Ensuite, récupérer l'adresse avec ce no_adresse
		Adresse adresse = utilisateurDAO.getAdresseByNoAdresse(noAdresse);
		System.out.println("Adresse récupérée avec no_adresse " + noAdresse + " : " + adresse); 

		return adresse;
	}

	@Override
	public int getCreditDeLUtilisateurConnecte(String pseudo) {
		Utilisateur utilisateur = findByPseudo(pseudo);

		if (utilisateur != null) {
			return utilisateur.getCredit();
		}
		return 0;
	}



	//PAGE MON PROFIL
	@Override
	public Utilisateur findByPseudo(String pseudo) {
		return utilisateurDAO.readByPseudo(pseudo);
	}

	public String getTelephoneDeLUtilisateurConnecte(String pseudo) {
		Utilisateur utilisateur = findByPseudo(pseudo);

		if (utilisateur != null) {
			return utilisateur.getTelephone();
		}
		return null;
	}



	//S'INSCRIRE:
	@Override
	public void add(String pseudo, String nom, String prenom, String email, String telephone, String motDePasse,
			int credit, boolean admin, Adresse adresse) {

		// Crypter le mot de passe avant de créer l'utilisateur
		String motDePasseCrypte = passwordEncoder.encode(motDePasse);
		Utilisateur utilisateur = new Utilisateur(pseudo, nom, prenom, email, telephone, motDePasseCrypte, credit, admin, adresse);
		utilisateurDAO.create(utilisateur);
	}	

	@Override
	@Transactional
	public void add(Utilisateur utilisateur) {
		System.out.println("Méthode add appelée - Ajouter un utilisateur");

		// Validation des données de la couche présentation
		BusinessException be = new BusinessException();
		boolean isValid = true;

		isValid &= validerUtilisateur(utilisateur, be);
		System.out.println("isValid:"+isValid);
		
		System.out.println("Validation du nom");
		isValid &= validerNom(utilisateur.getNom(), be);

		System.out.println("Validation du prénom");
		isValid &= validerPrenom(utilisateur.getPrenom(), be);

		System.out.println("Validation de l'email");
		isValid &= validerEmail(utilisateur.getEmail(), be);
		System.out.println("Validation de l'unicité de l'email");
		isValid &= validerUniqueEmail(utilisateur.getEmail(), be);
		System.out.println("Validation de l'unicité de l'email");
		isValid &= validerEmailExiste(utilisateur.getEmail(), be);

		System.out.println("Validation du pseudo");
		isValid &= validerUniquePseudo(utilisateur.getPseudo(), be);
		System.out.println("Validation de l'unicité du pseudo");
		isValid &= validerPseudo(utilisateur.getPseudo(), be);
		System.out.println("Validation de l'unicité du pseudo");
		isValid &= validerPseudoExiste(utilisateur.getPseudo(), be);

		System.out.println("Validation du téléphone");
		isValid &= validerTelephone(utilisateur.getTelephone(), be);

		System.out.println("Validation du mot de passe");
		isValid &= validerMotDePasse(utilisateur.getMotDePasse(), be);  

		System.out.println("Validation de l'adresse");
		isValid &= validerAdresse(utilisateur.getAdresse(), be);

		if (isValid) {
			System.out.println("Validation réussie, cryptage du mot de passe avant de créer l'utilisateur");

			// Crypter le mot de passe avant de créer l'utilisateur
			PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			String motDePasseCrypte = passwordEncoder.encode(utilisateur.getMotDePasse());
			utilisateur.setMotDePasse(motDePasseCrypte);  // Remplacer le mot de passe en clair par celui chiffré
			System.out.println("Mot de passe crypté : " + motDePasseCrypte);

			// Vérifier et affecter l'adresse
			utilisateur.getAdresse().setId(verifierEtAffecterAdresse(utilisateur.getAdresse()));
			System.out.println("Adresse affectée : " + utilisateur.getAdresse().getId());

			utilisateurDAO.create(utilisateur);
			System.out.println("Utilisateur créé avec succès : " + utilisateur.getPseudo());
		} else {
			System.out.println("Erreurs de validation détectées");
			throw be;
		}
	}



	/**
	 * Méthodes de validation des BO
	 */
	private boolean validerUtilisateur(Utilisateur u, BusinessException be) {
		if (u == null) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_NULL);
			return false;
		}
		return true;
	}

	private boolean validerNom(String nom, BusinessException be) {
		System.out.println("prenom " + nom);

		if (nom == null || nom.isBlank()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_NOM_VIDE);
			return false;
		} 

		if (nom.length() < 4 || nom.length() > 250) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_NOM_TAILLE);
			return false;
		}

		return true;
	}


	private boolean validerPrenom(String prenom, BusinessException be) {
		System.out.println("prenom " + prenom);

		if (prenom == null || prenom.isBlank()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PRENOM_VIDE);
			return false;
		} 

		if (prenom.length() < 4 || prenom.length() > 250) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PRENOM_TAILLE);
			return false;
		}

		return true;
	}


	private boolean validerTelephone(String telephone, BusinessException be) {

		System.out.println("telephone " + telephone);

		if(telephone == null || telephone.isBlank()) {
			return true;
		}else{
			// Regex to check valid telephone
			String regex ="^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$";

			System.out.println("telephone dans le else");
			if (!telephone.matches(regex)) {
				be.add(BusinessCode.VALIDATION_UTILISATEUR_TELEPHONE_PATTERN);
				return false;
			}
		}
		return true;
	}


	private boolean validerMotDePasse(String motDePasse, BusinessException be) {
		if (motDePasse == null || motDePasse.isBlank()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PASSWORD_BLANK);
			return false;
		}
		// Regex to check valid Password
		String regex ="^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,20}$";

		if (!motDePasse.matches(regex)) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PASSWORD_PATTERN);
			return false;
		}
		return true;
	}

	
	private boolean validerAdresse(Adresse adresse, BusinessException be) {

		if (adresse == null ) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_ADRESSE_NULL);
			return false;
		}

		if (adresse.getRue() == null || adresse.getRue().isBlank()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_RUE_BLANK);
			return false;
		}
		// Regex to check valid rue
		String regex1 ="^[A-Za-z,]{4,250}$";

		if (!adresse.getRue().matches(regex1)) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_RUE_PATTERN);
			return false;
		}
		if (adresse.getCodePostal() == null || adresse.getCodePostal().isBlank()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_CODE_POSTAL_BLANK);
			return false;
		}
		// Regex to check valid code postal
		String regex2 = "^\\d{5}$";
		if (!adresse.getCodePostal().matches(regex2)) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_CODE_POSTAL_PATTERN);
			return false;
		}
		if (adresse.getVille() == null || adresse.getVille().isBlank()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_VILLE_BLANK);
			return false;
		}
		if (adresse.getVille().length() < 4 || adresse.getVille().length() > 250) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_VILLE_LENGTH);
			return false;
		}
		return true;
	}


	private boolean validerEmail(String email, BusinessException be) {
		if (email == null || email.isBlank()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_EMAIL_BLANK);
			return false;
		}
		// Regex to check valid email
		String regex ="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

		if (!email.matches(regex)) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_EMAIL_PATTERN);
			return false;
		}
		return true;
	}


	private boolean validerUniqueEmail(String email, BusinessException be) {
		try {
			int count = utilisateurDAO.uniqueEmail(email);
			if (count == 1) {
				be.add(BusinessCode.VALIDATION_UTILISATEUR_UNIQUE_EMAIL);
				return false;
			}
		} catch (DataAccessException e) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_UNIQUE_EMAIL);
			e.printStackTrace();
			return false;
		}
		return true;
	}


	private boolean validerEmailExiste(String emailUtilisateur, BusinessException be) {
		// L'email doit exister - s'il n'existe pas il y aura levée de l'exception
		// DataAccessException
		// Il faut gérer les 2 cas
		try {
			Utilisateur u = utilisateurDAO.read(emailUtilisateur);
			if (u == null) {
				// Il n'y a pas d'utilisateur correspondant en base
				be.add(BusinessCode.VALIDATION_UTILISATEUR_DB_NULL);
				return false;
			}
		} catch (DataAccessException e) {
			// Impossible de trouver un utilisateur
			// Il n'y a pas d'utilisateur correspondant en base
			be.add(BusinessCode.VALIDATION_UTILISATEUR_DB_NULL);
			return false;
		}
		return true;
	}

	private boolean validerUniquePseudo(String pseudo, BusinessException be) {
		int count = utilisateurDAO.uniquePseudo(pseudo);
		if (count == 1) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_UNIQUE_PSEUDO);
			return false;
		}
		return true;
	}


	private boolean validerPseudoExiste(String pseudoUtilisateur, BusinessException be) {

		try {
			Utilisateur u = utilisateurDAO.read(pseudoUtilisateur);
			if (u == null) {
				be.add(BusinessCode.VALIDATION_UTILISATEUR_DB_NULL);
				return false;
			}
		} catch (DataAccessException e) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_DB_NULL);
			return false;
		}
		return true;
	}

	@Override
	public boolean validerPseudo(String pseudo, BusinessException be) {

		if (pseudo == null || pseudo.isBlank()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PSEUDO_BLANK);
			return false;
		}
		// Regex to check valid email
		String regex ="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";

		if (!pseudo.matches(regex)) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_EMAIL_PATTERN);
			return false;
		}
		return true;
	}


	@Override
	public long verifierEtAffecterAdresse(Adresse adresse) {

		int idAdresse = (int) adresseDAO.readAdresseConnue(adresse);

		System.out.println( "idAdresse " + idAdresse);

		if (idAdresse > 0) {
			return idAdresse;
		} else {
			adresseDAO.create(adresse);
			return adresse.getId();
		}

	}


}
