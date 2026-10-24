# Gravity Changer — Minecraft 26.2

[English](README.md) · [Français](README.fr.md)

Bibliothèque extraite d’[Immersive Portals Minestom](https://github.com/N4TH4NOT/immersive-portals-minestom/blob/port-26.2/README.fr.md) : physique Minestom autonome, états/géométrie communs et adaptateur Fabric pour Gravity Changer de NightEpiphany. Java 25 requis ; versions dans [gradle.properties](gradle.properties).

## Compilation

```sh
./gradlew build
```

Les JAR sont produits dans `build/libs/` de chaque module. `gravity-api` et `gravity-minestom` sont des bibliothèques Java ; `gravity-fabric` est un adaptateur Fabric qui embarque l’API. Les [artefacts CI](https://github.com/N4TH4NOT/gravity-changer/actions) sont également disponibles. Aucune publication Maven Central n’est supposée.

## Installation et utilisation Fabric

Installer [Gravity Changer Unofficial Port pour 26.2](https://modrinth.com/mod/gravity-changer-unofficial-port/version/vpktEkWn), Fabric API et Cloth Config. Immersive Portals embarque déjà cet adaptateur, son API et Cloth Config. Pour une autre intégration, installer `gravity-fabric` séparément. Le JAR du mod amont n’est pas redistribué ici.

En multijoueur Fabric, installer aussi les mods requis sur le serveur. Avec Minestom, les mods Fabric s’installent côté client ; intégrer la bibliothèque Java au serveur.

```mcfunction
/attribute @s gravity_changer:gravity_direction base set 2
/attribute @s gravity_changer:gravity_strength base set 12
/give @s gravity_changer:gravity_changer_north
/portal toggle teleportChangesGravity
```

Ces exemples orientent la gravité vers le nord, règlent l’intensité, donnent un changeur créatif et basculent la rotation de gravité du portail ciblé. Les valeurs de direction et les autres objets figurent dans le [mod amont](https://github.com/NightEpiphany/GravityChanger/blob/de77cc3bf140343273c9de76d3fa18375949d0df/README.md#commands-262). Ces commandes de mod ne s’exécutent pas sur Minestom.

## Intégration

- [Dépendances Minestom, PortalService et physique autonome](docs/gravity-26.2.fr.md).
- [Responsabilités client/serveur et limites](docs/rapport-gravite-minestom.fr.md).
- [Intégration des portails et essais en jeu](https://github.com/N4TH4NOT/immersive-portals-minestom/blob/port-26.2/docs/port-26.2.fr.md).

Les bibliothèques ne dépendent pas du SDK de portails. Les noms Java publics, les octets du pont v2 et `teleportChangesGravity` restent compatibles. `compat-tests/` est compilé par le dépôt de portails contre son SDK courant.

## Références et licences

- [Immersive Portals](https://github.com/iPortalTeam/ImmersivePortalsMod), qouteall et contributeurs ; [Minestom](https://github.com/Minestom/Minestom) et sa [documentation](https://minestom.net/docs/).
- [Sources 26.2](https://github.com/NightEpiphany/GravityChanger/tree/de77cc3bf140343273c9de76d3fa18375949d0df) de NightEpiphany, dont [RotationUtil](https://github.com/NightEpiphany/GravityChanger/blob/de77cc3bf140343273c9de76d3fa18375949d0df/src/main/java/com/moigferdsrte/gravitychanger/util/RotationUtil.java) ; références historiques [Gravity Changer fork](https://modrinth.com/mod/gravity-api-fork) et [Gravity API](https://modrinth.com/mod/gravity-api).
- [Fabric](https://fabricmc.net/use/), [Fabric API](https://modrinth.com/mod/fabric-api), [Cloth Config](https://modrinth.com/mod/cloth-config), [Mod Menu](https://modrinth.com/mod/modmenu) ; intégrations de rendu des portails : [Sodium](https://modrinth.com/mod/sodium), [Iris](https://modrinth.com/mod/iris).

Code sous [Apache-2.0](LICENSE) ; les transformations adaptées conservent leur [notice MIT](licenses/GravityChanger-MIT.txt). Attributions dans [NOTICE](NOTICE).
