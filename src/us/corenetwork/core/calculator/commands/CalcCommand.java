package us.corenetwork.core.calculator.commands;

import org.bukkit.command.CommandSender;

import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.calculator.CalculatorSettings;
import us.corenetwork.core.calculator.MathEval;

public class CalcCommand extends BaseCalcCommand {

	public CalcCommand()
	{
		desc = "Evaluate simple mathematical expressions";
		permission = "calc";
		needPlayer = false;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		String expression = concatWithSeparator(args, " ");
		MathEval math = new MathEval();
		double result = 0;

		// evaluate it!
		try
		{
			result = math.evaluate(expression);
		} catch (Exception e)
		{
			PlayerUtils.Message(CalculatorSettings.MESSAGE_INCORRECT_EXPRESSION.string(), sender);
			return;
		}

		PlayerUtils.Message(CalculatorSettings.MESSAGE_RESULT.string().replaceAll("%result%", String.valueOf(result)), sender);
	}

	private String concatWithSeparator(String[] array, String separator)
	{
		StringBuilder builder = new StringBuilder();
		for (String s : array)
		{
			builder.append(s);
		}
		return builder.toString();
	}
}
