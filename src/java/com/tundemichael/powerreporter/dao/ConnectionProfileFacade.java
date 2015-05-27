
package com.tundemichael.powerreporter.dao;

import com.tundemichael.powerreporter.entities.ConnectionProfile;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author michael.orokola
 */
@Stateless
public class ConnectionProfileFacade extends AbstractFacade<ConnectionProfile> {
    @PersistenceContext(unitName = "PowerReporterPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ConnectionProfileFacade() {
        super(ConnectionProfile.class);
    }
    
}
