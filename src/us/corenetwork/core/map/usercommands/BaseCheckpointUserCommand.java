package us.corenetwork.core.map.usercommands;

import us.corenetwork.core.AbstractCoreCommand;

public abstract class BaseCheckpointUserCommand extends AbstractCoreCommand {

	public BaseCheckpointUserCommand() {
		permissionNode = "core.map.checkpoint.";
	}

}