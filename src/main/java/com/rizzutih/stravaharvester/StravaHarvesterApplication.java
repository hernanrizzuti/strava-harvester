package com.rizzutih.stravaharvester;

import com.rizzutih.stravaharvester.model.Argument;
import com.rizzutih.stravaharvester.processor.ArgumentProcessor;
import com.rizzutih.stravaharvester.service.ActivitiesServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

@SpringBootApplication
public class StravaHarvesterApplication implements CommandLineRunner {

    final private ActivitiesServiceImpl activitiesService;
    final private ArgumentProcessor argumentProcessor;

    public StravaHarvesterApplication(final ActivitiesServiceImpl activitiesService,
                                      final ArgumentProcessor argumentProcessor) {
        this.activitiesService = activitiesService;
        this.argumentProcessor = argumentProcessor;
    }

    public static void main(String[] args) {
        SpringApplication.exit(SpringApplication.run(StravaHarvesterApplication.class, args));
    }

    @Override
    public void run(String... args) throws Exception {
        final CommandLine commandLine = new CommandLine(argumentProcessor);
        final int exitCode = commandLine.execute(args);
        if (exitCode != 0) {
            throw new IllegalArgumentException("Failed to parse options");
        }
        final Argument argument = commandLine.getExecutionResult();
        activitiesService.harvestActivities(argument);
        System.exit(0);
    }

}
