package us.corenetwork.core.calculator.commands;

import us.corenetwork.core.AbstractCoreCommand;

public abstract class BaseCalcCommand extends AbstractCoreCommand {

	public BaseCalcCommand() {
		permissionNode = "core.calc.";
	}
}
