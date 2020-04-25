package br.embrapa.cnpaf.inmetdata.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.spi.LoggingEvent;

import br.embrapa.cnpaf.inmetdata.enumerate.MessageEnum;
import br.embrapa.cnpaf.inmetdata.exception.GenericException;
import br.embrapa.cnpaf.inmetdata.exception.ServiceException;
import br.embrapa.cnpaf.inmetdata.util.ErrorUtil;
import br.embrapa.cnpaf.inmetdata.util.NetworkUtil;

/**
 * <br>
 * <p>
 * <b>Class responsible for writing and retrieve messages in the log file.</b>
 * </p>
 * <p>
 * The log is stored in a text file (asc ii) whose location and name are configured on the system configuration file. By default it is in the log folder with
 * the name inmetdata.log.
 * </p>
 * <p>
 * The system log level may contain the following levels, from least restrictive to most restrictive: <br>
 * <br>
 * <tt>ALL -> DEBUG -> INFO -> WARNING -> ERROR -> FATAL.</tt><br>
 * <br>
 * If prompted to write a message from a lower level (less restrictive) than the level set for the system log, it will simply be discarded. For example, say
 * that the level set for the system is "ERROR", then only messages with the level "ERROR" and "FATAL" will be written in the log.
 * </p>
 * <br>
 * <p>
 * <i>The system log is implemented using the framework Log4J.</i>
 * </p>
 * 
 * @author Sergio Lopes Jr. and Rubens de Castro Pereira.
 * @version 0.2
 * @since 03/03/2020 (creation date)
 * 
 */
public class LogService {

	public static final String DEFAULT_LOG_CLIENT = LogService.class.getSimpleName();
	public static final Level DEFAULT_LOG_LEVEL = Level.ERROR;
	public static final String DEFAULT_LOG_FILE = "inmetdata.log";
	public static final int DEFAULT_LOG_MAX_SIZE = 1024; // maximum log file size in megabytes (MB)
	public static final String DEFAULT_LOG_PATTERN = "[%t] %c:%L %m%n";
	public static final String DEFAULT_LOG_HEADER = "LogDate ; LogDateMillis ; LogClass; Thread; Source ; Line ; MessageCode ; MessageText";

	public static final String MANDATORY_PATTERN_LOG = "%2d{dd/MM/yyyy HH:mm} ; %5p ; ";
	public static final int MANDATORY_PATTERN_LOG_SIZE = 19;
	public static final String LOG_SIZE_MEASUREMENT_UNIT = "MB";
	// public static final String LOG_FILE_APPENDER_NAME = "FILE_APENDER";

	public static final String SEPARATOR = ";--------------------------------------------------------------------------------";
	public static final String SPACE = "";
	public static final String CHARSET = "UTF-8";
	public static final String LINE_FEED = "\n";

	public static final int DATE_START_POSITION = 0;
	public static final int DATE_END_POSITION = 10;
	public static final int LEVEL_START_POSITION = 17;
	public static final int LEVEL_END_POSITION = 22;

	public static List<LogService> logServices;

	private Logger logger;
	private RollingFileAppender logFileAppender;
	private SitisPatternLayout logLayout;

	private MessageService messageService;
	private TimeService timeService;
	private String logClientName;
	private Level logLevel;
	private String logFileName;
	private int logMaxSize;
	private String logPattern;
	private String logHeader;

	/**
	 * Public constructor with the message service and log client name parameters.
	 * 
	 * @param messageService
	 *            Istance of the message service used to retrieve system messages.
	 * @param logClienteName
	 *            Name of the client object of the logging service.
	 * @throws ServiceException
	 *             Error in starting of system service.
	 */
	public LogService(MessageService messageService, String logClientName) throws ServiceException {
		this(messageService, logClientName, DEFAULT_LOG_LEVEL, DEFAULT_LOG_FILE, DEFAULT_LOG_MAX_SIZE, DEFAULT_LOG_PATTERN, DEFAULT_LOG_HEADER);
	}

