package com.sense360.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class APIExecutorService {

  public final static ExecutorService executorService = Executors.newFixedThreadPool(5);

}
