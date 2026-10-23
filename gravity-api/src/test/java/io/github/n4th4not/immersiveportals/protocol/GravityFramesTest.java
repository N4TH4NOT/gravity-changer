package io.github.n4th4not.immersiveportals.protocol;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class GravityFramesTest {
    @Test
    void allFramesPreserveHandednessAndInvertExactly() {
        for (var direction : GravityDirection.values()) {
            Vec3 vector = new Vec3(1.25, -2.5, 3.75);
            assertEquals(vector, direction.toLocal(direction.toWorld(vector)));
            assertEquals(vector.lengthSquared(), direction.toWorld(vector).lengthSquared(), 1e-10);
            assertVector(direction.vector(), direction.toWorld(new Vec3(0, -1, 0)));
            assertVector(
                    direction.toWorld(new Vec3(0, 0, 1)),
                    direction
                            .toWorld(new Vec3(1, 0, 0))
                            .cross(direction.toWorld(new Vec3(0, 1, 0))));
        }
        assertEquals(
                GravityDirection.NORTH,
                GravityDirection.DOWN.rotated(
                        Quaternion.axisAngle(new Vec3(1, 0, 0), Math.PI / 2)));
    }

    private static void assertVector(Vec3 expected, Vec3 actual) {
        assertEquals(expected.x(), actual.x(), 1e-10);
        assertEquals(expected.y(), actual.y(), 1e-10);
        assertEquals(expected.z(), actual.z(), 1e-10);
    }

}
