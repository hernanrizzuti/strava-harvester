package com.rizzutih.stravaharvester.web.strava.restclient;

import com.rizzutih.stravaharvester.client.StravaRestClient;
import com.rizzutih.stravaharvester.config.ApplicationConfigProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;

import static com.rizzutih.stravaharvester.web.strava.restclient.builders.TestApplicationConfigPropertiesBuilder.testApplicationConfigProperties;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StravaRestClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> httpEntityCaptor;

    @Captor
    private ArgumentCaptor<String> urlCaptor;

    @Test
    void shouldCallStravaApi() {
        final ApplicationConfigProperties configProperties = testApplicationConfigProperties().setProperties();
        final StravaRestClient client = new StravaRestClient(restTemplate, configProperties);
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime eightYearsAgo = now.minusYears(8);
        final int pageNumber = 1;
        final int activitiesPerPage = 156;
        final long epochNow = now.toInstant().getEpochSecond();
        final long epochEightYearsAgo = eightYearsAgo.toInstant().getEpochSecond();
        final String accessToken = "8762b4b5c5922676c55827fabd21a4c252b7eafa";
        final String url = format("%s%s?before=%s&after=%s&page=%s&per_page=%s", configProperties.getUri(),
                configProperties.getEndpoints().getActivities(), epochNow, epochEightYearsAgo, pageNumber, activitiesPerPage);
        client.getActivities(accessToken, epochNow, epochEightYearsAgo, pageNumber, activitiesPerPage);
        verify(restTemplate).exchange(urlCaptor.capture(), eq(HttpMethod.GET), httpEntityCaptor.capture(), any(ParameterizedTypeReference.class));
        assertEquals(url, urlCaptor.getValue());
        HttpHeaders headers = httpEntityCaptor.getValue().getHeaders();
        assertEquals(MediaType.APPLICATION_JSON, headers.getAccept().get(0));
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
        assertEquals("Bearer "+ accessToken, headers.get("Authorization").get(0));
    }
}