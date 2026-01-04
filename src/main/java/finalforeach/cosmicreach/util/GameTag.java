package finalforeach.cosmicreach.util;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Predicate;


public class GameTag
{
	public static final HashMap<String, GameTag> TAG_MAP = new HashMap<String, GameTag>();
	
	public final String name;
	
	private GameTag(String tagName)
	{
		this.name = tagName;
	}
	
	@Override
	public String toString()
	{
		return name;
	}

	public static GameTag get(String tagName) 
	{
		Objects.requireNonNull(tagName);
		var tag = TAG_MAP.get(tagName);
		if(tag != null) 
		{
			return tag;
		}
		synchronized (TAG_MAP)
		{
			tag = TAG_MAP.get(tagName);
			if(tag != null) 
			{
				return tag;
			}
			tag = new GameTag(tagName);
			TAG_MAP.put(tagName, tag);
			return tag;
		}
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return this == obj;
	}

	public static void findTags(IGameTagList results, Predicate<GameTag> predicate)
	{
		synchronized (TAG_MAP)
		{
			for(var tag : TAG_MAP.values()) 
			{
				if(predicate.test(tag)) 
				{
					results.add(tag);
				}
			}
		}
		
	}
}
