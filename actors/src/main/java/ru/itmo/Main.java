package ru.itmo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
import ru.itmo.actor.MasterActor;
import ru.itmo.server.StubServer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;

public class Main {
    public static final Timeout TIMEOUT = Timeout.apply(10, TimeUnit.SECONDS);

    public static void main(String[] args) throws IOException {
        ActorSystem system = ActorSystem.create("browsers");

        try (StubServer server = new StubServer()) {
            ActorRef master = system.actorOf(
                Props.create(
                    MasterActor.class,
                    Map.of(
                        "Yandex", "http://localhost:8080",
                        "Google", "http://localhost:8080"
                    )
                ),
                "master"
            );

            System.out.println(ask(master, "query", TIMEOUT).toCompletableFuture().join());
        } finally {
            system.terminate();
        }
    }
}