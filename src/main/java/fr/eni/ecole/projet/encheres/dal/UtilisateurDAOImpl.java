package fr.eni.ecole.projet.encheres.dal;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.ecole.projet.encheres.bo.Adresse;
import fr.eni.ecole.projet.encheres.bo.Utilisateur;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO {


	private final String FIND_ADRESSE_BY_NO_ADRESSE = "SELECT rue, code_postal, ville FROM ADRESSES WHERE no_adresse = :noAdresse";
	private final String FIND_CREDIT_BY_PSEUDO = "SELECT credit FROM UTILISATEURS WHERE pseudo = :pseudo";
	private final String FIND_NO_ADRESSE_BY_PSEUDO = "SELECT no_adresse FROM UTILISATEURS WHERE pseudo = :pseudo";
	private final String FIND_TEL_BY_PSEUDO = "SELECT telephone FROM UTILISATEURS WHERE pseudo = :pseudo";
	private final String FIND_BY_PSEUDO = "SELECT pseudo, nom, prenom, email, telephone, credit, administrateur, no_adresse FROM UTILISATEURS WHERE pseudo = :pseudo";
	private final String FIND_BY_EMAIL = "SELECT pseudo, email, nom, prenom, credit, administrateur FROM UTILISATEURS WHERE EMAIL = :email";
	private final String FIND_ALL = "SELECT pseudo, nom, prenom, email FROM UTILISATEURS";

	private final String COUNT_USERS_BY_NO_ADRESSE = "SELECT COUNT(*) FROM UTILISATEURS WHERE no_adresse = :noAdresse";
	private final String COUNT_EMAIL = "SELECT count(email) FROM UTILISATEURS WHERE email = :email";
	private final String COUNT_PSEUDO = "SELECT count(pseudo) FROM UTILISATEURS WHERE pseudo = :pseudo";
	private final String COUNT_MDP = "SELECT count(mot_de_passe) FROM UTILISATEURS WHERE mot_de_passe = :motDePasse";

	private final String DELETE_ADRESSE_BY_NO_ADRESSE = "DELETE FROM ADRESSES WHERE no_adresse = :noAdresse";
	private final String DELETE_USER_BY_PSEUDO = "DELETE FROM UTILISATEURS WHERE pseudo = :pseudo";
	private final String DELETE_ARTICLE_BY_PSEUDO = "DELETE FROM ARTICLES_A_VENDRE WHERE id_utilisateur = :pseudo";
	private final String DELETE_ENCHERE_BY_PSEUDO = "DELETE FROM ENCHERES WHERE id_utilisateur = :pseudo";

	private final String UPDATE_USER = "UPDATE UTILISATEURS SET nom = :nom, prenom = :prenom, email = :email, telephone = :telephone, no_adresse = :noAdresse WHERE pseudo = :pseudo";
	private final String UPDATE_USER_MDP = "UPDATE UTILISATEURS SET mot_de_passe = :motDePasse WHERE pseudo = :pseudo";

	private final String INSERT = "INSERT INTO UTILISATEURS(pseudo, nom, prenom, email, telephone, mot_de_passe, credit, no_adresse) VALUES (:pseudo, :nom, :prenom, :email, :telephone, :motDePasse, :credit, :noAdresse)";

	public static final String DEBITER_ACQUEREUR = "UPDATE Utilisateurs SET credit = (SELECT credit FROM UTILISATEURS WHERE pseudo = :pseudo) - :montant WHERE pseudo = :pseudo;";
	public static final String RECREDITER_UTILISATEUR = "UPDATE Utilisateurs SET credit = (SELECT credit FROM UTILISATEURS WHERE pseudo = :pseudo) + :montant WHERE pseudo = :pseudo;";

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public UtilisateurDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}



	// MODIFIER MOT DE PASSE
	@Override
	public Utilisateur readByPseudo(String pseudo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", pseudo);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_PSEUDO,namedParameters,
				new UtilisateurRowMapper()
				);
	}

	// Compte combien de fois le mot de passe saisi apparaît dans la base
	public int countPassword(String motDePasse) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("motDePasse", motDePasse);

		int count = namedParameterJdbcTemplate.queryForObject(COUNT_MDP, namedParameters, Integer.class);
		System.out.println("Count: " + count);
		return count;
	}



	// Met à jour le mot de passe de l'utilisateur
	public void updateMotDePasse(Utilisateur utilisateur) {
		System.out.println("La méthode mettreAjourMotDePasse est appelée.");

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("motDePasse", utilisateur.getMotDePasse());
		namedParameters.addValue("pseudo", utilisateur.getPseudo());

		namedParameterJdbcTemplate.update(UPDATE_USER_MDP, namedParameters);
		System.out.println("Mot de passe mis à jour dans la base de données pour l'utilisateur : " + utilisateur.getPseudo());
	}


	// SUPPRIMER MON PROFIL
	// Récupérer le no_adresse à partir du pseudo
	// Compter les utilisateurs ayant le même no_adresse
	public int countUsersByNoAdresse(int noAdresse) {
		System.out.println("Début de la comptabilisation des utilisateurs ayant le même no_adresse : " + noAdresse);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("noAdresse", noAdresse);

		int count = namedParameterJdbcTemplate.queryForObject(COUNT_USERS_BY_NO_ADRESSE, namedParameters, Integer.class);
		System.out.println("Nombre d'utilisateurs avec no_adresse " + noAdresse + " : " + count);

		return count;
	}




	//ENCHERES
	@Override
	public void debiter(int montant, String pseudo) {
		MapSqlParameterSource namedParams = new MapSqlParameterSource();
		namedParams.addValue("montant", montant);
		namedParams.addValue("pseudo", pseudo);
		namedParameterJdbcTemplate.update(DEBITER_ACQUEREUR, namedParams);
	}

	@Override
	public void recrediterPrecedentEncherisseur(int montant, String pseudo) {
		MapSqlParameterSource namedParams = new MapSqlParameterSource();
		namedParams.addValue("montant", montant);
		namedParams.addValue("pseudo", pseudo);
		namedParameterJdbcTemplate.update(RECREDITER_UTILISATEUR, namedParams);
	}

	// SUPPRIMER MON PROFIL
	public void deleteByPseudo(String pseudo) {
		System.out.println("Suppression de l'utilisateur avec le pseudo : " + pseudo);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", pseudo);
		namedParameterJdbcTemplate.update(DELETE_USER_BY_PSEUDO, namedParameters);
		System.out.println("Utilisateur avec le pseudo " + pseudo + " supprimé.");
	}

	public void deleteAdresseByNoAdresse(int noAdresse) {
		System.out.println("Suppression de l'adresse avec no_adresse : " + noAdresse);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("noAdresse", noAdresse);
		namedParameterJdbcTemplate.update(DELETE_ADRESSE_BY_NO_ADRESSE, namedParameters);
		System.out.println("Adresse avec no_adresse " + noAdresse + " supprimée.");
	}

	public void deleteArticleByPseudo(String pseudo) {
		System.out.println("Suppression de l'article avec pseudo : " + pseudo);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", pseudo);
		namedParameterJdbcTemplate.update(DELETE_ARTICLE_BY_PSEUDO, namedParameters);
		System.out.println("Pseudo avec pseudo " + pseudo + " supprimée.");
	}

	public void deleteEnchereByPseudoe(String pseudo) {
		System.out.println("Suppression de l'enchere avec pseudo : " + pseudo);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", pseudo);
		namedParameterJdbcTemplate.update(DELETE_ENCHERE_BY_PSEUDO, namedParameters);
		System.out.println("Enchere avec pseudo " + pseudo + " supprimée.");
	}


	// MODIFIER MON PROFIL 
	@Override
	public void update(Utilisateur utilisateur) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		namedParameters.addValue("pseudo", utilisateur.getPseudo());
		namedParameters.addValue("nom", utilisateur.getNom());
		namedParameters.addValue("prenom", utilisateur.getPrenom());
		namedParameters.addValue("email", utilisateur.getEmail());
		namedParameters.addValue("telephone", utilisateur.getTelephone());
		namedParameters.addValue("adresse", utilisateur.getAdresse());
		namedParameters.addValue("noAdresse", utilisateur.getAdresse().getNoAdresse());

		// Log pour vérifier les paramètres envoyés à la base de données
		System.out.println("Exécution de la requête : UPDATE UTILISATEURS SET nom = ?, prenom = ?, email = ?, telephone = ?, no_adresse = ? WHERE pseudo = ?");
		System.out.println("Paramètres : " + namedParameters);

		namedParameterJdbcTemplate.update(UPDATE_USER, namedParameters);
	}


	// Récupérer no_adresse par pseudo
	@Override
	public int getNoAdresseByPseudo(String pseudo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", pseudo);
		return namedParameterJdbcTemplate.queryForObject(FIND_NO_ADRESSE_BY_PSEUDO, namedParameters, Integer.class);
	}

	public Adresse getAdresseByNoAdresse(int noAdresse) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("noAdresse", noAdresse);

		return namedParameterJdbcTemplate.queryForObject(FIND_ADRESSE_BY_NO_ADRESSE, namedParameters, new RowMapper<Adresse>() {
			@Override
			public Adresse mapRow(ResultSet rs, int rowNum) throws SQLException {
				Adresse adresse = new Adresse();
				adresse.setRue(rs.getString("rue"));
				adresse.setCodePostal(rs.getString("code_postal"));
				adresse.setVille(rs.getString("ville"));
				return adresse;
			}
		});
	}

	@Override
	public int getCreditByPseudo(String pseudo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", pseudo);  
		return namedParameterJdbcTemplate.queryForObject(FIND_CREDIT_BY_PSEUDO, namedParameters, Integer.class);  
	}




	//PAGE MON PROFIL

	@Override
	public String getTelephoneByPseudo(String pseudo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", pseudo);
		return namedParameterJdbcTemplate.queryForObject(FIND_TEL_BY_PSEUDO, namedParameters, String.class);
	}




	// S'INSCRIRE:
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
		namedParameters.addValue("credit", utilisateur.getCredit());
		namedParameters.addValue("noAdresse", utilisateur.getAdresse().getId());	

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
			// u.setMotDePasse(rs.getString("mot_de_passe")); 

			// Association Adresse
			Adresse a = new Adresse();
			a.setId(rs.getInt("no_adresse"));
			u.setAdresse(a);

			System.out.println("utilisateur :  " + u);

			return u;
		}
	}





}

