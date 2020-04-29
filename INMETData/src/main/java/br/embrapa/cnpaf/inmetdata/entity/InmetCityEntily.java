package br.embrapa.cnpaf.inmetdata.entity;

import java.io.Serializable;

public class InmetCityEntily implements Serializable, Comparable<InmetCityEntily> {

	private static final long serialVersionUID = 1L;
	private Long id;
	private double latitude;
	private double longitude;
	private String name;
	private InmetStateEntily stateEntily;

	/**
	 * 
	 */
	public InmetCityEntily() {
		super();
	}

	/**
	 * @param id
	 * @param latitude
	 * @param longitude
	 * @param name
	 * @param stateEntily
	 */
	public InmetCityEntily(Long id, double latitude, double longitude, String name,
			br.embrapa.cnpaf.inmetdata.entity.InmetStateEntily stateEntily) {
		super();
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.stateEntily = stateEntily;
	}

	/**
	 * @return the stateEntily
	 */
	public InmetStateEntily getStateEntily() {
		return stateEntily;
	}

	/**
	 * @param stateEntily the stateEntily to set
	 */
	public void setStateEntily(InmetStateEntily stateEntily) {
		this.stateEntily = stateEntily;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "cityEntily [id=" + id + ", latitude=" + latitude + ", longitude=" + longitude + ", name=" + name
				+ ", stateEntily=" + stateEntily + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((stateEntily == null) ? 0 : stateEntily.hashCode());
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
		InmetCityEntily other = (InmetCityEntily) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (stateEntily == null) {
			if (other.stateEntily != null)
				return false;
		} else if (!stateEntily.equals(other.stateEntily))
			return false;
		return true;
	}

	@Override
	public int compareTo(InmetCityEntily o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
