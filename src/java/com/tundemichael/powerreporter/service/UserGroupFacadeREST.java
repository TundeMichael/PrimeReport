package com.tundemichael.powerreporter.service;

import com.tundemichael.powerreporter.entities.UserGroup;
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
@Path("usergroups")
public class UserGroupFacadeREST extends AbstractFacade<UserGroup> {
    @PersistenceContext(unitName = "PowerReporterPU")
    private EntityManager em;

    public UserGroupFacadeREST() {
        super(UserGroup.class);
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public UserGroup find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces("application/json")
    public List<UserGroup> findAll() {
        return super.findAll();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
