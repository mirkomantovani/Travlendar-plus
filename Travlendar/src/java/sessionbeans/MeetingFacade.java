/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Meeting;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Mirko
 */
@Stateless
public class MeetingFacade extends AbstractFacade<Meeting> implements MeetingFacadeLocal {

    @PersistenceContext(unitName = "TravlendarPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    
    //getting instances of the Meeting entity with select meeting from Meeting meeting
    @Override
    public List getMeetingsFromUID(int uid) {
    return em.createQuery(
    "SELECT m FROM Meeting m WHERE m.meetingPK.uid = :userid")
    .setParameter("userid", uid)
    .setMaxResults(100)
    .getResultList();
}
    
    @Override
    public List getMeetingsFromName(String partialName){
        return em.createQuery("SELECT m FROM Meeting m WHERE m.name LIKE :partname")
                .setParameter("partname", "%"+partialName+"%")
                .setMaxResults(10)
                .getResultList();
        
    }

    public MeetingFacade() {
        super(Meeting.class);
    }
    
}
