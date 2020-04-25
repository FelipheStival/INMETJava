package br.embrapa.cnpaf.inmetdata.util;

import java.util.List;

import br.embrapa.cnpaf.inmetdata.enumerate.MessageEnum;
import br.embrapa.cnpaf.inmetdata.exception.GenericException;
import br.embrapa.cnpaf.inmetdata.exception.ParameterUnknownException;
import br.embrapa.cnpaf.inmetdata.exception.ParameterValueInvalidException;
import br.embrapa.cnpaf.inmetdata.exception.PersistenceException;
import br.embrapa.cnpaf.inmetdata.exception.ServiceException;
import br.embrapa.cnpaf.inmetdata.service.MessageService;

/**
 * <br>
 * <p>
 * <b>Utility class with methods for getting exception erros.</b>
 * </p>
 * <br>
 * 
 * @author Sergio Lopes Jr. and Rubens de Castro Pereira.
 * @version 0.1
 * @since 03/03/2020 (creation date)
 * 
 */
public class ErrorUtil {

	/**
	 * Creates an GenericException exception with the parameters informed.
	 * 
	 * @param error
	 *            Error associated with the error.
	 * @param className
	 *            Class name being performed at the time that this error occurred.
	 * @param methodName
	 *            Method name being performed at the time that this error occurred.
	 * @param exceptionMessage
	 *            Original message of some exception caught in error handling.
	 * @param causes
	 *            Exceptions that caused the current exception.
	 * @param messageParameters
	 *            Parameter list to be informed in error message formatting retrieved from the messaging service.
	 * @return Exception with the parameters informed.
	 */
	public static GenericException getGenericExceptionError(MessageEnum error, String className, String methodName, String exceptionMessage,
			List<GenericException> causes, String... messageParameters) {
		return new GenericException(ErrorUtil.getMessage(error, exceptionMessage, messageParameters), null, error, className, methodName, exceptionMessage, causes);
	}

	/**
	 * Creates an PersistenceException exception with the parameters informed.
	 * 
	 * @param error
	 *            Error associated with the error.
	 * @param className
	 *            Class name being performed at the time that this error occurred.
	 * @param methodName
	 *            Method name being performed at the time that this error occurred.
	 * @param exceptionMessage
	 *            Original message of some exception caught in error handling.
	 * @param causes
	 *            Exceptions that caused the current exception.
	 * @param messageParameters
	 *            Parameter list to be informed in error message formatting retrieved from the messaging service.
	 * @return Exception with the parameters informed.
	 */
	public static PersistenceException getPersistenceExceptionError(MessageEnum error, String className, String methodName, String exceptionMessage,
			List<GenericException> causes, String... messageParameters) {
		return new PersistenceException(ErrorUtil.getMessage(error, exceptionMessage, messageParameters), null, error, className, methodName, exceptionMessage, causes);
	}

	/**
	 * Creates an exception with the parameters informed.
	 * 
	 * @param error
	 *            Error associated with the error.
	 * @param className
	 *            Class name being performed at the time that this error occurred.
	 * @param methodName
	 *            Method name being performed at the time that this error occurred.
	 * @param exceptionMessage
	 *            Original message of some exception caught in error handling.
	 * @param causes
	 *            Exceptions that caused the current exception.
	 * @param messageParameters
	 *            Parameter list to be informed in error message formatting retrieved from the messaging service.
	 * @return Exception with the parameters informed.
	 */
	public static ServiceException getServiceExceptionError(MessageEnum error, String className, String methodName, String exceptionMessage,
			List<GenericException> causes, String... messageParameters) {
		return new ServiceException(ErrorUtil.getMessage(error, exceptionMessage, messageParameters), null, error, className, methodName, exceptionMessage, causes);
	}

	/**
	 * Creates an GenericException exception with the parameters informed.
	 * 
	 * @param ipAddress
	 *            Ip address where the error occurred.
	 * @param error
	 *            Error associated with the error.
	 * @param className
	 *            Class name being performed at the time that this error occurred.
	 * @param methodName
	 *            Method name being performed at the time that this error occurred.
	 * @param exceptionMessage
	 *            Original message of some exception caught in error handling.
	 * @param causes
	 *            Exceptions that caused the current exception.
	 * @param messageParameters
	 *            Parameter list to be informed in error message formatting retrieved from the messaging service.
	 * @return Exception with the parameters informed.
	 */
	public static GenericException getGenericExceptionError(String ipAddress, MessageEnum error, String className, String methodName, String exceptionMessage,
			List<GenericException> causes, String... messageParameters) {
		return new GenericException(ErrorUtil.getMessage(error, exceptionMessage, messageParameters), ipAddress, error, className, methodName, exceptionMessage, causes);
	}

