package br.embrapa.cnpaf.inmetdata.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;
import br.embrapa.cnpaf.inmetdata.entity.InmetCityEntily;
import br.embrapa.cnpaf.inmetdata.entity.InmetStateEntily;
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
public class InmetCityDataDAO extends GenericDAO<InmetCityDataDAO, InmetCityEntily> {

	private static InmetCityDataDAO instance;

	/**
	 * Private class constructor.
	 * 
	 * @param logClientName Name of the client object of the logging service.
	 * @param logLevel      Log level to be used in log service.
	 * @throws PersistenceException Occurrence of any problems in creating of the
	 *                              DAO.
	 */
	private InmetCityDataDAO(String logClientName, Level logLevel) throws PersistenceException {
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
	public static synchronized InmetCityDataDAO getInstanceOf(String logClientName, Level logLevel)
			throws PersistenceException {
		if (InmetCityDataDAO.instance == null) {
			InmetCityDataDAO.instance = new InmetCityDataDAO(logClientName, logLevel);
		}
		return InmetCityDataDAO.instance;
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
	public static synchronized InmetCityDataDAO getInstanceOf() throws PersistenceException {
		return InmetCityDataDAO.getInstanceOf(InmetCityDataDAO.class.getSimpleName(), LOG_DEFAULT_LEVEL);
	}

	@Override
	public InmetCityDataDAO save(InmetCityEntily entity) throws PersistenceException {

		// validate entity
		Long id = entity != null ? entity.getId() : null;
		// save Relationship
		this.saveStationRelationship(entity.getStateEntily());

		// save ou update the entity
		id = super.save(//
				entity.getId() //
				, "INSERT INTO public." + TABLE_INMET_CITY + "(" + //
						"id_state," + //
						"latitude," + //
						"longitude," + //
						"name)" + //
						"VALUES" + "(" + //
						entity.getStateEntily().getId() + "," + //
						entity.getLatitude() + "," + //
						entity.getLongitude() + "," + //
						"'" + entity.getName() + "'" + ")" + ";"////
				, "UPDATE public." + TABLE_INMET_CITY + " SET " + //
						"id=" + entity.getId() + "," + //
						"id_state=" + entity.getStateEntily().getId() + "," + //
						"latitude=" + entity.getLatitude() + "," + //
						"longitude=" + entity.getLongitude() + "," + //
						"name=" + "'" + entity.getName() + "'" + //
						" WHERE id= " + entity.getId()); //

		// return DAO instance
		return this;
	}

	@Override
	public InmetCityDataDAO remove(Long id) throws PersistenceException {

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
	public InmetCityEntily find(Long id) throws PersistenceException {
		return super.find(id, "SELECT * FROM " + TABLE_INMET_CITY + " WHERE id=" + id + ";");
	}

	@Override
	public List<InmetCityEntily> list() throws PersistenceException {
		return super.list("SELECT * FROM " + TABLE_INMET_CITY + ";");
	}

	@Override
	protected InmetCityDataDAO init() throws PersistenceException {

		// initializing variables
		List<String> queries = new ArrayList<String>();

		// SQL for entity table create
		queries.add(//
				"CREATE TABLE IF NOT EXISTS public." + TABLE_INMET_CITY + "(" //
						+ "id bigserial NOT NULL," //
						+ "id_state bigserial NOT NULL REFERENCES " + TABLE_INMET_STATE + "(id)," //
						+ "latitude double precision NOT NULL," + "longitude double precision NOT NULL," //
						+ "name text NOT NULL," + "PRIMARY KEY (id)" + ");"); //

		// initializing table
		super.init(queries);

		// Population init
		List<InmetCityEntily> entilies = this.list();
		if (entilies.size() == 0) {
			queries.clear();
			queries.add("INSERT INTO city(id,id_state,latitude,longitude,name) VALUES " + //
					"(1,1,-14.133,-47.5232,'Alto Paraiso'),\r\n" + //
					"(2,1,-15.9026,-52.2451,'Aragarcas'),\r\n" + //
					"(3,1,-16.9668,-51.8175,'Caiaponia'),\r\n" + //
					"(4,1,-18.1547,-47.9276,'Catalao'),\r\n" + //
					"(5,1,-16.7848,-47.6129,'Cristalina'),\r\n" + //
					"(7,1,-17.337,-49.9147,'Edeia'),\r\n" + //
					"(8,1,-15.2201,-48.9901,'Goianesia'),\r\n" + //
					"(9,1,-16.6428,-49.2202,'Goiania'),\r\n" + //
					"(10,1,-15.9397,-50.1414,'Goias'),\r\n" + //
					"(11,1,-16.423,-51.1488,'Ipora'),\r\n" + //
					"(12,1,-14.9797,-49.5399,'Itapaci'),\r\n" + //
					"(13,1,-18.4097,-49.192,'Itumbiara'),\r\n" + //
					"(14,1,-17.9236,-51.7174,'Jatai'),\r\n" + //
					"(15,1,-16.2605,-47.9669,'Luziania'),\r\n" + //
					"(16,1,-47.4548,-52.6012,'Mineiros'),\r\n" + //
					"(17,1,-13.2535,-46.8903,'Monte Alegre de Goias'),\r\n" + //
					"(18,1,-17.745,-49.1016,'Morrinhos'),\r\n" + //
					"(19,1,-16.9625,-50.4254,'Parauna'),\r\n" + //
					"(20,1,-17.3041,-48.2841,'Pires do Rio'),\r\n" + //
					"(21,1,-13.3095,-49.1174,'Porangatu'),\r\n" + //
					"(22,1,-14.0892,-46.3664,'Posse'),\r\n" + //
					"(23,1,-17.7853,-50.9648,'Rio Verde'),\r\n" + //
					"(24,1,-12.8204,-50.3359,'Sao Miguel do Araguaia'),\r\n" + //
					"(25,1,-18.9691,-50.6334,'Sao Simao'),\r\n" + //
					"(26,2,-14.0164,-52.2116,'Agua Boa'),\r\n" + //
					"(27,2,-10.0772,-56.1792,'Alta Floresta'),\r\n" + //
					"(28,2,-17.3393,-53.2244,'Alto Araguaia'),\r\n" + //
					"(29,2,-17.841,-53.2894,'Alto Taquari'),\r\n" + //
					"(30,2,-9.5633,-57.3935,'Apiacas'),\r\n" + //
					"(31,2,-16.0747,-57.693,'Caceres'),\r\n" + //
					"(32,2,-13.7859,-57.8384,'Campo Novo dos Parecis'),\r\n" + //
					"(33,2,-15.5314,-55.1356,'Campo Verde'),\r\n" + //
					"(34,2,-9.9705,-55.8275,'Carlinda'),\r\n" + //
					"(35,2,-13.708,-59.7623,'Comodoro'),\r\n" + //
					"(36,2,-9.9062,-58.5722,'Cotriguacu'),\r\n" + //
					"(37,2,-15.6068,-56.0609,'Cuiaba'),\r\n" + //
					"(38,2,-13.1848,-53.2573,'Gaucha do Norte'),\r\n" + //
					"(39,2,-9.9525,-54.8978,'Guarata do Norte'),\r\n" + //
					"(40,2,-16.3415,-53.766,'Guiratinga'),\r\n" + //
					"(41,2,-17.175,-54.5016,'Itiquira'),\r\n" + //
					"(42,2,-11.2801,-57.5266,'Juara'),\r\n" + //
					"(43,2,-11.3751,-58.7747,'Juina'),\r\n" + //
					"(44,2,-13.0386,-57.0922,'Nova Maringa'),\r\n" + //
					"(45,2,-13.4111,-54.7521,'Nova Ubirata'),\r\n" + //
					"(46,2,-12.5219,-58.2314,'Novo Mundo'),\r\n" + //
					"(47,2,-14.4214,-54.036,'Paranatinga'),\r\n" + //
					"(48,2,-15.3255,-59.2338,'Pontes e Lacerda'),\r\n" + //
					"(49,2,-15.3247,-57.2258,'Porto Estrela'),\r\n" + //
					"(50,2,-15.5799,54.381,'Primavera do Leste'),\r\n" + //
					"(51,2,-12.6273,-52.2208,'Querencia'),\r\n" + //
					"(52,2,-16.4624,-54.5801,'Rondonopolis'),\r\n" + //
					"(53,2,-14.8289,-56.4419,'Rosario Oeste'),\r\n" + //
					"(54,2,-15.1248,-58.127,'Salto do Ceu'),\r\n" + //
					"(55,2,-14.9278,-53.8836,'Santo Antonio do Leste'),\r\n" + //
					"(56,2,-11.619,-50.7278,'Sao Felix do Araguaia'),\r\n" + //
					"(57,2,-13.4539,-56.6773,'Sao Jose do Rio Claro'),\r\n" + //
					"(58,2,-10.4841,-52.3725,'Sao Jose do Xingu'),\r\n" + //
					"(59,2,-13.304,-58.7635,'Sapezal'),\r\n" + //
					"(60,2,-11.9879,-51.426,'Serra Nova Dourada'),\r\n" + //
					"(61,2,-11.9822,-55.5659,'Sinop'),\r\n" + //
					"(62,2,-12.5551,-55.7228,'Sorriso'),\r\n" + //
					"(63,2,-14.6501,-57.4315,'Tangara da Serra'),\r\n" + //
					"(64,2,-15.0626,-59.8729,'Vila Bela da Santissima Trindade'),\r\n" + //
					"(65,3,-9.949,-62.9618,'Ariquemes'),\r\n" + //
					"(66,3,-11.4458,-61.4341,'Cacoal'),\r\n" + //
					"(67,3,-8.7936,-63.8459,'Porto Velho'),\r\n" + //
					"(68,3,-12.7348,-60.1577,'Vilhena'),\r\n" + //
					"(69,4,-11.284,-47.2121,'Almas'),\r\n" + //
					"(70,4,-12.5922,-49.5287,'Araguacu'),\r\n" + //
					"(71,4,-7.1039,-48.2012,'Araguaina'),\r\n" + //
					"(72,4,-5.6437,-48.1118,'Araguatins'),\r\n" + //
					"(73,4,-8.1546,-46.6393,'Campos Lindos'),\r\n" + //
					"(74,4,-8.0927,-48.4786,'Colinas do Tocantins'),\r\n" + //
					"(75,4,-11.5944,-46.8472,'Dianopolis'),\r\n" + //
					"(76,4,-11.8873,-49.6082,'Formoso do Araguaia'),\r\n" + //
					"(77,4,-11.7457,-49.0497,'Gurupi'),\r\n" + //
					"(78,4,-10.8282,-49.8478,'Lagoa da Confusao'),\r\n" + //
					"(79,4,-9.5763,-49.7233,'Marianopolis do Tocantis'),\r\n" + //
					"(80,4,-10.4344,-45.9219,'Mateiros'),\r\n" + //
					"(81,4,-10.1907,-48.3018,'Palmas'),\r\n" + //
					"(82,4,-12.6148,-47.8719,'Parana'),\r\n" + //
					"(83,4,-8.9686,-48.1772,'Pedro Afonso'),\r\n" + //
					"(84,4,-12.0153,-48.5445,'Peixe'),\r\n" + //
					"(85,4,-10.4769,-49.6294,'Pium'),\r\n" + //
					"(86,4,-9.7933,-47.1327,'Rio Sono'),\r\n" + //
					"(87,4,-7.1241,-48.7812,'Santa Fe do Araguaia'),\r\n" + //
					"(88,4,-11.429,-48.1848,'Santa Rosa do Tocantins'),\r\n" + //
					"(89,5,-3.2702,-52.3948,'Altamira'),\r\n" + //
					"(90,5,-1.4112,-48.4395,'Belem'),\r\n" + //
					"(91,5,-1.0472,-46.7857,'Braganca'),\r\n" + //
					"(92,5,-1.6802,-50.4781,'Breves'),\r\n" + //
					"(93,5,-2.2396,-49.4998,'Cameta'),\r\n" + //
					"(94,5,-1.7347,-47.0575,'Capitao Poco'),\r\n" + //
					"(95,5,-1.3008,-47.9479,'Castanhal'),\r\n" + //
					"(96,5,-8.3036,-49.2827,'Conceicao do Araguaia'),\r\n" + //
					"(97,5,-4.2805,-47.5639,'Dom Eliseu'),\r\n" + //
					"(98,5,-4.2769,-55.993,'Itaituba'),\r\n" + //
					"(99,5,-5.3664,-49.0511,'Maraba'),\r\n" + //
					"(100,5,-3.5109,-52.9634,'Medicilandia'),\r\n" + //
					"(101,5,-6.32,-55.7877,'Mina Palito'),\r\n" + //
					"(102,5,-2,-54.0764,'Monte Alegre'),\r\n" + //
					"(103,5,-4.2439,-49.9393,'Novo Repartimento'),\r\n" + //
					"(104,5,-1.8807,-55.5198,'Obidos'),\r\n" + //
					"(105,5,-3.8437,-50.638,'Pacaja'),\r\n" + //
					"(106,5,-2.9903,-47.4078,'Paragominas'),\r\n" + //
					"(107,5,-3.864,-54.2154,'Placas'),\r\n" + //
					"(108,5,-1.8221,-52.1117,'Porto de Moz'),\r\n" + //
					"(109,5,-8.3432,-50.0069,'Redencao'),\r\n" + //
					"(110,5,-4.8275,48.1735,'Rondon do Para'),\r\n" + //
					"(111,5,-0.6188,-47.3565,'Salinopolis'),\r\n" + //
					"(112,5,-9.3386,-50.3502,'Santana do Araguaia'),\r\n" + //
					"(113,5,-2.5025,-54.7202,'Santarem'),\r\n" + //
					"(114,5,-6.6396,-51.9569,'Sao Felix do Xingu'),\r\n" + //
					"(115,5,-6.0774,-50.1422,'Serra dos Carajas'),\r\n" + //
					"(116,5,-0.7278,-48.5157,'Soure'),\r\n" + //
					"(117,5,-2.5926,-48.3605,'Tome Acu'),\r\n" + //
					"(118,5,-6.7432,-51.1418,'Tucuma'),\r\n" + //
					"(119,5,-3.8227,-49.6749,'Tucurui'),\r\n" + //
					"(120,5,-7.1105,-49.926,'Xinguara'),\r\n" + //
					"(121,6,-9.1081,-45.932,'Alto Parnaiba'),\r\n" + //
					"(122,6,-4.243,-44.7949,'Bacabal'),\r\n" + //
					"(123,6,-7.4556,-46.0275,'Balsas'),\r\n" + //
					"(124,6,-5.5067,-45.237,'Barra do Corda'),\r\n" + //
					"(125,6,-4.3205,-46.4495,'Buriticupu'),\r\n" + //
					"(126,6,-7.3372,-47.4598,'Carolina'),\r\n" + //
					"(127,6,-4.8213,-43.3436,'Caxias'),\r\n" + //
					"(128,6,-3.7426,-43.352,'Chapadinha'),\r\n" + //
					"(129,6,-6.0331,-44.2334,'Colinas'),\r\n" + //
					"(130,6,-6.6532,-47.4182,'Estreito'),\r\n" + //
					"(131,6,-2.5923,-42.7075,'Farol Preguicas'),\r\n" + //
					"(132,6,-2.2708,-43.624,'Farol Santana'),\r\n" + //
					"(133,6,-5.816,-46.1622,'Grajau'),\r\n" + //
					"(134,6,-5.5557,-47.4597,'Imperatriz'),\r\n" + //
					"(135,6,-2.5267,-44.2165,'Sao Luis'),\r\n" + //
					"(136,6,-1.6612,-45.3725,'Turiacu'),\r\n" + //
					"(137,6,-3.2691,-45.651,'Ze Doca'),\r\n" + //
					"(138,7,-8.4415,-43.8654,'Alvorada do Gurgueia'),\r\n" + //
					"(139,7,-6.0894,-42.7273,'Angical do Piaui'),\r\n" + //
					"(140,7,-8.3335,-45.0946,'Baixa Grande do Ribeira'),\r\n" + //
					"(141,7,-9.0832,-44.3264,'Bom Jesus do Piaui'),\r\n" + //
					"(142,7,-4.8641,-42.1455,'Campo Maior'),\r\n" + //
					"(143,7,-8.1179,-42.9757,'Canto do Buriti'),\r\n" + //
					"(144,7,-9.2858,-43.3244,'Caracol'),\r\n" + //
					"(145,7,-5.3492,-41.5122,'Castelo do Piaui'),\r\n" + //
					"(146,7,-10.4291,-45.173,'Corrente'),\r\n" + //
					"(147,7,-6.7614,-43.0033,'Floriano'),\r\n" + //
					"(148,7,-9.8751,-45.3458,'Gilbues'),\r\n" + //
					"(149,7,-6.9741,-42.1468,'Oeiras'),\r\n" + //
					"(150,7,-3.0866,-41.7831,'Parnaiba'),\r\n" + //
					"(151,7,-8.1322,-41.1429,'Paulistana'),\r\n" + //
					"(152,7,-7.071,-41.404,'Picos'),\r\n" + //
					"(153,7,-4.276,-41.7945,'Piripiri'),\r\n" + //
					"(154,7,-8.3649,-42.2503,'Sao Joao do Piaui'),\r\n" + //
					"(155,7,-5.911,-42.7187,'Sao Pedro do Piaui'),\r\n" + //
					"(156,7,-9.0332,-42.7011,'Sao Raimundo Nonato'),\r\n" + //
					"(157,7,-5.0347,-42.8013,'Teresina'),\r\n" + //
					"(158,7,-7.4414,-44.345,'Urucui'),\r\n" + //
					"(159,7,-6.3992,-41.7399,'Valenca do Piaui'),\r\n" + //
					"(160,8,-3.121,-40.0872,'Acarau'),\r\n" + //
					"(161,8,-7.3009,-39.2711,'Barbalha'),\r\n" + //
					"(162,8,-7.0777,-40.3626,'Campos Sales'),\r\n" + //
					"(163,8,-5.1866,-40.6721,'Crateus'),\r\n" + //
					"(164,8,-3.8157,-38.5377,'Fortaleza'),\r\n" + //
					"(165,8,-4.2613,-38.931,'Guaramiranga'),\r\n" + //
					"(166,8,-6.3963,-39.2689,'Iguatu'),\r\n" + //
					"(167,8,-3.4842,-39.5887,'Itapipoca'),\r\n" + //
					"(168,8,-5.9055,-38.6277,'Jaguaribe'),\r\n" + //
					"(169,8,-4.8377,-37.6997,'Jaguaruana'),\r\n" + //
					"(170,8,-5.1366,-38.3565,'Morada Nova'),\r\n" + //
					"(171,8,-4.9787,-39.0571,'Quixada'),\r\n" + //
					"(172,8,-5.1745,-39.2893,'Quixeramobim'),\r\n" + //
					"(173,8,-3.7481,-40.3457,'Sobral'),\r\n" + //
					"(174,8,-6.0174,-40.2813,'Taua'),\r\n" + //
					"(175,8,-3.7321,-41.0118,'Tiangua'),\r\n" + //
					"(176,9,-15.5964,-47.6258,'Aguas Emendadas'),\r\n" + //
					"(177,9,-15.7893,-47.9257,'Brasilia'),\r\n" + //
					"(178,9,-15.6528,-48.2013,'Braszlandia'),\r\n" + //
					"(179,9,-15.9351,-48.1373,'Gama'),\r\n" + //
					"(180,9,-16.0122,-47.5574,'Paranoa'),\r\n" + //
					"(181,10,-10.9524,-37.0543,'Aracaju'),\r\n" + //
					"(182,10,-10.4739,-36.482,'Brejo Grande'),\r\n" + //
					"(183,10,-10.3997,-37.7475,'Carira'),\r\n" + //
					"(184,10,-10.6717,-37.4715,'Itabaiana'),\r\n" + //
					"(185,10,-11.2725,-37.795,'Itabaianinha'),\r\n" + //
					"(186,10,-10.2077,-37.4318,'Nossa Senhora da Gloria'),\r\n" + //
					"(187,10,-10.738,-38.1083,'Poco Verde'),\r\n" + //
					"(188,11,-21.1329,-48.8404,'Ariranha'),\r\n" + //
					"(189,11,-23.1017,-48.9409,'Avare'),\r\n" + //
					"(190,11,-22.4712,-48.5575,'Barra Bonita'),\r\n" + //
					"(191,11,-24.9628,-48.4163,'Barra do Turvo'),\r\n" + //
					"(192,11,-20.5591,-48.5449,'Barretos'),\r\n" + //
					"(193,11,-23.5238,-46.8994,'Barueri'),\r\n" + //
					"(194,11,-22.358,-49.0288,'Bauru'),\r\n" + //
					"(195,11,-20.9491,-48.4899,'Bebedouro'),\r\n" + //
					"(196,11,-23.8446,-46.1433,'Bertioga'),\r\n" + //
					"(197,11,-22.9519,-46.5305,'Braganca Paulista'),\r\n" + //
					"(198,11,-22.689,-45.0054,'Cachoeira Paulista'),\r\n" + //
					"(199,11,-22.7502,-45.6038,'Campos do jordao'),\r\n" + //
					"(200,11,-21.7805,-47.0752,'Casa Branca'),\r\n" + //
					"(201,11,-21.4576,-51.5522,'Dracena'),\r\n" + //
					"(202,11,-20.5844,-47.3825,'Franca'),\r\n" + //
					"(203,11,-21.8555,-48.7997,'Ibitinga'),\r\n" + //
					"(204,11,-24.6716,-47.5458,'Iguape'),\r\n" + //
					"(205,11,-23.9819,-48.8858,'Itapeva'),\r\n" + //
					"(206,11,-22.415,-46.8052,'Itapira'),\r\n" + //
					"(207,11,-20.3597,-47.7752,'Ituverava'),\r\n" + //
					"(208,11,-20.165,-50.5951,'Jales'),\r\n" + //
					"(209,11,-21.0856,-49.9203,'Jose Bonifacio'),\r\n" + //
					"(210,11,-21.666,-49.7348,'Lins'),\r\n" + //
					"(211,11,-22.2352,-49.9651,'Marilia'),\r\n" + //
					"(212,11,-22.949,-49.8945,'Ourinhos'),\r\n" + //
					"(213,11,-22.7031,-47.6233,'Piracicaba'),\r\n" + //
					"(214,11,-21.3384,-48.114,'Pradopolis'),\r\n" + //
					"(215,11,-22.1198,-51.4086,'Presidente Prudente'),\r\n" + //
					"(216,11,-22.3728,-50.9747,'Rancharia'),\r\n" + //
					"(217,11,-24.5331,-47.8641,'Registro'),\r\n" + //
					"(218,11,-21.9803,-47.8839,'Sao Carlos'),\r\n" + //
					"(219,11,-23.2283,-45.417,'Sao Luis do Paraitinga'),\r\n" + //
					"(220,11,-23.852,-48.1648,'Sao Miguel Arcanjo'),\r\n" + //
					"(221,11,-23.4962,-46.62,'Sao Paulo - Mirante do Santana'),\r\n" + //
					"(222,11,-23.7244,-46.6775,'Sao Paulo - Interlagos'),\r\n" + //
					"(223,11,-23.8107,-45.4025,'Sao Sebastiao'),\r\n" + //
					"(225,11,-23.426,-47.5855,'Sorocaba'),\r\n" + //
					"(226,11,-23.0416,-45.5208,'Taubate'),\r\n" + //
					"(227,11,-21.9272,-50.4902,'Tupa'),\r\n" + //
					"(228,11,-21.3191,-50.9301,'Valparaiso'),\r\n" + //
					"(229,11,-20.4032,-49.966,'Votuporanga'),\r\n" + //
					"(230,12,-5.6265,-37.8149,'Apodi'),\r\n" + //
					"(231,12,-6.4674,-37.0849,'Caico'),\r\n" + //
					"(232,12,-5.1599,-35.4876,'Calcanhar'),\r\n" + //
					"(233,12,-5.5348,-36.8723,'Ipanguacu'),\r\n" + //
					"(234,12,-5.151,-36.5731,'Macau'),\r\n" + //
					"(235,12,-4.904,-37.3669,'Mossoro'),\r\n" + //
					"(236,12,-5.8371,-35.2079,'Natal'),\r\n" + //
					"(237,12,-6.2279,-36.0265,'Santa Cruz'),\r\n" + //
					"(238,13,-6.9754,-35.7181,'Areia'),\r\n" + //
					"(239,13,-7.4832,-36.2864,'Cabaceiras'),\r\n" + //
					"(240,13,-6.5618,-35.1353,'Camaratuba'),\r\n" + //
					"(241,13,-7.2255,-35.9048,'Campina Grande'),\r\n" + //
					"(242,13,-7.3184,-38.1408,'Itaporanga'),\r\n" + //
					"(243,13,-7.1654,-34.8156,'Joao Pessoa'),\r\n" + //
					"(244,13,-7.8944,-37.1247,'Monteiro'),\r\n" + //
					"(245,13,-7.0798,-37.2728,'Patos'),\r\n" + //
					"(246,13,-6.8357,-38.3115,'Sao Goncalo'),\r\n" + //
					"(247,14,-8.4335,-37.0554,'Arco Verde'),\r\n" + //
					"(248,14,-8.504,-39.3152,'Cabrobo'),\r\n" + //
					"(249,14,-8.3651,-36.0284,'Caruaru'),\r\n" + //
					"(250,14,-8.5987,-38.584,'Floresta'),\r\n" + //
					"(251,14,-8.9109,-36.4933,'Garanhuns'),\r\n" + //
					"(252,14,-8.5095,-37.7115,'Ibimirim'),\r\n" + //
					"(253,14,-7.8858,-40.1026,'Ouricuri'),\r\n" + //
					"(254,14,-8.6666,-35.5679,'Palmares'),\r\n" + //
					"(255,14,-9.3883,-40.5232,'Petrolina'),\r\n" + //
					"(256,14,-8.0592,-34.9592,'Recife'),\r\n" + //
					"(257,14,-8.058,-39.0961,'Salgueiro'),\r\n" + //
					"(258,14,-0.9168,-29.3459,'Sao Pedro e Sao Paulo'),\r\n" + //
					"(259,14,-7.9542,-38.295,'Serra Talhada'),\r\n" + //
					"(260,14,-7.8396,-35.801,'Surubim'),\r\n" + //
					"(261,15,-7.6107,-72.6812,'Cruzeiro do Sul'),\r\n" + //
					"(262,15,-11.0237,-68.7351,'Epitaciolandia'),\r\n" + //
					"(263,15,-8.1426,-70.3435,'Feijo'),\r\n" + //
					"(264,15,-8.95,-72.7867,'Marechal Thaumaturgo'),\r\n" + //
					"(265,15,-9.3583,-69.9262,'Parque Estadual Chandless'),\r\n" + //
					"(266,15,-8.2671,-72.7478,'Porto Walter'),\r\n" + //
					"(267,15,-9.9578,-68.1651,'Rio Branco'),\r\n" + //
					"(268,16,-9.8045,-36.6191,'Arapiraca'),\r\n" + //
					"(269,16,-10.1284,-36.2863,'Coruripe'),\r\n" + //
					"(270,16,-9.5511,-35.7701,'Maceio'),\r\n" + //
					"(271,16,-9.4203,-36.6203,'Palmeiras dos Indios'),\r\n" + //
					"(272,16,-9.7492,-37.4307,'Pao de Acucar'),\r\n" + //
					"(273,16,-9.6222,-37.7671,'Piranhas'),\r\n" + //
					"(274,16,-9.2874,-35.5658,'Sao Luis do Quitunde'),\r\n" + //
					"(275,17,-7.2054,-59.8885,'Apui'),\r\n" + //
					"(276,17,-3.5833,-59.1294,'Autazes'),\r\n" + //
					"(277,17,-0.9872,-62.9242,'Barcelos'),\r\n" + //
					"(278,17,-8.7768,-67.3325,'Boca do Acre'),\r\n" + //
					"(279,17,-4.0974,-63.1453,'Coari'),\r\n" + //
					"(280,17,-6.6503,-69.8686,'Eirunepe'),\r\n" + //
					"(281,17,-7.5525,-63.0713,'Humaita'),\r\n" + //
					"(282,17,-3.1333,-58.4827,'Itacoatiara'),\r\n" + //
					"(283,17,-7.2606,-64.7885,'Labrea'),\r\n" + //
					"(284,17,-3.2946,-60.6284,'Manacapuru'),\r\n" + //
					"(285,17,-3.1036,-60.0154,'Manaus'),\r\n" + //
					"(286,17,-5.7885,-61.2882,'Manicore'),\r\n" + //
					"(287,17,-3.399,-57.6737,'Maues'),\r\n" + //
					"(288,17,-5.1411,-60.3805,'Novo Aripuana'),\r\n" + //
					"(289,17,-2.6391,-56.7561,'Parintins'),\r\n" + //
					"(290,17,-2.0565,-60.0257,'Presidente Figueiredo'),\r\n" + //
					"(291,17,-2.6336,-59.6005,'Rio Urubu'),\r\n" + //
					"(292,17,-0.1252,-67.0612,'Sao gabriel da Cachoeira'),\r\n" + //
					"(293,17,-2.5347,-57.758,'Urucara'),\r\n" + //
					"(294,18,-0.5678,-50.8235,'Itaubal'),\r\n" + //
					"(295,18,-0.035,-51.0887,'Macapa'),\r\n" + //
					"(296,18,-3.8135,-51.8625,'Oiapoque'),\r\n" + //
					"(297,18,-0.6943,-51.404,'Porto Grande'),\r\n" + //
					"(298,19,-17.963,-38.7032,'Abrolhos'),\r\n" + //
					"(299,19,-13.0094,-39.6168,'Amargosa'),\r\n" + //
					"(300,19,-11.0848,-43.1389,'Barra'),\r\n" + //
					"(301,19,-12.1247,-45.027,'Barreiras'),\r\n" + //
					"(302,19,-16.088,-39.2153,'Belmonte'),\r\n" + //
					"(303,19,-13.251,-43.4053,'Bom Jesus da Lapa'),\r\n" + //
					"(304,19,-14.1818,-41.6722,'Brumado'),\r\n" + //
					"(305,19,-10.7229,-43.6512,'Buritirama'),\r\n" + //
					"(306,19,-17.7394,-39.2586,'Caravelas'),\r\n" + //
					"(307,19,-12.0358,-37.6838,'Conde'),\r\n" + //
					"(308,19,-13.2333,-44.6173,'Correntina'),\r\n" + //
					"(309,19,-12.6754,-39.0895,'Cruz das Almas'),\r\n" + //
					"(310,19,-9.0013,-39.9134,'Curaca'),\r\n" + //
					"(311,19,-10.4549,-41.2069,'Delfino'),\r\n" + //
					"(312,19,-10.5372,-38.9966,'Euclides da Cunha'),\r\n" + //
					"(313,19,-12.1962,-38.9673,'Feira de Santana'),\r\n" + //
					"(314,19,-11.0521,-45.2007,'Formosa do Rio Preto'),\r\n" + //
					"(315,19,-14.2081,-42.7496,'Guanambi'),\r\n" + //
					"(316,19,-12.193,-43.2134,'Ibotirama'),\r\n" + //
					"(317,19,-14.6588,-39.1814,'Ilheus'),\r\n" + //
					"(318,19,-14.1713,-39.6925,'Ipiau'),\r\n" + //
					"(319,19,-11.3289,-41.8645,'Irece'),\r\n" + //
					"(320,19,-12.524,-40.2997,'Itaberaba'),\r\n" + //
					"(321,19,-17.4366,-39.5508,'Itamaraju'),\r\n" + //
					"(322,19,-15.2446,-40.2295,'Itapetinga'),\r\n" + //
					"(323,19,-13.5278,-40.1197,'Itirucu'),\r\n" + //
					"(324,19,-11.2051,-40.4649,'Jacobina'),\r\n" + //
					"(325,19,-10.0807,-38.3459,'Jeremoabo'),\r\n" + //
					"(326,19,-12.5578,-41.3888,'Lencois'),\r\n" + //
					"(327,19,-12.1523,-45.8297,'Luis Eduardo Magalhaes'),\r\n" + //
					"(328,19,-12.1316,-40.3542,'Macajuba'),\r\n" + //
					"(329,19,-13.9069,-38.9722,'Marau'),\r\n" + //
					"(330,19,-13.1556,-41.7741,'Piata'),\r\n" + //
					"(331,19,-10.0183,-42.5002,'Pilao Arcado'),\r\n" + //
					"(332,19,-16.3889,-39.1823,'Porto Seguro'),\r\n" + //
					"(333,19,-10.9846,-39.617,'Queimadas'),\r\n" + //
					"(334,19,-9.6256,-42.0772,'Remanso'),\r\n" + //
					"(335,19,-11.0587,-38.444,'Ribeira do Amparo'),\r\n" + //
					"(336,19,-13.0055,-38.5057,'Salvador'),\r\n" + //
					"(337,19,-12.8082,-38.4959,'Salvador-Est. Radio Marinha'),\r\n" + //
					"(338,19,-11.0027,-44.5249,'Santa Rita de Cassia'),\r\n" + //
					"(339,19,-10.443,-40.1482,'Senhor do Bonfim'),\r\n" + //
					"(340,19,-11.6645,-39.0229,'Serrinha'),\r\n" + //
					"(341,19,-9.8336,-39.4956,'Uaua'),\r\n" + //
					"(342,19,-15.2802,-39.0912,'Uma'),\r\n" + //
					"(343,19,-13.3436,-39.1266,'Valenca'),\r\n" + //
					"(344,19,-14.8864,-40.8013,'Vitoria da Conquista'),\r\n" + //
					"(345,20,-20.1041,-41.1068,'Afonso Claudio'),\r\n" + //
					"(346,20,-20.7504,-41.4888,'Alegre'),\r\n" + //
					"(347,20,-20.6365,-40.7418,'Alfredo Chaves'),\r\n" + //
					"(348,20,-18.2915,-40.7364,'Ecoporanga'),\r\n" + //
					"(349,20,-19.3569,-40.0686,'Linhares'),\r\n" + //
					"(350,20,-19.4071,-40.5398,'Marilandia'),\r\n" + //
					"(351,20,-18.6952,-40.3905,'Nova Venecia'),\r\n" + //
					"(352,20,-21.1008,-41.0393,'Presidente Kennedy'),\r\n" + //
					"(353,20,-19.9883,-40.5795,'Santa Teresa'),\r\n" + //
					"(354,20,-18.6761,-39.864,'Sao Mateus'),\r\n" + //
					"(355,20,-20.3854,-41.1899,'Venda Nova do Imigrante'),\r\n" + //
					"(356,20,-20.467,-40.404,'Vila velha'),\r\n" + //
					"(357,20,-20.271,-40.306,'Vitoria-Goiabeiras'),\r\n" + //
					"(358,21,-15.7515,-41.4577,'Aguas Vermelhas'),\r\n" + //
					"(359,21,-19.5327,-41.0908,'Aimores'),\r\n" + //
					"(360,21,-16.1667,-40.6877,'Almenara'),\r\n" + //
					"(361,21,-16.8488,-42.0353,'Aracuai'),\r\n" + //
					"(362,21,-19.6056,-46.9496,'Araxa'),\r\n" + //
					"(363,21,-20.0311,-46.0088,'Bambui'),\r\n" + //
					"(364,21,-21.2283,-43.7677,'Barbacema'),\r\n" + //
					"(365,21,-19.98,-43.9586,'Belo horizonte-Cercadinho'),\r\n" + //
					"(366,21,-19.8839,-43.9693,'Belo Horizonte-Pampulha'),\r\n" + //
					"(367,21,-15.5242,-46.4355,'Buritis'),\r\n" + //
					"(368,21,-21.918,-46.3829,'Caldas'),\r\n" + //
					"(369,21,-19.5392,-49.5181,'Campina Verde'),\r\n" + //
					"(370,21,-17.7055,-42.3892,'Capelinha'),\r\n" + //
					"(371,21,-19.7357,-42.1371,'Caratinga'),\r\n" + //
					"(372,21,-15.3001,-45.6174,'Chapada Gaucha'),\r\n" + //
					"(373,21,-19.9858,-48.1515,'Conceicao das Alagoas'),\r\n" + //
					"(374,21,-21.5467,-43.261,'Coronel Pacheco'),\r\n" + //
					"(375,21,-18.7477,-44.4537,'Curvelo'),\r\n" + //
					"(376,21,-18.231,-43.6482,'Diamantina'),\r\n" + //
					"(377,21,-20.1732,-44.8749,'Divinopolis'),\r\n" + //
					"(378,21,-19.4819,-45.5939,'Dores do Indaia'),\r\n" + //
					"(379,21,-19.8853,-44.4168,'Florestal'),\r\n" + //
					"(380,21,-20.4549,-45.4538,'Formiga'),\r\n" + //
					"(381,21,-18.8303,-41.977,'Governador Valadares'),\r\n" + //
					"(382,21,-18.7868,-42.9429,'Guanhaes'),\r\n" + //
					"(383,21,-17.5613,-47.1992,'Guarda Mor'),\r\n" + //
					"(384,21,-20.0314,-44.0112,'Ibirite'),\r\n" + //
					"(385,21,-16.5756,-41.4855,'Itaobim'),\r\n" + //
					"(386,21,-18.9529,-49.525,'Ituiutaba'),\r\n" + //
					"(387,21,-15.8028,-43.297,'Janauba'),\r\n" + //
					"(388,21,-15.448,-44.3663,'Januaria'),\r\n" + //
					"(389,21,-17.7847,-46.1193,'Joao Pinheiro'),\r\n" + //
					"(390,21,-21.7699,-43.3643,'Juiz de Fora'),\r\n" + //
					"(391,21,-21.6807,-45.9443,'Machado'),\r\n" + //
					"(392,21,-20.2633,-42.1828,'Manhuacu'),\r\n" + //
					"(393,21,-18.7806,-40.9865,'Mantena'),\r\n" + //
					"(394,21,-22.3145,-45.373,'Maria da Fe'),\r\n" + //
					"(395,21,-15.0859,-44.016,'Mocambinho'),\r\n" + //
					"(396,21,-14.4082,-44.4041,'Montalvania'),\r\n" + //
					"(397,21,-16.6863,-43.8437,'Montes Claros'),\r\n" + //
					"(398,21,-22.8616,-46.0433,'Monte Verde'),\r\n" + //
					"(399,21,-21.1048,-42.3759,'Muriae'),\r\n" + //
					"(400,21,-20.7149,-44.8645,'Oliveira'),\r\n" + //
					"(401,21,-20.5565,-43.7562,'Ouro Branco'),\r\n" + //
					"(402,21,-17.2443,-46.8816,'Paracatu'),\r\n" + //
					"(403,21,-22.3957,-44.9619,'Passa Quatro'),\r\n" + //
					"(404,21,-20.7452,-46.6339,'Passos'),\r\n" + //
					"(405,21,-18.5206,-46.4406,'Patos de Minas'),\r\n" + //
					"(406,21,-18.9966,-46.9859,'Patrocinio'),\r\n" + //
					"(407,21,-17.258,-44.8356,'Pirapora'),\r\n" + //
					"(408,21,-15.7231,-42.4357,'Rio Pardo de Minas'),\r\n" + //
					"(409,21,-19.8752,-47.4341,'Sacramento'),\r\n" + //
					"(410,21,-16.1603,-42.3102,'Salinas'),\r\n" + //
					"(411,21,-21.1065,-44.2509,'Sao Joao Del Rei'),\r\n" + //
					"(412,21,-16.3627,-45.1238,'Sao Romao'),\r\n" + //
					"(413,21,-20.9098,-47.1142,'Sao Sebastiao do Paraiso'),\r\n" + //
					"(414,21,-17.7987,-40.2499,'Serra dos Aimores'),\r\n" + //
					"(415,21,-19.4552,-44.1733,'Sete Lagoas'),\r\n" + //
					"(416,21,-17.8928,-41.5154,'Teofilo Otoni'),\r\n" + //
					"(417,21,-19.5738,-42.6224,'Timoteo'),\r\n" + //
					"(418,21,-18.2008,-45.4598,'Tres Marias'),\r\n" + //
					"(419,21,-19.71,-47.9618,'Uberaba'),\r\n" + //
					"(420,21,-18.917,-48.2556,'Uberlandia'),\r\n" + //
					"(421,21,-16.5541,-46.8819,'Unai'),\r\n" + //
					"(422,21,-21.5665,-45.4043,'Varginha'),\r\n" + //
					"(423,21,-20.7626,-42.864,'Vicosa'),\r\n" + //
					"(424,22,-20.4444,-52.8758,'Agua Clara'),\r\n" + //
					"(425,22,-23.0025,-55.3293,'Amambai'),\r\n" + //
					"(426,22,-22.148,-53.7637,'Angelica'),\r\n" + //
					"(427,22,-20.4754,-55.784,'Aquidauana'),\r\n" + //
					"(428,22,-22.955,-62.626,'Aral Moreira'),\r\n" + //
					"(429,22,-21.7521,-52.4712,'Bataguassu'),\r\n" + //
					"(430,22,-22.1015,-56.5407,'Bela Vista'),\r\n" + //
					"(431,22,-21.2982,-52.0689,'Brasilandia'),\r\n" + //
					"(432,22,-22.657,-54.8193,'Caarapo'),\r\n" + //
					"(433,22,-20.4471,-54.7226,'Campo Grande'),\r\n" + //
					"(434,22,-19.1224,-51.7207,'Cassilandia'),\r\n" + //
					"(435,22,-18.8021,-52.6026,'Chapadao do Sul'),\r\n" + //
					"(436,22,-18.9966,-57.6375,'Corumba'),\r\n" + //
					"(437,22,-18.4926,-53.1712,'Costa Rica'),\r\n" + //
					"(438,22,-18.5121,-54.7361,'Coxim'),\r\n" + //
					"(439,22,-22.1939,-54.9113,'Dourados'),\r\n" + //
					"(440,22,-23.6448,-54.5702,'Iguatemi'),\r\n" + //
					"(441,22,-22.0928,-54.7988,'Itapora'),\r\n" + //
					"(442,22,-23.4495,-54.1818,'Itaquirai'),\r\n" + //
					"(443,22,-22.3004,-53.8228,'Invinhema'),\r\n" + //
					"(444,22,-21.4785,-56.1377,'Jardim'),\r\n" + //
					"(445,22,-22.8572,-54.6056,'Juti'),\r\n" + //
					"(446,22,-22.5753,-55.1603,'Laguna Carapa'),\r\n" + //
					"(447,22,-21.609,-55.1775,'Maracaju'),\r\n" + //
					"(448,22,-20.3955,-56.4317,'Miranda'),\r\n" + //
					"(449,22,-18.9888,-56.6228,'Nhumirim'),\r\n" + //
					"(450,22,-21.4509,-54.3419,'Nova Alvorada do Sul'),\r\n" + //
					"(451,22,-19.6955,-51.1817,'Paranaiba'),\r\n" + //
					"(452,22,-18.0727,-54.5488,'Pedro Gomes'),\r\n" + //
					"(453,22,-22.5524,-55.7163,'Ponta Pora'),\r\n" + //
					"(454,22,-21.7058,-57.8865,'Porto Murtinho'),\r\n" + //
					"(455,22,-20.4669,-53.763,'Ribas do Rio Pardo'),\r\n" + //
					"(456,22,-21.7749,-54.5281,'Rio Brilhante'),\r\n" + //
					"(457,22,-21.3058,-52.8203,'Santa Rita do Rio Pardo'),\r\n" + //
					"(458,22,-19.4203,-54.553,'Sao Gabriel do Oeste'),\r\n" + //
					"(459,22,-20.3514,-51.4302,'Selviria'),\r\n" + //
					"(460,22,-23.9668,-55.0242,'Sete Quedas'),\r\n" + //
					"(461,22,-20.9816,-54.9718,'Sidrolandia'),\r\n" + //
					"(462,22,-17.6352,-54.7604,'Sonora'),\r\n" + //
					"(463,22,-20.79,-51.7122,'Tres Lagoas'),\r\n" + //
					"(464,23,-24.5708,-52.8002,'Campina da Lagoa'),\r\n" + //
					"(465,23,-24.7869,-49.9992,'Castro'),\r\n" + //
					"(466,23,-23.3591,-52.9319,'Cidade Gaucha'),\r\n" + //
					"(467,23,-26.4172,-52.3487,'Clevelandia'),\r\n" + //
					"(468,23,-25.3224,-49.1577,'Colombo'),\r\n" + //
					"(469,23,-25.4486,-49.2306,'Curitiba'),\r\n" + //
					"(470,23,-22.6393,-52.8901,'Diamante do Norte'),\r\n" + //
					"(471,23,-25.699,-53.0952,'Dois Vizinhos'),\r\n" + //
					"(472,23,-25.6018,-54.483,'Foz do Iguacu'),\r\n" + //
					"(473,23,-26.3984,-51.3536,'General Carneiro'),\r\n" + //
					"(474,23,-24.1889,-53.049,'Goioere'),\r\n" + //
					"(475,23,-23.3903,-53.6359,'Icaraima'),\r\n" + //
					"(476,23,-25.5678,-51.0779,'Inacio Martins'),\r\n" + //
					"(477,23,-25.0107,-50.8538,'Ivai'),\r\n" + //
					"(478,23,-23.7733,-50.1805,'Japira'),\r\n" + //
					"(479,23,-23.5052,-49.9463,'Joaquim Tavora'),\r\n" + //
					"(480,23,-25.3689,-52.3919,'Laranjeiras do Sul'),\r\n" + //
					"(481,23,-24.5333,-54.0192,'Marechal Candido Rondon'),\r\n" + //
					"(482,23,-23.4053,-51.9358,'Maringa'),\r\n" + //
					"(483,23,-25.5089,-48.8086,'Morretes'),\r\n" + //
					"(484,23,-23.4152,-50.5777,'Nova Fatima'),\r\n" + //
					"(485,23,-24.4373,-51.963,'Nova Tebas'),\r\n" + //
					"(486,23,-22.6582,-52.1345,'Paranapoema'),\r\n" + //
					"(487,23,-25.7218,-53.7479,'Planalto'),\r\n" + //
					"(488,23,-25.8356,-50.3689,'Sao Mateus do Sul'),\r\n" + //
					"(489,23,-24.2803,-50.2101,'Ventania'),\r\n" + //
					"(490,24,-22.9756,-44.3034,'Angra dos Reis'),\r\n" + //
					"(491,24,-22.9754,-42.0214,'Arraial do Cabo'),\r\n" + //
					"(492,24,-21.5877,-41.9583,'Cambuci'),\r\n" + //
					"(493,24,-21.7147,-41.344,'Campos dos Goytacazes'),\r\n" + //
					"(494,24,-22.0416,-41.0518,'Campos-Sao Tome'),\r\n" + //
					"(495,24,-21.9387,-42.6009,'Carmo'),\r\n" + //
					"(496,24,-22.5898,-43.2822,'Duque de Caxias-Xerem'),\r\n" + //
					"(497,24,-22.3739,-44.7031,'Itatiaia-Parque Nacional'),\r\n" + //
					"(498,24,-22.3763,-41.812,'Macae'),\r\n" + //
					"(499,24,-22.8673,-43.102,'Niteroi'),\r\n" + //
					"(500,24,-22.3348,-42.6769,'Nova Friburgo-Salinas'),\r\n" + //
					"(501,24,-23.2235,-44.7268,'Paraty'),\r\n" + //
					"(502,24,-22.4648,-43.2915,'Petropilis-Pico do Couto'),\r\n" + //
					"(503,24,-22.4509,-44.4447,'Resende'),\r\n" + //
					"(504,24,-22.6535,-44.0409,'Rio Claro-Passa Tres'),\r\n" + //
					"(505,24,-22.9882,-43.1904,'Rio de Janeiro-Forte de Copacabana'),\r\n" + //
					"(506,24,-22.9398,-43.4028,'Rio de Janeiro-Jacarepagua'),\r\n" + //
					"(507,24,-23.0503,-43.5956,'Rio de Janeiro-Marambaia'),\r\n" + //
					"(508,24,-22.8613,-43.4114,'Rio de Janeiro-Vila Militar'),\r\n" + //
					"(509,24,-21.9505,-42.0104,'Santa Maria Madalena'),\r\n" + //
					"(510,24,-22.8713,-42.6092,'Saquarema-Sampaio Correia'),\r\n" + //
					"(511,24,-22.7578,-43.6848,'Seropedica-Ecologia Agricola'),\r\n" + //
					"(512,24,-22.6459,-42.4157,'Silvia Jardim'),\r\n" + //
					"(513,24,-22.4489,-42.9871,'Teresopolis-Parque Nacional'),\r\n" + //
					"(514,24,-22.0983,-43.2085,'Tres Rios'),\r\n" + //
					"(515,25,-29.709,-55.5254,'Alegrete'),\r\n" + //
					"(516,25,-31.3478,-54.0132,'Bage'),\r\n" + //
					"(517,25,-29.1645,-51.5342,'Bento Goncalves'),\r\n" + //
					"(518,25,-30.5453,-53.467,'Cacapava do Sul'),\r\n" + //
					"(519,25,-30.8079,-51.8342,'Camaqua'),\r\n" + //
					"(520,25,-29.0491,-50.1496,'Cambara do Sul'),\r\n" + //
					"(521,25,-29.6742,-51.064,'Campo Bom'),\r\n" + //
					"(522,25,-29.3687,-50.8272,'Canela'),\r\n" + //
					"(523,25,-31.4032,-52.7006,'Cangucu'),\r\n" + //
					"(524,25,-31.8025,-52.4072,'Capao do Leao-Pelotas'),\r\n" + //
					"(525,25,-28.6034,-53.6735,'Cruz Alta'),\r\n" + //
					"(526,25,-31.0025,-54.6181,'Dom Pedrito'),\r\n" + //
					"(527,25,-30.5431,-52.5246,'Encruzilhado do Sul'),\r\n" + //
					"(528,25,-27.6577,-52.3058,'Erechim'),\r\n" + //
					"(529,25,-27.3956,-53.4294,'Fredrico Westphalen'),\r\n" + //
					"(530,25,-28.6534,-53.1118,'Ibiruba'),\r\n" + //
					"(531,25,-32.5348,-53.3758,'Jaguarao'),\r\n" + //
					"(532,25,-28.2223,-51.5128,'Lagoa Vermelha'),\r\n" + //
					"(533,25,-31.2482,-50.9062,'Mostardas'),\r\n" + //
					"(534,25,-27.9203,-53.318,'Palmeira das Missoes'),\r\n" + //
					"(535,25,-28.2268,-52.4035,'Passo Fundo'),\r\n" + //
					"(536,25,-30.0535,-51.1747,'Porto alegre'),\r\n" + //
					"(537,25,-30.3685,-56.4371,'Quarai'),\r\n" + //
					"(538,25,-32.0787,-52.1677,'Rio Grande'),\r\n" + //
					"(539,25,-29.8721,-52.3819,'Rio Pardo'),\r\n" + //
					"(540,25,-29.7249,-53.7204,'Santa Maria'),\r\n" + //
					"(541,25,-30.8424,-55.613,'Santana do Livramento'),\r\n" + //
					"(542,25,-27.8904,-54.48,'Santa Rosa'),\r\n" + //
					"(543,25,-33.7422,-53.3722,'Santa Vitoria do Palmar'),\r\n" + //
					"(544,25,-29.1915,-54.8856,'Santiago'),\r\n" + //
					"(545,25,-27.8543,-53.7911,'Santo Augusto'),\r\n" + //
					"(546,25,-28.65,-56.0162,'Sao Borja'),\r\n" + //
					"(547,25,-30.3414,-54.3109,'Sao Gabriel'),\r\n" + //
					"(548,25,-28.7486,-50.0578,'Sao Jose dos Ausentes'),\r\n" + //
					"(549,25,-28.4171,-54.9624,'Sao Luiz Gonzaga'),\r\n" + //
					"(550,25,-29.7021,-54.6943,'Sao Vicente do Sul'),\r\n" + //
					"(551,25,-28.7048,-51.8707,'Serafina Correa'),\r\n" + //
					"(552,25,-28.8592,-52.5423,'Soledade'),\r\n" + //
					"(553,25,-29.4503,-51.8242,'Teutonia'),\r\n" + //
					"(554,25,-29.3503,-49.7332,'Torres'),\r\n" + //
					"(555,25,-30.0102,-50.1358,'Tramandai'),\r\n" + //
					"(556,25,-29.0893,-53.8266,'Tupancireta'),\r\n" + //
					"(557,25,-29.8398,-57.0818,'Uruguaiana'),\r\n" + //
					"(558,25,-28.5136,-50.8827,'Vacaria'),\r\n" + //
					"(559,26,-28.9313,-49.4979,'Aranrangua'),\r\n" + //
					"(560,26,-26.8191,-50.9855,'Cacador'),\r\n" + //
					"(561,26,-27.3838,-51.216,'Campos Novos'),\r\n" + //
					"(562,26,-27.0853,-52.6357,'Chapeco'),\r\n" + //
					"(563,26,-27.2886,-50.6042,'Curitibanos'),\r\n" + //
					"(564,26,-26.2865,-53.6331,'Dionisio Cerqueira'),\r\n" + //
					"(565,26,-27.6025,-48.62,'Florianopolis-Sao Jose'),\r\n" + //
					"(566,26,-26.9137,-49.2679,'Indaial'),\r\n" + //
					"(567,26,-26.9509,-48.762,'Itajai'),\r\n" + //
					"(568,26,-26.0813,-48.6417,'Itapoa'),\r\n" + //
					"(569,26,-27.4184,-49.6468,'Ituporanga'),\r\n" + //
					"(570,26,-27.1692,-51.5589,'Joacaba'),\r\n" + //
					"(571,26,-27.8022,-50.3354,'Lajes'),\r\n" + //
					"(572,26,-28.6044,-48.8133,'Laguna-Farol Santa Marta'),\r\n" + //
					"(573,26,-26.3936,-50.3632,'Major Vieira'),\r\n" + //
					"(574,26,-28.1269,-49.4796,'Morro da Igreja-Bom Jardim da Serra'),\r\n" + //
					"(575,26,-26.4064,-52.8503,'Novo Horizonte'),\r\n" + //
					"(576,26,-27.6785,-49.042,'Rancho Queimado'),\r\n" + //
					"(577,26,-26.9375,-50.1454,'Rio do Campo'),\r\n" + //
					"(578,26,-26.2484,-49.5806,'Rio Negrinho'),\r\n" + //
					"(579,26,-28.2756,-49.9346,'Sao Joaquim'),\r\n" + //
					"(580,26,-26.7766,-53.5045,'Sao Miguel do Oeste'),\r\n" + //
					"(581,26,-28.5325,-49.3152,'Urussanga'),\r\n" + //
					"(582,26,-26.9386,-52.398,'Xanxare');"); //

			// initializing population
			super.init(queries);
		}

		return this;
	}

	@Override
	protected InmetCityEntily getEntity(ResultSet queryResult) throws PersistenceException {

		Long id = null;
		try {
			// retrieving the attributes
			id = queryResult.getObject("id") != null ? queryResult.getLong("id") : null;

			// Recovering relationship
			Long idState = queryResult.getLong("id_state");
			InmetStateEntily entity = this.getStationRelationship(idState);

			// creating new entity with attributes retrieved from database
			return new InmetCityEntily(queryResult.getLong("id"), queryResult.getDouble("latitude"),
					queryResult.getDouble("longitude"), queryResult.getString("name"), entity);

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
	private InmetCityDataDAO saveStationRelationship(InmetStateEntily entity) throws PersistenceException {
		if (entity != null) {
			InmetStateDataDAO.getInstanceOf().save(entity);
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
	private InmetCityDataDAO removeStationRelationship(Long entityId) {
		try {
			InmetStateDataDAO.getInstanceOf().remove(entityId);
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
	private InmetStateEntily getStationRelationship(Long entityId) throws PersistenceException {
		if (entityId != null) {
			return InmetStateDataDAO.getInstanceOf().find(entityId);
		}
		return null;
	}

}
