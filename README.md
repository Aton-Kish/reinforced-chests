![Reinforced Chests](./images/header.png)

[![Mod Loader: Fabric](https://img.shields.io/static/v1?label=modloader&message=fabric&color=brightgreen)](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
![Mod Environment](https://img.shields.io/static/v1?label=environment&message=client%2Fserver&color=yellow)
[![Downloads](https://raw.githubusercontent.com/Aton-Kish/mcmod-stats/main/reinforced-chests/downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/reinforced-chests)
[![MIT License](https://img.shields.io/static/v1?label=licence&message=MIT&color=blue)](./LICENSE)
[![build](https://github.com/Aton-Kish/reinforced-chests/actions/workflows/build.yaml/badge.svg?branch=1.17)](https://github.com/Aton-Kish/reinforced-chests/actions/workflows/build.yaml?query=branch:1.17)

# Reinforced Chests

The Reinforced Chests mod adds reinforced chests.

[<img alt="Requires Fabric API" src="https://i.imgur.com/Ol1Tcf8.png" width="128"/>](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

## Reinforced Storage Mod Series

- [Reinforced Shulker Boxes](https://github.com/Aton-Kish/reinforced-shulker-boxes)
- [Reinforced Barrels](https://github.com/Aton-Kish/reinforced-barrels)

## Recipe

| Name            | Type            | Ingredients                     | Recipe                                                                                               | Description                                                                                                                                                     |
| --------------- | --------------- | ------------------------------- | ---------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Copper Chest    | Shaped Crafting | Chest + Copper Ingot            | <img alt="Copper Chest Recipe" src="./images/recipes/copper_chest.png" width="256" />                | A copper chest has 45 slots of inventory space, and a large copper chest has twice that amount, at 90 slots.                                                    |
| Iron Chest      | Shaped Crafting | Copper Chest + Iron Ingot       | <img alt="Iron Chest Recipe" src="./images/recipes/iron_chest.png" width="256" />                    | An iron chest has 54 slots of inventory space, and a large iron chest has twice that amount, at 108 slots.                                                      |
| Gold Chest      | Shaped Crafting | Iron Chest + Gold Ingot         | <img alt="Gold Chest Recipe" src="./images/recipes/gold_chest.png" width="256" />                    | A gold chest has 81 slots of inventory space, and a large gold chest has twice that amount, at 162 slots.                                                       |
| Diamond Chest   | Shaped Crafting | Gold Chest + Diamond            | <img alt="Diamond Chest Recipe" src="./images/recipes/diamond_chest.png" width="256" />              | A diamond chest has 108 slots of inventory space, and a large diamond chest has twice that amount, at 216 slots.                                                |
| Netherite Chest | Smithing        | Diamond Chest + Netherite Ingot | <img alt="Netherite Chest Recipe" src="./images/recipes/netherite_chest_smithing.png" width="256" /> | A netherite chest has 108 slots of inventory space, and a large netherite chest has twice that amount, at 216 slots. This is resistant to blast, fire and lava. |

## Configure

[The Reinforced Core lib](https://github.com/Aton-Kish/reinforced-core) has been integrated with [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu) since version 3.0.0.

![Mod Menu](./images/modmenu/modmenu.png)

### Screen Type

_Available in Reinforced Chests mod version 2.1.0+._

Screen type is `SINGLE` or `SCROLL`. (default: `SINGLE`)

| `SINGLE` screen                               | `SCROLL` screen                                |
| --------------------------------------------- | ---------------------------------------------- |
| ![Single Screen](./images/modmenu/single.png) | ![Scroll Screen](./images/modmenu/scroll6.png) |

### Scroll Screen

#### Rows

_Available in Reinforced Chests mod version 2.1.0+._

Rows is an integer in the range from `6` to `9`. (default: `6`)

| Rows: `6`                                              | Rows: `9`                                              |
| ------------------------------------------------------ | ------------------------------------------------------ |
| ![Scroll Screen: 6 rows](./images/modmenu/scroll6.png) | ![Scroll Screen: 9 rows](./images/modmenu/scroll9.png) |

## License

The Reinforced Chests mod is licensed under the MIT License, see [LICENSE](./LICENSE).
