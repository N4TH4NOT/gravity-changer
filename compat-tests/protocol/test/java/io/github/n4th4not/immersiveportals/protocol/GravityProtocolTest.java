package io.github.n4th4not.immersiveportals.protocol;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.UUID;

class GravityProtocolTest {
    @Test
    void completeFramesMatchThePreExtractionBridgeVersionTwo() throws java.io.IOException {
        var actual = new java.util.ArrayList<String>();
        for (var direction : GravityDirection.values()) {
            var state = new GravityState(direction, 12);
            var message = new BridgeMessage.Gravity("demo:world", 23,
                    UUID.fromString("01234567-89ab-cdef-fedc-ba9876543210"), state);
            var correction = new BridgeMessage.Correction("demo:world", Vec3.ZERO,
                    new Vec3(1, 2, 3), .5, 23, -30, state);
            actual.add(direction + " gravity " + java.util.HexFormat.of().formatHex(BridgeCodec.encode(message)));
            actual.add(direction + " correction " + java.util.HexFormat.of().formatHex(BridgeCodec.encode(correction)));
        }
        actual.add("hello " + java.util.HexFormat.of().formatHex(BridgeCodec.encode(new BridgeMessage.Hello(
                BridgeCodec.VERSION, BridgeCodec.MINECRAFT_PROTOCOL, BridgeCodec.FEATURE_GRAVITY))));
        try (var stream = getClass().getResourceAsStream("/gravity-bridge-v2.txt")) {
            assertNotNull(stream);
            var expected = new String(stream.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8).lines().toList();
            assertEquals(expected, actual);
        }
    }

    @Test
    void stateAndAtomicCorrectionsRoundTrip() {
        for (var direction : GravityDirection.values()) {
            var state = new GravityState(direction, 12);
            var message = new BridgeMessage.Gravity("demo:world", 23, UUID.randomUUID(), state);
            assertEquals(message, BridgeCodec.decode(BridgeCodec.encode(message)));
            var correction =
                    new BridgeMessage.Correction(
                            "demo:world", Vec3.ZERO, new Vec3(1, 2, 3), .5, 23, -30, state);
            assertEquals(correction, BridgeCodec.decode(BridgeCodec.encode(correction)));
        }
        var hello =
                new BridgeMessage.Hello(
                        BridgeCodec.VERSION,
                        BridgeCodec.MINECRAFT_PROTOCOL,
                        BridgeCodec.FEATURE_GRAVITY);
        assertEquals(hello, BridgeCodec.decode(BridgeCodec.encode(hello)));
    }

    @Test
    void invalidDirectionsAndStrengthsAreRejectedOnTheWire() {
        for (double invalid : new double[] {Double.NaN, Double.POSITIVE_INFINITY, 4.9, 15.1})
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new GravityState(GravityDirection.DOWN, invalid));
        byte[] encoded =
                BridgeCodec.encode(
                        new BridgeMessage.Gravity(
                                "demo:a", 1, UUID.randomUUID(), GravityState.DEFAULT));
        encoded[encoded.length - 9] = 6;
        assertThrows(IllegalArgumentException.class, () -> BridgeCodec.decode(encoded));
        assertThrows(IllegalArgumentException.class, () -> GravityDirection.fromId(-1));
    }
}
