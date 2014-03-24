Core
====

Essential commands and universal modules


#Modules


- [Calculator] (#Calculator)
- [Checkpoints] (#Checkpoints)
- [Core] (#Core)
- [Player] (#Player)
- [Respawn] (#Respawn)
- [Scoreboard] (#Scoreboard)
- [Teleport] (#Teleport)
- [Trapped] (#Trapped)

==

##Calculator

Command | Permission | Description
--- | --- | ---
`/calc <expression>`     | `core.calc.calc` | Evaluate simple mathematical expressions

##Checkpoints
Create custom checkpoint list, add/remove checkpoints.
Use checkpoint lists in custom obstacle courses, parkour maps, etc.

List creation:

Command | Permission | Description
--- | --- | ---
`/chp help`     | `core.checkpoints.chp.help` | List all possible `/chp` commands.
`/chp create <list_name>`     | `core.checkpoints.chp.create` | Create checkpoint in specified list.
`/chp deleteList <list_name>`     | `core.checkpoints.chp.deleteist` | Delete list of checkpoints.
`/chp move <list_name> <checkpoint_number>`     | `core.checkpoints.chp.move` | Move checkpoint to your current. position.

Usage:

Command | Permission | Description
--- | --- | ---
`/checkpoint save <player_name> <list> <ch_number>`     | `core.checkpoints.checkpoint.save` | Save player at specified checkpoint.
`/checkpoint clear <player_name>`     | `core.checkpoints.checkpoint.clear` | Clear any saved checkpoint for the player. 
`/checkpoint`     | `core.checkpoints.checkpoint.tele` | Return calling player to his last saved checkpoint.


##Core

Command | Permission | Description
--- | --- | ---
`/core help`     | `core.help` |  List all possible `/core` commands.
`/core reload`     | `core.reload` |  Reload config of all modules.
`/core tp`     | `core.tp` |  Upgrade of vanilla `/tp` command.
`/core sudo`     | `core.sudo` |  Run any command as op.


##Player

Command | Permission | Description
--- | --- | ---
`/clear`     | `core.player.clear` | Upgrade of vanilla `/clear`.

##Respawn

Command | Permission | Description
--- | --- | ---
`/rspawn`     | `core.respawn.rspawn` | Respawn player randomly.
`/togglespawn`     | `core.respawn.togglespawn` | Toggle lucky/unlucky spawn.
`/unprotect`     | `core.respawn.unprotect` | Cancel the after-respawn protection.
`/protect`     | `core.respawn.protect` | Protect player.
`/nodrop`     | `core.respawn.nodrop` | Set playerDropItem as cancelled.

##Scoreboard

Module for scoreboard management.

##Teleport

Various teleportations.

Command | Permission | Description
--- | --- | ---
`/brind <player>`     | `core.teleport.bring` | Brings player to the caller.
`/swap <player> <player>`     | `core.teleport.swap` | Swap two players.
`/swap <player> <player>`     | `core.teleport.swap` | Swap two players.
`/tp ~`     | `core.tp` | Alias for `/core tp`.

Warps:

Command | Permission | Description
--- | --- | ---
`/warp help`     | `core.teleport.bring` | List all possible `/warp` commands.
`/warp set <name>`     | `core.teleport.swap` | Create/edit warp.
`/warp delete <name>`     | `core.teleport.swap` | Delete warp.
`/warp warp <name>`     | `core.tp` | Move caller to warp.

##Trapped

Command | Permission | Description
--- | --- | ---
`/trapped`     | `core.trapped.trapped` | Teleport caller to nearby, but randomized safe location, where he can build.
