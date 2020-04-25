package br.embrapa.cnpaf.inmetdata.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;

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
public class InmetStationDAO extends GenericDAO<InmetStationDAO, InmetStationEntity> {

	private static InmetStationDAO instance;

	/**
	 * Private class constructor.
	 * 
	 * @param logClientName Name of the client object of the logging service.
	 * @param logLevel      Log level to be used in log service.
	 * @throws PersistenceException Occurrence of any problems in creating of the
	 *                              DAO.
	 */
	private InmetStationDAO(String logClientName, Level logLevel) throws PersistenceException {
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
	public static synchronized InmetStationDAO getInstanceOf(String logClientName, Level logLevel)
			throws PersistenceException {
		if (InmetStationDAO.instance == null) {
			InmetStationDAO.instance = new InmetStationDAO(logClientName, logLevel);
		}
		return InmetStationDAO.instance;
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
	public static synchronized InmetStationDAO getInstanceOf() throws PersistenceException {
		return InmetStationDAO.getInstanceOf(InmetStationDAO.class.getSimpleName(), LOG_DEFAULT_LEVEL);
	}

	@Override
	public InmetStationDAO save(InmetStationEntity entity) throws PersistenceException {

		// validate entity
		Long id = entity != null ? entity.getId() : null;

		// saving moduleAddress relationship with module address entity
//		this.saveStationRelationship(entity.getStation());

		// save ou update the entity
		id = super.save(//
				entity.getId() //
				, "INSERT INTO " + TABLE_INMET_STATION + "(" + //
						"code," + //
						"latitude," + //
						"longitude," + //
						"city," + //
						"state," + //
						"start_date)" + //
						"VALUES(" + //
						"'" + entity.getCode() + "'" + "," + //
						+entity.getLatitude() + "," + //
						+entity.getLongitude() + "," + //
						"'" + entity.getCity() + "'" + "," + //
						"'" + entity.getState() + "'" + "," + //
						"'" + entity.getStartDate() + "'" + //
						")" + ";",
				"UPDATE " + TABLE_INMET_STATION + " SET " //
						+ "code=" + "'" + entity.getCode() + "'" + "," //
						+ "latitude=" + entity.getLatitude() + "," //
						+ "longitude=" + entity.getLongitude()  + "," //
						+ "city=" + "'" + entity.getCity() +  "'"  + "," //
						+ "state=" + "'" + entity.getState() + "'"  + ","//
						+ "start_date=" + "'" + entity.getStartDate() + "'" //
						+ "WHERE id=" + entity.getId() //
						+ ";");

		// return DAO instance
		return this;
	}

	@Override
	public InmetStationDAO remove(Long id) throws PersistenceException {

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
			super.remove(id, "DELETE FROM " + TABLE_INMET_STATION + " WHERE id=" + id + ";");

			// removing remotesModulesAddresses relationship with module address entity
//			this.removeStationRelationship(station.getId());

		} catch (PersistenceException e) {
			throw (e);
		}

		// return DAO instance
		return this;
	}

	@Override
	public InmetStationEntity find(Long id) throws PersistenceException {
		return super.find(id, "SELECT * FROM " + TABLE_INMET_STATION + " WHERE id=" + id + ";");
	}

	@Override
	public List<InmetStationEntity> list() throws PersistenceException {
		return super.list("SELECT * FROM " + TABLE_INMET_STATION + ";");
	}

	@Override
	public InmetStationDAO init() throws PersistenceException {

		// initializing variables
		List<String> queries = new ArrayList<String>();

		// SQL for entity table create
		queries.add(//
				"CREATE TABLE IF NOT EXISTS public." + TABLE_INMET_STATION + // n
						"(" + //
						"id  bigserial NOT NULL," + //
						"code text NOT NULL," + //
						"latitude double precision NOT NULL," + //
						"longitude double precision NOT NULL," + //
						"city text NOT NULL," + //
						"state character(2) NOT NULL," + //
						"start_date date NOT NULL," + //
						"end_date date NOT NULL," + //
						" PRIMARY KEY (id)" + //
						")"// n
		);
		// initializing table
		super.init(queries);
		return this;
	}

	@Override
	protected InmetStationEntity getEntity(ResultSet queryResult) throws PersistenceException {

		Long id = null;
		try {
			// retrieving the attributes
			id = queryResult.getObject("id") != null ? queryResult.getLong("id") : null;
			// InmetStationEntity station =
			// this.getStationRelationship(queryResult.getLong("station_id"));

			// creating new entity with attributes retrieved from database
			return new InmetStationEntity(//
					queryResult.getLong("id"), //
					queryResult.getString("code"), //
					queryResult.getDouble("latitude"), //
					queryResult.getDouble("longitude"), //
					queryResult.getString("city"), //
					queryResult.getString("state"), //
					TimeUtil.stringToLocalDate(queryResult.getString("start_date"))
					//
			);

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
	private InmetStationDAO saveStationRelationship(InmetStationEntity entity) throws PersistenceException {
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
	private InmetStationDAO removeStationRelationship(Long entityId) {
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
