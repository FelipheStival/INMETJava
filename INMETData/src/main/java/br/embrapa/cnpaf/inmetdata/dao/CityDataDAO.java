package br.embrapa.cnpaf.inmetdata.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.management.Query;
import javax.swing.text.html.parser.Entity;

import org.apache.log4j.Level;
import org.checkerframework.checker.units.qual.s;

import br.embrapa.cnpaf.inmetdata.entity.CityEntily;
import br.embrapa.cnpaf.inmetdata.entity.InmetHourlyDataEntity;
import br.embrapa.cnpaf.inmetdata.entity.InmetStationEntity;
import br.embrapa.cnpaf.inmetdata.enumerate.MessageEnum;
import br.embrapa.cnpaf.inmetdata.exception.PersistenceException;
import br.embrapa.cnpaf.inmetdata.util.NetworkUtil;
import br.embrapa.cnpaf.inmetdata.util.TimeUtil;

/**
 * <br>
 * <p>
 * <b> Singleton Class responsible for by performing the persistence of entities
 * of the type InmetHourlyDataEntity.</b>
 * </p>
 * <p>
 * It uses the standard Data Access Object design - DAO, in order to separate
 * the other system classes the code needed to interface with the database. For
 * this it defines the methods save, delete, find and list to persist or
 * retrieve the entities.
 * </p>
 * <p>
 * To retrieve an instance of this class use the static method getInstanceOf
 * ():<br>
 * <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;
 * <tt> InmetHourlyDataDAO dao = InmetHourlyDataDAO.getInstanceOf(logClientName, logLevel);</tt>
 * </p>
 * <br>
 * 
 * @author Sergio Lopes Jr.
 * @version 0.1
 * @since 03/03/2020 (creation date)
 *
 */
public class CityDataDAO extends GenericDAO<CityDataDAO,CityEntily> {

	private static CityDataDAO instance;

	/**
	 * Private class constructor.
	 * 
	 * @param logClientName Name of the client object of the logging service.
	 * @param logLevel      Log level to be used in log service.
	 * @throws PersistenceException Occurrence of any problems in creating of the
	 *                              DAO.
	 */
	private CityDataDAO(String logClientName, Level logLevel) throws PersistenceException {
		super(logClientName, logLevel);

		// initializing database
		this.init();

		// DAO create success
		this.success(MessageEnum.GENERIC_DAO_INFO_SUCCESS_CREATE, NetworkUtil.getLocalIpAddress(),
				this.getDAODescriptor());
	}

	/**
	 * Method to retrieve the instance of DAO. This class has a single instance for
	 * any application (Singleton).
	 * 
	 * @return Returns the instance of DAO.
	 * @throws PersistenceException Occurrence of any problems in creating of the
	 *                              DAO.
	 */
	public static synchronized CityDataDAO getInstanceOf(String logClientName, Level logLevel)
			throws PersistenceException {
		if (CityDataDAO.instance == null) {
			CityDataDAO.instance = new CityDataDAO(logClientName, logLevel);
		}
		return CityDataDAO.instance;
	}

	/**
	 * Method to retrieve the instance of DAO. This class has a single instance for
	 * any application (Singleton).<br>
	 * To use this method, it is necessary that the DAO instance has already been
	 * created by executing the getInstanceOf(String logClientName, Level logLevel)
	 * method.
	 * 
	 * @return Returns the instance of DAO.
	 * @throws PersistenceException Occurrence of any problems in retrieving of the
	 *                              DAO instance.
	 */
	public static synchronized CityDataDAO getInstanceOf() throws PersistenceException {
		return CityDataDAO.getInstanceOf(CityDataDAO.class.getSimpleName(), LOG_DEFAULT_LEVEL);
	}

