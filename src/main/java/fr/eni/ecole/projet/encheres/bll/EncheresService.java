package fr.eni.ecole.projet.encheres.bll;

import java.util.List;

import fr.eni.ecole.projet.encheres.bo.Adresse;
import fr.eni.ecole.projet.encheres.bo.ArticleAVendre;
import fr.eni.ecole.projet.encheres.bo.Categorie;
import fr.eni.ecole.projet.encheres.bo.Enchere;
import fr.eni.ecole.projet.encheres.enums.AchatsOuVentesFilter;

public interface EncheresService {

	void ajouterArticleAVendre(ArticleAVendre article);
	
	List<ArticleAVendre> consulterEncheres();
	
	List<ArticleAVendre> consulterEncheresActives(Long categorieId, String searchTerm);

	List<ArticleAVendre> consulterEncheresActives(Long categorieId, String searchTerm, AchatsOuVentesFilter achatsOuVentesFilter);

	List<Categorie> consulterCategories();

	List<Adresse> consulterAdressesDisponibles(long id);

	Adresse consulterAdresseParId(long id);

	Categorie consulterCategorieParId(long id);

	ArticleAVendre consulterArticle(long id);

	Enchere consulterMeilleureEnchere(long id);

	void encherirSurArticle(int montantEnchere, long idArticle, String name);
}
