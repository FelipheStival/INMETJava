package br.embrapa.cnpaf.inmetdata.service;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Locale;

import org.apache.log4j.Level;

import br.embrapa.cnpaf.inmetdata.enumerate.MessageEnum;
import br.embrapa.cnpaf.inmetdata.exception.ServiceException;
import br.embrapa.cnpaf.inmetdata.util.NetworkUtil;

/**
 * <br>
 * <p>
 * <b>Singleton class responsible for system settings.</b>
 * </p>
 * <p>
 * </p>
 * <p>
 * To retrieve an instance of this class use the static method getInstanceOf ():<br>
 * <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; <tt> ConfigurationService configurationService = ConfigurationService.getInstanceOf();</tt>
 * </p>
 * <br>
 * 
 * @author Sergio Lopes Jr.
 * @version 0.1
 * @since 03/03/2020 (creation date)
 * 
 */
public class ConfigurationService {

	public static final Locale LANGUAGE_DEFAULT = new Locale("pt", "BR");
	public static final Level LOG_LEVEL_DEFAULT = Level.ALL; // Level.ERROR;
	public static final ZoneId TIME_ZONE_DEFAULT = ZoneOffset.UTC;

	private static ConfigurationService instance;

	/**
	 * Private class constructor.
	 * 
	 * @param logClientName
	 *            Name of the client object of the logging service.
	 * @param logLevel
	 *            Log level to be used in log service.
	 * @throws ServiceException
	 *             Occurrence of any problems in creating of the service.
	 */
	private ConfigurationService(String logClientName, Level logLevel) throws ServiceException {
		super();
	}

	/**
	 * Method to retrieve the instance of service. This class has a single instance for any application (Singleton).
	 * 
	 * @param logClientName
	 *            Name of the client object of the logging service.
	 * @param logLevel
	 *            Log level to be used in log service.
	 * @return Returns the instance of service.
	 * @throws ServiceException
	 *             Occurrence of any problems in creating of the service.
	 */
	public static synchronized ConfigurationService getInstanceOf(String logClientName, Level logLevel) throws ServiceException {
		if (ConfigurationService.instance == null) {
			ConfigurationService.instance = new ConfigurationService(logClientName, logLevel);
		}
		return ConfigurationService.instance;
	}

	/**
	 * Method to retrieve the instance of service. This class has a single instance for any application (Singleton).
	 * 
	 * @return Returns the instance of service.
	 * @throws ServiceException
	 *             Occurrence of any problems in creating of the service.
	 */
	public static synchronized ConfigurationService getInstanceOf() throws ServiceException {
		return ConfigurationService.getInstanceOf(ConfigurationService.class.getSimpleName(), null);
	}

	/**
	 * Retrieves the operating language of the system.<br>
	 * All messages registered by the system, use this parameter to select the current language, in order to support internationalization.<br>
	 * The language supported so far is the Brazilian Portuguese (pt_BR).
	 * 
	 * @return The operating language of the system.
	 */
	public Locale getLanguage() {
		return LANGUAGE_DEFAULT;
	}

	/**
	 * Retrieves the log level to be used in all system services.<br>
	 * The system log level may contain the following levels, from least restrictive to most restrictive: <br>
	 * <tt>ALL -> DEBUG -> INFO -> WARNING -> ERROR -> FATAL.</tt>
	 * 
	 * @return The log level to be used in all system services.
	 */
	public Level getLogLevel() {
		return LOG_LEVEL_DEFAULT;
	};

	/**
	 * Retrieves the time zone to be used in system.<br>
	 * The defaul time zone is UTC.</tt>
	 * 
	 * @return The time zone used in system.
	 */
	public ZoneId getTimeZone() {
		return TIME_ZONE_DEFAULT;
	};

}
