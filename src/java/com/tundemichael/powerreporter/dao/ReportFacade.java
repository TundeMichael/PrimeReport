package com.tundemichael.powerreporter.dao;

import com.tundemichael.powerreporter.entities.Report;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author michael.orokola
 */
@Stateless
public class ReportFacade extends AbstractFacade<Report> {
    @PersistenceContext(unitName = "PowerReporterPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ReportFacade() {
        super(Report.class);
    }
    
}
