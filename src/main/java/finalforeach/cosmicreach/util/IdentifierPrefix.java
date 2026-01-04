package finalforeach.cosmicreach.util;

import java.util.HashMap;

public class IdentifierPrefix
{
	private final HashMap<String, Identifier> allIDs = new HashMap<>();
	public final String prefixName;

	protected IdentifierPrefix(String prefix)
	{
		this.prefixName = prefix;
	}

	public boolean containsID(String name)
	{
		return allIDs.containsKey(name);
	}

	public Identifier getID(String name)
	{
		var id = allIDs.get(name);
		if (id != null)
		{
			return id;
		}
		id = new Identifier().set(prefixName, name);
		allIDs.put(name, id);
		return id;
	}
}