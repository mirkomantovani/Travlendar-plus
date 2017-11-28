/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Meeting;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Mirko
 */
@Local
public interface MeetingFacadeLocal {

    void create(Meeting meeting);

    void edit(Meeting meeting);

    void remove(Meeting meeting);

    Meeting find(Object id);

    List<Meeting> findAll();

    List<Meeting> findRange(int[] range);

    int count();
    
}
