package br.embrapa.cnpaf.inmetdata.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;

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
public class InmetHourlyDataDAO extends GenericDAO<InmetHourlyDataDAO, InmetHourlyDataEntity> {

	private static InmetHourlyDataDAO instance;

	/**
	 * Private class constructor.
	 * 
	 * @param logClientName Name of the client object of the logging service.
	 * @param logLevel      Log level to be used in log service.
	 * @throws PersistenceException Occurrence of any problems in creating of the
	 *                              DAO.
	 */
	private InmetHourlyDataDAO(String logClientName, Level logLevel) throws PersistenceException {
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
	public static synchronized InmetHourlyDataDAO getInstanceOf(String logClientName, Level logLevel)
			throws PersistenceException {
		if (InmetHourlyDataDAO.instance == null) {
			InmetHourlyDataDAO.instance = new InmetHourlyDataDAO(logClientName, logLevel);
		}
		return InmetHourlyDataDAO.instance;
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
	public static synchronized InmetHourlyDataDAO getInstanceOf() throws PersistenceException {
		return InmetHourlyDataDAO.getInstanceOf(InmetHourlyDataDAO.class.getSimpleName(), LOG_DEFAULT_LEVEL);
	}

	/**
	 * this method returns a list with daily data, chosen for a period of time
	 * 
	 * @return Return a list with daily data
	 * @throws PersistenceException Occurrence of any problems in creating of the
	 *                              DAO.
	 */
	public List<InmetHourlyDataEntity> listByPeriodTime(LocalDate initDate, LocalDate endDate)
			throws PersistenceException {
		// initializing variables
		Connection connection = this.getConnection();
		Statement statement = null;
		List<InmetHourlyDataEntity> data = new ArrayList<InmetHourlyDataEntity>();

		String query = " SELECT * FROM public. " + TABLE_INMET_HOURLY_DATA + //
				" WHERE measurement_date >= " + "'" + initDate + "'" + //
				" AND " + //
				"measurement_date <= " + "'" + endDate + "'";//

		try {
			// execute sql query
			statement = connection.createStatement();
			statement.execute(query);
			ResultSet resultSet = statement.getResultSet();

			// getting result
			while (resultSet.next()) {

				// getting relationship
				InmetStationEntity entity = this.getStationRelationship(resultSet.getLong("station_id"));

				data.add(new InmetHourlyDataEntity( //
						resultSet.getLong("id"), //
						entity, //
						resultSet.getDate("measurement_date").toLocalDate(), //
						resultSet.getString("measure_time"), //
						resultSet.getFloat("minimum_temperature"), //
						resultSet.getFloat("maximum_temperature"), //
						resultSet.getFloat("instant_temperature"), //
						resultSet.getFloat("minimum_precipitation"), //
						resultSet.getFloat("maximum_precipitation"), //
						resultSet.getFloat("instant_precipitation"), //
						resultSet.getFloat("minimum_relative_air_humidity"), //
						resultSet.getFloat("maximum_relative_air_humidity"), //
						resultSet.getFloat("instant_relative_air_humidity"), //
						resultSet.getFloat("wind_speed"), //
						resultSet.getInt("wind_direction"), //
						resultSet.getFloat("blast"), //
						resultSet.getFloat("global_radiation"), //
						resultSet.getFloat("minimum_dew_point"), //
						resultSet.getFloat("maximum_dew_point"), //
						resultSet.getFloat("instant_dew_point"), //
						resultSet.getFloat("instant_dew_point"))); //

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;

	}

	/**
	 * This method returns a list of daily data for the chosen station
	 * 
	 * @return Return a list with daily data
	 * @throws PersistenceException Occurrence of any problems in creating of the
	 *                              DAO.
	 */
	public List<InmetHourlyDataEntity> listByStation(long idStation) throws PersistenceException {
		// initializing variables
		Connection connection = this.getConnection();
		Statement statement = null;
		List<InmetHourlyDataEntity> Data = new ArrayList<InmetHourlyDataEntity>();

		String query = " SELECT  * FROM " + //
				"public." + TABLE_INMET_HOURLY_DATA + //
				" WHERE station_id = " + idStation; //

		try {
			// execute sql query
			statement = connection.createStatement();
			statement.execute(query);
			ResultSet resultSet = statement.getResultSet();

			// getting result
			while (resultSet.next()) {

				// getting relationship
				InmetStationEntity entity = this.getStationRelationship(resultSet.getLong("station_id"));

				Data.add(new InmetHourlyDataEntity( //
						resultSet.getLong("id"), //
						entity, //
						resultSet.getDate("measurement_date").toLocalDate(), //
						resultSet.getString("measure_time"), //
						resultSet.getFloat("minimum_temperature"), //
						resultSet.getFloat("maximum_temperature"), //
						resultSet.getFloat("instant_temperature"), //
						resultSet.getFloat("minimum_precipitation"), //
						resultSet.getFloat("maximum_precipitation"), //
						resultSet.getFloat("instant_precipitation"), //
						resultSet.getFloat("minimum_relative_air_humidity"), //
						resultSet.getFloat("maximum_relative_air_humidity"), //
						resultSet.getFloat("instant_relative_air_humidity"), //
						resultSet.getFloat("wind_speed"), //
						resultSet.getInt("wind_direction"), //
						resultSet.getFloat("blast"), //
						resultSet.getFloat("global_radiation"), //
						resultSet.getFloat("minimum_dew_point"), //
						resultSet.getFloat("maximum_dew_point"), //
						resultSet.getFloat("instant_dew_point"), //
						resultSet.getFloat("instant_dew_point"))); //
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Data;
	}

	@Override
	public InmetHourlyDataDAO save(InmetHourlyDataEntity entity) throws PersistenceException {

		// validate entity
		Long id = entity != null ? entity.getId() : null;

		// save Relationship
		this.saveStationRelationship(entity.getEntilyStation());

		// save ou update the entity
		id = super.save(//
				id //
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
	public InmetHourlyDataDAO remove(Long id) throws PersistenceException {

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
	public InmetHourlyDataEntity find(Long id) throws PersistenceException {
		return super.find(id, "SELECT * FROM " + TABLE_INMET_HOURLY_DATA + " WHERE station_id=" + id + ";");
	}

	@Override
	public List<InmetHourlyDataEntity> list() throws PersistenceException {
		return super.list("SELECT * FROM " + TABLE_INMET_HOURLY_DATA + ";");
	}

	@Override
	protected InmetHourlyDataDAO init() throws PersistenceException {

		// initializing variables
		List<String> queries = new ArrayList<String>();

		// SQL for entity table create
		queries.add(//
				"CREATE TABLE IF NOT EXISTS " + TABLE_INMET_HOURLY_DATA + " ( "//
						+ "id bigserial primary key "//
						+ ", station_id bigserial REFERENCES " + TABLE_INMET_STATION + "(id) "//
//						+ ", station character varying(10) DEFAULT NULL "//
						+ ", measurement_date date DEFAULT NULL "//
						+ ", measure_Time time without time zone DEFAULT NULL "//
						+ ", minimum_temperature real DEFAULT NULL "//
						+ ", maximum_temperature real DEFAULT NULL "//
						+ ", instant_temperature real DEFAULT NULL "//
						+ ", minimum_precipitation real DEFAULT NULL "//
						+ ", maximum_precipitation real DEFAULT NULL "//
						+ ", instant_precipitation real DEFAULT NULL "//
						+ ", minimum_relative_air_humidity real DEFAULT NULL "//
						+ ", maximum_relative_air_humidity real DEFAULT NULL "//
						+ ", instant_relative_air_humidity real DEFAULT NULL "//
						+ ", wind_speed real DEFAULT NULL "//
						+ ", wind_direction integer DEFAULT NULL "//
						+ ", blast real DEFAULT NULL "//
						+ ", global_radiation real DEFAULT NULL "//
						+ ", minimum_dew_point real DEFAULT NULL "//
						+ ", maximum_dew_point real DEFAULT NULL "//
						+ ", instant_dew_point real DEFAULT NULL "//
						+ ", rain real DEFAULT NULL "//
						+ ", unique (id, measurement_date) "//
						+ "); "//
		);

		// initializing table
		super.init(queries);
		return this;
	}

	/**
	 * this method returns the highest date in the bank
	 * 
	 * @return Returns the highest date of the selected station
	 * @param id_station Station ID
	 * @throws PersistenceException Occurrence of any problems in creating of the
	 *                              DAO.
	 */
	public LocalDate getBiggerDateByStation(Long id_station) throws PersistenceException {
		// initializing variables
		Connection connection = this.getConnection();
		Statement statement = null;
		LocalDate maxDate = null;

		String query = "SELECT max(measurement_date) " + //
				" FROM " + TABLE_INMET_HOURLY_DATA + //
				" WHERE station_id = " + id_station; //

		try {
			// execute sql query
			statement = connection.createStatement();
			statement.execute(query);
			ResultSet resultSet = statement.getResultSet();

			// getting result
			while (resultSet.next()) {
				if (resultSet.getString("max") != null) {
					maxDate = TimeUtil.stringToLocalDate(resultSet.getString("max"));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return maxDate;
	}

	@Override
	protected InmetHourlyDataEntity getEntity(ResultSet queryResult) throws PersistenceException {

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
	private InmetHourlyDataDAO saveStationRelationship(InmetStationEntity entity) throws PersistenceException {
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
	private InmetHourlyDataDAO removeStationRelationship(Long entityId) {
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
}
