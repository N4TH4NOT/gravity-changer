package io.github.n4th4not.gravity.api;

import static org.junit.jupiter.api.Assertions.*;

import io.github.n4th4not.immersiveportals.protocol.*;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

class GravityCompatibilityTest {
    @Test
    void payloadMatchesTheExistingNineByteFormatForEveryDirection() throws IOException {
        for (var direction : GravityDirection.values()) {
            var state = new GravityState(direction, 12);
            var bytes = new ByteArrayOutputStream();
            GravityWire.write(new DataOutputStream(bytes), state);
            assertEquals("0" + direction.ordinal() + "4028000000000000", HexFormat.of().formatHex(bytes.toByteArray()));
            assertEquals(state, GravityWire.read(new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()))));
        }
        assertThrows(IllegalArgumentException.class,
                () -> GravityWire.read(new DataInputStream(new ByteArrayInputStream(HexFormat.of().parseHex("064028000000000000")))));
        assertThrows(EOFException.class,
                () -> GravityWire.read(new DataInputStream(new ByteArrayInputStream(new byte[] {0}))));
    }

    @Test
    void pendingStateAppliesAfterSpawnAndReappliesToReplacementButNeverAnotherIdentity() {
        var key = new GravityIdentity("demo:a", 7, UUID.randomUUID());
        var entities = new HashMap<GravityIdentity, Object>();
        var applied = new IdentityHashMap<Object, GravityState>();
        var cache = new GravityStateCache<>(entities::get, applied::put);
        var state = new GravityState(GravityDirection.WEST, 12);
        cache.receive(key, state);
        Object wrong = new Object();
        entities.put(new GravityIdentity("demo:a", 7, UUID.randomUUID()), wrong);
        entities.put(new GravityIdentity("demo:b", 7, key.uuid()), wrong);
        cache.tick();
        assertTrue(applied.isEmpty());
        Object first = new Object();
        entities.put(key, first);
        cache.tick();
        assertEquals(state, applied.get(first));
        Object replacement = new Object();
        entities.put(key, replacement);
        cache.tick();
        assertEquals(state, applied.get(replacement));
        assertFalse(applied.containsKey(wrong));
        cache.clear(applied::put);
        assertEquals(GravityState.DEFAULT, applied.get(replacement));
    }

    @Test
    void expiredAndEvictedUpdatesCannotAffectEntitiesThatAppearLater() {
        var clock = new AtomicLong();
        var entities = new HashMap<GravityIdentity, Object>();
        var applied = new IdentityHashMap<Object, GravityState>();
        var cache = new GravityStateCache<>(entities::get, applied::put, clock::get);
        var oldest = new GravityIdentity("demo:a", 0, UUID.randomUUID());
        cache.receive(oldest, GravityState.DEFAULT);
        for (int i = 1; i <= 4096; i++)
            cache.receive(new GravityIdentity("demo:a", i, UUID.randomUUID()), GravityState.DEFAULT);
        entities.put(oldest, new Object());
        cache.tick();
        assertTrue(applied.isEmpty());
        var pending = new GravityIdentity("demo:a", 5000, UUID.randomUUID());
        cache.receive(pending, GravityState.DEFAULT);
        clock.set(60_000_000_001L);
        entities.put(pending, new Object());
        cache.tick();
        assertTrue(applied.isEmpty());
    }

    @Test
    void respawnStateChangesAndPeriodicRefreshStillTriggerPublication() {
        var key = new GravityIdentity("demo:a", 7, UUID.randomUUID());
        var sync = new GravitySync();
        assertTrue(sync.shouldSend(key, GravityState.DEFAULT, 0, false));
        assertFalse(sync.shouldSend(key, GravityState.DEFAULT, 1, false));
        assertTrue(sync.shouldSend(key, new GravityState(GravityDirection.EAST), 2, false));
        assertTrue(sync.shouldSend(key, new GravityState(GravityDirection.EAST), 3, true));
        assertFalse(sync.shouldSend(key, new GravityState(GravityDirection.EAST), 102, false));
        assertTrue(sync.shouldSend(key, new GravityState(GravityDirection.EAST), 103, false));
        sync.clear();
        assertTrue(sync.shouldSend(key, new GravityState(GravityDirection.EAST), 104, false));
        sync.expire(305);
        assertTrue(sync.shouldSend(key, new GravityState(GravityDirection.EAST), 305, false));
    }

    @Test
    void stateAlreadyIncludedInACorrectionDoesNotNeedASecondPacket() {
        var key = new GravityIdentity("demo:a", 7, UUID.randomUUID());
        var state = new GravityState(GravityDirection.NORTH, 12);
        var sync = new GravitySync();
        assertTrue(sync.recordSent(key, state, 10));
        assertFalse(sync.shouldSend(key, state, 12, false));
        assertTrue(sync.shouldSend(key, state, 14, true));
        assertTrue(sync.shouldSend(key, new GravityState(GravityDirection.WEST, 12), 16, false));
        assertFalse(sync.shouldSend(key, new GravityState(GravityDirection.WEST, 12), 115, false));
        assertTrue(sync.shouldSend(key, new GravityState(GravityDirection.WEST, 12), 116, false));
    }
}
