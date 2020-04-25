package br.embrapa.cnpaf.inmetdata.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Level;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

import br.embrapa.cnpaf.inmetdata.enumerate.MessageEnum;
import br.embrapa.cnpaf.inmetdata.exception.GenericException;
import br.embrapa.cnpaf.inmetdata.exception.PersistenceException;
import br.embrapa.cnpaf.inmetdata.service.ConfigurationService;
import br.embrapa.cnpaf.inmetdata.service.LogService;
import br.embrapa.cnpaf.inmetdata.service.MessageService;
import br.embrapa.cnpaf.inmetdata.service.TimeService;
import br.embrapa.cnpaf.inmetdata.util.ErrorUtil;
import br.embrapa.cnpaf.inmetdata.util.NetworkUtil;

/**
 * <br>
 * <p>
 * <b>Interface with the persistence methods for the entities in the system.</b>
 * </p>
 * <p>
 * It uses the standard Data Access Object design - DAO, in order to separate the other system classes the code needed to interface with the database. For this
 * it defines the methods save, delete, find and list to persist or retrieve the bodies.
 * </p>
 * <br>
 * 
 * @author @author Rubens de Castro Pereira and Sergio Lopes Jr.
 * @version 0.1
 * @since 03/03/2020 (creation date)
 *
 * @param <D>
 *            Concrete class that implements GenericDAO.
 * @param <E>
 *            Entity class to be stored or retrieved by DAO.
 */
public abstract class GenericDAO<D, E> {

	public static final String TABLE_INMET_STATION = "station";
	public static final String TABLE_INMET_CITY = "city";
	public static final String TABLE_INMET_STATE = "state";
	public static final String TABLE_INMET_HOURLY_DATA = "inmet_hourly_data";
	public static final String INDEX_INMET_HOURLY_DATA_STATION_AND_MEASUREMENT_DATE = "idx_INMET_HOURLY_DATA_MEASUREMENT_DATE_AND_STATION_CODE";
	public static final String TABLE_INMET_DAILY_DATA = "inmet_daily_data";
	public static final String INDEX_INMET_DAILY_DATA_STATION_AND_MEASUREMENT_DATE = "idx_INMET_DAILY_DATA_MEASUREMENT_DATE_AND_STATION_CODE";

	public static final Level LOG_DEFAULT_LEVEL = Level.ERROR;
	public static final int DUPLICATE_KEY_ERROR_CODE = 1555; // SQLITE_CONSTRAINT_PRIMARYKEY
	public static final String DATABASE_JDBC_CLASS = "org.postgresql.Driver";
	public static final String DATABASE_JDBC_URI = "jdbc:postgresql://localhost:5432/INMET";
	public static final String DATABASE_JDBC_USER = "postgres";
	public static final String DATABASE_JDBC_PASSWORD = "root";

	private static Connection connection;

	protected final ConfigurationService configurationService;
	protected final MessageService messageService;
	protected final LogService logService;
	protected final TimeService timeService;
	protected TimeBasedGenerator UUIDGenerator;

