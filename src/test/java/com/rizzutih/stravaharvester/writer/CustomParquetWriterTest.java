package com.rizzutih.stravaharvester.writer;

import com.rizzutih.stravaharvester.model.Activity;
import com.rizzutih.stravaharvester.web.strava.restclient.builders.TestActivityBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.InputFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomParquetWriterTest {

    String testFilesDir = "src/test/resources/test-files/";

    @Test
    void shouldWriteParquet() throws IOException {
        final CustomParquetWriter customParquetWriter = new CustomParquetWriter();
        final Activity activity = TestActivityBuilder.testActivityBuilder().build();
        final Activity activity2 = TestActivityBuilder.testActivityBuilder().build();
        final Activity activity3 = TestActivityBuilder.testActivityBuilder().build();
        final List<Activity> activityList = Arrays.asList(activity, activity2, activity3);
        Path path = new Path(testFilesDir + "sample.parquet");
        customParquetWriter.write(activityList, "activity_schema.avsc", path);

        List<GenericData.Record> actualRecords = readParquet(HadoopInputFile.fromPath(path, new Configuration()));
        GenericData.Record actualRecord = actualRecords.get(0);
        assertEquals(activity.getName(), actualRecord.get("name").toString());
        assertEquals(activity.getDistance(), actualRecord.get("distance"));
        assertEquals(activity.getTotalElevationGain(), actualRecord.get("total_elevation_gain"));
        assertEquals(activity.getSportType().getValue(), actualRecord.get("sport_type").toString());
        assertEquals(activity.getStartDate().toEpochMilli(), actualRecord.get("start_date"));
        assertEquals(activity.getAverageSpeed(), actualRecord.get("average_speed"));
        assertEquals(activity.getMaxSpeed(), actualRecord.get("max_speed"));
        assertEquals(activity.getAverageCadence(), actualRecord.get("average_cadence"));
        assertEquals(activity.getAverageTemp(), actualRecord.get("average_temp"));
    }

    private List<GenericData.Record> readParquet(final InputFile filePath) throws IOException {
        try (ParquetReader<GenericData.Record> reader = AvroParquetReader.<GenericData.Record>builder(filePath)
                .withConf(new Configuration()).build()) {
            GenericData.Record record;
            List<GenericData.Record> records = new ArrayList<>();
            while ((record = reader.read()) != null) {
                records.add(record);
            }
            return records;
        }
    }

    @AfterEach
    void cleanUp() {
        FileSystemUtils.deleteRecursively(new File(testFilesDir));
    }

}