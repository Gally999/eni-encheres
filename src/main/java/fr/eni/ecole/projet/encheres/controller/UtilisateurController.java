package fr.eni.ecole.projet.encheres.controller;



import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.eni.ecole.projet.encheres.bll.UtilisateurService;
import fr.eni.ecole.projet.encheres.bo.Adresse;
import fr.eni.ecole.projet.encheres.bo.Utilisateur;
import fr.eni.ecole.projet.encheres.exceptions.BusinessException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.validation.Valid;

@Controller

public class UtilisateurController {
	
	
	private UtilisateurService utilisateurService;
	
	
	public UtilisateurController (UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}
		
	
	// MODIFIER MOT DE PASSE
	@GetMapping("/modifier-motDePasse")
	public String modifierMotDePasse(Principal principal, Model model) {
	    
		// Récupérer l'utilisateur connecté à partir du principal
		String pseudo = principal.getName();  
	    Utilisateur utilisateur = utilisateurService.findByPseudo(pseudo);
	    
	    System.out.println("Pseudo : " + utilisateur.getPseudo());
    	if (utilisateur != null) {
	            model.addAttribute("utilisateur", utilisateur);  
	            return "view-modifier-motDePasse"; 
	     } else {
	            System.out.println("Aucune adresse trouvée.");
	            return "view-modifier-motDePasse"; 
	     }
	}
	
	
	@PostMapping("/modifier-motDePasse")
	public String modifierMotDePasse(
	        @Validated 
	        @ModelAttribute("utilisateur") Utilisateur utilisateur,
	        BindingResult bindingResult, Principal principal) {
	    
	   
	    System.out.println("Entrée dans la méthode modifierMotDePasse");
		
	    // Récupérer le pseudo à partir du principal
	    String pseudo = principal.getName();
	    

	    System.out.println("Mot de passe saisi : " + utilisateur.getMotDePasseSaisi());
	    System.out.println("Mot de passe new : " + utilisateur.getMotDePasseNew());
	    System.out.println("Mot de passe confirmation : " + utilisateur.getMotDePasseConfirmation());
	    System.out.println("Pseudo : " + utilisateur.getPseudo());
	    
	    
	     // Vérification que le nouveau mot de passe et la confirmation sont identiques
	    if (utilisateur.getMotDePasseSaisi() == null || !utilisateur.getMotDePasseConfirmation().equals(utilisateur.getMotDePasseNew())) {
	        System.out.println("Erreur: Les mots de passe ne correspondent pas");
	        bindingResult.rejectValue("motDePasseConfirmation", "validation.utilisateur.motDePasse.confirmation");
	    }
	    // Si des erreurs de validation existent, renvoyer à la page de modification du mot de passe
//	    if (bindingResult.hasErrors()) {
//	        System.out.println("Il y a des erreurs de validation.");
//	     // Affiche les erreurs de validation dans la console pour déboguer
//	        bindingResult.getAllErrors().forEach(error -> System.out.println("Erreur : " + error.getDefaultMessage()));
//	        return "view-modifier-motDePasse";
//	        
//	    } else {
	        try {
	            	        	
	            System.out.println("Entrée dans le try");
	            
	            // Mise à jour du mot de passe de l'utilisateur
	            utilisateurService.mettreAjourMotDePasse(utilisateur);
	            
	            System.out.println("Le mot de passe de l'utilisateur mis à jour avec succès.");	            
	            
	            
	            return "view-modifier-motDePasse";
	            
	        } catch (BusinessException e) {
	            // Gestion des erreurs de business logic
	            e.getClefsExternalisations().forEach(key -> {
	                ObjectError error = new ObjectError("globalError", key);
	                bindingResult.addError(error);
	            });
	            return "view-modifier-motDePasse";
	        }
	    }
	//}


//    
//	// SUPPRIMER MON PROFIL
//    @PostMapping("/supprimer-compte")
//	public String supprimerCompte(Principal principal, RedirectAttributes redirectAttributes) {
//	    System.out.println("Requête POST reçue pour supprimer le compte");
//
//	    String pseudo = principal.getName();
//	    System.out.println("Pseudo de l'utilisateur connecté : " + pseudo);
//
//	    // Supprimer l'utilisateur de la base de données
//	    boolean success = utilisateurService.supprimerUtilisateur(pseudo);
//
//	    if (success) {
//	        // Si la suppression a réussi
//	        redirectAttributes.addFlashAttribute("message", "Votre compte a été supprimé avec succès.");
//	        return "view-utilisateur-creer";
//	    } else {
//	        // En cas d'échec
//	        redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression du compte.");
//	        return "redirect:/monProfil/detail";
//	    }
//	}

