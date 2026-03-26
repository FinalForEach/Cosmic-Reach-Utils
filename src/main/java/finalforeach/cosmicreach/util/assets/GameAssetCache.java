package finalforeach.cosmicreach.util.assets;

import java.util.HashMap;
import java.util.function.Function;

import com.badlogic.gdx.files.FileHandle;

public class GameAssetCache<ASSET_TYPE>
{
	public final HashMap<FileHandle, ASSET_TYPE> allAssetsOfType = new HashMap<>();
	public final Function<FileHandle, ASSET_TYPE> instantiator;
	public GameAssetCache(Function<FileHandle, ASSET_TYPE> instantiator)
	{
		this.instantiator = instantiator;
	}
}