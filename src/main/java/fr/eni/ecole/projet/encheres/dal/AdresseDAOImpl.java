package fr.eni.ecole.projet.encheres.dal;

import fr.eni.ecole.projet.encheres.bo.Adresse;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdresseDAOImpl implements AdresseDAO {

  // TODO add the one from user
  public static final String FIND_ALL_AVAILABLE = "SELECT no_adresse, rue, code_postal, ville FROM Adresses WHERE adresse_eni = 1;";

  private final JdbcTemplate jdbcTemplate;

  public AdresseDAOImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Adresse> findAllAvailable() {
    return jdbcTemplate.query(FIND_ALL_AVAILABLE, new BeanPropertyRowMapper<>(Adresse.class));
  }
}
