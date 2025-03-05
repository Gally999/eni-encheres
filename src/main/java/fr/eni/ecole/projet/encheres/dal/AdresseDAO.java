package fr.eni.ecole.projet.encheres.dal;

import fr.eni.ecole.projet.encheres.bo.Adresse;

import java.util.List;

public interface AdresseDAO {
  List<Adresse> findAllAvailable(long id);
}
