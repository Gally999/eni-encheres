package fr.eni.ecole.projet.encheres.enums;

public enum VenteFilter implements AchatsOuVentesFilter {
  NON_DEBUTEES(0), EN_COURS(1), TERMINEES(2);

  private final int value;

  VenteFilter(int value) {
    this.value = value;
  }

  public static VenteFilter getVenteFilter(int value) {
    return switch (value) {
      case 1 -> VenteFilter.EN_COURS;
      case 2 -> VenteFilter.TERMINEES;
      default -> VenteFilter.NON_DEBUTEES;
    };
  }

  public int getValue() {
    return value;
  }
}
