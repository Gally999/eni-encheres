package fr.eni.ecole.projet.encheres.dal;

import fr.eni.ecole.projet.encheres.bo.ArticleAVendre;
import fr.eni.ecole.projet.encheres.bo.Enchere;
import fr.eni.ecole.projet.encheres.bo.Utilisateur;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EnchereDAOImpl implements EnchereDAO {

  public static final String FIND_BEST_FOR_ID = "SELECT TOP 1 id_utilisateur, no_article, date_enchere, montant_enchere FROM Encheres WHERE no_article = :id ORDER BY montant_enchere DESC;";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public EnchereDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Enchere> findByArticleId(long id) {
    return List.of();
  }

  @Override
  public Enchere findMeilleureEncherePourArticle(long id) {
    SqlParameterSource namedParam = new MapSqlParameterSource("id", id);
    return jdbcTemplate.queryForObject(FIND_BEST_FOR_ID, namedParam, new EnchereRowMapper());
  }

  static class EnchereRowMapper implements RowMapper<Enchere> {
    @Override
    public Enchere mapRow(ResultSet rs, int rowNum) throws SQLException {
      Enchere e = new Enchere();
      e.setMontant(rs.getInt("montant_enchere"));
      e.setDate(rs.getTimestamp("date_enchere").toLocalDateTime());

      // Association avec l'article
      ArticleAVendre a = new ArticleAVendre();
      a.setId(rs.getLong("no_article"));
      e.setArticle(a);

      // Association avec l'acquereur
      Utilisateur u = new Utilisateur();
      u.setPseudo(rs.getString("id_utilisateur"));
      e.setAcquereur(u);

      return e;
    }
  }
}
