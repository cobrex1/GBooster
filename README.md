<!-- PROJECT SHIELDS -->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![Quality][quality-shield]][quality-url]

<!-- PROJECT LOGO -->
<!--suppress ALL -->
<br />
<p align="center">
  <a href="https://github.com/TamrielNetwork/GBooster">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">GBooster</h3>

  <p align="center">
    Global Boosters for Spigot and Paper
    <br />
    <a href="https://github.com/TamrielNetwork/GBooster"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/TamrielNetwork/GBooster">View Demo</a>
    ·
    <a href="https://github.com/TamrielNetwork/GBooster/issues">Report Bug</a>
    ·
    <a href="https://github.com/TamrielNetwork/GBooster/issues">Request Feature</a>
  </p>

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#description">Description</a></li>
        <li><a href="#features">Features</a></li>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#commands-and-permissions">Commands and Permissions</a></li>
      </ul>
    </li>
    <li>
      <a href="#configuration">Configuration</a>
      <ul>
        <li><a href="#configyml">config.yml</a></li>
        <li><a href="#messagesyml">messages.yml</a></li>
        <li><a href="#placeholders">Placeholders</a></li>
      </ul>
    </li>
    <li><a href="#servers-using-gbooster">Servers using GBooster</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgements">Acknowledgements</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->

## About The Project

### Description

GBooster is a Booster Plugin created to provide Global Boosters for JobsReborn, McMMO and Minecraft!

This plugin is perfect for any server wishing to receive donations from their players without becoming pay to win!

### Features

