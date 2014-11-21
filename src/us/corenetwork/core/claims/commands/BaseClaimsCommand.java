package us.corenetwork.core.claims.commands;

import us.corenetwork.core.AbstractCoreCommand;

public abstract class BaseClaimsCommand extends AbstractCoreCommand {

	public BaseClaimsCommand() {
		permissionNode = "core.claims.";
	}
}
