package models.exeptions;

/**
 *
 * @author Elliot
 */
/**
 * Gère les exceptions de mauvais type lors de la création de noeuds ou de liens
 * @author Elliot
 */
public class MauvaisType extends RuntimeException{

    public MauvaisType() {
        super("Le type n'est pas valide");
    }

    public MauvaisType(char typeLieu) {
        super(String.format("Le type n'est pas valide, type utilisé : %s",typeLieu));
    }
    
}
