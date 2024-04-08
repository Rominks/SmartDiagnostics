package com.smrt.smartdiagnostics;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Cucumber.class)
@SpringBootTest(classes = SmartDiagnosticsApplication.class)
@ContextConfiguration(classes = CucumberSpringConfiguration.class)
@CucumberOptions(
        features = {"src/main/resources"}
//        glue = {"com.smrt.smartdiagnostics.Steps" }
)
public class TestRunner {

}
