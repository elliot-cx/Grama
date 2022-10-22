package gui;

import models.Graph;
import models.Lien;
import models.Noeud;
import controller.interfaces.GraphDisplayListener;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 *
 * @author Elliot
 */
public class GraphDisplayPane extends JPanel {

    //General Attributs
    private Graph graph;
    public static final int DIAMETRE_NOEUD = 40;

    //Abilities Attributs
    //Représente les capacités d'intéractions de l'utilisateur
    private boolean selectAbility = true;
    private int selectsNumberAbility = 2;
    private boolean moveAbility = true;
    private boolean compareAbility = false;
    private boolean zoomAbility = false;

    //Selections
    private LinkedList<Noeud> selected_noeuds;
    private Lien selected_lien;

    //Listener
    private GraphDisplayListener listener;

    // Graph Visibility
    private boolean background_visible = true;
    private ArrayList<Character> TypeNoeudsVisibles;
    private ArrayList<Character> TypeLiensVisibles;

    //View attributs
    /*
    private float zoom_factor = 1.0f;
    private Point offset_starting_point;
    private float translateX_offset = 0;
    private float translateY_offset = 0;
     */
    //Components
    private JPopupMenu noeud_menu;

    //Constructor
    public GraphDisplayPane() {
        //this.setPreferredSize(new Dimension(500, 300));
        this.selected_noeuds = new LinkedList<>();
        this.selected_noeuds.push(null);
        initComponents();
    }

