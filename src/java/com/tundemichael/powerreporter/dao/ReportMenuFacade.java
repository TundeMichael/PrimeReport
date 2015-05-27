package com.tundemichael.powerreporter.dao;

import com.tundemichael.powerreporter.entities.ReportMenu;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author michael.orokola
 */
@Stateless
public class ReportMenuFacade extends AbstractFacade<ReportMenu> {
    @PersistenceContext(unitName = "PowerReporterPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ReportMenuFacade() {
        super(ReportMenu.class);
    }
    
}
