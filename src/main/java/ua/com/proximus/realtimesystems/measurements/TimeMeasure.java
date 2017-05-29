package ua.com.proximus.realtimesystems.measurements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeMeasure {
  private static TimeMeasure instance;

  private long timeOfStart = 0;
  private Map<Integer, List<Long>> measurements = new HashMap<Integer, List<Long>>();

  private TimeMeasure() {}

  public static TimeMeasure getInstance() {
    synchronized (TimeMeasure.class) {
      if (instance == null) {
        instance = new TimeMeasure();
      }
    }
    return instance;
  }

  public void start() {
    timeOfStart = System.nanoTime();
  }

  public void save(int index) {
    long timeInNanos = System.nanoTime() - timeOfStart;
    if (!measurements.containsKey(index)) {
      measurements.put(index, new ArrayList<Long>());
    }
    measurements.get(index).add(timeInNanos);
  }

  public Map<Integer, List<Long>> getMeasurements() {
    return measurements;
  }

  public List<Long> getMeasurements(int index) {
    return measurements.getOrDefault(index, null);
  }

  public int getCount(int index) {
    List<Long> list = measurements.getOrDefault(index, null);
    return list == null ? 0 : list.size();
  }

  public double getAverage(int index) {
    List<Long> list = measurements.getOrDefault(index, null);
    double sum = 0.0;
    if (list != null) {
      for (long aList : list) {
        sum += aList;
      }
      sum /= list.size();
    }
    return sum;
  }

  public void clear() {
    measurements.clear();
  }
}
