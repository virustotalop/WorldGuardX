# New User Guide

Thank you for installing WorldGuardX! By default, when you first install WorldGuard, most features will be disabled until you enable the ones that you want to use. Please note this is different than vanilla WorldGuard. WorldGuardX is a fork of the original WorldGuard, please do not ask sk89q or any of the WorldGuard authors for help.

## Requirements

You will need to be using CraftBukkit for your server or one of its derivatives such as Spigot, PaperSpigot or Cauldron. If you are using Minecraft Forge, you will have to use Cauldron in order to use WorldGuard. You will also need to have WorldEdit installed.

It is not possible to use WorldGuard with the plain vanilla server from Mojang.

## Installation

Installation is simple!

1. Extract the `WorldGuard.jar` file and put it into your server's `plugins` folder. If the folder does not exist yet, you can create it yourself.
2. Run the Bukkit server.
3. Look inside `plugins/WorldGuard` and configure the plugin as necessary. There are also in-game commands.

By default, only "ops" can use WorldGuard commands. If you install a permissions plugin, then you can assign fine grained permissions to your server's trusted users.

## Documentation

To learn how to use WorldGuardX, check out [the WorldGuard wiki](http://wiki.sk89q.com/wiki/WorldGuard).
Note WorldGuardX will have some features that vanilla WorldGuard does not. A complete wiki will be available in the future.

## Frequently Asked Questions

### How do I protect my spawn?

Check out the [region protection tutorial](http://wiki.sk89q.com/wiki/$%7Bproject.name%7D/Regions/Tutorial) on the wiki.

### Players can't do anything!

WorldGuardX will typically not block something without also telling the user that he or she does not have permission. Please make sure that it's not another plugin that is preventing players from interacting with the world.

Also, be aware that spawn protection is a feature of vanilla Minecraft and you must disable that in `bukkit.yml`. It only allows ops to do anything in a specified radius around the world's spawn point.

### I made a region but anyone can build in it!

1. If players get a "you can't do that here" message but they are still able to build, it's because another plugin is likely undoing the protection offered by WorldGuard.
2. If no message is being sent, make sure that there is a region where you think one is. 

### Where can I file a bug report or request a feature?

Visit our [issue tracker](https://github.com/virustotalop/WorldGuardX/issues). It's also used for WorldEdit and our other projects.

### Is WorldGuardX open source?

Yes! [View the source code on GitHub](https://github.com/virustotalop/WorldGuardX).
