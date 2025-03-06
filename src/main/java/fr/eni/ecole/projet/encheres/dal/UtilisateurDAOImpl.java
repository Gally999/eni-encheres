
package fr.eni.ecole.projet.encheres.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import fr.eni.ecole.projet.encheres.bo.Adresse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.ecole.projet.encheres.bo.Utilisateur;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO {
		
		private final String INSERT = "INSERT INTO UTILISATEURS(pseudo, nom, prenom, email, telephone, mot_de_passe, credit, administrateur, no_adresse) VALUES (:pseudo, :nom, :prenom, :email, :telephone, :motDePasse, :credit, :administrateur,:no_adresse)";
		private final String FIND_BY_PSEUDO = "SELECT pseudo, nom, prenom, email, telephone, credit, administrateur, no_adresse FROM UTILISATEURS WHERE pseudo = :pseudo";
		private final String FIND_BY_EMAIL = "SELECT pseudo, email, nom, prenom, credit, administrateur FROM UTILISATEURS WHERE EMAIL = :email";
		private final String FIND_ALL = "SELECT pseudo, nom, prenom, email FROM UTILISATEURS";
		private final String UPDATE = "UPDATE UTILISATEURS SET pseudo = :pseudo,nom = :nom, prenom = :prenom, email = :email, telephone = :telephone WHERE email = :email";
		
		private final String COUNT_EMAIL = "SELECT count(email) FROM UTILISATEURS WHERE email = :email";
		private final String COUNT_PSEUDO = "SELECT count(pseudo) FROM UTILISATEURS WHERE pseudo = :pseudo";
		
		@Autowired
		private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
		
		@Override
		public void create(Utilisateur utilisateur) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			
			namedParameters.addValue("pseudo", utilisateur.getPseudo());
			namedParameters.addValue("nom", utilisateur.getNom());
			namedParameters.addValue("prenom", utilisateur.getPrenom());
			namedParameters.addValue("email", utilisateur.getEmail());
			namedParameters.addValue("telephone", utilisateur.getTelephone());
			namedParameters.addValue("adresse", utilisateur.getAdresse());
			namedParameters.addValue("motDePasse", utilisateur.getMotDePasse());
			
			namedParameterJdbcTemplate.update(INSERT, namedParameters);
		}
		
		@Override
		public List<Utilisateur> findAll() {
			return namedParameterJdbcTemplate.query(FIND_ALL, new UtilisateurRowMapper());
		}
		@Override
		public Utilisateur read(String emailUtilisateur) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("email", emailUtilisateur);
			
			return namedParameterJdbcTemplate.queryForObject(FIND_BY_EMAIL, namedParameters,
					new UtilisateurRowMapper());
		}

		@Override
		public Utilisateur readByPseudo(String pseudo) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("pseudo", pseudo);
			return namedParameterJdbcTemplate.queryForObject(
					FIND_BY_PSEUDO,
					namedParameters,
          new UtilisateurRowMapper()
			);
		}

		@Override
		public void update(Utilisateur utilisateur) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			
			namedParameters.addValue("pseudo", utilisateur.getPseudo());
			namedParameters.addValue("nom", utilisateur.getNom());
			namedParameters.addValue("prenom", utilisateur.getPrenom());
			namedParameters.addValue("email", utilisateur.getEmail());
			namedParameters.addValue("telephone", utilisateur.getTelephone());
			namedParameters.addValue("adresse", utilisateur.getAdresse());
			namedParameters.addValue("motDePasse", utilisateur.getMotDePasse());
			
			namedParameterJdbcTemplate.update(UPDATE, namedParameters);
		}
		
		@Override
		public int uniqueEmail(String email) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("email", email);
			return namedParameterJdbcTemplate.queryForObject(COUNT_EMAIL, namedParameters, Integer.class);
		}
		
		@Override
		public int uniquePseudo(String pseudo) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("pseudo", pseudo);
			return namedParameterJdbcTemplate.queryForObject(COUNT_PSEUDO, namedParameters, Integer.class);
		}

	static class UtilisateurRowMapper implements RowMapper<Utilisateur> {
		@Override
		public Utilisateur mapRow(ResultSet rs, int rowNum) throws SQLException {
			Utilisateur u = new Utilisateur();
			u.setPseudo(rs.getString("pseudo"));
			u.setNom(rs.getString("nom"));
			u.setPrenom(rs.getString("prenom"));
			u.setEmail(rs.getString("email"));
			u.setTelephone(rs.getString("telephone"));
			u.setCredit(rs.getInt("credit"));
			u.setAdmin(rs.getBoolean("administrateur"));

			// Association Adresse
			Adresse a = new Adresse();
			a.setId(rs.getInt("no_adresse"));
			u.setAdresse(a);

			return u;
		}
	}
}
	
	
	
	

