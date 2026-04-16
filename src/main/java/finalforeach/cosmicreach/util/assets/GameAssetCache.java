package finalforeach.cosmicreach.util.assets;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import com.badlogic.gdx.files.FileHandle;

public class GameAssetCache<ASSET_TYPE>
{
	public final ConcurrentHashMap<FileHandle, ASSET_TYPE> allAssetsOfType = new ConcurrentHashMap<>();
	public final Function<FileHandle, ASSET_TYPE> instantiator;
	public GameAssetCache(Function<FileHandle, ASSET_TYPE> instantiator)
	{
		this.instantiator = instantiator;
	}
}