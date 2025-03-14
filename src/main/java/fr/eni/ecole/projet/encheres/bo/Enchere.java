package fr.eni.ecole.projet.encheres.bo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Enchere implements Serializable {
  private static final long serialVersionUID = 1L;

  private Utilisateur acquereur;
  private ArticleAVendre article;
  private LocalDateTime date;
  private int montant;

  public Enchere() {
  }

  public Enchere(Utilisateur acquereur, ArticleAVendre article, LocalDateTime date, int montant) {
    this.acquereur = acquereur;
    this.article = article;
    this.date = date;
    this.montant = montant;
  }

  public Utilisateur getAcquereur() {
    return acquereur;
  }

  public void setAcquereur(Utilisateur acquereur) {
    this.acquereur = acquereur;
  }

  public ArticleAVendre getArticle() {
    return article;
  }

  public void setArticle(ArticleAVendre article) {
    this.article = article;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public int getMontant() {
    return montant;
  }

  public void setMontant(int montant) {
    this.montant = montant;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Enchere enchere = (Enchere) o;
    return montant == enchere.montant && Objects.equals(acquereur, enchere.acquereur) && Objects.equals(article, enchere.article) && Objects.equals(date, enchere.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(acquereur, article, date, montant);
  }

  @Override
  public String toString() {
    return "Enchere[" +
        "acquereur=" + acquereur +
        ", article=" + article +
        ", date=" + date +
        ", montant=" + montant +
        ']';
  }
}
