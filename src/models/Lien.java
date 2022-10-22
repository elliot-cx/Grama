package models;

import models.exeptions.MauvaisType;
import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author Elliot
 */
/**
 * Une classe permettant de créer des liens
 * @author Elliot
 */
public class Lien {
    
    private final Noeud lieu1;
    private final Noeud lieu2;
    private char typeRoute; //a: autoroute, n : national , d: départementale
    private float distance;
    
    /**
     * Créer un nouveau Lien entre deux noeuds
     * @param lieu1 Noeud 1
     * @param lieu2 Noeud 2
     * @param typeRoute Type de Lien
     * @param distance Distance entre les deux noeuds
     */
    public Lien(Noeud lieu1, Noeud lieu2, char typeRoute,float distance) {
        String[] type_autorise = {"A","N","D"};
        if(!Arrays.asList(type_autorise).contains(String.valueOf(typeRoute))){
            throw new MauvaisType(typeRoute);
        }
        this.lieu1 = lieu1;
        this.lieu2 = lieu2;
        this.typeRoute = typeRoute;
        this.distance = distance;
    }

    /**
     * Obtient le premier noeud du lien
     * @return Un lieu noeud
     */
    public Noeud getLieu1() {
        return lieu1;
    }

    /**
     * Obtient le premier noeud du lien
     * @return Un lieu noeud
     */
    public Noeud getLieu2() {
        return lieu2;
    }

    /**
     * Obtient le type de route du lien
     * @return Un type de route char
     */
    public char getTypeRoute() {
        return typeRoute;
    }

    /**
     * Obtient la distance du lieu
     * @return Une distance float
     */
    public float getDistance() {
        return distance;
    }
    
    /**
     * Renvoie l'autre noeud par rapport à un lien
     * @param noeud Un des deux noeuds relié du lien
     * @return Le noeud voisin du noeud en paramètre
     */
    public Noeud getVoisin(Noeud noeud){
        if (this.lieu1.equals(noeud)){
            return this.lieu2;
        }
        return this.lieu1;
    }
    
    /**
     * Redéfinition d'un type de route
     * @param typeRoute 
     */
    public void setTypeRoute(char typeRoute) {
        this.typeRoute = typeRoute;
    }
    
    //ToString
    
    @Override
    public String toString() {
        return typeRoute + ", " + distance + "km";
    }
    
    public String getTypeStr(){
        return switch(this.typeRoute){
            case 'D' -> "Départementale";
            case 'N' -> "Nationale";
            case 'A' -> "Autoroute";
            default -> "Inconnue";
        };
    }
    
    //Equals et HashCode

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.typeRoute;
        hash = 23 * hash + Float.floatToIntBits(this.distance);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Lien other = (Lien) obj;
        if (this.typeRoute != other.typeRoute) {
            return false;
        }
        if (this.distance != other.distance) {
            return false;
        }
        
        if (Objects.equals(this.lieu1, other.lieu1) && Objects.equals(this.lieu2, other.lieu2)){
            return true;
        }
        
        if (Objects.equals(this.lieu1, other.lieu2) && Objects.equals(this.lieu2, other.lieu1)){
            return true;
        }
        return false;
    }
}