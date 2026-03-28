package finalforeach.cosmicreach.util.rendering;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Deflater;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

import finalforeach.cosmicreach.util.OSInfo;
import finalforeach.cosmicreach.util.SaveLocation;
import finalforeach.cosmicreach.util.logging.Logger;

public class Screenshots
{
	static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	static String lastDateStr;
	static int i = 1;

	public static Pixmap createScreenshotPixmap(boolean panorama, int panoramaSize)
	{
		// https://libgdx.com/wiki/graphics/taking-a-screenshot
		int x = 0;
		int y = 0;
		int w = Gdx.graphics.getBackBufferWidth();
		int h = Gdx.graphics.getBackBufferHeight();
		Pixmap pixmap;
		if (panorama || panoramaSize != -1)
		{
			int size = Math.max(w, h);
			if (panoramaSize != -1)
			{
				size = panoramaSize;
			}
			pixmap = Pixmap.createFromFrameBuffer(x, y, size, size);

		} else
		{
			pixmap = Pixmap.createFromFrameBuffer(x, y, w, h);
		}

		ByteBuffer pixels = pixmap.getPixels();

		// This loop makes sure the whole screenshot is opaque and looks exactly like
		// what the user is seeing
		int size = pixels.limit();
		for (int i = 3; i < size; i += 4)
		{
			pixels.put(i, (byte) 255);
		}
		return pixmap;
	}

	public static String getNextScreenshotFileName(String suffix)
	{
		String dateStr = dateFormat.format(new Date());
		if (dateStr.equals(lastDateStr))
		{
			dateStr += "_" + i;
			i++;
		} else
		{
			lastDateStr = dateStr;
			i = 1;
		}
		String screenshotDirLoc = SaveLocation.getScreenshotFolderLocation();
		new File(screenshotDirLoc).mkdirs();
		String screenshotFileName = screenshotDirLoc;
		screenshotFileName += "/";
		screenshotFileName += dateStr;
		if (suffix != null)
		{
			screenshotFileName += suffix;
		}
		screenshotFileName += ".png";
		return screenshotFileName;
	}

	public static String takeScreenshot(boolean panorama)
	{
		try
		{
			String screenshotFileName = getNextScreenshotFileName(null);

			var pixmap = createScreenshotPixmap(panorama, -1);
			var fh = Gdx.files.absolute(screenshotFileName);
			PixmapIO.writePNG(fh, pixmap, Deflater.DEFAULT_COMPRESSION, true);
			if (!panorama)
			{
				copyScreenshotToClipboard(pixmap);
			}
			pixmap.dispose();

			return screenshotFileName;
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	private static void copyScreenshotToClipboard(Pixmap pixmap)
	{
		if (OSInfo.isMac)
		{
			// NOPE.
			return;
		}
		try
		{
			BufferedImage img = new BufferedImage(pixmap.getWidth(), pixmap.getHeight(), BufferedImage.TYPE_INT_ARGB);
			for (int y = 0; y < pixmap.getHeight(); y++)
			{
				for (int x = 0; x < pixmap.getWidth(); x++)
				{
					int pixel = pixmap.getPixel(x, y);
					int r = (pixel >> 24) & 0xff;
					int g = (pixel >> 16) & 0xff;
					int b = (pixel >> 8) & 0xff;
					int a = (pixel) & 0xff;
					int argb = ((a & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					img.setRGB(x, pixmap.getHeight() - 1 - y, argb);
				}
			}
			var t = new TransferableImage(img);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(t, null);
		} catch (Exception ex)
		{
			Logger.error("Failed to copy screenshot to clipboard", ex);
		}
	}
}

class TransferableImage implements Transferable
{
	private final Image image;

	public TransferableImage(Image image)
	{
		this.image = image;
	}

	public DataFlavor[] getTransferDataFlavors()
	{
		return new DataFlavor[] { DataFlavor.imageFlavor };
	}

	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		return DataFlavor.imageFlavor.equals(flavor);
	}

	public Object getTransferData(DataFlavor flavor)
	{
		return image;
	}
}
