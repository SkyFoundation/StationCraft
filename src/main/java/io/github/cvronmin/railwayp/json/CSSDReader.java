package io.github.cvronmin.railwayp.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import com.google.common.collect.Maps;

import io.github.cvronmin.railwayp.Reference;

/**
 * CustomStationSignageDefinition Reader
 *
 */
public class CSSDReader {
	public static final CSSDReader INSTANCE = new CSSDReader();
	private final Map<String, CustomStationSignageDefinition> map = Maps.newHashMap();
	private CSSDReader(){}
	
	public void read(){
		File[] files = new File("/" + Reference.NAME + "/cssd/").listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".json");
			}
		});
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			String s = file.getName().replace(".json", "");
			try {
				CustomStationSignageDefinition cssd = readFromStream(new FileInputStream(file));
				map.put(s, cssd);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private CustomStationSignageDefinition readInternalCSSD(String name) throws IOException {
		InputStream stream = CSSDReader.class.getResourceAsStream("/assets/" + Reference.MODID + "/cssd/" + name + ".json");
		return readFromStream(stream);
	}
	
	private CustomStationSignageDefinition readFromStream(InputStream stream) throws IOException{
		InputStreamReader reader = new InputStreamReader(stream);
		CustomStationSignageDefinition d = CustomStationSignageDefinition.GSON.fromJson(reader, CustomStationSignageDefinition.class);
		reader.close();
		return d;
	}
	
	public CustomStationSignageDefinition getDefinition(String name){
		return map.get(name);
	}
	
	public boolean isDefinitionExist(String name){
		return map.containsKey(name);
	}
}
