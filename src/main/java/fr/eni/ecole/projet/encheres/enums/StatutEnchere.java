package fr.eni.ecole.projet.encheres.enums;

public enum StatutEnchere {
  PAS_COMMENCEE(0), EN_COURS(1), CLOTUREE(2), LIVREE(3), ANNULEE(100);

  private final int value;

  StatutEnchere(int value) {
    this.value = value;
  }

  public StatutEnchere getStatutEnchere(int value) {
    return switch (value) {
      case 1 -> StatutEnchere.EN_COURS;
      case 2 -> StatutEnchere.CLOTUREE;
      case 3 -> StatutEnchere.LIVREE;
      case 100 -> StatutEnchere.ANNULEE;
      default -> StatutEnchere.PAS_COMMENCEE;
    };
  }

  public int getValue() {
    return value;
  }
}
