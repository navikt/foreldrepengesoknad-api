package no.nav.foreldrepenger.front;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EventListener {

  private final static Logger LOGGER = LoggerFactory.getLogger(EventListener.class);

  public void processMessage(String content) {
    LOGGER.info(content);
  }

}
