package models;

import models.exeptions.MauvaisType;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author Elliot
 */
/**
 * Une classe permettant de créer des noeuds
 * @author Elliot
 */
public class Noeud {
    private LinkedList<Lien> Liens;
    private char typeLieu; //v: ville, l : loisir , r: restaurant
    private final String nom;
    private Color color;
    private Point position;
    
    /**
     * Permet de créer un Noeud
     * @param typeLieu désigne le type de Lieu par un char (V,R,L)
     * @param nom définit le nom du Noeud
     */
    public Noeud(char typeLieu,String nom) {
        String[] type_autorise = {"V","R","L"};
        if(!Arrays.asList(type_autorise).contains(String.valueOf(typeLieu))){
            throw new MauvaisType(typeLieu);
        }
        Liens = new LinkedList<>();
        this.typeLieu = typeLieu;
        this.nom = nom;
        this.position = new Point(0,0);
        switch(typeLieu){
            case 'V' -> {
                this.color = Color.red;
            }
            case 'R' -> {
                this.color = Color.blue;
            }
            default -> {
                this.color = Color.green;    
            }
        }
    }
    
    
    /**
     * Permet d'ajouter un Lien au Noeud
     * @param Lien Un objet de type Lien
     */
    public void addLien(Lien Lien){
        Liens.add(Lien);
    }
    
    /**
     * @return Le nom du Noeud
     */
    public String getNom() {
        return nom;
    }
    
    /**
     * Récupére le type de lieu du noeud (Restaurant, Loisir, Ville)
     * @return Un type de lieu
     */
    public char getType(){
        return typeLieu;
    }
    
    /**
     * Récupére la couleur représentant le type de noeud
     * @return Une couleur de noeud
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Récupére la liste de liens du noeud actuel
     * @return Les liens relié au noeud
     */
    public LinkedList<Lien> getLiens() {
        return Liens;
    }

    /**
     * Obtient la position X du noeud actuel
     * @return La position X du noeud en entier
     */
    public int getPosX() {
        return this.position.x;
    }

    /**
     * Obtient la position Y du noeud actuel
     * @return La position Y du noeud en entier
     */
    public int getPosY() {
        return this.position.y;
    }

    /**
     * Obtient la position du noeud
     * @return Un point
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Défini la position du point
     * @param position 
     */
    public void setPosition(Point position) {
        this.position = position;
    }
    
    /**
     * Retourne le type de lieu sous forme d'une chaine de caractère
     * @return Chaine de caractère
     */
    public String getTypeStr(){
        return switch (this.typeLieu) {
            case 'V' -> "Ville";
            case 'R' -> "restaurant";
            case 'L' -> "Loisir";
            default -> "Inconnu";
        };
    }
    
    /**
     * Récupere la liste des voisins d'un noeud
     * @return Une liste de noeud
     */
    public ArrayList<Noeud> getVoisins(){
        ArrayList<Noeud> liste_voisins = new ArrayList<>();
        for(Lien lien : this.Liens){
            liste_voisins.add(lien.getVoisin(this));
        }
        return liste_voisins;
    }
    
    /**
     * Récupére la liste des voisins d'un noeud en sélectionnant un type
     * @param type
     * @return Une liste de noeud
     */
    public ArrayList<Noeud> getVoisins(char type){
        ArrayList<Noeud> liste_voisins = new ArrayList<>();
        for(Noeud voisin: this.getVoisins()){
            if (voisin.getType() == type){
                liste_voisins.add(voisin);
            }
        }
        return liste_voisins;
    }
    
    /**
     * Obtiens la liste des voisins se situant à 2 distances du noeud actuel
     * @return Liste de Noeuds
     */
    public ArrayList<Noeud> getVoisins2Distance(){
        ArrayList<Noeud> liste_voisins = new ArrayList<>();
        ArrayList<Noeud> voisinsDirect = this.getVoisins();
        for(Noeud n : voisinsDirect){
            for(Noeud n2 : n.getVoisins()){
                if(!n2.equals(this) && !voisinsDirect.contains(n2) && !liste_voisins.contains(n2)){
                    liste_voisins.add(n2);
                }
            }
        }
        return liste_voisins;
    }

    @Override
    public String toString() {
        return typeLieu + ", " + nom;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.typeLieu;
        hash = 83 * hash + Objects.hashCode(this.nom);
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
        final Noeud other = (Noeud) obj;
        if (this.typeLieu != other.typeLieu) {
            return false;
        }
        return Objects.equals(this.nom, other.nom);
    }
  
}