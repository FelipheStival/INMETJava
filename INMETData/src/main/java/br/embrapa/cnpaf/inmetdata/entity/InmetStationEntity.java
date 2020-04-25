package br.embrapa.cnpaf.inmetdata.entity;

import java.io.Serializable;
import java.time.LocalDate;
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
public class InmetStationEntity implements Serializable, Comparable<InmetStationEntity> {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String code;
	private double latitude;
	private double longitude;
	private String city;
	private String state;
	private LocalDate startDate;

	/**
	 * Public constructor without parameters.
	 * 
	 */
	public InmetStationEntity() {
		super();
	}

	/**
	 * @param id
	 * @param code
	 * @param latitude
	 * @param longitude
	 * @param city
	 * @param state
	 * @param startDate
	 */
	public InmetStationEntity(Long id, String code, double latitude, double longitude, String city, String state,
			LocalDate startDate) {
		super();
		this.id = id;
		this.code = code;
		this.latitude = latitude;
		this.longitude = longitude;
		this.city = city;
		this.state = state;
		this.startDate = startDate;
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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the startDate
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		InmetStationEntity other = (InmetStationEntity) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
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
	public int compareTo(InmetStationEntity o) {
		// TODO Auto-generated method stub
		String ownEntity = StringUtil
				.removeAccent(this.getCode() + ";" + TimeUtil.formatterLocalDateToYYYYMMDD(this.getStartDate()));
		String otherEntity = StringUtil
				.removeAccent(o.getCode() + ";" + TimeUtil.formatterLocalDateToYYYYMMDD(o.getStartDate()));
		return ownEntity.compareTo(otherEntity);
	}

	/**
	 * Retrieve the object from the json string.
	 * 
	 * @param json Json string of the object.
	 * @return Object retrieved from the json string.
	 * @throws ParameterValueInvalidException Error in the retrieving the object
	 *                                        from the json string
	 */
	public static InmetStationEntity valueOf(final String json) throws ParameterValueInvalidException {
		try {
			// creating the object from json
			InmetStationEntity entity = JsonUtil.getJsonConverter().fromJson(json, InmetStationEntity.class);
			return entity;

		} catch (Throwable e) {
			throw ErrorUtil.getParameterValueInvalidExceptionError(NetworkUtil.getLocalIpAddress(),
					MessageEnum.INMET_HOURLY_DATA_ENTITY_ERROR_JSON_PARSING,
					InmetDiarlyDataEntity.class.getSimpleName(), "valueOf", e.getMessage(), null, json);
		}
	}

}
