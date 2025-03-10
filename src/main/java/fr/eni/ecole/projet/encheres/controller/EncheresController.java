package fr.eni.ecole.projet.encheres.controller;

import java.security.Principal;
import java.util.List;

import fr.eni.ecole.projet.encheres.bll.UtilisateurService;
import fr.eni.ecole.projet.encheres.bo.Adresse;
import fr.eni.ecole.projet.encheres.bo.Categorie;
import fr.eni.ecole.projet.encheres.bo.Utilisateur;
import fr.eni.ecole.projet.encheres.exceptions.BusinessException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import fr.eni.ecole.projet.encheres.bll.EncheresService;
import fr.eni.ecole.projet.encheres.bo.ArticleAVendre;

@Controller
@SessionAttributes({ "categoriesEnSession" })
public class EncheresController {
	
	private final EncheresService encheresService;
	private final UtilisateurService utilisateurService;

	public EncheresController(EncheresService encheresService, UtilisateurService utilisateurService) {
		this.encheresService = encheresService;
    this.utilisateurService = utilisateurService;
  }

	@GetMapping("/")
	public String afficherEncheresActives(
			@RequestParam(name = "categorieId", required = false) Long categorieId,
			@RequestParam(value = "keyword", required = false) String keyword,
			Model model
	) {
		// Récupérer les enchères actives de la BLL
		List<ArticleAVendre> encheresActives = encheresService.consulterEncheresActives(categorieId, keyword);
		// Ajout des enchères actives dans le model
		model.addAttribute("encheresActives", encheresActives);
		model.addAttribute("keyword", keyword);
		model.addAttribute("categorieId", categorieId);
		return "view-encheres";
	}

	@GetMapping("/article/creer")
	public String creationArticle(
			Model model,
			Principal principal
	) {
		System.out.println("EncheresController - get formulaire article à créer");
		if (principal != null && principal.getName() != null) {
			injectUserAddresses(principal.getName(), model);
			ArticleAVendre article = new ArticleAVendre();
			System.out.println("Article = " + article);
			model.addAttribute("article", article);
			return "view-article-form";
		}
		return "redirect:/";
	}

	@PostMapping("/article/creer")
	public String ajouterArticle(
			@Valid @ModelAttribute("article") ArticleAVendre article,
			BindingResult bindingResult,
			Model model,
			Principal principal
	) {
		System.out.println("EnchèresController - post formulaire article");
		System.out.println("errors = " + bindingResult.getAllErrors());
		if (!bindingResult.hasErrors()) {
			try {
				Utilisateur user = utilisateurService.findByPseudo(principal.getName());
				article.setVendeur(user);

				encheresService.ajouterArticleAVendre(article);
				return "redirect:/";
				
			} catch (BusinessException e) {
				injectUserAddresses(principal.getName(), model);
				e.getClefsExternalisations().forEach(key -> {
					ObjectError error = new ObjectError("globalError", key);
					bindingResult.addError(error);
				});
				return "view-article-form";
			}
		} else {
			injectUserAddresses(principal.getName(), model);
			return "view-article-form";
		}
	}

	private void injectUserAddresses(String userPseudo, Model model) {
		Utilisateur user = utilisateurService.findByPseudo(userPseudo);
		List<Adresse> adresses = encheresService.consulterAdressesDisponibles(user.getAdresse().getId());
		if (adresses != null && !adresses.isEmpty()) {
			model.addAttribute("adressesDisponibles", adresses);
		}
	}

	@ModelAttribute("categoriesEnSession")
	public List<Categorie> chargerCategories() {
		System.out.println("EncheresController - charger categories");
		List<Categorie> categories = encheresService.consulterCategories();
		System.out.println("categories =" + categories);
		return categories;
	}
	
}
