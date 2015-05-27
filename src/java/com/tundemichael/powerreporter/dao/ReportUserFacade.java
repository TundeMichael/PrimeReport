package com.tundemichael.powerreporter.dao;

import com.tundemichael.powerreporter.entities.ReportUser;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author michael.orokola
 */
@Stateless
public class ReportUserFacade extends AbstractFacade<ReportUser> {
    @PersistenceContext(unitName = "PowerReporterPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ReportUserFacade() {
        super(ReportUser.class);
    }
    
}
