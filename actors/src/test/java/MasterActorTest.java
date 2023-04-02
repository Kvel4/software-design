import akka.actor.ActorRef;
import akka.actor.Props;
import akka.util.Timeout;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.itmo.actor.MasterActor;
import ru.itmo.server.StubServer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;

public class MasterActorTest extends BaseTest {

    private ActorRef master;

    @BeforeEach
    public void setup() {
        master = system.actorOf(
            Props.create(
                MasterActor.class,
                Map.of(
                    "Yandex", "http://localhost:8080",
                    "Google", "http://localhost:8080"
                )
            ),
            "master"
        );
    }

    @Test
    public void testWithoutTimeout() throws IOException {
        try (StubServer server = new StubServer()) {
            String result = (String) ask(master, "query", Timeout.apply(1, TimeUnit.SECONDS))
                .toCompletableFuture()
                .join();

            Assertions.assertEquals("{Google=[query1, query2], Yandex=[query1, query2]}", result);
        }
    }

    @Test
    public void testWithTimeout() throws IOException {
        try (StubServer server = new StubServer(2)) {
            String result = (String) ask(master, "query", Timeout.apply(3, TimeUnit.SECONDS))
                .toCompletableFuture()
                .join();

            Assertions.assertEquals("{}", result);
        }
    }
}
