package br.embrapa.cnpaf.inmetdata.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import org.apache.log4j.Level;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.embrapa.cnpaf.inmetdata.converter.LocalDateConverterJson;
import br.embrapa.cnpaf.inmetdata.converter.LocalDateTimeConverterJson;
import br.embrapa.cnpaf.inmetdata.converter.LocalTimeConverterJson;
import br.embrapa.cnpaf.inmetdata.converter.LogLevelConverterJson;
import br.embrapa.cnpaf.inmetdata.converter.UUIDConverterJson;

/**
 * <br>
 * <p>
 * <b>Utility class with methods for getting json converters based on the Gson framework.</b>
 * </p>
 * <br>
 * 
 * @author Sergio Lopes Jr. and Rubens Pereira de Castro.
 * @version 0.1
 * @since 03/03/2020 (creation date)
 * 
 */
public class JsonUtil {

	private static final Gson gsonConverter = new GsonBuilder()//
			.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverterJson())//
			.registerTypeAdapter(LocalDate.class, new LocalDateConverterJson())//
			.registerTypeAdapter(LocalTime.class, new LocalTimeConverterJson())//
			.registerTypeAdapter(Level.class, new LogLevelConverterJson())//
			.registerTypeAdapter(UUID.class, new UUIDConverterJson())//
			.enableComplexMapKeySerialization()//
			.create();

	private static final Gson gsonConverterWithExposeAnnotation = new GsonBuilder()//
			.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverterJson())//
			.registerTypeAdapter(LocalDate.class, new LocalDateConverterJson())//
			.registerTypeAdapter(LocalTime.class, new LocalTimeConverterJson())//
			.registerTypeAdapter(Level.class, new LogLevelConverterJson())//
			.registerTypeAdapter(UUID.class, new UUIDConverterJson())//
			.enableComplexMapKeySerialization()//
			.excludeFieldsWithoutExposeAnnotation()//
			.create();

	public static Gson getJsonConverter() {
		return gsonConverter;
	}

	public static Gson getJsonConverterWithExposeAnnotation() {
		return gsonConverterWithExposeAnnotation;
	}

}
