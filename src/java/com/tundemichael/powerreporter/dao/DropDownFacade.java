package com.tundemichael.powerreporter.dao;

import com.tundemichael.powerreporter.entities.DropDown;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author michael.orokola
 */
@Stateless
public class DropDownFacade extends AbstractFacade<DropDown> {
    @PersistenceContext(unitName = "PowerReporterPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public DropDownFacade() {
        super(DropDown.class);
    }
    
}
