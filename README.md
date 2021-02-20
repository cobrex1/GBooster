<!-- PROJECT SHIELDS -->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![Quality][quality-shield]][quality-url]

<!-- PROJECT LOGO -->
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
</p>

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
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

This plugin is perfect for any server wishing to provide a way of donating to their players without becoming pay to win!

### Features

* Global Minecraft Experience Boosters
* Global McMMO Experience Boosters
* Global Jobs Experience and Money Boosters
* MySQL/MariaDB support
* A Bar that shows active boosters and hides automatically when there are none active
* Support for scheduled boosters from JobsReborn
* Placeholders and the ability to create a menu to activate boosters through [DeluxeMenus](https://www.spigotmc.org/resources/deluxemenus.11734/) for example

### Built With

* [Gradle](https://gradle.org/install/)
* [OpenJDK 11](https://sourceforge.net/projects/openjdk11/)


<!-- GETTING STARTED -->
## Getting Started

To get the plugin running on your server follow these simple steps.

### Commands and Permissions

1. Permission: `gbooster.give`
 * Command: `/gbooster give <player> <boosterid> <amount>`
 * Description: Give a player a certain amount of boosters
2. Permission: `DEFAULT`
 * Command: `/gbooster use <boosterid>`
 * Description: Use a specified booster

### Configuration

```
bar-pattern: ""          This bar will be displayed if there are active boosters
default-bar-message: ""  This message will be displayed above the bar if the empty bar option is set to true
empty-bar: true          true to display the default message in the bar

storage-system: yaml     Choose between mysql or yaml

saving-time: 600         How often to save players (in seconds)


boosters:
  booster-id:
    type: jobs_money     Choose the type among jobs_xp, jobs_money, mcmmo and minecraft
    multiplier: 2
    duration: 60         (in seconds)
```

<!-- ROADMAP -->
## Roadmap

See the [open issues](https://github.com/TamrielNetwork/GBooster/issues) for a list of proposed features (and known issues).

<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

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

Leopold Meinel - [contact@tamriel.me](mailto:contact@tamriel.me) - email

Project Link: [https://github.com/TamrielNetwork/GBooster](https://github.com/TamrielNetwork/GBooster)

<!-- ACKNOWLEDGEMENTS -->
## Acknowledgements

* [README.md - othneildrew](https://github.com/othneildrew/Best-README-Template)
* [Initial Development - Manu](https://github.com/zManu27/)

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
[quality-shield]: https://img.shields.io/scrutinizer/quality/g/TamrielNetwork/GBooster?label=quality&style=for-the-badge
[quality-url]: https://scrutinizer-ci.com/g/TamrielNetwork/GBooster/reports/
