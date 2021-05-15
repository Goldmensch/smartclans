# Smartclans
![GitHub](https://img.shields.io/github/license/Goldmensch/smartclans?style=for-the-badge) ![GitHub release (latest by date)](https://img.shields.io/github/v/release/Goldmensch/smartclans?style=for-the-badge)   ![Spigotversion Info](https://img.shields.io/static/v1?label=Spigot&message=1.16.5&color=blue&style=for-the-badge) 
> **A clan plugin for Spigot that tries to combine great functionality, great extensibility and easy configuration.**

## Info
Currently, the plugin is in the **development phase** and does not yet have a published version.
 - Setup, use and more info can be found in the      
   [wiki](https://github.com/Goldmensch/smartclans/wiki).
 - The plugin is primarily programmed for 1.16.5 of spigot, all other
   versions can work but don't have to.

## Download
Downloads are available in the [release section](https://github.com/Goldmensch/smartclans/releases), you can also download it via SpigotMc. 
*(Info: SpigotMc will be added upon release)*

## Building ![DevBuild Info](https://img.shields.io/static/v1?label=Beta-Version&message=3.0-Snapshot&color=orange&style=flat-square)
Another way to get the plugin is to build it yourself from source, please note that these are not releases but developer builds, which may contain bugs or may be non-functional.

First you have to clone the plugin with the command: `git clone https://github.com/Goldmensch/smartclans.git` or download it with the button "Download Zip" and unpack it. Now open a terminal (cmd/terminal/shell) and navigate to the plugin repository (the folder you downloaded) and execute the following command in the folder (where all files are):

 - `gradlew buildProject` for Windows 
 - `./gradlew buildProject` for Unix (Linux/Mac)

Now a `build` folder with some subfolders has been created, interesting for us is the `libs` folder because a file with the extension `-all` has been created there. This file is the plugin jar. Path: `(your path to the repo)/build/libs/smartclans-verison-all.jar`

## bStats ![bStats Players](https://img.shields.io/bstats/players/10354?style=flat-square) ![bStats Servers](https://img.shields.io/bstats/servers/10354?style=flat-square)
We use bStats to get anonymous plugin information, which we need to get an overview of the requested versions/countries and adjust our updates accordingly. We ask you to send this data, but you can also deactivate it.

![enter image description here](https://bstats.org/signatures/bukkit/smartclans.svg)

## License
This project (and the wiki) are licensed under the MIT licence.
