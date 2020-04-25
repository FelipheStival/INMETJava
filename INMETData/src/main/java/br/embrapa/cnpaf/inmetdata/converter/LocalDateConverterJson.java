package br.embrapa.cnpaf.inmetdata.converter;

import java.lang.reflect.Type;
import java.time.LocalDate;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import br.embrapa.cnpaf.inmetdata.exception.ServiceException;
import br.embrapa.cnpaf.inmetdata.service.TimeService;
import br.embrapa.cnpaf.inmetdata.util.TimeUtil;

/**
 * <br>
 * <p>
 * <b> Converter for LocalDate type attributes to the Gson framework.</b>
 * </p>
 * <p>
 * To use this converter see example:
 * </p>
 * <p>
 * Gson gson = new GsonBuilder() .registerTypeAdapter(LocalDate.class, new LocalDateConverterJson()) .create();
 * </p>
 * <br>
 * 
 * @author Rubens de Castro Pereira and Sergio Lopes Jr.
 * @version 0.2
 * @since 03/03/2020 (creation date)
 * 
 */
public class LocalDateConverterJson implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

	@Override
	public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
		try {
//			return new JsonPrimitive(TimeService.getInstanceOf().toMillis(localDate));
			return new JsonPrimitive(TimeService.getInstanceOf().getFormatterSqlDate().format(localDate));
		} catch (ServiceException e) {
			return null;
		}
	}

	@Override
	public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		try {
//			return TimeService.getInstanceOf().toLocalDateTime(json.getAsJsonPrimitive().getAsLong()).toLocalDate();
			return TimeService.getInstanceOf().toLocalDateFromSql(json.getAsJsonPrimitive().getAsString());
		} catch (ServiceException e) {
			return null;
		}
	}
}
