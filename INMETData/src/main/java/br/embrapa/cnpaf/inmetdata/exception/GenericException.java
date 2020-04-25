package br.embrapa.cnpaf.inmetdata.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import br.embrapa.cnpaf.inmetdata.enumerate.MessageEnum;
import br.embrapa.cnpaf.inmetdata.util.JsonUtil;
import br.embrapa.cnpaf.inmetdata.util.NetworkUtil;

/**
 * <br>
 * <p>
 * <b> General exception class to system.</b>
 * </p>
 * 
 * @author Sergio Lopes Jr. and Rubens de Castro Pereira.
 * @version 0.1
 * @since 03/03/2020 (creation date)
 * 
 */
public class GenericException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;

	@Expose
	protected String message;

	@Expose
	protected String ipAddress;

	@Expose
	protected MessageEnum error;

	@Expose
	protected String className;

	@Expose
	protected String methodName;

	@Expose
	protected String exceptionMessage;

	@Expose
	protected List<GenericException> causes;

	/**
	 * Public class constructor with message parameter.
	 * 
	 * @param message
	 *            Message associated with this error.
	 */
	public GenericException(String message) {
		this(message, null, null, null, null, null, null);
	}

	/**
	 * Public class constructor with exception parameter.
	 * 
	 * @param exception
	 *            Except for the parameters for setting this exception.
	 */
	public GenericException(GenericException exception) {
		this(exception.getMessage(), exception.getIpAddress(), exception.getError(), exception.getClassName(), exception.getMethodName(), exception.getMessage(), exception.getCauses());
	}

	/**
	 * Public constructor with all parameters.
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
	 * @param exceptionMessage
	 *            Original message of some exception caught in error handling.
	 * @param causes
	 *            Exceptions that caused the current exception.
	 */
	public GenericException(String message, String ipAddress, MessageEnum error, String className, String methodName, String exceptionMessage,
			List<GenericException> causes) {
		super(message);
		this.message = message;
		this.ipAddress = (ipAddress != null) ? ipAddress : NetworkUtil.getLocalIpAddress();
		this.error = error;
		this.className = className;
		this.methodName = methodName;
		this.exceptionMessage = exceptionMessage;
		this.causes = (causes != null) ? causes : new ArrayList<GenericException>();
	}

	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * Settings the message associated with this error.
	 * 
	 * @param message
	 *            Message associated with this error.
	 * @return Instance of this exception.
	 */
	public GenericException setMessage(String message) {
		this.message = message;
		return this;
	}

	/**
	 * Retrieving the ip address where the error occurred.
	 * 
	 * @return Ip Addresss where the error occurred.
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Sets the Ip Addresss where the error occurred.
	 * 
	 * @param ipAddress
	 *            Ip Addresss where the error occurred.
	 * @return Instance of this exception.
	 */
	public GenericException setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
		return this;
	}

	/**
	 * Returns the error associated with the error.
	 * 
	 * @return Error associated with the error.
	 */
	public MessageEnum getError() {
		return error;
	}

	/**
	 * Settings the error associated with the error.
	 * 
	 * @param error
	 *            Error associated with the error.
	 * @return Instance of this exception.
	 */
	public GenericException setError(MessageEnum error) {
		this.error = error;
		return this;
	}

	/**
	 * Returns the class name being performed at the time that this error occurred.
	 * 
	 * @return Class name being performed at the time that this error occurred.
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Settings the class name being performed at the time that this error occurred.
	 * 
	 * @param className
	 *            Class name being performed at the time that this error occurred.
	 * @return Instance of this exception.
	 */
	public GenericException setClassName(String className) {
		this.className = className;
		return this;
	}

	/**
	 * Returns the method name associated with the error.
	 * 
	 * @return Method name associated with the error.
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Settings the method name associated with the error.
	 * 
	 * @param methodName
	 *            Method name associated with the error.
	 * @return Instance of this exception.
	 */
	public GenericException setMethodName(String methodName) {
		this.methodName = methodName;
		return this;
	}

	/**
	 * Returns the message from original exception associated with this error.
	 * 
	 * @return Message from original exception associated with this error.
	 */
	public String getExceptionMessage() {
		return exceptionMessage;
	}

	/**
	 * Settings the message from original exception associated with this error.
	 * 
	 * @param exceptionMessage
	 *            Message from original exception associated with this error.
	 * @return Instance of this exception.
	 */
	public GenericException setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
		return this;
	}

	/**
	 * Retrieving the exceptions that caused the current exception..
	 * 
	 * @return Exceptions that caused the current exception.
	 */
	public List<GenericException> getCauses() {
		return causes;
	}

	/**
	 * Adds a cause to a current exception.
	 * 
	 * @param cause
	 *            Exception that caused the current exception.
	 * @return Instance of this exception.
	 */
	public GenericException addCause(GenericException cause) {
		try {
			if (this.causes.indexOf(cause) == -1) {
				this.causes.add(cause);
			}
		} catch (Exception e) {
		}
		return this;
	}

	/**
	 * Adds causes to a current exception.
	 * 
	 * @param causes
	 *            Exceptions that caused the current exception.
	 * @return Instance of this exception.
	 */
	public GenericException addCauses(List<GenericException> causes) {
		if (causes != null) {
			for (GenericException cause : causes) {
				this.addCause(cause);
			}
		}
		return this;
	}

	/**
	 * Removes a cause to a current exception.
	 * 
	 * @param cause
	 *            Cause to be removed from this exception.
	 * @return Instance of this exception.
	 */
	public GenericException removeCause(GenericException cause) {
		try {
			this.causes.remove(cause);
		} catch (Exception e) {
		}
		return this;
	}

	/**
	 * Clears all added causes for this exception.
	 * 
	 * @return Instance of this exception.
	 */
	public GenericException clearCauses() {
		try {
			this.causes.clear();
		} catch (Exception e) {
		}
		return this;
	}

	/**
	 * List only the major (innermost) causes of this exception.
	 * 
	 * @return List containing only the major (innermost) causes of this exception.
	 */
	public List<GenericException> resumeCauses() {
		return (this.getCauses().isEmpty()) ? this.getCauses() : resumeCauses(this);
	}

	/**
	 * List all causes of this exception. converts a tree-root structure to list.
	 * 
	 * @return List containing all causes of this exception.
	 */
	public List<GenericException> listAllCauses() {
		if (this.getCauses().isEmpty()) {
			return this.getCauses();
		}
		List<GenericException> causes = new ArrayList<GenericException>();
		listAllCauses(this, causes);
		causes.remove(0);
		return causes;
	}

	/**
	 * List all causes of this exception. converts a tree-root structure to list.
	 * 
	 * @return List containing all causes of this exception.
	 */
	public String allCausesToString() {

		String allCauses = "";
		boolean first = true;
		if (this.causes != null) {
			for (GenericException cause : this.listAllCauses()) {
				if (!first) {
					allCauses += " | ";
				}
				allCauses = cause.getMessage();
				first = false;
			}
		}
		return allCauses;
	}

	/**
	 * Recursive method to list only the major (innermost) causes of this exception.
	 * 
	 * @param error
	 *            Exception to recover the main causes.
	 * 
	 * @return List containing only the major (innermost) causes of this exception.
	 */
	protected static List<GenericException> resumeCauses(GenericException error) {
		ArrayList<GenericException> mainCauses = new ArrayList<GenericException>();
		for (GenericException cause : error.getCauses()) {
			List<GenericException> causes = resumeCauses(cause);
			if (causes.isEmpty()) {
				mainCauses.add(cause);
			} else {
				mainCauses.addAll(causes);
			}
		}
		return mainCauses;
	}

	/**
	 * Recursive method to list all causes of this exception. converts a tree-root structure to list.
	 * 
	 * @param error
	 *            Exception to recover all causes.
	 * @param causes
	 *            List to add the causes of the exception.
	 * @return List containing all causes of this exception.
	 */
	protected static void listAllCauses(GenericException error, List<GenericException> causes) {
		if (error != null) {
			causes.add(error);
			List<GenericException> nextCauses = error.getCauses();
			if (nextCauses != null) {
				for (GenericException nextCause : nextCauses) {
					listAllCauses(nextCause, causes);
				}
			}
		}
	}

	/**
	 * Returns the exception data in json format.
	 * 
	 * @return Exception data in json format.
	 */
	public String toJSON() {
		return JsonUtil.getJsonConverterWithExposeAnnotation().toJson(this);
	}

	/**
	 * Retrieving exception from string in json format.
	 * 
	 * @param json
	 *            String in json format with exception attributes.
	 * @return Exception with attributes retrieving from string.
	 */
	public static GenericException valueOf(String json) {
		try {
			return JsonUtil.getJsonConverterWithExposeAnnotation().fromJson(json, GenericException.class);
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((causes == null) ? 0 : causes.hashCode());
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((error == null) ? 0 : error.hashCode());
		result = prime * result + ((exceptionMessage == null) ? 0 : exceptionMessage.hashCode());
		result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericException other = (GenericException) obj;
		if (causes == null) {
			if (other.causes != null)
				return false;
		} else if (!causes.equals(other.causes))
			return false;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (error != other.error)
			return false;
		if (exceptionMessage == null) {
			if (other.exceptionMessage != null)
				return false;
		} else if (!exceptionMessage.equals(other.exceptionMessage))
			return false;
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		return true;
	}

}
