/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Break;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Mirko
 */
@Local
public interface BreakFacadeLocal {

    void create(Break b);

    void edit(Break b);

    void remove(Break b);

    Break find(Object id);

    List<Break> findAll();
    
    List getBreaksFromUID(int uid);

    List<Break> findRange(int[] range);

    int count();
    
}
