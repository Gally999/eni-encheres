package fr.eni.ecole.projet.encheres.controller;

import fr.eni.ecole.projet.encheres.bll.EncheresService;
import fr.eni.ecole.projet.encheres.bll.UtilisateurService;
import fr.eni.ecole.projet.encheres.bo.*;
import fr.eni.ecole.projet.encheres.exceptions.BusinessException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/article")
@SessionAttributes({ "categoriesEnSession" })
public class ArticleController {

  private final EncheresService encheresService;
  private final UtilisateurService utilisateurService;

  public ArticleController(EncheresService encheresService, UtilisateurService utilisateurService) {
    this.encheresService = encheresService;
    this.utilisateurService = utilisateurService;
  }

  @GetMapping("/creer")
  public String creationArticle(
      Model model,
      Principal principal
  ) {
    System.out.println("EncheresController - get formulaire article à créer");
    if (principal != null && principal.getName() != null) {
      injectUserAddresses(principal.getName(), model);
      ArticleAVendre article = new ArticleAVendre();
      model.addAttribute("article", article);
      return "view-article-form";
    }
    return "redirect:/";
  }

  @PostMapping("/creer")
  public String ajouterArticle(
      @Valid @ModelAttribute("article") ArticleAVendre article,
      BindingResult bindingResult,
      Model model,
      Principal principal
  ) {
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

  @GetMapping("/detail")
  public String afficherDetailArticle(
      @RequestParam(name = "id", required = true) long id,
      Model model
  ) {
    if (id > 0) {
      ArticleAVendre article = encheresService.consulterArticle(id);
      if (article != null) {
        model.addAttribute("article", article);

        if (article.getCategorie() != null && article.getCategorie().getId() > 0) {
          Categorie categorie = encheresService.consulterCategorieParId(article.getCategorie().getId());
          if (categorie != null) {
            model.addAttribute("categorie", categorie);
          }
        }
        if (article.getAdresseRetrait() != null && article.getAdresseRetrait().getId() > 0) {
          Adresse adresse = encheresService.consulterAdresseParId(article.getAdresseRetrait().getId());
          if (adresse != null) {
            model.addAttribute("adresse", adresse);
          }
        }
        if (article.getId() > 0) {
          Enchere enchere = encheresService.consulterMeilleureEnchere(article.getId());
          model.addAttribute("meilleureEnchere", enchere);
        }
        return "view-article-detail";
      } else {
        System.out.println("Article inconnu");
      }
    } else {
      System.out.println("Identifiant inconnu");
    }
    return "view-encheres";
  }

  private void injectUserAddresses(String userPseudo, Model model) {
    Utilisateur user = utilisateurService.findByPseudo(userPseudo);
    List<Adresse> adresses = encheresService.consulterAdressesDisponibles(user.getAdresse().getId());
    if (adresses != null && !adresses.isEmpty()) {
      model.addAttribute("adressesDisponibles", adresses);
    }
  }

}
