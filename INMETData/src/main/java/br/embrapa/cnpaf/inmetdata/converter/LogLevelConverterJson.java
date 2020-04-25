package br.embrapa.cnpaf.inmetdata.converter;

import java.lang.reflect.Type;

import org.apache.log4j.Level;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * <br>
 * <p>
 * <b>Converter for LocalTime type attributes to the Gson framework.</b>
 * </p>
 * <p>
 * To use this converter see example:
 * </p>
 * <p>
 * Gson gson = new GsonBuilder() .registerTypeAdapter(LocalDateTime.class, new LocalTimeConverter()) .create();
 * </p>
 * <br>
 * 
 * @author Rubens de Castro Pereira and Sergio Lopes Jr.
 * @version 0.2
 * @since 03/03/2020 (creation date)
 * 
 */
public class LogLevelConverterJson implements JsonSerializer<Level>, JsonDeserializer<Level> {

	@Override
	public JsonElement serialize(Level logLevel, Type type, JsonSerializationContext jsonSerializationContext) {
		return new JsonPrimitive((logLevel != null) ? logLevel.toString() : null);
	}

	@Override
	public Level deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		return Level.toLevel(json.getAsJsonPrimitive().getAsString());
	}
}
