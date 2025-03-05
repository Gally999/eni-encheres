package fr.eni.ecole.projet.encheres.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import fr.eni.ecole.projet.encheres.bo.Adresse;
import fr.eni.ecole.projet.encheres.bo.Categorie;
import fr.eni.ecole.projet.encheres.exceptions.BusinessException;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
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
@SessionAttributes({ "categoriesEnSession"})
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
	public String creationArticle(Model model) {
		System.out.println("EncheresController - get formulaire article à créer");
		// TODO vérifier que l'utilisateur•ice est connecté•e pour pouvoir créer un article

		// TODO ajouter l'id du membre en session pour récupérer son adresse
		List<Adresse> adresses = encheresService.consulterAdressesDisponibles();
		if (adresses != null && !adresses.isEmpty()) {
			model.addAttribute("adressesDisponibles", adresses);
		}
		ArticleAVendre article = new ArticleAVendre();
		article.setDateDebutEncheres(LocalDate.now());
		article.setDateFinEncheres(LocalDate.now().plusDays(15));
		model.addAttribute("article", article);
		System.out.println("Article avec dates ? = " + article);
		return "view-article-form";
	}

	@PostMapping("/article/creer")
	public String ajouterArticle(
			@Valid @ModelAttribute("article") ArticleAVendre article,
			BindingResult bindingResult
	) {
		System.out.println("EnchèresController - post formulaire article");
		// article.getVendeur().setPseudo(membreEnSession.getPseudo());
		if (!bindingResult.hasErrors()) {
			try {
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
			return "view-article-form";
		}
	}

	@ModelAttribute("categoriesEnSession")
	public List<Categorie> chargerCategories() {
		System.out.println("EncheresController - charger categories");
		return encheresService.consulterCategories();
	}
	
}
