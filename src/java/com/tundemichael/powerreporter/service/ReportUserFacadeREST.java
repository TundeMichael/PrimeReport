
package com.tundemichael.powerreporter.service;

import com.tundemichael.powerreporter.entities.ReportUser;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author michael.orokola
 */
@Stateless
@Path("reportusers")
public class ReportUserFacadeREST extends AbstractFacade<ReportUser> {
    @PersistenceContext(unitName = "PowerReporterPU")
    private EntityManager em;

    public ReportUserFacadeREST() {
        super(ReportUser.class);
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public ReportUser find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces("application/json")
    public List<ReportUser> findAll() {
        return super.findAll();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
