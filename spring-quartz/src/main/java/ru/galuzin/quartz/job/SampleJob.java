package ru.galuzin.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.galuzin.quartz.service.SampleJobService;

@Component
public class SampleJob implements Job {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SampleJobService jobService;//autowired by AutoWiringSpringBeanJobFactory

    public void execute(JobExecutionContext context) throws JobExecutionException {

        logger.info("Job ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

        jobService.executeSampleJob();

        logger.info("Next job scheduled @ {}", context.getNextFireTime());
    }
}