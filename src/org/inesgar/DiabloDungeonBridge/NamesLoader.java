package org.inesgar.DiabloDungeonBridge;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class NamesLoader
{
	private File dataFolder;
	private final DiabloDungeonBridge plugin;

	public NamesLoader(DiabloDungeonBridge instance)
	{
		plugin = instance;
		dataFolder = instance.getDataFolder();
	}

	/**
	 * Creates a file with given name
	 * 
	 * @param name
	 */
	public void writeDefault(String name)
	{
		File actual = new File(dataFolder, name);
		if (!actual.exists())
		{
			try
			{
				InputStream input = this.getClass().getResourceAsStream(
						"/" + name);
				FileOutputStream output = new FileOutputStream(actual);
				byte[] buf = new byte[8192];
				int length = 0;
				while ((length = input.read(buf)) > 0)
				{
					output.write(buf, 0, length);
				}
				output.close();
				input.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public DiabloDungeonBridge getPlugin()
	{
		return plugin;
	}
}
