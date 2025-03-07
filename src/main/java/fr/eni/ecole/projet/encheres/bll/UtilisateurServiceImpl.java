

package fr.eni.ecole.projet.encheres.bll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    private PasswordEncoder passwordEncoder;  // Injecter PasswordEncoder


	
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
	public void add(String pseudo, String nom, String prenom, String email, String telephone, String motDePasse,
			int credit, boolean admin, Adresse adresse) {
		
		// Crypter le mot de passe avant de créer l'utilisateur
        String motDePasseCrypte = passwordEncoder.encode(motDePasse);
        Utilisateur utilisateur = new Utilisateur(pseudo, nom, prenom, email, telephone, motDePasseCrypte, credit, admin, adresse);
        utilisateurDAO.create(utilisateur);
    }
	
	@Override
	public List<Utilisateur> getUtilisateurs() {
		return utilisateurDAO.findAll();
	}

	@Override
	public Utilisateur findByEmail(String emailUtilisateur) {
		
		// Il nous faut l'utilisateur et les ventes-achats associés ?
		Utilisateur u = utilisateurDAO.read(emailUtilisateur);

		return u;
	}

	@Override
	public Utilisateur findByPseudo(String pseudo) {
		return utilisateurDAO.readByPseudo(pseudo);
	}


	@Override
	public void update(Utilisateur utilisateur) {
		// Validation des données de la couche présentation
		BusinessException be = new BusinessException();
		boolean isValid = true;
		isValid &= validerUtilisateur(utilisateur, be);
		isValid &= validerEmail(utilisateur.getEmail(), be);
		isValid &= validerUniqueEmail(utilisateur.getEmail(), be);
		isValid &= validerUniquePseudo(utilisateur.getPseudo(), be);
		isValid &= validerTelephone(utilisateur.getTelephone(), be);
		isValid &= validerAdresse(utilisateur.getAdresse(), be);
		
		
		if (isValid) {
			try {
				utilisateurDAO.update(utilisateur);
			} catch (DataAccessException e) {// Exception de la couche DAL
				// Rollback automatique
				be.add(BusinessCode.BLL_UTILISATEURS_UPDATE_ERREUR);
				throw be;
			}
		} else {
			throw be;
		}
	}
	

	@Override
	@Transactional
	public void add(Utilisateur utilisateur) {
	    // Validation des données de la couche présentation
	    BusinessException be = new BusinessException();
	    boolean isValid = true;
	    isValid &= validerUtilisateur(utilisateur, be);
	    isValid &= validerEmail(utilisateur.getEmail(), be);
	    isValid &= validerUniqueEmail(utilisateur.getEmail(), be);
	    isValid &= validerUniquePseudo(utilisateur.getPseudo(), be);
	    isValid &= validerTelephone(utilisateur.getTelephone(), be);
	    isValid &= validerMotDePasse(utilisateur.getMotDePasse(), be);  // Validation du mot de passe
	    isValid &= validerMotDePasseConfirmation(utilisateur, be);  // Validation de la confirmation du mot de passe
	    isValid &= validerAdresse(utilisateur.getAdresse(), be);
	    
	    if (isValid) {
	        // Crypter le mot de passe avant de créer l'utilisateur
	        String motDePasseCrypte = passwordEncoder.encode(utilisateur.getMotDePasse());
	        utilisateur.setMotDePasse(motDePasseCrypte);  // Remplacer le mot de passe en clair par celui chiffré
	        
	        utilisateur.getAdresse().setId(verifierEtAffecterAdresse(utilisateur.getAdresse()));
	        
	        utilisateurDAO.create(utilisateur);
	    } else {
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
	
	private boolean validerMotDePasseConfirmation(Utilisateur utilisateur, BusinessException be) {
	    if (utilisateur.getMotDePasseConfirmation() == null || utilisateur.getMotDePasseConfirmation().isBlank()) {
	        be.add(BusinessCode.VALIDATION_UTILISATEUR_CONFIRMATION_PASSWORD_BLANK);
	        return false;
	    }

	    if (!utilisateur.getMotDePasse().equals(utilisateur.getMotDePasseConfirmation())) {
	        be.add(BusinessCode.VALIDATION_UTILISATEUR_PASSWORD_CONFIRMATION_MISMATCH);
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
		String regex1 ="^\\d+\\s[A-Za-zÀ-ÿ0-9\\s\\-]+$";

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

	@Override
	public int uniqueEmail(String email) {
		return 0;
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
	public int uniquePseudo(String pseudo) {
		return 0;
	}

	@Override
	public long verifierEtAffecterAdresse(Adresse adresse) {
		
		long idAdresse = adresseDAO.readAdresseConnue(adresse);
		
		System.out.println( "idAdresse " + idAdresse);
		
		if (idAdresse > 0) {
			return idAdresse;
		} else {
			adresseDAO.create(adresse);
			return adresse.getId();
		}
		
	}
	
			
		
}
