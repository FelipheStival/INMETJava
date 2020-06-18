package br.embrapa.cnpaf.inmetdata.entity;

import java.io.Serializable;
import java.time.LocalDate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import br.embrapa.cnpaf.inmetdata.enumerate.MessageEnum;
import br.embrapa.cnpaf.inmetdata.exception.ParameterValueInvalidException;
import br.embrapa.cnpaf.inmetdata.util.ErrorUtil;
import br.embrapa.cnpaf.inmetdata.util.JsonUtil;
import br.embrapa.cnpaf.inmetdata.util.NetworkUtil;
import br.embrapa.cnpaf.inmetdata.util.StringUtil;
import br.embrapa.cnpaf.inmetdata.util.TimeUtil;

/**
 * <br>
 * <p>
 * <b>Entity containing the information of an module.</b>
 * </p>
 * <p>
 * Each module of the system is capable of controlling a soil column or a set of
 * climatic devices.
 * </p>
 * <br>
 * 
 * @author Rubens de Castro Pereira and Sergio Lopes Jr.
 * @version 0.1
 * @since 03/03/2020 (creation date)
 * 
 */
public class InmetHourlyDataEntity implements Serializable, Comparable<InmetHourlyDataEntity> {

	private static final long serialVersionUID = 1L;

	@Expose
	@SerializedName("id")
	private Long id;

	@Expose
	@SerializedName("entilyStation")
	private InmetStationEntity entilyStation;

	@Expose
	@SerializedName("DT_MEDICAO")
	private LocalDate measurementDate;

	@Expose
	@SerializedName("HR_MEDICAO")
	private String measureTime;

	@Expose
	@SerializedName("TEM_MIN")
	private float minimumTemperature;

	@Expose
	@SerializedName("TEM_MAX")
	private float maximumTemperature;

	@Expose
	@SerializedName("TEM_INS")
	private float instantTemperature;

	@Expose
	@SerializedName("PRE_MIN")
	private float minimumPrecipitation;

	@Expose
	@SerializedName("PRE_MAX")
	private float maximumPrecipitation;

	@Expose
	@SerializedName("PRE_INS")
	private float instantPrecipitation;

	@Expose
	@SerializedName("UMD_MIN")
	private float minimumRelativeAirHumidity;

	@Expose
	@SerializedName("UMD_MAX")
	private float maximumRelativeAirHumidity;

	@Expose
	@SerializedName("UMD_INS")
	private float instantRelativeAirHumidity;

	@Expose
	@SerializedName("VEN_VEL")
	private float windSpeed;

	@Expose
	@SerializedName("VEN_DIR")
	private int windDirection;

	@Expose
	@SerializedName("VEN_RAJ")
	private float blast;

	@Expose
	@SerializedName("RAD_GLO")
	private float globalRadiation;

	@Expose
	@SerializedName("PTO_MIN")
	private float minimumDewPoint;

	@Expose
	@SerializedName("PTO_MAX")
	private float maximumDewPoint;

	@Expose
	@SerializedName("PTO_INS")
	private float instantDewPoint;

	@Expose
	@SerializedName("CHUVA")
	private float rain;

	/**
	 * Public constructor without parameters.
	 * 
	 */
	public InmetHourlyDataEntity() {
		super();
	}

	/**
	 * @param id
	 * @param entilyStation
	 * @param measurementDate
	 * @param measureTime
	 * @param minimumTemperature
	 * @param maximumTemperature
	 * @param instantTemperature
	 * @param minimumPrecipitation
	 * @param maximumPrecipitation
	 * @param instantPrecipitation
	 * @param minimumRelativeAirHumidity
	 * @param maximumRelativeAirHumidity
	 * @param instantRelativeAirHumidity
	 * @param windSpeed
	 * @param windDirection
	 * @param blast
	 * @param globalRadiation
	 * @param minimumDewPoint
	 * @param maximumDewPoint
	 * @param instantDewPoint
	 * @param rain
	 */
	public InmetHourlyDataEntity(Long id, InmetStationEntity entilyStation, LocalDate measurementDate,
			String measureTime, float minimumTemperature, float maximumTemperature, float instantTemperature,
			float minimumPrecipitation, float maximumPrecipitation, float instantPrecipitation,
			float minimumRelativeAirHumidity, float maximumRelativeAirHumidity, float instantRelativeAirHumidity,
			float windSpeed, int windDirection, float blast, float globalRadiation, float minimumDewPoint,
			float maximumDewPoint, float instantDewPoint, float rain) {
		super();
		this.id = id;
		this.entilyStation = entilyStation;
		this.measurementDate = measurementDate;
		this.measureTime = measureTime;
		this.minimumTemperature = minimumTemperature;
		this.maximumTemperature = maximumTemperature;
		this.instantTemperature = instantTemperature;
		this.minimumPrecipitation = minimumPrecipitation;
		this.maximumPrecipitation = maximumPrecipitation;
		this.instantPrecipitation = instantPrecipitation;
		this.minimumRelativeAirHumidity = minimumRelativeAirHumidity;
		this.maximumRelativeAirHumidity = maximumRelativeAirHumidity;
		this.instantRelativeAirHumidity = instantRelativeAirHumidity;
		this.windSpeed = windSpeed;
		this.windDirection = windDirection;
		this.blast = blast;
		this.globalRadiation = globalRadiation;
		this.minimumDewPoint = minimumDewPoint;
		this.maximumDewPoint = maximumDewPoint;
		this.instantDewPoint = instantDewPoint;
		this.rain = rain;
	}

