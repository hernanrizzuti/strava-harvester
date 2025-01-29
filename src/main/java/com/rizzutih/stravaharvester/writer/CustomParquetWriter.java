package com.rizzutih.stravaharvester.writer;

import com.rizzutih.stravaharvester.model.Activity;
import com.rizzutih.stravaharvester.model.Athlete;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.data.TimeConversions;
import org.apache.avro.generic.GenericData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.hadoop.util.HadoopOutputFile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static java.lang.String.format;

@Component
@Slf4j
public class CustomParquetWriter {

    public void writeActivity(final List<Activity> activities,
                              final String schemaName,
                              final Path location) throws IOException {

        final GenericData timeSupport = getTimeSupport();
        final Schema schema = getSchema(schemaName);
        try (ParquetWriter<GenericData.Record> pw = getParquetWriter(location, timeSupport, schema)) {
            for (Activity activity : activities) {
                final GenericData.Record activityRecord = new GenericData.Record(schema);
                activityRecord.put("athlete_strava_id", activity.getAthleteStravaId());
                activityRecord.put("name", activity.getName());
                activityRecord.put("distance", activity.getDistance());
                activityRecord.put("total_elevation_gain", activity.getTotalElevationGain());
                activityRecord.put("sport_type", activity.getSportType().getValue());
                activityRecord.put("start_date", activity.getStartDate());
                activityRecord.put("average_speed", activity.getAverageSpeed());
                activityRecord.put("max_speed", activity.getMaxSpeed());
                activityRecord.put("average_cadence", activity.getAverageCadence());
                activityRecord.put("average_temp", activity.getAverageTemp());
                activityRecord.put("moving_time", activity.getMovingTime());
                activityRecord.put("moving_time_in_seconds", activity.getMovingTimeInSeconds());
                activityRecord.put("pace", activity.getPace());
                activityRecord.put("pace_in_seconds", activity.getPaceInSeconds());
                activityRecord.put("distance_unit", activity.getDistanceUnit());
                activityRecord.put("elevation_unit", activity.getElevationUnit());
                pw.write(activityRecord);
            }
            log.info("Successful wrote parquet files for activities to location {}", location);
        }
    }

    private GenericData getTimeSupport() {
        final GenericData timeSupport = new GenericData();
        timeSupport.addLogicalTypeConversion(new TimeConversions.DateConversion());
        timeSupport.addLogicalTypeConversion(new TimeConversions.TimestampMillisConversion());
        return timeSupport;
    }

    private ParquetWriter<GenericData.Record> getParquetWriter(Path location, GenericData timeSupport, Schema schema) throws IOException {
        return AvroParquetWriter.<GenericData.Record>builder(HadoopOutputFile.fromPath(location,
                        new Configuration(true)))
                .withSchema(schema)
                .withWriteMode(ParquetFileWriter.Mode.OVERWRITE)
                .withDataModel(timeSupport)
                .withCompressionCodec(CompressionCodecName.SNAPPY)
                .build();
    }

    private Schema getSchema(final String schemaName) throws IOException {
        final Schema.Parser schema = new Schema.Parser();
        return schema.parse(getClass().getResourceAsStream(format("/schema/%s", schemaName)));
    }

    public void writeAthlete(final Athlete athlete,
                             final String schemaName,
                             Path location) throws IOException {

        final GenericData timeSupport = getTimeSupport();
        final Schema schema = getSchema(schemaName);
        try (ParquetWriter<GenericData.Record> pw = getParquetWriter(location, timeSupport, schema)) {
            final GenericData.Record athleteRecord = new GenericData.Record(schema);
            athleteRecord.put("strava_id",athlete.getStravaId());
            athleteRecord.put("firstname", athlete.getFirstname());
            athleteRecord.put("lastname", athlete.getLastname());
            pw.write(athleteRecord);
        }
        log.info("Successful wrote parquet file for athlete to location {}", location);
    }
}
