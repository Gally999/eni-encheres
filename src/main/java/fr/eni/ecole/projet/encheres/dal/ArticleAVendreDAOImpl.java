package fr.eni.ecole.projet.encheres.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import fr.eni.ecole.projet.encheres.enums.StatutEnchere;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.eni.ecole.projet.encheres.bo.Adresse;
import fr.eni.ecole.projet.encheres.bo.ArticleAVendre;
import fr.eni.ecole.projet.encheres.bo.Categorie;
import fr.eni.ecole.projet.encheres.bo.Utilisateur;

@Repository
public class ArticleAVendreDAOImpl implements ArticleAVendreDAO {
	private static final String FIND_BY_ID = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, statut_enchere, prix_initial, prix_vente, id_utilisateur, no_categorie, no_adresse_retrait FROM Articles_A_Vendre WHERE no_article = :id;";
	private static final String FIND_ALL_ACTIVES = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, statut_enchere, prix_initial, prix_vente, id_utilisateur, no_categorie, no_adresse_retrait "
													+ "	FROM Articles_A_Vendre " 
													+ "	WHERE statut_enchere = 1;";
	private static final String INSERT_ARTICLE = "INSERT INTO Articles_A_Vendre(nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, id_utilisateur, no_categorie, no_adresse_retrait) "
													+ " VALUES(:nom, :description, :dateDebut, :dateFin, :prixInitial, :idUtilisateur, :idCategorie, :idAdresse);";
	private static final String FIND_BY_CAT_AND_SEARCH_TERM = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, statut_enchere, prix_initial, prix_vente, id_utilisateur, no_categorie, no_adresse_retrait "
													+ "	FROM Articles_A_Vendre "
													+ "	WHERE statut_enchere = 1 AND no_categorie IN (:categorieIds) AND nom_article LIKE :search;";
	private static final String FIND_ENCHERES_REMPORTEES = "SELECT a.no_article, a.nom_article, a.description, a.date_debut_encheres, a.date_fin_encheres, a.statut_enchere, a.prix_initial, MAX(e.montant_enchere) as prix_vente, a.id_utilisateur, a.no_categorie, a.no_adresse_retrait "
													+ " FROM Articles_A_Vendre a "
													+	" INNER JOIN Encheres e "
													+	" ON a.no_article = e.no_article "
													+	" WHERE statut_enchere IN (2, 3) AND e.id_utilisateur = :utilisateurId AND no_categorie IN (:categorieIds) AND nom_article LIKE :search "
													+	" GROUP BY a.no_article, a.nom_article, a.description, a.date_debut_encheres, a.date_fin_encheres, a.statut_enchere, a.prix_initial, prix_vente, a.id_utilisateur, a.no_categorie, a.no_adresse_retrait, e.id_utilisateur;";

	private static final String FIND_ENCHERES_EN_COURS = "SELECT a.no_article, a.nom_article, a.description, a.date_debut_encheres, a.date_fin_encheres, a.statut_enchere, a.prix_initial, a.prix_vente, a.id_utilisateur, a.no_categorie, a.no_adresse_retrait "
													+ " FROM Articles_A_Vendre a "
													+	" INNER JOIN Encheres e "
													+	" ON a.no_article = e.no_article "
													+	" WHERE statut_enchere IN (1) AND e.id_utilisateur = :utilisateurId AND no_categorie IN (:categorieIds) AND nom_article LIKE :search "
													+	" GROUP BY a.no_article, a.nom_article, a.description, a.date_debut_encheres, a.date_fin_encheres, a.statut_enchere, a.prix_initial, prix_vente, a.id_utilisateur, a.no_categorie, a.no_adresse_retrait, e.id_utilisateur;";

	private static final String FIND_VENTES_EN_COURS = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, statut_enchere, prix_initial, prix_vente, id_utilisateur, no_categorie, no_adresse_retrait "
													+ "FROM ARTICLES_A_VENDRE WHERE id_utilisateur = :utilisateurId AND statut_enchere IN (:status) AND no_categorie IN (:categorieIds) AND nom_article LIKE :search;";

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public ArticleAVendreDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void create(ArticleAVendre article) {
		System.out.println("ArticleDAO.create() " + article);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		MapSqlParameterSource namedParams = new MapSqlParameterSource();
		namedParams.addValue("nom", article.getNom());
		namedParams.addValue("description", article.getDescription());
		namedParams.addValue("dateDebut", article.getDateDebutEncheres());
		namedParams.addValue("dateFin", article.getDateFinEncheres());
		namedParams.addValue("prixInitial", article.getPrixInitial());
		namedParams.addValue("idUtilisateur", article.getVendeur().getPseudo());
		namedParams.addValue("idCategorie", article.getCategorie().getId());
		namedParams.addValue("idAdresse", article.getAdresseRetrait().getId());
		
		jdbcTemplate.update(INSERT_ARTICLE, namedParams, keyHolder);
		
		if (keyHolder != null && keyHolder.getKey() != null) {
			// Mise à jour de l'identifiant de l'article généré par la base
			article.setId(keyHolder.getKey().longValue());
		}
	}

