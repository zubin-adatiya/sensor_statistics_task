package com.epam.sensor.stats;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The type Sensor statistics task.
 */
public class SensorStatisticsTask {
 private final Map<String, SensorStatisticsData> sensorDataMap;
 private int processedFiles;
 private long processedMeasurements;
 private long failedMeasurements;

    /**
     * Gets sensor data map.
     *
     * @return the sensor data map
     */
    public Map<String, SensorStatisticsData> getSensorDataMap() {
        return sensorDataMap;
    }

    /**
     * Gets processed files.
     *
     * @return the processed files
     */
    public int getProcessedFiles() {
        return processedFiles;
    }

    /**
     * Gets processed measurements.
     *
     * @return the processed measurements
     */
    public long getProcessedMeasurements() {
        return processedMeasurements;
    }

    /**
     * Gets failed measurements.
     *
     * @return the failed measurements
     */
    public long getFailedMeasurements() {
        return failedMeasurements;
    }

    /**
     * Class to execute/manipulate Sensor statistics data.
     *
     * @param processedFiles the processed files
     */
    public SensorStatisticsTask(int processedFiles){
     this.sensorDataMap = new ConcurrentHashMap<>();
     this.processedFiles = processedFiles;
     this.processedMeasurements = 0;
     this.failedMeasurements = 0;

 }

    /**
     * Gets started.
     *
     * @param f the f
     * @throws IOException the io exception
     */
    public  void getStarted(File f) throws IOException {
        BufferedReader br = readFile(f);
        processFile(br);
    }

    /**
     * Calculate measurements.
     */
    public void calculateMeasurements(){
        List<SensorStatisticsData> stats = new ArrayList<>(sensorDataMap.values());
        Comparator<SensorStatisticsData> byAvg = Comparator
                .comparingDouble(SensorStatisticsData::getAvg);
        Collections.sort(stats, byAvg.reversed());
        this.failedMeasurements = stats.stream().map(s->s.getCountNan()).collect(Collectors.summingInt(Integer::intValue));
        this.processedMeasurements = stats.stream().map(s->s.getCountTotal()).collect(Collectors.summingInt(Integer::intValue));

        System.out.println("Num of processed files:" + this.processedFiles);
        System.out.println("Num of processed measurements:" + this.processedMeasurements);
        System.out.println("Num of failed measurements:"+this.failedMeasurements);

        System.out.println("Sensors with highest avg humidity:");
        System.out.println("sensor-id,min,avg,max");
        for(SensorStatisticsData stat: stats){
            if(stat.isNan()){
                System.out.println(stat.getSensorId()+",NaN,NaN,NaN");
            }else{
                System.out.println(stat.getSensorId()+ "," +stat.getMin()+ "," +stat.getAvg()+ "," +stat.getMax());
            }

        }
    }


    /**
     * Read file buffered reader.
     *
     * @param f the f
     * @return the buffered reader
     * @throws IOException the io exception
     */
//function to read file
    public BufferedReader readFile(File f) throws IOException {

        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        return br;

    }

    /**
     * Process file.
     *
     * @param br the br
     * @throws IOException the io exception
     */
//function to process each line in buffered reader
    public void processFile(BufferedReader br) throws IOException {
        String s;
        String[] words = null;
        while((s=br.readLine())!= null) {
            if(!s.startsWith("sensor-id")){
                words = s.split(",");
                if(words.length> 0 ){
                    if(sensorDataMap.containsKey(words[0])){
                        sensorDataMap.get(words[0]).addSensorData(words[1]);
                    } else{
                        sensorDataMap.put(words[0], new SensorStatisticsData(words[0], words[1]));
                    }
                }
            }

        }
    }

}
