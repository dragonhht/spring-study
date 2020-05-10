package com.github.dragonhht.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dragonhht.batch.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import static java.lang.String.format;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBatchStudyApplicationTests {

    @Autowired
    private JobRegistry jobRegistry;
    @Autowired
    private JobLauncher jobLauncher;

    @Test
    public void contextLoads()  {
        String jobName = "saveUserJob";
        try {
            Job job = jobRegistry.getJob(jobName);
            JobExecution jobExecution = jobLauncher.run(job, createJobParams());
            if (!jobExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
                throw new RuntimeException(format("%s Job execution failed.", jobName));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(format("%s Job execution failed.", jobName));
        }

    }

    private static JobParameters createJobParams() {
        return new JobParametersBuilder().addDate("date", new Date()).toJobParameters();
    }

    @Test
    public void createMsg() throws IOException {
        // 清空数据
        String filePath = "D:\\my_work_spance\\idea_workspance\\MyJavaStudy\\spring-batch-study\\src\\main\\resources\\resource.txt";
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();

        try (OutputStream out = new FileOutputStream(file)){
            ObjectMapper mapper = new ObjectMapper();
            for (int i = 0; i < 1000; i++) {
                User user = new User("id_" + i, "name_" + i, "address_" + i, i);
                String val = mapper.writeValueAsString(user) + "\n";
                out.write(val.getBytes());
            }
        }

    }

}
