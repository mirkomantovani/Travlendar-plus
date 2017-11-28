/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Travelmean;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Mirko
 */
@Local
public interface TravelmeanFacadeLocal {

    void create(Travelmean travelmean);

    void edit(Travelmean travelmean);

    void remove(Travelmean travelmean);

    Travelmean find(Object id);

    List<Travelmean> findAll();

    List<Travelmean> findRange(int[] range);

    int count();
    
}
