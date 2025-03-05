package fr.eni.ecole.projet.encheres.controller;

import java.util.List;

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

@Controller
public class EncheresController {
	
	private EncheresService encheresService;

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
		// vérifier que l'utilisateur•ice est connecté•e pour pouvoir créer un article
		model.addAttribute("article", new ArticleAVendre());
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
	
}
