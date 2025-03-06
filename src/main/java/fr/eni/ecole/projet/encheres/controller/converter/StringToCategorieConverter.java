package fr.eni.ecole.projet.encheres.controller.converter;

import fr.eni.ecole.projet.encheres.bll.EncheresService;
import fr.eni.ecole.projet.encheres.bo.Categorie;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCategorieConverter implements Converter<String, Categorie> {

  // Injection des Services
  private final EncheresService encheresService;

  public StringToCategorieConverter(EncheresService encheresService) {
    this.encheresService = encheresService;
  }

  @Override
  public Categorie convert(String id) {
    long parsedId = Long.parseLong(id);
    return encheresService.consulterCategorieParId(parsedId);
  }
}
