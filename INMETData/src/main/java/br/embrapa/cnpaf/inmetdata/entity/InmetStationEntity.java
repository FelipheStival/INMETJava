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
	private CityEntily cityEntily;
	private LocalDate startDate;

	/**
	 * 
	 */
	public InmetStationEntity() {
		super();
	}

	/**
	 * @param id
	 * @param code
	 * @param cityEntily
	 * @param startDate
	 */
	public InmetStationEntity(Long id, String code, CityEntily cityEntily, LocalDate startDate) {
		super();
		this.id = id;
		this.code = code;
		this.cityEntily = cityEntily;
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
	 * @return the cityEntily
	 */
	public CityEntily getCityEntily() {
		return cityEntily;
	}

	/**
	 * @param cityEntily the cityEntily to set
	 */
	public void setCityEntily(CityEntily cityEntily) {
		this.cityEntily = cityEntily;
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
		result = prime * result + ((cityEntily == null) ? 0 : cityEntily.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
		if (cityEntily == null) {
			if (other.cityEntily != null)
				return false;
		} else if (!cityEntily.equals(other.cityEntily))
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
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "InmetStationEntity [id=" + id + ", code=" + code + ", cityEntily=" + cityEntily + ", startDate="
				+ startDate + "]";
	}

	@Override
	public int compareTo(InmetStationEntity o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
