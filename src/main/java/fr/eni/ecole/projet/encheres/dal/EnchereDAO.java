package fr.eni.ecole.projet.encheres.dal;

import fr.eni.ecole.projet.encheres.bo.Enchere;

import java.util.List;

public interface EnchereDAO {
  List<Enchere> findByArticleId(long id);
  Enchere findMeilleureEncherePourArticle(long id);
}
