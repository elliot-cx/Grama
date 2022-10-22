package GraphTest;

import models.Lien;
import models.Noeud;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Elliot
 */
public class LiensTest {
    
    @Test
    public void LiensTest(){
        Noeud test_noeud_1 = new Noeud('V',"Test");
        Noeud test_noeud_2 = new Noeud('R',"Restaurant");
    
        Lien test_lien_1 = new Lien(test_noeud_1,test_noeud_2,'D',50);
        
        test_noeud_1.addLien(test_lien_1);
        test_noeud_2.addLien(test_lien_1);
        
        Assert.assertEquals(test_noeud_2,test_lien_1.getVoisin(test_noeud_1));
        Assert.assertEquals(test_noeud_1,test_lien_1.getVoisin(test_noeud_2));
        
        Assert.assertArrayEquals(new Noeud[]{test_noeud_1}, test_noeud_2.getVoisins().toArray());
        Assert.assertArrayEquals(new Noeud[]{test_noeud_2}, test_noeud_1.getVoisins().toArray());
        
        Assert.assertArrayEquals(new Noeud[]{test_noeud_1}, test_noeud_2.getVoisins('V').toArray());
        Assert.assertArrayEquals(new Noeud[]{}, test_noeud_2.getVoisins('R').toArray());
        Assert.assertArrayEquals(new Noeud[]{}, test_noeud_2.getVoisins('L').toArray());
    }
    
   
}
