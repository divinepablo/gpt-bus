package test;

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

public class SubUnsubTest {
    public static final int ITERATIONS = 1000;

    private static double getMS(long start) {
        return (System.nanoTime() - start) / 1E+6;
    }
    public static void test() {
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


        long start = System.nanoTime();


        for (int i = 0; i < ITERATIONS; i++) {
            gptbus.register(event);
            gptbus.unregister(event);
        }

        System.out.println("GPTBus " + getMS(start) + "ms");

        start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            radbus.subscribe(event);
            radbus.unsubscribe(event);
        }
        System.out.println("RadBus " + getMS(start) + "ms");

        start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            azurabus.register(event);
            azurabus.unregister(event);
        }
        System.out.println("AzuraBus " + getMS(start) + "ms");

        start = System.nanoTime();

        for (int i = 0; i < ITERATIONS; i++) {
            homobus.subscribe(event);
            homobus.unsubscribe(event);
        }

        System.out.println("HomoBus " + getMS(start) + "ms");

        start = System.nanoTime();

        for (int i = 0; i < ITERATIONS; i++) {
            guavaBus.register(event);
            guavaBus.unregister(event);
        }

        System.out.println("GuavaBus " + getMS(start) + "ms");

        start = System.nanoTime();

        for (int i = 0; i < ITERATIONS; i++) {
            lwjeb.subscribe(event);
            lwjeb.unsubscribe(event);
        }

        System.out.println("LWJEB " + getMS(start) + "ms");
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
