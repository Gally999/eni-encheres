package fr.eni.ecole.projet.encheres.dal;

import java.util.List;

import fr.eni.ecole.projet.encheres.bo.ArticleAVendre;

public interface ArticleAVendreDAO {
	
	void create(ArticleAVendre article);
	
	ArticleAVendre read(long id);
	
	List<ArticleAVendre> findAllActive();

  List<ArticleAVendre> findByCatAndSearchTerm(List<Long> categorieIds, String search);
}
