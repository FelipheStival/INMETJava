package br.embrapa.cnpaf.inmetdata.service;

import java.util.List;

import org.apache.log4j.Level;

import br.embrapa.cnpaf.inmetdata.enumerate.MessageEnum;
import br.embrapa.cnpaf.inmetdata.exception.GenericException;
import br.embrapa.cnpaf.inmetdata.exception.ServiceException;
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
 * @author Sergio Lopes Jr.
 * @version 0.1
 * @since 03/03/2020 (creation date)
 *
 * @param <E>
 *            Entity class to be stored or retrieved by DAO.
 */
abstract class GenericService<E> {

	// class attributes
	protected final ConfigurationService configurationService;
	protected final MessageService messageService;
	protected final LogService logService;
	protected final TimeService timeService;

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
	protected GenericService(String logClientName, Level logLevel) throws ServiceException {
		super();

		try {
			this.configurationService = ConfigurationService.getInstanceOf();
			this.messageService = MessageService.getInstanceOf();
			this.logService = new LogService(messageService, logClientName, (logLevel != null) ? logLevel : this.getConfigurationService().getLogLevel());
			this.timeService = TimeService.getInstanceOf();

		} catch (Throwable e) {
			throw this.error(NetworkUtil.getLocalIpAddress(), MessageEnum.GENERIC_SERVICE_ERROR_CREATE, this.getClass().getSimpleName(), "GenericService", e.getMessage(), null, true, NetworkUtil.getLocalIpAddress(), this.getServiceDescriptor());
		}
	}

	/**
	 * Retrieves the service descriptor.
	 * 
	 * @return Service descriptor.
	 */
	public String getServiceDescriptor() {
		return this.getClass().getSimpleName();
	}

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
	 * @return Service instance.
	 */
	protected E success(MessageEnum status, String... messageParameters) {
		try {
			this.getLogService().info(MessageService.getInstanceOf().getMessage(status.name(), messageParameters));
		} catch (ServiceException e) {
		}
		return (E) this;
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
	protected ServiceException error(String ipAddress, MessageEnum status, String className, String methodName, String exceptionMessage,
			List<GenericException> causes, boolean writeLog, String... messageParameters) {
		ServiceException exception = ErrorUtil.getServiceExceptionError(ipAddress, status, className, methodName, exceptionMessage, causes, messageParameters);
		if (writeLog) {
			this.getLogService().error(exception.getMessage());
		}
		return exception;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((logService == null) ? 0 : logService.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof GenericService))
			return false;
		GenericService other = (GenericService) obj;
		if (logService == null) {
			if (other.logService != null)
				return false;
		} else if (!logService.equals(other.logService))
			return false;
		return true;
	}

}
