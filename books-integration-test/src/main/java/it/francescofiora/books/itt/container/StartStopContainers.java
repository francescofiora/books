package it.francescofiora.books.itt.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;

/**
 * Handles Start and Stop Containers.
 */
@Slf4j
public class StartStopContainers {

  private final List<GenericContainer<?>> containers = new ArrayList<>();

  /**
   * Add new container to the list.
   *
   * @param container GenericContainer
   */
  public void add(GenericContainer<?> container) {
    containers.add(container);
    container.start();
    logInfo(container);
  }

  /**
   * Log info of a container.
   *
   * @param container GenericContainer
   */
  public void logInfo(GenericContainer<?> container) {
    log.debug("### " + container.getContainerName() + " - " + container.getDockerImageName());
    log.debug("ContainerId: " + container.getContainerId());
    log.debug("ImageId: " + container.getContainerInfo().getImageId());
    log.debug("Host: " + container.getHost());
    log.debug("Platform: " + container.getContainerInfo().getPlatform());
  }

  /**
   * Stop and close all containers.
   */
  public void stopAndCloseAll() {
    Collections.reverse(containers);
    for (var container : containers) {
      stopAndClose(container);
    }
  }

  /**
   * Check if all containers are running.
   *
   * @return return true if all containers are running.
   */
  public boolean areRunning() {
    for (var container : containers) {
      if (!container.isRunning()) {
        log.error("Container " + container.getContainerName() + " is not running!");
        return false;
      }
    }
    return true;
  }

  private void stopAndClose(GenericContainer<?> container) {
    if (container != null) {
      if (container.isRunning()) {
        container.stop();
      }
      container.close();
    }
  }
}
