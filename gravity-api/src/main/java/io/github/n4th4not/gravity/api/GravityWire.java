package io.github.n4th4not.gravity.api;

import io.github.n4th4not.immersiveportals.protocol.GravityDirection;
import io.github.n4th4not.immersiveportals.protocol.GravityState;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/** Nine-byte gravity payload, compatible with Immersive Portals bridge version 2. */
public final class GravityWire {
    private GravityWire() {}
    public static void write(DataOutput out, GravityState state) throws IOException {
        out.writeByte(state.direction().ordinal());
        out.writeDouble(state.strength());
    }
    public static GravityState read(DataInput in) throws IOException {
        return new GravityState(GravityDirection.fromId(in.readUnsignedByte()), in.readDouble());
    }
}
