package fr.eni.ecole.projet.encheres.exceptions;

public class BusinessCode {
	// Clefs de validation des BO
	
	
	public static final String VALIDATION_UTILISATEUR_NOM_VIDE = "validation.utilisateur.nom.null";
	public static final String VALIDATION_UTILISATEUR_NOM_TAILLE = "validation.utilisateur.nom.taille";

	public static final String VALIDATION_UTILISATEUR_PRENOM_VIDE = "validation.utilisateur.prenom.null";
	public static final String VALIDATION_UTILISATEUR_PRENOM_TAILLE = "validation.utilisateur.prenom.taille";
	
	public static final String VALIDATION_UTILISATEUR_NULL = "validation.utilisateur.null";
	public static final String VALIDATION_UTILISATEUR_DB_NULL = "validation.utilisateur.db.null";
	public static final String BLL_UTILISATEURS_UPDATE_ERREUR = "miseAjour.utilisateur.erreur";

	public static final String VALIDATION_UTILISATEUR_EMAIL_BLANK = "validation.utilisateur.email.blank";
	public static final String VALIDATION_UTILISATEUR_EMAIL_PATTERN = "validation.utilisateur.email.pattern";
	public static final String VALIDATION_UTILISATEUR_UNIQUE_EMAIL = "validation.utilisateur.unique.email";

	public static final String VALIDATION_UTILISATEUR_UNIQUE_PSEUDO ="validation.utilisateur.unique.pseudo";
	public static final String VALIDATION_UTILISATEUR_PSEUDO_BLANK = "validation.utilisateur.pseudo.blank";
	public static final String VALIDATION_UTILISATEUR_PSEUDO_PATTERN = "validation.utilisateur.pseudo.pattern";

	public static final String VALIDATION_UTILISATEUR_PASSWORD_BLANK = "validation.utilisateur.motDePasse.blank";
	public static final String VALIDATION_UTILISATEUR_PASSWORD_PATTERN = "validation.utilisateur.motDePasse.pattern";

	public static final String VALIDATION_UTILISATEUR_PASSWORD_CONFIRMATION_MISMATCH = "validation.utilisateur.motDePasse.confirmation";
	public static final String VALIDATION_UTILISATEUR_CONFIRMATION_PASSWORD_BLANK = "validation.utilisateur.confirmationMotDePasse.blank";
	public static final String BLL_UTILISATEURS_MOT_DE_PASSE_VIDE="validation.utilisateur.confirmationMotDePasse.blank";
	
	public static final String VALIDATION_UTILISATEUR_RUE_BLANK = "validation.utilisateur.rue.blank";
	public static final String VALIDATION_UTILISATEUR_RUE_PATTERN = "validation.utilisateur.rue.pattern";

	public static final String VALIDATION_UTILISATEUR_CODE_POSTAL_BLANK = "validation.utilisateur.codePostal.blank";
	public static final String VALIDATION_UTILISATEUR_CODE_POSTAL_PATTERN ="validation.utilisateur.codePostal.pattern";

	public static final String VALIDATION_UTILISATEUR_TELEPHONE_PATTERN = "validation.utilisateur.telephone.pattern";

	public static final String VALIDATION_UTILISATEUR_VILLE_BLANK = "validation.utilisateur.ville.blank";
	public static final String VALIDATION_UTILISATEUR_VILLE_LENGTH ="validation.utilisateur.ville.length";

	public static final String VALIDATION_UTILISATEUR_ADRESSE_NULL = "validation.utilisateur.adresse.null";
	public static final String VALIDATION_UTILISATEUR_ADRESSE_ID_INCONNU = "validation.utilisateur.adresse.id_inconnu";

	public static final String VALIDATION_EMAIL = "Validation.email";
	public static final String VALIDATION_DATE_FIN_ENCHERES_FUTURE = "validation.utilisateur.date.fin.future";

	public static final String ERROR_CREATION_ARTICLE = "error.creation.article";


	public static final String VALIDATION_ARTICLE_CATEGORIE_NULL = "validation.article.categorie.null";
	public static final String VALIDATION_ARTICLE_CATEGORIE_ID_INCONNU = "validation.article.categorie.id.inconnu";
	public static final String BLL_UTILISATEURS_MOT_DE_PASSE_INCORRECT = "validation.chgt.motDePasse.incorrect";
	public static final String BLL_UTILISATEURS_MOT_DE_PASSE_CONFIRMATION_INCORRECT = "validation.confirmation.incorrecte";
	public static final String VALIDATION_UTILISATEUR_EMAIL_LENGTH = "Validation.email.taille";
}