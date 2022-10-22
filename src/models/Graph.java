package models;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Cette classe permet de générer un Graph avec des noeuds et des liens et
 * d'intéragir avec ces derniers grâce à de nombreuses méthodes
 *
 * @author Elliot
 */
/**
 * Une classe permettant de générer et utiliser un graph avec des noeuds et des liens
 * @author Elliot
 */
public class Graph {

    private ArrayList<Noeud> Noeuds;
    private ArrayList<Lien> Liens;
    private int nb_erreurs;

    
    public Graph() {
        this.Noeuds = new ArrayList<>();
        this.Liens = new ArrayList<>();
    }

    /**
     * Récupère la liste des noeuds du Graph
     *
     * @return Liste de noeuds
     */
    public ArrayList<Noeud> getNoeuds() {
        return Noeuds;
    }

    /**
     * Récupère la liste des liens du Graph
     *
     * @return Liste de liens
     */
    public ArrayList<Lien> getLiens() {
        return Liens;
    }

    /**
     * Défini la liste des noeuds du graphe
     * @param noeuds Une liste de noeud
     */
    public void setNoeuds(ArrayList<Noeud> noeuds) {
        this.Noeuds = noeuds;
    }

    /**
     * Défini la liste des liens du graphe
     * @param liens Une liste de lien
     */
    public void setLiens(ArrayList<Lien> liens) {
        this.Liens = liens;
    }

    /**
     * Recupère le nombre de Liens d'un type spécifique contenu dans une liste
     * de liens
     *
     * @param type le type de lien à compter
     * @return nombre de Liens de type
     */
    public int getNbLiens(ArrayList<Lien> ListeLiens, char type) {
        int nb = 0;
        for (Lien l : ListeLiens) {
            if (l.getTypeRoute() == type) {
                nb++;
            }
        }
        return nb;
    }

    /**
     * Recupère le nombre de Noeuds d'un type spécifique contenu dans une liste
     * de noeuds
     *
     * @param type le type du noeud à compter
     * @return nombre de Noeuds de type
     */
    public int getNbNoeuds(ArrayList<Noeud> ListeNoeuds, char type) {
        int nb = 0;
        for (Noeud n : ListeNoeuds) {
            if (n.getType() == type) {
                nb++;
            }
        }
        return nb;
    }

    /**
     * Obtiens le nombre d'erreurs présents dans le fichier
     *
     * @return Nombre d'erreur du fichier
     */
    public int getNb_erreurs() {
        return nb_erreurs;
    }

    /**
     * Obtiens la liste des noeuds contenant une chaine de caractère présente
     * dans leurs noms
     *
     * @param nom le nom partiel ou total à chercher
     * @return Une liste de Noeuds contenant le nom
     */
    public ArrayList<Noeud> searchNoeuds(String nom) {
        ArrayList<Noeud> ListeNoeuds = new ArrayList<>();
        for (Noeud n : this.Noeuds) {
            if (n.toString().toLowerCase().contains(nom.toLowerCase())) {
                ListeNoeuds.add(n);
            }
        }
        return ListeNoeuds;
    }

    /**
     * Obtiens la liste des liens contenant une chaine de caractère présente
     * dans leurs noms
     *
     * @param nom le nom partiel ou total à chercher
     * @return Une liste de Liens contenant le nom
     */
    public ArrayList<Lien> searchLiens(String nom) {
        ArrayList<Lien> ListeLiens = new ArrayList<>();
        for (Lien l : this.Liens) {
            if (l.toString().toLowerCase().contains(nom.toLowerCase())) {
                ListeLiens.add(l);
            }
        }
        return ListeLiens;
    }

    /**
     * Obtiens le noeud à partir de son nom
     *
     * @param nom Le nom du noeud (équivalent à 'Noeud.toString();' )
     * @return retourne le noeud si il a été trouvé sinon null
     */
    public Noeud getNoeud(String nom) {
        for (Noeud n : this.Noeuds) {
            if (n.toString().equals(nom)) {
                return n;
            }
        }
        return null;
    }

    /**
     * Obtiens un lien à partir de son nom (Type de lien + Distance)
     *
     * @param nom Le nom du lien (équivalent à 'Lien.toString();' )
     * @return retourne le lien si il a été trouvé sinon null
     */
    public Lien getLien(String nom) {
        for (Lien l : this.Liens) {
            if (l.toString().equals(nom)) {
                return l;
            }
        }
        return null;
    }

    /**
     * Obtien un noeud à partir de ses coordonnées
     *
     * @param location Un point qui définit sa position
     * @param DiametreNoeud La taille du diametre graphique du noeud
     * @param first Retourne le premier résultat trouvé ou non
     * @return
     */
    public Noeud getNoeud(Point location, int DiametreNoeud, boolean first) {
        Noeud noeud_found = null;
        for (Noeud n : this.getNoeuds()) {
            Point center_point = new Point(n.getPosX() + DiametreNoeud / 2, n.getPosY() + DiametreNoeud / 2);
            if (Point.distance(location.x, location.y, center_point.x, center_point.y) <= DiametreNoeud / 2) {
                noeud_found = n;

                //retourne dès le premier noeud trouvé
                if (first) {
                    return noeud_found;
                }
            }
        }
        return noeud_found;
    }