	@Override
	public CityDataDAO save(CityEntily entity) throws PersistenceException {

		// validate entity
		Long id = entity != null ? entity.getId() : null;

		// save ou update the entity
		id = super.save(//
				entity.getId() //
				, "INSERT INTO " + "public." + TABLE_INMET_HOURLY_DATA + "(" + //
						"station_id," + //
						"measurement_date," + //
						"measure_time," + //
						"minimum_temperature," + //
						"maximum_temperature," + //
						"instant_temperature," + //
						"minimum_precipitation," + //
						"maximum_precipitation," + //
						"instant_precipitation," + //
						"minimum_relative_air_humidity," + //
						"maximum_relative_air_humidity," + //
						"instant_relative_air_humidity," + //
						"wind_speed," + //
						"wind_direction," + //
						"blast," + //
						"global_radiation," + //
						"minimum_dew_point," + //
						"maximum_dew_point," + //
						"instant_dew_point," + //
						"rain)" + //
						"VALUES (" //
						+ "'" + entity.getEntilyStation().getId() + "'" + "," //
						+ "'" + entity.getMeasurementDate() + "'" + ","//
						+ "'" + entity.getMeasureTime() + "'" + ","//
						+ entity.getMinimumTemperature() + ","//
						+ entity.getMaximumTemperature() + ","//
						+ entity.getInstantTemperature() + ","//
						+ entity.getMinimumPrecipitation() + "," //
						+ entity.getMaximumPrecipitation() + "," //
						+ entity.getInstantPrecipitation() + "," //
						+ entity.getMinimumRelativeAirHumidity() + "," //
						+ entity.getMaximumRelativeAirHumidity() + ","//
						+ entity.getInstantRelativeAirHumidity() + ","//
						+ entity.getWindSpeed() + ","//
						+ entity.getWindDirection() + ","//
						+ entity.getBlast() + ","//
						+ entity.getGlobalRadiation() + ","//
						+ entity.getMinimumDewPoint() + ","//
						+ entity.getMaximumDewPoint() + ","//
						+ entity.getInstantDewPoint() + ","//
						+ entity.getBlast() + ")" + ";"//
				, "UPDATE" + "public." + TABLE_INMET_HOURLY_DATA + "SET" //
						+ "station_id=" + entity.getEntilyStation().getId() + "," //
						+ "measurement_date=" + entity.getMeasurementDate() + "," //
						+ "measure_time=" + entity.getMeasureTime() + "," //
						+ "minimum_temperature=" + entity.getMinimumTemperature() + "," //
						+ "maximum_temperature=" + entity.getMaximumTemperature() + "," //
						+ "instant_temperature=" + entity.getInstantTemperature() + "," //
						+ "minimum_precipitation=" + entity.getMinimumPrecipitation() + "," //
						+ "maximum_precipitation=" + entity.getMaximumPrecipitation() + "," //
						+ "instant_precipitation=" + entity.getInstantPrecipitation() + "," //
						+ "minimum_relative_air_humidity=" + entity.getMinimumRelativeAirHumidity() + "," //
						+ "maximum_relative_air_humidity=" + entity.getMaximumRelativeAirHumidity() + "," //
						+ "instant_relative_air_humidity=" + entity.getInstantRelativeAirHumidity() + "," //
						+ "wind_speed=" + entity.getWindSpeed() + "," //
						+ "wind_direction=" + entity.getWindDirection() + "," //
						+ "blast=" + entity.getBlast() + "," //
						+ "global_radiation=" + entity.getGlobalRadiation() + "," //
						+ "minimum_dew_point=" + entity.getMinimumDewPoint() + "," //
						+ "maximum_dew_point=" + entity.getMaximumDewPoint() + "," //
						+ "instant_dew_point=" + entity.getInstantDewPoint() + "," //
						+ "rain=" + entity.getRain() + "," //
						+ "WHERE station_id =" + entity.getEntilyStation().getId() + ";");

		// return DAO instance
		return this;
	}

	@Override
	public CityDataDAO remove(Long id) throws PersistenceException {

		// verifying that the id is valid
		if (id == null || id == 0) {
			throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.GENERIC_DAO_ERROR_REMOVE,
					this.getClass().getSimpleName(), "remove", null, null, true, NetworkUtil.getLocalIpAddress(),
					this.getDAODescriptor(), String.valueOf(id));
		}

		try {
			// retrienving id for station relationship
//			InmetStationEntity station = this.find(id).getStation();

			// removing entity
			super.remove(id, "DELETE FROM " + TABLE_INMET_HOURLY_DATA + " WHERE id=" + id + ";");

			// removing remotesModulesAddresses relationship with module address entity
//			this.removeStationRelationship(station.getId());

		} catch (PersistenceException e) {
			throw (e);
		}

