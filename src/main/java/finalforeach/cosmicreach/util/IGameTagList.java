package finalforeach.cosmicreach.util;

public interface IGameTagList extends Iterable<GameTag>
{
	/**
	 * Adds the GameTag to this list.
	 * @return true if successfully added, false if already present
	 * */
	boolean add(GameTag tag);

	/**
	 * Checks if the GameTag is present in this list.
	 * @return true if present, false otherwise
	 * */
	boolean contains(GameTag tag);

	/**
	 * Removes the GameTag from this list.
	 * @return true if successfully removed, false if not present
	 * */
	boolean remove(GameTag tag);

	/**
	 * Clears all GameTags from this list.
	 * */
	void clear();
}