    /**
     * Obtien un lien à partir de ses coordonnées
     *
     * @param location Un point qui définit sa position
     * @param DiametreNoeud La taille du diametre graphique du noeud
     * @param first Retourne le premier résultat trouvé ou non
     * @return
     */
    public Lien getLien(Point location, int DiametreNoeud, boolean first) {
        Lien lien_found = null;
        for (Lien l : this.getLiens()) {

            Point pt1 = new Point(
                    l.getLieu1().getPosX() + DiametreNoeud / 2,
                    l.getLieu1().getPosY() + DiametreNoeud / 2);
            Point pt2 = new Point(
                    l.getLieu2().getPosX() + DiametreNoeud / 2,
                    l.getLieu2().getPosY() + DiametreNoeud / 2);

            Point center_point = new Point(((pt1.x + pt2.x) / 2), ((pt1.y + pt2.y) / 2));

            if (Point.distance(location.x, location.y, center_point.x, center_point.y) <= 25) {
                lien_found = l;

                //retourne dès le premier lien trouvé 
                if (first) {
                    return lien_found;
                }
            }
        }
        return lien_found;
    }

    /**
     * Obtiens la distance entre deux noeuds
     *
     * @param noeud1
     * @param noeud2
     * @return Un entier représentant la distance en nombre de noeud parcouru
     */
    public int getDistance(Noeud noeud1, Noeud noeud2) {
        if (noeud1 != null && noeud2 != null && noeud1 != noeud2) {
            if (noeud1.getVoisins().contains(noeud2)) {
                return 1;
            }
            if (noeud1.getVoisins2Distance().contains(noeud2)) {
                return 2;
            }
            return 0;
        }
        return -1;
    }

    /**
     * Compare les types de noeuds à 2 distances entre deux noeuds
     * @param noeud1
     * @param noeud2
     * @param type Type de noeud à comparer
     * @return Un entier représentant la différence entre les deux
     */
    public int Compare2DistanceNodeType(Noeud noeud1, Noeud noeud2, char type) {
        if (noeud1 != null && noeud2 != null && noeud1 != noeud2) {
            int nb1 = this.getNbNoeuds(noeud1.getVoisins2Distance(), type);
            int nb2 = this.getNbNoeuds(noeud2.getVoisins2Distance(), type);
            if (nb1 > nb2) {
                return 1;
            } else if (nb1 < nb2) {
                return -1;
            }
        }
        return 0;
    }
    
    /**
     * Réinitialise la position de tout les points du graph à X:0 Y:0
     */
    public void resetNoeudsPositions(){
        for(Noeud n : this.getNoeuds()){
            n.setPosition(new Point(0,0));
        }
    }

    /**
     * Création d'un sous graphe à partir d'une liste de noeud
     * @param ListeNoeuds Une liste de noeud
     * @return Un objet de type graph
     */
    public Graph getSubGraph(ArrayList<Noeud> ListeNoeuds) {
        Graph subGraph = new Graph();

        for (Noeud n : ListeNoeuds) {

            Noeud subNoeud = new Noeud(n.getType(), n.getNom());

            if (subGraph.Noeuds.contains(subNoeud)) {
                subNoeud = subGraph.Noeuds.get(subGraph.Noeuds.indexOf(subNoeud));
            }

            for (Lien l : n.getLiens()) {

                if (ListeNoeuds.contains(l.getVoisin(n))) {

                    Noeud subNoeudVoisin = new Noeud(l.getVoisin(n).getType(), l.getVoisin(n).getNom());

                    if (!subGraph.Noeuds.contains(subNoeudVoisin)) {
                        subGraph.Noeuds.add(subNoeudVoisin);
                    } else {
                        subNoeudVoisin = subGraph.Noeuds.get(subGraph.Noeuds.indexOf(subNoeudVoisin));
                    }

                    Lien l_temporaire = new Lien(subNoeud, subNoeudVoisin, l.getTypeRoute(), l.getDistance());

                    subNoeud.addLien(l_temporaire);
                    subGraph.Liens.add(l_temporaire);
                }
            }

            if (!subGraph.Noeuds.contains(subNoeud)) {
                subGraph.Noeuds.add(subNoeud);
            };
        }

        return subGraph;
    }

    /**
     * Trouver le noeud ayant la distance la plus faible
     * @param dist Un tableau de distance représenté en float
     * @param b La liste des noeuds déjà visités représenté par un boolean
     * @return L'index de la distance minimum
     */
    public int minDistance(float dist[], boolean b[]) {
        float min = Float.MAX_VALUE;
        int index = -1;

        for (int i = 0; i < this.getNoeuds().size(); i++) {
            if (b[i] == false) {
                if (dist[i] <= min) {
                    min = dist[i];
                    index = i;
                }
            }
        }
        return index;
    }

