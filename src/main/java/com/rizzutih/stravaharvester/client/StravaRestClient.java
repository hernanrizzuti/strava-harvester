package com.rizzutih.stravaharvester.client;

import com.rizzutih.stravaharvester.config.ApplicationConfigProperties;
import com.rizzutih.stravaharvester.web.response.strava.ActivityResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.lang.String.format;

@Component
public class StravaRestClient {

    private final RestTemplate restTemplate;

    private final ApplicationConfigProperties configProperties;

    public StravaRestClient(final RestTemplate restTemplate,
                            final ApplicationConfigProperties configProperties) {
        this.restTemplate = restTemplate;
        this.configProperties = configProperties;
    }

    public ResponseEntity<List<ActivityResponse>> getActivities(final String accessToken,
                                                                final long before,
                                                                final long after,
                                                                final int pageNumber,
                                                                final int activitiesPerPage) {

        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<String> entity = new HttpEntity<>(headers);
        final String endpoint = configProperties.getEndpoints().getActivities();
        final String uri = configProperties.getUri();

        final String url = format("%s%s?before=%s&after=%s&page=%s&per_page=%s", uri, endpoint,
                before, after, pageNumber, activitiesPerPage);

        return restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
        });
    }


}