		// return DAO instance
		return this;
	}

	@Override
	public CityEntily find(Long id) throws PersistenceException {
		return super.find(id, "SELECT * FROM " + TABLE_INMET_CITY + " WHERE station_id=" + id + ";");
	}

	@Override
	public List<CityEntily> list() throws PersistenceException {
		return super.list("SELECT * FROM " + TABLE_INMET_CITY + ";");
	}

	@Override
	protected CityDataDAO init() throws PersistenceException {

		// initializing variables
		List<String> queries = new ArrayList<String>();

		// SQL for entity table create
		queries.add(//
				"CREATE TABLE public."  + TABLE_INMET_CITY +
				"(" +
				    "id bigserial NOT NULL," +
				    "id_state bigserial NOT NULL REFERENCES " + TABLE_INMET_STATE + "(id)," +
				    "latitude double precision NOT NULL," +
				    "longitude double precision NOT NULL," +
				    "name text NOT NULL," +
				    "start_date date NOT NULL," +
				    "PRIMARY KEY (id)" +
				");"
		);
		
		// initializing table
		super.init(queries);
		
		//Populando tabela
		
		return this;
	}

	@Override
	protected CityEntily getEntity(ResultSet queryResult) throws PersistenceException {

		Long id = null;
		try {
			// retrieving the attributes
			id = queryResult.getObject("id") != null ? queryResult.getLong("id") : null;

			// Recovering relationship
			Long idStation = queryResult.getLong("station_id");
			InmetStationEntity entity = this.getStationRelationship(idStation);

			// creating new entity with attributes retrieved from database
			return new InmetHourlyDataEntity(//
					queryResult.getLong("id"), //
					entity, TimeUtil.stringToLocalDate(queryResult.getString("measurement_date")), //
					queryResult.getString("measure_time"), //
					queryResult.getFloat("minimum_temperature"), //
					queryResult.getFloat("maximum_temperature"), //
					queryResult.getFloat("instant_temperature"), //
					queryResult.getFloat("minimum_precipitation"), //
					queryResult.getFloat("maximum_precipitation"), //
					queryResult.getFloat("instant_precipitation"), //
					queryResult.getFloat("minimum_relative_air_humidity"), //
					queryResult.getFloat("maximum_relative_air_humidity"), //
					queryResult.getFloat("instant_relative_air_humidity"), //
					queryResult.getFloat("wind_speed"), //
					queryResult.getInt("wind_direction"), //
					queryResult.getFloat("blast"), //
					queryResult.getFloat("global_radiation"), //
					queryResult.getFloat("minimum_dew_point"), //
					queryResult.getFloat("maximum_dew_point"), //
					queryResult.getFloat("instant_dew_point"), //
					queryResult.getFloat("rain"));//

		} catch (Throwable e) {
			throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.GENERIC_DAO_ERROR_GET_ENTITY,
					this.getClass().getSimpleName(), "getEntity", e.getMessage(), null, true,
					NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), String.valueOf(id));
		}
	}

	/**
	 * Saves the module address entity associated with the moduleAddress
	 * relationship (1-1).
	 * 
	 * @param entity Relationship entity to be saved.
	 * @return Returns the instance of DAO.
	 * @throws PersistenceException Occurrence of any problems in saving of the
	 *                              relationship.
	 */
	private CityDataDAO saveStationRelationship(InmetStationEntity entity) throws PersistenceException {
		if (entity != null) {
			InmetStationDAO.getInstanceOf().save(entity);
		}
		return this;
	}

	/**
	 * Removes the module address entity associated with the moduleAddress
	 * relationship (1-1) according to the informed identification (ID).
	 * 
	 * @param entityId Identifier (ID) of the entity associated with the
	 *                 moduleAddress relationship.
	 * @return Returns the instance of DAO.
	 */
	private CityDataDAO removeStationRelationship(Long entityId) {
		try {
			InmetStationDAO.getInstanceOf().remove(entityId);
		} catch (Throwable e) {
		}
		return this;
	}

	/**
	 * Retrieves the module address entity associated with the moduleAddress
	 * relationship (1-1) according to the informed identification (ID).
	 * 
	 * @param entityId Identification of the entity (ID) of the relationship.
	 * @return Returns the instance of relationship entity.
	 * @throws PersistenceException Occurrence of any problems in retrieving of the
	 *                              relationship.
	 */
	private InmetStationEntity getStationRelationship(Long entityId) throws PersistenceException {
		if (entityId != null) {
			return InmetStationDAO.getInstanceOf().find(entityId);
		}
		return null;
	}

	@Override
	public CityDataDAO save(CityEntily entity) throws PersistenceException {
		// TODO Auto-generated method stub
		return null;
	}
}
