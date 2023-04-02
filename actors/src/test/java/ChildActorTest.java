import akka.actor.Props;
import akka.util.Timeout;
import org.junit.jupiter.api.Test;
import ru.itmo.actor.ChildActor;
import ru.itmo.model.NamedResult;
import ru.itmo.server.StubServer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChildActorTest extends BaseTest {
    public static final Timeout TIMEOUT = Timeout.apply(10, TimeUnit.SECONDS);

    @Test
    public void successSendMessageChildActor() throws IOException {
        try (StubServer server = new StubServer()) {
            var child = system.actorOf(
                Props.create(ChildActor.class, "http://localhost:8080"),
                "child"
            );

            NamedResult result = (NamedResult) ask(child, "request", TIMEOUT).toCompletableFuture().join();

            assertEquals(2, result.result().links().size());
            assertEquals(List.of("request1", "request2"), result.result().links());
        }
    }
}
