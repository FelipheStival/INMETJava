package br.embrapa.cnpaf.inmetdata.enumerate;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * <br>
 * <p>
 * <b>Enum with possible status of execution of the system actions.<br>
 * This file contains the successes, alerts and errors processed by the system.</b>
 * </p>
 * <p>
 * The enum items follow the convention of all uppercase letters and the words separated by underline. <br>
 * They should have the following format: NAME_CLASS_SEVERITY_ITEM_DESCRIPTION.<br>
 * <br>
 * The possible severities are:<br>
 * <ul>
 * <li>INFO - Successful information about executing an action.</li>
 * <li>WARN - Alerts on the occurrence of some non-impeding error for the execution of an action.</li>
 * <li>ERROR - Impedance error for execution of an action, but that does not imply the operation of the system as a whole.</li>
 * <li>FATAL - Error that prevents the application from continuing its execution.</li>
 * </ul>
 * </p>
 * 
 * @author @author Rubens de Castro Pereira, Sergio Lopes Jr and Feliphe Stival Valadares Guiliane.
 * @version 0.1
 * @since 03/03/2020 (creation date)
 * 
 */
public enum MessageEnum implements Serializable {

	// -----------------------------------------------------------------------------
	// CONVERTER (10000 - 19999)
	// -----------------------------------------------------------------------------
	//

	// -----------------------------------------------------------------------------
	// DAO (20000 - 29999)
	// -----------------------------------------------------------------------------
	//

	// -------------------------- GenericDAO -------------------------------
	@SerializedName("20000")
	GENERIC_DAO_INFO_SUCCESS_CREATE(20000),

	@SerializedName("20001")
	GENERIC_DAO_INFO_SUCCESS_INIT(20001),

	@SerializedName("20002")
	GENERIC_DAO_INFO_SUCCESS_SAVE(20002),

	@SerializedName("20003")
	GENERIC_DAO_INFO_SUCCESS_REMOVE(20003),

	@SerializedName("20004")
	GENERIC_DAO_INFO_SUCCESS_FIND(20004),

	@SerializedName("20005")
	GENERIC_DAO_INFO_SUCCESS_LIST_ALL(20005),

	@SerializedName("20006")
	GENERIC_DAO_INFO_SUCCESS_GET_ENTITY(20006),

	@SerializedName("20007")
	GENERIC_DAO_ERROR_CREATE(20007),

	@SerializedName("20008")
	GENERIC_DAO_ERROR_INIT(20008),

	@SerializedName("20009")
	GENERIC_DAO_ERROR_SAVE(20009),

	@SerializedName("20010")
	GENERIC_DAO_ERROR_SAVE_INCONSISTENTLY(20010),

	@SerializedName("20011")
	GENERIC_DAO_ERROR_SAVE_RELATIONSHIP_INCONSISTENTLY(20011),

	@SerializedName("20012")
	GENERIC_DAO_ERROR_SAVE_WITHOUT_ID(20012),

	@SerializedName("20013")
	GENERIC_DAO_ERROR_REMOVE(20013),

	@SerializedName("20014")
	GENERIC_DAO_ERROR_REMOVE_INCONSISTENTLY(20014),

	@SerializedName("20015")
	GENERIC_DAO_ERROR_FIND(20015),

	@SerializedName("20016")
	GENERIC_DAO_ERROR_LIST_ALL(20016),

	@SerializedName("20017")
	GENERIC_DAO_ERROR_GET_ENTITY(20017),

	@SerializedName("20018")
	GENERIC_DAO_ERROR_DATABASE_CONNECTION(20018),

	@SerializedName("20019")
	GENERIC_DAO_INFO_SUCCESS_SAVE_RELATIONSHIP(20019),

	@SerializedName("20020")
	GENERIC_DAO_ERROR_SAVE_RELATIONSHIP(20020),

	@SerializedName("20021")
	GENERIC_DAO_INFO_SUCCESS_REMOVE_RELATIONSHIP(20021),

	@SerializedName("20022")
	GENERIC_DAO_ERROR_REMOVE_RELATIONSHIP(20022),

	@SerializedName("20023")
	GENERIC_DAO_ERROR_DUPLICATE_KEY(20023),

	@SerializedName("20024")
	GENERIC_DAO_INFO_SUCCESS_GET_ENTITY_ID(20024),

