package com.fri.rso.fririders.recommendations.health;

import com.fri.rso.fririders.recommendations.config.ConfigProperties;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.inject.Inject;

public class RecommendationsHealthCheck implements HealthCheck{
    @Inject
    private ConfigProperties configProperties;

    @Override
    public HealthCheckResponse call() {

        if (configProperties.isHealthy()) {
            return HealthCheckResponse.named(RecommendationsHealthCheck.class.getSimpleName()).up().build();
        } else {
            return HealthCheckResponse.named(RecommendationsHealthCheck.class.getSimpleName()).down().build();
        }

    }
}
