package fr.eni.ecole.projet.encheres.dal;

import fr.eni.ecole.projet.encheres.bo.Categorie;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CategorieDAOImpl implements CategorieDAO {

  public static final String FIND_ALL = "SELECT no_categorie, libelle FROM Categories;";
  public static final String FIND_BY_ID = "SELECT no_categorie, libelle FROM Categories WHERE no_categorie = :categorieId;";
  private final NamedParameterJdbcTemplate jdbcTemplate;

  public CategorieDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Categorie> findAll() {
    return jdbcTemplate.query(FIND_ALL, new CategorieRowMapper());
  }

  @Override
  public Categorie read(long id) {
    MapSqlParameterSource namedParam = new MapSqlParameterSource();
    namedParam.addValue("categorieId", id);
    return jdbcTemplate.queryForObject(FIND_BY_ID, namedParam, new CategorieRowMapper());
  }

  static class CategorieRowMapper implements RowMapper<Categorie> {
    @Override
    public Categorie mapRow(ResultSet rs, int rowNum) throws SQLException {
      Categorie c = new Categorie();
      c.setId(rs.getLong("no_categorie"));
      c.setLibelle(rs.getString("libelle"));
      return c;
    }
  }
}