	@SerializedName("20025")
	GENERIC_DAO_ERROR_GET_ENTITY_ID(20025),

	@SerializedName("20026")
	GENERIC_DAO_ERROR_SAVE_ATTRIBUTE_INCONSISTENTLY(20026),

	@SerializedName("20027")
	GENERIC_DAO_ERROR_SAVE_ATTRIBUTE(20027),

	@SerializedName("20028")
	GENERIC_DAO_INFO_SUCCESS_SAVE_ATTRIBUTE(20028),
	
	// --------------------------- InmetHourlyDataEntity ---------------------------
	@SerializedName("30000")
	INMET_HOURLY_DATA_ENTITY_ERROR_JSON_PARSING(30000),

	@SerializedName("30001")
	INMET_HOURLY_DATA_ERROR_INVALID_STATION(30001),

	// -----------------------------------------------------------------------------
	// ENUMERATE (40000 - 49999)
	// -----------------------------------------------------------------------------
	//

	// -----------------------------------------------------------------------------
	// EXCEPTION (50000 - 59999)
	// -----------------------------------------------------------------------------
	//

	// -----------------------------------------------------------------------------
	// MAIN (60000 - 69999)
	// -----------------------------------------------------------------------------
	//

	// -------------------------------- INMETData ----------------------------------
	@SerializedName("60000")
	INMETDATA_INFO_SUCCESS_CREATE(60000),

	// -----------------------------------------------------------------------------
	// SERVICES (70000 - 79999)
	// -----------------------------------------------------------------------------
	//

	// ------------------------------- MessageService ------------------------------
	@SerializedName("70000")
	MESSAGE_SERVICE_ERROR_NOT_POSSIBLE_RETRIEVE_MESSAGE_FILE(70000),

	// ------------------------------- LogService ----------------------------------
	@SerializedName("70100")
	LOG_SERVICE_INFO_SUCCESS_CREATING_SERVICE(70100),

	@SerializedName("70101")
	LOG_SERVICE_ERROR_INVALID_MESSAGING_SERVICE(70101),

	@SerializedName("70102")
	LOG_SERVICE_ERROR_UNABLE_RETRIEVE_LOG_SERVICE_LIST(70102),

	@SerializedName("70103")
	LOG_SERVICE_ERROR_LOG_FILE_NOT_FOUND(70103),

	@SerializedName("70104")
	LOG_SERVICE_ERROR_UNABLE_RECOVER_LOG_INFORMATION(70104),

	@SerializedName("70105")
	LOG_SERVICE_ERROR_UNABLE_UPDATE_APPENDER_FILE(70105),

	@SerializedName("70106")
	LOG_SERVICE_ERROR_IN_CONVERSION_DATA_VALUES(70106),

	// -------------------------- GenericService -------------------------------
	@SerializedName("70200")
	GENERIC_SERVICE_INFO_SUCCESS_CREATE(70200),

	@SerializedName("70201")
	GENERIC_SERVICE_ERROR_CREATE(70201),

	// ------------------------- InmetService ------------------------------
	@SerializedName("70300")
	INMET_SERVICE_INFO_SUCCESS_CREATING_SERVICE(70300),

	@SerializedName("70301")
	INMET_SERVICE_INFO_SUCCESS_GET_HOURLY_DATA(70301),

	@SerializedName("70302")
	INMET_SERVICE_ERROR_GET_HOURLY_DATA(70302),
	
	@SerializedName("70303")
	INMET_SERVICE_ERROR_EMPTY_HOURLY_DATA(70303),

	// -----------------------------------------------------------------------------
	// UTIL (80000 - 89999)
	// -----------------------------------------------------------------------------
	//

	;

	private int value;

	/**
	 * Private class constructor.
	 * 
	 * @param value
	 *            Integer value associated with the enum constant desired.
	 */
	private MessageEnum(int value) {
		this.value = value;
	}

