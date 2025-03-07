package fr.eni.ecole.projet.encheres.controller.converter;

import fr.eni.ecole.projet.encheres.bll.EncheresService;
import fr.eni.ecole.projet.encheres.bo.Adresse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAdresseConverter implements Converter<String, Adresse> {

  // Injection des Services
  private final EncheresService encheresService;

  public StringToAdresseConverter(EncheresService encheresService) {
    this.encheresService = encheresService;
  }

  @Override
  public Adresse convert(String id) {
    long parsedId = Long.parseLong(id);
    return encheresService.consulterAdresseParId(parsedId);
  }
}
