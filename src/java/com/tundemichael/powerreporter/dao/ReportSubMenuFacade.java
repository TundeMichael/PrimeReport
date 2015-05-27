package com.tundemichael.powerreporter.dao;

import com.tundemichael.powerreporter.entities.ReportSubMenu;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author michael.orokola
 */
@Stateless
public class ReportSubMenuFacade extends AbstractFacade<ReportSubMenu> {
    @PersistenceContext(unitName = "PowerReporterPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ReportSubMenuFacade() {
        super(ReportSubMenu.class);
    }
    
}
