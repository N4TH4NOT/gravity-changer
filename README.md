# Gravity Changer — Minecraft 26.2

[English](README.md) · [Français](README.fr.md)

Gravity library extracted from [Immersive Portals Minestom](https://github.com/N4TH4NOT/immersive-portals-minestom/tree/port-26.2): standalone Minestom physics, shared state/geometry and a Fabric adapter for NightEpiphany's Gravity Changer. Requires Java 25; dependency pins are in [gradle.properties](gradle.properties).

## Build

```sh
./gradlew build
```

JARs are produced in each module's `build/libs/`. `gravity-api` and `gravity-minestom` are Java libraries; `gravity-fabric` is a Fabric adapter and bundles the API. [CI artifacts](https://github.com/N4TH4NOT/gravity-changer/actions) are also available. No Maven Central publication is assumed.

## Fabric installation and use

Install [Gravity Changer Unofficial Port for 26.2](https://modrinth.com/mod/gravity-changer-unofficial-port/version/vpktEkWn), Fabric API and Cloth Config. Immersive Portals already bundles this adapter, its API and Cloth Config. For another integration, install `gravity-fabric` separately. The upstream mod JAR is not redistributed here.

On Fabric multiplayer, install the required mods on the server too. On Minestom, Fabric mods belong on the client; integrate the Java library on the server.

```mcfunction
/attribute @s gravity_changer:gravity_direction base set 2
/attribute @s gravity_changer:gravity_strength base set 12
/give @s gravity_changer:gravity_changer_north
/portal toggle teleportChangesGravity
```

These examples select northward gravity, set strength, give a creative direction changer and toggle gravity rotation on the targeted portal. Direction values and other items are documented by the [upstream mod](https://github.com/NightEpiphany/GravityChanger/blob/de77cc3bf140343273c9de76d3fa18375949d0df/README.md#commands-262). These mod commands do not run on Minestom.

## Integration

- [Minestom dependency setup, PortalService and standalone physics](docs/gravity-26.2.md).
- [Client/server responsibilities and limitations](docs/rapport-gravite-minestom.md).
- [Portal integration and runtime checks](https://github.com/N4TH4NOT/immersive-portals-minestom/blob/port-26.2/docs/port-26.2.md).

The libraries do not depend on the portal SDK. Public Java names, bridge v2 bytes and `teleportChangesGravity` remain compatible. `compat-tests/` is compiled by the portal repository against its current SDK.

## References and licenses

- [Immersive Portals](https://github.com/iPortalTeam/ImmersivePortalsMod), qouteall and contributors; [Minestom](https://github.com/Minestom/Minestom) and its [documentation](https://minestom.net/docs/).
- NightEpiphany's [26.2 sources](https://github.com/NightEpiphany/GravityChanger/tree/de77cc3bf140343273c9de76d3fa18375949d0df), including [RotationUtil](https://github.com/NightEpiphany/GravityChanger/blob/de77cc3bf140343273c9de76d3fa18375949d0df/src/main/java/com/moigferdsrte/gravitychanger/util/RotationUtil.java); historical [Gravity Changer fork](https://modrinth.com/mod/gravity-api-fork) and [Gravity API](https://modrinth.com/mod/gravity-api).
- [Fabric](https://fabricmc.net/use/), [Fabric API](https://modrinth.com/mod/fabric-api), [Cloth Config](https://modrinth.com/mod/cloth-config), [Mod Menu](https://modrinth.com/mod/modmenu); portal rendering integrations: [Sodium](https://modrinth.com/mod/sodium), [Iris](https://modrinth.com/mod/iris).

Code is under [Apache-2.0](LICENSE); adapted transformations retain their [MIT notice](licenses/GravityChanger-MIT.txt). See [NOTICE](NOTICE) for attribution.
