package com.fri.rso.fririders.recommendations.services;


import com.fri.rso.fririders.recommendations.entities.*;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@RequestScoped
public class RecommendationsBean {

    private Logger logger = LogManager.getLogger(RecommendationsBean.class.getName());

    private static Client client = ClientBuilder.newClient();

    @Inject
    @DiscoverService(value="display-bookings", version = "1.0.x", environment = "dev")
    private Optional<String> bookingsUrl;

    @Inject
    @DiscoverService(value="users", version = "1.0.x", environment = "dev")
    private Optional<String> usersUrl;

    @Inject
    private AuthBean authBean;

    private String accommodationsUrl = "http://accommodations:8081/v1/accommodations";


    @CircuitBreaker(requestVolumeThreshold = 2)
    @Fallback(fallbackMethod = "getUserRecommendationsFallback")
    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    public Recommendation getUserRecommendations(String userId) {
        if (this.usersUrl.isPresent() && this.bookingsUrl.isPresent()){
            try{
                logger.info("Calling bookings service ...");
                String usrBookingsUrl = this.bookingsUrl.get() + "/v1/bookings/user/" + userId;
                logger.info("URL: " + usrBookingsUrl);

                //get users bookings
                List<Booking> bookings =
                        client.target(usrBookingsUrl)
                                .request(MediaType.APPLICATION_JSON)
                                .get((new GenericType<List<Booking>>() {
                                }));

                //get accommodations
                logger.info("Calling accommodations service.");

                List<Accommodation> allAccommodations =
                        client.target(accommodationsUrl)
                                .request(MediaType.APPLICATION_JSON)
                                .get((new GenericType<List<Accommodation>>() {
                                }));

                logger.info("Received "+allAccommodations.size()+"accommodations.");

                //new user - without previous bookings
                if(bookings == null || bookings.size() == 0){
                    //return all accommodations
                    return new Recommendation(0, userId, allAccommodations);
                }
                else{ //bookings exist
                    // get locations
                    List<String> places = new ArrayList<>();
                    for (Booking b : bookings){
                        logger.info("Calling bookings service.");
                        String bookingsAccUrl = this.bookingsUrl.get() + "/v1/bookings/" + b.getId() + "/accommodation";
                        logger.info("URL: " + bookingsUrl);
                        Accommodation a =
                                client.target(bookingsAccUrl)
                                        .request(MediaType.APPLICATION_JSON)
                                        .get((new GenericType<Accommodation>() {
                                        }));
                        if(!places.contains(a.getLocation()))
                            places.add(a.getLocation());
                    }

                    //based on previous bookings recommend accommodations
                    //get accommodations nearby
                    List<Accommodation> recommendedAccommodations = new ArrayList<>();
                    for(String place : places){
                        for(Accommodation a: allAccommodations){
                            if (place.equals(a.getLocation())){
                                recommendedAccommodations.add(a);
                            }
                        }
                    }

                    logger.info(recommendedAccommodations.size()+" recommendations found.");
                    if(recommendedAccommodations.size() == 0)
                        return new Recommendation(0, userId, allAccommodations);
                    else
                        return new Recommendation(0, userId, recommendedAccommodations);

                }
            }
            catch (WebApplicationException | ProcessingException e) {
                logger.error(e);
                throw new InternalServerErrorException(e);
            }
        }
        return null;
    }


    public Recommendation getUserRecommendationsFallback(String userId) {
        logger.warn("getUserRecommendationsFallback called.");
        Recommendation r = new Recommendation(0, "N/A", new ArrayList<>());
        return r;
    }
}
