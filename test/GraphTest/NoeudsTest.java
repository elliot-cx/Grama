package GraphTest;

import models.Noeud;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Elliot
 */
public class NoeudsTest {
    
    @Test
    public void NoeudTest(){
        Noeud nv_test_1 = new Noeud('V',"Ville Test 1");
        Noeud nr_test_1 = new Noeud('R',"Ville Test 1");
        
        Assert.assertNotEquals(nv_test_1, nr_test_1);
        
        Noeud nv_test_2 = new Noeud('V',"Ville Test 1");
        
        Assert.assertEquals(nv_test_1,nv_test_2);
        
        Noeud nr_test_2 = new Noeud('R',"Restaurant Test");
        
        Assert.assertNotEquals(nr_test_2, nr_test_1); 
    }
    
}
