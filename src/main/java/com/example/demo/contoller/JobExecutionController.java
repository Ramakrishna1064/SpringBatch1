package com.example.demo.contoller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobExecutionController {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	@Qualifier("demoJob")
	Job demoJob;

	@Autowired
	@Qualifier("exportUserJo")
	Job exportUserJob;

	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(15);
		taskExecutor.setMaxPoolSize(20);
		taskExecutor.setQueueCapacity(30);
		return taskExecutor;
	}

	@Bean
	public JobLauncher jobLauncher(ThreadPoolTaskExecutor taskExecutor, JobRepository jobRepository) {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setTaskExecutor(taskExecutor);
		jobLauncher.setJobRepository(jobRepository);
		return jobLauncher;
	}

	@Scheduled(cron = "*/10 * * * * *")
	public void run1() {
		Map<String, JobParameter> confMap = new HashMap<>();
		confMap.put("time", new JobParameter(System.currentTimeMillis()));
		JobParameters jobParameters = new JobParameters(confMap);
		try {
			jobLauncher.run(demoJob, jobParameters);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	@Scheduled(cron = "*/20 * * * * *")
	public void run2() {
		Map<String, JobParameter> confMap = new HashMap<>();
		confMap.put("time", new JobParameter(System.currentTimeMillis()));
		JobParameters jobParameters = new JobParameters(confMap);
		try {
			jobLauncher.run(exportUserJob, jobParameters);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
