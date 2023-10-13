package br.com.plxprado.bachexample.config;

import br.com.plxprado.bachexample.model.TransactionPerson;
import br.com.plxprado.bachexample.service.processor.PerssonCustomTransaction;
import br.com.plxprado.bachexample.service.reader.PersonSituationReader;
import br.com.plxprado.bachexample.service.writer.PersonUpdateWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration

public class BatchConfig {


    @Bean
    public ItemReader<TransactionPerson> itemReader() throws Exception {
        return new PersonSituationReader();
    }

    @Bean
    public ItemProcessor<TransactionPerson, TransactionPerson> itemProcessor() {
        return new PerssonCustomTransaction();
    }

    @Bean
    public ItemWriter<TransactionPerson> itemWriter() throws Exception {
        return new PersonUpdateWriter();
    }


    @Bean(name = "updateFinancialSituationJob")
    public Job job(JobRepository jobRepository, @Qualifier("step1") Step step1) {

        return new JobBuilder("updateFinancialSituationJob", jobRepository)
                .start(step1)
                .build();
    }

    @Bean
    protected Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager, ItemReader<TransactionPerson> reader,
                         ItemProcessor<TransactionPerson, TransactionPerson> processor, ItemWriter<TransactionPerson> writer) {


        return new StepBuilder("step1", jobRepository).<TransactionPerson, TransactionPerson> chunk(10, transactionManager)
                .reader(reader)
                .taskExecutor(taskExecutor())
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager getTransactionManager() {
        return new JpaTransactionManager();
    }

    @Bean(name = "jobRepository")
    public JobRepository getJobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setTransactionManager(getTransactionManager());
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean(name = "jobLauncher")
    public JobLauncher getJobLauncher() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addString("jobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(getJobRepository());

        jobLauncher.afterPropertiesSet();


        return jobLauncher;
    }

    @Bean
    @Profile("local")
    public DataSourceInitializer dataSourceInitializer(@Qualifier("dataSource") final DataSource dataSource) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource("/schema-drop-postgresql.sql"));
        resourceDatabasePopulator.addScript(new ClassPathResource("/schema-postgresql.sql"));
//        resourceDatabasePopulator.addScript(new ClassPathResource("/migration-postgresql.sql"));
        resourceDatabasePopulator.addScript(new ClassPathResource("/create-person-ddl.sql"));
        resourceDatabasePopulator.addScript(new ClassPathResource("/populaperson.sql"));
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }

}
