package BSDSClient1;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class postCount implements Runnable {
  private String postUrl;
  private int iterationPerThread;
  private AtomicInteger requestSent;
  private AtomicInteger requestSuccess;
  private List<Long> latencyList;

  public postCount(String postUrl, int iterationPerThread, AtomicInteger requestSent,
                   AtomicInteger requestSuccess, List<Long> latencyList) {
    this.postUrl = postUrl;
    this.iterationPerThread = iterationPerThread;
    this.requestSent = requestSent;
    this.requestSuccess = requestSuccess;
    this.latencyList = latencyList;
  }

  @Override
  public void run() {
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target(postUrl);

    for (int i = 0; i < iterationPerThread; i++) {
      long taskStartTime = System.nanoTime();
      Response response = target.request()
          .buildPost(Entity.entity("", MediaType.TEXT_PLAIN)).invoke();
      long taskEndTime = System.nanoTime();

      synchronized (latencyList) {
        latencyList.add((taskEndTime - taskStartTime) / 1000000);
      }

      requestSent.incrementAndGet();

      if (response.getStatus() == 200) {
        requestSuccess.incrementAndGet();
      }

      response.close();
    }
  }
}