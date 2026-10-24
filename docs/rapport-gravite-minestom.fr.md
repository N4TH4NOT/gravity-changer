# Responsabilités et limites de la gravité

[English](rapport-gravite-minestom.md) · [Français](rapport-gravite-minestom.fr.md)

| Composant | Responsabilité |
| --- | --- |
| Bibliothèque Minestom | Gravité autoritaire, physique passive des entités vivantes et géométrie transformée. |
| SDK de portails | Négociation de capacité, états distants, validation des interactions/traversées et corrections. |
| Mod de portails client | Vues distantes et prédiction des traversées. |
| Gravity Changer client | Déplacements du joueur, caméra, corps et collisions dans le référentiel de gravité. |

Les déplacements des joueurs restent simulés côté client. Une seconde accélération serveur dupliquerait la gravité. La négociation de capacité ne valide pas les mouvements ; les contrôles ordinaires relèvent de l’application.

La physique passive conserve les collisions balayées de Minestom, transforme les boîtes debout et restaure les paramètres pris en charge à la réinitialisation. Les poses et contrôleurs personnalisés peuvent nécessiter une adaptation. Pathfinding mural, dégâts, règles de saut et groupes de passagers restent à la charge de l’application. Les entités non vivantes conservent leur gravité normale ; les angles arbitraires ne sont pas pris en charge.

## Clients mixtes

| Client | Comportement |
| --- | --- |
| Mods de portails et de gravité | Vues de portail et gravité négociée. |
| Mod de portails seul | Portails à gravité normale disponibles ; état joueur non standard et traversées incompatibles refusés. |
| Sans mod de portails | Aucun pont ni vue ; admission contrôlée par l’application serveur. |

Une traversée refusée peut être brièvement prédite avant correction. Adapter la présentation des chemins restreints au jeu. Une rotation de portail reste autorisée si la gravité résultante est normale.

Les scénarios réels Minestom/client couvrent gravité murale, traversées, interactions distantes et clients incompatibles. Ils utilisent Mesa llvmpipe, sans représenter l’ensemble des GPU physiques. Voir le [guide d’intégration](gravity-26.2.fr.md) et le [guide de validation des portails](https://github.com/N4TH4NOT/immersive-portals-minestom/blob/port-26.2/docs/port-26.2.fr.md) pour reproduire les essais.
