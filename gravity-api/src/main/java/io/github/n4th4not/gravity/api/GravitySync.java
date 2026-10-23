package io.github.n4th4not.gravity.api;

import io.github.n4th4not.immersiveportals.protocol.GravityState;
import java.util.HashMap;
import java.util.Map;

/** Per-viewer bounded publication history. Access from the owning session's thread. */
public final class GravitySync {
    private record Sent(GravityState state, long tick) {}
    private final Map<GravityIdentity, Sent> sent = new HashMap<>();
    public boolean shouldSend(GravityIdentity key, GravityState state, long tick, boolean force) {
        var old = sent.get(key);
        if (!force && old != null && old.state().equals(state) && tick - old.tick() < 100)
            return false;
        if (sent.size() >= 4096 && !sent.containsKey(key)) return false;
        sent.put(key, new Sent(state, tick));
        return true;
    }
    public void expire(long tick) {
        sent.entrySet().removeIf(e -> tick - e.getValue().tick() > 200);
    }
    public void clear() { sent.clear(); }
}
