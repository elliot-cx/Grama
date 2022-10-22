package controller.interfaces;

import models.Lien;
import models.Noeud;
import java.util.LinkedList;

/**
 *
 * @author Elliot
 */
/**
 * Interface gérant les évenements du GraphDisplayPane
 * @author Elliot
 */
public interface GraphDisplayListener {
    
    public void SelectedNoeudsChanged(LinkedList<Noeud> Noeuds);
    
    public void SelectedLienChanged(Lien lien);
    
}
