package io.github.n4th4not.immersiveportals.minestom;

import static org.junit.jupiter.api.Assertions.*;

import io.github.n4th4not.immersiveportals.protocol.*;
import net.kyori.adventure.key.Key;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.event.*;
import net.minestom.server.event.entity.EntityTickEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.world.DimensionType;
import org.junit.jupiter.api.*;
import java.util.UUID;

/** Runs without the portal SDK on the classpath. */
class StandaloneGravityTest {
    @BeforeAll static void initialize() { MinecraftServer.init(); }
    @AfterAll static void shutdown() { MinecraftServer.stopCleanly(); }

    @Test
    void directionalPhysicsAndCleanupWorkWithoutPortalService() {
        var world = new InstanceContainer(UUID.randomUUID(), DimensionType.OVERWORLD, Key.key("test:gravity"));
        MinecraftServer.getInstanceManager().registerInstance(world);
        var events = EventNode.all("standalone-gravity");
        try (var gravity = new PortalGravity(player -> false)) {
            events.addListener(EntityTickEvent.class, event -> gravity.tick(event.getEntity()));
            MinecraftServer.getGlobalEventHandler().addChild(events);
            var entity = new LivingEntity(EntityType.ARMOR_STAND);
            entity.setInstance(world, new Pos(2, 80, 2)).join();
            var originalBox = entity.getBoundingBox();
            var originalAerodynamics = entity.getAerodynamics();
            for (int y = 79; y <= 81; y++) world.setBlock(5, y, 2, Block.STONE);
            gravity.set(entity, GravityDirection.EAST);
            for (int i = 0; i < 50; i++) entity.tick(i * 50L);
            assertTrue(entity.getPosition().x() > 4.5);
            assertTrue(entity.getPosition().x() <= 5.00001);
            assertEquals(80, entity.getPosition().y(), 1e-6);
            assertTrue(gravity.isOnGround(entity));
            gravity.reset(entity);
            assertEquals(originalAerodynamics, entity.getAerodynamics());
            assertEquals(originalBox, entity.getBoundingBox());
            assertEquals(GravityState.DEFAULT, gravity.get(entity));
        } finally {
            MinecraftServer.getGlobalEventHandler().removeChild(events);
        }
    }
}
