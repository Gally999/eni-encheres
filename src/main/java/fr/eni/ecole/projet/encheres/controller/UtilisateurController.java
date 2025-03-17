package fr.eni.ecole.projet.encheres.controller;



import java.security.Principal;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.eni.ecole.projet.encheres.bll.UtilisateurService;
import fr.eni.ecole.projet.encheres.bo.Adresse;
import fr.eni.ecole.projet.encheres.bo.Utilisateur;
import fr.eni.ecole.projet.encheres.exceptions.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@Validated
public class UtilisateurController {


	private UtilisateurService utilisateurService;


	public UtilisateurController (UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}


	// MODIFIER MOT DE PASSE
	@GetMapping("/modifier-motDePasse")
	public String modifierMotDePasse(Principal principal, Model model) {
		String pseudo = principal.getName();
		model.addAttribute("pseudo", pseudo);
		return "view-modifier-motDePasse";
	}

	// Traite la demande de modification du mot de passe
	@PostMapping("/modifier-motDePasse")
	public String modifierMotDePasse(
			@RequestParam("motDePasseSaisi") String motDePasseSaisi,
			@RequestParam("motDePasseNew") String motDePasseNew,
			@RequestParam("motDePasseConfirmation") String motDePasseConfirmation,
			Principal principal,
			Model model) {

		String pseudo = principal.getName();
		Utilisateur utilisateur = utilisateurService.findByPseudo(pseudo);

		// Vérification que l'utilisateur existe
		if (utilisateur == null) {
			model.addAttribute("message", "Utilisateur non trouvé");
			return "view-modifier-motDePasse";
		}

		// Vérification que les mots de passe ne sont pas vides et qu'ils correspondent
		if (motDePasseNew == null || motDePasseNew.trim().isEmpty() || motDePasseConfirmation == null || motDePasseConfirmation.trim().isEmpty()) {
			model.addAttribute("message", "Veuillez saisir les deux nouveaux mots de passe");
			return "view-modifier-motDePasse";
		}

		// Vérification que le nouveau mot de passe et la confirmation sont identiques
		if (!motDePasseNew.equals(motDePasseConfirmation)) {
			model.addAttribute("message", "Les mots de passe ne correspondent pas");
			return "view-modifier-motDePasse";
		}

		// Validation du mot de passe
		if (!isValid(motDePasseNew)) {
			model.addAttribute("message", "Le nouveau mot de passe ne respecte pas les critères de validité");
			return "view-modifier-motDePasse";
		}

		try {
			// Mise à jour du mot de passe de l'utilisateur
			utilisateurService.mettreAjourMotDePasse(motDePasseNew, motDePasseSaisi, utilisateur);
			model.addAttribute("message", "Mot de passe mis à jour avec succès");
		} catch (BusinessException e) {
			// Si une exception est levée par le service
			model.addAttribute("message", e.getMessage());
		}

		return "view-modifier-motDePasse";
	}


