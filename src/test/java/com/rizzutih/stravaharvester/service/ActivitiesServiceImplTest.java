package com.rizzutih.stravaharvester.service;

import com.rizzutih.stravaharvester.client.StravaRestClient;
import com.rizzutih.stravaharvester.exception.StravaResponseException;
import com.rizzutih.stravaharvester.factory.ActivityFactory;
import com.rizzutih.stravaharvester.factory.AthleteFactory;
import com.rizzutih.stravaharvester.model.Activity;
import com.rizzutih.stravaharvester.model.Argument;
import com.rizzutih.stravaharvester.model.Athlete;
import com.rizzutih.stravaharvester.web.response.strava.ActivityResponse;
import com.rizzutih.stravaharvester.web.response.strava.AthleteResponse;
import com.rizzutih.stravaharvester.web.response.strava.SportType;
import com.rizzutih.stravaharvester.writer.CustomParquetWriter;
import org.apache.hadoop.fs.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.rizzutih.stravaharvester.web.strava.restclient.builders.TestActivityBuilder.testActivityBuilder;
import static com.rizzutih.stravaharvester.web.strava.restclient.builders.TestActivityResponseBuilder.testActivityResponseBuilder;
import static com.rizzutih.stravaharvester.web.strava.restclient.builders.TestAthleteBuilder.testAthleteBuilder;
import static com.rizzutih.stravaharvester.web.strava.restclient.builders.TestAthleteResponseBuilder.testAthleteResponseBuilder;
import static java.util.Collections.singletonList;
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

    private ActivitiesServiceImpl activitiesService;

    private final String accessToken = string().next();

    private Argument argument;

    private final String baseDestination = "src/test/resources";

    private final String activityDestination = String.format("%s/%s", baseDestination, "activities.parquet");
    final String athleteDestination = String.format("%s/%s", baseDestination, "athlete.parquet");


    @BeforeEach
    void setUp() {
        activitiesService = new ActivitiesServiceImpl(stravaRestClient, customParquetWriter, activityFactory, athleteFactory);
        argument = Argument.builder()
                .harvestedActivityDestination(baseDestination)
                .activityYears(2)
                .accessToken(accessToken)
                .sportType(SportType.RUN)
                .build();
    }

    @Test
    void shouldHarvestActivities() throws StravaResponseException, IOException {

        //given
        final AthleteResponse athleteResponse = testAthleteResponseBuilder().build();
        final ActivityResponse activityResponse = testActivityResponseBuilder().build();

        final Athlete athlete = testAthleteBuilder().build();
        final Activity activity = testActivityBuilder().build();

        final List<ActivityResponse> activityResponseList = singletonList(activityResponse);
        final ResponseEntity<List<ActivityResponse>> activityResponseEntity = ResponseEntity.ok(activityResponseList);
        final ResponseEntity<AthleteResponse> athleteResponseEntity = ResponseEntity.ok(athleteResponse);

        //and
        when(stravaRestClient.getAthlete(accessToken)).thenReturn(athleteResponseEntity);
        when(stravaRestClient.getActivities(eq(accessToken), anyLong(), anyLong(), anyInt(), eq(156)))
                .thenReturn(activityResponseEntity)
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        //and
        when(activityFactory.getInstance(singletonList(activityResponseList), athleteResponse))
                .thenReturn(singletonList(activity));


        when(athleteFactory.getInstance(athleteResponse)).thenReturn(athlete);

        //when
        activitiesService.harvestActivities(argument);

        //then
        verify(customParquetWriter).writeAthlete(any(Athlete.class), eq("athlete_schema.avsc"), eq(new Path(athleteDestination)));
        verify(customParquetWriter).writeActivity(anyList(), eq("activity_schema.avsc"), eq(new Path(activityDestination)));
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
            activitiesService.harvestActivities(argument);
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
            activitiesService.harvestActivities(argument);
        });
    }
}