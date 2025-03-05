package fr.eni.ecole.projet.encheres.bll;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.eni.ecole.projet.encheres.exceptions.BusinessCode;
import fr.eni.ecole.projet.encheres.exceptions.BusinessException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import fr.eni.ecole.projet.encheres.bo.ArticleAVendre;
import fr.eni.ecole.projet.encheres.dal.ArticleAVendreDAO;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EncheresServiceImpl implements EncheresService {
	
	private final ArticleAVendreDAO articleDAO;

	public EncheresServiceImpl(ArticleAVendreDAO articleDAO) {
		this.articleDAO = articleDAO;
	}

	@Override
	// @Transactional
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

	private boolean validerDateFin(LocalDate dateDebutEncheres, LocalDate dateFinEncheres, BusinessException be) {
			if (!dateFinEncheres.isAfter(dateDebutEncheres)) {
				be.add(BusinessCode.VALIDATION_DATE_FIN_ENCHERES_FUTURE);
				return false;
			}
			return true;
	}
}
