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

	// FIXME: I don't like these here, but it'll do for now instead of over-engineering metadata
	public float animationFrameDuration;
	public int animationFrameCount;
	
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

	public static final Identifier FALLBACK_ID = Identifier.of("base:textures/blocks/debug.png");

	public static GameTexture load(Identifier id)
	{
		final var targetId = id == null ? FALLBACK_ID : id;

		final var cachedTex = MAP.get(targetId);
		if (cachedTex != null)
		{
			return cachedTex;
		}

		final var tex = new GameTexture(targetId);
		if (GameProperties.isClient)
		{
			Threads.runOnMainThread(() -> {
				tex.texture = GameAssetLoader.getAssetOfType(TEXTURE_CACHE, targetId);
				if (tex.texture == null && !FALLBACK_ID.equals(targetId))
				{
					tex.texture = GameAssetLoader.getAssetOfType(TEXTURE_CACHE, FALLBACK_ID);
				}
			});
		}
		MAP.put(targetId, tex);
		return tex;
	}

	public static GameTexture load(String fileName)
	{
		if (fileName == null)
		{
			return load(FALLBACK_ID);
		}
		return load(Identifier.of(fileName));
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
