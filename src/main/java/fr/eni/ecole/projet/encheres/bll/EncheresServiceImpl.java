package fr.eni.ecole.projet.encheres.bll;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.eni.ecole.projet.encheres.bo.Adresse;
import fr.eni.ecole.projet.encheres.bo.Categorie;
import fr.eni.ecole.projet.encheres.dal.AdresseDAO;
import fr.eni.ecole.projet.encheres.dal.CategorieDAO;
import fr.eni.ecole.projet.encheres.exceptions.BusinessCode;
import fr.eni.ecole.projet.encheres.exceptions.BusinessException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import fr.eni.ecole.projet.encheres.bo.ArticleAVendre;
import fr.eni.ecole.projet.encheres.dal.ArticleAVendreDAO;


@Service
public class EncheresServiceImpl implements EncheresService {
	
	private final ArticleAVendreDAO articleDAO;
	private final CategorieDAO categorieDAO;
	private final AdresseDAO adresseDAO;

	public EncheresServiceImpl(ArticleAVendreDAO articleDAO, CategorieDAO categorieDAO, AdresseDAO adresseDAO) {
		this.articleDAO = articleDAO;
		this.categorieDAO = categorieDAO;
    this.adresseDAO = adresseDAO;
  }

	@Override
	public void ajouterArticleAVendre(ArticleAVendre article) {
		// Validation des données de la couche présentation
		BusinessException be = new BusinessException();
		boolean isValid = true;
		isValid = validerDateFin(article.getDateDebutEncheres(), article.getDateFinEncheres(), be);

		if (isValid) {
			System.out.println("création article = " + article);
      try {
        articleDAO.create(article);
      } catch (DataAccessException e) {
        be.add(BusinessCode.ERROR_CREATION_ARTICLE);
				throw be;
      }
    } else {
			throw be;
		}
	}

	@Override
	public List<ArticleAVendre> consulterEncheres() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ArticleAVendre> consulterEncheresActives() {
		List<ArticleAVendre> allActive = articleDAO.findAllActive();
		if (allActive == null) {
			allActive = new ArrayList<>();
		}
		return allActive;
	}

	@Override
	public List<Categorie> consulterCategories() {
		return categorieDAO.findAll();
	}

	@Override
	public List<Adresse> consulterAdressesDisponibles(long id) {
		return adresseDAO.findAllAvailable(id);
	}

	private boolean validerDateFin(LocalDate dateDebutEncheres, LocalDate dateFinEncheres, BusinessException be) {
			if (!dateFinEncheres.isAfter(dateDebutEncheres)) {
				be.add(BusinessCode.VALIDATION_DATE_FIN_ENCHERES_FUTURE);
				return false;
			}
			return true;
	}

	@Override
	public List<Adresse> consulterAdressesDisponibles() {
		// TODO Auto-generated method stub
		return null;
	}
}
