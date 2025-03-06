package fr.eni.ecole.projet.encheres.controller;

import java.util.Comparator;
import java.util.List;

import fr.eni.ecole.projet.encheres.bo.Adresse;
import fr.eni.ecole.projet.encheres.bo.Categorie;
import fr.eni.ecole.projet.encheres.bo.Utilisateur;
import fr.eni.ecole.projet.encheres.exceptions.BusinessException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;

import fr.eni.ecole.projet.encheres.bll.EncheresService;
import fr.eni.ecole.projet.encheres.bo.ArticleAVendre;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({ "categoriesEnSession", "userEnSession" })
public class EncheresController {
	
	private final EncheresService encheresService;

	public EncheresController(EncheresService encheresService) {
		this.encheresService = encheresService;
	}

	@GetMapping("/")
	public String afficherEncheresActives(Model model) {
		// Récupérer les enchères actives de la BLL
		List<ArticleAVendre> encheresActives = encheresService.consulterEncheresActives();
		// Ajout des enchères actives dans le model
		model.addAttribute("encheresActives", encheresActives);
		return "view-encheres";
	}

	@GetMapping("/article/creer")
	public String creationArticle(
			Model model,
			@ModelAttribute("userEnSession") Utilisateur userEnSession
	) {
		System.out.println("EncheresController - get formulaire article à créer");
		System.out.println("userEnSession" + userEnSession);
		if (userEnSession != null && userEnSession.getPseudo() != null) {
			injectUserAddresses(userEnSession, model);

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
			@ModelAttribute("userEnSession") Utilisateur userEnSession,
			Model model) {
		System.out.println("EnchèresController - post formulaire article");
		System.out.println("errors = " + bindingResult.getAllErrors());
		if (!bindingResult.hasErrors()) {
			try {
				System.out.println("J'entre dans le try");
				System.out.println("userEnSession dans le Enchères Controller" + userEnSession);
				// TODO Pourquoi le userEnSession prend le 'nom' de l'article ?
				article.setVendeur(userEnSession);
				System.out.println("article avec vendeur = " + article);
				encheresService.ajouterArticleAVendre(article);
				return "redirect:/";
			} catch (BusinessException e) {
				e.getClefsExternalisations().forEach(key -> {
					ObjectError error = new ObjectError("globalError", key);
					bindingResult.addError(error);
				});
				return "view-article-form";
			}
		} else {
			injectUserAddresses(userEnSession, model);
			return "view-article-form";
		}
	}

	private void injectUserAddresses(Utilisateur userEnSession, Model model) {
		List<Adresse> adresses = encheresService.consulterAdressesDisponibles(userEnSession.getAdresse().getId());
		if (adresses != null && !adresses.isEmpty()) {
			Comparator<Adresse> comparator = (a1, a2) -> {
				if (a1.getId() == userEnSession.getAdresse().getId()) return -1;
				if (a2.getId() == userEnSession.getAdresse().getId()) return 1;
				return a1.compareTo(a2);
			};
			adresses.sort(comparator);
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
