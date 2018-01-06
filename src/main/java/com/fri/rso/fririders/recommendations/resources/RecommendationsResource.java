package com.fri.rso.fririders.recommendations.resources;
import com.fri.rso.fririders.recommendations.entities.User;
import com.fri.rso.fririders.recommendations.services.RecommendationsBean;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("recommendations")
public class RecommendationsResource {

    private Logger logger = LogManager.getLogger( RecommendationsResource.class.getName() );

    @Inject
    private RecommendationsBean recommendationsBean;


    @GET
    public Response getAllRecommendations() {
        logger.info("REST CALL: getAllRecommendations");
        return null;
    }


    @GET
    @Path("/{userId}")
    public Response getUserRecommendations(@PathParam("userId") int mId) {
        logger.info("REST CALL: getUserRecommendations.");
        return null;
    }
}
