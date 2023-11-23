package com.rizzutih.stravaharvester.web.strava.response;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SportType {

    ALPINESKI("AlpineSki"),
    BACKCOUNTRYSKI("BackcountrySki"),
    BADMINTON("Badminton"),
    CANOEING("Canoeing"),
    CROSSFIT("Crossfit"),
    EBIKERIDE("EBikeRide"),
    ELLIPTICAL("Elliptical"),
    EMOUNTAINBIKERIDE("EMountainBikeRide"),
    GOLF("Golf"),
    GRAVELRIDE("GravelRide"),
    HANDCYCLE("Handcycle"),
    HIGHINTENSITYINTERVALTRAINING("HighIntensityIntervalTraining"),
    HIKE("Hike"),
    ICESKATE("IceSkate"),
    INLINESKATE("InlineSkate"),
    KAYAKING("Kayaking"),
    KITESURF("Kitesurf"),
    MOUNTAINBIKERIDE("MountainBikeRide"),
    NORDICSKI("NordicSki"),
    PICKLEBALL("Pickleball"),
    PILATES("Pilates"),
    RACQUETBALL("Racquetball"),
    RIDE("Ride"),
    ROCKCLIMBING("RockClimbing"),
    ROLLERSKI("RollerSki"),
    ROWING("Rowing"),
    RUN("Run"),
    SAIL("Sail"),
    SKATEBOARD("Skateboard"),
    SNOWBOARD("Snowboard"),
    SNOWSHOE("Snowshoe"),
    SOCCER("Soccer"),
    SQUASH("Squash"),
    STAIRSTEPPER("StairStepper"),
    STANDUPPADDLING("StandUpPaddling"),
    SURFING("Surfing"),
    SWIM("Swim"),
    TABLETENNIS("TableTennis"),
    TENNIS("Tennis"),
    TRAILRUN("TrailRun"),
    VELOMOBILE("Velomobile"),
    VIRTUALRIDE("VirtualRide"),
    VIRTUALROW("VirtualRow"),
    VIRTUALRUN("VirtualRun"),
    WALK("Walk"),
    WEIGHTTRAINING("WeightTraining"),
    WHEELCHAIR("Wheelchair"),
    WINDSURF("Windsurf"),
    WORKOUT("Workout"),
    YOGA("Yoga");

    private final String value;

    SportType(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
