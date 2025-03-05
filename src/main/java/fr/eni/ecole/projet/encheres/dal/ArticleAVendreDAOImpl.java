package fr.eni.ecole.projet.encheres.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
	
	private static final String FIND_ALL_ACTIVES = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, statut_enchere, prix_initial, prix_vente, id_utilisateur, no_categorie, no_adresse_retrait "
													+ "	FROM Articles_A_Vendre " 
													+ "	WHERE statut_enchere = 1;";
	private static final String INSERT_ARTICLE = "INSERT INTO Articles_A_Vendre(nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, id_utilisateur, no_categorie, no_adresse_retrait) "
			+ " VALUES(:nom, :description, :dateDebut, :dateFin, :prixInitial, :idUtilisateur, :idCategorie, :idAdresse);";
	
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ArticleAVendre> findAllActive() {
		return jdbcTemplate.query(FIND_ALL_ACTIVES, new ArticleAVendreRowMapper());
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
