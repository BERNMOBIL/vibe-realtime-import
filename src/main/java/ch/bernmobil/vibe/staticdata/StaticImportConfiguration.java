package ch.bernmobil.vibe.staticdata;

import ch.bernmobil.vibe.staticdata.entity.sync.AreaMapper;
import ch.bernmobil.vibe.staticdata.entity.sync.CalendarDateMapper;
import ch.bernmobil.vibe.staticdata.entity.sync.JourneyMapper;
import ch.bernmobil.vibe.staticdata.entity.sync.RouteMapper;
import ch.bernmobil.vibe.staticdata.entity.sync.StopMapper;
import ch.bernmobil.vibe.staticdata.importer.AreaImport;
import ch.bernmobil.vibe.staticdata.importer.CalendarDateImport;
import ch.bernmobil.vibe.staticdata.importer.Import;
import ch.bernmobil.vibe.staticdata.importer.RouteImport;
import ch.bernmobil.vibe.staticdata.importer.StopImport;
import ch.bernmobil.vibe.staticdata.importer.StopTimeImport;
import ch.bernmobil.vibe.staticdata.importer.TripImport;
import ch.bernmobil.vibe.staticdata.processor.AreaProcessor;
import ch.bernmobil.vibe.staticdata.processor.CalendarDateProcessor;
import ch.bernmobil.vibe.staticdata.processor.JourneyProcessor;
import ch.bernmobil.vibe.staticdata.processor.RouteProcessor;
import ch.bernmobil.vibe.staticdata.processor.ScheduleProcessor;
import ch.bernmobil.vibe.staticdata.processor.StopProcessor;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.stereotype.Component;

@Component
class HibernateSessionFactory {
    @Bean
    public HibernateJpaSessionFactoryBean sessionFactory() {
        return new HibernateJpaSessionFactoryBean();
    }
}

@Configuration
@EnableBatchProcessing
public class StaticImportConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SessionFactory sessionFactory;

    @Autowired
    public StaticImportConfiguration(JobBuilderFactory jobBuilderFactory,
        StepBuilderFactory stepBuilderFactory, SessionFactory sessionFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.sessionFactory = sessionFactory;
    }

    @Bean
    public Job importStaticJob(){
        return jobBuilderFactory.get("importStaticJob")
                .incrementer(new RunIdIncrementer())
                .flow(areaImportStep())
                .next(stopImportStep())
                .next(routeImportStep())
                .next(journeyImportStep())
                .next(calendarDateImportStep())
                .next(scheduleImportStep())
                .next(areaMapperStep())
                .next(calendarDateMapperStep())
                .next(journeyMapperStep())
                .next(routeMapperStep())
                .next(stopMapperStep())
                .end()
                .build();
    }



    @Bean
    public Step areaMapperStep() {
        return createSaveMappingStep("areaMapper", 10, new AreaMapper.BatchReader());
    }

    @Bean
    public Step calendarDateMapperStep() {
        return createSaveMappingStep("calendarMapper", 10, new CalendarDateMapper.BatchReader());
    }

    @Bean
    public Step journeyMapperStep() {
        return createSaveMappingStep("journeyMapper", 10, new JourneyMapper.BatchReader());
    }

    @Bean
    public Step routeMapperStep() {
        return createSaveMappingStep("routeMapper", 10, new RouteMapper.BatchReader());
    }

    @Bean
    public Step stopMapperStep() {
        return createSaveMappingStep("stopMapper", 10, new StopMapper.BatchReader());
    }

    @Bean
    public Step areaImportStep() {
        return createStepBuilder("areaImportStep", 10, new AreaImport(sessionFactory), new AreaProcessor());
    }
    @Bean
    public Step stopImportStep() {
        return createStepBuilder("stopImportStep", 10, new StopImport(sessionFactory), new StopProcessor());
    }

    @Bean
    public Step routeImportStep() {
        return createStepBuilder("routeImportStep", 10, new RouteImport(sessionFactory), new RouteProcessor());
    }

    @Bean
    public Step calendarDateImportStep() {
        return createStepBuilder("calendarDateImportStep", 10, new CalendarDateImport(sessionFactory), new CalendarDateProcessor());
    }

    @Bean
    public Step journeyImportStep() {
        return createStepBuilder("journeyImportStep", 10, new TripImport(sessionFactory), new JourneyProcessor());
    }

    @Bean
    public Step scheduleImportStep() {
        return createStepBuilder("scheduleImportStep", 100, new StopTimeImport(sessionFactory), new ScheduleProcessor());
    }

    public <TIn, TOut> Step createStepBuilder(String name, int chunkSize, Import<TIn> importer, ItemProcessor<TIn, TOut> processor) {
        return stepBuilderFactory.get(name)
            .<TIn, TOut>chunk(chunkSize)
            .reader(importer.reader())
            .processor(processor)
            .writer(importer.writer())
            .build();
    }

    public <T> Step createSaveMappingStep(String name, int chunkSize, ItemReader<T> reader) {
        HibernateItemWriter<T> writer = new HibernateItemWriter<>();
        writer.setSessionFactory(sessionFactory);

        return stepBuilderFactory.get(name)
            .<T, T>chunk(chunkSize)
            .reader(reader)
            .writer(writer)
            .build();
    }
}
