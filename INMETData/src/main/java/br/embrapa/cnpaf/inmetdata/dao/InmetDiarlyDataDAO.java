package br.embrapa.cnpaf.inmetdata.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;

import br.embrapa.cnpaf.inmetdata.entity.InmetDiarlyDataEntity;
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
public class InmetDiarlyDataDAO extends GenericDAO<InmetDiarlyDataDAO, InmetDiarlyDataEntity> {

	private static InmetDiarlyDataDAO instance;

	/**
	 * Private class constructor.
	 * 
	 * @param logClientName Name of the client object of the logging service.
	 * @param logLevel      Log level to be used in log service.
	 * @throws PersistenceException Occurrence of any problems in creating of the
	 *                              DAO.
	 */
	private InmetDiarlyDataDAO(String logClientName, Level logLevel) throws PersistenceException {
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
	public static synchronized InmetDiarlyDataDAO getInstanceOf(String logClientName, Level logLevel)
			throws PersistenceException {
		if (InmetDiarlyDataDAO.instance == null) {
			InmetDiarlyDataDAO.instance = new InmetDiarlyDataDAO(logClientName, logLevel);
		}
		return InmetDiarlyDataDAO.instance;
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
	public static synchronized InmetDiarlyDataDAO getInstanceOf() throws PersistenceException {
		return InmetDiarlyDataDAO.getInstanceOf(InmetDiarlyDataDAO.class.getSimpleName(), LOG_DEFAULT_LEVEL);
	}

	@Override
	public InmetDiarlyDataDAO save(InmetDiarlyDataEntity entity) throws PersistenceException {

		// validate entity
		Long id = entity != null ? entity.getId() : null;

		// save ou update the entity
		this.saveStationRelationship(entity.getEntilyStation());

		// save ou update the entity
		id = super.save(//
				id //
				, "INSERT INTO " + "public." + TABLE_INMET_DAILY_DATA + "(" + //
						"station_id," + //
						"measurement_date," + //
						"minimum_temperature," + //
						"maximum_temperature," + //
						"minimum_precipitation," + //
						"maximum_precipitation," + //
						"minimum_relative_air_humidity," + //
						"maximum_relative_air_humidity," + //
						"wind_speed," + //
						"wind_direction," + //
						"global_radiation," + //
						"minimum_dew_point," + //
						"maximum_dew_point," + //
						"rain)" + //
						"VALUES (" //
						+ "'" + entity.getEntilyStation().getId() + "'" + "," //
						+ "'" + entity.getMeasurementDate() + "'" + ","//
						+ entity.getMinimumTemperature() + ","//
						+ entity.getMaximumTemperature() + ","//
						+ entity.getMinimumPrecipitation() + "," //
						+ entity.getMaximumPrecipitation() + "," //
						+ entity.getMinimumRelativeAirHumidity() + "," //
						+ entity.getMaximumRelativeAirHumidity() + ","//
						+ entity.getWindSpeed() + ","//
						+ entity.getWindDirection() + ","//
						+ entity.getGlobalRadiation() + ","//
						+ entity.getMinimumDewPoint() + ","//
						+ entity.getMaximumDewPoint() + ","//
						+ entity.getRain() + ")" + ";"//
				, "UPDATE" + "public." + TABLE_INMET_DAILY_DATA + "SET" //
						+ "station_id=" + entity.getEntilyStation().getId() + "," //
						+ "measurement_date=" + entity.getMeasurementDate() + "," ////
						+ "minimum_temperature=" + entity.getMinimumTemperature() + "," //
						+ "maximum_temperature=" + entity.getMaximumTemperature() + "," //
						+ "minimum_precipitation=" + entity.getMinimumPrecipitation() + "," //
						+ "maximum_precipitation=" + entity.getMaximumPrecipitation() + "," //
						+ "minimum_relative_air_humidity=" + entity.getMinimumRelativeAirHumidity() + "," //
						+ "maximum_relative_air_humidity=" + entity.getMaximumRelativeAirHumidity() + "," //
						+ "wind_speed=" + entity.getWindSpeed() + "," //
						+ "wind_direction=" + entity.getWindDirection() + "," //
						+ "global_radiation=" + entity.getGlobalRadiation() + "," //
						+ "minimum_dew_point=" + entity.getMinimumDewPoint() + "," //
						+ "maximum_dew_point=" + entity.getMaximumDewPoint() + "," //
						+ "rain=" + entity.getRain() + "," //
						+ "WHERE station_id =" + entity.getEntilyStation().getId() + ";");

		// return DAO instance
		return this;
	}

	@Override
	public InmetDiarlyDataDAO remove(Long id) throws PersistenceException {

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
			super.remove(id, "DELETE FROM " + TABLE_INMET_DAILY_DATA + " WHERE id=" + id + ";");

			// removing remotesModulesAddresses relationship with module address entity
//			this.removeStationRelationship(station.getId());

		} catch (PersistenceException e) {
			throw (e);
		}

		// return DAO instance
		return this;
	}

	@Override
	public InmetDiarlyDataEntity find(Long id) throws PersistenceException {
		return super.find(id, "SELECT * FROM " + TABLE_INMET_DAILY_DATA + " WHERE station_id=" + id + ";");
	}

	@Override
	public List<InmetDiarlyDataEntity> list() throws PersistenceException {
		return super.list("SELECT * FROM " + TABLE_INMET_DAILY_DATA + ";");
	}

	/**
	 * this method returns a list with daily data, chosen for a period of time
	 * 
	 * @return Return a list with daily data
	 * @throws PersistenceException Occurrence of any problems in creating of the
	 *                              DAO.
	 */
	public List<InmetDiarlyDataEntity> listByPeriodTime(LocalDate initDate, LocalDate endDate)
			throws PersistenceException {
		// initializing variables
		Connection connection = this.getConnection();
		Statement statement = null;
		List<InmetDiarlyDataEntity> data = new ArrayList<InmetDiarlyDataEntity>();

		String query = " SELECT * FROM public. " + TABLE_INMET_DAILY_DATA + //
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

				data.add(new InmetDiarlyDataEntity( //
						resultSet.getLong("id"), //
						entity, //
						resultSet.getDate("measurement_date").toLocalDate(), //
						resultSet.getFloat("minimum_temperature"), //
						resultSet.getFloat("maximum_temperature"), //
						resultSet.getFloat("minimum_precipitation"), //
						resultSet.getFloat("maximum_precipitation"), //
						resultSet.getFloat("minimum_relative_air_humidity"), //
						resultSet.getFloat("maximum_relative_air_humidity"), //
						resultSet.getFloat("wind_speed"), //
						resultSet.getFloat("wind_direction"), //
						resultSet.getFloat("global_radiation"), //
						resultSet.getFloat("minimum_dew_point"), //
						resultSet.getFloat("maximum_dew_point"), //
						resultSet.getFloat("rain")));//
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
	public List<InmetDiarlyDataEntity> listByStation(long idStation) throws PersistenceException {
		// initializing variables
		Connection connection = this.getConnection();
		Statement statement = null;
		List<InmetDiarlyDataEntity> diarlyData = new ArrayList<InmetDiarlyDataEntity>();

		String query = " SELECT  * FROM " + //
				"public." + TABLE_INMET_DAILY_DATA + //
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

				diarlyData.add(new InmetDiarlyDataEntity( //
						resultSet.getLong("id"), //
						entity, //
						resultSet.getDate("measurement_date").toLocalDate(), //
						resultSet.getFloat("minimum_temperature"), //
						resultSet.getFloat("maximum_temperature"), //
						resultSet.getFloat("minimum_precipitation"), //
						resultSet.getFloat("maximum_precipitation"), //
						resultSet.getFloat("minimum_relative_air_humidity"), //
						resultSet.getFloat("maximum_relative_air_humidity"), resultSet.getFloat("wind_speed"), //
						resultSet.getFloat("wind_direction"), //
						resultSet.getFloat("global_radiation"), //
						resultSet.getFloat("minimum_dew_point"), //
						resultSet.getFloat("maximum_dew_point"), //
						resultSet.getFloat("rain")));//
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return diarlyData;
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
				" FROM " + TABLE_INMET_DAILY_DATA + //
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
	protected InmetDiarlyDataDAO init() throws PersistenceException {

		// initializing variables
		List<String> queries = new ArrayList<String>();

		// SQL for entity table create
		queries.add(//
				"CREATE TABLE IF NOT EXISTS " + TABLE_INMET_DAILY_DATA + " ( "//
						+ "id bigserial primary key "//
						+ ", station_id bigserial REFERENCES " + TABLE_INMET_STATION + "(id) "//
//						+ ", station character varying(10) DEFAULT NULL "//
						+ ", measurement_date date DEFAULT NULL "//
						+ ", minimum_temperature real DEFAULT NULL "//
						+ ", maximum_temperature real DEFAULT NULL "//
						+ ", minimum_precipitation real DEFAULT NULL "//
						+ ", maximum_precipitation real DEFAULT NULL "//
						+ ", minimum_relative_air_humidity real DEFAULT NULL "//
						+ ", maximum_relative_air_humidity real DEFAULT NULL "//
						+ ", wind_speed real DEFAULT NULL "//
						+ ", wind_direction integer DEFAULT NULL "//
						+ ", global_radiation real DEFAULT NULL "//
						+ ", minimum_dew_point real DEFAULT NULL "//
						+ ", maximum_dew_point real DEFAULT NULL "//
						+ ", rain real DEFAULT NULL "//
						+ ", unique (id, measurement_date) "//
						+ "); "//
		);

		// initializing table
		super.init(queries);
		return this;
	}

	@Override
	protected InmetDiarlyDataEntity getEntity(ResultSet queryResult) throws PersistenceException {

		Long id = null;
		try {
			// retrieving the attributes
			id = queryResult.getObject("id") != null ? queryResult.getLong("id") : null;

			InmetStationEntity station = this.getStationRelationship(queryResult.getLong("station_id"));

			// creating new entity with attributes retrieved from database
			return new InmetDiarlyDataEntity( //
					queryResult.getLong("id"), //
					station, TimeUtil.stringToLocalDate(queryResult.getString("measurement_date")), //
					queryResult.getFloat("minimum_temperature"), //
					queryResult.getFloat("maximum_temperature"), //
					queryResult.getFloat("minimum_precipitation"), //
					queryResult.getFloat("maximum_precipitation"), //
					queryResult.getFloat("minimum_relative_air_humidity"), //
					queryResult.getFloat("maximum_relative_air_humidity"), //
					queryResult.getFloat("wind_speed"), //
					queryResult.getFloat("wind_direction"), //
					queryResult.getFloat("global_radiation"), //
					queryResult.getFloat("minimum_dew_point"), //
					queryResult.getFloat("maximum_dew_point"), //
					queryResult.getFloat("maximum_dew_point"));//

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
	private InmetDiarlyDataDAO saveStationRelationship(InmetStationEntity entity) throws PersistenceException {
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
	private InmetDiarlyDataDAO removeStationRelationship(Long entityId) {
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
