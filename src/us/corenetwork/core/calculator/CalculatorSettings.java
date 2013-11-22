package us.corenetwork.core.calculator;

public enum CalculatorSettings {

	ENABLED("Enabled", true),

	MESSAGE_RESULT("Messages.Result", "&aResult: &f%result%"),
	MESSAGE_INCORRECT_EXPRESSION("Messages.IncorrectExpression", "Incorrect expression.");
	protected String string;
	protected Object def;
	
	private CalculatorSettings(String string, Object def)
	{
		this.string = string;
		this.def = def;
	}

	public Integer integer()
	{
		return (Integer) CalculatorModule.instance.config.get(string, def);
	}
	
	public String string()
	{
		return (String) CalculatorModule.instance.config.get(string, def);
	}
	
	public static String getCommandDescription(String cmd, String def)
	{
		String path = "CommandDescriptions." + cmd;
		
		Object descO = CalculatorModule.instance.config.get(path);
		if (descO == null)
		{
			CalculatorModule.instance.config.set(path, "&a/chp " + cmd + " &8-&f " + def);
			CalculatorModule.instance.saveConfig();
			descO = CalculatorModule.instance.config.get(path);
		}
		
		return (String) descO;
		
	}
}
