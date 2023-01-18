package gay.hetero.test;

import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import gay.hetero.eventbus.EventBus;
import gay.hetero.eventbus.Subscribe;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.radbus.Listen;
import io.github.nevalackin.radbus.PubSub;
import rip.hippo.lwjeb.configuration.BusConfigurations;
import rip.hippo.lwjeb.configuration.config.impl.BusPubSubConfiguration;
import rip.hippo.lwjeb.message.publish.impl.ExperimentalMessagePublisher;
import test.TestEvent;

public class Main {
    private static final Object MONKEY = new Object();
    private static final Event MONKEY2 = new Event() {};
    public static void main(String[] args) {
        EventBus gptbus = new EventBus();
        PubSub<Object> radbus = PubSub.newInstance(System.err::println);
        best.azura.eventbus.core.EventBus azurabus = new best.azura.eventbus.core.EventBus();
        io.github.nevalackin.homoBus.bus.impl.EventBus<Object> homobus = new io.github.nevalackin.homoBus.bus.impl.EventBus<>();
        com.google.common.eventbus.EventBus guavaBus = new com.google.common.eventbus.EventBus();
        rip.hippo.lwjeb.bus.PubSub<Object> lwjeb = new rip.hippo.lwjeb.bus.PubSub<>(new BusConfigurations.Builder().setConfiguration(
                BusPubSubConfiguration.class, () -> {
                    BusPubSubConfiguration busPubSubConfiguration = BusPubSubConfiguration.getDefault();
                    busPubSubConfiguration.setPublisher(new ExperimentalMessagePublisher<>());
                    return busPubSubConfiguration;
                }
        ).build());
        EventSubscriber event = new EventSubscriber();

        int iterations = 1_000_000;
        long start = System.currentTimeMillis();
        gptbus.register(event);

        for (int i = 0; i < iterations; i++) {
            gptbus.post(MONKEY2);
        }
        gptbus.unregister(event);
        System.out.println("GPTBus " + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        radbus.subscribe(event);
        for (int i = 0; i < iterations; i++) {
            radbus.publish(MONKEY2);
        }
        radbus.unsubscribe(event);
        System.out.println("RadBus " + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        azurabus.register(event);
        for (int i = 0; i < iterations; i++) {
            azurabus.call(MONKEY2);
        }
        azurabus.unregister(event);
        System.out.println("AzuraBus " + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        homobus.subscribe(event);
        for (int i = 0; i < iterations; i++) {
            azurabus.call(MONKEY2);
        }
        homobus.unsubscribe(event);
        System.out.println("HomoBus " + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        guavaBus.register(event);
        for (int i = 0; i < iterations; i++) {
            guavaBus.post(MONKEY2);
        }
        guavaBus.unregister(event);
        System.out.println("GuavaBus " + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        lwjeb.subscribe(event);
        for (int i = 0; i < iterations; i++) {
            lwjeb.post(MONKEY2).dispatch();
        }
        lwjeb.unsubscribe(event);
        System.out.println("LWJEB " + (System.currentTimeMillis() - start) + "ms");
    }



    public static class EventSubscriber {


        @EventLink
        public final Listener<Object> emptyListener = event -> {
        };

        @EventHandler
        public final best.azura.eventbus.handler.Listener<TestEvent> eventListener = event -> {

        };

        @Subscribe
        @Listen
        @com.google.common.eventbus.Subscribe
        public final void eventListener(Object message) {

        }

    }
}