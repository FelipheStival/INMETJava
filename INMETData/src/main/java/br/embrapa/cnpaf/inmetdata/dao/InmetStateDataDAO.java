package br.embrapa.cnpaf.inmetdata.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;

import br.embrapa.cnpaf.inmetdata.entity.InmetStateEntily;
import br.embrapa.cnpaf.inmetdata.entity.InmetStationEntity;
import br.embrapa.cnpaf.inmetdata.enumerate.MessageEnum;
import br.embrapa.cnpaf.inmetdata.exception.PersistenceException;
import br.embrapa.cnpaf.inmetdata.util.NetworkUtil;

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
public class InmetStateDataDAO extends GenericDAO<InmetStateDataDAO, InmetStateEntily> {

	private static InmetStateDataDAO instance;

	/**
	 * Private class constructor.
	 * 
	 * @param logClientName Name of the client object of the logging service.
	 * @param logLevel      Log level to be used in log service.
	 * @throws PersistenceException Occurrence of any problems in creating of the
	 *                              DAO.
	 */
	private InmetStateDataDAO(String logClientName, Level logLevel) throws PersistenceException {
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
	public static synchronized InmetStateDataDAO getInstanceOf(String logClientName, Level logLevel)
			throws PersistenceException {
		if (InmetStateDataDAO.instance == null) {
			InmetStateDataDAO.instance = new InmetStateDataDAO(logClientName, logLevel);
		}
		return InmetStateDataDAO.instance;
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
	public static synchronized InmetStateDataDAO getInstanceOf() throws PersistenceException {
		return InmetStateDataDAO.getInstanceOf(InmetStateDataDAO.class.getSimpleName(), LOG_DEFAULT_LEVEL);
	}

	@Override
	public InmetStateDataDAO save(InmetStateEntily entity) throws PersistenceException {

		// validate entity
		Long id = entity != null ? entity.getId() : null;

		// save ou update the entity
		id = super.save(//
				id //
				,
				"INSERT INTO public." + TABLE_INMET_STATE + "(" +
						"name" + ")" +
						" VALUES " +
						"(" + "'" +  entity.getName() + "'" + ");"
				, "UPDATE public." + TABLE_INMET_STATE +  " SET " +
				  "name=" + "'" + entity.getName() + "'" +
				  " WHERE id=" +  entity.getId());

		// return DAO instance
		return this;
	}

	@Override
	public InmetStateDataDAO remove(Long id) throws PersistenceException {

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
	public InmetStateEntily find(Long id) throws PersistenceException {
		return super.find(id, "SELECT * FROM " + TABLE_INMET_STATE + " WHERE id=" + id + ";");
	}

	@Override
	public List<InmetStateEntily> list() throws PersistenceException {
		return super.list("SELECT * FROM " + TABLE_INMET_STATE + ";");
	}

	@Override
	protected InmetStateDataDAO init() throws PersistenceException {

		// initializing variables
		List<String> queries = new ArrayList<String>();

		// SQL for entity table create
		queries.add(//
				"CREATE TABLE IF NOT EXISTS public." + TABLE_INMET_STATE + //
						"(" + //
						"id bigserial NOT NULL," + //
						"name text NOT NULL," + //
						"PRIMARY KEY (id)" + //
						");");

		// initializing table
		super.init(queries);

		// Iniciando populacao
		List<InmetStateEntily> stateEntilies = this.list();
		if (stateEntilies.size() == 0) {
			queries.clear();
			queries.add("INSERT INTO " + TABLE_INMET_STATE + "(name)" + //
					"VALUES " + //
					"('GO'),\r\n" + //
					" ('MT'),\r\n" + //
					" ('RO'),\r\n" + //
					" ('TO'),\r\n" + //
					" ('PA'),\r\n" + //
					" ('MA'),\r\n" + //
					" ('PI'),\r\n" + //
					" ('CE'),\r\n" + //
					" ('DF'),\r\n" + //
					" ('SE'),\r\n" + //
					" ('SP'),\r\n" + //
					" ('RN'),\r\n" + //
					" ('PB'),\r\n" + //
					" ('PE'),\r\n" + //
					" ('AC'),\r\n" + //
					" ('AL'),\r\n" + //
					" ('AM'),\r\n" + //
					" ('AP'),\r\n" + //
					" ('BA'),\r\n" + //
					" ('ES'),\r\n" + //
					" ('MG'),\r\n" + //
					" ('MS'),\r\n" + //
					" ('PR'),\r\n" + //
					" ('RJ'),\r\n" + //
					" ('RS'),\r\n" + //
					" ('SC')\r\n" + //
					""); //

			this.init(queries);

		}
		return this;
	}

	@Override
	protected InmetStateEntily getEntity(ResultSet queryResult) throws PersistenceException {

		Long id = null;
		try {
			// retrieving the attributes
			id = queryResult.getObject("id") != null ? queryResult.getLong("id") : null;

			// creating new entity with attributes retrieved from database
			return new InmetStateEntily(
					id, //
					queryResult.getString("name") //
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
	private InmetStateDataDAO saveStationRelationship(InmetStationEntity entity) throws PersistenceException {
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
	private InmetStateDataDAO removeStationRelationship(Long entityId) {
		try {
			InmetStationDAO.getInstanceOf().remove(entityId);
		} catch (Throwable e) {
		}
		return this;
	}

}
