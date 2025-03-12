package fr.eni.ecole.projet.encheres.dal;

import java.util.List;

import fr.eni.ecole.projet.encheres.bo.ArticleAVendre;
import fr.eni.ecole.projet.encheres.enums.StatutEnchere;

public interface ArticleAVendreDAO {
	
	void create(ArticleAVendre article);
	
	ArticleAVendre read(long id);
	
	List<ArticleAVendre> findAllActive();

  List<ArticleAVendre> findByCatAndSearchTerm(List<Long> categorieIds, String search);

	List<ArticleAVendre> findEncheresRemportees(String utilisateurId, List<Long> categorieIds, String search);

	List<ArticleAVendre> findEncheresEnCours(String currentUserName, List<Long> categoriesToRetrieve, String searchTerm);

	List<ArticleAVendre> findArticlesEnVente(String currentUserName, List<Long> categoriesToRetrieve, String searchTerm, List<StatutEnchere> status);
}