	/**
	 * Public constructor with the message service, log client name and log level parameters.
	 * 
	 * @param messageService
	 *            Istance of the message service used to retrieve system messages.
	 * @param logClienteName
	 *            Name of the client object of the logging service.
	 * @param level
	 *            Desired level for the system log.
	 * @throws ServiceException
	 *             Error in starting of system service.
	 */
	public LogService(MessageService messageService, String logClientName, Level logLevel) throws ServiceException {
		this(messageService, logClientName, logLevel, DEFAULT_LOG_FILE, DEFAULT_LOG_MAX_SIZE, DEFAULT_LOG_PATTERN, DEFAULT_LOG_HEADER);
	}

	/**
	 * Public constructor with the message service, log client name, log level and log file name parameters.
	 * 
	 * @param messageService
	 *            Istance of the message service used to retrieve system messages.
	 * @param logClienteName
	 *            Name of the client object of the logging service.
	 * @param level
	 *            Desired level for the system log.
	 * @param logFileName
	 *            Log file name used to store log messages.
	 * @throws ServiceException
	 *             Error in starting of system service.
	 */
	public LogService(MessageService messageService, String logClientName, Level logLevel, String logFileName) throws ServiceException {
		this(messageService, logClientName, logLevel, logFileName, DEFAULT_LOG_MAX_SIZE, DEFAULT_LOG_PATTERN, DEFAULT_LOG_HEADER);
	}

	/**
	 * Public constructor with all parameters.
	 * 
	 * @param messageService
	 *            Istance of the message service used to retrieve system messages.
	 * @param logClienteName
	 *            Name of the client object of the logging service.
	 * @param level
	 *            Desired level for the system log.
	 * @param logFileName
	 *            Log file name used to store log messages.
	 * @param logMaxSize
	 *            Maximum size of the log file in megabytes (MB).
	 * @param logPattern
	 *            Pattern of the log line formatting.
	 * @param logHeader
	 *            Header used in the log file.
	 * @throws ServiceException
	 *             Error in starting of system service.
	 */
	public LogService(MessageService messageService, String logClientName, Level logLevel, String logFileName, int logMaxSize, String logPattern,
			String logHeader) throws ServiceException {

		// configuring the log4j context
		this.initContext();

		// validating parameters
		if (messageService == null) {
			throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.LOG_SERVICE_ERROR_INVALID_MESSAGING_SERVICE, this.getClass().getSimpleName(), "LogService", null, null, NetworkUtil.getLocalIpAddress(), logClientName);
		}
		if (logClientName == null) {
			logClientName = DEFAULT_LOG_CLIENT;
		}
		if (logLevel == null) {
			logLevel = DEFAULT_LOG_LEVEL;
		}
		if (logMaxSize < 1) {
			logMaxSize = DEFAULT_LOG_MAX_SIZE;
		}
		if (logHeader == null) {
			logHeader = DEFAULT_LOG_HEADER;
		}
		if (logPattern == null) {
			logPattern = DEFAULT_LOG_PATTERN;
		}
		if (logFileName == null) {
			logFileName = DEFAULT_LOG_FILE;
		}

		// creating collection of the log services
		if (logServices == null) {
			logServices = new ArrayList<LogService>();
			logServices = Collections.synchronizedList(logServices);
		}

		// initializing parameters
		this.setMessageService(messageService);
		this.setTimeService(TimeService.getInstanceOf());
		this.setLogClientName(logClientName);
		this.setLogPattern(logPattern);
		this.setLogLevel(logLevel);
		this.setLogMaxSize(logMaxSize);
		this.setLogHeader(logHeader);
		this.setLogFileName(logFileName);

