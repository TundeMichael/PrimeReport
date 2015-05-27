package com.tundemichael.powerreporter.dao;

import com.tundemichael.powerreporter.entities.UserProperty;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author michael.orokola
 */
@Stateless
public class UserPropertyFacade extends AbstractFacade<UserProperty> {
    @PersistenceContext(unitName = "PowerReporterPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public UserPropertyFacade() {
        super(UserProperty.class);
    }
    
}
