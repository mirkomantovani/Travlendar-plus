/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Warning;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Mirko
 */
@Local
public interface WarningFacadeLocal {

    void create(Warning warning);

    void edit(Warning warning);

    void remove(Warning warning);

    Warning find(Object id);

    List<Warning> findAll();

    List<Warning> findRange(int[] range);
    
    public List getWarningsFromUID(int uid);

    int count();
    
}
