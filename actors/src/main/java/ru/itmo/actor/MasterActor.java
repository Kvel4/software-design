package ru.itmo.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import akka.actor.UntypedActor;
import ru.itmo.model.NamedResult;
import scala.concurrent.duration.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MasterActor extends UntypedActor {

    private static final int TIMEOUT = 1;

    private final Map<String, String> browsers;

    public final Map<String, List<String>> resultMap = new HashMap<>();

    private ActorRef sender;

    public MasterActor(Map<String, String> browsers) {
        this.browsers = browsers;
        getContext().setReceiveTimeout(Duration.create(TIMEOUT, TimeUnit.SECONDS));
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof String query) {
            sender = sender();

            for (var browser : browsers.entrySet()) {
                var refBrowser = context().actorOf(
                        Props.create(
                            ChildActor.class,
                            browser.getValue()
                        ),
                        browser.getKey()
                    );

                refBrowser.tell(query, self());
            }
        } else if (message instanceof NamedResult r) {
            resultMap.put(r.name(), r.result().links());

            if (resultMap.size() == browsers.size()) {
                sender.tell(resultMap.toString(), self());
                getContext().stop(self());
            }
        } else if (message instanceof ReceiveTimeout) {
            sender.tell(resultMap.toString(), self());
            getContext().stop(self());
        }
    }
}
