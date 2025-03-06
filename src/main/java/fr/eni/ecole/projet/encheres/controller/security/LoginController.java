package fr.eni.ecole.projet.encheres.controller.security;

import fr.eni.ecole.projet.encheres.bll.UtilisateurService;
import fr.eni.ecole.projet.encheres.bo.Utilisateur;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.security.Principal;

@Controller
@SessionAttributes({ "userEnSession" })
public class LoginController {

	private final UtilisateurService utilisateurService;

  public LoginController(UtilisateurService utilisateurService) {
    this.utilisateurService = utilisateurService;
  }

  @GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/session")
	public String chargerUserEnSession(
			@ModelAttribute("userEnSession") Utilisateur userEnSession,
			Principal principal
	) {
		Utilisateur userACharger = utilisateurService.findByPseudo(principal.getName());
		System.out.println("userACharger /session = " + userACharger);
		if (userACharger != null) {
			userEnSession.setPseudo(userACharger.getPseudo());
			userEnSession.setNom(userACharger.getNom());
			userEnSession.setPrenom(userACharger.getPrenom());
			userEnSession.setEmail(userACharger.getEmail());
			userEnSession.setCredit(userACharger.getCredit());
			userEnSession.setTelephone(userACharger.getTelephone());
			userEnSession.setAdresse(userACharger.getAdresse());
			userEnSession.setAdmin(userACharger.isAdmin());
		} else {
			userEnSession.setPseudo(null);
			userEnSession.setNom(null);
			userEnSession.setPrenom(null);
			userEnSession.setEmail(null);
			userEnSession.setCredit(0);
			userEnSession.setTelephone(null);
			userEnSession.setAdresse(null);
			userEnSession.setAdmin(false);
		}
		return "redirect:/";
	}

	@ModelAttribute("userEnSession")
	public Utilisateur userEnSession() {
		System.out.println("Add Attribut userEnSession Session by Login Controller");
		return new Utilisateur();
	}

}
