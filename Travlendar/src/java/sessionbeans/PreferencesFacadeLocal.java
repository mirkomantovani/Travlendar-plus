/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Preferences;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Mirko
 */
@Local
public interface PreferencesFacadeLocal {
    

    void create(Preferences preferences);

    void edit(Preferences preferences);

    void remove(Preferences preferences);

    Preferences find(Object id);

    List<Preferences> findAll();

    List<Preferences> findRange(int[] range);

    int count();
    
}
