package io.github.cvronmin.railwayp.json;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import io.github.cvronmin.railwayp.Reference;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.fml.common.FMLLog;

public class CustomStationSignageDefinition {
	public static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(CustomStationSignageDefinition.class, new Deserializer())
			.registerTypeAdapter(Parameter.class, new Parameter.Deserializer())
			.registerTypeAdapter(Polygon.class, new Polygon.Deserializer())
			.registerTypeAdapter(Text.class, new Text.Deserializer()).create();

	private EnumDefinitionType type;
	private final int[] size = new int[3];
	private final Map<String, Parameter> parameter = Maps.newHashMap();
	private final Map<String, Polygon> polygons = Maps.newHashMap();
	private final Map<String, Text> texts = Maps.newHashMap();

	public EnumDefinitionType getType() {
		return type;
	}

	public int[] getSize() {
		return size;
	}

	public boolean isSizesFullyDefined() {
		return size[0] != 0 && size[1] != 0 && size[2] != 0;
	}

	public Map<String, Parameter> getParameter() {
		return parameter;
	}

	public Map<String, Polygon> getPolygons() {
		return polygons;
	}

	public Map<String, Text> getTexts() {
		return texts;
	}

	public static class Deserializer implements JsonDeserializer<CustomStationSignageDefinition> {
		static Deserializer INSTANCE = new Deserializer();