* Global Minecraft Experience Boosters
* Global McMMO Experience Boosters
* Global Jobs Experience and Money Boosters
* MySQL/MariaDB support
* A Bar that shows active boosters and hides automatically when there are none active
* Support for scheduled boosters from JobsReborn
* Placeholders and the ability to create a menu to activate boosters
  through [DeluxeMenus](https://www.spigotmc.org/resources/deluxemenus.11734/) for example
* Usage of Placeholders in BossBar

### Built With

* [Gradle 7](https://docs.gradle.org/7.4/release-notes.html)
* [OpenJDK 17](https://openjdk.java.net/projects/jdk/17/)

<!-- GETTING STARTED -->

## Getting Started

To get the plugin running on your server follow these simple steps.

### Commands and Permissions

1. Permission: `gbooster.give`

* Command: `/gbooster give <player> <boosterid> <amount>`
* Description: Give a player a certain amount of boosters

2. Permission: `gbooster.use`

* Command: `/gbooster use <boosterid>`
* Description: Use a specified booster

3. Permission: `gbooster.time`

* Command: `/gbooster time`
* Description: Shows time of booster that ends next

## Configuration

### config.yml

```
#---------------------------------------------------------------#
#   __________________                       __                 #
#  /  _____/\______   \ ____   ____  _______/  |_  ___________  #
# /   \  ___ |    |  _//  _ \ /  _ \/  ___/\   __\/ __ \_  __ \ #
# \    \_\  \|    |   (  <_> |  <_> )___ \  |  | \  ___/|  | \/ #
#  \______  /|______  /\____/ \____/____  > |__|  \___  >__|    #
#         \/        \/                  \/            \/        #
#---------------------------------------------------------------#

# This bar will be displayed in order when boosters are active
# To not display anything for specified values set them to: ""
# Set every value to "" to disable the bar.
bar-pattern:
  minecraft: "&a%minecraft%% &fExp"
  mcmmo: "&a%mcmmo%% &fMcMMO"
  jobs-xp: "&a%jobs_xp%% &fJob"
  jobs-money: "&a%jobs_money%% &e$"
  duration: " &7[&f%duration% min&7]"
  separator: " &8&mo&r "

# Choose a storage system (mysql or yaml)
storage-system: yaml

# How often the plugin should save players (time in seconds)
saving-time: 600

mysql:
  host: "localhost"
  port: 3306
  database: gbooster
  username: "gbooster"
  password: ""
  prefix: "server_"
  # prefix: "" # Use for no prefix

boosters:
  jobs_xp_2:
    # Choose from jobs_xp, jobs_money, mcmmo and minecraft
    type: jobs_xp
    # Choose a multiplier (only numbers from 1-8 -> +20% would be 1.2)
    multiplier: 2
    # Choose a duration (seconds)
    duration: 3600
  jobs_money_2:
    type: jobs_money
    multiplier: 2
    duration: 3600
  mcmmo_xp_2:
    type: mcmmo
    multiplier: 2
    duration: 3600
  minecraft_xp_2:
    type: minecraft
    # Don't use floating-point numbers for type minecraft!
    multiplier: 2
    duration: 3600
```

### messages.yml

```
cmd: "&fUsage: &b/gbooster use &3<booster>"
give-boosters: "&fYou gave %amount% %booster% to %player%"
# Comment line (#) below and uncomment the one below that to get a message!
receive-boosters: [ ]
#receive-boosters: "&aYou got &c%amount%x &b%booster%"
active-booster: "&aYou just activated a booster"
active-booster-broadcast: "&b%player%&a just activated a booster"
no-active-booster: "&cThere is no active booster!"
booster-timer: "&fNext booster ends in &b%duration%&f minutes."
no-perms: "&cYou don't have enough permissions!"
player-only: "&cThis command can only be executed by players!"
invalid-player: "&cInvalid player!"
invalid-booster: "&cID doesn't exist!"
invalid-amount: "&cInvalid amount!"
countdown-active: "&cYou can only use the same type of booster once!"
limit: "&cThe global limit has been reached!"
no-booster: "&cYou don't have a booster of this type!"
```

### Placeholders

1. Display amount of boosters from player

* Structure: `%gbooster_` + `booster_id`¹ + `%`
* ¹ Exact ID from config.yml including `_`, can be found under `boosters:`
* Example: `%gbooster_minecraft_xp_2%`

2. Display multiplier of booster id in percent

* Structure: `%gbooster_` + `booster_id`¹ + `_multiplier%`
* ¹ Exact ID from config.yml including `_`, can be found under `boosters:`
* Example: `%gbooster_minecraft_xp_2_multiplier%`

3. Display duration of booster id in minutes

* Structure: `%gbooster_` + `booster_id`¹ + `_duration%`
* ¹ Exact ID from config.yml including `_`, can be found under `boosters:`
* Example: `%gbooster_minecraft_xp_2_duration%`

4. Display time until next booster runs out

* Structure: `%gbooster_time%`

5. Usage Example

* [DeluxeMenus](https://github.com/TamrielNetwork/GBooster/blob/main/DeluxeMenus/gui_menus/booster_menu.yml)

<!-- ROADMAP -->

## Roadmap

See the [open issues](https://github.com/TamrielNetwork/GBooster/issues) for a list of proposed features (and known
issues).

<!-- CONTRIBUTING -->

## Contributing

Contributions are what make the open source community such an amazing place to be, learn, inspire, and create. Any
contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<!-- LICENSE -->

## License

Distributed under the GNU General Public License v3.0. See `LICENSE` for more information.

<!-- CONTACT -->

## Contact

Leopold Meinel - [@TamrielN](https://twitter.com/TamrielN) - Twitter

Leopold Meinel - [contact@tamriel.me](mailto:contact@tamriel.me) - eMail

Project Link - [GBooster](https://github.com/TamrielNetwork/GBooster) - GitHub

<!-- ACKNOWLEDGEMENTS -->

## Acknowledgements

* [README.md - othneildrew](https://github.com/othneildrew/Best-README-Template)
* [Initial Development - Manu](https://github.com/zManu27/)
* [McMMO & Jobs softdepend - Cobrex1](https://github.com/cobrex1/)
* [Various dependency updates - Romvnly-Gaming](https://github.com/Romvnly-Gaming)
* [BoosterActivateEvent - JustinDevB](https://github.com/JustinDevB)
* [PAPI parsing BossBar - benlmyers](https://github.com/benlmyers)

<!-- MARKDOWN LINKS & IMAGES -->

[contributors-shield]: https://img.shields.io/github/contributors-anon/TamrielNetwork/GBooster?style=for-the-badge

[contributors-url]: https://github.com/TamrielNetwork/GBooster/graphs/contributors

[forks-shield]: https://img.shields.io/github/forks/TamrielNetwork/GBooster?label=Forks&style=for-the-badge

[forks-url]: https://github.com/TamrielNetwork/GBooster/network/members

[stars-shield]: https://img.shields.io/github/stars/TamrielNetwork/GBooster?style=for-the-badge

[stars-url]: https://github.com/TamrielNetwork/GBooster/stargazers

[issues-shield]: https://img.shields.io/github/issues/TamrielNetwork/GBooster?style=for-the-badge

[issues-url]: https://github.com/TamrielNetwork/GBooster/issues

[license-shield]: https://img.shields.io/github/license/TamrielNetwork/GBooster?style=for-the-badge

[license-url]: https://github.com/TamrielNetwork/GBooster/blob/main/LICENSE

[quality-shield]: https://img.shields.io/codefactor/grade/github/TamrielNetwork/GBooster?style=for-the-badge

[quality-url]: https://www.codefactor.io/repository/github/TamrielNetwork/GBooster

