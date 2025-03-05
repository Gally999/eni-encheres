package fr.eni.ecole.projet.encheres.dal;

import fr.eni.ecole.projet.encheres.bo.Categorie;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategorieDAOImpl implements CategorieDAO {

  public static final String FIND_ALL = "SELECT no_categorie, libelle FROM Categories";

  private final JdbcTemplate jdbcTemplate;

  public CategorieDAOImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Categorie> findAll() {
    return jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Categorie.class));
  }
}
