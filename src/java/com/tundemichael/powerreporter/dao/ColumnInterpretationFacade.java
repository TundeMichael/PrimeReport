package com.tundemichael.powerreporter.dao;

import com.tundemichael.powerreporter.entities.ColumnInterpretation;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author michael.orokola
 */
@Stateless
public class ColumnInterpretationFacade extends AbstractFacade<ColumnInterpretation> {
    @PersistenceContext(unitName = "PowerReporterPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ColumnInterpretationFacade() {
        super(ColumnInterpretation.class);
    }
    
}
