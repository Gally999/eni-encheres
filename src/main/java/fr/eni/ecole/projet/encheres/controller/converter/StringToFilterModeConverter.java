package fr.eni.ecole.projet.encheres.controller.converter;

import fr.eni.ecole.projet.encheres.enums.FilterMode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToFilterModeConverter implements Converter<String, FilterMode> {
  @Override
  public FilterMode convert(String value) {
    int id = Integer.parseInt(value);
    return FilterMode.getFilterMode(id);
  }
}