    private void initComponents() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                GraphDisplayPane.this.DisplayPaneMousePressed(e);
            }
        });
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                GraphDisplayPane.this.DisplayPaneMouseDragged(e);
            }
        });
        this.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                GraphDisplayPane.this.DisplayPaneWheelMoved(e);
            }
        });

        //TODO : Améliorer la visibilité des noeuds
        this.TypeNoeudsVisibles = new ArrayList<>();
        this.setTypeNoeudVisibles('V', true);
        this.setTypeNoeudVisibles('R', true);
        this.setTypeNoeudVisibles('L', true);

        this.TypeLiensVisibles = new ArrayList<>();
        this.setTypeLiensVisibles('D', true);
        this.setTypeLiensVisibles('N', true);
        this.setTypeLiensVisibles('A', true);

        //Initialisation de la PopupMenu
        noeud_menu = new JPopupMenu("Menu");
        JMenuItem voisin1distance = new JMenuItem("Voisins 1 distance");
        JMenuItem voisin2distance = new JMenuItem("Voisins 2 distances");
        noeud_menu.add(voisin1distance);
        noeud_menu.add(voisin2distance);

        voisin1distance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphDisplayPane.this.DisplayPaneShow1Distance(e);
            }
        });
        voisin2distance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphDisplayPane.this.DisplayPaneShow2Distance(e);
            }
        });
    }

    /**
     * Récupére le grpah qui va être affiché
     *
     * @return Un graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Défini le graphe qui va être affiché
     *
     * @param graph
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
        this.selected_noeuds = new LinkedList<>();
        this.selected_noeuds.add(null);
        this.calcPositions();
        this.repaint();
    }

    /**
     * Permet de selectionner un noeud qui sera afficher graphiquement
     *
     * @param noeud
     */
    public void selectNoeud(Noeud noeud) {
        if (this.graph.getNoeuds().contains(noeud)) {

            if (this.selected_noeuds.contains(noeud)) {
                this.selected_noeuds.clear();
                this.selected_noeuds.push(null);
            }

            this.selected_noeuds.push(noeud);

            if (this.selected_noeuds.size() > this.selectsNumberAbility) {
                this.selected_noeuds.removeLast();
            }
        } else {
            //Le noeud est null
            this.selected_noeuds.clear();
            this.selected_noeuds.push(null);
        }
        if (this.listener != null) {
            this.listener.SelectedNoeudsChanged(selected_noeuds);
        }

        repaint();
    }

    /**
     * Permet de selectionner un lien qui sera afficher graphiquement
     *
     * @param lien
     */
    public void selectLien(Lien lien) {
        if (graph.getLiens().contains(lien)) {
            this.selected_lien = lien;
        } else if (lien == null) {
            this.selected_lien = null;
        }
        if (this.listener != null) {
            this.listener.SelectedLienChanged(selected_lien);
        }
        repaint();
    }

    /**
     * Permet de savoir si le background est visible
     *
     * @return
     */
    public boolean isBackground_visible() {
        return background_visible;
    }

    /**
     * Permet de rendre le background visible ou pas
     *
     * @param background_visible
     */
    public void setBackground_visible(boolean background_visible) {
        this.background_visible = background_visible;
        this.repaint();
    }

    /**
     * Permet de rendre les noeuds visibles ou non
     *
     * @param type
     */
    public void setTypeNoeudVisibles(char type, boolean visible) {
        boolean isContaining = this.TypeNoeudsVisibles.contains(type);
        if (visible) {
            if (!isContaining) {
                this.TypeNoeudsVisibles.add(type);
            }
        } else {
            if (isContaining) {
                this.TypeNoeudsVisibles.remove(this.TypeNoeudsVisibles.indexOf(type));
            }
        }
        this.repaint();
    }

    /**
     * Permet de rendre les liens visibles ou non
     *
     * @param type
     */
    public void setTypeLiensVisibles(char type, boolean visible) {

        boolean isContaining = this.TypeLiensVisibles.contains(type);
        if (visible) {
            if (!isContaining) {
                this.TypeLiensVisibles.add(type);
            }
        } else {
            if (isContaining) {
                this.TypeLiensVisibles.remove(this.TypeLiensVisibles.indexOf(type));
            }
        }
        this.repaint();
    }

    /**
     * Pour savoir si l'utilisateur à le droit de comparer ou non
     *
     * @return
     */
    public boolean hasCompareAbility() {
        return compareAbility;
    }

    /**
     * Permet de donner le droit ou non de comparer des noeuds entre eux
     *
     * @param compareAbility
     */
    public void setCompareAbility(boolean compareAbility) {
        this.compareAbility = compareAbility;
    }

    public boolean hasMoveAbility() {
        return moveAbility;
    }

    public void setMoveAbility(boolean moveAbility) {
        this.moveAbility = moveAbility;
    }

    public boolean hasSelectAbility() {
        return selectAbility;
    }

    public void setSelectAbility(boolean selectAbility) {
        this.selectAbility = selectAbility;
    }

    public int getSelectsNumberAbility() {
        return selectsNumberAbility;
    }

    public void setSelectsNumberAbility(int selectsNumberAbility) {
        this.selectsNumberAbility = selectsNumberAbility;
    }

    public boolean hasZoomAbility() {
        return zoomAbility;
    }

    public void setZoomAbility(boolean zoomAbility) {
        this.zoomAbility = zoomAbility;
    }

    /**
     * Pour définir la classe écouteur
     *
     * @param listener
     */
    public void setListener(GraphDisplayListener listener) {
        this.listener = listener;
    }

    /**
     * Quand l'utilisateur clique sur le panel
     *
     * @param e
     */
    public void DisplayPaneMousePressed(MouseEvent e) {
        if (this.selectAbility) {
            Point mousePoint = new Point(e.getX(), e.getY());
            if (SwingUtilities.isLeftMouseButton(e) || SwingUtilities.isRightMouseButton(e)) {

                //Obtenir un noeud par rapport à sa localisation
                Noeud noeud_select = graph.getNoeud(mousePoint, DIAMETRE_NOEUD, false);

                //on vérifie que on clique bien sur un noeud
                if (noeud_select != null) {

                    //on vérifie que le type est bien affiché
                    if (TypeNoeudsVisibles.contains(noeud_select.getType())) { // && !this.selected_noeuds.contains(noeud_select)
                        this.selectNoeud(noeud_select);
                    }

                    //si il s'agit d'un clique droit on affiche le menu
                    if (SwingUtilities.isRightMouseButton(e) && compareAbility) {
                        noeud_menu.show(this, e.getX(), e.getY());
                    }

                } else {
                    //sinon il s'agit soit d'un lien soit rien 
                    this.selectNoeud(null);

                    //Priorité sur la sélection de Noeud
                    //Obtenir un lien par rapport à sa localisation
                    Lien lien_select = graph.getLien(mousePoint, DIAMETRE_NOEUD, false);

                    if (lien_select != null) {
                        if (TypeLiensVisibles.contains(lien_select.getTypeRoute())) {
                            this.selectLien(lien_select);
                        }
                    } else {
                        this.selectLien(null);
                    }
                }

                repaint();

            } else {
                //MiddleButton

                //TODO : Translation
            }
        }
    }

    /**
     * Quand la souris se déplace en ayant le clic appuyé
     *
     * @param e
     */
    public void DisplayPaneMouseDragged(MouseEvent e) {
        if (this.moveAbility) {
            if (this.selected_noeuds.getFirst() != null && SwingUtilities.isLeftMouseButton(e)) {
                Point offset_pt = new Point(e.getX() - DIAMETRE_NOEUD / 2, e.getY() - DIAMETRE_NOEUD / 2);

                //TODO : Ajuster les limiteurs dans des variables globals
                if (offset_pt.x < 5) {
                    offset_pt.x = 5;
                }
                if (offset_pt.y < 5) {
                    offset_pt.y = 5;
                }

                if (offset_pt.x > getParent().getWidth() - DIAMETRE_NOEUD - 5) {
                    offset_pt.x = getParent().getWidth() - DIAMETRE_NOEUD - 5;
                }
                if (offset_pt.y > getParent().getHeight() - DIAMETRE_NOEUD - 20) {
                    offset_pt.y = getParent().getHeight() - DIAMETRE_NOEUD - 20;
                }
                this.selected_noeuds.getFirst().setPosition(offset_pt);
                repaint();
            } else {
                //TODO : Clique droit pour déplacer le graph entier
            }
        }
    }

    /**
     * Quand l'ulisateur utilise son scroll
     *
     * @param e
     */
    public void DisplayPaneWheelMoved(MouseWheelEvent e) {
        if (this.zoomAbility) {
            //TODO : Zoom ability 
            if (e.getWheelRotation() < 0) {
                //scroll up
            } else {
                //scroll down
            }
        }
    }

    /**
     * Permet d'afficher la fenêtre des sous graphe à 1 distance du dernier
     * noeud selectionné
     *
     * @param e
     */
    public void DisplayPaneShow1Distance(ActionEvent e) {
        ArrayList<Noeud> SubNoeuds = this.selected_noeuds.getFirst().getVoisins();
        SubNoeuds.add(this.selected_noeuds.getFirst());
        Graph v_Graph = graph.getSubGraph(SubNoeuds);
        new VoisinageNoeudForm(
                (JFrame) SwingUtilities.windowForComponent(this),
                true,
                "Voisinage 1 distance de " + this.selected_noeuds.getFirst().toString(),
                v_Graph,
                v_Graph.getNoeud(this.selected_noeuds.getFirst().toString())
        );
    }

    /**
     * Permet d'afficher la fenêtre des sous graphe à 2 distance du dernier
     * noeud selectionné
     *
     * @param e
     */
    public void DisplayPaneShow2Distance(ActionEvent e) {
        ArrayList<Noeud> SubNoeuds = this.selected_noeuds.getFirst().getVoisins2Distance();
        SubNoeuds.add(this.selected_noeuds.getFirst());
        Graph v_Graph = graph.getSubGraph(SubNoeuds);
        new VoisinageNoeudForm(
                (JFrame) SwingUtilities.windowForComponent(this),
                true,
                "Voisinage 2 distance de " + this.selected_noeuds.getFirst().toString(),
                v_Graph,
                this.selected_noeuds.getFirst()
        );
    }

    //Helpers Functions
    /**
     * Donne l'angle entre deux point
     *
     * @param pt1
     * @param pt2
     * @return Un angle
     */
    private float getAngle(Point pt1, Point pt2) {
        float angle = (float) Math.toDegrees(Math.atan2(pt1.y - pt2.y, pt1.x - pt2.x));
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }

    /**
     * Donne un point aléatoire dans la zone de la fenêtre
     *
     * @return
     */
    private Point randomPoint() {
        int x = (int) (Math.random() * ((this.getWidth() - DIAMETRE_NOEUD - 10) - (DIAMETRE_NOEUD + 10)));
        int y = (int) (Math.random() * ((this.getHeight() - DIAMETRE_NOEUD - 10) - (DIAMETRE_NOEUD + 10)));
        return new Point(x, y);
    }

    /**
     * Permet de savoir si deux noeuds se superpose
     *
     * @param noeud
     * @return
     */
    private boolean isNodeOverlapping(Noeud noeud) {
        for (Noeud n : this.graph.getNoeuds()) {
            if (n != noeud) {
                if (Point.distance(n.getPosX(), n.getPosY(), noeud.getPosX(), noeud.getPosY()) <= DIAMETRE_NOEUD * 2) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Défini la position de chaque noeud aléatoire
     */
    public void calcPositions() {
        if (this.graph != null) {
            for (Noeud n : this.graph.getNoeuds()) {

                //300 représente le nombre d'essais car sinon ça peut bloquer avec une boucle while
                for (int i = 0; i < 300; i++) {
                    if (isNodeOverlapping(n) || (n.getPosX() == 0 && n.getPosY() == 0)) {
                        n.setPosition(randomPoint());
                    } else {
                        break;
                    }
                }
                /*
                while (isNodeOverlapping(n) || (n.getPosX() == 0 && n.getPosY() == 0)) {
                    n.setPosition(randomPoint());
                }
                 */
            }
        }
    }

    /**
     * Déssine les liens sur le panel
     *
     * @param g
     */
    private void paintLiens(Graphics2D g) {
        for (Lien l : getGraph().getLiens()) {
            if (this.TypeLiensVisibles.contains(l.getTypeRoute())
                    && this.TypeNoeudsVisibles.contains(l.getLieu1().getType())
                    && this.TypeNoeudsVisibles.contains(l.getLieu2().getType())) {
                if (l != this.selected_lien) {
                    GradientPaint gp = new GradientPaint(
                            l.getLieu1().getPosX(),
                            l.getLieu1().getPosY(),
                            l.getLieu1().getColor(),
                            l.getLieu2().getPosX(),
                            l.getLieu2().getPosY(),
                            l.getLieu2().getColor()
                    );
                    g.setPaint(gp);
                } else {
                    g.setColor(Color.black);
                }

            } else {
                if (this.background_visible) {
                    g.setColor(Color.gray);
                } else {
                    continue;
                }
            }
            g.drawLine(
                    l.getLieu1().getPosX() + DIAMETRE_NOEUD / 2,
                    l.getLieu1().getPosY() + DIAMETRE_NOEUD / 2,
                    l.getLieu2().getPosX() + DIAMETRE_NOEUD / 2,
                    l.getLieu2().getPosY() + DIAMETRE_NOEUD / 2
            );
        }
    }

    /**
     * Déssine les noeud sur le panel
     *
     * @param g
     */
    private void paintNoeuds(Graphics2D g) {
        for (Noeud n : getGraph().getNoeuds()) {
            if (this.TypeNoeudsVisibles.contains(n.getType())) {
                g.setColor(this.selected_noeuds.contains(n) ? Color.yellow : n.getColor());
            } else {
                if (this.background_visible) {
                    g.setColor(Color.gray);
                } else {
                    continue;
                }
            }
            g.fillOval(n.getPosX(), n.getPosY(), DIAMETRE_NOEUD, DIAMETRE_NOEUD);

            g.setColor(this.selected_noeuds.contains(n) ? Color.black : Color.black);
            g.drawOval(n.getPosX(), n.getPosY(), DIAMETRE_NOEUD, DIAMETRE_NOEUD);
            g.setColor(Color.black);

            //Affichage du type de noeud graphiquement
            g.setFont(new Font("Segoe UI", Font.BOLD, 20));
            String ntype = String.valueOf(n.getType());
            int s_size = g.getFontMetrics().stringWidth(ntype);
            g.drawString(
                    ntype,
                    n.getPosX() + (DIAMETRE_NOEUD / 2 - s_size / 2),
                    n.getPosY() + (DIAMETRE_NOEUD / 2 + s_size / 2)
            );

        }
    }

    /**
     * Dessine les détails des noeuds et des liens
     *
     * @param g
     */
    private void paintLabels(Graphics2D g) {
        //g.getFontMetrics().stringWidth(text);
        //permet de récupérer la taille d'un text

        //Choix de la font pour les noeuds
        g.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        for (Noeud n : getGraph().getNoeuds()) {

            if (this.TypeNoeudsVisibles.contains(n.getType())) {
                g.setColor(Color.black);
            } else {
                if (this.background_visible) {
                    g.setColor(Color.gray);
                } else {
                    continue;
                }
            }
            int s_size = g.getFontMetrics().stringWidth(n.getNom());
            g.drawString(
                    n.getNom(),
                    n.getPosX() + (DIAMETRE_NOEUD / 2 - s_size / 2),
                    n.getPosY() + DIAMETRE_NOEUD + 15
            );
        }

        //Choix de la font pour les labels des liens
        g.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        for (Lien l : getGraph().getLiens()) {

            if (this.TypeLiensVisibles.contains(l.getTypeRoute())
                    && this.TypeNoeudsVisibles.contains(l.getLieu1().getType())
                    && this.TypeNoeudsVisibles.contains(l.getLieu2().getType())) {
                //création du gradientPaint
                if (l != this.selected_lien) {
                    GradientPaint gp = new GradientPaint(
                            l.getLieu1().getPosX(),
                            l.getLieu1().getPosY(),
                            l.getLieu1().getColor(),
                            l.getLieu2().getPosX(),
                            l.getLieu2().getPosY(),
                            l.getLieu2().getColor()
                    );

                    g.setPaint(gp);
                } else {
                    g.setColor(Color.black);
                }

            } else {
                if (this.background_visible) {
                    g.setColor(Color.gray);
                } else {
                    continue;
                }
            }

            Point pt1 = new Point(
                    l.getLieu1().getPosX() + DIAMETRE_NOEUD / 2,
                    l.getLieu1().getPosY() + DIAMETRE_NOEUD / 2);
            Point pt2 = new Point(
                    l.getLieu2().getPosX() + DIAMETRE_NOEUD / 2,
                    l.getLieu2().getPosY() + DIAMETRE_NOEUD / 2);

            Point pt_milieu = new Point(((pt1.x + pt2.x) / 2), ((pt1.y + pt2.y) / 2));

            String nom_route = String.format("%s : %s Km", String.valueOf(l.getTypeRoute()), l.getDistance());

            //on récupère la taille du text avec une marge de 10 px
            int s_size = g.getFontMetrics().stringWidth(nom_route) + 10;

            if (Point.distance(pt1.x, pt1.y, pt2.x, pt2.y) > s_size + DIAMETRE_NOEUD) {

                //on conserve la transformation pour la remttre à celle d'origine plus tard
                AffineTransform origin_trans = g.getTransform();

                //Obtiens l'angle entre les deux points par rapport au plan cartésien de la fenetre
                float angle = getAngle(pt1, pt2);

                //permet d'écrire de text toujours vers le haut
                if (angle > 90.0 && angle < 270) {
                    angle = getAngle(pt2, pt1);
                }

                //change la direction du graphics
                g.rotate(Math.toRadians(angle), pt_milieu.x, pt_milieu.y);

                //création du rectangle contenant l'info de la route
                g.fillRoundRect(
                        pt_milieu.x - s_size / 2,
                        pt_milieu.y - 15 / 2,
                        s_size,
                        15,
                        10,
                        10
                );

                g.setColor(Color.white);

                g.drawString(nom_route, pt_milieu.x - (s_size / 2) + 5, pt_milieu.y + 5);

                //remise à l'ancien transform
                g.setTransform(origin_trans);
            }
        }
    }

    @Override
    protected final void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(this.getBackground());
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2d.setStroke(new BasicStroke(2.0f));
        if (this.graph != null) {
            paintLiens(g2d);
            paintNoeuds(g2d);
            paintLabels(g2d);
        }
        g.dispose();
    }

}
