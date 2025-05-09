package fr.eni.ecole.projet.encheres.bll;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.eni.ecole.projet.encheres.bo.*;
import fr.eni.ecole.projet.encheres.dal.*;
import fr.eni.ecole.projet.encheres.enums.*;
import fr.eni.ecole.projet.encheres.exceptions.BusinessCode;
import fr.eni.ecole.projet.encheres.exceptions.BusinessException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EncheresServiceImpl implements EncheresService {

	private final ArticleAVendreDAO articleDAO;
	private final CategorieDAO categorieDAO;
	private final AdresseDAO adresseDAO;
	private final UtilisateurDAO utilisateurDAO;
	private final EnchereDAO enchereDAO;

	public EncheresServiceImpl(ArticleAVendreDAO articleDAO, CategorieDAO categorieDAO, AdresseDAO adresseDAO, UtilisateurDAOImpl utilisateurDAO, EnchereDAO enchereDAO) {
		this.articleDAO = articleDAO;
		this.categorieDAO = categorieDAO;
		this.adresseDAO = adresseDAO;
		this.utilisateurDAO = utilisateurDAO;
    this.enchereDAO = enchereDAO;
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
			// On récupère toutes les catégories ou on les formatte en liste
			List<Long> categoriesToRetrieve = getCategories(categorieId);
			articles = articleDAO.findByCatAndSearchTerm(categoriesToRetrieve, searchTerm);
		}
		if (articles == null) {
			// Si aucun article ne correspond à la recherche, on renvoie une liste vide
			articles = new ArrayList<>();
		}
		return articles;
	}

	@Override
	public List<ArticleAVendre> consulterEncheresActives(Long categorieId, String searchTerm, AchatsOuVentesFilter achatsOuVentesFilter) {
		Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
		// On vérifie qu'on a bien un•e utilisateur•ice connecté•e
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();

			// On récupère toutes les catégories ou on les formate en liste
			List<Long> categoriesToRetrieve = getCategories(categorieId);
			// Dans le cas où les achats sont sélectionnés
			if (achatsOuVentesFilter instanceof AchatFilter) {
				// récupérer les articles sur lesquels l'utilisateur a enchéri ou gagné
				return switch ((AchatFilter) achatsOuVentesFilter) {
					case REMPORTEES -> articleDAO.findEncheresRemportees(currentUserName, categoriesToRetrieve, searchTerm);
					case EN_COURS -> articleDAO.findEncheresEnCours(currentUserName, categoriesToRetrieve, searchTerm);
					case OUVERTES -> consulterEncheresActives(categorieId, searchTerm);
				};
			} else {
				// récupérer les articles vendus par l'utilisateur : en cours, non débutées ou terminées
				return switch ((VenteFilter) achatsOuVentesFilter) {
					case NON_DEBUTEES -> articleDAO.findArticlesEnVente(currentUserName, categoriesToRetrieve, searchTerm,List.of(StatutEnchere.PAS_COMMENCEE));
					case EN_COURS -> articleDAO.findArticlesEnVente(currentUserName, categoriesToRetrieve, searchTerm, List.of(StatutEnchere.EN_COURS));
					case TERMINEES -> articleDAO.findArticlesEnVente(currentUserName, categoriesToRetrieve, searchTerm, List.of(StatutEnchere.CLOTUREE, StatutEnchere.LIVREE));
				};
			}
		}
		return List.of();
	}

	private List<Long> getCategories(Long categorieId) {
		List<Long> categoriesToRetrieve;
		if (categorieId == null) {
			// Si pas de catégorie sélectionnée alors on va chercher toutes les catégories disponibles
			categoriesToRetrieve = categorieDAO.findAll().stream().map(Categorie::getId).toList();
		} else {
			// Si une catégorie a été sélectionnée alors on la transforme en liste
			categoriesToRetrieve = List.of(categorieId);
		}
		return categoriesToRetrieve;
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

	@Override
	public ArticleAVendre consulterArticle(long id) {
		return articleDAO.read(id);
	}

	@Override
	public Enchere consulterMeilleureEnchere(long id) {
		Enchere meilleureEnchere;
    try {
      meilleureEnchere = enchereDAO.findMeilleureEncherePourArticle(id);
    } catch (EmptyResultDataAccessException e) {
     meilleureEnchere = new Enchere();
    }
		return meilleureEnchere;
  }

	@Override
	@Transactional
	public void encherirSurArticle(int montantEnchere, long idArticle, String pseudo) {
		// Validation des données de la couche présentation
		BusinessException be = new BusinessException();
		boolean isValid = true;
		isValid &= validerUserExists(pseudo, be);
		isValid &= validerEncherisseur(pseudo, idArticle, be);
		isValid &= validerArticleExists(idArticle, be);
		isValid &= validerCreditSuffisant(pseudo, montantEnchere, be);

		// Si tout est valide :
		if (isValid) {
			try {
				Enchere enchere = consulterMeilleureEnchere(idArticle);
				// On ajoute l'enchère dans la base de données, table Encheres
				enchereDAO.addEnchere(montantEnchere, idArticle, pseudo);
				// On retire le crédit de l'aquéreur
				utilisateurDAO.debiter(montantEnchere, pseudo);
				// On recrédite l'ancien acquéreur
				if (enchere != null && enchere.getMontant() > 0) {
					utilisateurDAO.recrediterPrecedentEncherisseur(enchere.getMontant(), enchere.getAcquereur().getPseudo());
				}
			} catch (DataAccessException e) {
				be.add(BusinessCode.ERROR_AJOUT_ENCHERE);
			}
		} else {
			be.add(BusinessCode.ERROR_AJOUT_ENCHERE);
		}
	}

	@Override
	public void supprimerArticle(long id) {
		BusinessException be = new BusinessException();
		if (id > 0) {
			ArticleAVendre article = articleDAO.read(id);
			if (article.getDateDebutEncheres().isAfter(LocalDate.now())) {
				try {
					articleDAO.delete(id);
				} catch (DataAccessException e) {
					be.add(BusinessCode.ERROR_DELETE_ARTICLE);
					throw be;
				}
				be.add(BusinessCode.ERROR_DELETE_ARTICLE);
				throw be;
			}
    } else {
			throw be;
		}
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

	private boolean validerUserExists(String pseudo, BusinessException be) {
		if (pseudo == null || pseudo.isEmpty()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PSEUDO_BLANK);
			return false;
		}
		try {
			if (utilisateurDAO.readByPseudo(pseudo) == null) {
				be.add(BusinessCode.VALIDATION_UTILISATEUR_DB_NULL);
				return false;
			}
		} catch (DataAccessException e) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_DB_NULL);
			return false;
		}
		return true;
	}

	private boolean validerArticleExists(long idArticle, BusinessException be) {
		if (idArticle <= 0) {
			be.add(BusinessCode.VALIDATION_ARTICLE_NULL);
			return false;
		}
		try {
			if (articleDAO.read(idArticle) == null) {
				be.add(BusinessCode.VALIDATION_ARTICLE_NULL);
				return false;
			}
		} catch (DataAccessException e) {
			be.add(BusinessCode.VALIDATION_ARTICLE_NULL);
			return false;
		}
		return true;
	}

	private boolean validerCreditSuffisant(String pseudo, int montantEnchere, BusinessException be) {
		if (pseudo == null || pseudo.isEmpty()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PSEUDO_BLANK);
			return false;
		}
		try {
			Utilisateur user = utilisateurDAO.readByPseudo(pseudo);
			if (user == null) {
				be.add(BusinessCode.VALIDATION_UTILISATEUR_DB_NULL);
				return false;
			} else {
				return user.getCredit() >= montantEnchere;
			}
		} catch (DataAccessException e) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_DB_NULL);
			return false;
		}
	}

	private boolean validerEncherisseur(String pseudo, long idArticle, BusinessException be) {
		if (pseudo == null || pseudo.isEmpty()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PSEUDO_BLANK);
			return false;
		}
		if (idArticle <= 0) {
			be.add(BusinessCode.VALIDATION_ARTICLE_NULL);
		}
		try {
			Enchere enchere = enchereDAO.findMeilleureEncherePourArticle(idArticle);
			ArticleAVendre articleAVendre = articleDAO.read(idArticle);
			if (enchere == null || articleAVendre == null) {
				return false;
			} else {
				// On vérifie que l'encherisseur n'est pas l'enchérisseur précédent ET que l'enchérisseur n'est pas le vendeur
				return !enchere.getAcquereur().getPseudo().equals(pseudo)
						&& !articleAVendre.getVendeur().getPseudo().equals(pseudo);
			}
		} catch (EmptyResultDataAccessException e) {
			return true;
		} catch (DataAccessException e) {
			return false;
		}
	}

}