		@Override
		public CustomStationSignageDefinition deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			JsonObject jobj = json.getAsJsonObject();
			CustomStationSignageDefinition cssd = new CustomStationSignageDefinition();
			if (jobj.has("type")) {
				try {
					cssd.type = EnumDefinitionType.getTypeFormString(JsonUtils.getString(jobj, "type"));
				} catch (Exception e) {
					FMLLog.log(Reference.NAME, Level.WARN, e, "cannot read definition type");
					cssd.type = EnumDefinitionType.OTHERS;
				}
			} else
				cssd.type = EnumDefinitionType.OTHERS;
			if (jobj.has("size")) {
				try {
					JsonArray array = JsonUtils.getJsonArray(jobj, "size");
					for (int i = 0; i < cssd.size.length; i++) {
						cssd.size[i] = array.get(i).getAsInt();
					}
				} catch (Exception e) {
					FMLLog.log(Reference.NAME, Level.WARN, e, "cannot read the size in definition");
				}
			}
			if (jobj.has("parameters")) {
				JsonObject pobj = JsonUtils.getJsonObject(jobj, "parameters");
				for (Entry<String, JsonElement> entry : pobj.entrySet()) {
					cssd.parameter.put(entry.getKey(),
							(Parameter) context.deserialize(entry.getValue(), Parameter.class));
				}
			}
			try {
				JsonArray qobj = JsonUtils.getJsonArray(jobj, "polygons");
				int i = 0;
				for (JsonElement je : qobj) {
					String name;
					Polygon p = (Polygon) context.deserialize(je, Polygon.class);
					if (p.isPolygonAnnoynous())
						name = "Polygon@" + i++;
					else
						name = p.getName();
					cssd.polygons.put(name, p);
				}
			} catch (Exception e) {

			}
			if (jobj.has("texts")) {
				try {
					JsonArray qobj = JsonUtils.getJsonArray(jobj, "texts");
					int i = 0;
					for (JsonElement je : qobj) {
						String name;
						Text p = (Text) context.deserialize(je, Text.class);
						if (p.isTextAnnoynous())
							name = "Text@" + i++;
						else
							name = p.getName();
						cssd.texts.put(name, p);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			return cssd;
		}

	}

	public static enum EnumDefinitionType {
		PLATFORM_BANNER, NAME_BANNER, ROUTE_SIGNAGE, NORMAL_SIGNAGE, GROUND_SIGNAGE, OTHERS, CUSTOM;
		public static EnumDefinitionType getTypeFormString(String s) {
			return EnumDefinitionType.valueOf(s.toUpperCase(Locale.ENGLISH));
		}

		@Override
		public String toString() {
			return super.toString().toLowerCase(Locale.ENGLISH);
		}
	}

	public static class Parameter {
		private String description;
		private String type;

		public String getDescription() {
			return description;
		}

		public String getType() {
			return type;
		}

		public static class Deserializer implements JsonDeserializer<Parameter> {
			@Override
			public Parameter deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
					throws JsonParseException {
				JsonObject jobj = json.getAsJsonObject();
				Parameter p = new Parameter();
				p.type = jobj.get("type").getAsString();
				if (jobj.has("description")) {
					p.description = jobj.get("description").getAsString();
				}
				return p;
			}
		}
	}

	public static class Polygon {
		private String name;
		private float[] vertices;
		private String background;
		private String color;

		public String getName() {
			return name;
		}

		public boolean isPolygonAnnoynous() {
			return name.isEmpty();
		}

		public float[] getVertices() {
			return vertices;
		}

		public String getColor() {
			return color;
		}

		public String getBackground() {
			return background;
		}

		public boolean isNoBackground() {
			return background.isEmpty();
		}

		public boolean isBackgroundVariable() {
			return background.startsWith("@id/");
		}

		public boolean isNoColor() {
			return color.isEmpty();
		}

		public boolean isColorVariable() {
			return color.startsWith("@id/");
		}

		public boolean isColorHexColor() {
			return color.startsWith("0x") && color.length() <= 8;
		}

		public static class Deserializer implements JsonDeserializer<Polygon> {
			@Override
			public Polygon deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
					throws JsonParseException {
				JsonObject jobj = json.getAsJsonObject();
				Polygon q = new Polygon();
				if (jobj.has("name")) {
					q.name = JsonUtils.getString(jobj, "name");
				}
				try {
					JsonArray array = JsonUtils.getJsonArray(jobj, "vertices");
					q.vertices = new float[array.size()];
					for (int i = 0; i < q.vertices.length; i++) {
						q.vertices[i] = array.get(i).getAsFloat();
					}
					if ((q.vertices.length % 3) != 0) {
						FMLLog.log(Reference.NAME, Level.WARN,
								"insufficient vertices\'s coordinates! Trimming to fit 3 coordinates for each vertex...");
						q.vertices = Arrays.copyOf(q.vertices, q.vertices.length - q.vertices.length % 3);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (jobj.has("background")) {
					q.background = JsonUtils.getString(jobj, "background");
				} else {
					q.background = "";
				}
				if (jobj.has("color")) {
					q.color = JsonUtils.getString(jobj, "color");
				} else {
					q.color = "";
				}
				return q;
			}
		}
	}

	public static class Text {
		private String name;
		private String text;
		private boolean isVertical;
		private int posX, posY;
		private EnumAlign align = EnumAlign.LEFT;
		private String color;

		public String getName() {
			return name;
		}

		public boolean isTextAnnoynous() {
			return text.isEmpty();
		}

		public String getText() {
			return text;
		}

		public boolean isTextVariable() {
			return text.startsWith("@id/");
		}

		public boolean isNoText() {
			return text.isEmpty();
		}

		public boolean isVerticalText() {
			return isVertical;
		}
		
		public int getX(){return posX;}public int getY(){return posY;}
		
		public EnumAlign getAlign(){return align;}
		
		public String getColor(){return color;}
		
		public boolean isNoColor() {
			return color.isEmpty();
		}

		public boolean isColorVariable() {
			return color.startsWith("@id/");
		}

		public boolean isColorHexColor() {
			return color.startsWith("0x") && color.length() <= 8;
		}

		public static class Deserializer implements JsonDeserializer<Text> {

			@Override
			public Text deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
					throws JsonParseException {
				Text text = new Text();
				JsonObject jobj = json.getAsJsonObject();
				if (jobj.has("name")) {
					text.name = JsonUtils.getString(jobj, "name");
				}
				if (jobj.has("text")) {
					text.text = JsonUtils.getString(jobj, "text");
				}
				if (jobj.has("vertical")) {
					text.isVertical = JsonUtils.getBoolean(jobj, "vertical");
				}
				if(jobj.has("align")){
					text.align = EnumAlign.getTypeFormString(JsonUtils.getString(jobj, "align"));
				}
				if(jobj.has("x")) text.posX = JsonUtils.getInt(jobj, "x");
				if(jobj.has("y")) text.posY = JsonUtils.getInt(jobj, "y");
				if(jobj.has("color")){
					text.color = JsonUtils.getString(jobj, "color");
				}
				return text;
			}

		}
	}
	public static enum EnumAlign{
		LEFT,RIGHT,TOP,BOTTON,CENTER;
		public static EnumAlign getTypeFormString(String s) {
			return EnumAlign.valueOf(s.toUpperCase(Locale.ENGLISH));
		}

		@Override
		public String toString() {
			return super.toString().toLowerCase(Locale.ENGLISH);
		}
	}
}
