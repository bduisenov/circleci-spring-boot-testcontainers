package com.example;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.GenericContainer;

import java.io.File;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class,webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public abstract class AbstractIntegrationTest {

    @ClassRule
    public static DockerComposeContainer docker = new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
            .withExposedService("redis_1", 6379)
            .withLocalCompose(true);

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

            EnvironmentTestUtils.addEnvironment("testcontainers", configurableApplicationContext.getEnvironment(),
                    "spring.redis.host=" + docker.getServiceHost("redis_1", 6379),
                    "spring.redis.port=" + docker.getServicePort("redis_1", 6379)
            );
        }
    }

}
