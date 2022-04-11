package com.epam.sensor.stats;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class to find and read sensor statistics files
 */
public class SensorFile implements Runnable{
    private final File file;
    private final SensorStatisticsTask task;


    /**
     * Instantiates a new Sensor file.
     *
     * @param file the file
     * @param task the task
     */
    public SensorFile(File file, SensorStatisticsTask task) {
        this.file = file;
        this.task = task;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Please enter sensor statistics directory:");
        // Enter data using BufferReader
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        // Reading data using readLine
        String directory = reader.readLine();
        List<File> fileList = new ArrayList<>();
        for (File file : new File(directory).listFiles()) {
            String filePath = file.getAbsolutePath();
            if ("csv".equals(filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length()))) {
                System.out.println("CSV file found -> " + filePath);
                fileList.add(file);
            }
        }
        processFiles(fileList);
    }

    //run() method is evoked when threads are started
    public void run() {
        try {
            this.task.getStarted(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Process files.
     *
     * @param fileList the file list
     * @throws IOException the io exception
     */
    public static void processFiles(List<File> fileList) throws IOException {
        int processedFiles = fileList.size();
        List<File> chunkedFiles = new ArrayList<>();
        SensorStatisticsTask task = new SensorStatisticsTask(processedFiles);
        for(File file:fileList){
            chunkedFiles.addAll(splitFile(file, 10));
        }
        ExecutorService MainThreadPool = Executors.newFixedThreadPool(5);//Initialize main thread pool
        for(File file:fileList){
            MainThreadPool.submit((Runnable) new SensorFile(file, task)); //run method is called for each chunk of file i-e separate threads are created for each chunk
        }
        MainThreadPool.shutdown();  //main thread pool terminated

        while (!MainThreadPool.isTerminated()) {
        } //continue until all the threads are terminated
        task.calculateMeasurements();
    }

    /**
     * Split file list.
     *
     * @param file           the file
     * @param sizeOfFileInMB the size of file in mb
     * @return the list
     * @throws IOException the io exception
     */
//method to spilt files in chunk so that each thread will process small chunk of file
    public static List<File> splitFile(File file, int sizeOfFileInMB) throws IOException {
        int counter = 1;
        List<File> files = new ArrayList<File>();
        int sizeOfChunk =    1024 * 1024 * sizeOfFileInMB;
        String eof = System.lineSeparator();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String name = file.getName();
            String line = br.readLine();
            while (line != null) {
                File newFile = new File(file.getParent(), name + "." + String.format("%03d", counter++));
                try (OutputStream out = new BufferedOutputStream(new FileOutputStream(newFile))) {
                    int fileSize = 0;
                    while (line != null) {
                        byte[] bytes = (line + eof).getBytes(Charset.defaultCharset());
                        if (fileSize + bytes.length > sizeOfChunk)
                            break;
                        out.write(bytes);
                        fileSize += bytes.length;
                        line = br.readLine();
                    }
                }
                files.add(newFile);
            }
        }
        return files;
    }
}
