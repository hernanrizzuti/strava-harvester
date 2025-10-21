package com.rizzutih.stravaharvester.processor;

import com.rizzutih.stravaharvester.model.Argument;
import com.rizzutih.stravaharvester.web.response.strava.SportType;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.Arrays;
import java.util.concurrent.Callable;

@Command
@Component
public class ArgumentProcessor implements Callable<Argument> {

    @Option(required = true, names = "--accessToken")
    private String accessToken;

    @Option(required = true, names = "--activityYears")
    private String activityYears;

    @Option(required = true, names = "--harvestedActivityDestination")
    private String harvestedActivityDestination;

    @Option(names = "--sportType")
    private String sportType;


    @Override
    public Argument call() {
        return Argument.builder()
                .accessToken(accessToken)
                .activityYears(Integer.parseInt(activityYears))
                .harvestedActivityDestination(harvestedActivityDestination)
                .sportType(matchSportType(sportType))
                .build();
    }

    private SportType matchSportType(final String sportType) {
        return Arrays.stream(SportType.values())
                .filter(x -> sportType.equals(x.getValue()))
                .findFirst()
                .orElse(SportType.ALL);
    }
}
