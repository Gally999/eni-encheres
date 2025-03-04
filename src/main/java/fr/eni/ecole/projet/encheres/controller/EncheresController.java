package fr.eni.ecole.projet.encheres.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fr.eni.ecole.projet.encheres.bll.EncheresService;
import fr.eni.ecole.projet.encheres.bo.ArticleAVendre;

@Controller
public class EncheresController {
	
	private EncheresService encheresService;
	
	
	public EncheresController(EncheresService encheresService) {
		this.encheresService = encheresService;
	}


	public void welcome() {
		System.out.println ("Test Welcome");
	}
	
	@GetMapping("/")
	public String afficherEncheresActives(Model model) {
		// Récupérer les enchères actives de la BLL
		List<ArticleAVendre> encheresActives = encheresService.consulterEncheresActives();
		// Ajout des enchères actives dans le model
		model.addAttribute("encheresActives", encheresActives);
		
		return "view-encheres";
	}
	
}
