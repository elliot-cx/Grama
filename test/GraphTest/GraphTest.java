package GraphTest;


import models.Graph;
import org.junit.Assert;
import org.junit.Test;
/**
 *
 * @author Elliot
 */
public class GraphTest {
    
    @Test
    public void GraphTest(){
        
        Graph test_graph_1 = new Graph();
        
        test_graph_1.LoadCSV("graph_map.csv");
        
        Assert.assertEquals(0, test_graph_1.getNb_erreurs());
        
        Assert.assertEquals(33,test_graph_1.getLiens().size());
        
        Assert.assertEquals(30, test_graph_1.getNoeuds().size());
        
    }
    
    
}
