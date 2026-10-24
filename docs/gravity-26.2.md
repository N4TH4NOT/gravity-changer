# Minestom gravity integration

[English](gravity-26.2.md) · [Français](gravity-26.2.fr.md)

## Dependency

Use a Gradle composite; publishing with `./gradlew publishToMavenLocal` is another option.

```groovy
// settings.gradle
includeBuild('../gravity-changer')
// build.gradle
repositories { mavenCentral() }
dependencies {
    implementation 'io.github.n4th4not.gravity:gravity-minestom:1.0.0-alpha.1'
}
```

Use the Minestom version pinned in [gradle.properties](../gradle.properties). For portals, consume the [portal SDK](https://github.com/N4TH4NOT/immersive-portals-minestom/blob/port-26.2/docs/minestom.md), which re-exports gravity; do not configure a second engine.

## With PortalService

After creating and registering worlds through `PortalService`, mutate state on the entity's thread. Imports retain the historical `io.github.n4th4not.immersiveportals` packages.

```java
if (portals.supportsGravity(player)) {
    portals.gravity().set(player, new GravityState(GravityDirection.NORTH, 12));
}
portals.gravity().set(armorStand, GravityDirection.EAST);
handle.update(builder -> builder.extraNbt(
    PortalOptions.builder().changesGravity(true).build().encode()));
handle.teleport(armorStand);
```

Merge other portal options before replacing `extraNbt`. Wait for bridge negotiation before assigning nonstandard player gravity; `PlayerSpawnEvent` alone is too early. `PortalService` owns ticking, publication and cleanup. Close it at shutdown.

Portal rotation changes the nearest cardinal gravity direction only when `changesGravity` is enabled; strength is preserved. Velocity is already in world coordinates. Rejected traversal restores the authoritative state; cancelled transfers restore scale and gravity.

## Standalone physics

The capability predicate does not supply networking. Reject player capabilities until your application provides its own client bridge.

```java
var gravity = new PortalGravity(player -> false);
var events = EventNode.all("gravity");
events.addListener(EntityTickEvent.class, e -> gravity.tick(e.getEntity()));
MinecraftServer.getGlobalEventHandler().addChild(events);
var cleanup = MinecraftServer.getSchedulerManager().buildTask(gravity::cleanup)
    .repeat(TaskSchedule.tick(20)).schedule();
gravity.set(armorStand, GravityDirection.EAST);
```

At shutdown, cancel `cleanup`, remove `events` and call `gravity.close()`. `reset(entity)` restores managed physics parameters. Use `gravity.isOnGround(entity)` for support in the gravity direction; Minestom's native flag remains vertical.

## Contract and checks

Public FQNs, `PortalService.gravity()`, bridge v2 field order and `teleportChangesGravity` remain unchanged. A teleport correction already carries gravity: `GravitySync.recordSent` prevents a following identical update, while appearances and periodic refreshes remain valid.

[`PortalGravity`](../gravity-minestom/src/main/java/io/github/n4th4not/immersiveportals/minestom/PortalGravity.java) defines the physics contract. [Responsibilities and limitations](rapport-gravite-minestom.md) cover player simulation, poses and mixed clients. Reproduce in-game checks from the [portal validation guide](https://github.com/N4TH4NOT/immersive-portals-minestom/blob/port-26.2/docs/port-26.2.md#gravity).
