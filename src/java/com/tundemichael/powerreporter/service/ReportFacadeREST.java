
package com.tundemichael.powerreporter.service;

import com.google.gson.JsonArray;
import com.tundemichael.powerreporter.dao.SetupDAO;
import com.tundemichael.powerreporter.entities.ConnectionProfile;
import com.tundemichael.powerreporter.entities.Report;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author michael.orokola
 */
@Stateless
@Path("reports")
public class ReportFacadeREST extends AbstractFacade<Report> {
    @PersistenceContext(unitName = "PowerReporterPU")
    private EntityManager em;
    SetupDAO pdao;

    public ReportFacadeREST() {
        super(Report.class);
    }

    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public void edit(@PathParam("id") Long id, Report entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Report find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces("application/json")
    public List<Report> findAll() {
        return super.findAll();
    }
    
    @GET
    @Path("data/{id}/{sql}")
    @Produces("application/json")
    public Response getReportData(@PathParam("id") Long id , @PathParam("sql") String sql){
        
        try {
            String reportStr;
            Report rep = super.find(id);
            ConnectionProfile profile = rep.getConnectionProfile();
            String dbType = "";
            
            //sql = rep.getReportQuery();
            
            if ("mysql".equals(profile.getDatabaseType())) {
                dbType = "jdbc:mysql://";
            }
            
            if ("sybase".equals(profile.getDatabaseType())) {
                dbType = "jdbc:sybase:Tds:";
            }
            
            pdao = new SetupDAO(dbType + profile.getIpAdd() + ":" + profile.getPortNo() + "/"
                    + profile.getDatabaseName(), profile.getUsername(), profile.getPassword(),
                    profile.getDatabaseName());
            
            pdao.open();
            JsonArray report = pdao.getReport(sql);
            
            if (report != null) {
                reportStr = report.toString();
            } else {
                reportStr = pdao.getMessage("404", "No record found");
            }
            
            return Response.status(200).entity(reportStr).build();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ReportFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(500).entity(pdao.getMessage("500", ex.getMessage())).build();
        } catch(Exception ex){
            return Response.status(500).entity(pdao.getMessage("500", ex.getMessage())).build();
        }
    
    }
    
    
    @GET
    @Path("datastr/{id}/{sql}")
    @Produces("application/json")
    public String getReportDataStr(@PathParam("id") Long id , @PathParam("sql") String sql){
        
        try {
            String reportStr;
            Report rep = super.find(id);
            ConnectionProfile profile = rep.getConnectionProfile();
            String dbType = "";
            
            //sql = rep.getReportQuery();
            
            if ("mysql".equals(profile.getDatabaseType())) {
                dbType = "jdbc:mysql://";
            }
            
            if ("sybase".equals(profile.getDatabaseType())) {
                dbType = "jdbc:sybase:Tds:";
            }
            
            pdao = new SetupDAO(dbType + profile.getIpAdd() + ":" + profile.getPortNo() + "/"
                    + profile.getDatabaseName(), profile.getUsername(), profile.getPassword(),
                    profile.getDatabaseName());
            
            pdao.open();
            JsonArray report = pdao.getReport(sql);
            
            if (report != null) {
                reportStr = report.toString();
            } else {
                reportStr = pdao.getMessage("404", "No record found");
            }
            
            return ""; //Response.status(200).entity(reportStr).build();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ReportFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return ""; //Response.status(500).entity(pdao.getMessage("500", ex.getMessage())).build();
        } catch(Exception ex){
            return ""; //Response.status(500).entity(pdao.getMessage("500", ex.getMessage())).build();
        }
        
    }

    @GET
    @Path("{from}/{to}")
    @Produces("application/json")
    public List<Report> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
