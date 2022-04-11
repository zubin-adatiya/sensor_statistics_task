package com.epam.sensor.stats;

/**
 * Class to contain Sensor statistics data.
 */
public class SensorStatisticsData {
    private String sensorId;
    private int min;
    private int max;
    private double avg;
    private int countAvg;
    private int countTotal;
    private int countNan;
    private boolean isNan;

    /**
     * Gets min.
     *
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets min.
     *
     * @param min the min
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Gets max.
     *
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets max.
     *
     * @param max the max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Gets avg.
     *
     * @return the avg
     */
    public double getAvg() {
        return avg;
    }

    /**
     * Sets avg.
     *
     * @param avg the avg
     */
    public void setAvg(double avg) {
        this.avg = avg;
    }

    /**
     * Gets count avg.
     *
     * @return the count avg
     */
    public int getCountAvg() {
        return countAvg;
    }

    /**
     * Sets count avg.
     *
     * @param countAvg the count avg
     */
    public void setCountAvg(int countAvg) {
        this.countAvg = countAvg;
    }

    /**
     * Is nan boolean.
     *
     * @return the boolean
     */
    public boolean isNan() {
        return isNan;
    }

    /**
     * Sets nan.
     *
     * @param nan the nan
     */
    public void setNan(boolean nan) {
        isNan = nan;
    }

    /**
     * Gets sensor id.
     *
     * @return the sensor id
     */
    public String getSensorId() {
        return sensorId;
    }

    /**
     * Sets sensor id.
     *
     * @param sensorId the sensor id
     */
    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    /**
     * Gets count total.
     *
     * @return the count total
     */
    public int getCountTotal() {
        return countTotal;
    }

    /**
     * Sets count total.
     *
     * @param countTotal the count total
     */
    public void setCountTotal(int countTotal) {
        this.countTotal = countTotal;
    }

    /**
     * Gets count nan.
     *
     * @return the count nan
     */
    public int getCountNan() {
        return countNan;
    }

    /**
     * Sets count nan.
     *
     * @param countNan the count nan
     */
    public void setCountNan(int countNan) {
        this.countNan = countNan;
    }

    /**
     * Instantiates a new Sensor statistics data.
     *
     * @param sensorId    the sensor id
     * @param sensorValue the sensor value
     */
    public SensorStatisticsData(String sensorId, String sensorValue){
        this.sensorId = sensorId;
        if(sensorValue.equals("NaN")){
            this.isNan = true;
            this.countNan = 1;
            this.avg = -1;
        }else{
            int sensorValueInt = Integer.parseInt(sensorValue);
            this.min = sensorValueInt;
            this.max = sensorValueInt;
            this.avg = sensorValueInt;
            this.countAvg = 1;
        }
        this.countTotal = 1;

    }

    /**
     * Add sensor data.
     *
     * @param sensorValueStr the sensor value str
     */
    public void addSensorData(String sensorValueStr){
        this.countTotal++;
        if(!sensorValueStr.equals("NaN")) {
            int sensorValue = Integer.parseInt(sensorValueStr);
            this.isNan = false;
            this.avg = this.avg == -1 ? 0 : this.avg;
            if (sensorValue < this.min) {
                this.min = sensorValue;
            }
            if (sensorValue > this.max) {
                this.max = sensorValue;
            }
            double prevAvg = this.avg * this.countAvg;
            countAvg++;
            this.avg = (prevAvg + sensorValue) / countAvg;
        }else{
            this.countNan++;
        }

    }

}
