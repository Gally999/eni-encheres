package fr.eni.ecole.projet.encheres.dal;

import fr.eni.ecole.projet.encheres.bo.Adresse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AdresseDAOImpl implements AdresseDAO {

  public static final String FIND_ALL_AVAILABLE = "SELECT no_adresse, rue, code_postal, ville FROM Adresses " +
      "WHERE adresse_eni = 1 OR no_adresse = :id;";
  public static final String FIND_BY_ID = "SELECT no_adresse, rue, code_postal, ville FROM Adresses WHERE no_adresse = :adresseId;";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public AdresseDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Adresse> findAllAvailable(long idAdresseUser) {
    MapSqlParameterSource namedParam = new MapSqlParameterSource();
    namedParam.addValue("id", idAdresseUser);
    return jdbcTemplate.query(FIND_ALL_AVAILABLE, namedParam, new AdresseRowMapper());
  }

  @Override
  public Adresse read(long id) {
    MapSqlParameterSource namedParam = new MapSqlParameterSource();
    namedParam.addValue("adresseId", id);
    return jdbcTemplate.queryForObject(FIND_BY_ID, namedParam, new AdresseRowMapper());
  }

  static class AdresseRowMapper implements RowMapper<Adresse> {
    @Override
    public Adresse mapRow(ResultSet rs, int rowNum) throws SQLException {
      Adresse a = new Adresse();
      a.setId(rs.getLong("no_adresse"));
      a.setRue(rs.getString("rue"));
      a.setCodePostal(rs.getString("code_postal"));
      a.setVille(rs.getString("ville"));
      return a;
    }
  }
}
