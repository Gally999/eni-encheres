package fr.eni.ecole.projet.encheres.dal;

import java.util.List;

import org.springframework.dao.DataAccessException;

import fr.eni.ecole.projet.encheres.bo.Adresse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class AdresseDAOImpl implements AdresseDAO {

  public static final String FIND_ALL_AVAILABLE = "SELECT no_adresse, rue, code_postal, ville FROM Adresses " +
      "WHERE adresse_eni = 1 OR no_adresse = :id ORDER BY adresse_eni ASC;";
  public static final String FIND_BY_ID = "SELECT no_adresse, rue, code_postal, ville FROM Adresses WHERE no_adresse = :adresseId;";

  public static final String CREATE_ADRESSE = "INSERT INTO ADRESSES (rue, code_postal, ville, adresse_eni) VALUES (:rue, :codePostal, :ville, 0)";

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

  

  @Override
  public void create(Adresse adresse) {
	  
	  KeyHolder keyHolder = new GeneratedKeyHolder();
	   
	  MapSqlParameterSource namedParameter = new MapSqlParameterSource();
	  namedParameter.addValue("rue", adresse.getRue());
	  namedParameter.addValue("codePostal", adresse.getCodePostal());
	  namedParameter.addValue("ville", adresse.getVille());
	  jdbcTemplate.update(CREATE_ADRESSE, namedParameter, keyHolder);
	  
	  if (keyHolder != null && keyHolder.getKey() != null) {  
		  adresse.setId(keyHolder.getKey().longValue());
		}
  }
  
  //Méthode qui vérifie si l'adresse existe et donne IdAdresse sinon 0 
   @Override
   public long readAdresseConnue(Adresse adresse) {
	    String sqlSelect = "SELECT no_adresse FROM ADRESSES WHERE rue = :rue AND code_postal = :codePostal AND ville = :ville";
    
      MapSqlParameterSource namedParameters = new MapSqlParameterSource();
      namedParameters.addValue("rue", adresse.getRue());
      namedParameters.addValue("codePostal", adresse.getCodePostal());
      namedParameters.addValue("ville", adresse.getVille());


      try {
        return jdbcTemplate.queryForObject(sqlSelect, namedParameters, Long.class);
      } catch (DataAccessException e) {
        return 0;
      }
   }


   @Override
   public List<Adresse> findAllAvailable() {
	   // TODO Auto-generated method stub
	   return null;
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
