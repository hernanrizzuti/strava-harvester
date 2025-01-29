package com.rizzutih.stravaharvester.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Athlete {

    private long stravaId;

    private String firstname;

    private String lastname;

}
