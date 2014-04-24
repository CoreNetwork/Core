package us.corenetwork.core.map.admincommands;

import us.corenetwork.core.AbstractCoreCommand;

public abstract class BaseCheckpointCommand extends AbstractCoreCommand {
	
	public BaseCheckpointCommand() {
		permissionNode = "core.map.chp.";
	}

}