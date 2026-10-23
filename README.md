# Gravity Changer — Minecraft 26.2

Bibliothèque de gravité extraite d’[Immersive Portals Minestom](https://github.com/N4TH4NOT/immersive-portals-minestom/tree/port-26.2) : moteur Minestom, API commune et adaptateur Fabric pour **Gravity Changer Unofficial Port** de NightEpiphany.

Ce dépôt fournit les états, la physique serveur et l’intégration avec les portails. Le mod amont fournit les déplacements, la caméra et les objets de gravité du joueur sur Minecraft Fabric. Son JAR se télécharge séparément ; il n’est pas redistribué ici.

## Versions et modules

| Composant | Version utilisée |
| --- | --- |
| Minecraft | **26.2** |
| Java | **25** |
| Fabric Loader | **0.20.1** |
| Fabric API | **0.161.0+26.2** |
| Gravity Changer Unofficial Port | **1.5.2-beta.5-mc26.2** ([publication exacte](https://modrinth.com/mod/gravity-changer-unofficial-port/version/vpktEkWn)) |
| Cloth Config | **26.2.155+fabric** |
| Minestom | **2026.10.07-26.2** |
| Cette bibliothèque | **1.0.0-alpha.1** |

| Module | Responsabilité | Dépendance aux portails |
| --- | --- | --- |
| [`gravity-api`](gravity-api) | Six référentiels, direction/intensité, géométrie immuable, codec et caches bornés ; sans Minecraft ni Minestom. | Aucune |
| [`gravity-minestom`](gravity-minestom) | État autoritaire, yeux/regard, boîtes orientées et physique passive. | Aucune |
| [`gravity-fabric`](gravity-fabric) | Adaptateur du mod amont et gestion de son animation lors d’une traversée ; embarque l’API. | Aucune |
| [`compat-tests`](compat-tests) | Tests du protocole, traversées et interactions, exemple Minestom et scénarios en jeu. | Compilés par le dépôt de portails |

## Compiler et récupérer les JAR

Avec un JDK 25 :

```sh
git clone https://github.com/N4TH4NOT/gravity-changer.git
cd gravity-changer
./gradlew build
```

Le wrapper fournit Gradle **9.8.0** ; le module Fabric utilise Loom **1.19.2**. Sous Windows, utiliser `gradlew.bat`.

| Fichier produit | Usage |
| --- | --- |
| `gravity-fabric/build/libs/gravity-fabric-1.0.0-alpha.1.jar` | Adaptateur Fabric ; son API est déjà embarquée. |
| `gravity-minestom/build/libs/gravity-minestom-1.0.0-alpha.1.jar` | Bibliothèque Java pour le serveur Minestom, avec `gravity-api` comme dépendance transitive. |
| `gravity-api/build/libs/gravity-api-1.0.0-alpha.1.jar` | Types et calculs communs. |

Les JAR `-sources` contiennent les sources. Les builds GitHub Actions sont accessibles dans les [artefacts de la CI](https://github.com/N4TH4NOT/gravity-changer/actions), après une exécution réussie. Les modules Minestom/API sont des bibliothèques Java : leur intégration se fait avec Gradle ou Maven.

## Installer et utiliser le mod sur Fabric

### Installation avec Immersive Portals

1. Installer Minecraft **26.2**, Java 25 et Fabric Loader **0.20.1**.
2. Placer **Fabric API**, **Immersive Portals pour 26.2** et **Gravity Changer Unofficial Port 1.5.2-beta.5-mc26.2** dans `mods/`.
3. Fournir **Cloth Config** : la version actuelle d’Immersive Portals l’embarque déjà. Pour une installation du mod amont sans les portails, installer Cloth Config séparément. Mod Menu est optionnel.
4. En multijoueur Fabric, installer également les mods et dépendances nécessaires sur le serveur Fabric.

Le JAR d’Immersive Portals embarque déjà `gravity-fabric` et `gravity-api` : conserver cette copie intégrée. Pour utiliser l’adaptateur séparément dans une autre intégration Fabric, installer le JAR `gravity-fabric` et ses dépendances ; la gravité directionnelle du joueur reste fournie par le mod amont.

Sur un serveur **Minestom**, les JAR Fabric s’installent sur le **client Minecraft**. Le serveur utilise les bibliothèques Java décrites plus bas. Un client sans Gravity Changer conserve sa gravité normale ; le pont refuse les traversées qui nécessitent une autre gravité.

### Commandes Minecraft 26.2

Sur Fabric, avec les permissions de commande appropriées, changer les attributs du joueur :

```mcfunction
/attribute @s gravity_changer:gravity_direction base set 2
/attribute @s gravity_changer:gravity_strength base set 12
```

La première commande fait tomber le joueur vers le nord. La seconde règle l’intensité.

| Valeur de direction | Direction | Axe du monde |
| --- | --- | --- |
| `0` | `DOWN` — bas | `-Y` |
| `1` | `UP` — haut | `+Y` |
| `2` | `NORTH` — nord | `-Z` |
| `3` | `SOUTH` — sud | `+Z` |
| `4` | `WEST` — ouest | `-X` |
| `5` | `EAST` — est | `+X` |

L’intensité par défaut est **9.8**, dans **[5, 15]**. Elle multiplie l’accélération native par `intensité / 9.8`. Pour consulter puis rétablir les valeurs de base :

```mcfunction
/attribute @s gravity_changer:gravity_direction base get
/attribute @s gravity_changer:gravity_strength base get
/attribute @s gravity_changer:gravity_direction base set 0
/attribute @s gravity_changer:gravity_strength base set 9.8
```

Ces commandes concernent le serveur Fabric et les attributs du mod amont. Sur Minestom, piloter les états avec l’API ci-dessous ; les registres et commandes ajoutés par le mod Fabric ne sont pas chargés sur le serveur Minestom.

### Objets et traversées

En créatif, un objet directionnel du mod amont s’obtient par exemple avec :

```mcfunction
/give @s gravity_changer:gravity_changer_north
```

L’utiliser avec un clic droit pour changer sa gravité. Les variantes `down`, `up`, `south`, `west` et `east` ciblent les autres directions. Le mod amont fournit aussi des ancres qui imposent une direction lorsqu’elles sont tenues et des objets de test de zone/intensité ; les changeurs sont réservés au créatif dans la version ciblée.

Dans Immersive Portals, en ciblant un portail avec les permissions requises :

```mcfunction
/portal toggle teleportChangesGravity
```

Cette commande **bascule** l’option. Lorsqu’elle est active, la rotation du portail transforme la direction de gravité ; l’intensité est conservée. L’option NBT est `teleportChangesGravity`. L’animation de gravité s’aligne immédiatement pendant la traversée pour laisser le portail gérer son interpolation ; les changements ordinaires conservent l’animation du mod.

## Intégrer la gravité dans Minestom

### Dépendances sans publication Maven préalable

Depuis un projet serveur placé à côté de ce dépôt, ajouter à `settings.gradle` :

```groovy
includeBuild('../gravity-changer')
```

Puis dans `build.gradle` :

```groovy
plugins { id 'java' }
java { toolchain.languageVersion = JavaLanguageVersion.of(25) }
repositories { mavenCentral() }
dependencies {
    implementation 'net.minestom:minestom:2026.10.07-26.2'
    implementation 'io.github.n4th4not.gravity:gravity-minestom:1.0.0-alpha.1'
}
```

Le composite Gradle résout les modules à partir des sources locales. Autre possibilité : exécuter `./gradlew publishToMavenLocal` dans ce dépôt puis ajouter `mavenLocal()` aux dépôts du serveur. Les coordonnées ci-dessus ne supposent aucune publication sur Maven Central.

Pour utiliser également les portails, récupérer leur dépôt et son sous-module :

```sh
git clone --recurse-submodules --branch port-26.2 https://github.com/N4TH4NOT/immersive-portals-minestom.git
```

Dans les paramètres du projet serveur, utiliser alors `includeBuild('../immersive-portals-minestom')`, et la dépendance `implementation 'io.github.n4th4not:minestom:7.0.0-alpha.1'`. Le SDK réexporte les modules de gravité et son protocole ; son sous-module épingle les sources de ce dépôt. Le [projet d’exemple complet](https://github.com/N4TH4NOT/immersive-portals-minestom/tree/port-26.2/minestom-example) montre l’initialisation du serveur.

### Avec `PortalService` : négociation et synchronisation incluses

Après `MinecraftServer.init()`, créer le service. `source` et `destination` désignent deux instances créées par votre serveur ; `create` les enregistre auprès du pont.

```java
import io.github.n4th4not.immersiveportals.minestom.PortalOptions;
import io.github.n4th4not.immersiveportals.minestom.PortalService;
import io.github.n4th4not.immersiveportals.protocol.GravityDirection;
import io.github.n4th4not.immersiveportals.protocol.GravityState;
import io.github.n4th4not.immersiveportals.protocol.Quaternion;
import io.github.n4th4not.immersiveportals.protocol.Vec3;

PortalService portals = new PortalService();
var portal = portals.create(source, destination, builder -> builder
        .origin(new Vec3(0, 72, 0))
        .destination(new Vec3(20, 80, 0))
        .size(4, 4)
        .rotation(Quaternion.axisAngle(new Vec3(1, 0, 0), Math.PI / 2))
        .extraNbt(PortalOptions.builder().changesGravity(true).build().encode()));

// À exécuter sur le thread de l’entité, après la négociation du pont.
if (portals.supportsGravity(player)) {
    portals.gravity().set(player, new GravityState(GravityDirection.NORTH, 12));
}

portals.gravity().set(armorStand, GravityDirection.EAST);
Vec3 eyes = portals.gravity().eyePosition(armorStand);
Vec3 look = portals.gravity().lookDirection(armorStand);
boolean grounded = portals.gravity().isOnGround(armorStand);
portal.teleport(armorStand);
portals.gravity().reset(armorStand);

// À la fermeture du service : désenregistre les tâches/événements et restaure la gravité.
portals.close();
```

`player` et `armorStand` sont des entités déjà créées et placées dans une instance. Attendre la fin de la négociation : la présence du joueur dans `PlayerSpawnEvent` ne garantit pas encore `supportsGravity(player)`. Une affectation non standard à un joueur sans capacité négociée lève `IllegalStateException`.

`PortalService` gère déjà les ticks, la synchronisation et le nettoyage : utiliser son instance `gravity()` pendant toute sa durée de vie. Les états des entités visibles sont transmis dans le monde courant et dans les vues distantes. Les messages identifient le monde, l’ID numérique et l’UUID ; un état reçu avant l’apparition attend la bonne entité.

Sans `.changesGravity(true)`, la traversée conserve la direction actuelle. La position des yeux, le regard et la vitesse sont transformés avec le portail ; l’intensité est conservée. Une traversée rejetée ou un transfert annulé restaure les états concernés. Les exemples et tests intégrés se trouvent dans [`compat-tests/minestom`](compat-tests/minestom).

### Sans le SDK de portails : physique serveur autonome

Le moteur peut servir seul aux entités passives. Après l’initialisation de Minestom :

```java
import io.github.n4th4not.immersiveportals.minestom.PortalGravity;
import io.github.n4th4not.immersiveportals.protocol.GravityDirection;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityTickEvent;
import net.minestom.server.timer.TaskSchedule;

// Ce moteur autonome n’a pas de pont client : aucun joueur ne négocie sa capacité ici.
var gravity = new PortalGravity(player -> false);
var events = EventNode.all("game-gravity");
events.addListener(EntityTickEvent.class, event -> gravity.tick(event.getEntity()));
MinecraftServer.getGlobalEventHandler().addChild(events);
var cleanup = MinecraftServer.getSchedulerManager()
        .buildTask(gravity::cleanup).repeat(TaskSchedule.tick(20)).schedule();

gravity.set(armorStand, GravityDirection.EAST);

// À la fermeture de votre système :
cleanup.cancel();
MinecraftServer.getGlobalEventHandler().removeChild(events);
gravity.close();
```

Le prédicat du constructeur vérifie une capacité client ; il ne crée aucun protocole réseau. Pour les joueurs, l’affichage orienté et la synchronisation des vues, utiliser `PortalService` ou implémenter un pont client complet avant d’accepter cette capacité.

Le moteur conserve les collisions balayées de Minestom et applique accélération/frottements dans le référentiel local. `reset` et `close` restaurent les paramètres aérodynamiques et les boîtes pris en charge. Il conserve `NoGravity`, utilise l’échelle native et laisse les mouvements des joueurs à leur client. Le pathfinding mural, les dégâts, les véhicules et les contrôleurs de poses particulières relèvent du jeu serveur.

## Contrat de compatibilité avec Immersive Portals

Les noms publics historiques sont conservés :

- `io.github.n4th4not.immersiveportals.protocol.{GravityDirection,GravityState,Vec3,Quaternion}` ;
- `io.github.n4th4not.immersiveportals.minestom.PortalGravity` et `PortalService.gravity()` ;
- les noms des classes d’animation et du mixin existants.

Le pont conserve le canal `imm_ptl:bridge`, sa version **2**, le bit de capacité **1**, les identifiants de messages et leur ordre de champs. Le contenu direction/intensité reste un octet de direction suivi d’un double, soit neuf octets. L’option NBT `teleportChangesGravity` garde son sens. L’identifiant Fabric de l’adaptateur reste `immersive_portals_gravity`.

Les façades publiques et les raccordements aux mondes/collisions/caméras restent dans le dépôt de portails. Les implémentations de gravité appartiennent ici ; les tests intégrés sont compilés par le dépôt consommateur, sans dépendance circulaire des bibliothèques.

## Validation et développement conjoint

```sh
# Dans gravity-changer : tests autonomes et JAR.
./gradlew build

# Dans immersive-portals-minestom : sources épinglées et tests de compatibilité.
git submodule update --init --recursive
./gradlew build validateAccessWidener -PportalSmoke compileSmokeTestJava

# Variante pour modifier deux clones voisins :
./gradlew -PgravitySourceDir=../gravity-changer build validateAccessWidener \
  -PportalSmoke compileSmokeTestJava
```

La validation de l’extraction comprend **42 tests** dans les deux projets, le contrôle des signatures publiques et la comparaison des trames antérieures octet pour octet. Les parcours en jeu ont été exécutés sous Vulkan, puis OpenGL avec Sodium/Iris, avec et sans le mod de gravité. Les essais graphiques utilisent Mesa llvmpipe ; ils ne couvrent pas toutes les cartes physiques ni tous les shaderpacks.

Le [guide détaillé](docs/gravity-26.2.md#validation-reproductible) donne les commandes pour lancer ensemble le serveur d’exemple Minestom et le client de validation ; le [rapport Minestom](docs/rapport-gravite-minestom.md) détaille la physique, les responsabilités et les limites.

## Références et licences

- [Immersive Portals Minestom, branche 26.2](https://github.com/N4TH4NOT/immersive-portals-minestom/tree/port-26.2), origine de l’extraction ; [état de référence audité](https://github.com/N4TH4NOT/immersive-portals-minestom/tree/6cb59045d7559ce5cd9ec9696d8b254e6a2b950e).
- [Immersive Portals de qouteall et contributeurs](https://github.com/iPortalTeam/ImmersivePortalsMod), projet d’origine, sous Apache-2.0.
- [Gravity Changer de NightEpiphany](https://github.com/NightEpiphany/GravityChanger), [sources 26.2 examinées](https://github.com/NightEpiphany/GravityChanger/tree/de77cc3bf140343273c9de76d3fa18375949d0df), [publication Modrinth ciblée](https://modrinth.com/mod/gravity-changer-unofficial-port/version/vpktEkWn) et [commandes documentées dans cette révision](https://github.com/NightEpiphany/GravityChanger/blob/de77cc3bf140343273c9de76d3fa18375949d0df/README.md#commands-262).
- [RotationUtil adapté](https://github.com/NightEpiphany/GravityChanger/blob/de77cc3bf140343273c9de76d3fa18375949d0df/src/main/java/com/moigferdsrte/gravitychanger/util/RotationUtil.java), sous MIT. La [notice conservée](licenses/GravityChanger-MIT.txt) accompagne les JAR.
- [Gravity Changer, fork de qouteall](https://modrinth.com/mod/gravity-api-fork), et [Gravity API](https://modrinth.com/mod/gravity-api), références historiques citées par le port amont ; la dépendance utilisée ici est le port 26.2 indiqué dans le tableau.
- [Minestom](https://github.com/Minestom/Minestom) et [documentation](https://minestom.net/docs/).
- [Installation Fabric](https://fabricmc.net/use/), [Fabric API](https://modrinth.com/mod/fabric-api), [Cloth Config](https://modrinth.com/mod/cloth-config) et [Mod Menu](https://modrinth.com/mod/modmenu).
- [Sodium](https://modrinth.com/mod/sodium) et [Iris](https://modrinth.com/mod/iris), intégrations graphiques vérifiées avec les portails.
- [Commande Minecraft `attribute`](https://minecraft.wiki/w/Commands/attribute).

L’extraction et les adaptations sont sous [Apache-2.0](LICENSE), avec les attributions dans [NOTICE](NOTICE). Les noms Java historiques restent intentionnellement présents pour préserver les intégrations existantes.
