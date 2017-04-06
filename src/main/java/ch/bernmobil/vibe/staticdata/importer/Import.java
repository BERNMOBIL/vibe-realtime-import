package ch.bernmobil.vibe.staticdata.importer;

import org.hibernate.SessionFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;

public abstract class Import<TIn> {
    private SessionFactory sessionFactory;
    private String[] fieldNames;
    private String filePath;
    private FieldSetMapper<TIn> fieldSetMapper;

    public Import(SessionFactory sessionFactory, String[] fieldNames, String filePath, FieldSetMapper<TIn> fieldSetMapper) {
        this.sessionFactory = sessionFactory;
        this.fieldNames = fieldNames;
        this.filePath = filePath;
        this.fieldSetMapper = fieldSetMapper;
    }

    public Import(SessionFactory sessionFactory, String[] fieldNames, String filePath){
        this(sessionFactory, fieldNames, filePath, new BeanWrapperFieldSetMapper<>());
    }

    @Bean
    public FlatFileItemReader<TIn> reader(){
        FlatFileItemReader<TIn> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<TIn>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(fieldNames);
            }});
            setFieldSetMapper(fieldSetMapper);
        }});
        return reader;
    }

    @Bean
    public <TOut> HibernateItemWriter<TOut> writer() {
        HibernateItemWriter<TOut> writer = new HibernateItemWriter<>();
        writer.setSessionFactory(sessionFactory);
        return writer;
    }
}
