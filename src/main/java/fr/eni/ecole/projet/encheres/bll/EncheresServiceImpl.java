package fr.eni.ecole.projet.encheres.bll;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.eni.ecole.projet.encheres.bo.Adresse;
import fr.eni.ecole.projet.encheres.bo.Categorie;
import fr.eni.ecole.projet.encheres.bo.Utilisateur;
import fr.eni.ecole.projet.encheres.dal.*;
import fr.eni.ecole.projet.encheres.exceptions.BusinessCode;
import fr.eni.ecole.projet.encheres.exceptions.BusinessException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import fr.eni.ecole.projet.encheres.bo.ArticleAVendre;

@Service
public class EncheresServiceImpl implements EncheresService {

	private final ArticleAVendreDAO articleDAO;
	private final CategorieDAO categorieDAO;
	private final AdresseDAO adresseDAO;
	private final UtilisateurDAO utilisateurDAO;

	public EncheresServiceImpl(ArticleAVendreDAO articleDAO, CategorieDAO categorieDAO, AdresseDAO adresseDAO, UtilisateurDAOImpl utilisateurDAO) {
		this.articleDAO = articleDAO;
		this.categorieDAO = categorieDAO;
		this.adresseDAO = adresseDAO;
		this.utilisateurDAO = utilisateurDAO;
	}

	@Override
	public void ajouterArticleAVendre(ArticleAVendre article) {
		// Validation des données de la couche présentation
		BusinessException be = new BusinessException();
		boolean isValid = true;
		isValid &= validerUserExists(article.getVendeur(), be);
		isValid &= validerAdresse(article.getAdresseRetrait(), be);
		isValid &= validerCategorie(article.getCategorie(), be);
		isValid &= validerDateFin(article.getDateDebutEncheres(), article.getDateFinEncheres(), be);

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
	public List<ArticleAVendre> consulterEncheresActives(Long categorieId, String searchTerm) {
		List<ArticleAVendre> articles;
		if (categorieId == null && (searchTerm == null || searchTerm.isEmpty())) {
			// Si pas de catégorie ni de mot recherché, on tombe dans le cas par défaut des enchères actives
			articles = articleDAO.findAllActive();
		} else {
			if (categorieId == null) {
				// Si pas de catégorie sélectionnée alors on va chercher toutes les catégories disponibles
				List<Long> allIds = categorieDAO.findAll().stream().map(Categorie::getId).toList();
				articles = articleDAO.findByCatAndSearchTerm(allIds, searchTerm);
			} else {
				// Si une catégorie a été sélectionnée alors on la transforme en liste
				articles = articleDAO.findByCatAndSearchTerm(List.of(categorieId), searchTerm);
			}
		}
		if (articles == null) {
			// Si aucun article ne correspond à la recherche, on renvoie une liste vide
			articles = new ArrayList<>();
		}
		return articles;
	}

	@Override
	public List<Categorie> consulterCategories() {
		return categorieDAO.findAll();
	}

	@Override
	public List<Adresse> consulterAdressesDisponibles(long id) {
		return adresseDAO.findAllAvailable(id);
	}

	@Override
	public Adresse consulterAdresseParId(long id) {
		return adresseDAO.read(id);
	}

	@Override
	public Categorie consulterCategorieParId(long id) {
		return categorieDAO.read(id);
	}

	private boolean validerDateFin(LocalDate dateDebutEncheres, LocalDate dateFinEncheres, BusinessException be) {
		if (!dateFinEncheres.isAfter(dateDebutEncheres)) {
			be.add(BusinessCode.VALIDATION_DATE_FIN_ENCHERES_FUTURE);
			return false;
		}
		return true;
	}

	private boolean validerAdresse(Adresse adresse, BusinessException be) {
		if (adresse == null) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_ADRESSE_NULL);
			return false;
		}
		if (adresse.getId() <= 0) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_ADRESSE_ID_INCONNU);
			return false;
		}
		try {
			if (adresseDAO.read(adresse.getId()) == null) {
				be.add(BusinessCode.VALIDATION_UTILISATEUR_ADRESSE_ID_INCONNU);
				return false;
			}
		} catch (DataAccessException e) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_ADRESSE_ID_INCONNU);
			return false;
		}
		return true;
	}

	private boolean validerCategorie(Categorie categorie, BusinessException be) {
		if (categorie == null) {
			be.add(BusinessCode.VALIDATION_ARTICLE_CATEGORIE_NULL);
			return false;
		}
		if (categorie.getId() <= 0) {
			be.add(BusinessCode.VALIDATION_ARTICLE_CATEGORIE_ID_INCONNU);
			return false;
		}
    try {
      if (adresseDAO.read(categorie.getId()) == null) {
        be.add(BusinessCode.VALIDATION_ARTICLE_CATEGORIE_ID_INCONNU);
        return false;
      }
    } catch (DataAccessException e) {
      be.add(BusinessCode.VALIDATION_ARTICLE_CATEGORIE_ID_INCONNU);
			return false;
    }
    return true;
	}

	private boolean validerUserExists(Utilisateur user, BusinessException be) {
		if (user == null) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_NULL);
			return false;
		}
		if (user.getPseudo() == null || user.getPseudo().isEmpty()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PSEUDO_BLANK);
			return false;
		}
    try {
      if (utilisateurDAO.readByPseudo(user.getPseudo()) == null) {
        be.add(BusinessCode.VALIDATION_UTILISATEUR_DB_NULL);
        return false;
      }
    } catch (DataAccessException e) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_DB_NULL);
			return false;
    }
    return true;
	}
}
