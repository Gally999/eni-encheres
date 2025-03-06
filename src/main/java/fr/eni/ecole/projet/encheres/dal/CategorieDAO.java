package fr.eni.ecole.projet.encheres.dal;

import fr.eni.ecole.projet.encheres.bo.Categorie;

import java.util.List;

public interface CategorieDAO {
  List<Categorie> findAll();

  Categorie read(long id);
}
