/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Break;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Mirko
 */
@Stateless
public class BreakFacade extends AbstractFacade<Break> implements BreakFacadeLocal {

    @PersistenceContext(unitName = "TravlendarPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @Override
    public List getBreaksFromUID(int uid) {
    return em.createQuery(
    "SELECT b FROM Break b WHERE b.breakPK.uid = :userid")
    .setParameter("userid", uid)
    .setMaxResults(40)
    .getResultList();
    }

    public BreakFacade() {
        super(Break.class);
    }
    
}
