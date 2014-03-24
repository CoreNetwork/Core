package us.corenetwork.core.checkpoints.usercommands;

import us.corenetwork.core.AbstractCoreCommand;

public abstract class BaseCheckpointUserCommand extends AbstractCoreCommand {

	public BaseCheckpointUserCommand() {
		permissionNode = "core.checkpoints.checkpoint.";
	}

}