package br.embrapa.cnpaf.inmetdata.exception;

import java.util.List;

import br.embrapa.cnpaf.inmetdata.enumerate.MessageEnum;

/**
 * <br>
 * <p>
 * <b> Error occurred at run time when the value for given parameter is invalid.</b>
 * </p>
 * 
 * @author Sergio Lopes Jr. and Rubens de Castro Pereira.
 * @version 0.1
 */
public class ParameterValueInvalidException extends GenericException {

	private static final long serialVersionUID = 1L;

	/**
	 * Public class constructor with exception parameter.
	 * 
	 * @param exception
	 *            Except for the parameters for setting this exception.
	 */
	public ParameterValueInvalidException(GenericException exception) {
		super(exception);
	}

	/**
	 * Public class constructor with all parameters.
	 * 
	 * @param message
	 *            Message associated with this error.
	 * @param ipAddress
	 *            Ip address where the error occurred.
	 * @param error
	 *            Error associated with the error.
	 * @param className
	 *            Class name being performed at the time that this error occurred.
	 * @param methodName
	 *            Method name being performed at the time that this error occurred.
	 * @param errorMessage
	 *            Message from original exception associated with this error.
	 * @param causes
	 *            Exceptions that caused the current exception.
	 */
	public ParameterValueInvalidException(String message, String ipAddress, MessageEnum error, String className, String methodName, String exceptionMessage,
			List<GenericException> causes) {
		super(message, ipAddress, error, className, methodName, exceptionMessage, causes);
	}

}
