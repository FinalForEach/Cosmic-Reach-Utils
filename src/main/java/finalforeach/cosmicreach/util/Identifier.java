package finalforeach.cosmicreach.util;

import java.util.HashMap;

public class Identifier
{
	private static HashMap<String, IdentifierPrefix> allPrefixes = new HashMap<>();
	protected String namespace;
	protected String name;
	protected String id;

	protected Identifier()
	{
	}

	public static IdentifierPrefix getPrefix(String prefix)
	{
		var idPrefix = allPrefixes.get(prefix);
		if (idPrefix == null)
		{
			idPrefix = new IdentifierPrefix(prefix);
			allPrefixes.put(prefix, idPrefix);
		}
		return idPrefix;
	}

	public static Identifier of(String id)
	{
		int index = id.indexOf(':');

		if (index != id.lastIndexOf(':'))
		{
			throw new IllegalArgumentException("Malformed Identifier String: \"" + id + "\"");
		}

		if (index == -1)
		{
			return of("base", id);
		}

		return of(id.substring(0, index), id.substring(index + 1));
	}

	public static Identifier of(String namespace, String name)
	{
		return getPrefix(namespace).getID(name);
	}

	public String toPath()
	{
		return namespace + "/" + name;
	}

	public String getNamespace()
	{
		return namespace;
	}

	public String getName()
	{
		return name;
	}

	protected Identifier set(String id)
	{
		int index = id.indexOf(':');

		if (index != id.lastIndexOf(':'))
		{
			throw new IllegalArgumentException("Malformed Identifier String: \"" + id + "\"");
		}

		if (index == -1)
		{
			return set("base", id);
		}

		return set(id.substring(0, index), id.substring(index + 1));
	}

	protected Identifier set(String namespace, String name)
	{
		this.namespace = namespace != null ? namespace : "base";
		this.name = name;
		this.id = namespace + ":" + name;
		return this;
	}

	@Override
	public String toString()
	{
		return id;
	}

	@Override
	public int hashCode()
	{
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Identifier other = (Identifier) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}