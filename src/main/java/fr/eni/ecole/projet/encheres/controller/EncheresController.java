package fr.eni.ecole.projet.encheres.controller;

import java.security.Principal;
import java.util.List;

import fr.eni.ecole.projet.encheres.bo.Categorie;
import fr.eni.ecole.projet.encheres.enums.AchatFilter;
import fr.eni.ecole.projet.encheres.enums.FilterMode;
import fr.eni.ecole.projet.encheres.enums.VenteFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fr.eni.ecole.projet.encheres.bll.EncheresService;
import fr.eni.ecole.projet.encheres.bo.ArticleAVendre;

@Controller
@SessionAttributes({ "categoriesEnSession" })
public class EncheresController {
	
	private final EncheresService encheresService;

	public EncheresController(EncheresService encheresService) {
		this.encheresService = encheresService;
  }


//	@GetMapping("/")
//	public String afficherEncheresActives(
//			@RequestParam(name = "categorieId", required = false) Long categorieId,
//			@RequestParam(value = "keyword", required = false) String keyword,
//			@RequestParam(value = "achatsOuVentes", required = false, defaultValue = "0") FilterMode achatsOuVentes, // 0 ou 1
//			@RequestParam(value = "achats", required = false, defaultValue = "0") AchatFilter achats,
//			@RequestParam(value = "ventes", required = false, defaultValue = "1") VenteFilter ventes,
//			Model model,
//			Principal principal
//	) {
//		System.out.println("EncheresController");
//		model.addAttribute("achatsOuVentes", achatsOuVentes.getValue());
//		model.addAttribute("achats", achats.getValue());
//		model.addAttribute("ventes", ventes.getValue());
//
//		if (principal == null || principal.getName() == null) {
//			// Récupérer les enchères actives de la BLL en mode déconnecté
//			List<ArticleAVendre> encheresActives = encheresService.consulterEncheresActives(categorieId, keyword);
//			model.addAttribute("encheresActives", encheresActives);
//		} else {
//			System.out.println("connexion par défaut");
//			// Récupérer les enchères actives de la BLL en mode connecté
//			List<ArticleAVendre> encheresFiltrees = encheresService.consulterEncheresActives(categorieId, keyword, achatsOuVentes.getValue() == 0 ? achats : ventes);
//			model.addAttribute("encheresActives", encheresFiltrees);
//		}
//		// Ajout des enchères actives dans le model
//		model.addAttribute("categorieId", categorieId);
//		model.addAttribute("keyword", keyword);
//
//		return "view-encheres";
//	}

	@GetMapping("/")
	public String afficherEncheresActives(
			@RequestParam(name = "categorieId", required = false) Long categorieId,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "achatsOuVentes", required = false, defaultValue = "0") FilterMode achatsOuVentes, // 0 ou 1
			@RequestParam(value = "achats", required = false, defaultValue = "0") AchatFilter achats,
			@RequestParam(value = "ventes", required = false, defaultValue = "1") VenteFilter ventes,
			Model model,
			Principal principal
	) {
		model.addAttribute("achatsOuVentes", achatsOuVentes.getValue());
		model.addAttribute("achats", achats.getValue());
		model.addAttribute("ventes", ventes.getValue());

		if (principal == null || principal.getName() == null) {
			// Récupérer les enchères actives de la BLL en mode déconnecté
			List<ArticleAVendre> encheresActives = encheresService.consulterEncheresActives(categorieId, keyword);
			model.addAttribute("encheresActives", encheresActives);
		} else {
			// Récupérer les enchères actives de la BLL en mode connecté
			List<ArticleAVendre> encheresFiltrees = encheresService.consulterEncheresActives(categorieId, keyword, achatsOuVentes.getValue() == 0 ? achats : ventes);
			model.addAttribute("encheresActives", encheresFiltrees);
		}
		// Ajout des enchères actives dans le model
		model.addAttribute("categorieId", categorieId);
		model.addAttribute("keyword", keyword);

		return "view-encheres";
	}


	@ModelAttribute("categoriesEnSession")
	public List<Categorie> chargerCategories() {
		System.out.println("EncheresController - charger categories");
		List<Categorie> categories = encheresService.consulterCategories();
		System.out.println("categories =" + categories);
		return categories;
	}
}
