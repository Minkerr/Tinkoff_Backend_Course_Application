package edu.java.scrapper.configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import java.time.Duration;

public class BucketConfig {
    @Value(value = "${bucket.queryNumber}")
    private int queryCount;
    @Bean
    public Bucket createNewBucket() {
        Bandwidth limit = Bandwidth
            .builder()
            .capacity(queryCount)
            .refillIntervally(queryCount, Duration.ofMinutes(1))
            .build();

        return Bucket.builder().addLimit(limit).build();
    }
}
