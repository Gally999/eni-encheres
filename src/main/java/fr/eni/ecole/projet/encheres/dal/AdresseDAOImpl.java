package fr.eni.ecole.projet.encheres.dal;

import fr.eni.ecole.projet.encheres.bo.Adresse;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdresseDAOImpl implements AdresseDAO {

  // TODO add the one from user
  public static final String FIND_ALL_AVAILABLE = "SELECT no_adresse, rue, code_postal, ville FROM Adresses " +
      "WHERE adresse_eni = 1 OR no_adresse = :id;";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public AdresseDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Adresse> findAllAvailable(long idAdresseUser) {
    MapSqlParameterSource namedParam = new MapSqlParameterSource();
    namedParam.addValue("id", idAdresseUser);
    return jdbcTemplate.query(FIND_ALL_AVAILABLE, namedParam, new BeanPropertyRowMapper<>(Adresse.class));
  }
}