	//PAGE MODIFIER MON PROFIL
  	@PostMapping("/monProfil/detail")
	public String modifierProfil(
	        @Valid 
	        @ModelAttribute("utilisateur") 
	        Utilisateur utilisateur,
	        BindingResult bindingResult) {
	    
	    System.out.println("Entrée dans la méthode modifierProfil");

	    // Vérification des erreurs de validation
	    if (bindingResult.hasErrors()) {
	        System.out.println("Il y a des erreurs de validation.");
	        return "view-mon-profil-detail";
	    } else {
	        try {
	        	// On ne modifie pas le pseudo, on le réaffecte à l'ancien pseudo
	            String pseudoActuel = utilisateur.getPseudo();
	            utilisateur.setPseudo(pseudoActuel);
	            
	            // On ne modifie pas le credit, on le réaffecte à l'ancien pseudo
	            int creditActuel = utilisateur.getCredit();
	            utilisateur.setCredit(creditActuel);
	        	
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
	    int credit = utilisateurService.getCreditDeLUtilisateurConnecte(pseudo);
	    Adresse adresse = utilisateurService.getAdresseDeLUtilisateurConnecte(pseudo);

	    // Vérifier si l'adresse est correctement récupérée
	    System.out.println("Adresse : " + adresse);
	    if (adresse != null) {
	    	System.out.println("Adresse : " + adresse);
	    	System.out.println("Rue: " + adresse.getRue());
	        System.out.println("Code Postal: " + adresse.getCodePostal());
	        System.out.println("Ville: " + adresse.getVille());
	    } else {
	        System.out.println("Aucune adresse trouvée.");
	    }

	    // Passer l'objet adresse au modèle
	    model.addAttribute("utilisateur", utilisateur);
	    model.addAttribute("adresse", adresse);
	    model.addAttribute("credit", credit);
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
		
		
		// Récupération de l'objet utilisateur du formulaire
		// sauvegarde
		@PostMapping("/creer")
		public String creerUtilisateur(
		        @Valid 
		        @ModelAttribute("utilisateur") Utilisateur utilisateur,
		        BindingResult bindingResult) {

		    // Définir un crédit par défaut 
		    utilisateur.setCredit(10); 

	    // Vérification côté serveur que les mots de passe correspondent
	    if (utilisateur.getMotDePasse() == null || !utilisateur.getMotDePasse().equals(utilisateur.getMotDePasseConfirmation())) {
		        bindingResult.rejectValue("motDePasseConfirmation", "validation.utilisateur.motDePasse.confirmation");
		    }

		    if (bindingResult.hasErrors()) {
		        return "view-utilisateur-creer";
		    } else {
		        try {
		            utilisateur.setCredit(10);
		            utilisateurService.add(utilisateur);  
		            return "redirect:/";  
		        } catch (BusinessException e) {
		            // Ajouter les erreurs au BindingResult pour les afficher dans le formulaire
		            e.getClefsExternalisations().forEach(key -> {
		                ObjectError error = new ObjectError("globalError", key);
		                bindingResult.addError(error);
		            });
		            return "view-utilisateur-creer";
		        }
		    }
		}
	
	}

	
	
	
	
