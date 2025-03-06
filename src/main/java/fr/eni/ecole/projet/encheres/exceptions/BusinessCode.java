package fr.eni.ecole.projet.encheres.exceptions;

public class BusinessCode {
	//Clefs de validation des BO
	public static final String VALIDATION_UTILISATEUR_NULL = "validation.utilisateur.null";
	
	public static final String VALIDATION_UTILISATEUR_EMAIL_BLANK = "validation.utilisateur.email.blank";
	public static final String VALIDATION_UTILISATEUR_EMAIL_PATTERN = "validation.utilisateur.email.pattern";
	public static final String VALIDATION_UTILISATEUR_UNIQUE_EMAIL = "validation.utilisateur.unique.email";
	
	public static final String VALIDATION_UTILISATEUR_DB_NULL = "validation.utilisateur.db.null";
	
	
	public static final String VALIDATION_UTILISATEUR_UNIQUE_PSEUDO ="validation.utilisateur.unique.pseudo";
	public static final String VALIDATION_UTILISATEUR_PSEUDO_BLANK = "validation.utilisateur.pseudo.blank";
	public static final String VALIDATION_UTILISATEUR_PSEUDO_PATTERN = "validation.utilisateur.pseudo.pattern";
	public static final String VALIDATION_UTILISATEUR_PASSWORD_BLANK = "validation.utilisateur.motDePasse.blank";
	public static final String VALIDATION_UTILISATEUR_PASSWORD_PATTERN = "validation.utilisateur.motDePasse.pattern";
	public static final String VALIDATION_UTILISATEUR_RUE_BLANK = "validation.utilisateur.rue.blank";
	public static final String VALIDATION_UTILISATEUR_CODE_POSTAL_BLANK = "validation.utilisateur.codePostal.blank";
	public static final String VALIDATION_UTILISATEUR_VILLE_BLANK = "validation.utilisateur.ville.blank";
	public static final String VALIDATION_UTILISATEUR_VILLE_LENGTH ="validation.utilisateur.ville.length";
	public static final String VALIDATION_UTILISATEUR_RUE_PATTERN = "validation.utilisateur.rue.pattern";
	public static final String VALIDATION_UTILISATEUR_CODE_POSTAL_PATTERN ="validation.utilisateur.codePostal.pattern";
	public static final String VALIDATION_UTILISATEUR_TELEPHONE_PATTERN = "validation.utilisateur.telephone.pattern";
	public static final String VALIDATION_UTILISATEUR_ADRESSE_NULL = "validation.utilisateur.adresse.null";
	public static final String VALIDATION_UTILISATEUR_CONFIRMATION_PASSWORD_BLANK = "validation.utilisateur.confirmationMotDePasse.blank";
	public static final String VALIDATION_UTILISATEUR_PASSWORD_CONFIRMATION_MISMATCH = "validation.utilisateur.motDePasse.confirmation";
	public static final String VALIDATION_DATE_FIN_ENCHERES_FUTURE = "validation.utilisateur.dateFinEncheres.future";
	public static final String ERROR_CREATION_ARTICLE = "error.creation.article";
	public static final String VALIDATION_UTILISATEUR_UNIQUE_EMAIL_ERROR = "validation.utilisateur.uniqueEmailError";

	public static final String BLL_UTILISATEURS_UPDATE_ERREUR = null;
}