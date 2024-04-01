package edu.java.bot.configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import java.time.Duration;

public class BucketConfig {
    @Value(value = "${bucket.queryCount}")
    private int queryNumber;
    @Bean
    public Bucket createNewBucket() {
        Bandwidth limit = Bandwidth
            .builder()
            .capacity(queryNumber)
            .refillIntervally(queryNumber, Duration.ofMinutes(1))
            .build();

        return Bucket.builder().addLimit(limit).build();
    }
}
