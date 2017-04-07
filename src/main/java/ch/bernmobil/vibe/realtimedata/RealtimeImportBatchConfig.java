package ch.bernmobil.vibe.realtimedata;

import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdate;
import ch.bernmobil.vibe.realtimedata.repository.RealtimeUpdateRepository;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class RealtimeImportBatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource batchDataSource;
    private final DataSource mapperDataSource;
    private final DataSource staticDataSource;


    @Autowired
    public RealtimeImportBatchConfig(
        JobBuilderFactory jobBuilderFactory,
        StepBuilderFactory stepBuilderFactory,
        DataSource batchDataSource,
        @Qualifier("MapperDataSource") DataSource mapperDataSource,
        @Qualifier("StaticDataSource") DataSource staticDataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.batchDataSource = batchDataSource;
        this.mapperDataSource = mapperDataSource;
        this.staticDataSource = staticDataSource;
    }

    @Bean
    public Job job() {
        return jobBuilderFactory
            .get("realtimeImportJob")
            .flow(realtimeUpdatesImport())
            .end()
            .build();
    }

    private Step realtimeUpdatesImport() {


        return stepBuilderFactory
            .get("realtimeImporterStep")
            .<ScheduleUpdate, ScheduleUpdate>chunk(10)
            .reader(getReader())
            //.processor(new ScheduleUpdateProcessor())
            .writer(getWriter())
            .build();
    }

    private JdbcBatchItemWriter<ScheduleUpdate> getWriter(){
        ItemPreparedStatementSetter<ScheduleUpdate> setter = ((item, ps) -> {
            ps.setTime(1, item.getActualArrival());
            ps.setTime(2, item.getActualDeparture());
            ps.setLong(3, item.getSchedule());
        });

        JdbcBatchItemWriter<ScheduleUpdate> writer = new JdbcBatchItemWriter<>();
        writer.setSql("INSERT INTO schedule_update(actual_arrival, actual_departure, schedule) VALUES(?, ?, ?)");
        writer.setItemPreparedStatementSetter(setter);
        writer.setDataSource(staticDataSource);
        return writer;
    }

    private ItemReader<ScheduleUpdate> getReader() {
        return new ItemReader<ScheduleUpdate>() {
            ListItemReader<ScheduleUpdate> reader;

            @Override
            public ScheduleUpdate read() throws Exception{
                if(reader == null) {
                    RealtimeUpdateRepository repository = new RealtimeUpdateRepository(staticDataSource, mapperDataSource);
                    reader = new ListItemReader<>(repository.getScheduleUpdates());
                }

                return reader.read();
            }
        };
    }
}