    /**
     * Récupére la distance du chemin minimal entre deux noeuds
     * @param noeudDepart
     * @param noeudArrive
     * @return La distance en float
     */
    public float Dijkstra(Noeud noeudDepart, Noeud noeudArrive) {
        //ArrayList<Noeud>
        /*


        ArrayList<Noeud> NoeudsRestants = new ArrayList<>(this.getNoeuds());
        NoeudsRestants.remove(noeudDepart);

        while(NoeudsRestants.size() > 0){

        }
         */
        ArrayList<Noeud> chemin = new ArrayList<>();
        ArrayList<Noeud> listeNoeuds = this.getNoeuds();
        int nbNoeuds = listeNoeuds.size();

        float dist[] = new float[nbNoeuds];

        boolean b[] = new boolean[nbNoeuds];

        for (int i = 0; i < nbNoeuds; i++) {
            dist[i] = Float.MAX_VALUE;
            b[i] = false;
        }

        dist[listeNoeuds.indexOf(noeudDepart)] = 0;

        for (int i = 0; i < nbNoeuds - 1; i++) {
            int u = minDistance(dist, b);

            b[u] = true;

            LinkedList<Lien> listeLiens = listeNoeuds.get(u).getLiens();

            for (int j = 0; j < listeLiens.size(); j++) {
                Lien lien = listeLiens.get(j);
                int n_voisin = listeNoeuds.indexOf(lien.getVoisin(listeNoeuds.get(u)));
                if (!b[j] && dist[u] != Float.MAX_VALUE
                        && dist[u] + lien.getDistance() < dist[n_voisin]) {
                    dist[n_voisin] = dist[u] + lien.getDistance();
                }
            }

        }

        return dist[listeNoeuds.indexOf(noeudArrive)];

    }

    /**
     * Charge un graph à partir d'un fichier CSV
     *
     * @param path Chemin du fichier
     */
    public boolean LoadCSV(String path) {
        //Réinitialisation lors d'un chargement de map
        this.Noeuds.clear();
        this.Liens.clear();

        try {
            Path filepath = Path.of(path);
            List<String> lines = Files.readAllLines(filepath);

            for (String line : lines) {
                if (!line.equals("")) {
                    line = line.split(";;")[0];

                    //System.out.println(line);
                    String[] noeud = line.split(":", 2);

                    /*
                    Noeud est donc un tableau avec 2 index
                        0 : Représente les info du noeud actuel
                        1 : Représente ses liens 
                     */
                    String[] info_noeud = noeud[0].split(",");

                    Noeud noeud_actuel = new Noeud(info_noeud[0].charAt(0), info_noeud[1]);

                    if (!this.Noeuds.contains(noeud_actuel)) {
                        this.Noeuds.add(noeud_actuel);
                    } else {
                        noeud_actuel = this.Noeuds.get(this.Noeuds.indexOf(noeud_actuel));
                    }

                    //vérification d'existance
                    String[] Liens = noeud[1].split(";");

                    for (String lien : Liens) {

                        String[] info_lien = lien.split("::");

                        String[] attribut_lien = info_lien[0].split(",");

                        char type_lien = attribut_lien[0].charAt(0);

                        float distance_lien = Float.parseFloat(attribut_lien[1]);

                        String[] noeud_lien = info_lien[1].split(",");

                        Noeud noeud_dest = new Noeud(noeud_lien[0].charAt(0), noeud_lien[1]);

                        if (!this.Noeuds.contains(noeud_dest)) {
                            this.Noeuds.add(noeud_dest);
                        } else {
                            noeud_dest = this.Noeuds.get(this.Noeuds.indexOf(noeud_dest));
                        }

                        Lien lien_actuel = new Lien(noeud_actuel, noeud_dest, type_lien, distance_lien);

                        if (!this.Liens.contains(lien_actuel)) {
                            this.Liens.add(lien_actuel);
                            noeud_dest.addLien(lien_actuel);
                            noeud_actuel.addLien(lien_actuel);
                        } else {
                            this.nb_erreurs += 1;
                            //System.out.println("Route déjà existante :" + lien_actuel.toString());
                        }
                    }
                }
            }
        } catch (IOException ex) {
            //Erreur de lecture
            javax.swing.JOptionPane.showMessageDialog(null, "Une erreur est survenue lors de l'ouverture du fichier !", "Grama", 0);
            return false;
        } catch (IndexOutOfBoundsException ex) {
            this.nb_erreurs += 1;
        } catch (Exception e) {
            this.nb_erreurs += 1;
        }
        return true;
    }

    /**
     * Sauvegarde le graph actuellement chargé
     *
     * @param path Emplacement de sauvegarde
     */
    public void SaveCSV(String path) {
        //TODO
    }
}
