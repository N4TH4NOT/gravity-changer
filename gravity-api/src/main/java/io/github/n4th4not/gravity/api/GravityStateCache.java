package io.github.n4th4not.gravity.api;

import io.github.n4th4not.immersiveportals.protocol.GravityState;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.LongSupplier;

/** Bounded pending states, including updates received before a remote entity is spawned. */
public final class GravityStateCache<E> {
    private final class Entry {
        final GravityState state;
        final long received = clock.getAsLong();
        WeakReference<E> applied = new WeakReference<>(null);
        Entry(GravityState state) { this.state = state; }
    }
    private final Map<GravityIdentity, Entry> states = new LinkedHashMap<>();
    private final Function<GravityIdentity, E> findEntity;
    private final BiConsumer<E, GravityState> applyState;
    private final LongSupplier clock;
    public GravityStateCache(Function<GravityIdentity, E> findEntity, BiConsumer<E, GravityState> applyState) {
        this(findEntity, applyState, System::nanoTime);
    }
    GravityStateCache(Function<GravityIdentity, E> findEntity,
            BiConsumer<E, GravityState> applyState, LongSupplier clock) {
        this.findEntity = findEntity;
        this.applyState = applyState;
        this.clock = clock;
    }
    public void receive(GravityIdentity key, GravityState state) {
        states.remove(key);
        var entry = new Entry(state);
        states.put(key, entry);
        if (states.size() > 4096) states.remove(states.keySet().iterator().next());
        apply(key, entry);
    }
    public void tick() {
        long now = clock.getAsLong();
        states.entrySet().removeIf(e -> now - e.getValue().received > 60_000_000_000L);
        states.forEach(this::apply);
    }
    private void apply(GravityIdentity key, Entry entry) {
        E entity = findEntity.apply(key);
        if (entity != null && entry.applied.get() != entity) {
            applyState.accept(entity, entry.state);
            entry.applied = new WeakReference<>(entity);
        }
    }
    public void clear(BiConsumer<E, GravityState> resetState) {
        for (var entry : states.values()) {
            E entity = entry.applied.get();
            if (entity != null) resetState.accept(entity, GravityState.DEFAULT);
        }
        states.clear();
    }
}
