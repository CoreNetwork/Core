package us.corenetwork.core.trapped.commands;

import us.corenetwork.core.AbstractCoreCommand;

public abstract class BaseTrappedCommand extends AbstractCoreCommand {

	public BaseTrappedCommand() {
		permissionNode = "core.trapped.";
	}
}
