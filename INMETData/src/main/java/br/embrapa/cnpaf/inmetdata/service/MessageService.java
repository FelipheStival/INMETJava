package br.embrapa.cnpaf.inmetdata.service;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import br.embrapa.cnpaf.inmetdata.enumerate.MessageEnum;
import br.embrapa.cnpaf.inmetdata.exception.ServiceException;
import br.embrapa.cnpaf.inmetdata.util.NetworkUtil;


/**
 * <br>
 * <p>
 * <b>Singleton class responsible for retrieve the system messages.</b>
 * </p>
 * <p>
 * These messages are configured from the file "messages_<language>_<country>.properties" located in the "i18n" system folder. The system will search for the
 * compatible file with the language setting for him..
 * </p>
 * <p>
 * To retrieve an instance of this class use the static method getInstanceOf ():<br>
 * <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; <tt> MessageService messageService = MessageService.getInstanceOf();</tt>
 * </p>
 * <br>
 * 
 * @author Sergio Lopes Jr. and Rubens de Castro Pereira.
 * @version 0.2
 * @since 03/03/2020 (creation date)
 * 
 */
public class MessageService {

	public static final String MESSAGE_SERVICE_ERROR_NOT_POSSIBLE_RETRIEVE_MESSAGE_FILE_PT_BR = "Não foi possível recuperar o arquivo de mensagens para o idioma portugûes Brasil (pt_BR).";
	public static final String MESSAGE_SERVICE_ERROR_NOT_POSSIBLE_RETRIEVE_MESSAGE_FILE_EN_US = "Could not retrieve message file for defined language.";
	public static final String DEFAULT_MESSAGE_FILE = "i18n/messages";
	public static final Locale DEFAULT_LANGUAGE = new Locale("pt", "BR");

	private static MessageService instance;
	private String messageFileName;
	private Locale language;
	private ResourceBundle messages;

	/**
	 * Private constructor class
	 * 
	 * @throws ServiceException
	 *             Error in starting of system service.
	 */
	private MessageService() throws ServiceException {

		// initializing parameters
		this.messageFileName = DEFAULT_MESSAGE_FILE;
		this.language = DEFAULT_LANGUAGE;
	}

	/**
	 * Method to retrieve the instance of MessageService. This class has a single instance for any application (Singleton).
	 * 
	 * @return Message Service Instance.
	 * @throws ServiceException
	 *             Error in starting of system service.
	 */
	public static synchronized MessageService getInstanceOf() throws ServiceException {
		if (MessageService.instance == null) {
			MessageService.instance = new MessageService();
		}
		return MessageService.instance;
	}

	/**
	 * Retrieves the message file name used to store system messages.
	 * 
	 * @return Message file name used to store system messages.
	 */
	public String getMessageFileName() {
		return messageFileName;
	}

	/**
	 * Sets the message file name used to store system messages.
	 * 
	 * @param messageFileName
	 *            Message file name used to store system messages.
	 * @return Message Service Instance.
	 * @throws ServiceException
	 *             Error in setting message file used to store system messages. Could not retrieve message file for defined language.
	 */
	public MessageService setMessageFileName(String messageFileName) throws ServiceException {
		if (messageFileName != null && !this.messageFileName.equals(messageFileName)) {
			this.init(messageFileName, this.getLanguage());
			this.messageFileName = messageFileName;
		}
		return this;
	}

	/**
	 * Retrieves the language used in the messaging service.
	 * 
	 * @return Language used in the messaging service.
	 */
	public Locale getLanguage() {
		return language;
	}

	/**
	 * Sets the language used in the messaging service.
	 * 
	 * @param language
	 *            Language used in the messaging service.
	 * @return Message Service Instance.
	 * @throws ServiceException
	 *             Error in setting language used in the messaging service. Could not retrieve message file for defined language.
	 */
	public MessageService setLanguage(Locale language) throws ServiceException {
		if (language != null && !this.language.equals(language)) {
			this.init(this.getMessageFileName(), language);
			this.language = language;
		}
		return this;
	}

	/**
	 * Verifies that the system message file has been recovered and the service initialized correctly.
	 * 
	 * @return True if the service was properly initialized and false otherwise.
	 */
	public boolean isInitialized() {
		return this.messages != null;
	}

