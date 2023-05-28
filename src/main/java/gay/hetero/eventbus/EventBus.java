package gay.hetero.eventbus;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventBus {
    private final ConcurrentHashMap<Class<?>, ConcurrentHashMap<Object, Method>> handlers = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void register(Object object) {
        for (Method method : object.getClass().getMethods()) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation but has " + parameterTypes.length + " arguments. Subscriber methods must have exactly 1 argument.");
                }

                Class<?> eventType = parameterTypes[0];
                handlers.putIfAbsent(eventType, new ConcurrentHashMap<>());
                handlers.get(eventType).put(object, method);
            }
        }
    }

    public void unregister(Object object) {
        for (ConcurrentHashMap<Object, Method> map : handlers.values()) {
            map.remove(object);
        }
    }

    public void post(Object event) {
        ConcurrentHashMap<Object, Method> map = handlers.get(event.getClass());
        if (map != null) {
            for (Object subscriber : map.keySet()) {
                Method method = map.get(subscriber);
                try {
                    method.invoke(subscriber, event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void postAsync(Object event) {
        ConcurrentHashMap<Object, Method> map = handlers.get(event.getClass());
        if (map != null) {
            for (Object subscriber : map.keySet()) {
                Method method = map.get(subscriber);
                executor.execute(() -> {
                    try {
                        method.invoke(subscriber, event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

}