	/**
	 * Retrieves the integer value associated with the enum constant.
	 * 
	 * @return Integer value associated with the enum constant.
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * Retrieves the enum constant of this class from the integer value provided. If there is no match to any constant, null is returned.
	 * 
	 * @param value
	 *            Integer value associated with the enum constant desired.
	 * @return Enum constant associated with the integer value provided.
	 */
	public static MessageEnum valueOf(int value) {
		switch (value) {

			// -----------------------------------------------------------------------------
			// CONVERTER (10000 - 19999)
			// -----------------------------------------------------------------------------
			//

			// -----------------------------------------------------------------------------
			// DAO (20000 - 29999)
			// -----------------------------------------------------------------------------

			// -------------------------- GenericDAO -------------------------------
			case 20000:
				return MessageEnum.GENERIC_DAO_INFO_SUCCESS_CREATE;

			case 20001:
				return MessageEnum.GENERIC_DAO_INFO_SUCCESS_INIT;

			case 20002:
				return MessageEnum.GENERIC_DAO_INFO_SUCCESS_SAVE;

			case 20003:
				return MessageEnum.GENERIC_DAO_INFO_SUCCESS_REMOVE;

			case 20004:
				return MessageEnum.GENERIC_DAO_INFO_SUCCESS_FIND;

			case 20005:
				return MessageEnum.GENERIC_DAO_INFO_SUCCESS_LIST_ALL;

			case 20006:
				return MessageEnum.GENERIC_DAO_INFO_SUCCESS_GET_ENTITY;

			case 20007:
				return MessageEnum.GENERIC_DAO_ERROR_CREATE;

			case 20008:
				return MessageEnum.GENERIC_DAO_ERROR_INIT;

			case 20009:
				return MessageEnum.GENERIC_DAO_ERROR_SAVE;

			case 20010:
				return MessageEnum.GENERIC_DAO_ERROR_SAVE_INCONSISTENTLY;

			case 20011:
				return MessageEnum.GENERIC_DAO_ERROR_SAVE_RELATIONSHIP_INCONSISTENTLY;

			case 20012:
				return MessageEnum.GENERIC_DAO_ERROR_SAVE_WITHOUT_ID;

			case 20013:
				return MessageEnum.GENERIC_DAO_ERROR_REMOVE;

			case 20014:
				return MessageEnum.GENERIC_DAO_ERROR_REMOVE_INCONSISTENTLY;

			case 20015:
				return MessageEnum.GENERIC_DAO_ERROR_FIND;

			case 20016:
				return MessageEnum.GENERIC_DAO_ERROR_LIST_ALL;

			case 20017:
				return MessageEnum.GENERIC_DAO_ERROR_GET_ENTITY;

			case 20018:
				return MessageEnum.GENERIC_DAO_ERROR_DATABASE_CONNECTION;

			case 20019:
				return MessageEnum.GENERIC_DAO_INFO_SUCCESS_SAVE_RELATIONSHIP;

			case 20020:
				return MessageEnum.GENERIC_DAO_ERROR_SAVE_RELATIONSHIP;

			case 20021:
				return MessageEnum.GENERIC_DAO_INFO_SUCCESS_REMOVE_RELATIONSHIP;

			case 20022:
				return MessageEnum.GENERIC_DAO_ERROR_REMOVE_RELATIONSHIP;

			case 20023:
				return MessageEnum.GENERIC_DAO_ERROR_DUPLICATE_KEY;

			case 20024:
				return MessageEnum.GENERIC_DAO_INFO_SUCCESS_GET_ENTITY_ID;

			case 20025:
				return MessageEnum.GENERIC_DAO_ERROR_GET_ENTITY_ID;

			case 20026:
				return MessageEnum.GENERIC_DAO_ERROR_SAVE_ATTRIBUTE_INCONSISTENTLY;

			case 20027:
				return MessageEnum.GENERIC_DAO_ERROR_SAVE_ATTRIBUTE;

			case 20028:
				return MessageEnum.GENERIC_DAO_INFO_SUCCESS_SAVE_ATTRIBUTE;

			// -------------------------- InmetHourlyDataDAO -------------------------------

			// -----------------------------------------------------------------------------
			// ENTITY (30000 - 39999)
			// -----------------------------------------------------------------------------
			//
			// ------------------------------- ModuleEntity --------------------------------
			case 30000:
				return MessageEnum.INMET_HOURLY_DATA_ENTITY_ERROR_JSON_PARSING;

			case 30001:
				return MessageEnum.INMET_HOURLY_DATA_ERROR_INVALID_STATION;

			// -----------------------------------------------------------------------------
			// ENUMERATE (40000 - 49999)
			// -----------------------------------------------------------------------------
			//

			// -----------------------------------------------------------------------------
			// EXCEPTION (50000 - 59999)
			// -----------------------------------------------------------------------------
			//

			// -----------------------------------------------------------------------------
			// MAIN (60000 - 69999)
			// -----------------------------------------------------------------------------
			//

			// --------------------------------- InmetData ---------------------------------
			case 60000:
				return MessageEnum.INMETDATA_INFO_SUCCESS_CREATE;

			// -----------------------------------------------------------------------------
			// SERVICES (70000 - 79999)
			// -----------------------------------------------------------------------------
			//

			// ------------------------------- MessageService ------------------------------
			case 70000:
				return MessageEnum.MESSAGE_SERVICE_ERROR_NOT_POSSIBLE_RETRIEVE_MESSAGE_FILE;

			// ------------------------------- LogService ----------------------------------
			case 70100:
				return MessageEnum.LOG_SERVICE_INFO_SUCCESS_CREATING_SERVICE;

			case 70101:
				return MessageEnum.LOG_SERVICE_ERROR_INVALID_MESSAGING_SERVICE;

			case 70102:
				return MessageEnum.LOG_SERVICE_ERROR_UNABLE_RETRIEVE_LOG_SERVICE_LIST;

			case 70103:
				return MessageEnum.LOG_SERVICE_ERROR_LOG_FILE_NOT_FOUND;

			case 70104:
				return MessageEnum.LOG_SERVICE_ERROR_UNABLE_RECOVER_LOG_INFORMATION;

			case 70105:
				return MessageEnum.LOG_SERVICE_ERROR_UNABLE_UPDATE_APPENDER_FILE;

			case 70106:
				return MessageEnum.LOG_SERVICE_ERROR_IN_CONVERSION_DATA_VALUES;

			// -------------------------- GenericService -------------------------------
			case 70200:
				return MessageEnum.GENERIC_SERVICE_INFO_SUCCESS_CREATE;

			case 70201:
				return MessageEnum.GENERIC_SERVICE_ERROR_CREATE;

			// ---------------------------- InmetService -------------------------------
			case 70300:
				return MessageEnum.INMET_SERVICE_INFO_SUCCESS_CREATING_SERVICE;

			case 70301:
				return MessageEnum.INMET_SERVICE_INFO_SUCCESS_GET_HOURLY_DATA;

			case 70302:
				return MessageEnum.INMET_SERVICE_ERROR_GET_HOURLY_DATA;

			case 70303:
				return MessageEnum.INMET_SERVICE_ERROR_EMPTY_HOURLY_DATA;
				

			// -----------------------------------------------------------------------------
			// UTIL (80000 - 89999)
			// -----------------------------------------------------------------------------
			//
			

			// -----------------------------------------------------------------------------
			// UNKNOWN
			// -----------------------------------------------------------------------------
			//
			default:
				return null;
		}
	}

