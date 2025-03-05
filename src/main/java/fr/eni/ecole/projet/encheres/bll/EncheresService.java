package fr.eni.ecole.projet.encheres.bll;

import java.util.List;

import fr.eni.ecole.projet.encheres.bo.Adresse;
import fr.eni.ecole.projet.encheres.bo.ArticleAVendre;
import fr.eni.ecole.projet.encheres.bo.Categorie;

public interface EncheresService {

	void ajouterArticleAVendre(ArticleAVendre article);
	
	List<ArticleAVendre> consulterEncheres();
	
	List<ArticleAVendre> consulterEncheresActives();

  List<Categorie> consulterCategories();

	List<Adresse> consulterAdressesDisponibles(long id);
}