	/**
	 * Private class constructor.
	 * 
	 * @param logClientName
	 *            Name of the client object of the logging service.
	 * @param logLevel
	 *            Log level to be used in log service.
	 * @throws PersistenceException
	 *             Occurrence of any problems in creating of the DAO.
	 */
	protected GenericDAO(String logClientName, Level logLevel) throws PersistenceException {
		super();

		try {
			this.configurationService = ConfigurationService.getInstanceOf();
			this.messageService = MessageService.getInstanceOf();
			this.logService = new LogService(messageService, logClientName, (logLevel != null) ? logLevel : this.getConfigurationService().getLogLevel());
			this.timeService = TimeService.getInstanceOf();
			this.UUIDGenerator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());

		} catch (Throwable e) {
			throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.GENERIC_DAO_ERROR_CREATE, this.getClass().getSimpleName(), "GenericDAO", e.getMessage(), null, true, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor());
		}
	}

	/**
	 * Retrieves the DAO descriptor.
	 * 
	 * @return DAO descriptor.
	 */
	public String getDAODescriptor() {
		return this.getClass().getSimpleName();
	}

	/**
	 * Saves the state of the attributes of an entity in the database server. During the persistence of a new entity your id is automatically generated and
	 * inserted in it. If it already exists in the database server, its attributes are updated without generating new id.
	 * 
	 * @param entity
	 *            Entity whose attributes you want to save in the database server.
	 * @return Entity retrieved of the database server.
	 * @throws PersistenceException
	 *             An error occurred while saving the entity attributes in the database server.
	 */
	public abstract D save(E entity) throws PersistenceException;

	/**
	 * Exclude an entity previously saved in the database server.
	 * 
	 * @param id
	 *            Identifier (ID) of the entity that you want to delete the database server.
	 * @return DAO instance.
	 * @throws PersistenceException
	 *             An error has occurred when removing the entity in the database server.
	 */
	public abstract D remove(Long id) throws PersistenceException;

	/**
	 * Retrieve the status of the attributes of an entity previously saved in the database server from your identifier (ID).
	 * 
	 * @param id
	 *            Identifier (ID) of the entity whose attributes you want to retrieve the database server.
	 * @return Entity retrieved from the database server.
	 * @throws PersistenceException
	 *             There was an error retrieving the entity attributes in the database server.
	 */
	public abstract E find(Long id) throws PersistenceException;

	/**
	 * Retrieves all entities handled by DAO, previously saved in the database server.
	 * 
	 * @return Entity retrieved of the database server.
	 * @throws PersistenceException
	 *             An error occurred while retrieving the list entities of the database server.
	 */
	public abstract List<E> list() throws PersistenceException;

	/**
	 * Initialize the database, creating the table if it does not already exist.
	 * 
	 * @return DAO instance.
	 * @throws PersistenceException
	 *             An error has occurred when initializing database.
	 */
	protected abstract D init() throws PersistenceException;

	/**
	 * Retrieving the entity from the result of execution of the query to the database server. <br>
	 * Since each entity has a different set of attributes, this method is called by the methods "save" and "find" for the entity's creation after the execution
	 * of the query to the database server.
	 * 
	 * @param queryResult
	 *            Result of execution of the query to the database server.
	 * @return Entity created from the result of execution of the query to the database server.
	 * @throws PersistenceException
	 *             An error has occurred when retrieving the entity.
	 */
	protected abstract E getEntity(ResultSet queryResult) throws PersistenceException;

	/**
	 * Retrieves the configuration service.
	 * 
	 * @return The Configuration Service.
	 */
	protected ConfigurationService getConfigurationService() {
		return configurationService;
	}

	/**
	 * Retrieves the message service.
	 * 
	 * @return Message Service.
	 */
	protected MessageService getMessageService() {
		return messageService;
	}

	/**
	 * Retrieves the log service.
	 * 
	 * @return Log Service.
	 */
	protected LogService getLogService() {
		return logService;
	}

	/**
	 * Retrieves the time service.
	 * 
	 * @return Time Service.
	 */
	protected TimeService getTimeService() {
		return timeService;
	}

	/**
	 * Writing success message with the parameters informed.
	 * 
	 * @param status
	 *            MessageEnum item associated with the success.
	 * @param messageParameters
	 *            Parameter list to be informed in error message formatting retrieved from the messaging service.
	 * @return Entity instance.
	 */
	protected D success(MessageEnum status, String... messageParameters) {
		this.getLogService().info(this.getMessageService().getMessage(status.name(), messageParameters));
		return (D) this;
	}

	/**
	 * Creating an exception with the parameters informed.
	 * 
	 * @param ipAddress
	 *            Ip address where the error occurred.
	 * @param status
	 *            MessageEnum item associated with the error.
	 * @param className
	 *            Class name being performed at the time that this error occurred.
	 * @param methodName
	 *            Method name being performed at the time that this error occurred.
	 * @param exceptionMessage
	 *            Original message of some exception caught in error handling.
	 * @param causes
	 *            Exceptions that caused the current error.
	 * @param writeLog
	 *            If true the error is written to the log.
	 * @param messageParameters
	 *            Parameter list to be informed in error message formatting retrieved from the messaging service.
	 * @return Exception with the parameters informed.
	 */
	protected PersistenceException error(String ipAddress, MessageEnum status, String className, String methodName, String exceptionMessage,
			List<GenericException> causes, boolean writeLog, String... messageParameters) {
		PersistenceException exception = ErrorUtil.getPersistenceExceptionError(ipAddress, status, className, methodName, exceptionMessage, causes, messageParameters);
		try {
			this.getLogService().error(exception.getMessage());
		} catch (Throwable e) {
		}
		return exception;
	}

	/**
	 * Retrieving the connection for access to the database server.
	 * 
	 * @return Connection for access to the database server.
	 */
	protected Connection getConnection() throws PersistenceException {

		// checking if the connection to the database exists
		if (GenericDAO.connection != null) {
			return GenericDAO.connection;
		}

		// creating database connection
		try {
			// loading JDBC driver
			Class.forName(DATABASE_JDBC_CLASS);

			// get database connection
			GenericDAO.connection = DriverManager.getConnection(DATABASE_JDBC_URI, DATABASE_JDBC_USER, DATABASE_JDBC_PASSWORD);
			GenericDAO.connection.setAutoCommit(false);

		} catch (Throwable e) {

			// database connection creation error
			throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.GENERIC_DAO_ERROR_DATABASE_CONNECTION, this.getClass().getSimpleName(), "getConnection", e.getMessage(), null, true, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor());
		}

		// return new database connection created
		return connection;
	}

	/**
	 * Retrieves the entity identifier (ID) by executing the informed query.
	 * 
	 * @param idField
	 *            Name of the database field containing the entity identifier (ID).
	 * @param idQuery
	 *            Query to retrieving entity identifier (ID).
	 * @return Entity identifier (ID).
	 * @throws PersistenceException
	 *             An error occurred while retrieving entity identifier (ID).
	 */
	protected Long getEntityId(String idField, String idQuery) throws PersistenceException {

		// initializing variables
		Connection connection = this.getConnection();
		Statement query = null;

		try {
			// execute sql query
			query = connection.createStatement();
			query.execute(idQuery);
			ResultSet queryResult = query.getResultSet();

			// checking if found any corresponding entity informed the ID
			if (!queryResult.next()) {
				throw new Exception();
			}

			// retrieving the attributes
			Long id = queryResult.getObject(idField) != null ? queryResult.getLong(idField) : null;

			// return the recovered entity
			this.success(MessageEnum.GENERIC_DAO_INFO_SUCCESS_GET_ENTITY_ID, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), idQuery);
			return id;

		} catch (Throwable e) {

			// canceling the transaction
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
				}
			}

			// committing the transaction
			this.commit(connection, query);

			// entity fiding error
			throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.GENERIC_DAO_ERROR_GET_ENTITY_ID, this.getClass().getSimpleName(), "executeQuery", e.getMessage(), null, true, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), idQuery);

		} finally {

			// committing the transaction
			this.commit(connection, query);
		}
	}

	/**
	 * Save or update an entity from informed queries.
	 * 
	 * // * @param newEntity // * True if the entity was never saved in the database, false if it should be updated.
	 * 
	 * @param id
	 *            Identifier (ID) of the entity that you want to update the database server.
	 * @param saveQuery
	 *            Query to save a new entity in the database server.
	 * @param updateQuery
	 *            Query to update an entity previously saved in the database server.
	 * @return DAO instance.
	 * @throws PersistenceException
	 *             An error occurred while saving the entity attributes in the database server.
	 */
	// protected E save(boolean newEntity, String saveQuery, String updateQuery) throws PersistenceException {
	protected Long save(Long id, String saveQuery, String updateQuery) throws PersistenceException {
		
		// initializing variables
		Connection connection = this.getConnection();
		Statement query = null;

		try {
			synchronized (this) {

				// execute sql query
				query = connection.createStatement();
				query.execute((id == null) ? saveQuery : updateQuery, Statement.RETURN_GENERATED_KEYS);

				// checking whether the entity was saved
				int queryResult = query.getUpdateCount();

				// creating the object with id in the database server if it does not exist yet
				// if (queryResult == 0) {
				if (queryResult == 0 && id != null) {
					query.execute(saveQuery);
					queryResult = query.getUpdateCount();
				}

				if (queryResult != 1) {
					// entity saves inconsistently
					throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.GENERIC_DAO_ERROR_SAVE_INCONSISTENTLY, this.getClass().getSimpleName(), "save", null, null, false, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), String.valueOf(id));
				}

				// retrieving the id for new entity
				if (id == null) {
					ResultSet newId = query.getGeneratedKeys();
					if (newId.next()) {
						id = newId.getLong(1);
					} else {
						throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.GENERIC_DAO_ERROR_SAVE_WITHOUT_ID, this.getClass().getSimpleName(), "save", null, null, false, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), String.valueOf(id));
					}
				}
			}

			// return id for the new entity
			this.success(MessageEnum.GENERIC_DAO_INFO_SUCCESS_SAVE, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), String.valueOf(id));

			// return (E) this;
			return id;

		} catch (Throwable e) {

			// canceling the transaction
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
				}
			}

			// entity saving error
			MessageEnum error = (e instanceof SQLException && ((SQLException) e).getErrorCode() == DUPLICATE_KEY_ERROR_CODE) ? MessageEnum.GENERIC_DAO_ERROR_DUPLICATE_KEY : MessageEnum.GENERIC_DAO_ERROR_SAVE;
			throw this.error(NetworkUtil.getLocalIpAddress(), error, this.getClass().getSimpleName(), "save", e.getMessage(), null, true, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), String.valueOf(id));

		} finally {

			// committing the transaction
			this.commit(connection, query);
		}
	}

	/**
	 * Saves the value of an attribute to the entity associated with the informed id.
	 * 
	 * @param id
	 *            Identifier of the entity (id) whose attribute value is to be saved in the database.
	 * @param attribute
	 *            Name of the attribute that you want to save to the database.
	 * @param query
	 *            Query to save a attribute value in the database server.
	 * @return DAO instance.
	 * @throws PersistenceException
	 *             An error occurred while saving the attribute value in the database server.
	 */
	protected E saveAttribute(Long id, String attribute, String attributeQuery) throws PersistenceException {

		// initializing variables
		Connection connection = this.getConnection();
		Statement query = null;

		try {
			// execute sql query
			query = connection.createStatement();
			query.execute(attributeQuery);

			// checking whether the entity was saved
			int queryResult = query.getUpdateCount();

			if (queryResult != 1) {
				// entity saves inconsistently
				throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.GENERIC_DAO_ERROR_SAVE_ATTRIBUTE_INCONSISTENTLY, this.getClass().getSimpleName(), "saveAttribute", null, null, false, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), String.valueOf(id), attribute);
			}

			// return id for the new entity
			this.success(MessageEnum.GENERIC_DAO_INFO_SUCCESS_SAVE_ATTRIBUTE, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), String.valueOf(id), attribute);
			return (E) this;

		} catch (Throwable e) {

			// canceling the transaction
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
				}
			}

			// entity saving error
			throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.GENERIC_DAO_ERROR_SAVE_ATTRIBUTE, this.getClass().getSimpleName(), "saveAttribute", e.getMessage(), null, true, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), String.valueOf(id), attribute);

		} finally {

			// committing the transaction
			this.commit(connection, query);
		}
	}

	/**
	 * Save an relationship keys by informed query.
	 * 
	 * @param id
	 *            Identifier of the entity (id) whose relationship is to be saved in the database.
	 * @param relationship
	 *            Name of the relationship that you want to save to the database.
	 * @param query
	 *            Query to save a relationship in the database server.
	 * @return DAO instance.
	 * @throws PersistenceException
	 *             An error occurred while saving the relationship attributes in the database server.
	 */
	protected E saveRelationship(Long id, String relationship, String relationshipQuery) throws PersistenceException {

		// initializing variables
		Connection connection = this.getConnection();
		Statement query = null;

		try {
			// execute sql query
			query = connection.createStatement();
			query.execute(relationshipQuery);

			// checking whether the entity was saved
			int queryResult = query.getUpdateCount();

			if (queryResult != 1) {
				// entity saves inconsistently
				throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.GENERIC_DAO_ERROR_SAVE_RELATIONSHIP_INCONSISTENTLY, this.getClass().getSimpleName(), "saveRelationship", null, null, false, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), String.valueOf(id), relationship);
			}

			// return id for the new entity
			this.success(MessageEnum.GENERIC_DAO_INFO_SUCCESS_SAVE_RELATIONSHIP, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), String.valueOf(id), relationship);
			return (E) this;

		} catch (Throwable e) {

			// canceling the transaction
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
				}
			}

			// entity saving error
			throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.GENERIC_DAO_ERROR_SAVE_RELATIONSHIP, this.getClass().getSimpleName(), "saveRelationship", e.getMessage(), null, true, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), String.valueOf(id), relationship);

		} finally {

			// committing the transaction
			this.commit(connection, query);
		}
	}

	/**
	 * Remove an entity from informed query.
	 * 
	 * @param id
	 *            Identifier (ID) of the entity that you want to remove the database server.
	 * @param removeQuery
	 *            Query to remove an entity previously saved in the database server.
	 * @return DAO instance.
	 * @throws PersistenceException
	 *             An error occurred while removing the entity attributes in the database server.
	 */
	protected D remove(Long id, String removeQuery) throws PersistenceException {

		// initializing variables
		Connection connection = this.getConnection();
		Statement query = null;

		try {
			// execute sql query
			query = connection.createStatement();
			query.execute(removeQuery);

			// checking whether the entity was removed
			int queryResult = query.getUpdateCount();
			if (queryResult != 1) {
				throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.GENERIC_DAO_ERROR_REMOVE_INCONSISTENTLY, this.getClass().getSimpleName(), "remove", null, null, false, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), String.valueOf(id));
			}

		} catch (Throwable e) {

			// canceling the transaction
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
				}
			}

			// entity removing error
			throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.GENERIC_DAO_ERROR_REMOVE, this.getClass().getSimpleName(), "remove", e.getMessage(), null, true, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), String.valueOf(id));

		} finally {

			// committing the transaction
			this.commit(connection, query);
		}

		// return DAO instance
		this.success(MessageEnum.GENERIC_DAO_INFO_SUCCESS_REMOVE, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), String.valueOf(id));
		return (D) this;
	}

	/**
	 * Remove an relationship keys by informed query.
	 * 
	 * @param id
	 *            Identifier of the entity (id) whose relationship is to be saved in the database.
	 * @param relationship
	 *            Name of the relationship that you want to save to the database.
	 * @param relationshipQuery
	 *            Query to remove a relationship in the database server.
	 * @return DAO instance.
	 * @throws PersistenceException
	 *             An error occurred while removing the relationship attributes in the database server.
	 */
	protected D removeRelationship(Long id, String relationship, String relationshipQuery) throws PersistenceException {

		// initializing variables
		Connection connection = this.getConnection();
		Statement query = null;

		try {
			// execute sql query
			query = connection.createStatement();
			query.execute(relationshipQuery);

		} catch (Throwable e) {

			// canceling the transaction
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
				}
			}

			// entity removing error
			throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.GENERIC_DAO_ERROR_REMOVE_RELATIONSHIP, this.getClass().getSimpleName(), "removeRelationship", e.getMessage(), null, true, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), String.valueOf(id), relationship);

		} finally {

			// committing the transaction
			this.commit(connection, query);
		}

		// return DAO instance
		this.success(MessageEnum.GENERIC_DAO_INFO_SUCCESS_REMOVE_RELATIONSHIP, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), String.valueOf(id), relationship);
		return (D) this;
	}

	/**
	 * Retrieving an entity from informed query.
	 * 
	 * @param id
	 *            Identifier (ID) of the entity that you want to retrieving the database server.
	 * @param findQuery
	 *            Query to retrieve an entity previously saved in the database server.
	 * @throws PersistenceException
	 *             An error occurred while retrieving the entity attributes in the database server.
	 */
	protected E find(Long id, String findQuery) throws PersistenceException {
		
		// initializing variables
		Connection connection = this.getConnection();
		Statement query = null;

		try {
			// execute sql query
			query = connection.createStatement();
			query.execute(findQuery);
			ResultSet queryResult = query.getResultSet();

			// checking if found any corresponding entity informed the ID
			if (!queryResult.next()) {
				throw new Exception();
			}

			// return the recovered entity
			this.success(MessageEnum.GENERIC_DAO_INFO_SUCCESS_FIND, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), String.valueOf(id));
			return this.getEntity(queryResult);

		} catch (Throwable e) {

			// canceling the transaction
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
				}
			}

			// entity fiding error
			throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.GENERIC_DAO_ERROR_FIND, this.getClass().getSimpleName(), "find", e.getMessage(), null, true, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor(), String.valueOf(id));

		} finally {

			// committing the transaction
			this.commit(connection, query);
		}
	}

	/**
	 * Retrieving all entities from informed query.
	 * 
	 * @param queryListAll
	 *            Query to retrieving all entities previously saved in the database server.
	 * @throws PersistenceException
	 *             An error occurred while retrieving all entities attributes in the database server.
	 */
	protected List<E> list(String queryListAll) throws PersistenceException {

		// initializing variables
		Connection connection = this.getConnection();
		Statement query = null;

		try {
			// execute sql query
			query = connection.createStatement();
			query.execute(queryListAll);
			ResultSet queryResult = query.getResultSet();

			// retrieving all entities
			List<E> entities = new ArrayList<E>();
			while (queryResult.next()) {

				// add entity into list
				entities.add(this.getEntity(queryResult));
			}

			// return the list of all the recovered entities
			this.success(MessageEnum.GENERIC_DAO_INFO_SUCCESS_LIST_ALL, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor());
			return entities;

		} catch (Throwable e) {

			// canceling the transaction
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
				}
			}

			// list all entities error
			throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.GENERIC_DAO_ERROR_LIST_ALL, this.getClass().getSimpleName(), "listAll", e.getMessage(), null, true, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor());

		} finally {

			// committing the transaction
			this.commit(connection, query);
		}

	}

	/**
	 * Queries for creating tables, if there are in the database server.
	 * 
	 * @param queries
	 *            Queries for creating tables.
	 * @return DAO instance.
	 * @throws PersistenceException
	 *             An error has occurred when creating tables.
	 */
	protected D init(List<String> queries) throws PersistenceException {

		// initializing query statement
		Connection connection = this.getConnection();
		Statement query = null;

		try {
			// initializing variables
			query = connection.createStatement();

			// execute sql queries
			for (String queryText : queries) {
				query.execute(queryText);
			}

			// DAO initialization success
			this.success(MessageEnum.GENERIC_DAO_INFO_SUCCESS_INIT, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor());
			return (D) this;

		} catch (Throwable e) {

			// canceling the transaction
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
				}
			}

			// DAO initializing error
			throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.GENERIC_DAO_ERROR_INIT, this.getClass().getSimpleName(), "init", e.getMessage(), null, true, NetworkUtil.getLocalIpAddress(), this.getDAODescriptor());

		} finally {

			// committing the transaction
			this.commit(connection, query);
		}
	}

	/**
	 * Commits the transaction.
	 * 
	 * @param connection
	 *            Connection whose transaction is to be committed.
	 * @param query
	 *            Query to be terminated after transaction commit.
	 * @return DAO instance.
	 */
	protected D commit(Connection connection, Statement query) {

		// committing the transaction
		if (connection != null) {
			try {
				connection.commit();
			} catch (SQLException e) {
			}
		}

		// closes sql statement and connection
		if (query != null) {
			try {
				query.close();
			} catch (SQLException e) {
			}
		}

		// return DAO instace
		return (D) this;
	}

	/**
	 * Generate unique ID (UUID).
	 * 
	 * @return Unique ID (UUID).
	 */
	protected UUID generateId() {
		return this.UUIDGenerator.generate();
	}

}
