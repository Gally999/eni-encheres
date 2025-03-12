package fr.eni.ecole.projet.encheres.controller.converter;

import fr.eni.ecole.projet.encheres.enums.AchatFilter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAchatFilterConverter implements Converter<String, AchatFilter> {
  @Override
  public AchatFilter convert(String value) {
    int id = Integer.parseInt(value);
    System.out.println("id from AchatFilter = " + id);
    return AchatFilter.getAchatFilter(id);
  }
}

