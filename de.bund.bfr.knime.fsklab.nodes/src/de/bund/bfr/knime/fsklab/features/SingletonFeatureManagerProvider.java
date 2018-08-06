package de.bund.bfr.knime.fsklab.features;

import org.togglz.core.activation.UsernameActivationStrategy;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.repository.StateRepository;
import org.togglz.core.repository.mem.InMemoryStateRepository;
import org.togglz.core.spi.FeatureManagerProvider;
import org.togglz.core.user.FeatureUser;
import org.togglz.core.user.SimpleFeatureUser;
import org.togglz.core.user.UserProvider;

public class SingletonFeatureManagerProvider implements FeatureManagerProvider {

  private static FeatureManager featureManager;

  public int priority() {
    return 30;
  }

  public synchronized FeatureManager getFeatureManager() {

    if (featureManager == null) {

      StateRepository repository = new InMemoryStateRepository();

      // Enable FEATURE_ONE for admin
      repository.setFeatureState(
          new FeatureState(Features.FEATURE_ONE, true).setStrategyId(UsernameActivationStrategy.ID)
              .setParameter(UsernameActivationStrategy.PARAM_USERS, "admin"));

      // Enable FEATURE_TWO for admin
      repository.setFeatureState(
          new FeatureState(Features.FEATURE_TWO, true).setStrategyId(UsernameActivationStrategy.ID)
              .setParameter(UsernameActivationStrategy.PARAM_USERS, "admin"));

      featureManager = new FeatureManagerBuilder().featureEnum(Features.class)
          .stateRepository(repository).userProvider(new JavaUserProvider()).build();
    }

    return featureManager;

  }

  private class JavaUserProvider implements UserProvider {

    public FeatureUser getCurrentUser() {
      String userName = System.getProperty("user.name");

      if (userName.equals("thomas")) {
        return new SimpleFeatureUser("admin", true);
      }
      return new SimpleFeatureUser("endUser", false);
    }
  }
}
