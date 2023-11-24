package com.rizzutih.stravaharvester.writer;

import com.rizzutih.stravaharvester.model.Activity;
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

    public void write(final List<Activity> activities,
                      final String schemaName,
                      final Path location) throws IOException {

        GenericData timeSupport = new GenericData();
        timeSupport.addLogicalTypeConversion(new TimeConversions.DateConversion());
        timeSupport.addLogicalTypeConversion(new TimeConversions.TimestampMillisConversion());
        final Schema schema = getSchema(schemaName);
        try(ParquetWriter<GenericData.Record> pw =
                AvroParquetWriter.<GenericData.Record>builder(HadoopOutputFile.fromPath(location,
                                new Configuration(true)))
                        .withSchema(schema)
                        .withWriteMode(ParquetFileWriter.Mode.OVERWRITE)
                        .withDataModel(timeSupport)
                        .withCompressionCodec(CompressionCodecName.SNAPPY)
                        .build()){

            writeParquet(schema, pw, activities);
            log.info("Successful wrote parquet files for activities to location {}", location);
        }


    }

    private Schema getSchema(String schemaName) throws IOException {
        final Schema.Parser schema = new Schema.Parser();
        return schema.parse(getClass().getResourceAsStream(format("/schema/%s", schemaName)));
    }


    private void writeParquet(final Schema schema,
                              final ParquetWriter<GenericData.Record> pw,
                              final List<Activity> activities) throws IOException {

        for (Activity activity : activities) {
            final GenericData.Record activityRecord = new GenericData.Record(schema);
            activityRecord.put("name", activity.getName());
            activityRecord.put("distance", activity.getDistance());
            activityRecord.put("total_elevation_gain", activity.getTotalElevationGain());
            activityRecord.put("sport_type", activity.getSportType().getValue());
            activityRecord.put("start_date", activity.getStartDate());
            activityRecord.put("average_speed", activity.getAverageSpeed());
            activityRecord.put("max_speed", activity.getMaxSpeed());
            activityRecord.put("average_cadence", activity.getAverageCadence());
            activityRecord.put("average_temp", activity.getAverageTemp());
            pw.write(activityRecord);
        }
    }

}
