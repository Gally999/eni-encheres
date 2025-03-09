package fr.eni.ecole.projet.encheres.controller;



import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import fr.eni.ecole.projet.encheres.bll.UtilisateurService;
import fr.eni.ecole.projet.encheres.bo.Adresse;
import fr.eni.ecole.projet.encheres.bo.Utilisateur;
import fr.eni.ecole.projet.encheres.exceptions.BusinessException;
import jakarta.validation.Valid;

@Controller
public class UtilisateurController {
	
	
	// Injection de UtilisateurService
	@Autowired
    private UtilisateurService utilisateurService;
	
	
	public UtilisateurController (UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}
		
	//PAGE MODIFIER MON PROFIL

	
	
    
	@GetMapping("/monProfil/modifier")
	public String afficherProfil(Principal principal, Model model) {
	    String pseudo = principal.getName();  

	    Utilisateur utilisateur = utilisateurService.findByPseudo(pseudo);
	    int credit = utilisateurService.getCreditDeLUtilisateurConnecte(pseudo);
	    Adresse adresse = utilisateurService.getAdresseDeLUtilisateurConnecte(pseudo);

	    // Vérifier si l'adresse est correctement récupérée
	    System.out.println("Adresse : " + adresse);
	    if (adresse != null) {
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

	    return "view-mon-profil-modifier"; 
	}


	   
	    
	    
	    
	
	@PostMapping("/monProfil/modifier")
	public String modifierProfil(
			@Valid
			@ModelAttribute("utilisateur") Utilisateur u, BindingResult bindinResult) {
		
		if (bindinResult.hasErrors()) {
			return "view-utilisateur-detail";
		} else {
			try{
				System.out.println("L’utilisateur récupéré depuis le formulaire : ");
			
				System.out.println(u);
			
				//Sauvegarder les modifications
				utilisateurService.update(u);
			
				// Redirection l’affichage à la page de connexion
				return "redirect:/login";
			} catch (BusinessException e) {
			
				//Afficher les messages d’erreur - il faut les injecter dans le contexte de BindingResult
				e.getClefsExternalisations().forEach(key -> {
				ObjectError error = new ObjectError("globalError", key);
				bindinResult.addError(error);
				});
				return "redirect:/monProfil/modifier";
			}
		}
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
		            utilisateurService.add(utilisateur);  // Ajoutez l'utilisateur
		            return "redirect:/";  // Redirection après inscription réussie
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

	
	
	
	
