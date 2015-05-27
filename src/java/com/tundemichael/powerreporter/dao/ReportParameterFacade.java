package com.tundemichael.powerreporter.dao;

import com.tundemichael.powerreporter.entities.ReportParameter;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author michael.orokola
 */
@Stateless
public class ReportParameterFacade extends AbstractFacade<ReportParameter> {
    @PersistenceContext(unitName = "PowerReporterPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ReportParameterFacade() {
        super(ReportParameter.class);
    }
    
}
