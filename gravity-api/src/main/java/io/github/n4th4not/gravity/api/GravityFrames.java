package io.github.n4th4not.gravity.api;

import io.github.n4th4not.immersiveportals.protocol.GravityState;
import io.github.n4th4not.immersiveportals.protocol.Quaternion;
import io.github.n4th4not.immersiveportals.protocol.Vec3;

/** Gravity geometry shared by adapters without depending on a portal class. */
public final class GravityFrames {
    private GravityFrames() {}
    public static GravityState afterRotation(GravityState before, Quaternion rotation, boolean changes) {
        return changes ? before.rotated(rotation) : before;
    }
    public static Vec3 localLookAfterRotation(GravityState after, Quaternion rotation, Vec3 worldLook) {
        return after.direction().toLocal(rotation.rotate(worldLook));
    }
    public static Vec3 feetAtEye(GravityState state, Vec3 eye, double eyeHeight) {
        return eye.subtract(state.direction().toWorld(new Vec3(0, eyeHeight, 0)));
    }
}
