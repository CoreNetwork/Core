package us.corenetwork.core.player.commands;

import us.corenetwork.core.AbstractCoreCommand;

public abstract class BasePlayerCommand extends AbstractCoreCommand {

	public BasePlayerCommand() {
		permissionNode = "core.player.";
	}

}