	/**
	 * Checking whether the enum item matches a success situation.
	 * 
	 * @return True, if it is a situation of error, false otherwise.
	 */
	public boolean isSuccess() {
		return this.toString().indexOf("_SUCCESS_") >= 0;
	}

	/**
	 * Checking whether the error severity associated with the enum item is "INFO".
	 * 
	 * @return True, if the error server is "INFO", false otherwise.
	 */
	public boolean isInfo() {
		return this.toString().indexOf("_INFO_") >= 0;
	}

	/**
	 * Checking whether the error severity associated with the enum item is "WARNING".
	 * 
	 * @return True, if the error server is "WARNING", false otherwise.
	 */
	public boolean isWarning() {
		return this.toString().indexOf("_WARN_") >= 0;
	}

	/**
	 * Checking whether the error severity associated with the enum item is "ERROR".
	 * 
	 * @return True, if the error server is "ERROR", false otherwise.
	 */
	public boolean isError() {
		return this.toString().indexOf("_ERROR_") >= 0;
	}

	/**
	 * Checking whether the error severity associated with the enum item is "FATAL".
	 * 
	 * @return True, if the error server is "FATAL", false otherwise.
	 */
	public boolean isFatal() {
		return this.toString().indexOf("_FATAL_") >= 0;
	}

}