	// Méthode de validation du mot de passe
	private boolean isValid(String motDePasse) {
		// Vérifie si le mot de passe a une longueur entre 8 et 20 caractères,
		// contient au moins une majuscule, une minuscule, un caractère spécial et un chiffre
		String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_])(?=.*[0-9]).{8,20}$";
		return motDePasse.matches(regex);
	}


	// SUPPRIMER MON PROFIL


	@PostMapping("/supprimer-compte")
	public String supprimerCompte(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		// Récupérer le nom de l'utilisateur connecté
		String pseudo = SecurityContextHolder.getContext().getAuthentication().getName();

		// Supprimer l'utilisateur de la base de données
		boolean success = utilisateurService.supprimerUtilisateur(pseudo);

		if (success) {
			// Si la suppression a réussi, on déconnecte l'utilisateur
			org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null) {
				// Passer request et response pour effectuer correctement le logout
				new SecurityContextLogoutHandler().logout(request, response, authentication);
			}

			// Ajouter un message flash pour indiquer la suppression réussie
			redirectAttributes.addFlashAttribute("message", "Votre compte a été supprimé avec succès.");

			// Rediriger vers la page de création d'utilisateur (mode déconnecté)
			return "redirect:/creer"; 
		} else {
			// En cas d'échec, ajouter un message d'erreur
			redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression du compte.");
			return "redirect:/monProfil/detail"; 
		}
	}


	//PAGE MODIFIER MON PROFIL
	@PostMapping("/monProfil/detail")
	public String modifierProfil(
			@Valid 
			@ModelAttribute("utilisateur") 
			Utilisateur utilisateur,
			BindingResult bindingResult,
			Model model) {

		System.out.println("Entrée dans la méthode modifierProfil");

		// Vérification des erreurs de validation
		if (bindingResult.hasErrors()) {
			System.out.println("Il y a des erreurs de validation.");
			model.addAttribute("utilisateur", utilisateur);
			return "view-mon-profil-detail";
		} else {
			try {

				System.out.println(utilisateur);

				// Mise à jour de l'utilisateur
				utilisateurService.mettreAjourUtilisateur(utilisateur);

				System.out.println("Utilisateur et adresse mis à jour avec succès.");
				return "view-mon-profil-detail";
			} catch (BusinessException e) {
				// Gestion des erreurs de business logic
				e.getClefsExternalisations().forEach(key -> {
					ObjectError error = new ObjectError("globalError", key);
					bindingResult.addError(error);
				});
				return "view-mon-profil-detail";
			}
		}
	}



	@GetMapping("/monProfil/detail")
	public String afficherProfil(Principal principal, Model model) {
		String pseudo = principal.getName();  

		Utilisateur utilisateur = utilisateurService.findByPseudo(pseudo);

		Adresse adresse = utilisateurService.getAdresseDeLUtilisateurConnecte(pseudo);

		// Vérifier si l'adresse est correctement récupérée
		System.out.println("Adresse : " + adresse);
		if (adresse != null) {
			System.out.println("Adresse : " + adresse);
			System.out.println("Rue: " + adresse.getRue());
			System.out.println("Code Postal: " + adresse.getCodePostal());
			System.out.println("Ville: " + adresse.getVille());
			utilisateur.setAdresse(adresse);
		} else {
			System.out.println("Aucune adresse trouvée.");
			adresse = new Adresse();      

		}

		// Passer l'objet adresse au modèle
		model.addAttribute("utilisateur", utilisateur);
		System.out.println("Adresse dans le modèle : " + model.getAttribute("adresse"));

		return "view-mon-profil-detail"; 
	}



	//PAGE MON PROFIL:
	@GetMapping("/monProfil")
	public String detailUtilisateur(Principal principal, Model model) {
		// Récupérer le pseudo de l'utilisateur connecté
		String pseudo = principal.getName();
		System.out.println("Pseudo de l'utilisateur connecté : " + pseudo);

		// Récupérer l'utilisateur à partir de son pseudo
		Utilisateur utilisateur = utilisateurService.findByPseudo(pseudo);
		if (utilisateur != null) {
			System.out.println("Utilisateur trouvé : " + utilisateur.getNom() + " " + utilisateur.getPrenom());
		} else {
			System.out.println("Utilisateur non trouvé !");
		}


		String telephone = utilisateurService.getTelephoneDeLUtilisateurConnecte(pseudo);
		System.out.println("Téléphone récupéré : " + telephone);


		model.addAttribute("utilisateur", utilisateur);
		model.addAttribute("telephone", telephone);

		return "view-mon-profil"; 
	}

	//PAGE S'INSCRIRE:		
	// Création d'un nouvel utilisateur
	@GetMapping("/creer")
	public String creerUtilisateur(Model model) {
		Utilisateur utilisateur = new Utilisateur();
		// Ajout de l'instance dans le modèle
		model.addAttribute("utilisateur", utilisateur);
		return "view-utilisateur-creer";
	}


	@PostMapping("/creer")
	public String creerUtilisateur(
			@RequestParam("motDePasse") String motDePasse,  
			@RequestParam("motDePasseConfirmation") String motDePasseConfirmation,  
			@ModelAttribute("utilisateur") Utilisateur utilisateur,  
			BindingResult bindingResult, 
			Model model) {

		System.out.println("Mot de passe saisi : " + motDePasse);
		System.out.println("Mot de passe de confirmation : " + motDePasseConfirmation);

		if (motDePasse == null || !motDePasse.equals(motDePasseConfirmation)) {
			System.out.println("Erreur : les mots de passe ne correspondent pas.");
			bindingResult.rejectValue("motDePasseConfirmation", "validation.utilisateur.motDePasse.confirmation");
		} else {
			System.out.println("Les mots de passe correspondent.");
		}

		if (bindingResult.hasErrors()) {
			System.out.println("Des erreurs de validation ont été détectées.");
			bindingResult.getAllErrors().forEach(error -> {
				System.out.println("Erreur : " + error.getDefaultMessage());
			});
			return "view-utilisateur-creer";
		} else {
			System.out.println("Aucune erreur de validation. Création de l'utilisateur.");

			utilisateur.setMotDePasse(motDePasse);  
			utilisateur.setCredit(10);

			try {
				System.out.println("Tentative d'ajouter l'utilisateur dans la base de données.");
				utilisateurService.add(utilisateur);

				model.addAttribute("popupMessage", "Votre compte est créé avec succès, félicitations !");
				model.addAttribute("popupType", "success");

				System.out.println("Utilisateur ajouté avec succès.");
				return "redirect:/";
			} catch (BusinessException e) {
				System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
				if (e.getClefsExternalisations() != null) {
					e.getClefsExternalisations().forEach(error -> {
						System.out.println("Erreur d'externalisation : " + error);
					});
				}

				if (e.getClefsExternalisations() != null) {
					e.getClefsExternalisations().forEach(key -> {
						ObjectError error = new ObjectError("globalError", key);
						bindingResult.addError(error);
						System.out.println("Erreur globale : " + key);
					});
				}
				return "view-utilisateur-creer";
			}
		}
	}
}
