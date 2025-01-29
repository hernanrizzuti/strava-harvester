package com.rizzutih.stravaharvester.service;

import com.rizzutih.stravaharvester.client.StravaRestClient;
import com.rizzutih.stravaharvester.exception.StravaResponseException;
import com.rizzutih.stravaharvester.factory.ActivityFactory;
import com.rizzutih.stravaharvester.factory.AthleteFactory;
import com.rizzutih.stravaharvester.web.response.strava.ActivityResponse;
import com.rizzutih.stravaharvester.web.response.strava.AthleteResponse;
import com.rizzutih.stravaharvester.writer.CustomParquetWriter;
import org.apache.hadoop.fs.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.rizzutih.stravaharvester.web.strava.restclient.builders.TestActivityResponseBuilder.testActivityResponseBuilder;
import static com.rizzutih.stravaharvester.web.strava.restclient.builders.TestAthleteResponseBuilder.testAthleteResponseBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.org.fyodor.generators.RDG.string;

@ExtendWith(MockitoExtension.class)
class ActivitiesServiceImplTest {

    @Mock
    private StravaRestClient stravaRestClient;
    @Mock
    private CustomParquetWriter customParquetWriter;
    @Mock
    private ActivityFactory activityFactory;

    @Mock
    private AthleteFactory athleteFactory;

    @Captor
    private ArgumentCaptor<List<List<ActivityResponse>>> activityResponseCaptor;

    @Captor
    private ArgumentCaptor<AthleteResponse> athleteResponseCaptor;

    private ActivitiesServiceImpl activitiesService;

    private String accessToken = string().next();

    @BeforeEach
    void setUp(){
        activitiesService = new ActivitiesServiceImpl(stravaRestClient, customParquetWriter, activityFactory, athleteFactory);
    }

    @Test
    void shouldHarvestActivities() throws StravaResponseException, IOException {

        //given
        final AthleteResponse athleteResponse = testAthleteResponseBuilder().build();
        final ActivityResponse activityResponse = testActivityResponseBuilder().build();

        final String destination = "src/test/resources/activities.parquet";
        final List<ActivityResponse> activityResponseList = Arrays.asList(activityResponse);
        final ResponseEntity<List<ActivityResponse>> responseEntity = ResponseEntity.ok(activityResponseList);

        //and

        when(stravaRestClient.getAthlete(accessToken)).thenReturn(ResponseEntity.ok(athleteResponse));
        when(stravaRestClient.getActivities(eq(accessToken), anyLong(), anyLong(), anyInt(), eq(156)))
                .thenReturn(responseEntity)
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        //when
        activitiesService.harvestActivities(accessToken, 2, destination);

        //then
        verify(athleteFactory).getInstance(athleteResponseCaptor.capture());
        final AthleteResponse actualAthleteResponse = athleteResponseCaptor.getValue();
        verify(activityFactory).getInstance(activityResponseCaptor.capture(), any(AthleteResponse.class));
        final List<List<ActivityResponse>> actualActivityResponseList = activityResponseCaptor.getValue();
        verify(customParquetWriter).writeActivity(anyList(), eq("activity_schema.avsc"), eq(new Path(destination)));
        assertEquals(activityResponse, actualActivityResponseList.get(0).get(0));
        assertEquals(athleteResponse, actualAthleteResponse);
    }

    @Test
    void shouldThrowExceptionWhenActivityResponseIsNotOK() {
        //given
        final String destination = "src/test/resources/activities.parquet";
        final AthleteResponse athleteResponse = testAthleteResponseBuilder().build();

        //and
        when(stravaRestClient.getAthlete(accessToken)).thenReturn(ResponseEntity.ok(athleteResponse));

        when(stravaRestClient.getActivities(eq(accessToken), anyLong(), anyLong(), anyInt(), eq(156)))
                .thenReturn(ResponseEntity.notFound().build());

        //when
        assertThrows(StravaResponseException.class, () -> {
            activitiesService.harvestActivities(accessToken, 2, destination);
        });
    }

    @Test
    void shouldThrowExceptionWhenAthleteResponseIsNotOK() {
        //given
        final ActivitiesServiceImpl activitiesService = new ActivitiesServiceImpl(stravaRestClient, customParquetWriter, activityFactory, athleteFactory);
        final String destination = "src/test/resources/activities.parquet";

        //and
        when(stravaRestClient.getAthlete(accessToken)).thenReturn(ResponseEntity.notFound().build());

        //when
        assertThrows(StravaResponseException.class, () -> {
            activitiesService.harvestActivities(accessToken, 2, destination);
        });
    }
}