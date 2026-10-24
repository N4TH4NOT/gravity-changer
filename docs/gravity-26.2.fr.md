# Intégration de la gravité dans Minestom

[English](gravity-26.2.md) · [Français](gravity-26.2.fr.md)

## Dépendance

Utiliser un composite Gradle ; la publication avec `./gradlew publishToMavenLocal` est une autre possibilité.

```groovy
// settings.gradle
includeBuild('../gravity-changer')
// build.gradle
repositories { mavenCentral() }
dependencies {
    implementation 'io.github.n4th4not.gravity:gravity-minestom:1.0.0-alpha.1'
}
```

Utiliser la version Minestom de [gradle.properties](../gradle.properties). Pour les portails, consommer le [SDK de portails](https://github.com/N4TH4NOT/immersive-portals-minestom/blob/port-26.2/docs/minestom.fr.md), qui réexporte la gravité ; ne pas créer de second moteur.

## Avec PortalService

Après création et enregistrement des mondes via `PortalService`, modifier l’état sur le thread de l’entité. Les imports conservent les packages historiques `io.github.n4th4not.immersiveportals`.

```java
if (portals.supportsGravity(player)) {
    portals.gravity().set(player, new GravityState(GravityDirection.NORTH, 12));
}
portals.gravity().set(armorStand, GravityDirection.EAST);
handle.update(builder -> builder.extraNbt(
    PortalOptions.builder().changesGravity(true).build().encode()));
handle.teleport(armorStand);
```

Fusionner les autres options du portail avant de remplacer `extraNbt`. Attendre la négociation du pont avant d’attribuer une gravité non standard au joueur ; `PlayerSpawnEvent` seul arrive trop tôt. `PortalService` gère ticks, publication et nettoyage. Le fermer à l’arrêt.

La rotation du portail transforme la gravité vers la direction cardinale la plus proche seulement si `changesGravity` est activé ; l’intensité est conservée. La vitesse est déjà exprimée dans le monde. Un refus restaure l’état autoritaire ; un transfert annulé restaure échelle et gravité.

## Physique autonome

Le prédicat de capacité ne fournit aucun réseau. Refuser la capacité des joueurs tant que l’application n’a pas son propre pont client.

```java
var gravity = new PortalGravity(player -> false);
var events = EventNode.all("gravity");
events.addListener(EntityTickEvent.class, e -> gravity.tick(e.getEntity()));
MinecraftServer.getGlobalEventHandler().addChild(events);
var cleanup = MinecraftServer.getSchedulerManager().buildTask(gravity::cleanup)
    .repeat(TaskSchedule.tick(20)).schedule();
gravity.set(armorStand, GravityDirection.EAST);
```

À l’arrêt, annuler `cleanup`, retirer `events` et appeler `gravity.close()`. `reset(entity)` restaure les paramètres physiques pris en charge. Utiliser `gravity.isOnGround(entity)` pour le support dans la direction de gravité ; l’indicateur natif Minestom reste vertical.

## Contrat et vérification

Les noms publics, `PortalService.gravity()`, l’ordre des champs du pont v2 et `teleportChangesGravity` sont inchangés. Une correction de téléportation contient déjà la gravité : `GravitySync.recordSent` évite une mise à jour identique suivante, tout en conservant apparitions et rappels périodiques.

[`PortalGravity`](../gravity-minestom/src/main/java/io/github/n4th4not/immersiveportals/minestom/PortalGravity.java) définit le contrat physique. Les [responsabilités et limites](rapport-gravite-minestom.fr.md) couvrent joueurs, poses et clients mixtes. Les essais en jeu figurent dans le [guide de validation des portails](https://github.com/N4TH4NOT/immersive-portals-minestom/blob/port-26.2/docs/port-26.2.fr.md#gravité).
