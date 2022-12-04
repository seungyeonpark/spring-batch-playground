package com.playground.springbatch.chunk;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class ChunkConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static final int chunkSize = 10;
    private static final String jobName = "chunkJob";
    private static final String stepName = "chunkStep";

    @Bean
    public Job chunkJob() {
        return jobBuilderFactory.get(jobName)
                .start(chunkStep())
                .build();
    }

    @Bean
    public Step chunkStep() {
        return stepBuilderFactory.get(stepName)
                .<String, String>chunk(chunkSize)
                .build();
    }

    @Bean
    public JpaPagingItemReader<String> chunkItemReader() {
        return new JpaPagingItemReaderBuilder<String>()
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .build();
    }

    @Bean
    public ItemProcessor<String, String> chunkProcessor() {
        return item -> "TEST " + item;
    }

    @Bean
    public JpaItemWriter<String> chunkItemWriter() {
        return new JpaItemWriterBuilder<String>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
