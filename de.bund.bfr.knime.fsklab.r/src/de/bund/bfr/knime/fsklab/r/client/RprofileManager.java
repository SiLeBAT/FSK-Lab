package de.bund.bfr.knime.fsklab.r.client;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import com.sun.jna.Platform;
import de.bund.bfr.knime.fsklab.preferences.PreferenceInitializer;
import de.bund.bfr.knime.fsklab.r.server.RConnectionFactory;

public class RprofileManager {

  private static int rHandlerCount;
  private static ReentrantLock lock = new ReentrantLock();
  public final static String BFR_R_PLUGIN_NAME= "de.bund.bfr.binary.r.win32.x86_64_3.4.4";
  
  public static void subscribe() throws IOException {
    try {
      lock.lock();
      if (rHandlerCount++ == 0 && (PreferenceInitializer.getRPath().contains(BFR_R_PLUGIN_NAME) || (Platform.isLinux() && !PreferenceInitializer.isRConda()))) {
        RConnectionFactory.createFskLibrary();
        RConnectionFactory.backupProfile();
        RConnectionFactory.configureProfile();
      }
    } finally {
      lock.unlock();
    }
  }

  public synchronized static void unSubscribe() throws IOException {
    try {
      lock.lock();
      if (--rHandlerCount == 0 && PreferenceInitializer.isRProfileToBeRestored() && (PreferenceInitializer.getRPath().contains(BFR_R_PLUGIN_NAME) || (Platform.isLinux() && !PreferenceInitializer.isRConda()))) {
        RConnectionFactory.restoreProfile();
      }
    } finally {
      lock.unlock();
    }

  }

  
}