	/**
	 * Creates an PersistenceException exception with the parameters informed.
	 * 
	 * @param ipAddress
	 *            Ip address where the error occurred.
	 * @param error
	 *            Error associated with the error.
	 * @param className
	 *            Class name being performed at the time that this error occurred.
	 * @param methodName
	 *            Method name being performed at the time that this error occurred.
	 * @param exceptionMessage
	 *            Original message of some exception caught in error handling.
	 * @param causes
	 *            Exceptions that caused the current exception.
	 * @param messageParameters
	 *            Parameter list to be informed in error message formatting retrieved from the messaging service.
	 * @return Exception with the parameters informed.
	 */
	public static PersistenceException getPersistenceExceptionError(String ipAddress, MessageEnum error, String className, String methodName, String exceptionMessage,
			List<GenericException> causes, String... messageParameters) {
		return new PersistenceException(ErrorUtil.getMessage(error, exceptionMessage, messageParameters), ipAddress, error, className, methodName, exceptionMessage, causes);
	}

	/**
	 * Creates an exception with the parameters informed.
	 * 
	 * @param ipAddress
	 *            Ip address where the error occurred.
	 * @param error
	 *            Error associated with the error.
	 * @param className
	 *            Class name being performed at the time that this error occurred.
	 * @param methodName
	 *            Method name being performed at the time that this error occurred.
	 * @param exceptionMessage
	 *            Original message of some exception caught in error handling.
	 * @param causes
	 *            Exceptions that caused the current exception.
	 * @param messageParameters
	 *            Parameter list to be informed in error message formatting retrieved from the messaging service.
	 * @return Exception with the parameters informed.
	 */
	public static ServiceException getServiceExceptionError(String ipAddress, MessageEnum error, String className, String methodName, String exceptionMessage,
			List<GenericException> causes, String... messageParameters) {
		return new ServiceException(ErrorUtil.getMessage(error, exceptionMessage, messageParameters), ipAddress, error, className, methodName, exceptionMessage, causes);
	}

	/**
	 * Creates an ParameterUnknownException exception with the parameters informed.
	 * 
	 * @param ipAddress
	 *            Ip address where the error occurred.
	 * @param error
	 *            Error associated with the error.
	 * @param className
	 *            Class name being performed at the time that this error occurred.
	 * @param methodName
	 *            Method name being performed at the time that this error occurred.
	 * @param exceptionMessage
	 *            Original message of some exception caught in error handling.
	 * @param causes
	 *            Exceptions that caused the current exception.
	 * @param messageParameters
	 *            Parameter list to be informed in error message formatting retrieved from the messaging service.
	 * @return Exception with the parameters informed.
	 */
	public static ParameterUnknownException getParameterUnknownExceptionError(String ipAddress, MessageEnum error, String className, String methodName,
			String exceptionMessage, List<GenericException> causes, String... messageParameters) {
		return new ParameterUnknownException(ErrorUtil.getMessage(error, exceptionMessage, messageParameters), ipAddress, error, className, methodName, exceptionMessage, causes);
	}

	/**
	 * Creates an ParameterValueInvalidException exception with the parameters informed.
	 * 
	 * @param ipAddress
	 *            Ip address where the error occurred.
	 * @param error
	 *            Error associated with the error.
	 * @param className
	 *            Class name being performed at the time that this error occurred.
	 * @param methodName
	 *            Method name being performed at the time that this error occurred.
	 * @param exceptionMessage
	 *            Original message of some exception caught in error handling.
	 * @param causes
	 *            Exceptions that caused the current exception.
	 * @param messageParameters
	 *            Parameter list to be informed in error message formatting retrieved from the messaging service.
	 * @return Exception with the parameters informed.
	 */
	public static ParameterValueInvalidException getParameterValueInvalidExceptionError(String ipAddress, MessageEnum error, String className, String methodName,
			String exceptionMessage, List<GenericException> causes, String... messageParameters) {
		return new ParameterValueInvalidException(ErrorUtil.getMessage(error, exceptionMessage, messageParameters), ipAddress, error, className, methodName, exceptionMessage, causes);
	}

	/**
	 * Retrieving error message formated from message service.
	 * 
	 * @param error
	 *            Error associated with the error.
	 * @param exceptionMessage
	 *            Original message of some exception caught in error handling.
	 * @param messageParameters
	 *            Parameter list to be informed in error message formatting retrieved from the messaging service.
	 * @return
	 */
	public static String getMessage(MessageEnum error, String exceptionMessage, String... messageParameters) {
		try {
			if (exceptionMessage == null && messageParameters == null) {
				return MessageService.getInstanceOf().getResumeMessage(error.name());
			}
			if (exceptionMessage == null && messageParameters != null) {
				return MessageService.getInstanceOf().getResumeMessage(error.name(), messageParameters);
			}
			if (exceptionMessage != null && messageParameters == null) {
				return MessageService.getInstanceOf().getResumeMessage(error.name(), new String[] { exceptionMessage });
			}
			if (exceptionMessage != null && messageParameters != null) {
				String[] params = new String[messageParameters.length + 1];
				for (int ct = 0; ct < messageParameters.length; ct++) {
					params[ct] = messageParameters[ct];
				}
				params[messageParameters.length] = exceptionMessage;
				return MessageService.getInstanceOf().getResumeMessage(error.name(), params);
			}

		} catch (Exception e) {
			System.out.println("ErrorUtil - getMessage 6 : exception class = " + e.getClass().getSimpleName() + ", message = " + e.getMessage() + ", cause = " + e.getCause());
		}
		return null;
	}
}
