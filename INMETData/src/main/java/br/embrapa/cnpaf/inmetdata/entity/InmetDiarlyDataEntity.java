package br.embrapa.cnpaf.inmetdata.entity;

import java.io.Serializable;
import java.time.LocalDate;
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
public class InmetDiarlyDataEntity implements Serializable, Comparable<InmetDiarlyDataEntity> {

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
	@SerializedName("TEM_MIN")
	private Float minimumTemperature;

	@Expose
	@SerializedName("TEM_MAX")
	private Float maximumTemperature;

	@Expose
	@SerializedName("PRE_MIN")
	private Float minimumPrecipitation;

	@Expose
	@SerializedName("PRE_MAX")
	private Float maximumPrecipitation;

	@Expose
	@SerializedName("UMD_MIN")
	private Float minimumRelativeAirHumidity;

	@Expose
	@SerializedName("UMD_MAX")
	private Float maximumRelativeAirHumidity;

	@Expose
	@SerializedName("VEN_VEL")
	private Float windSpeed;

	@Expose
	@SerializedName("VEN_DIR")
	private Float windDirection;

	@Expose
	@SerializedName("RAD_GLO")
	private Float globalRadiation;

	@Expose
	@SerializedName("PTO_MIN")
	private Float minimumDewPoint;

	@Expose
	@SerializedName("PTO_MAX")
	private Float maximumDewPoint;

	@Expose
	@SerializedName("CHUVA")
	private Float rain;

	/**
	 * Public constructor without parameters.
	 */
	public InmetDiarlyDataEntity() {
		super();
	}

