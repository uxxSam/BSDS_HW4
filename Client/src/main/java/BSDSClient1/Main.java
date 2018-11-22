package BSDSClient1;
import org.glassfish.jersey.client.ClientProperties;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Main {
  public List<String> latencyList = new ArrayList<>();
  public List<Long> latencyLongList = new ArrayList<>();
  public ConcurrentHashMap<Integer, Integer> latencyMap = new ConcurrentHashMap<>();

  public static void main(String[] args) {
    int maxThreads = Integer.parseInt(args[0]);
    String serverUrl = args[1];
    String dayNumber = args[2];
    int userPopulation = 9000000;
    int iterationPerThread = Integer.parseInt(args[3]);
    long testStartTime = System.nanoTime();

    String url = serverUrl;
    Main test = new Main();
    try {

      // ResteasyClientBuilder cb = new ResteasyClientBuilder();

//    ResteasyClientBuilder.establishConnectionTimeout();
//
//    Client client = new ResteasyClientBuilder()
//        .establishConnectionTimeout(transportConfig.getConnectionTimeout(), TimeUnit.MILLISECONDS)
//        .socketTimeout(transportConfig.getInvokeTimeout(), TimeUnit.MILLISECONDS)
//        .connectionPoolSize(clientTransportConfig.getClientBusinessPoolSize())// 连接池？
//        .build();

//    Client client = new ResteasyClientBuilder();
//
//
//
//    establishConnectionTimeout(20, TimeUnit.SECONDS).build();

      ClientBuilder cb = new ResteasyClientBuilder();
      cb.connectTimeout(30, TimeUnit.SECONDS);
      cb.readTimeout(30, TimeUnit.SECONDS);

      Client client = cb.build();


      // Client client = ClientBuilder.newClient();
      // Client client = new ResteasyClientBuilder().connectTimeout(15, TimeUnit.SECONDS).build();
//    cb.readTimeout(15, TimeUnit.SECONDS);
//
//    Client client = cb.build();
//    client.property(ClientProperties.CONNECT_TIMEOUT, 20000);
//    client.property(ClientProperties.READ_TIMEOUT,    20000);

      String getSingleURL = new StringBuilder(url).append("dropDataBase").toString();
      System.out.println(getSingleURL);
      WebTarget target = client.target(getSingleURL);
      target.request().get();

      try {
        TimeUnit.SECONDS.sleep(20);
      } catch (InterruptedException e) {
        System.out.println("ERROR!");
      }

      System.out.println(maxThreads);
      testStartTime = System.nanoTime();

      // Warmup phase
      System.out.println("Warmup phase");
      test.timeOperation(url, maxThreads / 10, iterationPerThread,
          0, 2, userPopulation, 5000, dayNumber, testStartTime);

      // TimeUnit.SECONDS.sleep(60);

      // Loading phase
      System.out.println("Loading phase");
      test.timeOperation(url, maxThreads / 2, iterationPerThread,
          3, 7, userPopulation, 5000, dayNumber, testStartTime);

     // TimeUnit.SECONDS.sleep(60);

      // Peak phase
      System.out.println("Peak phase");
      test.timeOperation(url, maxThreads, iterationPerThread,
          8, 18, userPopulation, 5000, dayNumber, testStartTime);

      // TimeUnit.SECONDS.sleep(60);

      // Cooldown phase
      System.out.println("Cooldown phase");
      test.timeOperation(url, maxThreads / 4, iterationPerThread,
          19, 23, userPopulation, 5000, dayNumber, testStartTime);


      PrintWriter out;
// throughput = count / time = (/s
      try {
        out = new PrintWriter("latency-8-" + maxThreads +".csv");

        for (Map.Entry<Integer, Integer> entry : test.latencyMap.entrySet()) {
          out.println(entry.getKey() + "," + entry.getValue());
        }

        out.close();
      } catch (IOException e) {

      }

      try {
        out = new PrintWriter("latency-long-8-" + maxThreads +".csv");

        for (long time : test.latencyLongList) {
          out.println(time);
        }

        out.close();
      } catch (IOException e) {

      }

      long totalLatency = 0;

      for (long latency : test.latencyLongList) {
        totalLatency += latency;
      }

      Collections.sort(test.latencyLongList);
      System.out.println("Average: " + totalLatency / test.latencyLongList.size());
      System.out.println("95th percentile: " + percentile(test.latencyLongList, 95));
      System.out.println("99th percentile: " + percentile(test.latencyLongList, 99));
    } catch (Exception e) {

    }

//    test = new Main();
//
//    client = ClientBuilder.newClient();
//    getSingleURL = new StringBuilder(url).append("dropDataBase").toString();
//    System.out.println(getSingleURL);
//    target = client.target(getSingleURL);
//    target.request().get();
//    try {
//      TimeUnit.MINUTES.sleep(1);
//    } catch (InterruptedException e) {
//      System.out.println("ERROR!");
//    }
//
//    maxThreads = 64;
//    testStartTime = System.nanoTime();
//
//    // Warmup phase
//    System.out.println("Warmup phase");
//    test.timeOperation(url, maxThreads / 10, iterationPerThread,
//        0, 2, userPopulation, 5000, dayNumber, testStartTime);
//
//    // Loading phase
//    System.out.println("Loading phase");
//    test.timeOperation(url, maxThreads / 2, iterationPerThread,
//        3, 7, userPopulation, 5000, dayNumber, testStartTime);
//
//    // Peak phase
//    System.out.println("Peak phase");
//    test.timeOperation(url, maxThreads, iterationPerThread,
//        8, 18, userPopulation, 5000, dayNumber, testStartTime);
//
//    // Cooldown phase
//    System.out.println("Cooldown phase");
//    test.timeOperation(url, maxThreads / 4, iterationPerThread,
//        19, 23, userPopulation, 5000, dayNumber, testStartTime);
//
//    out = null;
//// throughput = count / time = (/s
//    try {
//      out = new PrintWriter("latency-1-64t.csv");
//
//      for (Map.Entry<Integer, Integer> entry : test.latencyMap.entrySet()) {
//        out.println(entry.getKey() + "," + entry.getValue());
//      }
//
//      out.close();
//    } catch (IOException e) {
//
//    }
//
//    try {
//      out = new PrintWriter("latency-long-1-64t.csv");
//
//      for (long time: test.latencyLongList) {
//        out.println(time);
//      }
//
//      out.close();
//    } catch (IOException e) {
//
//    }
//
//    totalLatency = 0;
//
//    for (long latency : test.latencyLongList) {
//      totalLatency += latency;
//    }
//
//    Collections.sort(test.latencyLongList);
//    System.out.println("Average: " + totalLatency / test.latencyLongList.size());
//    System.out.println("95th percentile: " + percentile(test.latencyLongList, 95));
//    System.out.println("99th percentile: " + percentile(test.latencyLongList, 99));
//
//    test = new Main();
//
//    client = ClientBuilder.newClient();
//    getSingleURL = new StringBuilder(url).append("dropDataBase").toString();
//    System.out.println(getSingleURL);
//    target = client.target(getSingleURL);
//    target.request().get();
//    try {
//      TimeUnit.MINUTES.sleep(1);
//    } catch (InterruptedException e) {
//      System.out.println("ERROR!");
//    }
//
//    maxThreads = 128;
//    testStartTime = System.nanoTime();
//
//    // Warmup phase
//    System.out.println("Warmup phase");
//    test.timeOperation(url, maxThreads / 10, iterationPerThread,
//        0, 2, userPopulation, 5000, dayNumber, testStartTime);
//
//    // Loading phase
//    System.out.println("Loading phase");
//    test.timeOperation(url, maxThreads / 2, iterationPerThread,
//        3, 7, userPopulation, 5000, dayNumber, testStartTime);
//
//    // Peak phase
//    System.out.println("Peak phase");
//    test.timeOperation(url, maxThreads, iterationPerThread,
//        8, 18, userPopulation, 5000, dayNumber, testStartTime);
//
//    // Cooldown phase
//    System.out.println("Cooldown phase");
//    test.timeOperation(url, maxThreads / 4, iterationPerThread,
//        19, 23, userPopulation, 5000, dayNumber, testStartTime);
//
//
//    out = null;
//// throughput = count / time = (/s
//    try {
//      out = new PrintWriter("latency-1-128t.csv");
//
//      for (Map.Entry<Integer, Integer> entry : test.latencyMap.entrySet()) {
//        out.println(entry.getKey() + "," + entry.getValue());
//      }
//
//      out.close();
//    } catch (IOException e) {
//
//    }
//
//    try {
//      out = new PrintWriter("latency-long-1-128t.csv");
//
//      for (long time: test.latencyLongList) {
//        out.println(time);
//      }
//
//      out.close();
//    } catch (IOException e) {
//
//    }
//
//    totalLatency = 0;
//
//    for (long latency : test.latencyLongList) {
//      totalLatency += latency;
//    }
//
//    Collections.sort(test.latencyLongList);
//    System.out.println("Average: " + totalLatency / test.latencyLongList.size());
//    System.out.println("95th percentile: " + percentile(test.latencyLongList, 95));
//    System.out.println("99th percentile: " + percentile(test.latencyLongList, 99));
//
//    test = new Main();
//
//    client = ClientBuilder.newClient();
//    getSingleURL = new StringBuilder(url).append("dropDataBase").toString();
//    System.out.println(getSingleURL);
//    target = client.target(getSingleURL);
//    target.request().get();
//    try {
//      TimeUnit.MINUTES.sleep(1);
//    } catch (InterruptedException e) {
//      System.out.println("ERROR!");
//    }
//
//    maxThreads = 256;
//    testStartTime = System.nanoTime();
//
//    // Warmup phase
//    System.out.println("Warmup phase");
//    test.timeOperation(url, maxThreads / 10, iterationPerThread,
//        0, 2, userPopulation, 5000, dayNumber, testStartTime);
//
//    // Loading phase
//    System.out.println("Loading phase");
//    test.timeOperation(url, maxThreads / 2, iterationPerThread,
//        3, 7, userPopulation, 5000, dayNumber, testStartTime);
//
//    // Peak phase
//    System.out.println("Peak phase");
//    test.timeOperation(url, maxThreads, iterationPerThread,
//        8, 18, userPopulation, 5000, dayNumber, testStartTime);
//
//    // Cooldown phase
//    System.out.println("Cooldown phase");
//    test.timeOperation(url, maxThreads / 4, iterationPerThread,
//        19, 23, userPopulation, 5000, dayNumber, testStartTime);
//
//    out = null;
//// throughput = count / time = (/s
//    try {
//      out = new PrintWriter("latency-1-256t.csv");
//
//      for (Map.Entry<Integer, Integer> entry : test.latencyMap.entrySet()) {
//        out.println(entry.getKey() + "," + entry.getValue());
//      }
//
//      out.close();
//    } catch (IOException e) {
//
//    }
//
//    try {
//      out = new PrintWriter("latency-long-1-256t.csv");
//
//      for (long time: test.latencyLongList) {
//        out.println(time);
//      }
//
//      out.close();
//    } catch (IOException e) {
//
//    }
//
//    totalLatency = 0;
//
//    for (long latency : test.latencyLongList) {
//      totalLatency += latency;
//    }
//
//    Collections.sort(test.latencyLongList);
//    System.out.println("Average: " + totalLatency / test.latencyLongList.size());
//    System.out.println("95th percentile: " + percentile(test.latencyLongList, 95));
//    System.out.println("99th percentile: " + percentile(test.latencyLongList, 99));
  }

  public static long percentile(List<Long> latencies, double Percentile) {
    int index = (int)Math.ceil(((double)Percentile / (double)100) * (double)latencies.size());
    return latencies.get(index - 1);
  }

  private void timeOperation(String serverURL, int threadCount, int iteration,
    int intervalStart, int intervalEnd, int userCountMax, int stepCountMax,
                             String dayNumber, long testStartTime) {
    // System.out.println(threadCount);
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threadCount);

    for (int i = 0; i < threadCount; i++) {
      fixedThreadPool.execute(
          new Runnable() {
            @Override
            public void run() {
              ClientBuilder cb = new ResteasyClientBuilder();
              cb.connectTimeout(15, TimeUnit.SECONDS);
              cb.readTimeout(15, TimeUnit.SECONDS);

              Client client = cb.build();

//              Client client = ClientBuilder.newClient();
//              client.property(ClientProperties.CONNECT_TIMEOUT, 20000);
//              client.property(ClientProperties.READ_TIMEOUT,    20000);
              WebTarget target = null;

              for (int i = 0; i < iteration * (intervalEnd - intervalStart); i++) {
                int[] randomUserID = new int[3];
                int[] randomTime = new int[3];
                int[] randomStepCount = new int[3];

                for (int j = 0; j < 3; j++) {
                  randomUserID[j] = ThreadLocalRandom.current().nextInt(userCountMax);
                  randomTime[j]  = ThreadLocalRandom.current().nextInt(intervalStart, 9999999);
                  randomStepCount[j] = ThreadLocalRandom.current().nextInt(stepCountMax);
                }

                // POST /userID1/day/timeInterval1/stepCount1
                // POST /userID2/day/timeInterval2l/stepCount2
                for (int j = 0; j < randomUserID.length - 1; j++) {
                  String postURL = new StringBuilder().append(serverURL)
                      .append(randomUserID[j]).append('/')
                      .append(dayNumber).append('/').append(randomTime[j])
                      .append('/').append(randomStepCount[j]).toString();

                  target = client.target(postURL);

                  long startTime = System.nanoTime();

                  Response response = null;

                  try {
                    response = target.request()
                        .buildPost(Entity.entity("", MediaType.TEXT_PLAIN)).invoke();

                    long endTime = System.nanoTime();
                    long duration = (endTime - startTime) / 1000000;

                    synchronized (latencyLongList) {
                      latencyLongList.add(duration);
//                    latencyList.add(new StringBuilder().append(duration).append(',')
//                        .append((int)((System.nanoTime() - testStartTime) / 1000000000)).toString());
                    }

                    synchronized (latencyMap) {
                      int timeSlot = (int) ((System.nanoTime() - testStartTime) / 1000000000);
                      latencyMap.put(timeSlot, latencyMap.getOrDefault(timeSlot, 0) + 1);
                    }

                  } catch (Exception e) {

                  } finally {
                    if (response != null) {
                      response.close();
                    }
                  }
                }

                // GET /current/userID1
                String getCurrentURL = new StringBuilder(serverURL)
                    .append("current/").append(randomUserID[0]).toString();
                target = client.target(getCurrentURL);

                long startTime = System.nanoTime();

                Response response = null;

                try {
                  response = target.request().get();

                  long endTime = System.nanoTime();
                  long duration = (endTime - startTime) / 1000000;

                  synchronized (latencyLongList) {
                    latencyLongList.add(duration);
//                  latencyList.add(new StringBuilder().append(duration).append(',')
//                      .append((int)((System.nanoTime() - testStartTime) / 1000000000)).toString());

                  }

                  synchronized (latencyMap) {
                    int timeSlot = (int) ((System.nanoTime() - testStartTime) / 1000000000);
                    latencyMap.put(timeSlot, latencyMap.getOrDefault(timeSlot, 0) + 1);
                  }
                } catch (Exception e) {

                } finally {
                  if (response != null) {
                    response.close();
                  }
                }

                try {

                  // GET/single/userID2/day
                  String getSingleURL = new StringBuilder(serverURL)
                      .append("single/").append(randomUserID[1]).append('/')
                      .append(dayNumber).toString();
                  target = client.target(getSingleURL);

                  startTime = System.nanoTime();
                  response = target.request().get();
                  long endTime = System.nanoTime();
                  long duration = (endTime - startTime) / 1000000;

                  synchronized (latencyLongList) {
                    latencyLongList.add(duration);
//                  latencyList.add(new StringBuilder().append(duration).append(',')
//                      .append((int)((System.nanoTime() - testStartTime) / 1000000000)).toString());

                  }

                  synchronized (latencyMap) {
                    int timeSlot = (int) ((System.nanoTime() - testStartTime) / 1000000000);
                    latencyMap.put(timeSlot, latencyMap.getOrDefault(timeSlot, 0) + 1);
                  }
                } catch (Exception e) {

                } finally {
                  if (response != null) {
                    response.close();
                  }
                }

                try {

                  // POST /userID3/day/timeInterval3/stepCount3
                  String postURL = new StringBuilder().append(serverURL)
                      .append(randomUserID[2]).append('/')
                      .append(dayNumber).append('/').append(randomTime[2])
                      .append('/').append(randomStepCount[2]).toString();
                  target = client.target(postURL);
                  startTime = System.nanoTime();
                  response = target.request()
                      .buildPost(Entity.entity("", MediaType.TEXT_PLAIN)).invoke();
                  long endTime = System.nanoTime();
                  long duration = (endTime - startTime) / 1000000;

                  synchronized (latencyLongList) {
                    latencyLongList.add(duration);
//                  latencyList.add(new StringBuilder().append(duration).append(',')
//                      .append((int)((System.nanoTime() - testStartTime) / 1000000000)).toString());
                  }

                  synchronized (latencyMap) {
                    int timeSlot = (int) ((System.nanoTime() - testStartTime) / 1000000000);
                    latencyMap.put(timeSlot, latencyMap.getOrDefault(timeSlot, 0) + 1);
                  }
                } catch (Exception e) {

                } finally {
                  if (response != null) response.close();
                }
              }
            }
          });
    }

    fixedThreadPool.shutdown();

//    try {
//      fixedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
//    } catch (InterruptedException e) {
//
//    }

    while (!fixedThreadPool.isTerminated()) {
    }
  }
}
