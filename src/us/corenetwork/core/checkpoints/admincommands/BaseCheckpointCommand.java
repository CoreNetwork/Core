package us.corenetwork.core.checkpoints.admincommands;

import us.corenetwork.core.AbstractCoreCommand;

public abstract class BaseCheckpointCommand extends AbstractCoreCommand {
	
	public BaseCheckpointCommand() {
		permissionNode = "core.checkpoints.chp.";
	}

}