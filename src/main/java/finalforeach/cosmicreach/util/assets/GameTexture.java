package finalforeach.cosmicreach.util.assets;

import java.util.HashMap;
import java.util.Objects;

import com.badlogic.gdx.graphics.Texture;

import finalforeach.cosmicreach.util.GameProperties;
import finalforeach.cosmicreach.util.Identifier;
import finalforeach.cosmicreach.util.Threads;

public class GameTexture
{
	private static final HashMap<Identifier, GameTexture> MAP = new HashMap<>();

	public static final GameAssetCache<Texture> TEXTURE_CACHE = new GameAssetCache<Texture>(
			f -> new Texture(f));
	Identifier id;
	Texture texture;
	
	public GameTexture(Identifier id)
	{
		this.id = id;
	}
	
	public Texture get() 
	{
		return texture;
	}

	public void set(Texture texture)
	{
		Objects.requireNonNull(texture);
		if(id != null) 
		{
			throw new RuntimeException("Cannot set texture where id is set!");
		}
		this.texture = texture;
	}

	@SuppressWarnings("deprecation")
	public static GameTexture load(String fileName) 
	{
		final var id = Identifier.of(fileName);
		
		final var cachedTex = MAP.get(id);
		if(cachedTex != null) 
		{
			return cachedTex;
		}
		
		final var tex = new GameTexture(id);
		if(GameProperties.isClient) 
		{
			Threads.runOnMainThread(() -> {
				tex.texture = GameAssetLoader.getAssetOfType(TEXTURE_CACHE, id);
			});
		}
		MAP.put(id, tex);
		return tex;
	}
	
	public Identifier getID() {
		return id;
	}

	public int getWidth()
	{
		return get().getWidth();
	}

	public int getHeight()
	{
		return get().getHeight();
	}

	public static GameTexture wrap(Texture texture)
	{
		var gt = new GameTexture(null);
		gt.texture = texture;
		return gt;
	}
	
	@Override
	public String toString()
	{
		if(id == null) 
		{
			return super.toString();
		}
		return id.toString();
	}

	public void setTexture(Texture texture)
	{
		this.texture = texture;
	}

	public static GameTexture wrappedGameTexture()
	{
		return new GameTexture(null);
	}
}
