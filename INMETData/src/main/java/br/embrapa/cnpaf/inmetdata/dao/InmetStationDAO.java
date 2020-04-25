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
						" PRIMARY KEY (id)" + //
						")"// n
		);
		
		// populando tabela
		
		
		queries.add(//
				"INSERT INTO station(code,city,state,latitude,longitude,start_date) VALUES "
				+ "('A024','Alto Paraiso','GO',-14.133,-47.5232,'02/05/2007'),\r\n" + 
				" ('A013','Aragarcas','GO',-15.9026,-52.2451,'13/07/2007'),\r\n" + 
				" ('A023','Caiaponia','GO',-16.9668,-51.8175,'21/05/2007'),\r\n" + 
				" ('A034','Catalao','GO',-18.1547,-47.9276,'31/01/2008'),\r\n" + 
				" ('A036','Cristalina','GO',-16.7848,-47.6129,'13/12/2007'),\r\n" + 
				" ('A056','Cristalina','GO',-16.3993,-47.6258,'25/01/2018'),\r\n" + 
				" ('A029','Edeia','GO',-17.337,-49.9147,'26/08/2017'),\r\n" + 
				" ('A022','Goianesia','GO',-15.2201,-48.9901,'02/06/2007'),\r\n" + 
				" ('A002','Goiania','GO',-16.6428,-49.2202,'30/05/2001'),\r\n" + 
				" ('A014','Goias','GO',-15.9397,-50.1414,'16/07/2007'),\r\n" + 
				" ('A028','Ipora','GO',-16.423,-51.1488,'24/06/2013'),\r\n" + 
				" ('A015','Itapaci','GO',-14.9797,-49.5399,'03/02/2007'),\r\n" + 
				" ('A035','Itumbiara','GO',-18.4097,-49.192,'02/11/2007'),\r\n" + 
				" ('A016','Jatai','GO',-17.9236,-51.7174,'24/05/2007'),\r\n" + 
				" ('A012','Luziania','GO',-16.2605,-47.9669,'21/10/2006'),\r\n" + 
				" ('A026','Mineiros','GO',-47.4548,-52.6012,'23/10/2007'),\r\n" + 
				" ('A032','Monte Alegre de Goias','GO',-13.2535,-46.8903,'06/06/2007'),\r\n" + 
				" ('A003','Morrinhos','GO',-17.745,-49.1016,'30/05/2001'),\r\n" + 
				" ('A027','Parauna','GO',-16.9625,-50.4254,'30/03/2008'),\r\n" + 
				" ('A033','Pires do Rio','GO',-17.3041,-48.2841,'12/10/2007'),\r\n" + 
				" ('A005','Porangatu','GO',-13.3095,-49.1174,'04/06/2001'),\r\n" + 
				" ('A017','Posse','GO',-14.0892,-46.3664,'18/04/2007'),\r\n" + 
				" ('A025','Rio Verde','GO',-17.7853,-50.9648,'17/05/2007'),\r\n" + 
				" ('A031','Sao Miguel do Araguaia','GO',-12.8204,-50.3359,'30/06/2017'),\r\n" + 
				" ('A011','Sao Simao','GO',-18.9691,-50.6334,'14/07/2006'),\r\n" + 
				" ('A908','Agua Boa','MT',-14.0164,-52.2116,'16/12/2006'),\r\n" + 
				" ('A924','Alta Floresta','MT',-10.0772,-56.1792,'23/05/2007'),\r\n" + 
				" ('A909','Alto Araguaia','MT',-17.3393,-53.2244,'22/09/2011'),\r\n" + 
				" ('A934','Alto Taquari','MT',-17.841,-53.2894,'29/01/2008'),\r\n" + 
				" ('A910','Apiacas','MT',-9.5633,-57.3935,'24/10/2006'),\r\n" + 
				" ('A941','Caceres','MT',-16.0747,-57.693,'30/03/2012'),\r\n" + 
				" ('A905','Campo Novo dos Parecis','MT',-13.7859,-57.8384,'12/12/2012'),\r\n" + 
				" ('A912','Campo Verde','MT',-15.5314,-55.1356,'21/11/2006'),\r\n" + 
				" ('A926','Carlinda','MT',-9.9705,-55.8275,'10/04/2008'),\r\n" + 
				" ('A913','Comodoro','MT',-13.708,-59.7623,'28/11/2006'),\r\n" + 
				" ('A919','Cotriguacu','MT',-9.9062,-58.5722,'27/05/2007'),\r\n" + 
				" ('A901','Cuiaba','MT',-15.6068,-56.0609,'07/12/2002'),\r\n" + 
				" ('A930','Gaucha do Norte','MT',-13.1848,-53.2573,'06/06/2008'),\r\n" + 
				" ('A906','Guarata do Norte','MT',-9.9525,-54.8978,'20/12/2002'),\r\n" + 
				" ('A932','Guiratinga','MT',-16.3415,-53.766,'25/01/2008'),\r\n" + 
				" ('A933','Itiquira','MT',-17.175,-54.5016,'14/08/2008'),\r\n" + 
				" ('A914','Juara','MT',-11.2801,-57.5266,'02/11/2006'),\r\n" + 
				" ('A920','Juina','MT',-11.3751,-58.7747,'01/12/2006'),\r\n" + 
				" ('A928','Nova Maringa','MT',-13.0386,-57.0922,'17/04/2008'),\r\n" + 
				" ('A929','Nova Ubirata','MT',-13.4111,-54.7521,'13/04/2008'),\r\n" + 
				" ('A927','Novo Mundo','MT',-12.5219,-58.2314,'29/02/2008'),\r\n" + 
				" ('A915','Paranatinga','MT',-14.4214,-54.036,'19/12/2006'),\r\n" + 
				" ('A937','Pontes e Lacerda','MT',-15.3255,-59.2338,'31/01/2008'),\r\n" + 
				" ('A935','Porto Estrela','MT',-15.3247,-57.2258,'27/02/2008'),\r\n" + 
				" ('A923','Primavera do Leste','MT',-15.5799,54.381,'26/10/2007'),\r\n" + 
				" ('A916','Querencia','MT',-12.6273,-52.2208,'20/07/2007'),\r\n" + 
				" ('A907','Rondonopolis','MT',-16.4624,-54.5801,'17/11/2003'),\r\n" + 
				" ('A944','Rosario Oeste','MT',-14.8289,-56.4419,'30/05/2019'),\r\n" + 
				" ('A936','Salto do Ceu','MT',-15.1248,-58.127,'28/01/2008'),\r\n" + 
				" ('A931','Santo Antonio do Leste','MT',-14.9278,-53.8836,'31/05/2008'),\r\n" + 
				" ('A921','Sao Felix do Araguaia','MT',-11.619,-50.7278,'18/10/2007'),\r\n" + 
				" ('A903','Sao Jose do Rio Claro','MT',-13.4539,-56.6773,'25/02/2003'),\r\n" + 
				" ('A942','Sao Jose do Xingu','MT',-10.4841,-52.3725,'19/02/2019'),\r\n" + 
				" ('A911','Sapezal','MT',-13.304,-58.7635,'20/10/2017'),\r\n" + 
				" ('A943','Serra Nova Dourada','MT',-11.9879,-51.426,'29/03/2019'),\r\n" + 
				" ('A917','Sinop','MT',-11.9822,-55.5659,'29/10/2006'),\r\n" + 
				" ('A904','Sorriso','MT',-12.5551,-55.7228,'16/12/2002'),\r\n" + 
				" ('A902','Tangara da Serra','MT',-14.6501,-57.4315,'12/10/2002'),\r\n" + 
				" ('A922','Vila Bela da Santissima Trindade','MT',-15.0626,-59.8729,'24/11/2006'),\r\n" + 
				" ('A940','Ariquemes','RO',-9.949,-62.9618,'18/07/2008'),\r\n" + 
				" ('A939','Cacoal','RO',-11.4458,-61.4341,'20/07/2008'),\r\n" + 
				" ('A925','Porto Velho','RO',-8.7936,-63.8459,'11/07/2007'),\r\n" + 
				" ('A938','Vilhena','RO',-12.7348,-60.1577,'26/08/2008'),\r\n" + 
				" ('A053','Almas','TO',-11.284,-47.2121,'01/04/2016'),\r\n" + 
				" ('A054','Araguacu','TO',-12.5922,-49.5287,'19/08/2015'),\r\n" + 
				" ('A021','Araguaina','TO',-7.1039,-48.2012,'26/01/2007'),\r\n" + 
				" ('A044','Araguatins','TO',-5.6437,-48.1118,'12/03/2009'),\r\n" + 
				" ('A043','Campos Lindos','TO',-8.1546,-46.6393,'29/10/2012'),\r\n" + 
				" ('A049','Colinas do Tocantins','TO',-8.0927,-48.4786,'12/11/2016'),\r\n" + 
				" ('A038','Dianopolis','TO',-11.5944,-46.8472,'30/08/2008'),\r\n" + 
				" ('A039','Formoso do Araguaia','TO',-11.8873,-49.6082,'27/04/2008'),\r\n" + 
				" ('A019','Gurupi','TO',-11.7457,-49.0497,'20/12/2006'),\r\n" + 
				" ('A055','Lagoa da Confusao','TO',-10.8282,-49.8478,'15/11/2016'),\r\n" + 
				" ('A041','Marianopolis do Tocantis','TO',-9.5763,-49.7233,'24/11/2012'),\r\n" + 
				" ('A040','Mateiros','TO',-10.4344,-45.9219,'25/10/2012'),\r\n" + 
				" ('A009','Palmas','TO',-10.1907,-48.3018,'17/12/2004'),\r\n" + 
				" ('A010','Parana','TO',-12.6148,-47.8719,'04/03/2005'),\r\n" + 
				" ('A020','Pedro Afonso','TO',-8.9686,-48.1772,'23/01/2007'),\r\n" + 
				" ('A018','Peixe','TO',-12.0153,-48.5445,'01/12/2006'),\r\n" + 
				" ('A051','Pium','TO',-10.4769,-49.6294,'06/04/2016'),\r\n" + 
				" ('A050','Rio Sono','TO',-9.7933,-47.1327,'03/08/2016'),\r\n" + 
				" ('A048','Santa Fe do Araguaia','TO',-7.1241,-48.7812,'30/07/2016'),\r\n" + 
				" ('A052','Santa Rosa do Tocantins','TO',-11.429,-48.1848,'14/08/2015'),\r\n" + 
				" ('A253','Altamira','PA',-3.2702,-52.3948,'25/10/2018'),\r\n" + 
				" ('A201','Belem','PA',-1.4112,-48.4395,'20/01/2003'),\r\n" + 
				" ('A226','Braganca','PA',-1.0472,-46.7857,'11/03/2008'),\r\n" + 
				" ('A228','Breves','PA',-1.6802,-50.4781,'21/10/2017'),\r\n" + 
				" ('A236','Cameta','PA',-2.2396,-49.4998,'20/06/2008'),\r\n" + 
				" ('A248','Capitao Poco','PA',-1.7347,-47.0575,'18/04/2011'),\r\n" + 
				" ('A202','Castanhal','PA',-1.3008,-47.9479,'24/01/2003'),\r\n" + 
				" ('A241','Conceicao do Araguaia','PA',-8.3036,-49.2827,'04/09/2008'),\r\n" + 
				" ('A252','Dom Eliseu','PA',-4.2805,-47.5639,'08/03/2018'),\r\n" + 
				" ('A231','Itaituba','PA',-4.2769,-55.993,'17/02/2008'),\r\n" + 
				" ('A240','Maraba','PA',-5.3664,-49.0511,'25/06/2009'),\r\n" + 
				" ('A209','Medicilandia','PA',-3.5109,-52.9634,'17/02/2008'),\r\n" + 
				" ('A246','Mina Palito','PA',-6.32,-55.7877,'28/09/2010'),\r\n" + 
				" ('A239','Monte Alegre','PA',-2,-54.0764,'06/07/2012'),\r\n" + 
				" ('A235','Novo Repartimento','PA',-4.2439,-49.9393,'23/07/2008'),\r\n" + 
				" ('A232','Obidos','PA',-1.8807,-55.5198,'05/07/2012'),\r\n" + 
				" ('A210','Pacaja','PA',-3.8437,-50.638,'27/02/2008'),\r\n" + 
				" ('A212','Paragominas','PA',-2.9903,-47.4078,'18/10/2007'),\r\n" + 
				" ('A211','Placas','PA',-3.864,-54.2154,'14/02/2008'),\r\n" + 
				" ('A245','Porto de Moz','PA',-1.8221,-52.1117,'19/06/2019'),\r\n" + 
				" ('A254','Redencao','PA',-8.3432,-50.0069,'11/07/2019'),\r\n" + 
				" ('A214','Rondon do Para','PA',-4.8275,48.1735,'17/04/2008'),\r\n" + 
				" ('A215','Salinopolis','PA',-0.6188,-47.3565,'14/06/2008'),\r\n" + 
				" ('A233','Santana do Araguaia','PA',-9.3386,-50.3502,'30/05/2008'),\r\n" + 
				" ('A250','Santarem','PA',-2.5025,-54.7202,'02/09/2015'),\r\n" + 
				" ('A216','Sao Felix do Xingu','PA',-6.6396,-51.9569,'16/11/2016'),\r\n" + 
				" ('A230','Serra dos Carajas','PA',-6.0774,-50.1422,'03/09/2008'),\r\n" + 
				" ('A227','Soure','PA',-0.7278,-48.5157,'27/04/2008'),\r\n" + 
				" ('A213','Tome Acu','PA',-2.5926,-48.3605,'14/10/2007'),\r\n" + 
				" ('A234','Tucuma','PA',-6.7432,-51.1418,'18/07/2008'),\r\n" + 
				" ('A229','Tucurui','PA',-3.8227,-49.6749,'01/03/2008'),\r\n" + 
				" ('A247','Xinguara','PA',-7.1105,-49.926,'11/09/2016'),\r\n" + 
				" ('A223','Alto Parnaiba','MA',-9.1081,-45.932,'03/06/2008'),\r\n" + 
				" ('A220','Bacabal','MA',-4.243,-44.7949,'09/07/2008'),\r\n" + 
				" ('A204','Balsas','MA',-7.4556,-46.0275,'26/10/2007'),\r\n" + 
				" ('A221','Barra do Corda','MA',-5.5067,-45.237,'06/03/2008'),\r\n" + 
				" ('A238','Buriticupu','MA',-4.3205,-46.4495,'11/09/2008'),\r\n" + 
				" ('A205','Carolina','MA',-7.3372,-47.4598,'04/12/2007'),\r\n" + 
				" ('A237','Caxias','MA',-4.8213,-43.3436,'13/07/2008'),\r\n" + 
				" ('A206','Chapadinha','MA',-3.7426,-43.352,'17/09/2008'),\r\n" + 
				" ('A222','Colinas','MA',-6.0331,-44.2334,'03/06/2008'),\r\n" + 
				" ('A224','Estreito','MA',-6.6532,-47.4182,'27/02/2008'),\r\n" + 
				" ('A218','Farol Preguicas','MA',-2.5923,-42.7075,'22/11/2008'),\r\n" + 
				" ('A217','Farol Santana','MA',-2.2708,-43.624,'28/11/2008'),\r\n" + 
				" ('A207','Grajau','MA',-5.816,-46.1622,'10/09/2008'),\r\n" + 
				" ('A225','Imperatriz','MA',-5.5557,-47.4597,'01/03/2008'),\r\n" + 
				" ('A203','Sao Luis','MA',-2.5267,-44.2165,'29/01/2003'),\r\n" + 
				" ('A219','Turiacu','MA',-1.6612,-45.3725,'10/06/2008'),\r\n" + 
				" ('A255','Ze Doca','MA',-3.2691,-45.651,'18/09/2019'),\r\n" + 
				" ('A336','Alvorada do Gurgueia','PI',-8.4415,-43.8654,'17/11/2007'),\r\n" + 
				" ('A377','Angical do Piaui','PI',-6.0894,-42.7273,'14/11/2019'),\r\n" + 
				" ('A375','Baixa Grande do Ribeira','PI',-8.3335,-45.0946,'23/03/2018'),\r\n" + 
				" ('A326','Bom Jesus do Piaui','PI',-9.0832,-44.3264,'17/07/2007'),\r\n" + 
				" ('A376','Campo Maior','PI',-4.8641,-42.1455,'23/11/2018'),\r\n" + 
				" ('A365','Canto do Buriti','PI',-8.1179,-42.9757,'12/06/2010'),\r\n" + 
				" ('A337','Caracol','PI',-9.2858,-43.3244,'10/11/2007'),\r\n" + 
				" ('A361','Castelo do Piaui','PI',-5.3492,-41.5122,'20/04/2009'),\r\n" + 
				" ('A374','Corrente','PI',-10.4291,-45.173,'28/03/2018'),\r\n" + 
				" ('A311','Floriano','PI',-6.7614,-43.0033,'18/11/2004'),\r\n" + 
				" ('A364','Gilbues','PI',-9.8751,-45.3458,'16/05/2009'),\r\n" + 
				" ('A354','Oeiras','PI',-6.9741,-42.1468,'22/07/2008'),\r\n" + 
				" ('A308','Parnaiba','PI',-3.0866,-41.7831,'06/02/2003'),\r\n" + 
				" ('A330','Paulistana','PI',-8.1322,-41.1429,'31/08/2007'),\r\n" + 
				" ('A343','Picos','PI',-7.071,-41.404,'12/12/2008'),\r\n" + 
				" ('A335','Piripiri','PI',-4.276,-41.7945,'31/08/2007'),\r\n" + 
				" ('A331','Sao Joao do Piaui','PI',-8.3649,-42.2503,'26/08/2007'),\r\n" + 
				" ('A362','Sao Pedro do Piaui','PI',-5.911,-42.7187,'20/05/2009'),\r\n" + 
				" ('A345','Sao Raimundo Nonato','PI',-9.0332,-42.7011,'27/02/2008'),\r\n" + 
				" ('A312','Teresina','PI',-5.0347,-42.8013,'24/11/2004'),\r\n" + 
				" ('A346','Urucui','PI',-7.4414,-44.345,'06/03/2008'),\r\n" + 
				" ('A363','Valenca do Piaui','PI',-6.3992,-41.7399,'18/04/2009'),\r\n" + 
				" ('A360','Acarau','CE',-3.121,-40.0872,'22/04/2009'),\r\n" + 
				" ('A315','Barbalha','CE',-7.3009,-39.2711,'31/05/2007'),\r\n" + 
				" ('A347','Campos Sales','CE',-7.0777,-40.3626,'07/03/2008'),\r\n" + 
				" ('A342','Crateus','CE',-5.1866,-40.6721,'11/12/2008'),\r\n" + 
				" ('A305','Fortaleza','CE',-3.8157,-38.5377,'18/02/2003'),\r\n" + 
				" ('A314','Guaramiranga','CE',-4.2613,-38.931,'26/05/2007'),\r\n" + 
				" ('A319','Iguatu','CE',-6.3963,-39.2689,'30/05/2007'),\r\n" + 
				" ('A359','Itapipoca','CE',-3.4842,-39.5887,'05/09/2008'),\r\n" + 
				" ('A358','Jaguaribe','CE',-5.9055,-38.6277,'10/09/2008'),\r\n" + 
				" ('A339','Jaguaruana','CE',-4.8377,-37.6997,'10/11/2007'),\r\n" + 
				" ('A332','Morada Nova','CE',-5.1366,-38.3565,'24/08/2007'),\r\n" + 
				" ('A369','Quixada','CE',-4.9787,-39.0571,'20/03/2018'),\r\n" + 
				" ('A325','Quixeramobim','CE',-5.1745,-39.2893,'09/07/2007'),\r\n" + 
				" ('A306','Sobral','CE',-3.7481,-40.3457,'12/02/2003'),\r\n" + 
				" ('A324','Taua','CE',-6.0174,-40.2813,'12/07/2007'),\r\n" + 
				" ('A368','Tiangua','CE',-3.7321,-41.0118,'14/03/2018'),\r\n" + 
				" ('A045','Aguas Emendadas','DF',-15.5964,-47.6258,'02/10/2008'),\r\n" + 
				" ('A001','Brasilia','DF',-15.7893,-47.9257,'07/05/2000'),\r\n" + 
				" ('A042','Braszlandia','DF',-15.6528,-48.2013,'19/07/2017'),\r\n" + 
				" ('A046','Gama','DF',-15.9351,-48.1373,'01/10/2014'),\r\n" + 
				" ('A047','Paranoa','DF',-16.0122,-47.5574,'07/02/2017'),\r\n" + 
				" ('A409','Aracaju','SE',-10.9524,-37.0543,'12/02/2003'),\r\n" + 
				" ('A421','Brejo Grande','SE',-10.4739,-36.482,'17/07/2008'),\r\n" + 
				" ('A420','Carira','SE',-10.3997,-37.7475,'28/03/2008'),\r\n" + 
				" ('A451','Itabaiana','SE',-10.6717,-37.4715,'24/04/2017'),\r\n" + 
				" ('A417','Itabaianinha','SE',-11.2725,-37.795,'23/05/2007'),\r\n" + 
				" ('A453','Nossa Senhora da Gloria','SE',-10.2077,-37.4318,'24/03/2017'),\r\n" + 
				" ('A419','Poco Verde','SE',-10.738,-38.1083,'03/04/2008'),\r\n" + 
				" ('A736','Ariranha','SP',-21.1329,-48.8404,'15/11/2007'),\r\n" + 
				" ('A725','Avare','SP',-23.1017,-48.9409,'22/09/2006'),\r\n" + 
				" ('A741','Barra Bonita','SP',-22.4712,-48.5575,'28/04/2008'),\r\n" + 
				" ('A746','Barra do Turvo','SP',-24.9628,-48.4163,'04/07/2008'),\r\n" + 
				" ('A748','Barretos','SP',-20.5591,-48.5449,'19/06/2010'),\r\n" + 
				" ('A755','Barueri','SP',-23.5238,-46.8994,'23/03/2011'),\r\n" + 
				" ('A705','Bauru','SP',-22.358,-49.0288,'30/08/2001'),\r\n" + 
				" ('A764','Bebedouro','SP',-20.9491,-48.4899,'27/11/2016'),\r\n" + 
				" ('A765','Bertioga','SP',-23.8446,-46.1433,'01/02/2017'),\r\n" + 
				" ('A744','Braganca Paulista','SP',-22.9519,-46.5305,'20/12/2017'),\r\n" + 
				" ('A769','Cachoeira Paulista','SP',-22.689,-45.0054,'20/10/2017'),\r\n" + 
				" ('A706','Campos do jordao','SP',-22.7502,-45.6038,'14/03/2002'),\r\n" + 
				" ('A738','Casa Branca','SP',-21.7805,-47.0752,'28/06/2007'),\r\n" + 
				" ('A762','Dracena','SP',-21.4576,-51.5522,'03/11/2016'),\r\n" + 
				" ('A708','Franca','SP',-20.5844,-47.3825,'10/12/2002'),\r\n" + 
				" ('A737','Ibitinga','SP',-21.8555,-48.7997,'09/11/2007'),\r\n" + 
				" ('A712','Iguape','SP',-24.6716,-47.5458,'19/07/2006'),\r\n" + 
				" ('A714','Itapeva','SP',-23.9819,-48.8858,'25/07/2006'),\r\n" + 
				" ('A739','Itapira','SP',-22.415,-46.8052,'13/11/2007'),\r\n" + 
				" ('A753','Ituverava','SP',-20.3597,-47.7752,'17/08/2008'),\r\n" + 
				" ('A733','Jales','SP',-20.165,-50.5951,'23/08/2007'),\r\n" + 
				" ('A735','Jose Bonifacio','SP',-21.0856,-49.9203,'04/09/2007'),\r\n" + 
				" ('A727','Lins','SP',-21.666,-49.7348,'20/09/2006'),\r\n" + 
				" ('A763','Marilia','SP',-22.2352,-49.9651,'13/05/2017'),\r\n" + 
				" ('A716','Ourinhos','SP',-22.949,-49.8945,'30/08/2006'),\r\n" + 
				" ('A726','Piracicaba','SP',-22.7031,-47.6233,'26/09/2006'),\r\n" + 
				" ('A747','Pradopolis','SP',-21.3384,-48.114,'20/04/2008'),\r\n" + 
				" ('A707','Presidente Prudente','SP',-22.1198,-51.4086,'03/02/2003'),\r\n" + 
				" ('A718','Rancharia','SP',-22.3728,-50.9747,'02/09/2006'),\r\n" + 
				" ('A766','Registro','SP',-24.5331,-47.8641,'09/02/2017'),\r\n" + 
				" ('A711','Sao Carlos','SP',-21.9803,-47.8839,'05/09/2006'),\r\n" + 
				" ('A740','Sao Luis do Paraitinga','SP',-23.2283,-45.417,'01/11/2007'),\r\n" + 
				" ('A715','Sao Miguel Arcanjo','SP',-23.852,-48.1648,'16/08/2006'),\r\n" + 
				" ('A701','Sao Paulo - Mirante do Santana','SP',-23.4962,-46.62,'25/07/2006'),\r\n" + 
				" ('A771','Sao Paulo - Interlagos','SP',-23.7244,-46.6775,'14/03/2018'),\r\n" + 
				" ('A767','Sao Sebastiao','SP',-23.8107,-45.4025,'26/10/2017'),\r\n" + 
				" ('A770','Sao Simao','SP',-21.461,-47.5795,'03/07/2019'),\r\n" + 
				" ('A713','Sorocaba','SP',-23.426,-47.5855,'21/08/2006'),\r\n" + 
				" ('A728','Taubate','SP',-23.0416,-45.5208,'20/12/2006'),\r\n" + 
				" ('A768','Tupa','SP',-21.9272,-50.4902,'12/05/2017'),\r\n" + 
				" ('A734','Valparaiso','SP',-21.3191,-50.9301,'28/08/2007'),\r\n" + 
				" ('A729','Votuporanga','SP',-20.4032,-49.966,'07/12/2006'),\r\n" + 
				" ('A340','Apodi','RN',-5.6265,-37.8149,'14/11/2007'),\r\n" + 
				" ('A316','Caico','RN',-6.4674,-37.0849,'07/01/2007'),\r\n" + 
				" ('A344','Calcanhar','RN',-5.1599,-35.4876,'08/05/2008'),\r\n" + 
				" ('A372','Ipanguacu','RN',-5.5348,-36.8723,'08/11/2017'),\r\n" + 
				" ('A317','Macau','RN',-5.151,-36.5731,'06/01/2007'),\r\n" + 
				" ('A318','Mossoro','RN',-4.904,-37.3669,'22/05/2007'),\r\n" + 
				" ('A304','Natal','RN',-5.8371,-35.2079,'24/02/2003'),\r\n" + 
				" ('A367','Santa Cruz','RN',-6.2279,-36.0265,'17/08/2010'),\r\n" + 
				" ('A310','Areia','PB',-6.9754,-35.7181,'16/11/2004'),\r\n" + 
				" ('A348','Cabaceiras','PB',-7.4832,-36.2864,'28/02/2008'),\r\n" + 
				" ('A352','Camaratuba','PB',-6.5618,-35.1353,'04/05/2008'),\r\n" + 
				" ('A313','Campina Grande','PB',-7.2255,-35.9048,'22/12/2006'),\r\n" + 
				" ('A373','Itaporanga','PB',-7.3184,-38.1408,'03/11/2017'),\r\n" + 
				" ('A320','Joao Pessoa','PB',-7.1654,-34.8156,'21/07/2007'),\r\n" + 
				" ('A334','Monteiro','PB',-7.8944,-37.1247,'22/08/2007'),\r\n" + 
				" ('A321','Patos','PB',-7.0798,-37.2728,'21/07/2007'),\r\n" + 
				" ('A333','Sao Goncalo','PB',-6.8357,-38.3115,'06/11/2007'),\r\n" + 
				" ('A309','Arco Verde','PE',-8.4335,-37.0554,'20/11/2004'),\r\n" + 
				" ('A329','Cabrobo','PE',-8.504,-39.3152,'04/09/2007'),\r\n" + 
				" ('A341','Caruaru','PE',-8.3651,-36.0284,'21/11/2007'),\r\n" + 
				" ('A351','Floresta','PE',-8.5987,-38.584,'15/09/2008'),\r\n" + 
				" ('A322','Garanhuns','PE',-8.9109,-36.4933,'08/07/2007'),\r\n" + 
				" ('A349','Ibimirim','PE',-8.5095,-37.7115,'14/10/2008'),\r\n" + 
				" ('A366','Ouricuri','PE',-7.8858,-40.1026,'11/08/2010'),\r\n" + 
				" ('A357','Palmares','PE',-8.6666,-35.5679,'04/09/2008'),\r\n" + 
				" ('A307','Petrolina','PE',-9.3883,-40.5232,'21/02/2003'),\r\n" + 
				" ('A301','Recife','PE',-8.0592,-34.9592,'22/12/2004'),\r\n" + 
				" ('A370','Salgueiro','PE',-8.058,-39.0961,'15/09/2017'),\r\n" + 
				" ('A302','Sao Pedro e Sao Paulo','PE',-0.9168,-29.3459,'02/11/2003'),\r\n" + 
				" ('A350','Serra Talhada','PE',-7.9542,-38.295,'09/07/2008'),\r\n" + 
				" ('A328','Surubim','PE',-7.8396,-35.801,'09/03/2008'),\r\n" + 
				" ('A108','Cruzeiro do Sul','AC',-7.6107,-72.6812,'14/05/2015'),\r\n" + 
				" ('A140','Epitaciolandia','AC',-11.0237,-68.7351,'25/09/2009'),\r\n" + 
				" ('A138','Feijo','AC',-8.1426,-70.3435,'24/09/2009'),\r\n" + 
				" ('A137','Marechal Thaumaturgo','AC',-8.95,-72.7867,'27/10/2009'),\r\n" + 
				" ('A102','Parque Estadual Chandless','AC',-9.3583,-69.9262,'05/08/2008'),\r\n" + 
				" ('A136','Porto Walter','AC',-8.2671,-72.7478,'29/10/2009'),\r\n" + 
				" ('A104','Rio Branco','AC',-9.9578,-68.1651,'11/07/2008'),\r\n" + 
				" ('A353','Arapiraca','AL',-9.8045,-36.6191,'27/04/2008'),\r\n" + 
				" ('A355','Coruripe','AL',-10.1284,-36.2863,'06/09/2008'),\r\n" + 
				" ('A303','Maceio','AL',-9.5511,-35.7701,'25/02/2003'),\r\n" + 
				" ('A327','Palmeiras dos Indios','AL',-9.4203,-36.6203,'11/07/2007'),\r\n" + 
				" ('A323','Pao de Acucar','AL',-9.7492,-37.4307,'14/07/2007'),\r\n" + 
				" ('A371','Piranhas','AL',-9.6222,-37.7671,'20/09/2017'),\r\n" + 
				" ('A356','Sao Luis do Quitunde','AL',-9.2874,-35.5658,'08/09/2008'),\r\n" + 
				" ('A113','Apui','AM',-7.2054,-59.8885,'19/07/2008'),\r\n" + 
				" ('A120','Autazes','AM',-3.5833,-59.1294,'17/04/2008'),\r\n" + 
				" ('A128','Barcelos','AM',-0.9872,-62.9242,'19/04/2008'),\r\n" + 
				" ('A110','Boca do Acre','AM',-8.7768,-67.3325,'15/07/2008'),\r\n" + 
				" ('A117','Coari','AM',-4.0974,-63.1453,'12/04/2008'),\r\n" + 
				" ('A109','Eirunepe','AM',-6.6503,-69.8686,'31/08/2012'),\r\n" + 
				" ('A112','Humaita','AM',-7.5525,-63.0713,'25/04/2008'),\r\n" + 
				" ('A121','Itacoatiara','AM',-3.1333,-58.4827,'17/04/2008'),\r\n" + 
				" ('A111','Labrea','AM',-7.2606,-64.7885,'27/07/2008'),\r\n" + 
				" ('A119','Manacapuru','AM',-3.2946,-60.6284,'10/04/2004'),\r\n" + 
				" ('A101','Manaus','AM',-3.1036,-60.0154,'09/05/2000'),\r\n" + 
				" ('A133','Manicore','AM',-5.7885,-61.2882,'12/12/2011'),\r\n" + 
				" ('A122','Maues','AM',-3.399,-57.6737,'22/04/2008'),\r\n" + 
				" ('A144','Novo Aripuana','AM',-5.1411,-60.3805,'18/07/2019'),\r\n" + 
				" ('A123','Parintins','AM',-2.6391,-56.7561,'10/04/2008'),\r\n" + 
				" ('A126','Presidente Figueiredo','AM',-2.0565,-60.0257,'19/04/2008'),\r\n" + 
				" ('A125','Rio Urubu','AM',-2.6336,-59.6005,'26/06/2008'),\r\n" + 
				" ('A134','Sao gabriel da Cachoeira','AM',-0.1252,-67.0612,'31/08/2011'),\r\n" + 
				" ('A124','Urucara','AM',-2.5347,-57.758,'17/04/2008'),\r\n" + 
				" ('A251','Itaubal','AP',-0.5678,-50.8235,'24/08/2017'),\r\n" + 
				" ('A249','Macapa','AP',-0.035,-51.0887,'12/12/2013'),\r\n" + 
				" ('A242','Oiapoque','AP',-3.8135,-51.8625,'11/09/2008'),\r\n" + 
				" ('A244','Porto Grande','AP',-0.6943,-51.404,'17/09/2008'),\r\n" + 
				" ('A422','Abrolhos','BA',-17.963,-38.7032,'21/07/2008'),\r\n" + 
				" ('A434','Amargosa','BA',-13.0094,-39.6168,'11/07/2008'),\r\n" + 
				" ('A429','Barra','BA',-11.0848,-43.1389,'12/05/2008'),\r\n" + 
				" ('A402','Barreiras','BA',-12.1247,-45.027,'20/12/2001'),\r\n" + 
				" ('A447','Belmonte','BA',-16.088,-39.2153,'02/07/2009'),\r\n" + 
				" ('A418','Bom Jesus da Lapa','BA',-13.251,-43.4053,'18/05/2007'),\r\n" + 
				" ('A433','Brumado','BA',-14.1818,-41.6722,'28/04/2008'),\r\n" + 
				" ('A432','Buritirama','BA',-10.7229,-43.6512,'15/05/2008'),\r\n" + 
				" ('A405','Caravelas','BA',-17.7394,-39.2586,'20/12/2002'),\r\n" + 
				" ('A431','Conde','BA',-12.0358,-37.6838,'07/04/2008'),\r\n" + 
				" ('A416','Correntina','BA',-13.2333,-44.6173,'09/11/2007'),\r\n" + 
				" ('A406','Cruz das Almas','BA',-12.6754,-39.0895,'19/01/2003'),\r\n" + 
				" ('A448','Curaca','BA',-9.0013,-39.9134,'20/08/2015'),\r\n" + 
				" ('A443','Delfino','BA',-10.4549,-41.2069,'15/07/2008'),\r\n" + 
				" ('A442','Euclides da Cunha','BA',-10.5372,-38.9966,'31/03/2008'),\r\n" + 
				" ('A413','Feira de Santana','BA',-12.1962,-38.9673,'26/05/2007'),\r\n" + 
				" ('A452','Formosa do Rio Preto','BA',-11.0521,-45.2007,'02/06/2016'),\r\n" + 
				" ('A426','Guanambi','BA',-14.2081,-42.7496,'24/04/2008'),\r\n" + 
				" ('A439','Ibotirama','BA',-12.193,-43.2134,'28/04/2008'),\r\n" + 
				" ('A410','Ilheus','BA',-14.6588,-39.1814,'25/01/2003'),\r\n" + 
				" ('A445','Ipiau','BA',-14.1713,-39.6925,'30/06/2009'),\r\n" + 
				" ('A424','Irece','BA',-11.3289,-41.8645,'31/03/2008'),\r\n" + 
				" ('A408','Itaberaba','BA',-12.524,-40.2997,'29/01/2003'),\r\n" + 
				" ('A455','Itamaraju','BA',-17.4366,-39.5508,'28/05/2016'),\r\n" + 
				" ('A446','Itapetinga','BA',-15.2446,-40.2295,'25/06/2009'),\r\n" + 
				" ('A407','Itirucu','BA',-13.5278,-40.1197,'09/02/2003'),\r\n" + 
				" ('A440','Jacobina','BA',-11.2051,-40.4649,'01/05/2008'),\r\n" + 
				" ('A450','Jeremoabo','BA',-10.0807,-38.3459,'13/08/2015'),\r\n" + 
				" ('A425','Lencois','BA',-12.5578,-41.3888,'05/04/2008'),\r\n" + 
				" ('A404','Luis Eduardo Magalhaes','BA',-12.1523,-45.8297,'18/04/2002'),\r\n" + 
				" ('A412','Macajuba','BA',-12.1316,-40.3542,'03/02/2003'),\r\n" + 
				" ('A438','Marau','BA',-13.9069,-38.9722,'22/06/2008'),\r\n" + 
				" ('A430','Piata','BA',-13.1556,-41.7741,'03/05/2008'),\r\n" + 
				" ('A449','Pilao Arcado','BA',-10.0183,-42.5002,'01/04/2016'),\r\n" + 
				" ('A427','Porto Seguro','BA',-16.3889,-39.1823,'10/07/2008'),\r\n" + 
				" ('A436','Queimadas','BA',-10.9846,-39.617,'23/05/2008'),\r\n" + 
				" ('A423','Remanso','BA',-9.6256,-42.0772,'26/03/2008'),\r\n" + 
				" ('A458','Ribeira do Amparo','BA',-11.0587,-38.444,'20/09/2018'),\r\n" + 
				" ('A401','Salvador','BA',-13.0055,-38.5057,'13/05/2000'),\r\n" + 
				" ('A456','Salvador-Est. Radio Marinha','BA',-12.8082,-38.4959,'09/03/2018'),\r\n" + 
				" ('A415','Santa Rita de Cassia','BA',-11.0027,-44.5249,'28/05/2007'),\r\n" + 
				" ('A428','Senhor do Bonfim','BA',-10.443,-40.1482,'27/03/2008'),\r\n" + 
				" ('A441','Serrinha','BA',-11.6645,-39.0229,'09/05/2008'),\r\n" + 
				" ('A435','Uaua','BA',-9.8336,-39.4956,'20/05/2008'),\r\n" + 
				" ('A437','Uma','BA',-15.2802,-39.0912,'25/06/2008'),\r\n" + 
				" ('A444','Valenca','BA',-13.3436,-39.1266,'29/06/2009'),\r\n" + 
				" ('A414','Vitoria da Conquista','BA',-14.8864,-40.8013,'01/06/2007'),\r\n" + 
				" ('A657','Afonso Claudio','ES',-20.1041,-41.1068,'25/09/2011'),\r\n" + 
				" ('A617','Alegre','ES',-20.7504,-41.4888,'25/10/2006'),\r\n" + 
				" ('A615','Alfredo Chaves','ES',-20.6365,-40.7418,'02/11/2006'),\r\n" + 
				" ('A631','Ecoporanga','ES',-18.2915,-40.7364,'17/03/2017'),\r\n" + 
				" ('A614','Linhares','ES',-19.3569,-40.0686,'27/10/2006'),\r\n" + 
				" ('A632','Marilandia','ES',-19.4071,-40.5398,'20/03/2017'),\r\n" + 
				" ('A623','Nova Venecia','ES',-18.6952,-40.3905,'23/06/2008'),\r\n" + 
				" ('A622','Presidente Kennedy','ES',-21.1008,-41.0393,'18/06/2008'),\r\n" + 
				" ('A613','Santa Teresa','ES',-19.9883,-40.5795,'09/08/2007'),\r\n" + 
				" ('A616','Sao Mateus','ES',-18.6761,-39.864,'26/10/2006'),\r\n" + 
				" ('A633','Venda Nova do Imigrante','ES',-20.3854,-41.1899,'11/02/2017'),\r\n" + 
				" ('A634','Vila velha','ES',-20.467,-40.404,'15/02/2017'),\r\n" + 
				" ('A612','Vitoria-Goiabeiras','ES',-20.271,-40.306,'31/10/2006'),\r\n" + 
				" ('A 549','Aguas Vermelhas','MG',-15.7515,-41.4577,'09/09/2007'),\r\n" + 
				" ('A534','Aimores','MG',-19.5327,-41.0908,'05/08/2007'),\r\n" + 
				" ('A508','Almenara','MG',-16.1667,-40.6877,'09/12/2002'),\r\n" + 
				" ('A566','Aracuai','MG',-16.8488,-42.0353,'21/05/2017'),\r\n" + 
				" ('A505','Araxa','MG',-19.6056,-46.9496,'19/12/2002'),\r\n" + 
				" ('A565','Bambui','MG',-20.0311,-46.0088,'22/11/2016'),\r\n" + 
				" ('A502','Barbacema','MG',-21.2283,-43.7677,'05/12/2002'),\r\n" + 
				" ('F501','Belo horizonte-Cercadinho','MG',-19.98,-43.9586,'01/10/2014'),\r\n" + 
				" ('A521','Belo Horizonte-Pampulha','MG',-19.8839,-43.9693,'10/10/2006'),\r\n" + 
				" ('A544','Buritis','MG',-15.5242,-46.4355,'19/06/2007'),\r\n" + 
				" ('A530','Caldas','MG',-21.918,-46.3829,'28/11/2006'),\r\n" + 
				" ('A519','Campina Verde','MG',-19.5392,-49.5181,'14/07/2003'),\r\n" + 
				" ('A541','Capelinha','MG',-17.7055,-42.3892,'02/09/2007'),\r\n" + 
				" ('A554','Caratinga','MG',-19.7357,-42.1371,'23/05/2007'),\r\n" + 
				" ('A548','Chapada Gaucha','MG',-15.3001,-45.6174,'22/06/2007'),\r\n" + 
				" ('A520','Conceicao das Alagoas','MG',-19.9858,-48.1515,'16/07/2006'),\r\n" + 
				" ('A557','Coronel Pacheco','MG',-21.5467,-43.261,'17/10/2012'),\r\n" + 
				" ('A538','Curvelo','MG',-18.7477,-44.4537,'19/12/2006'),\r\n" + 
				" ('A537','Diamantina','MG',-18.231,-43.6482,'05/06/2006'),\r\n" + 
				" ('A564','Divinopolis','MG',-20.1732,-44.8749,'26/10/2017'),\r\n" + 
				" ('A536','Dores do Indaia','MG',-19.4819,-45.5939,'01/06/2007'),\r\n" + 
				" ('A535','Florestal','MG',-19.8853,-44.4168,'27/06/2008'),\r\n" + 
				" ('A524','Formiga','MG',-20.4549,-45.4538,'17/08/2006'),\r\n" + 
				" ('A532','Governador Valadares','MG',-18.8303,-41.977,'29/05/2007'),\r\n" + 
				" ('A533','Guanhaes','MG',-18.7868,-42.9429,'02/06/2007'),\r\n" + 
				" ('A546','Guarda Mor','MG',-17.5613,-47.1992,'11/07/2007'),\r\n" + 
				" ('A555','Ibirite','MG',-20.0314,-44.0112,'07/06/2008'),\r\n" + 
				" ('A550','Itaobim','MG',-16.5756,-41.4855,'05/09/2007'),\r\n" + 
				" ('A512','Ituiutaba','MG',-18.9529,-49.525,'11/05/2006'),\r\n" + 
				" ('A563','Janauba','MG',-15.8028,-43.297,'19/11/2016'),\r\n" + 
				" ('A559','Januaria','MG',-15.448,-44.3663,'04/06/2016'),\r\n" + 
				" ('A553','Joao Pinheiro','MG',-17.7847,-46.1193,'07/07/2007'),\r\n" + 
				" ('A518','Juiz de Fora','MG',-21.7699,-43.3643,'26/05/2007'),\r\n" + 
				" ('A567','Machado','MG',-21.6807,-45.9443,'21/07/2017'),\r\n" + 
				" ('A556','Manhuacu','MG',-20.2633,-42.1828,'25/09/2010'),\r\n" + 
				" ('A540','Mantena','MG',-18.7806,-40.9865,'01/08/2007'),\r\n" + 
				" ('A531','Maria da Fe','MG',-22.3145,-45.373,'01/12/2006'),\r\n" + 
				" ('A539','Mocambinho','MG',-15.0859,-44.016,'11/11/2007'),\r\n" + 
				" ('A526','Montalvania','MG',-14.4082,-44.4041,'26/06/2007'),\r\n" + 
				" ('A506','Montes Claros','MG',-16.6863,-43.8437,'16/12/2002'),\r\n" + 
				" ('A509','Monte Verde','MG',-22.8616,-46.0433,'19/12/2004'),\r\n" + 
				" ('A517','Muriae','MG',-21.1048,-42.3759,'28/08/2006'),\r\n" + 
				" ('A570','Oliveira','MG',-20.7149,-44.8645,'20/10/2017'),\r\n" + 
				" ('A513','Ouro Branco','MG',-20.5565,-43.7562,'28/07/2006'),\r\n" + 
				" ('A571','Paracatu','MG',-17.2443,-46.8816,'15/03/2008'),\r\n" + 
				" ('A529','Passa Quatro','MG',-22.3957,-44.9619,'30/05/2007'),\r\n" + 
				" ('A516','Passos','MG',-20.7452,-46.6339,'16/07/2006'),\r\n" + 
				" ('A562','Patos de Minas','MG',-18.5206,-46.4406,'12/05/2017'),\r\n" + 
				" ('A523','Patrocinio','MG',-18.9966,-46.9859,'22/08/2006'),\r\n" + 
				" ('A545','Pirapora','MG',-17.258,-44.8356,'04/07/2007'),\r\n" + 
				" ('A551','Rio Pardo de Minas','MG',-15.7231,-42.4357,'17/11/2007'),\r\n" + 
				" ('A525','Sacramento','MG',-19.8752,-47.4341,'18/08/2006'),\r\n" + 
				" ('A552','Salinas','MG',-16.1603,-42.3102,'14/07/2007'),\r\n" + 
				" ('A514','Sao Joao Del Rei','MG',-21.1065,-44.2509,'09/06/2006'),\r\n" + 
				" ('A547','Sao Romao','MG',-16.3627,-45.1238,'30/06/2007'),\r\n" + 
				" ('A561','Sao Sebastiao do Paraiso','MG',-20.9098,-47.1142,'17/08/2015'),\r\n" + 
				" ('A522','Serra dos Aimores','MG',-17.7987,-40.2499,'21/08/2006'),\r\n" + 
				" ('A569','Sete Lagoas','MG',-19.4552,-44.1733,'10/04/2016'),\r\n" + 
				" ('A527','Teofilo Otoni','MG',-17.8928,-41.5154,'25/08/2006'),\r\n" + 
				" ('A511','Timoteo','MG',-19.5738,-42.6224,'22/02/2006'),\r\n" + 
				" ('A528','Tres Marias','MG',-18.2008,-45.4598,'28/08/2006'),\r\n" + 
				" ('A568','Uberaba','MG',-19.71,-47.9618,'18/05/2017'),\r\n" + 
				" ('A507','Uberlandia','MG',-18.917,-48.2556,'14/12/2002'),\r\n" + 
				" ('A542','Unai','MG',-16.5541,-46.8819,'15/06/2007'),\r\n" + 
				" ('A515','Varginha','MG',-21.5665,-45.4043,'13/07/2006'),\r\n" + 
				" ('A510','Vicosa','MG',-20.7626,-42.864,'15/09/2005'),\r\n" + 
				" ('A756','Agua Clara','MS',-20.4444,-52.8758,'15/08/2010'),\r\n" + 
				" ('A750','Amambai','MS',-23.0025,-55.3293,'12/06/2008'),\r\n" + 
				" ('S701','Angelica','MS',-22.148,-53.7637,'09/04/2018'),\r\n" + 
				" ('A719','Aquidauana','MS',-20.4754,-55.784,'01/11/2006'),\r\n" + 
				" ('S702','Aral Moreira','MS',-22.955,-62.626,'13/04/2018'),\r\n" + 
				" ('A759','Bataguassu','MS',-21.7521,-52.4712,'21/03/2013'),\r\n" + 
				" ('A757','Bela Vista','MS',-22.1015,-56.5407,'09/08/2011'),\r\n" + 
				" ('S705','Brasilandia','MS',-21.2982,-52.0689,'06/04/2018'),\r\n" + 
				" ('S706','Caarapo','MS',-22.657,-54.8193,'06/03/2018'),\r\n" + 
				" ('A702','Campo Grande','MS',-20.4471,-54.7226,'11/09/2001'),\r\n" + 
				" ('A742','Cassilandia','MS',-19.1224,-51.7207,'08/03/2008'),\r\n" + 
				" ('A730','Chapadao do Sul','MS',-18.8021,-52.6026,'19/12/2006'),\r\n" + 
				" ('A724','Corumba','MS',-18.9966,-57.6375,'27/10/2006'),\r\n" + 
				" ('A760','Costa Rica','MS',-18.4926,-53.1712,'04/12/2012'),\r\n" + 
				" ('A720','Coxim','MS',-18.5121,-54.7361,'07/11/2006'),\r\n" + 
				" ('A721','Dourados','MS',-22.1939,-54.9113,'21/10/2006'),\r\n" + 
				" ('S709','Iguatemi','MS',-23.6448,-54.5702,'14/04/2018'),\r\n" + 
				" ('S710','Itapora','MS',-22.0928,-54.7988,'26/02/2018'),\r\n" + 
				" ('A752','Itaquirai','MS',-23.4495,-54.1818,'31/05/2008'),\r\n" + 
				" ('A709','Invinhema','MS',-22.3004,-53.8228,'10/02/2003'),\r\n" + 
				" ('A758','Jardim','MS',-21.4785,-56.1377,'04/08/2011'),\r\n" + 
				" ('A749','Juti','MS',-22.8572,-54.6056,'18/06/2008'),\r\n" + 
				" ('S711','Laguna Carapa','MS',-22.5753,-55.1603,'07/03/2018'),\r\n" + 
				" ('A731','Maracaju','MS',-21.609,-55.1775,'14/12/2006'),\r\n" + 
				" ('A722','Miranda','MS',-20.3955,-56.4317,'02/11/2006'),\r\n" + 
				" ('A717','Nhumirim','MS',-18.9888,-56.6228,'03/08/2006'),\r\n" + 
				" ('S712','Nova Alvorada do Sul','MS',-21.4509,-54.3419,'22/02/2018'),\r\n" + 
				" ('A710','Paranaiba','MS',-19.6955,-51.1817,'13/11/2007'),\r\n" + 
				" ('S714','Pedro Gomes','MS',-18.0727,-54.5488,'02/02/2018'),\r\n" + 
				" ('A703','Ponta Pora','MS',-22.5524,-55.7163,'06/09/2001'),\r\n" + 
				" ('A723','Porto Murtinho','MS',-21.7058,-57.8865,'24/10/2006'),\r\n" + 
				" ('S715','Ribas do Rio Pardo','MS',-20.4669,-53.763,'18/01/2018'),\r\n" + 
				" ('A743','Rio Brilhante','MS',-21.7749,-54.5281,'26/06/2008'),\r\n" + 
				" ('S716','Santa Rita do Rio Pardo','MS',-21.3058,-52.8203,'19/04/2018'),\r\n" + 
				" ('A732','Sao Gabriel do Oeste','MS',-19.4203,-54.553,'16/12/2006'),\r\n" + 
				" ('S717','Selviria','MS',-20.3514,-51.4302,'06/04/2018'),\r\n" + 
				" ('A751','Sete Quedas','MS',-23.9668,-55.0242,'06/06/2008'),\r\n" + 
				" ('A754','Sidrolandia','MS',-20.9816,-54.9718,'30/09/2008'),\r\n" + 
				" ('A761','Sonora','MS',-17.6352,-54.7604,'30/11/2012'),\r\n" + 
				" ('A704','Tres Lagoas','MS',-20.79,-51.7122,'03/09/2001'),\r\n" + 
				" ('B803','Campina da Lagoa','PR',-24.5708,-52.8002,'04/08/2016'),\r\n" + 
				" ('A819','Castro','PR',-24.7869,-49.9992,'09/07/2006'),\r\n" + 
				" ('A869','Cidade Gaucha','PR',-23.3591,-52.9319,'10/03/2008'),\r\n" + 
				" ('A876','Clevelandia','PR',-26.4172,-52.3487,'16/05/2008'),\r\n" + 
				" ('B806','Colombo','PR',-25.3224,-49.1577,'28/05/2016'),\r\n" + 
				" ('A807','Curitiba','PR',-25.4486,-49.2306,'28/01/2003'),\r\n" + 
				" ('A849','Diamante do Norte','PR',-22.6393,-52.8901,'08/03/2008'),\r\n" + 
				" ('A843','Dois Vizinhos','PR',-25.699,-53.0952,'29/03/2007'),\r\n" + 
				" ('A846','Foz do Iguacu','PR',-25.6018,-54.483,'15/02/2008'),\r\n" + 
				" ('A875','General Carneiro','PR',-26.3984,-51.3536,'10/04/2008'),\r\n" + 
				" ('A825','Goioere','PR',-24.1889,-53.049,'19/10/2006'),\r\n" + 
				" ('A824','Icaraima','PR',-23.3903,-53.6359,'15/11/2006'),\r\n" + 
				" ('A823','Inacio Martins','PR',-25.5678,-51.0779,'16/10/2006'),\r\n" + 
				" ('A818','Ivai','PR',-25.0107,-50.8538,'12/07/2006'),\r\n" + 
				" ('A871','Japira','PR',-23.7733,-50.1805,'28/02/2008'),\r\n" + 
				" ('A821','Joaquim Tavora','PR',-23.5052,-49.9463,'23/11/2006'),\r\n" + 
				" ('B804','Laranjeiras do Sul','PR',-25.3689,-52.3919,'07/08/2016'),\r\n" + 
				" ('A820','Marechal Candido Rondon','PR',-24.5333,-54.0192,'18/11/2006'),\r\n" + 
				" ('A835','Maringa','PR',-23.4053,-51.9358,'21/11/2006'),\r\n" + 
				" ('A873','Morretes','PR',-25.5089,-48.8086,'13/03/2008'),\r\n" + 
				" ('A842','Nova Fatima','PR',-23.4152,-50.5777,'08/02/2007'),\r\n" + 
				" ('A822','Nova Tebas','PR',-24.4373,-51.963,'20/12/2006'),\r\n" + 
				" ('A850','Paranapoema','PR',-22.6582,-52.1345,'05/03/2008'),\r\n" + 
				" ('A855','Planalto','PR',-25.7218,-53.7479,'07/11/2007'),\r\n" + 
				" ('A874','Sao Mateus do Sul','PR',-25.8356,-50.3689,'18/04/2011'),\r\n" + 
				" ('A872','Ventania','PR',-24.2803,-50.2101,'15/04/2011'),\r\n" + 
				" ('A628','Angra dos Reis','RJ',-22.9756,-44.3034,'25/08/2017'),\r\n" + 
				" ('A606','Arraial do Cabo','RJ',-22.9754,-42.0214,'22/09/2006'),\r\n" + 
				" ('A604','Cambuci','RJ',-21.5877,-41.9583,'20/11/2002'),\r\n" + 
				" ('A607','Campos dos Goytacazes','RJ',-21.7147,-41.344,'22/09/2006'),\r\n" + 
				" ('A620','Campos-Sao Tome','RJ',-22.0416,-41.0518,'13/06/2008'),\r\n" + 
				" ('A629','Carmo','RJ',-21.9387,-42.6009,'11/10/2018'),\r\n" + 
				" ('A603','Duque de Caxias-Xerem','RJ',-22.5898,-43.2822,'21/10/2002'),\r\n" + 
				" ('A635','Itatiaia-Parque Nacional','RJ',-22.3739,-44.7031,'01/09/2017'),\r\n" + 
				" ('A608','Macae','RJ',-22.3763,-41.812,'22/09/2006'),\r\n" + 
				" ('A627','Niteroi','RJ',-22.8673,-43.102,'13/07/2018'),\r\n" + 
				" ('A624','Nova Friburgo-Salinas','RJ',-22.3348,-42.6769,'18/09/2010'),\r\n" + 
				" ('A619','Paraty','RJ',-23.2235,-44.7268,'06/12/2006'),\r\n" + 
				" ('A610','Petropilis-Pico do Couto','RJ',-22.4648,-43.2915,'22/10/2006'),\r\n" + 
				" ('A609','Resende','RJ',-22.4509,-44.4447,'29/09/2006'),\r\n" + 
				" ('A626','Rio Claro-Passa Tres','RJ',-22.6535,-44.0409,'03/06/2016'),\r\n" + 
				" ('A652','Rio de Janeiro-Forte de Copacabana','RJ',-22.9882,-43.1904,'18/05/2007'),\r\n" + 
				" ('A636','Rio de Janeiro-Jacarepagua','RJ',-22.9398,-43.4028,'10/08/2017'),\r\n" + 
				" ('A602','Rio de Janeiro-Marambaia','RJ',-23.0503,-43.5956,'07/10/2002'),\r\n" + 
				" ('A621','Rio de Janeiro-Vila Militar','RJ',-22.8613,-43.4114,'13/04/2007'),\r\n" + 
				" ('A630','Santa Maria Madalena','RJ',-21.9505,-42.0104,'16/11/2012'),\r\n" + 
				" ('A667','Saquarema-Sampaio Correia','RJ',-22.8713,-42.6092,'02/08/2015'),\r\n" + 
				" ('A601','Seropedica-Ecologia Agricola','RJ',-22.7578,-43.6848,'07/05/2000'),\r\n" + 
				" ('A659','Silvia Jardim','RJ',-22.6459,-42.4157,'28/08/2015'),\r\n" + 
				" ('A618','Teresopolis-Parque Nacional','RJ',-22.4489,-42.9871,'01/11/2006'),\r\n" + 
				" ('A625','Tres Rios','RJ',-22.0983,-43.2085,'07/06/2016'),\r\n" + 
				" ('A826','Alegrete','RS',-29.709,-55.5254,'29/09/2006'),\r\n" + 
				" ('A827','Bage','RS',-31.3478,-54.0132,'04/01/2007'),\r\n" + 
				" ('A840','Bento Goncalves','RS',-29.1645,-51.5342,'01/12/2006'),\r\n" + 
				" ('A812','Cacapava do Sul','RS',-30.5453,-53.467,'22/06/2006'),\r\n" + 
				" ('A838','Camaqua','RS',-30.8079,-51.8342,'12/12/2006'),\r\n" + 
				" ('A897','Cambara do Sul','RS',-29.0491,-50.1496,'24/11/2016'),\r\n" + 
				" ('A884','Campo Bom','RS',-29.6742,-51.064,'29/11/2013'),\r\n" + 
				" ('A879','Canela','RS',-29.3687,-50.8272,'23/08/2008'),\r\n" + 
				" ('A811','Cangucu','RS',-31.4032,-52.7006,'24/01/2007'),\r\n" + 
				" ('A887','Capao do Leao-Pelotas','RS',-31.8025,-52.4072,'18/07/2019'),\r\n" + 
				" ('A853','Cruz Alta','RS',-28.6034,-53.6735,'31/05/2007'),\r\n" + 
				" ('A881','Dom Pedrito','RS',-31.0025,-54.6181,'23/04/2010'),\r\n" + 
				" ('A893','Encruzilhado do Sul','RS',-30.5431,-52.5246,'15/03/2018'),\r\n" + 
				" ('A828','Erechim','RS',-27.6577,-52.3058,'25/11/2006'),\r\n" + 
				" ('A854','Fredrico Westphalen','RS',-27.3956,-53.4294,'13/12/2007'),\r\n" + 
				" ('A883','Ibiruba','RS',-28.6534,-53.1118,'13/12/2012'),\r\n" + 
				" ('A836','Jaguarao','RS',-32.5348,-53.3758,'09/01/2007'),\r\n" + 
				" ('A844','Lagoa Vermelha','RS',-28.2223,-51.5128,'02/03/2007'),\r\n" + 
				" ('A878','Mostardas','RS',-31.2482,-50.9062,'12/03/2008'),\r\n" + 
				" ('A856','Palmeira das Missoes','RS',-27.9203,-53.318,'27/02/2008'),\r\n" + 
				" ('A839','Passo Fundo','RS',-28.2268,-52.4035,'27/11/2006'),\r\n" + 
				" ('A801','Porto alegre','RS',-30.0535,-51.1747,'18/09/2000'),\r\n" + 
				" ('A831','Quarai','RS',-30.3685,-56.4371,'17/10/2007'),\r\n" + 
				" ('A802','Rio Grande','RS',-32.0787,-52.1677,'06/09/2001'),\r\n" + 
				" ('A813','Rio Pardo','RS',-29.8721,-52.3819,'01/10/2006'),\r\n" + 
				" ('A803','Santa Maria','RS',-29.7249,-53.7204,'03/09/2001'),\r\n" + 
				" ('A804','Santana do Livramento','RS',-30.8424,-55.613,'21/11/2001'),\r\n" + 
				" ('A810','Santa Rosa','RS',-27.8904,-54.48,'15/11/2006'),\r\n" + 
				" ('A899','Santa Vitoria do Palmar','RS',-33.7422,-53.3722,'15/03/2008'),\r\n" + 
				" ('A833','Santiago','RS',-29.1915,-54.8856,'05/02/2009'),\r\n" + 
				" ('A805','Santo Augusto','RS',-27.8543,-53.7911,'05/12/2001'),\r\n" + 
				" ('A830','Sao Borja','RS',-28.65,-56.0162,'21/07/2007'),\r\n" + 
				" ('A832','Sao Gabriel','RS',-30.3414,-54.3109,'12/10/2007'),\r\n" + 
				" ('A829','Sao Jose dos Ausentes','RS',-28.7486,-50.0578,'26/10/2006'),\r\n" + 
				" ('A852','Sao Luiz Gonzaga','RS',-28.4171,-54.9624,'25/07/2007'),\r\n" + 
				" ('A889','Sao Vicente do Sul','RS',-29.7021,-54.6943,'06/04/2016'),\r\n" + 
				" ('A894','Serafina Correa','RS',-28.7048,-51.8707,'06/04/2016'),\r\n" + 
				" ('A837','Soledade','RS',-28.8592,-52.5423,'27/02/2008'),\r\n" + 
				" ('A882','Teutonia','RS',-29.4503,-51.8242,'04/10/2012'),\r\n" + 
				" ('A808','Torres','RS',-29.3503,-49.7332,'01/06/2006'),\r\n" + 
				" ('A834','Tramandai','RS',-30.0102,-50.1358,'09/03/2008'),\r\n" + 
				" ('A886','Tupancireta','RS',-29.0893,-53.8266,'11/08/2016'),\r\n" + 
				" ('A809','Uruguaiana','RS',-29.8398,-57.0818,'25/09/2006'),\r\n" + 
				" ('A880','Vacaria','RS',-28.5136,-50.8827,'26/04/2008'),\r\n" + 
				" ('A867','Aranrangua','SC',-28.9313,-49.4979,'28/09/2008'),\r\n" + 
				" ('A859','Cacador','SC',-26.8191,-50.9855,'19/03/2008'),\r\n" + 
				" ('A898','Campos Novos','SC',-27.3838,-51.216,'15/02/2019'),\r\n" + 
				" ('A895','Chapeco','SC',-27.0853,-52.6357,'18/02/2019'),\r\n" + 
				" ('A860','Curitibanos','SC',-27.2886,-50.6042,'03/03/2008'),\r\n" + 
				" ('A848','Dionisio Cerqueira','SC',-26.2865,-53.6331,'31/05/2008'),\r\n" + 
				" ('A806','Florianopolis-Sao Jose','SC',-27.6025,-48.62,'22/01/2003'),\r\n" + 
				" ('A817','Indaial','SC',-26.9137,-49.2679,'02/07/2006'),\r\n" + 
				" ('A868','Itajai','SC',-26.9509,-48.762,'24/06/2010'),\r\n" + 
				" ('A851','Itapoa','SC',-26.0813,-48.6417,'06/06/2007'),\r\n" + 
				" ('A863','Ituporanga','SC',-27.4184,-49.6468,'05/03/2008'),\r\n" + 
				" ('A841','Joacaba','SC',-27.1692,-51.5589,'19/09/2007'),\r\n" + 
				" ('A865','Lajes','SC',-27.8022,-50.3354,'06/11/2014'),\r\n" + 
				" ('A866','Laguna-Farol Santa Marta','SC',-28.6044,-48.8133,'31/05/2008'),\r\n" + 
				" ('A864','Major Vieira','SC',-26.3936,-50.3632,'09/02/2009'),\r\n" + 
				" ('A845','Morro da Igreja-Bom Jardim da Serra','SC',-28.1269,-49.4796,'18/06/2007'),\r\n" + 
				" ('A816','Novo Horizonte','SC',-26.4064,-52.8503,'20/09/2008'),\r\n" + 
				" ('A870','Rancho Queimado','SC',-27.6785,-49.042,'01/06/2016'),\r\n" + 
				" ('A861','Rio do Campo','SC',-26.9375,-50.1454,'06/03/2008'),\r\n" + 
				" ('A862','Rio Negrinho','SC',-26.2484,-49.5806,'22/03/2008'),\r\n" + 
				" ('A815','Sao Joaquim','SC',-28.2756,-49.9346,'12/04/2008'),\r\n" + 
				" ('A857','Sao Miguel do Oeste','SC',-26.7766,-53.5045,'12/03/2008'),\r\n" + 
				" ('A814','Urussanga','SC',-28.5325,-49.3152,'28/05/2008'),\r\n" + 
				" ('A858','Xanxare','SC',-26.9386,-52.398,'15/03/2008')\r\n" + 
				""				
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
