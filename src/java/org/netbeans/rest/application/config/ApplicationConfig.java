package org.netbeans.rest.application.config;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author michael.orokola
 */
@javax.ws.rs.ApplicationPath("api/v1")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.tundemichael.powerreporter.service.ConnectionProfileFacadeREST.class);
        resources.add(com.tundemichael.powerreporter.service.ReportFacadeREST.class);
        resources.add(com.tundemichael.powerreporter.service.ReportUserFacadeREST.class);
        resources.add(com.tundemichael.powerreporter.service.UserGroupFacadeREST.class);
        resources.add(org.netbeans.rest.application.config.NewCrossOriginResourceSharingFilter.class);
    }
    
}
