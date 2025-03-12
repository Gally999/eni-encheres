package fr.eni.ecole.projet.encheres.enums;

public enum AchatFilter {
  OUVERTES(0), EN_COURS(1), REMPORTEES(2);

  private int value;

  AchatFilter(int value) {
    this.value = value;
  }

  public static AchatFilter getAchatFilter(int value) {
    return switch (value) {
      case 0 -> AchatFilter.OUVERTES;
      case 1 -> AchatFilter.EN_COURS;
      case 2 -> AchatFilter.REMPORTEES;
      default -> AchatFilter.OUVERTES;
    };
  }

  public int getValue() {
    return value;
  }
}
