package us.corenetwork.core.teleport.commands.tp;

import us.corenetwork.core.AbstractCoreCommand;

public abstract class BaseTpCommand extends AbstractCoreCommand {

	public BaseTpCommand() {
		permissionNode = "core.teleport.";
	}
}
