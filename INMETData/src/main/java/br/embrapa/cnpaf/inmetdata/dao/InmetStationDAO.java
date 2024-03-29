package br.embrapa.cnpaf.inmetdata.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;

import br.embrapa.cnpaf.inmetdata.entity.InmetCityEntily;
import br.embrapa.cnpaf.inmetdata.entity.InmetStateEntily;
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
		this.saveStationRelationship(entity.getCityEntily());

		// save ou update the entity
		id = super.save(//
				entity.getId() //
				, "INSERT INTO public." + TABLE_INMET_STATION + "(" + //
						"id_city," + //
						"code," + "start_date)" + //
						"VALUES (" + //
						entity.getCityEntily().getId() + "," + //
						"'" + entity.getCode() + "'" + "," + //
						"'" + entity.getStartDate() + "'" + ");"//
				, //
				"UPDATE public." + TABLE_INMET_STATION + " SET " + //
						"id_city=" + entity.getCityEntily().getId() + "," + //
						"code=" + "'" + entity.getCode() + "'" + "," + //
						"start_date=" + "'" + entity.getStartDate() + "'" + //
						"WHERE id=" + entity.getId()); //

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
				"CREATE TABLE IF NOT EXISTS public." + TABLE_INMET_STATION + //
						"(" + //
						"id bigserial NOT NULL," + //
						"id_city bigserial NOT NULL REFERENCES " + TABLE_INMET_CITY + "(id)," + //
						"code text NOT NULL," + //
						"start_date date NOT NULL," + //
						"PRIMARY KEY (id)" + //
						");"//
		);

		super.init(queries);
		
		return this;
	}

	@Override
	protected InmetStationEntity getEntity(ResultSet queryResult) throws PersistenceException {

		Long id = null;
		try {
			// retrieving the attributes
			id = queryResult.getObject("id") != null ? queryResult.getLong("id") : null;

			// Recovering relationship
			Long idCity = queryResult.getLong("id_city");
			InmetCityEntily cityEntily = this.getCityRelationship(idCity);

			// creating new entity with attributes retrieved from database
			return new InmetStationEntity( //
					queryResult.getLong("id"), //
					queryResult.getString("code"), //
					cityEntily, //
					TimeUtil.stringToLocalDate(queryResult.getString("start_date"))//
			); //

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
	private InmetStationDAO saveStationRelationship(InmetCityEntily entity) throws PersistenceException {
		if (entity != null) {
			InmetCityDataDAO.getInstanceOf().save(entity);
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
	private InmetStationDAO removeCityRelationship(Long entityId) {
		try {
			InmetCityDataDAO.getInstanceOf().remove(entityId);
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
	private InmetCityEntily getCityRelationship(Long entityId) throws PersistenceException {
		if (entityId != null) {
			return InmetCityDataDAO.getInstanceOf().find(entityId);
		}
		return null;
	}
	
	/**
	 *This method inserts the records if the table is empty
	 * 
	 * @throws PersistenceException Occurrence of any problems in retrieving of the
	 *                              relationship.
	 */
	public void startRecords() throws PersistenceException {

		List<InmetStationEntity> stateEntilies = this.list();
		List<String> queries = new ArrayList<String>();

		if (stateEntilies.size() == 0) {
			queries.add("INSERT INTO station(id_city,code,start_date) " //
					+ "VALUES " //
					+ "(298,'A422','2008-07-20'),"  //
					+ "(160,'A360','2009-04-21'),"  //
					+ "(345,'A657','2011-09-24'),"  //
					+ "(26,'A908','2006-12-15'),"  //
					+ "(424,'A756','2010-08-14'),"  //
					+ "(176,'A045','2008-10-01'),"  //
					+ "(358,'A549','2007-09-08'),"  //
					+ "(359,'A534','2007-08-04'),"  //
					+ "(346,'A617','2006-10-24'),"  //
					+ "(515,'A826','2006-09-28'),"  //
					+ "(347,'A615','2006-11-01'),"  //
					+ "(69,'A053','2016-03-31'),"  //
					+ "(360,'A508','2002-12-08'),"  //
					+ "(27,'A924','2007-05-22'),"  //
					+ "(89,'A253','2018-10-24'),"  //
					+ "(28,'A909','2011-09-21'),"  //
					+ "(1,'A024','2007-05-01'),"  //
					+ "(121,'A223','2008-06-02'),"  //
					+ "(29,'A934','2008-01-28'),"  //
					+ "(138,'A336','2007-11-16'),"  //
					+ "(425,'A750','2008-06-11'),"  //
					+ "(299,'A434','2008-07-10'),"  //
					+ "(426,'S701','2018-04-08'),"  //
					+ "(139,'A377','2019-11-13'),"  //
					+ "(490,'A628','2017-08-24'),"  //
					+ "(30,'A910','2006-10-23'),"  //
					+ "(230,'A340','2007-11-13'),"  //
					+ "(275,'A113','2008-07-18'),"  //
					+ "(427,'A719','2006-10-31'),"  //
					+ "(181,'A409','2003-02-11'),"  //
					+ "(361,'A566','2017-05-20'),"  //
					+ "(2,'A013','2007-07-12'),"  //
					+ "(70,'A054','2015-08-18'),"  //
					+ "(71,'A021','2007-01-25'),"  //
					+ "(72,'A044','2009-03-11'),"  //
					+ "(428,'S702','2018-04-12'),"  //
					+ "(559,'A867','2008-09-27'),"  //
					+ "(268,'A353','2008-04-26'),"  //
					+ "(362,'A505','2002-12-18'),"  //
					+ "(247,'A309','2004-11-19'),"  //
					+ "(238,'A310','2004-11-15'),"  //
					+ "(65,'A940','2008-07-17'),"  //
					+ "(188,'A736','2007-11-14'),"  //
					+ "(491,'A606','2006-09-21'),"  //
					+ "(276,'A120','2008-04-16'),"  //
					+ "(189,'A725','2006-09-21'),"  //
					+ "(122,'A220','2008-07-08'),"  //
					+ "(516,'A827','2007-01-03'),"  //
					+ "(140,'A375','2018-03-22'),"  //
					+ "(123,'A204','2007-10-25'),"  //
					+ "(363,'A565','2016-11-21'),"  //
					+ "(364,'A502','2002-12-04'),"  //
					+ "(161,'A315','2007-05-30'),"  //
					+ "(277,'A128','2008-04-18'),"  //
					+ "(300,'A429','2008-05-11'),"  //
					+ "(190,'A741','2008-04-27'),"  //
					+ "(124,'A221','2008-03-05'),"  //
					+ "(191,'A746','2008-07-03'),"  //
					+ "(301,'A402','2001-12-19'),"  //
					+ "(192,'A748','2010-06-18'),"  //
					+ "(193,'A755','2011-03-22'),"  //
					+ "(429,'A759','2013-03-20'),"  //
					+ "(194,'A705','2001-08-29'),"  //
					+ "(195,'A764','2016-11-26'),"  //
					+ "(430,'A757','2011-08-08'),"  //
					+ "(90,'A201','2003-01-19'),"  //
					+ "(302,'A447','2009-07-01'),"  //
					+ "(365,'F501','2014-09-30'),"  //
					+ "(366,'A521','2006-10-09'),"  //
					+ "(517,'A840','2006-11-30'),"  //
					+ "(196,'A765','2017-01-31'),"  //
					+ "(278,'A110','2008-07-14'),"  //
					+ "(303,'A418','2007-05-17'),"  //
					+ "(141,'A326','2007-07-16'),"  //
					+ "(91,'A226','2008-03-10'),"  //
					+ "(197,'A744','2017-12-19'),"  //
					+ "(431,'S705','2018-04-05'),"  //
					+ "(177,'A001','2000-05-06'),"  //
					+ "(178,'A042','2017-07-18'),"  //
					+ "(182,'A421','2008-07-16'),"  //
					+ "(92,'A228','2017-10-20'),"  //
					+ "(304,'A433','2008-04-27'),"  //
					+ "(125,'A238','2008-09-10'),"  //
					+ "(305,'A432','2008-05-14'),"  //
					+ "(367,'A544','2007-06-18'),"  //
					+ "(432,'S706','2018-03-05'),"  //
					+ "(239,'A348','2008-02-27'),"  //
					+ "(248,'A329','2007-09-03'),"  //
					+ "(560,'A859','2008-03-18'),"  //
					+ "(518,'A812','2006-06-21'),"  //
					+ "(31,'A941','2012-03-29'),"  //
					+ "(198,'A769','2017-10-19'),"  //
					+ "(66,'A939','2008-07-19'),"  //
					+ "(3,'A023','2007-05-20'),"  //
					+ "(231,'A316','2007-01-06'),"  //
					+ "(232,'A344','2008-05-07'),"  //
					+ "(368,'A530','2006-11-27'),"  //
					+ "(519,'A838','2006-12-11'),"  //
					+ "(240,'A352','2008-05-03'),"  //
					+ "(520,'A897','2016-11-23'),"  //
					+ "(492,'A604','2002-11-19'),"  //
					+ "(93,'A236','2008-06-19'),"  //
					+ "(464,'B803','2016-08-03'),"  //
					+ "(241,'A313','2006-12-21'),"  //
					+ "(369,'A519','2003-07-13'),"  //
					+ "(521,'A884','2013-11-28'),"  //
					+ "(433,'A702','2001-09-10'),"  //
					+ "(142,'A376','2018-11-22'),"  //
					+ "(32,'A905','2012-12-11'),"  //
					+ "(33,'A912','2006-11-20'),"  //
					+ "(494,'A620','2008-06-12'),"  //
					+ "(199,'A706','2002-03-13'),"  //
					+ "(493,'A607','2006-09-21'),"  //
					+ "(73,'A043','2012-10-28'),"  //
					+ "(561,'A898','2019-02-14'),"  //
					+ "(162,'A347','2008-03-06'),"  //
					+ "(522,'A879','2008-08-22'),"  //
					+ "(523,'A811','2007-01-23'),"  //
					+ "(143,'A365','2010-06-11'),"  //
					+ "(524,'A887','2019-07-17'),"  //
					+ "(370,'A541','2007-09-01'),"  //
					+ "(94,'A248','2011-04-17'),"  //
					+ "(144,'A337','2007-11-09'),"  //
					+ "(371,'A554','2007-05-22'),"  //
					+ "(306,'A405','2002-12-19'),"  //
					+ "(183,'A420','2008-03-27'),"  //
					+ "(34,'A926','2008-04-09'),"  //
					+ "(495,'A629','2018-10-10'),"  //
					+ "(126,'A205','2007-12-03'),"  //
					+ "(249,'A341','2007-11-20'),"  //
					+ "(200,'A738','2007-06-27'),"  //
					+ "(434,'A742','2008-03-07'),"  //
					+ "(95,'A202','2003-01-23'),"  //
					+ "(145,'A361','2009-04-19'),"  //
					+ "(465,'A819','2006-07-08'),"  //
					+ "(4,'A034','2008-01-30'),"  //
					+ "(127,'A237','2008-07-12'),"  //
					+ "(372,'A548','2007-06-21'),"  //
					+ "(435,'A730','2006-12-18'),"  //
					+ "(128,'A206','2008-09-16'),"  //
					+ "(562,'A895','2019-02-17'),"  //
					+ "(466,'A869','2008-03-09'),"  //
					+ "(467,'A876','2008-05-15'),"  //
					+ "(279,'A117','2008-04-11'),"  //
					+ "(129,'A222','2008-06-02'),"  //
					+ "(74,'A049','2016-11-11'),"  //
					+ "(468,'B806','2016-05-27'),"  //
					+ "(35,'A913','2006-11-27'),"  //
					+ "(373,'A520','2006-07-15'),"  //
					+ "(96,'A241','2008-09-03'),"  //
					+ "(307,'A431','2008-04-06'),"  //
					+ "(374,'A557','2012-10-16'),"  //
					+ "(146,'A374','2018-03-27'),"  //
					+ "(308,'A416','2007-11-08'),"  //
					+ "(436,'A724','2006-10-26'),"  //
					+ "(269,'A355','2008-09-05'),"  //
					+ "(437,'A760','2012-12-03'),"  //
					+ "(36,'A919','2007-05-26'),"  //
					+ "(438,'A720','2006-11-06'),"  //
					+ "(163,'A342','2008-12-10'),"  //
					+ "(5,'A036','2007-12-12'),"  //
					+ "(5,'A056','2018-01-24'),"  //
					+ "(525,'A853','2007-05-30'),"  //
					+ "(309,'A406','2003-01-18'),"  //
					+ "(261,'A108','2015-05-13'),"  //
					+ "(37,'A901','2002-12-06'),"  //
					+ "(310,'A448','2015-08-19'),"  //
					+ "(469,'A807','2003-01-27'),"  //
					+ "(563,'A860','2008-03-02'),"  //
					+ "(375,'A538','2006-12-18'),"  //
					+ "(311,'A443','2008-07-14'),"  //
					+ "(470,'A849','2008-03-07'),"  //
					+ "(376,'A537','2006-06-04'),"  //
					+ "(75,'A038','2008-08-29'),"  //
					+ "(564,'A848','2008-05-30'),"  //
					+ "(377,'A564','2017-10-25'),"  //
					+ "(471,'A843','2007-03-28'),"  //
					+ "(97,'A252','2018-03-07'),"  //
					+ "(526,'A881','2010-04-22'),"  //
					+ "(378,'A536','2007-05-31'),"  //
					+ "(439,'A721','2006-10-20'),"  //
					+ "(201,'A762','2016-11-02'),"  //
					+ "(496,'A603','2002-10-20'),"  //
					+ "(348,'A631','2017-03-16'),"  //
					+ "(7,'A029','2017-08-25'),"  //
					+ "(280,'A109','2012-08-30'),"  //
					+ "(527,'A893','2018-03-14'),"  //
					+ "(262,'A140','2009-09-24'),"  //
					+ "(528,'A828','2006-11-24'),"  //
					+ "(130,'A224','2008-02-26'),"  //
					+ "(312,'A442','2008-03-30'),"  //
					+ "(131,'A218','2008-11-21'),"  //
					+ "(132,'A217','2008-11-27'),"  //
					+ "(263,'A138','2009-09-23'),"  //
					+ "(313,'A413','2007-05-25'),"  //
					+ "(250,'A351','2008-09-14'),"  //
					+ "(379,'A535','2008-06-26'),"  //
					+ "(147,'A311','2004-11-17'),"  //
					+ "(565,'A806','2003-01-21'),"  //
					+ "(380,'A524','2006-08-16'),"  //
					+ "(314,'A452','2016-06-01'),"  //
					+ "(76,'A039','2008-04-26'),"  //
					+ "(164,'A305','2003-02-17'),"  //
					+ "(472,'A846','2008-02-14'),"  //
					+ "(202,'A708','2002-12-09'),"  //
					+ "(529,'A854','2007-12-12'),"  //
					+ "(179,'A046','2014-09-30'),"  //
					+ "(251,'A322','2007-07-07'),"  //
					+ "(38,'A930','2008-06-05'),"  //
					+ "(473,'A875','2008-04-09'),"  //
					+ "(148,'A364','2009-05-15'),"  //
					+ "(8,'A022','2007-06-01'),"  //
					+ "(9,'A002','2001-05-29'),"  //
					+ "(10,'A014','2007-07-15'),"  //
					+ "(474,'A825','2006-10-18'),"  //
					+ "(381,'A532','2007-05-28'),"  //
					+ "(133,'A207','2008-09-09'),"  //
					+ "(315,'A426','2008-04-23'),"  //
					+ "(382,'A533','2007-06-01'),"  //
					+ "(165,'A314','2007-05-25'),"  //
					+ "(39,'A906','2002-12-19'),"  //
					+ "(383,'A546','2007-07-10'),"  //
					+ "(40,'A932','2008-01-24'),"  //
					+ "(77,'A019','2006-12-19'),"  //
					+ "(281,'A112','2008-04-24'),"  //
					+ "(252,'A349','2008-10-13'),"  //
					+ "(384,'A555','2008-06-06'),"  //
					+ "(530,'A883','2012-12-12'),"  //
					+ "(203,'A737','2007-11-08'),"  //
					+ "(316,'A439','2008-04-27'),"  //
					+ "(475,'A824','2006-11-14'),"  //
					+ "(204,'A712','2006-07-18'),"  //
					+ "(440,'S709','2018-04-13'),"  //
					+ "(166,'A319','2007-05-29'),"  //
					+ "(317,'A410','2003-01-24'),"  //
					+ "(134,'A225','2008-02-29'),"  //
					+ "(476,'A823','2006-10-15'),"  //
					+ "(566,'A817','2006-07-01'),"  //
					+ "(443,'A709','2003-02-09'),"  //
					+ "(233,'A372','2017-11-07'),"  //
					+ "(318,'A445','2009-06-29'),"  //
					+ "(11,'A028','2013-06-23'),"  //
					+ "(319,'A424','2008-03-30'),"  //
					+ "(184,'A451','2017-04-23'),"  //
					+ "(185,'A417','2007-05-22'),"  //
					+ "(320,'A408','2003-01-28'),"  //
					+ "(282,'A121','2008-04-16'),"  //
					+ "(98,'A231','2008-02-16'),"  //
					+ "(567,'A868','2010-06-23'),"  //
					+ "(321,'A455','2016-05-27'),"  //
					+ "(385,'A550','2007-09-04'),"  //
					+ "(12,'A015','2007-02-02'),"  //
					+ "(322,'A446','2009-06-24'),"  //
					+ "(205,'A714','2006-07-24'),"  //
					+ "(167,'A359','2008-09-04'),"  //
					+ "(206,'A739','2007-11-12'),"  //
					+ "(568,'A851','2007-06-05'),"  //
					+ "(441,'S710','2018-02-25'),"  //
					+ "(242,'A373','2017-11-02'),"  //
					+ "(442,'A752','2008-05-30'),"  //
					+ "(497,'A635','2017-08-31'),"  //
					+ "(294,'A251','2017-08-23'),"  //
					+ "(41,'A933','2008-08-13'),"  //
					+ "(323,'A407','2003-02-08'),"  //
					+ "(386,'A512','2006-05-10'),"  //
					+ "(13,'A035','2007-11-01'),"  //
					+ "(569,'A863','2008-03-04'),"  //
					+ "(207,'A753','2008-08-16'),"  //
					+ "(477,'A818','2006-07-11'),"  //
					+ "(324,'A440','2008-04-30'),"  //
					+ "(531,'A836','2007-01-08'),"  //
					+ "(168,'A358','2008-09-09'),"  //
					+ "(169,'A339','2007-11-09'),"  //
					+ "(208,'A733','2007-08-22'),"  //
					+ "(387,'A563','2016-11-18'),"  //
					+ "(388,'A559','2016-06-03'),"  //
					+ "(478,'A871','2008-02-27'),"  //
					+ "(444,'A758','2011-08-03'),"  //
					+ "(14,'A016','2007-05-23'),"  //
					+ "(325,'A450','2015-08-12'),"  //
					+ "(570,'A841','2007-09-18'),"  //
					+ "(243,'A320','2007-07-20'),"  //
					+ "(389,'A553','2007-07-06'),"  //
					+ "(479,'A821','2006-11-22'),"  //
					+ "(209,'A735','2007-09-03'),"  //
					+ "(42,'A914','2006-11-01'),"  //
					+ "(43,'A920','2006-11-30'),"  //
					+ "(390,'A518','2007-05-25'),"  //
					+ "(445,'A749','2008-06-17'),"  //
					+ "(283,'A111','2008-07-26'),"  //
					+ "(78,'A055','2016-11-14'),"  //
					+ "(532,'A844','2007-03-01'),"  //
					+ "(572,'A866','2008-05-30'),"  //
					+ "(446,'S711','2018-03-06'),"  //
					+ "(571,'A865','2014-11-05'),"  //
					+ "(480,'B804','2016-08-06'),"  //
					+ "(326,'A425','2008-04-04'),"  //
					+ "(349,'A614','2006-10-26'),"  //
					+ "(210,'A727','2006-09-19'),"  //
					+ "(327,'A404','2002-04-17'),"  //
					+ "(15,'A012','2006-10-20'),"  //
					+ "(498,'A608','2006-09-21'),"  //
					+ "(328,'A412','2003-02-02'),"  //
					+ "(295,'A249','2013-12-11'),"  //
					+ "(234,'A317','2007-01-05'),"  //
					+ "(270,'A303','2003-02-24'),"  //
					+ "(391,'A567','2017-07-20'),"  //
					+ "(573,'A864','2009-02-08'),"  //
					+ "(284,'A119','2004-04-09'),"  //
					+ "(285,'A101','2000-05-08'),"  //
					+ "(392,'A556','2010-09-24'),"  //
					+ "(286,'A133','2011-12-11'),"  //
					+ "(393,'A540','2007-07-31'),"  //
					+ "(99,'A240','2009-06-24'),"  //
					+ "(447,'A731','2006-12-13'),"  //
					+ "(329,'A438','2008-06-21'),"  //
					+ "(481,'A820','2006-11-17'),"  //
					+ "(264,'A137','2009-10-26'),"  //
					+ "(394,'A531','2006-11-30'),"  //
					+ "(79,'A041','2012-11-23'),"  //
					+ "(350,'A632','2017-03-19'),"  //
					+ "(211,'A763','2017-05-12'),"  //
					+ "(482,'A835','2006-11-20'),"  //
					+ "(80,'A040','2012-10-24'),"  //
					+ "(287,'A122','2008-04-21'),"  //
					+ "(100,'A209','2008-02-16'),"  //
					+ "(101,'A246','2010-09-27'),"  //
					+ "(16,'A026','2007-10-22'),"  //
					+ "(448,'A722','2006-11-01'),"  //
					+ "(395,'A539','2007-11-10'),"  //
					+ "(396,'A526','2007-06-25'),"  //
					+ "(102,'A239','2012-07-05'),"  //
					+ "(17,'A032','2007-06-05'),"  //
					+ "(398,'A509','2004-12-18'),"  //
					+ "(244,'A334','2007-08-21'),"  //
					+ "(397,'A506','2002-12-15'),"  //
					+ "(170,'A332','2007-08-23'),"  //
					+ "(483,'A873','2008-03-12'),"  //
					+ "(18,'A003','2001-05-29'),"  //
					+ "(574,'A845','2007-06-17'),"  //
					+ "(235,'A318','2007-05-21'),"  //
					+ "(533,'A878','2008-03-11'),"  //
					+ "(399,'A517','2006-08-27'),"  //
					+ "(236,'A304','2003-02-23'),"  //
					+ "(449,'A717','2006-08-02'),"  //
					+ "(499,'A627','2018-07-12'),"  //
					+ "(186,'A453','2017-03-23'),"  //
					+ "(450,'S712','2018-02-21'),"  //
					+ "(484,'A842','2007-02-07'),"  //
					+ "(500,'A624','2010-09-17'),"  //
					+ "(44,'A928','2008-04-16'),"  //
					+ "(485,'A822','2006-12-19'),"  //
					+ "(45,'A929','2008-04-12'),"  //
					+ "(351,'A623','2008-06-22'),"  //
					+ "(288,'A144','2019-07-17'),"  //
					+ "(575,'A816','2008-09-19'),"  //
					+ "(46,'A927','2008-02-28'),"  //
					+ "(103,'A235','2008-07-22'),"  //
					+ "(104,'A232','2012-07-04'),"  //
					+ "(149,'A354','2008-07-21'),"  //
					+ "(296,'A242','2008-09-10'),"  //
					+ "(400,'A570','2017-10-19'),"  //
					+ "(253,'A366','2010-08-10'),"  //
					+ "(212,'A716','2006-08-29'),"  //
					+ "(401,'A513','2006-07-27'),"  //
					+ "(105,'A210','2008-02-26'),"  //
					+ "(254,'A357','2008-09-03'),"  //
					+ "(81,'A009','2004-12-16'),"  //
					+ "(534,'A856','2008-02-26'),"  //
					+ "(271,'A327','2007-07-10'),"  //
					+ "(272,'A323','2007-07-13'),"  //
					+ "(402,'A571','2008-03-14'),"  //
					+ "(106,'A212','2007-10-17'),"  //
					+ "(82,'A010','2005-03-03'),"  //
					+ "(451,'A710','2007-11-12'),"  //
					+ "(486,'A850','2008-03-04'),"  //
					+ "(47,'A915','2006-12-18'),"  //
					+ "(180,'A047','2017-02-06'),"  //
					+ "(501,'A619','2006-12-05'),"  //
					+ "(19,'A027','2008-03-29'),"  //
					+ "(289,'A123','2008-04-09'),"  //
					+ "(150,'A308','2003-02-05'),"  //
					+ "(265,'A102','2008-08-04'),"  //
					+ "(403,'A529','2007-05-29'),"  //
					+ "(535,'A839','2006-11-26'),"  //
					+ "(404,'A516','2006-07-15'),"  //
					+ "(245,'A321','2007-07-20'),"  //
					+ "(405,'A562','2017-05-11'),"  //
					+ "(406,'A523','2006-08-21'),"  //
					+ "(151,'A330','2007-08-30'),"  //
					+ "(83,'A020','2007-01-22'),"  //
					+ "(452,'S714','2018-02-01'),"  //
					+ "(84,'A018','2006-11-30'),"  //
					+ "(255,'A307','2003-02-20'),"  //
					+ "(502,'A610','2006-10-21'),"  //
					+ "(330,'A430','2008-05-02'),"  //
					+ "(152,'A343','2008-12-11'),"  //
					+ "(331,'A449','2016-03-31'),"  //
					+ "(213,'A726','2006-09-25'),"  //
					+ "(273,'A371','2017-09-19'),"  //
					+ "(407,'A545','2007-07-03'),"  //
					+ "(20,'A033','2007-10-11'),"  //
					+ "(153,'A335','2007-08-30'),"  //
					+ "(85,'A051','2016-04-05'),"  //
					+ "(107,'A211','2008-02-13'),"  //
					+ "(487,'A855','2007-11-06'),"  //
					+ "(187,'A419','2008-04-02'),"  //
					+ "(453,'A703','2001-09-05'),"  //
					+ "(48,'A937','2008-01-30'),"  //
					+ "(21,'A005','2001-06-03'),"  //
					+ "(536,'A801','2000-09-17'),"  //
					+ "(108,'A245','2019-06-18'),"  //
					+ "(49,'A935','2008-02-26'),"  //
					+ "(297,'A244','2008-09-16'),"  //
					+ "(454,'A723','2006-10-23'),"  //
					+ "(332,'A427','2008-07-09'),"  //
					+ "(67,'A925','2007-07-10'),"  //
					+ "(266,'A136','2009-10-28'),"  //
					+ "(22,'A017','2007-04-17'),"  //
					+ "(214,'A747','2008-04-19'),"  //
					+ "(290,'A126','2008-04-18'),"  //
					+ "(352,'A622','2008-06-17'),"  //
					+ "(215,'A707','2003-02-02'),"  //
					+ "(50,'A923','2007-10-25'),"  //
					+ "(537,'A831','2007-10-16'),"  //
					+ "(333,'A436','2008-05-22'),"  //
					+ "(51,'A916','2007-07-19'),"  //
					+ "(171,'A369','2018-03-19'),"  //
					+ "(172,'A325','2007-07-08'),"  //
					+ "(216,'A718','2006-09-01'),"  //
					+ "(576,'A870','2016-05-31'),"  //
					+ "(256,'A301','2004-12-21'),"  //
					+ "(109,'A254','2019-07-10'),"  //
					+ "(217,'A766','2017-02-08'),"  //
					+ "(334,'A423','2008-03-25'),"  //
					+ "(503,'A609','2006-09-28'),"  //
					+ "(455,'S715','2018-01-17'),"  //
					+ "(335,'A458','2018-09-19'),"  //
					+ "(267,'A104','2008-07-10'),"  //
					+ "(456,'A743','2008-06-25'),"  //
					+ "(504,'A626','2016-06-02'),"  //
					+ "(505,'A652','2007-05-17'),"  //
					+ "(506,'A636','2017-08-09'),"  //
					+ "(507,'A602','2002-10-06'),"  //
					+ "(508,'A621','2007-04-12'),"  //
					+ "(577,'A861','2008-03-05'),"  //
					+ "(538,'A802','2001-09-05'),"  //
					+ "(578,'A862','2008-03-21'),"  //
					+ "(539,'A813','2006-09-30'),"  //
					+ "(408,'A551','2007-11-16'),"  //
					+ "(86,'A050','2016-08-02'),"  //
					+ "(291,'A125','2008-06-25'),"  //
					+ "(23,'A025','2007-05-16'),"  //
					+ "(110,'A214','2008-04-16'),"  //
					+ "(52,'A907','2003-11-16'),"  //
					+ "(53,'A944','2019-05-29'),"  //
					+ "(409,'A525','2006-08-17'),"  //
					+ "(257,'A370','2017-09-14'),"  //
					+ "(410,'A552','2007-07-13'),"  //
					+ "(111,'A215','2008-06-13'),"  //
					+ "(54,'A936','2008-01-27'),"  //
					+ "(336,'A401','2000-05-12'),"  //
					+ "(337,'A456','2018-03-08'),"  //
					+ "(237,'A367','2010-08-16'),"  //
					+ "(87,'A048','2016-07-29'),"  //
					+ "(540,'A803','2001-09-02'),"  //
					+ "(509,'A630','2012-11-15'),"  //
					+ "(338,'A415','2007-05-27'),"  //
					+ "(457,'S716','2018-04-18'),"  //
					+ "(542,'A810','2006-11-14'),"  //
					+ "(88,'A052','2015-08-13'),"  //
					+ "(353,'A613','2007-08-08'),"  //
					+ "(543,'A899','2008-03-14'),"  //
					+ "(112,'A233','2008-05-29'),"  //
					+ "(541,'A804','2001-11-20'),"  //
					+ "(113,'A250','2015-09-01'),"  //
					+ "(544,'A833','2009-02-04'),"  //
					+ "(55,'A931','2008-05-30'),"  //
					+ "(545,'A805','2001-12-04'),"  //
					+ "(546,'A830','2007-07-20'),"  //
					+ "(218,'A711','2006-09-04'),"  //
					+ "(56,'A921','2007-10-17'),"  //
					+ "(114,'A216','2016-11-15'),"  //
					+ "(547,'A832','2007-10-11'),"  //
					+ "(292,'A134','2011-08-30'),"  //
					+ "(458,'A732','2006-12-15'),"  //
					+ "(246,'A333','2007-11-05'),"  //
					+ "(411,'A514','2006-06-08'),"  //
					+ "(154,'A331','2007-08-25'),"  //
					+ "(579,'A815','2008-04-11'),"  //
					+ "(57,'A903','2003-02-24'),"  //
					+ "(58,'A942','2019-02-18'),"  //
					+ "(548,'A829','2006-10-25'),"  //
					+ "(135,'A203','2003-01-28'),"  //
					+ "(219,'A740','2007-10-31'),"  //
					+ "(274,'A356','2008-09-07'),"  //
					+ "(549,'A852','2007-07-24'),"  //
					+ "(354,'A616','2006-10-25'),"  //
					+ "(488,'A874','2011-04-17'),"  //
					+ "(220,'A715','2006-08-15'),"  //
					+ "(24,'A031','2017-06-29'),"  //
					+ "(580,'A857','2008-03-11'),"  //
					+ "(222,'A771','2018-03-13'),"  //
					+ "(221,'A701','2006-07-24'),"  //
					+ "(155,'A362','2009-05-19'),"  //
					+ "(258,'A302','2003-11-01'),"  //
					+ "(156,'A345','2008-02-26'),"  //
					+ "(412,'A547','2007-06-29'),"  //
					+ "(223,'A767','2017-10-25'),"  //
					+ "(413,'A561','2015-08-16'),"  //
					+ "(25,'A011','2006-07-13'),"  //
					+ "(25,'A770','2019-07-02'),"  //
					+ "(550,'A889','2016-04-05'),"  //
					+ "(59,'A911','2017-10-19'),"  //
					+ "(510,'A667','2015-08-01'),"  //
					+ "(459,'S717','2018-04-05'),"  //
					+ "(339,'A428','2008-03-26'),"  //
					+ "(551,'A894','2016-04-05'),"  //
					+ "(511,'A601','2000-05-06'),"  //
					+ "(414,'A522','2006-08-20'),"  //
					+ "(115,'A230','2008-09-02'),"  //
					+ "(60,'A943','2019-03-28'),"  //
					+ "(259,'A350','2008-07-08'),"  //
					+ "(340,'A441','2008-05-08'),"  //
					+ "(415,'A569','2016-04-09'),"  //
					+ "(460,'A751','2008-06-05'),"  //
					+ "(461,'A754','2008-09-29'),"  //
					+ "(512,'A659','2015-08-27'),"  //
					+ "(61,'A917','2006-10-28'),"  //
					+ "(173,'A306','2003-02-11'),"  //
					+ "(552,'A837','2008-02-26'),"  //
					+ "(462,'A761','2012-11-29'),"  //
					+ "(225,'A713','2006-08-20'),"  //
					+ "(62,'A904','2002-12-15'),"  //
					+ "(116,'A227','2008-04-26'),"  //
					+ "(260,'A328','2008-03-08'),"  //
					+ "(63,'A902','2002-10-11'),"  //
					+ "(174,'A324','2007-07-11'),"  //
					+ "(226,'A728','2006-12-19'),"  //
					+ "(416,'A527','2006-08-24'),"  //
					+ "(157,'A312','2004-11-23'),"  //
					+ "(513,'A618','2006-10-31'),"  //
					+ "(553,'A882','2012-10-03'),"  //
					+ "(175,'A368','2018-03-13'),"  //
					+ "(417,'A511','2006-02-21'),"  //
					+ "(117,'A213','2007-10-13'),"  //
					+ "(554,'A808','2006-05-31'),"  //
					+ "(555,'A834','2008-03-08'),"  //
					+ "(463,'A704','2001-09-02'),"  //
					+ "(418,'A528','2006-08-27'),"  //
					+ "(514,'A625','2016-06-06'),"  //
					+ "(118,'A234','2008-07-17'),"  //
					+ "(119,'A229','2008-02-29'),"  //
					+ "(227,'A768','2017-05-11'),"  //
					+ "(556,'A886','2016-08-10'),"  //
					+ "(136,'A219','2008-06-09'),"  //
					+ "(341,'A435','2008-05-19'),"  //
					+ "(419,'A568','2017-05-17'),"  //
					+ "(420,'A507','2002-12-13'),"  //
					+ "(342,'A437','2008-06-24'),"  //
					+ "(421,'A542','2007-06-14'),"  //
					+ "(293,'A124','2008-04-16'),"  //
					+ "(158,'A346','2008-03-05'),"  //
					+ "(557,'A809','2006-09-24'),"  //
					+ "(581,'A814','2008-05-27'),"  //
					+ "(558,'A880','2008-04-25'),"  //
					+ "(343,'A444','2009-06-28'),"  //
					+ "(159,'A363','2009-04-17'),"  //
					+ "(228,'A734','2007-08-27'),"  //
					+ "(422,'A515','2006-07-12'),"  //
					+ "(355,'A633','2017-02-10'),"  //
					+ "(489,'A872','2011-04-14'),"  //
					+ "(423,'A510','2005-09-14'),"  //
					+ "(64,'A922','2006-11-23'),"  //
					+ "(356,'A634','2017-02-14'),"  //
					+ "(68,'A938','2008-08-25'),"  //
					+ "(357,'A612','2006-10-30'),"  //
					+ "(344,'A414','2007-05-31'),"  //
					+ "(229,'A729','2006-12-06'),"  //
					+ "(582,'A858','2008-03-14'),"  //
					+ "(120,'A247','2016-09-10'),"  //
					+ "(137,'A255','2019-09-17')" //
					
			);

			this.init(queries);
		}
	}

}