		// adding log service into collection of the log services
		try {
			synchronized (logServices) {
				logServices.add(this);
			}
		} catch (Exception e) {
			throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.LOG_SERVICE_ERROR_UNABLE_RETRIEVE_LOG_SERVICE_LIST, this.getClass().getSimpleName(), "LogService", e.getMessage(), null, NetworkUtil.getLocalIpAddress(), logClientName);
		}

		// writing the initialization information log
		this.success(MessageEnum.LOG_SERVICE_INFO_SUCCESS_CREATING_SERVICE, NetworkUtil.getLocalIpAddress(), logClientName);
	}

	/**
	 * Retrieves the istance of the message service used to retrieve system messages.
	 * 
	 * @return The istance of the message service used to retrieve system messages.
	 */
	public MessageService getMessageService() {
		return messageService;
	}

	/**
	 * Sets the instance of the message service used to retrieve system messages.
	 * 
	 * @param messageService
	 *            Istance of the message service used to retrieve system messages.
	 * @return The log Service Instance.
	 */
	public LogService setMessageService(MessageService messageService) {
		if (messageService != null) {
			this.messageService = messageService;
		}
		return this;
	}

	/**
	 * Retrieves the istance of the time service used to retrieve system messages.
	 * 
	 * @return The istance of the time service used to retrieve system messages.
	 */
	public TimeService getTimeService() {
		return timeService;
	}

	/**
	 * Sets the instance of the time service used to retrieve system messages.
	 * 
	 * @param timeService
	 *            Istance of the time service used to retrieve system messages.
	 * @return The log service Instance.
	 */
	public LogService setTimeService(TimeService timeService) {
		if (timeService != null) {
			this.timeService = timeService;
		}
		return this;
	}

	/**
	 * Retrieves the name of the client object of the logging service.
	 * 
	 * @return Name of the client object of the logging service.
	 */
	public String getLogClientName() {
		return logClientName;
	}

	/**
	 * Sets the name of the client object of the logging service.
	 * 
	 * @param logClienteName
	 *            Name of the client object of the logging service.
	 * @return Log Service Instance.
	 */
	public LogService setLogClientName(String logClienteName) {
		if (logClienteName != null && !logClienteName.equals(this.logClientName)) {
			this.updateLogger(logClienteName);
			this.logClientName = logClienteName;
		}
		return this;
	}

	/**
	 * Retrieves the current level set to the system log. Only messages compatible with this level are written to the log file.
	 * 
	 * @return Current level set to the system log.
	 */
	public Level getLogLevel() {
		return logLevel;
	}

	/**
	 * Sets the desired level for the system log. Only messages compatible with this level is written to the system log file.
	 * 
	 * @param level
	 *            Desired level for the system log.
	 * @return Log Service Instance.
	 */
	public LogService setLogLevel(Level level) {
		if (level != null && !level.equals(this.logLevel)) {
			if (this.getLogger() != null) {
				this.getLogger().setLevel(level);
			}
			this.logLevel = level;
		}
		return this;
	}

	/**
	 * Retrieves the log file name used to store log messages.
	 * 
	 * @return Log file name used to store log messages.
	 */
	public String getLogFileName() {
		return logFileName;
	}

	/**
	 * Sets the log file name used to store log messages.
	 * 
	 * @param logFileName
	 *            Log file name used to store log messages.
	 * @return Log Service Instance.
	 * @throws ServiceException
	 *             Error in setting log file name used to store log messages.
	 */
	public LogService setLogFileName(String logFileName) throws ServiceException {
		if (logFileName != null && !logFileName.equals(this.logFileName)) {
			this.updateFileAppender(logFileName);
			this.logFileName = logFileName;
		}
		return this;
	}

	/**
	 * Retrieves the maximum size of the log file. After the log file reaches this size, the oldest messages are deleted.
	 * 
	 * @return Maximum size of the log file in megabytes (MB).
	 */
	public int getLogMaxSize() {
		return logMaxSize;
	}

	/**
	 * Sets the maximum size of the log file. After the log file reaches this size, the oldest messages are deleted.
	 * 
	 * @param logMaxSize
	 *            Maximum size of the log file in megabytes (MB).
	 * @return Log Service Instance.
	 */
	public LogService setLogMaxSize(int logMaxSize) {
		if (logMaxSize >= 1 && logMaxSize != this.getLogMaxSize()) {
			if (this.getFileAppender() != null) {
				((RollingFileAppender) this.getFileAppender()).setMaxFileSize(this.getLogMaxSize() + LOG_SIZE_MEASUREMENT_UNIT);
			}
			this.logMaxSize = logMaxSize;
		}
		return this;
	}

	/**
	 * Retrieves the pattern of the log line formatting.
	 * 
	 * @return Pattern of the log line formatting.
	 */
	public String getLogPattern() {
		return logPattern;
	}

	/**
	 * Sets the pattern of the log line formatting.
	 * 
	 * @param logPattern
	 *            Pattern of the log line formatting.
	 * @return Log Service Instance.
	 */
	public LogService setLogPattern(String logPattern) {
		if (logPattern != null && !logPattern.equals(this.getLogPattern())) {
			this.updateLogLayout(logPattern);
			this.logPattern = logPattern;
		}
		return this;
	}

	/**
	 * Retrieves the header used in the log file.
	 * 
	 * @return Header used in the log file.
	 */
	public String getLogHeader() {
		return logHeader;
	}

	/**
	 * Sets the header used in the log file.
	 * 
	 * @param logHeader
	 *            Header used in the log file.
	 * @return Log Service Instance.
	 */
	public LogService setLogHeader(String logHeader) {
		if (logHeader != null) {
			this.logHeader = logHeader;
		}
		return this;
	}

	/**
	 * Write a line filled by hyphens ("-") in the system log file.
	 * 
	 * @return Log Service Instance.
	 */
	public LogService separator() {
		this.getLogger().debug(LogService.SEPARATOR);
		return this;
	}

	/**
	 * Write a blank line in the system log file.
	 * 
	 * @return Log Service Instance.
	 */
	public LogService space() {
		this.getLogger().debug(LogService.SPACE);
		return this;
	}

	/**
	 * Write the message in the system log file with the level "DEBUG". If the current level set to the system log is not compatible with that level, the
	 * message is discarded.
	 * 
	 * @param message
	 *            Message to be written to the system log file.
	 * @return Log Service Instance.
	 */
	public LogService debug(String message) {
		this.getLogger().debug(this.removeLineFeed(message));
		return this;
	}

	/**
	 * Write the message in the system log file with the level "INFO". If the current level set to the system log is not compatible with that level, the message
	 * is discarded.
	 * 
	 * @param message
	 *            Message to be written to the system log file.
	 * @return Log Service Instance.
	 */
	public LogService info(String message) {
		this.getLogger().info(this.removeLineFeed(message));
		return this;
	}

	/**
	 * Write the message in the system log file with the level "WARNING". If the current level set to the system log is not compatible with that level, the
	 * message is discarded.
	 * 
	 * @param message
	 *            Message to be written to the system log file.
	 * @return Log Service Instance.
	 */
	public LogService warning(String message) {
		this.getLogger().warn(this.removeLineFeed(message));
		return this;
	}

	/**
	 * Write the message in the system log file with the level "ERROR". If the current level set to the system log is not compatible with that level, the
	 * message is discarded.
	 * 
	 * @param message
	 *            Message to be written to the system log file.
	 * @return Log Service Instance.
	 */
	public LogService error(String message) {
		this.getLogger().error(this.removeLineFeed(message));
		return this;
	}

	/**
	 * Write the message in the system log file with the level "FATAL". If the current level set to the system log is not compatible with that level, the
	 * message is discarded.
	 * 
	 * @param message
	 *            Message to be written to the system log file.
	 * @return Log Service Instance.
	 */
	public LogService fatal(String message) {
		this.getLogger().fatal(this.removeLineFeed(message));
		return this;
	}

	/**
	 * Retrieves messages stored in the system log file with the given level.
	 * 
	 * @param level
	 *            Level associated with the message during your written to the system log file.
	 * @return The messages stored in the system log file with the given level. Each message is separated by a line end break ("\ n").
	 * @throws ServiceException
	 *             Occurrence of any problems at open log file.
	 */
	public String list(int level) throws ServiceException {
		return this.list(level, null, null);
	}

	/**
	 * Retrieves messages stored in the system log file with the given level and the time informed.
	 * 
	 * 
	 * @param level
	 *            Level associated with the message during your written to the system log file.
	 * @param startDate
	 *            Date (dd/mm/yyyy) from which you want to retrieve messages.
	 * @param endDate
	 *            Date (dd/mm/yyyy) until which still want to retrieve the messages.
	 * @return The messages stored in the system log file with the level and the time informed. Each message is separated by a line end break ("\ n").
	 * @throws ServiceException
	 *             Occurrence of any problems at open log file.
	 */
	public String list(int level, LocalDate startDate, LocalDate endDate) throws ServiceException {

		// reading the log file
		StringBuilder logText = new StringBuilder();
		Scanner log = null;
		String logLine;
		try {
			log = new Scanner(new FileInputStream(this.getLogFileName()));
			while (log.hasNextLine()) {
				// reading next line of the file
				logLine = log.nextLine();

				// adding the line read the return string, if you have the level of log and desired date
				if (this.isDesiredLevel(logLine, level) && this.isDesiredDate(logLine, startDate, endDate)) {
					logText.append(logLine + LogService.LINE_FEED);
				}
			}

		} catch (FileNotFoundException e) {
			throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.LOG_SERVICE_ERROR_LOG_FILE_NOT_FOUND, this.getClass().getSimpleName(), "list", e.getMessage(), null, NetworkUtil.getLocalIpAddress(), this.getLogClientName(), this.getLogFileName());

		} catch (Exception e) {
			throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.LOG_SERVICE_ERROR_UNABLE_RECOVER_LOG_INFORMATION, this.getClass().getSimpleName(), "list", e.getMessage(), null, NetworkUtil.getLocalIpAddress(), this.getLogClientName(), this.getLogFileName());

		} finally {
			// closing the log file
			log.close();
		}

		// returned the string containing the lines read with the desired level of log
		return logText.toString();
	}

	/**
	 * Clears the log contents.
	 * 
	 * @return Log Service Instance.
	 */
	public LogService clear() {
		if (this.getFileAppender() != null) {
			((RollingFileAppender) this.getFileAppender()).rollOver();
		}
		return this;
	}

	/**
	 * Writing success message with the parameters informed.
	 * 
	 * @param status
	 *            MessageEnum item associated with the success.
	 * @param messageParameters
	 *            Parameter list to be informed in error message formatting retrieved from the messaging service.
	 * @return Service instance.
	 */
	protected LogService success(MessageEnum status, String... messageParameters) {
		this.info(this.getMessageService().getMessage(status.name(), messageParameters));
		return this;
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
	 * @param messageParameters
	 *            Parameter list to be informed in error message formatting retrieved from the messaging service.
	 * @return Exception with the parameters informed.
	 */
	protected ServiceException error(String ipAddress, MessageEnum status, String className, String methodName, String exceptionMessage,
			List<GenericException> causes, String... messageParameters) {
		ServiceException exception = ErrorUtil.getServiceExceptionError(ipAddress, status, className, methodName, exceptionMessage, causes, messageParameters);
		this.error(exception.getMessage());
		return exception;
	}

	/**
	 * Init Log4j context with basic configuration.
	 * 
	 * @return Log Service Instance.
	 */
	private LogService initContext() {
		try {
			Logger.getRootLogger().getAllAppenders().nextElement();
		} catch (Exception e) {
			BasicConfigurator.configure();
		}
		return this;
	}

	/**
	 * Checks if the message level on the line is the informed level.
	 * 
	 * @param logLine
	 *            Row retrieved from the system log file.
	 * @param level
	 *            Desired level for the log message.
	 * @return True, if the message in line is informed level.
	 */
	private boolean isDesiredLevel(String logLine, int level) {

		// checking that the desired level is valid
		Level desiredLevel = null;
		switch (level) {
			case Level.ALL_INT:
				return true;
			case Level.OFF_INT:
				return false;
			default:
				desiredLevel = Level.toLevel(level);
		}

		// recovering the level log of the read line
		String logLevel = logLine.substring(LogService.LEVEL_START_POSITION, LogService.LEVEL_END_POSITION).trim();

		// checking the level log of the read line
		if (logLevel.equals(desiredLevel.toString())) {
			return true;
		}

		// does not match the desired level
		return false;
	}

	/**
	 * Checks whether the date message on the line is comprised within the reporting period.
	 * 
	 * @param lineLog
	 *            Row retrieved from the system log file.
	 * @param startDate
	 *            Date (dd/mm/yyyy) from which you want to retrieve messages.
	 * @param endDate
	 *            Date (dd/mm/yyyy) until which still want to retrieve the messages.
	 * @return True, if the message date in the line is comprised within the reporting period.
	 */
	private boolean isDesiredDate(String lineLog, LocalDate startDate, LocalDate endDate) {

		// If the dates are invalid, automatically the method returns true
		if (startDate == null || endDate == null) {
			return true;
		}

		// recovering the level log of the read line
		LocalDate logDate;
		try {
			logDate = LocalDate.parse(lineLog.substring(LogService.DATE_START_POSITION, LogService.DATE_END_POSITION), this.getTimeService().getFormatterDate());
		} catch (DateTimeParseException e) {
			this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.LOG_SERVICE_ERROR_IN_CONVERSION_DATA_VALUES, this.getClass().getSimpleName(), "isDesiredDate", e.getMessage(), null, NetworkUtil.getLocalIpAddress(), this.getLogClientName());
			return false;
		}

		// checking the date of the read line
		return logDate.isEqual(startDate) || logDate.isEqual(endDate) || (logDate.isAfter(startDate) && logDate.isBefore(endDate));
	}

	/**
	 * Remove return e line feed characters of the message to be written in the log.
	 * 
	 * @param message
	 *            Message to be written to the log.
	 * @return Message with the return and line feed characters removed.
	 */
	private String removeLineFeed(String message) {
		if (message != null) {
			message = message.replaceAll("\\r+", "\\\\r");
			message = message.replaceAll("\\n+", "\\\\n");
		}
		return message;
	}

	/**
	 * Gets Logger object from log service.
	 * 
	 * @return Logger object from log service.
	 */
	private Logger getLogger() {
		return this.logger;
	}

	/**
	 * Update Logger object to log service.
	 * 
	 * @param logClienteName
	 *            Name of the client object of the logging service.
	 * @return Log Service Instance.
	 */
	private LogService updateLogger(String logClientName) {

		// validating parameters
		if (logClientName == null) {
			logClientName = DEFAULT_LOG_CLIENT;
		}
		if (logClientName.equals(this.getLogClientName())) {
			return this;
		}

		// creating the logger
		Logger newLogger = Logger.getLogger(logClientName);
		if (this.getLogLevel() != null) {
			newLogger.setLevel(this.getLogLevel());
		}
		if (this.getFileAppender() != null) {
			newLogger.addAppender(this.getFileAppender());
		}
		this.logger = newLogger;

		// return log service instance
		return this;
	}

	/**
	 * Gets Appender object associated to log file.
	 * 
	 * @return Appender object associated to log file.
	 */
	private Appender getFileAppender() {
		return this.logFileAppender;
	}

	/**
	 * Update file appender to Logger object at log service.
	 * 
	 * @param logFileName
	 *            Log file name.
	 * @return Log Service Instance.
	 * @throws ServiceException
	 *             Error in updating file appender to log service.
	 */
	private LogService updateFileAppender(String logFileName) throws ServiceException {

		// validating parameters
		if (logFileName == null) {
			logFileName = DEFAULT_LOG_FILE;
		}
		if (logFileName.equals(this.getLogFileName()) && this.getFileAppender() != null) {
			return this;
		}

		// checking if there is already a created appender to log file
		try {
			synchronized (logServices) {
				for (LogService logService : logServices) {
					if (logFileName.equals(logService.getLogFileName())) {
						if (this.getLogger() != null) {
							this.getLogger().addAppender(logService.getFileAppender());
						}
						this.logFileAppender = (RollingFileAppender) logService.getFileAppender();
						return this;
					}
				}
			}
		} catch (Exception e) {
			throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.LOG_SERVICE_ERROR_UNABLE_RETRIEVE_LOG_SERVICE_LIST, this.getClass().getSimpleName(), "updateFileAppender", e.getMessage(), null, NetworkUtil.getLocalIpAddress(), this.getLogClientName(), logFileName);
		}

		try {
			// creating new file appender
			RollingFileAppender newFileAppender = new RollingFileAppender();
			newFileAppender.setFile(logFileName);
			if (this.getLogLayout() != null) {
				newFileAppender.setLayout(this.getLogLayout());
			}
			newFileAppender.setMaxBackupIndex(0);
			newFileAppender.setEncoding(LogService.CHARSET);
			newFileAppender.activateOptions();

			// updating new file appender
			if (this.getFileAppender() != null && this.getLogger() != null) {
				this.getLogger().removeAppender(this.getFileAppender());
			}
			if (this.getLogger() != null) {
				this.getLogger().addAppender(newFileAppender);
			}
			this.logFileAppender = newFileAppender;
			return this;

		} catch (Exception e) {
			throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.LOG_SERVICE_ERROR_UNABLE_UPDATE_APPENDER_FILE, this.getClass().getSimpleName(), "updateFileAppender", e.getMessage(), null, NetworkUtil.getLocalIpAddress(), this.getLogClientName(), logFileName);
		}
	}

	/**
	 * Gets Layout object from logger.
	 * 
	 * @return Layout object from logger.
	 */
	private Layout getLogLayout() {
		return logLayout;
	}

	/**
	 * Update log layout to appenders at log service.
	 * 
	 * @param logPattern
	 *            Pattern of the log line formatting.
	 * @return Log Service Instance.
	 * @throws ServiceException
	 *             Error in updating layout to appenders at log service.
	 */
	private LogService updateLogLayout(String logPattern) {

		// validating parameters
		if (logPattern == null) {
			logPattern = DEFAULT_LOG_PATTERN;
		}
		if (logPattern.equals(this.getLogPattern())) {
			return this;
		}

		// creating new log layout
		SitisPatternLayout newLogLayout = new SitisPatternLayout(logPattern);

		// adding new log layout to console appender
		Appender currentConsoleAppender = this.getConsoleAppender();
		if (currentConsoleAppender != null) {
			currentConsoleAppender.setLayout(newLogLayout);
		}

		// adding new log layout to file appender
		Appender currentFileAppender = this.getFileAppender();
		if (currentFileAppender != null) {
			currentFileAppender.setLayout(newLogLayout);
		}

		// updating new log layout
		this.logLayout = newLogLayout;
		return this;
	}

	/**
	 * Gets ConsoleAppender object from logger.
	 * 
	 * @return ConsoleAppender object from logger.
	 */
	private Appender getConsoleAppender() {
		try {
			return (Appender) Logger.getRootLogger().getAllAppenders().nextElement();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Nested class for logging formatting.
	 * 
	 */
	private class SitisPatternLayout extends PatternLayout {

		/**
		 * Public constructor with the string pattern will use the logging formatting.
		 * 
		 */
		public SitisPatternLayout(String pattern) {
			super(MANDATORY_PATTERN_LOG + pattern);
		}

		@Override
		public String format(LoggingEvent event) {

			// changing the date of the event
			try {
				long currentTime = getTimeService().getDateMillis();
				String dateTime = getTimeService().getFormatterDateTime().format(Instant.ofEpochMilli(currentTime));
				return dateTime + " ; " + currentTime + " ; " + super.format(event).substring(MANDATORY_PATTERN_LOG_SIZE);
			} catch (Exception e) {
				return getTimeService().getDateString() + " ; " + super.format(event);
			}
		}

		@Override
		public String getHeader() {
			return getLogHeader() + "\n";
		}
	}

}
