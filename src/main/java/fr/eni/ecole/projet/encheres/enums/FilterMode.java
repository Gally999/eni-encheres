package fr.eni.ecole.projet.encheres.enums;

public enum FilterMode {
  ACHATS(0), VENTES(1);

  private int value;

  FilterMode(int value) {
    this.value = value;
  }

  public static FilterMode getFilterMode(int value) {
    return switch (value) {
      case 0 -> FilterMode.ACHATS;
      case 1 -> FilterMode.VENTES;
      default -> FilterMode.ACHATS;
    };
  }

  public int getValue() {
    return value;
  }
}
