package fr.eni.ecole.projet.encheres.exceptions;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    // Liste des clefs d'externalisation
    private List<String> clefsExternalisations;
    
    public BusinessException() {
        super();
    }
    
    public BusinessException(String string) {
        super(string);
    }
    
    public List<String> getClefsExternalisations() {
        if (this.clefsExternalisations == null) {
            return new ArrayList<>(); // Retourner une liste vide si la liste est null
        }
        return clefsExternalisations;
    }
    
    /**    
    * Permet d'ajouter une clef d'erreur
    * @param clef
    * @comportement initialise la liste si besoin
    * */
    public void add(String clef) {
        if (clefsExternalisations == null) {
            clefsExternalisations = new ArrayList<>();
        }
        clefsExternalisations.add(clef);
    }
    
    /**
     * @return permet de confirmer si des erreurs ont été chargées
     */
    public boolean isValid() {
        // Return true if no errors are added, otherwise false
        return clefsExternalisations == null || clefsExternalisations.isEmpty();
    }
    
    
    
}
