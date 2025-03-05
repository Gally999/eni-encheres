package fr.eni.ecole.projet.encheres.bll;

import java.util.List;

import fr.eni.ecole.projet.encheres.bo.ArticleAVendre;

public interface EncheresService {

	void ajouterArticleAVendre(ArticleAVendre article);
	
	List<ArticleAVendre> consulterEncheres();
	
	List<ArticleAVendre> consulterEncheresActives();

}
