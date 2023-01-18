package gay.hetero.eventbus;

interface EventHandler<T> {
    void handle(T event);
}