	/**
	 * @param id
	 * @param entilyStation
	 * @param measurementDate
	 * @param minimumTemperature
	 * @param maximumTemperature
	 * @param minimumPrecipitation
	 * @param maximumPrecipitation
	 * @param minimumRelativeAirHumidity
	 * @param maximumRelativeAirHumidity
	 * @param windSpeed
	 * @param windDirection
	 * @param globalRadiation
	 * @param minimumDewPoint
	 * @param maximumDewPoint
	 * @param rain
	 */
	public InmetDiarlyDataEntity(Long id, InmetStationEntity entilyStation, LocalDate measurementDate,
			Float minimumTemperature, Float maximumTemperature, Float minimumPrecipitation, Float maximumPrecipitation,
			Float minimumRelativeAirHumidity, Float maximumRelativeAirHumidity, Float windSpeed, Float windDirection,
			Float globalRadiation, Float minimumDewPoint, Float maximumDewPoint, Float rain) {
		super();
		this.id = id;
		this.entilyStation = entilyStation;
		this.measurementDate = measurementDate;
		this.minimumTemperature = minimumTemperature;
		this.maximumTemperature = maximumTemperature;
		this.minimumPrecipitation = minimumPrecipitation;
		this.maximumPrecipitation = maximumPrecipitation;
		this.minimumRelativeAirHumidity = minimumRelativeAirHumidity;
		this.maximumRelativeAirHumidity = maximumRelativeAirHumidity;
		this.windSpeed = windSpeed;
		this.windDirection = windDirection;
		this.globalRadiation = globalRadiation;
		this.minimumDewPoint = minimumDewPoint;
		this.maximumDewPoint = maximumDewPoint;
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
	 * @return the minimumTemperature
	 */
	public Float getMinimumTemperature() {
		return minimumTemperature;
	}

	/**
	 * @param minimumTemperature the minimumTemperature to set
	 */
	public void setMinimumTemperature(Float minimumTemperature) {
		this.minimumTemperature = minimumTemperature;
	}

	/**
	 * @return the maximumTemperature
	 */
	public Float getMaximumTemperature() {
		return maximumTemperature;
	}

	/**
	 * @param maximumTemperature the maximumTemperature to set
	 */
	public void setMaximumTemperature(Float maximumTemperature) {
		this.maximumTemperature = maximumTemperature;
	}

	/**
	 * @return the minimumPrecipitation
	 */
	public Float getMinimumPrecipitation() {
		return minimumPrecipitation;
	}

	/**
	 * @param minimumPrecipitation the minimumPrecipitation to set
	 */
	public void setMinimumPrecipitation(Float minimumPrecipitation) {
		this.minimumPrecipitation = minimumPrecipitation;
	}

	/**
	 * @return the maximumPrecipitation
	 */
	public Float getMaximumPrecipitation() {
		return maximumPrecipitation;
	}

	/**
	 * @param maximumPrecipitation the maximumPrecipitation to set
	 */
	public void setMaximumPrecipitation(Float maximumPrecipitation) {
		this.maximumPrecipitation = maximumPrecipitation;
	}

	/**
	 * @return the minimumRelativeAirHumidity
	 */
	public Float getMinimumRelativeAirHumidity() {
		return minimumRelativeAirHumidity;
	}

	/**
	 * @param minimumRelativeAirHumidity the minimumRelativeAirHumidity to set
	 */
	public void setMinimumRelativeAirHumidity(Float minimumRelativeAirHumidity) {
		this.minimumRelativeAirHumidity = minimumRelativeAirHumidity;
	}

	/**
	 * @return the maximumRelativeAirHumidity
	 */
	public Float getMaximumRelativeAirHumidity() {
		return maximumRelativeAirHumidity;
	}

	/**
	 * @param maximumRelativeAirHumidity the maximumRelativeAirHumidity to set
	 */
	public void setMaximumRelativeAirHumidity(Float maximumRelativeAirHumidity) {
		this.maximumRelativeAirHumidity = maximumRelativeAirHumidity;
	}

	/**
	 * @return the windSpeed
	 */
	public Float getWindSpeed() {
		return windSpeed;
	}

	/**
	 * @param windSpeed the windSpeed to set
	 */
	public void setWindSpeed(Float windSpeed) {
		this.windSpeed = windSpeed;
	}

	/**
	 * @return the windDirection
	 */
	public Float getWindDirection() {
		return windDirection;
	}

	/**
	 * @param windDirection the windDirection to set
	 */
	public void setWindDirection(Float windDirection) {
		this.windDirection = windDirection;
	}

	/**
	 * @return the globalRadiation
	 */
	public Float getGlobalRadiation() {
		return globalRadiation;
	}

	/**
	 * @param globalRadiation the globalRadiation to set
	 */
	public void setGlobalRadiation(Float globalRadiation) {
		this.globalRadiation = globalRadiation;
	}

	/**
	 * @return the minimumDewPoint
	 */
	public Float getMinimumDewPoint() {
		return minimumDewPoint;
	}

	/**
	 * @param minimumDewPoint the minimumDewPoint to set
	 */
	public void setMinimumDewPoint(Float minimumDewPoint) {
		this.minimumDewPoint = minimumDewPoint;
	}

	/**
	 * @return the maximumDewPoint
	 */
	public Float getMaximumDewPoint() {
		return maximumDewPoint;
	}

	/**
	 * @param maximumDewPoint the maximumDewPoint to set
	 */
	public void setMaximumDewPoint(Float maximumDewPoint) {
		this.maximumDewPoint = maximumDewPoint;
	}

	/**
	 * @return the rain
	 */
	public Float getRain() {
		return rain;
	}

	/**
	 * @param rain the rain to set
	 */
	public void setRain(Float rain) {
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
	public static InmetDiarlyDataEntity valueOf(final String json) throws ParameterValueInvalidException {
		try {
			// creating the object from json
			InmetDiarlyDataEntity entity = JsonUtil.getJsonConverter().fromJson(json, InmetDiarlyDataEntity.class);
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
			return JsonUtil.getJsonConverterWithExposeAnnotation().toJson(this);
		} catch (Throwable e) {
		}
		return null;
	}

	@Override
	public int compareTo(InmetDiarlyDataEntity entity) {
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
		result = prime * result + ((entilyStation == null) ? 0 : entilyStation.hashCode());
		result = prime * result + ((globalRadiation == null) ? 0 : globalRadiation.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((maximumDewPoint == null) ? 0 : maximumDewPoint.hashCode());
		result = prime * result + ((maximumPrecipitation == null) ? 0 : maximumPrecipitation.hashCode());
		result = prime * result + ((maximumRelativeAirHumidity == null) ? 0 : maximumRelativeAirHumidity.hashCode());
		result = prime * result + ((maximumTemperature == null) ? 0 : maximumTemperature.hashCode());
		result = prime * result + ((measurementDate == null) ? 0 : measurementDate.hashCode());
		result = prime * result + ((minimumDewPoint == null) ? 0 : minimumDewPoint.hashCode());
		result = prime * result + ((minimumPrecipitation == null) ? 0 : minimumPrecipitation.hashCode());
		result = prime * result + ((minimumRelativeAirHumidity == null) ? 0 : minimumRelativeAirHumidity.hashCode());
		result = prime * result + ((minimumTemperature == null) ? 0 : minimumTemperature.hashCode());
		result = prime * result + ((rain == null) ? 0 : rain.hashCode());
		result = prime * result + ((windDirection == null) ? 0 : windDirection.hashCode());
		result = prime * result + ((windSpeed == null) ? 0 : windSpeed.hashCode());
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
		InmetDiarlyDataEntity other = (InmetDiarlyDataEntity) obj;
		if (entilyStation == null) {
			if (other.entilyStation != null)
				return false;
		} else if (!entilyStation.equals(other.entilyStation))
			return false;
		if (globalRadiation == null) {
			if (other.globalRadiation != null)
				return false;
		} else if (!globalRadiation.equals(other.globalRadiation))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (maximumDewPoint == null) {
			if (other.maximumDewPoint != null)
				return false;
		} else if (!maximumDewPoint.equals(other.maximumDewPoint))
			return false;
		if (maximumPrecipitation == null) {
			if (other.maximumPrecipitation != null)
				return false;
		} else if (!maximumPrecipitation.equals(other.maximumPrecipitation))
			return false;
		if (maximumRelativeAirHumidity == null) {
			if (other.maximumRelativeAirHumidity != null)
				return false;
		} else if (!maximumRelativeAirHumidity.equals(other.maximumRelativeAirHumidity))
			return false;
		if (maximumTemperature == null) {
			if (other.maximumTemperature != null)
				return false;
		} else if (!maximumTemperature.equals(other.maximumTemperature))
			return false;
		if (measurementDate == null) {
			if (other.measurementDate != null)
				return false;
		} else if (!measurementDate.equals(other.measurementDate))
			return false;
		if (minimumDewPoint == null) {
			if (other.minimumDewPoint != null)
				return false;
		} else if (!minimumDewPoint.equals(other.minimumDewPoint))
			return false;
		if (minimumPrecipitation == null) {
			if (other.minimumPrecipitation != null)
				return false;
		} else if (!minimumPrecipitation.equals(other.minimumPrecipitation))
			return false;
		if (minimumRelativeAirHumidity == null) {
			if (other.minimumRelativeAirHumidity != null)
				return false;
		} else if (!minimumRelativeAirHumidity.equals(other.minimumRelativeAirHumidity))
			return false;
		if (minimumTemperature == null) {
			if (other.minimumTemperature != null)
				return false;
		} else if (!minimumTemperature.equals(other.minimumTemperature))
			return false;
		if (rain == null) {
			if (other.rain != null)
				return false;
		} else if (!rain.equals(other.rain))
			return false;
		if (windDirection == null) {
			if (other.windDirection != null)
				return false;
		} else if (!windDirection.equals(other.windDirection))
			return false;
		if (windSpeed == null) {
			if (other.windSpeed != null)
				return false;
		} else if (!windSpeed.equals(other.windSpeed))
			return false;
		return true;
	}

}
