package com.rizzutih.stravaharvester.processor;

import com.rizzutih.stravaharvester.model.Argument;
import com.rizzutih.stravaharvester.web.response.strava.SportType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.org.fyodor.generators.RDG.integer;
import static uk.org.fyodor.generators.RDG.string;

class ArgumentProcessorTest {

    private ArgumentProcessor processor;
    private CommandLine commandLine;

    @BeforeEach
    void setUp() {
        processor = new ArgumentProcessor();
        commandLine = new CommandLine(processor);
    }

    @Test
    void shouldParseArgs() {
        final String accessToken = string().next();
        final String activityYears = integer().next().toString();
        final String harvestedDestination = string().next();
        final String sportType = "Run";

        final String[] args = {format("--accessToken=%s", accessToken),
                format("--harvestedActivityDestination=%s", harvestedDestination),
                format("--activityYears=%s", activityYears),
                format("--sportType=%s", sportType)
        };

        final int exitCode = commandLine.execute(args);
        final Argument argument = commandLine.getExecutionResult();
        assertEquals(0, exitCode);
        assertEquals(accessToken, argument.getAccessToken());
        assertEquals(Integer.parseInt(activityYears), argument.getActivityYears());
        assertEquals(harvestedDestination, argument.getHarvestedActivityDestination());
        assertEquals(SportType.valueOf(sportType.toUpperCase()), argument.getSportType());
    }

    @Test
    void shouldFailToParseArgs() {
        final String accessToken = string().next();
        final String activityYears = integer().next().toString();
        final String harvestedDestination = string().next();

        final String[] args = {format("--accessTokens=%s", accessToken),
                format("--harvestDest=%s", harvestedDestination),
                format("--activityYears=%s", activityYears)
        };

        final int exitCode = commandLine.execute(args);
        assertEquals(2, exitCode);
    }
}