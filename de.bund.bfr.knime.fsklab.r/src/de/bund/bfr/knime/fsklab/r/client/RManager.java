package de.bund.bfr.knime.fsklab.r.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import de.bund.bfr.knime.fsklab.r.server.RConnectionFactory;

public class RManager {

  private static int rHandlerCount;
  private static ReentrantLock lock = new ReentrantLock();
  private static Map<Integer, String> rProcessID = new HashMap<>();
  private static final String KILL = "taskkill /F /IM ";
  
  public static void storeExecutorProcessID(int executorHash, String pID) throws IOException {
      rProcessID.put(executorHash, pID);
  }
  
  public static void subscribe() throws IOException {
    try {
      lock.lock();
      if (rHandlerCount++ == 0) {
        RConnectionFactory.createFskLibrary();
        RConnectionFactory.backupProfile();
        RConnectionFactory.configureProfile();
      }
    } finally {
      lock.unlock();
    }
  }

  public synchronized static void unSubscribe(Integer executorHash) throws IOException {
    try {
      lock.lock();
      if (--rHandlerCount == 0) {
        RConnectionFactory.restoreProfile();
      }
    } finally {
      System.out.println(rProcessID.get(executorHash));
      killProcess(""+rProcessID.get(executorHash));
      lock.unlock();
    }

  }
  
  public static void killProcess(String serviceName) {

		try {
			Runtime.getRuntime().exec(KILL + serviceName);
			System.out.println(serviceName+" killed successfully!");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

  
}