	@Override
	public ArticleAVendre read(long id) {
		MapSqlParameterSource namedParam = new MapSqlParameterSource();
		namedParam.addValue("id", id);
		return jdbcTemplate.queryForObject(FIND_BY_ID, namedParam, new ArticleAVendreRowMapper());
	}

	@Override
	public List<ArticleAVendre> findAllActive() {
		return jdbcTemplate.query(FIND_ALL_ACTIVES, new ArticleAVendreRowMapper());
	}

	@Override
	public List<ArticleAVendre> findByCatAndSearchTerm(List<Long> categorieIds, String search) {
		System.out.println("ArticleDAO.findByCatAndSearchTerm() " + categorieIds + " " + search);
		MapSqlParameterSource namedParams = new MapSqlParameterSource();
		namedParams.addValue("categorieIds", categorieIds);
		namedParams.addValue("search", "%" + search + "%");
		return jdbcTemplate.query(FIND_BY_CAT_AND_SEARCH_TERM, namedParams, new ArticleAVendreRowMapper());
	}

	@Override
	public List<ArticleAVendre> findEncheresRemportees(String utilisateurId, List<Long> categorieIds, String search) {
		MapSqlParameterSource namedParams = new MapSqlParameterSource();
		namedParams.addValue("utilisateurId", utilisateurId);
		namedParams.addValue("categorieIds", categorieIds);
		namedParams.addValue("search", "%" + search + "%");
		return jdbcTemplate.query(FIND_ENCHERES_REMPORTEES, namedParams, new ArticleAVendreRowMapper());
	}

	@Override
	public List<ArticleAVendre> findEncheresEnCours(String utilisateurId, List<Long> categorieIds, String search) {
		MapSqlParameterSource namedParams = new MapSqlParameterSource();
		namedParams.addValue("utilisateurId", utilisateurId);
		namedParams.addValue("categorieIds", categorieIds);
		namedParams.addValue("search", "%" + search + "%");
		return jdbcTemplate.query(FIND_ENCHERES_EN_COURS, namedParams, new ArticleAVendreRowMapper());
	}

	@Override
	public List<ArticleAVendre> findArticlesEnVente(String utilisateurId, List<Long> categorieIds, String search, List<StatutEnchere> status) {
		MapSqlParameterSource namedParams = new MapSqlParameterSource();
		namedParams.addValue("utilisateurId", utilisateurId);
		namedParams.addValue("categorieIds", categorieIds);
		namedParams.addValue("search", "%" + search + "%");
		List<Integer> statusValues = status.stream().map(StatutEnchere::getValue).toList();
		namedParams.addValue("status", statusValues);
		return jdbcTemplate.query(FIND_VENTES_EN_COURS, namedParams, new ArticleAVendreRowMapper());
	}

	static class ArticleAVendreRowMapper implements RowMapper<ArticleAVendre> {
		@Override
		public ArticleAVendre mapRow(ResultSet rs, int rowNum) throws SQLException {
			ArticleAVendre a = new ArticleAVendre();
			a.setId(rs.getLong("no_article"));
			a.setNom(rs.getString("nom_article"));
			a.setDescription(rs.getString("description"));
			a.setDateDebutEncheres(rs.getDate("date_debut_encheres").toLocalDate());
			a.setDateFinEncheres(rs.getDate("date_fin_encheres").toLocalDate());
			
			a.setStatut(rs.getInt("statut_enchere"));
			a.setPrixInitial(rs.getInt("prix_initial"));
			a.setPrixVente(rs.getInt("prix_vente"));

			// Association pour le vendeur
			Utilisateur vendeur = new Utilisateur();
			vendeur.setPseudo(rs.getString("id_utilisateur"));
			a.setVendeur(vendeur);

			// Association pour l'adresse
			Adresse adresse = new Adresse();
			adresse.setId(rs.getLong("no_adresse_retrait"));
			a.setAdresseRetrait(adresse);
			
			// Association pour la Catégorie
			Categorie categorie = new Categorie();
			categorie.setId(rs.getLong("no_categorie"));
			a.setCategorie(categorie);

			return a;
		}
	}

}
