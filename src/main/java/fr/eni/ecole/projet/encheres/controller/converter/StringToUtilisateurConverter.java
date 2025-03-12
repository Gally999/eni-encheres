package fr.eni.ecole.projet.encheres.controller.converter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import fr.eni.ecole.projet.encheres.bll.UtilisateurService;
import fr.eni.ecole.projet.encheres.bo.Utilisateur;

@Component
public class StringToUtilisateurConverter implements Converter<String, Utilisateur> {

  // Injection des Services
  private final UtilisateurService utilisateurService;

  // Constructeur pour l'injection de dépendance
  public StringToUtilisateurConverter(UtilisateurService utilisateurService) {
    this.utilisateurService = utilisateurService;
  }

  @Override
  public Utilisateur convert(String pseudo) {
    // Appel au service pour obtenir l'utilisateur en fonction du pseudo (de type String)
    return utilisateurService.findByPseudo(pseudo);
  }
}