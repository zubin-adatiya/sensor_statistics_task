package com.epam.sensor.stats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class SensorStatisticsTaskTest {

    @Test
    public void calculateMeasurementsTestSuccess() throws IOException {
        SensorStatisticsTask task = new SensorStatisticsTask(1);
        task.getStarted(new File(getClass().getClassLoader().getResource("Leader-2.csv").getFile()));
        task.calculateMeasurements();
        Assertions.assertEquals(1, task.getProcessedFiles());
        Assertions.assertEquals(4, task.getProcessedMeasurements());
        Assertions.assertEquals(1, task.getFailedMeasurements());
    }

    @Test
    public void calculateMeasurementsTestSuccessMultipleFiles() throws IOException {
        SensorStatisticsTask task = new SensorStatisticsTask(2);
        task.getStarted(new File(getClass().getClassLoader().getResource("Leader-1.csv").getFile()));
        task.getStarted(new File(getClass().getClassLoader().getResource("Leader-2.csv").getFile()));
        task.calculateMeasurements();
        Assertions.assertEquals(2, task.getProcessedFiles());
        Assertions.assertEquals(7, task.getProcessedMeasurements());
        Assertions.assertEquals(2, task.getFailedMeasurements());
    }

    @Test
    public void calculateMeasurementsTestNoData() throws IOException {
        SensorStatisticsTask task = new SensorStatisticsTask(0);
        task.getStarted(new File(getClass().getClassLoader().getResource("NoDataLeader.csv").getFile()));
        task.calculateMeasurements();
        Assertions.assertEquals(0, task.getProcessedFiles());
        Assertions.assertEquals(0, task.getProcessedMeasurements());
        Assertions.assertEquals(0, task.getFailedMeasurements());
    }

    @Test
    public void calculateMeasurementsTestFailure() throws IOException {
        SensorStatisticsTask task = new SensorStatisticsTask(0);
        IOException exception  = Assertions.assertThrows(
                IOException.class,
                () -> task.getStarted(new File("InvalidLeader.csv")),
                "Expected doThing() to throw, but it didn't"
        );

    }
}