	/**
	 * @return the entilyStation
	 */
	public InmetStationEntity getEntilyStation() {
		return entilyStation;
	}

	/**
	 * @param entilyStation the entilyStation to set
	 */
	public void setEntilyStation(InmetStationEntity entilyStation) {
		this.entilyStation = entilyStation;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the measurementDate
	 */
	public LocalDate getMeasurementDate() {
		return measurementDate;
	}

	/**
	 * @param measurementDate the measurementDate to set
	 */
	public void setMeasurementDate(LocalDate measurementDate) {
		this.measurementDate = measurementDate;
	}

	/**
	 * @return the measureTime
	 */
	public String getMeasureTime() {
		return measureTime;
	}

	/**
	 * @param measureTime the measureTime to set
	 */
	public void setMeasureTime(String measureTime) {
		this.measureTime = measureTime;
	}

	/**
	 * @return the minimumTemperature
	 */
	public float getMinimumTemperature() {
		return minimumTemperature;
	}

	/**
	 * @param minimumTemperature the minimumTemperature to set
	 */
	public void setMinimumTemperature(float minimumTemperature) {
		this.minimumTemperature = minimumTemperature;
	}

	/**
	 * @return the maximumTemperature
	 */
	public float getMaximumTemperature() {
		return maximumTemperature;
	}

	/**
	 * @param maximumTemperature the maximumTemperature to set
	 */
	public void setMaximumTemperature(float maximumTemperature) {
		this.maximumTemperature = maximumTemperature;
	}

	/**
	 * @return the instantTemperature
	 */
	public float getInstantTemperature() {
		return instantTemperature;
	}

	/**
	 * @param instantTemperature the instantTemperature to set
	 */
	public void setInstantTemperature(float instantTemperature) {
		this.instantTemperature = instantTemperature;
	}

	/**
	 * @return the minimumPrecipitation
	 */
	public float getMinimumPrecipitation() {
		return minimumPrecipitation;
	}

	/**
	 * @param minimumPrecipitation the minimumPrecipitation to set
	 */
	public void setMinimumPrecipitation(float minimumPrecipitation) {
		this.minimumPrecipitation = minimumPrecipitation;
	}

	/**
	 * @return the maximumPrecipitation
	 */
	public float getMaximumPrecipitation() {
		return maximumPrecipitation;
	}

	/**
	 * @param maximumPrecipitation the maximumPrecipitation to set
	 */
	public void setMaximumPrecipitation(float maximumPrecipitation) {
		this.maximumPrecipitation = maximumPrecipitation;
	}

	/**
	 * @return the instantPrecipitation
	 */
	public float getInstantPrecipitation() {
		return instantPrecipitation;
	}

	/**
	 * @param instantPrecipitation the instantPrecipitation to set
	 */
	public void setInstantPrecipitation(float instantPrecipitation) {
		this.instantPrecipitation = instantPrecipitation;
	}

	/**
	 * @return the minimumRelativeAirHumidity
	 */
	public float getMinimumRelativeAirHumidity() {
		return minimumRelativeAirHumidity;
	}

	/**
	 * @param minimumRelativeAirHumidity the minimumRelativeAirHumidity to set
	 */
	public void setMinimumRelativeAirHumidity(float minimumRelativeAirHumidity) {
		this.minimumRelativeAirHumidity = minimumRelativeAirHumidity;
	}

	/**
	 * @return the maximumRelativeAirHumidity
	 */
	public float getMaximumRelativeAirHumidity() {
		return maximumRelativeAirHumidity;
	}

	/**
	 * @param maximumRelativeAirHumidity the maximumRelativeAirHumidity to set
	 */
	public void setMaximumRelativeAirHumidity(float maximumRelativeAirHumidity) {
		this.maximumRelativeAirHumidity = maximumRelativeAirHumidity;
	}

	/**
	 * @return the instantRelativeAirHumidity
	 */
	public float getInstantRelativeAirHumidity() {
		return instantRelativeAirHumidity;
	}

	/**
	 * @param instantRelativeAirHumidity the instantRelativeAirHumidity to set
	 */
	public void setInstantRelativeAirHumidity(float instantRelativeAirHumidity) {
		this.instantRelativeAirHumidity = instantRelativeAirHumidity;
	}

	/**
	 * @return the windSpeed
	 */
	public float getWindSpeed() {
		return windSpeed;
	}

	/**
	 * @param windSpeed the windSpeed to set
	 */
	public void setWindSpeed(float windSpeed) {
		this.windSpeed = windSpeed;
	}

	/**
	 * @return the windDirection
	 */
	public int getWindDirection() {
		return windDirection;
	}

	/**
	 * @param windDirection the windDirection to set
	 */
	public void setWindDirection(int windDirection) {
		this.windDirection = windDirection;
	}

	/**
	 * @return the blast
	 */
	public float getBlast() {
		return blast;
	}

	/**
	 * @param blast the blast to set
	 */
	public void setBlast(float blast) {
		this.blast = blast;
	}

	/**
	 * @return the globalRadiation
	 */
	public float getGlobalRadiation() {
		return globalRadiation;
	}

	/**
	 * @param globalRadiation the globalRadiation to set
	 */
	public void setGlobalRadiation(float globalRadiation) {
		this.globalRadiation = globalRadiation;
	}

	/**
	 * @return the minimumDewPoint
	 */
	public float getMinimumDewPoint() {
		return minimumDewPoint;
	}

	/**
	 * @param minimumDewPoint the minimumDewPoint to set
	 */
	public void setMinimumDewPoint(float minimumDewPoint) {
		this.minimumDewPoint = minimumDewPoint;
	}

	/**
	 * @return the maximumDewPoint
	 */
	public float getMaximumDewPoint() {
		return maximumDewPoint;
	}

	/**
	 * @param maximumDewPoint the maximumDewPoint to set
	 */
	public void setMaximumDewPoint(float maximumDewPoint) {
		this.maximumDewPoint = maximumDewPoint;
	}

	/**
	 * @return the instantDewPoint
	 */
	public float getInstantDewPoint() {
		return instantDewPoint;
	}

	/**
	 * @param instantDewPoint the instantDewPoint to set
	 */
	public void setInstantDewPoint(float instantDewPoint) {
		this.instantDewPoint = instantDewPoint;
	}

	/**
	 * @return the rain
	 */
	public float getRain() {
		return rain;
	}

	/**
	 * @param rain the rain to set
	 */
	public void setRain(float rain) {
		this.rain = rain;
	}

	/**
	 * Retrieve the object from the json string.
	 * 
	 * @param json Json string of the object.
	 * @return Object retrieved from the json string.
	 * @throws ParameterValueInvalidException Error in the retrieving the object
	 *                                        from the json string
	 */
	public static InmetHourlyDataEntity valueOf(final String json) throws ParameterValueInvalidException {
		try {
			// creating the object from json
			InmetHourlyDataEntity entity = JsonUtil.getJsonConverter().fromJson(json, InmetHourlyDataEntity.class);
			return entity;

		} catch (Throwable e) {
			throw ErrorUtil.getParameterValueInvalidExceptionError(NetworkUtil.getLocalIpAddress(),
					MessageEnum.INMET_HOURLY_DATA_ENTITY_ERROR_JSON_PARSING,
					InmetDiarlyDataEntity.class.getSimpleName(), "valueOf", e.getMessage(), null, json);
		}
	}

	/**
	 * Convert the object in a json string.
	 * 
	 * @return Json string of the object.
	 */
	public String toString() {
		try {

			JsonParser parse = new JsonParser();
			String json = JsonUtil.getJsonConverterWithExposeAnnotation().toJson(this);
			JsonObject jsonObject = (JsonObject) parse.parse(json);
			jsonObject.remove("entilyStation");
			jsonObject.addProperty("CD_ESTACAO", entilyStation.getCode());

			return jsonObject.toString();

		} catch (Throwable e) {
		}
		return null;
	}

	@Override
	public int compareTo(InmetHourlyDataEntity entity) {
		// For ascending order
		String ownEntity = StringUtil.removeAccent(this.getEntilyStation().getCode() + ";"
				+ TimeUtil.formatterLocalDateToYYYYMMDD(this.getMeasurementDate()));
		String otherEntity = StringUtil.removeAccent(this.getEntilyStation().getCode() + ";"
				+ TimeUtil.formatterLocalDateToYYYYMMDD(entity.getMeasurementDate()));
		return ownEntity.compareTo(otherEntity);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(blast);
		result = prime * result + ((entilyStation == null) ? 0 : entilyStation.hashCode());
		result = prime * result + Float.floatToIntBits(globalRadiation);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + Float.floatToIntBits(instantDewPoint);
		result = prime * result + Float.floatToIntBits(instantPrecipitation);
		result = prime * result + Float.floatToIntBits(instantRelativeAirHumidity);
		result = prime * result + Float.floatToIntBits(instantTemperature);
		result = prime * result + Float.floatToIntBits(maximumDewPoint);
		result = prime * result + Float.floatToIntBits(maximumPrecipitation);
		result = prime * result + Float.floatToIntBits(maximumRelativeAirHumidity);
		result = prime * result + Float.floatToIntBits(maximumTemperature);
		result = prime * result + ((measureTime == null) ? 0 : measureTime.hashCode());
		result = prime * result + ((measurementDate == null) ? 0 : measurementDate.hashCode());
		result = prime * result + Float.floatToIntBits(minimumDewPoint);
		result = prime * result + Float.floatToIntBits(minimumPrecipitation);
		result = prime * result + Float.floatToIntBits(minimumRelativeAirHumidity);
		result = prime * result + Float.floatToIntBits(minimumTemperature);
		result = prime * result + Float.floatToIntBits(rain);
		result = prime * result + windDirection;
		result = prime * result + Float.floatToIntBits(windSpeed);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InmetHourlyDataEntity other = (InmetHourlyDataEntity) obj;
		if (Float.floatToIntBits(blast) != Float.floatToIntBits(other.blast))
			return false;
		if (entilyStation == null) {
			if (other.entilyStation != null)
				return false;
		} else if (!entilyStation.equals(other.entilyStation))
			return false;
		if (Float.floatToIntBits(globalRadiation) != Float.floatToIntBits(other.globalRadiation))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (Float.floatToIntBits(instantDewPoint) != Float.floatToIntBits(other.instantDewPoint))
			return false;
		if (Float.floatToIntBits(instantPrecipitation) != Float.floatToIntBits(other.instantPrecipitation))
			return false;
		if (Float.floatToIntBits(instantRelativeAirHumidity) != Float.floatToIntBits(other.instantRelativeAirHumidity))
			return false;
		if (Float.floatToIntBits(instantTemperature) != Float.floatToIntBits(other.instantTemperature))
			return false;
		if (Float.floatToIntBits(maximumDewPoint) != Float.floatToIntBits(other.maximumDewPoint))
			return false;
		if (Float.floatToIntBits(maximumPrecipitation) != Float.floatToIntBits(other.maximumPrecipitation))
			return false;
		if (Float.floatToIntBits(maximumRelativeAirHumidity) != Float.floatToIntBits(other.maximumRelativeAirHumidity))
			return false;
		if (Float.floatToIntBits(maximumTemperature) != Float.floatToIntBits(other.maximumTemperature))
			return false;
		if (measureTime == null) {
			if (other.measureTime != null)
				return false;
		} else if (!measureTime.equals(other.measureTime))
			return false;
		if (measurementDate == null) {
			if (other.measurementDate != null)
				return false;
		} else if (!measurementDate.equals(other.measurementDate))
			return false;
		if (Float.floatToIntBits(minimumDewPoint) != Float.floatToIntBits(other.minimumDewPoint))
			return false;
		if (Float.floatToIntBits(minimumPrecipitation) != Float.floatToIntBits(other.minimumPrecipitation))
			return false;
		if (Float.floatToIntBits(minimumRelativeAirHumidity) != Float.floatToIntBits(other.minimumRelativeAirHumidity))
			return false;
		if (Float.floatToIntBits(minimumTemperature) != Float.floatToIntBits(other.minimumTemperature))
			return false;
		if (Float.floatToIntBits(rain) != Float.floatToIntBits(other.rain))
			return false;
		if (windDirection != other.windDirection)
			return false;
		if (Float.floatToIntBits(windSpeed) != Float.floatToIntBits(other.windSpeed))
			return false;
		return true;
	}

}
