/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Preferences;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Mirko
 */
@Stateless
public class PreferencesFacade extends AbstractFacade<Preferences> implements PreferencesFacadeLocal {

    @PersistenceContext(unitName = "TravlendarPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PreferencesFacade() {
        super(Preferences.class);
    }
    
}
