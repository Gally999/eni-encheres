package fr.eni.ecole.projet.encheres.bll;

import java.util.List;

import fr.eni.ecole.projet.encheres.bo.Adresse;
import fr.eni.ecole.projet.encheres.bo.ArticleAVendre;
import fr.eni.ecole.projet.encheres.bo.Categorie;
import fr.eni.ecole.projet.encheres.enums.AchatFilter;
import fr.eni.ecole.projet.encheres.enums.AchatsOuVentesFilter;
import fr.eni.ecole.projet.encheres.enums.FilterMode;
import fr.eni.ecole.projet.encheres.enums.VenteFilter;

public interface EncheresService {

	void ajouterArticleAVendre(ArticleAVendre article);
	
	List<ArticleAVendre> consulterEncheres();
	
	List<ArticleAVendre> consulterEncheresActives(Long categorieId, String searchTerm);

	List<ArticleAVendre> consulterEncheresActives(Long categorieId, String searchTerm, FilterMode filterMode, AchatFilter achatFilter, VenteFilter venteFilter);

	List<Categorie> consulterCategories();

	List<Adresse> consulterAdressesDisponibles(long id);

	Adresse consulterAdresseParId(long id);

	Categorie consulterCategorieParId(long id);

	List<ArticleAVendre> consulterEncheresActives(Long categorieId, String searchTerm,
			AchatsOuVentesFilter achatsOuVentesFilter);
}
