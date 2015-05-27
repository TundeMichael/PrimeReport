package com.tundemichael.powerreporter.dao;

import com.tundemichael.powerreporter.entities.ReportColumn;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author michael.orokola
 */
@Stateless
public class ReportColumnFacade extends AbstractFacade<ReportColumn> {
    @PersistenceContext(unitName = "PowerReporterPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ReportColumnFacade() {
        super(ReportColumn.class);
    }
    
}
