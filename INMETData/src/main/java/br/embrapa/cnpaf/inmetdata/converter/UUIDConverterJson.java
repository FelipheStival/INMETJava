package br.embrapa.cnpaf.inmetdata.converter;

import java.lang.reflect.Type;
import java.util.UUID;

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
 * <b>Converter for UUID type attributes to the Gson framework.</b>
 * </p>
 * <p>
 * To use this converter see example:
 * </p>
 * <p>
 * Gson gson = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDConverter()).create();
 * </p>
 * <br>
 * 
 * @author Sergio Lopes Jr. and Rubens de Castro Pereira.
 * @version 0.1
 * @since 03/03/2020 (creation date)
 * 
 */
public class UUIDConverterJson implements JsonSerializer<UUID>, JsonDeserializer<UUID> {

	@Override
	public JsonElement serialize(UUID uuid, Type type, JsonSerializationContext jsonSerializationContext) {
		return new JsonPrimitive(uuid.toString());
	}

	@Override
	public UUID deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		return UUID.fromString(json.getAsString());
	}
}