	/**
	 * Retrieves system messages. These messages are configured from the file "messages_<language>_<country>.properties" located in the "i18n" system folder.
	 * The system will search for the compatible file with the language setting for him.
	 * 
	 * @return Message Service Instance.
	 * @throws ServiceException
	 *             Error in initializing messaging service. Could not retrieve message file for defined language.
	 */
	public MessageService init() throws ServiceException {
		this.init(this.getMessageFileName(), this.getLanguage());
		return this;
	}

	/**
	 * Initializes the message service by retrieving the system message file for the defined language.
	 * 
	 * @return Message Service Instance.
	 * @throws ServiceException
	 *             Error in initializing messaging service. Could not retrieve message file for defined language.
	 */
	private MessageService init(String messageFile, Locale language) throws ServiceException {

		// validating parameters
		if (messageFile == null) {
			messageFile = DEFAULT_MESSAGE_FILE;
		}
		if (language == null) {
			language = DEFAULT_LANGUAGE;
		}
		if (this.messages != null &&   messageFile.equals(this.getMessageFileName()) && language.equals(this.getLanguage())) {
			return this;
		}

		try {
			// creating the messages bundle
			ResourceBundle newMessages = ResourceBundle.getBundle(messageFile, language);

			// updating new messages bundle
			this.messages = newMessages;
			return this;

		} catch (Exception e) {
			String message = (this.getLanguage().equals(DEFAULT_LANGUAGE)) ? MESSAGE_SERVICE_ERROR_NOT_POSSIBLE_RETRIEVE_MESSAGE_FILE_PT_BR : MESSAGE_SERVICE_ERROR_NOT_POSSIBLE_RETRIEVE_MESSAGE_FILE_EN_US;
			throw new ServiceException(message, NetworkUtil.getLocalIpAddress(), MessageEnum.MESSAGE_SERVICE_ERROR_NOT_POSSIBLE_RETRIEVE_MESSAGE_FILE, this.getClass().getSimpleName(), "init", e.getMessage(), null);
		}
	}

	/**
	 * Retrieves the message associated with the id and automatically formats with informed arguments. The messages are retrieved according to the language
	 * setting for the system.
	 * 
	 * @param id
	 *            Message identifier in the system message file (key).
	 * @param args
	 *            Texts to be inserted in the body of the message.
	 * @return Formated message associated with the id informed.
	 * @throws ServiceException
	 */
	public String getMessage(String id, String... args) {
		return this.proccessMessage(false, id, args);
	}

	/**
	 * Retrieves the resumed message associated with the id and automatically formats with informed arguments. The messages are retrieved according to the
	 * language setting for the system.
	 * 
	 * @param id
	 *            Message identifier in the system message file (key).
	 * @param args
	 *            Texts to be inserted in the body of the message.
	 * @return Formated message associated with the id informed.
	 * @throws ServiceException
	 */
	public String getResumeMessage(String id, String... args) {
		return this.proccessMessage(true, id, args);
	}

	/**
	 * Retrieves the resumed message associated with the id and automatically formats with informed arguments. The messages are retrieved according to the
	 * language setting for the system.
	 * 
	 * @param resumed
	 *            True whether message logging data should be deleted and false otherwise.
	 * @param id
	 *            Message identifier in the system message file (key).
	 * @param args
	 *            Texts to be inserted in the body of the message.
	 * @return Formated message associated with the id informed.
	 * @throws ServiceException
	 */
	public String proccessMessage(boolean resumed, String id, String... args) {

		// verifying that the message file has already been retrieved
		try {
			if (!this.isInitialized()) {
				this.init(this.getMessageFileName(), this.getLanguage());
			}

			// retrieving the requested message
			String msg = id;
			try {
				msg = this.messages.getString(id);
			} catch (Exception e) {
			}

			// formating the requested message
			MessageFormat msgFormat = (resumed == true) ? new MessageFormat(msg) : new MessageFormat(String.format("%-50.50s", id) + " ; " + msg);
			return msgFormat.format(args);

		} catch (Exception e) {
			return null;
		}
	}

}
