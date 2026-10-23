package io.github.n4th4not.gravity.fabric;

import io.github.n4th4not.immersiveportals.protocol.GravityState;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

/** Platform contract and vanilla fallback; no Immersive Portals classes are referenced. */
public class GravityAdapter {
    public boolean isGravityChangerPresent() { return false; }
    public Vec3 getEyeOffset(Entity entity) { return new Vec3(0, entity.getEyeHeight(), 0); }
    public Direction getGravityDirection(Entity entity) { return Direction.DOWN; }
    public Direction getBaseGravityDirection(Entity entity) { return Direction.DOWN; }
    public GravityState getState(Entity entity) { return GravityState.DEFAULT; }
    public void applyState(Entity entity, GravityState state, boolean snapVisualRotation) {}
    public void setClientPlayerGravityDirection(Player player, Direction direction) {}
    public void setBaseGravityDirectionServer(Entity entity, Direction direction) {}
    public Quaternionf getExtraCameraRotation(Direction gravityDirection) { return null; }
    public Vec3 getWorldVelocity(Entity entity) { return entity.getDeltaMovement(); }
    public void setWorldVelocity(Entity entity, Vec3 velocity) { entity.setDeltaMovement(velocity); }
    public Vec3 transformPlayerToWorld(Direction gravity, Vec3 vector) { return vector; }
    public Vec3 transformWorldToPlayer(Direction gravity, Vec3 vector) { return vector; }
    public Direction transformDirPlayerToWorld(Direction gravity, Direction direction) { return direction; }
    public Direction transformDirWorldToPlayer(Direction gravity, Direction direction) { return direction; }
}
