# Gravity responsibilities and limitations

[English](rapport-gravite-minestom.md) · [Français](rapport-gravite-minestom.fr.md)

| Component | Responsibility |
| --- | --- |
| Minestom library | Authoritative gravity, passive living-entity physics and transformed geometry. |
| Portal SDK | Capability negotiation, remote entity state, interaction/traversal validation and corrections. |
| Client portal mod | Remote views and traversal prediction. |
| Client Gravity Changer | Player movement, camera, body and collisions in the gravity frame. |

Player movement remains client-simulated. Applying a second server acceleration would duplicate gravity. The capability handshake is not movement validation; ordinary movement checks belong to the application.

Passive physics retains Minestom's swept collisions, transforms standing boxes and restores managed parameters on reset. Custom poses and controllers may need adaptation. Wall pathfinding, damage, jumping rules and passenger groups remain application responsibilities. Nonliving entities retain normal gravity; arbitrary-angle gravity is unsupported.

## Mixed clients

| Client | Behavior |
| --- | --- |
| Portal mod and Gravity Changer | Portal views and negotiated gravity. |
| Portal mod only | Normal-gravity portals remain usable; nonstandard player state and incompatible traversals are rejected. |
| No portal mod | No bridge/views; admission is controlled by the server application. |

A rejected traversal can briefly be predicted before correction. Decide how to present restricted routes in your game. A portal's rotation may still be allowed if the resulting gravity is normal.

Real Minestom/client scenarios exercise wall-oriented gravity, traversal, remote interactions and unsupported clients. They use Mesa llvmpipe, not a representative set of physical GPUs. See the [integration guide](gravity-26.2.md) for setup and the [portal validation guide](https://github.com/N4TH4NOT/immersive-portals-minestom/blob/port-26.2/docs/port-26.2.md) for reproducible runs.
