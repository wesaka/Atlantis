package conclave.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GamePublisher {
    private static final Logger logger = LoggerFactory.getLogger(GamePublisher.class);
    private final Jedis publisherJedis;
    private final String channel;

    public GamePublisher(Jedis publisherJedis, String channel) {
        this.publisherJedis = publisherJedis;
        this.channel = channel;
    }

    public void start() {
        logger.info("Type your message (quit for terminate)");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                String line = reader.readLine();
                if (!"quit".equals(line)) {
                    publisherJedis.publish(channel, line);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            logger.error("IO failure while reading input", e);
        }
    }
}
