package finalforeach.cosmicreach.util;

public interface IGameTagged
{
	IGameTagList getTags();

	void initTagList();

	default void addTag(GameTag tag)
	{
		if (!hasTag(tag))
		{
			var tags = getTags();
			if (tags == null)
			{
				initTagList();
				tags = getTags();
			}
			tags.add(tag);
		}
	}

	default boolean hasAnyTag(GameTag[] tags)
	{
		for (var tag : tags)
		{
			if (hasTag(tag))
			{
				return true;
			}
		}
		return false;
	}

	default boolean hasTag(GameTag tag)
	{
		var tags = getTags();
		if (tags == null)
		{
			return false;
		}
		return tags.contains(tag);
	}

	default void removeTag(GameTag tag)
	{
		var tags = getTags();
		if (tags != null)
		{
			tags.remove(tag);
		}
	}

	default void addOrRemoveTag(GameTag tag, boolean shouldAdd)
	{
		if (shouldAdd)
		{
			addTag(tag);
		} else
		{
			removeTag(tag);
		}
	}

	default boolean toggleTag(GameTag tag)
	{
		if (hasTag(tag))
		{
			removeTag(tag);
			return false;
		} else
		{
			addTag(tag);
			return true;
		}
	}

	default void clear()
	{
		var tags = getTags();
		if (tags != null)
		{
			tags.clear();
		}
	}

	default boolean hasAny(IGameTagList tags)
	{
		for (var tag : tags)
		{
			if (hasTag(tag))
				return true;
		}
		return false;
	}
}
