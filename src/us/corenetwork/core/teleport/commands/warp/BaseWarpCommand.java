package us.corenetwork.core.teleport.commands.warp;

import us.corenetwork.core.AbstractCoreCommand;

public abstract class BaseWarpCommand extends AbstractCoreCommand {

	public BaseWarpCommand() {
		permissionNode = "core.teleport.warp.";
	}

}