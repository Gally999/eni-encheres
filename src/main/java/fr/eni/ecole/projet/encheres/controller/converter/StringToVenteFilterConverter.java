package fr.eni.ecole.projet.encheres.controller.converter;

import fr.eni.ecole.projet.encheres.enums.VenteFilter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToVenteFilterConverter implements Converter<String, VenteFilter>  {
  @Override
  public VenteFilter convert(String value) {
    int id = Integer.parseInt(value);
    System.out.println("id from VenteFilter: " + id);
    return VenteFilter.getVenteFilter(id);
  }
}
