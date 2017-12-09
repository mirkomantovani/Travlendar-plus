/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Warning;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Mirko
 */
@Stateless
public class WarningFacade extends AbstractFacade<Warning> implements WarningFacadeLocal {

    @PersistenceContext(unitName = "TravlendarPU")
    private EntityManager em;

    
    //getting instances of the Meeting entity with select meeting from Meeting meeting
    @Override
    public List getWarningsFromUID(int uid) {
    return em.createQuery(
    "SELECT w FROM Warning w WHERE w.warningPK.uid = :userid")
    .setParameter("userid", uid)
    .setMaxResults(100)
    .getResultList();
}
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WarningFacade() {
        super(Warning.class);
    }
    
}
