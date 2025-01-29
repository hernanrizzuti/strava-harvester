package com.rizzutih.stravaharvester.processor;

import com.rizzutih.stravaharvester.model.Argument;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

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


    @Override
    public Argument call() {
        return Argument.builder()
                .accessToken(accessToken)
                .activityYears(Integer.parseInt(activityYears))
                .harvestedActivityDestination(harvestedActivityDestination)
                .build();
    }
}
