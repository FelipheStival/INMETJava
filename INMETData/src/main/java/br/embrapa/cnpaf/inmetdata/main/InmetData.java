package br.embrapa.cnpaf.inmetdata.main;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.swing.plaf.SliderUI;

import com.ibm.icu.impl.CalendarAstronomer.Horizon;

import br.embrapa.cnpaf.classes.Period;
import br.embrapa.cnpaf.inmetdata.dao.InmetCityDataDAO;
import br.embrapa.cnpaf.inmetdata.dao.InmetDiarlyDataDAO;
import br.embrapa.cnpaf.inmetdata.dao.InmetHourlyDataDAO;
import br.embrapa.cnpaf.inmetdata.dao.InmetStationDAO;
import br.embrapa.cnpaf.inmetdata.dao.InmetStateDataDAO;
import br.embrapa.cnpaf.inmetdata.entity.InmetCityEntily;
import br.embrapa.cnpaf.inmetdata.entity.InmetDiarlyDataEntity;
import br.embrapa.cnpaf.inmetdata.entity.InmetHourlyDataEntity;
import br.embrapa.cnpaf.inmetdata.entity.InmetStationEntity;
import br.embrapa.cnpaf.inmetdata.entity.InmetStateEntily;
import br.embrapa.cnpaf.inmetdata.enumerate.MessageEnum;
import br.embrapa.cnpaf.inmetdata.exception.GenericException;
import br.embrapa.cnpaf.inmetdata.exception.PersistenceException;
import br.embrapa.cnpaf.inmetdata.exception.ServiceException;
import br.embrapa.cnpaf.inmetdata.service.ConfigurationService;
import br.embrapa.cnpaf.inmetdata.service.InmetService;
import br.embrapa.cnpaf.inmetdata.service.LogService;
import br.embrapa.cnpaf.inmetdata.service.MessageService;
import br.embrapa.cnpaf.inmetdata.service.TimeService;
import br.embrapa.cnpaf.inmetdata.util.ErrorUtil;
import br.embrapa.cnpaf.inmetdata.util.NetworkUtil;
import br.embrapa.cnpaf.inmetdata.util.TimeUtil;

/**
 * <br>
 * <p>
 * <b> Class responsible for activation the system.</b>
 * </p>
 * <br>
 * 
 * @author Sergio Lopes Jr.
 * @version 0.1
 * @since 03/03/2020 (creation date)
 * 
 */
public class InmetData {

	public static final String MSG_ERROR_INIT_SYSTEM = "Could not start the system: ";

	private static InmetData instance;
	private LogService logService;

	/**
	 * Private class constructor.
	 * 
	 * @throws ServiceException     Occurrence of any problems at start of system
	 *                              service.
	 * @throws PersistenceException Occurrence of any problems at start of system
	 *                              DAOs.
	 */
	private InmetData() throws ServiceException {
		super();

		// initializing log service
		this.logService = new LogService(MessageService.getInstanceOf(), InmetData.class.getSimpleName(),
				ConfigurationService.getInstanceOf().getLogLevel());

		// writing of the system activation details in log
		this.success(MessageEnum.INMETDATA_INFO_SUCCESS_CREATE, NetworkUtil.getLocalIpAddress());
	}

	/**
	 * Method to retrieve the instance of WebService. This class has a single
	 * instance for any application (Singleton).
	 * 
	 * @return Returns the instance of SitisEmbedded.
	 * @throws ServiceException Occurrence of any problems at start of system
	 *                          service.
	 */
	public static synchronized InmetData getInstanceOf() throws ServiceException {
		if (instance == null) {
			instance = new InmetData();
		}
		return instance;
	}

	/**
	 * Initializes system services and DAOs.
	 * 
	 * @return Instance of SitisEmbedded.
	 * @throws ServiceException Error in initializing system services and DAOs.
	 */
	private static InmetData init() throws ServiceException {
		try {
			// initializing DAOs
			InmetStationDAO.getInstanceOf();
			InmetHourlyDataDAO.getInstanceOf();
			InmetDiarlyDataDAO.getInstanceOf();

			// initializing services
			TimeService.getInstanceOf();
			InmetService.getInstanceOf();

		} catch (GenericException e) {
			throw new ServiceException(e);
		}

		return this;
	}

	/**
	 * Returns the instance of the log service.
	 * 
	 * @return Instance of the log service.
	 */
	private LogService getLogService() {
		return logService;
	}

	/**
	 * Writing success message with the parameters informed.
	 * 
	 * @param error             MessageEnum item associated with the success.
	 * @param messageParameters Parameter list to be informed in error message
	 *                          formatting retrieved from the messaging service.
	 * @return Service instance.
	 */
	private InmetData success(MessageEnum error, String... messageParameters) {
		try {
			this.getLogService().info(MessageService.getInstanceOf().getMessage(error.name(), messageParameters));
		} catch (ServiceException e) {
		}
		return this;
	}

	/**
	 * Creating an exception with the parameters informed.
	 * 
	 * @param error             MessageEnum item associated with the error.
	 * @param className         Class name being performed at the time that this
	 *                          error occurred.
	 * @param methodName        Method name being performed at the time that this
	 *                          error occurred.
	 * @param exceptionMessage  Original message of some exception caught in error
	 *                          handling.
	 * @param causes            Exceptions that caused the current error.
	 * @param writeLog          If true the error is written to the log.
	 * @param messageParameters Parameter list to be informed in error message
	 *                          formatting retrieved from the messaging service.
	 * @return Exception with the parameters informed.
	 */
	private ServiceException error(MessageEnum error, String className, String methodName, String exceptionMessage,
			List<GenericException> causes, boolean writeLog, String... messageParameters) {
		ServiceException exception = ErrorUtil.getServiceExceptionError(error, className, methodName, exceptionMessage,
				causes, messageParameters);
		if (writeLog) {
			try {
				this.getLogService().error(exception.getMessage());
			} catch (Throwable e) {
			}
		}
		return exception;
	}

	/**
	 * Main method for system activation
	 * 
	 * @param args Command-Line Arguments.
	 * @throws PersistenceException Occurrence of any problems at start of system
	 *                              DAOs.
	 */
	public static void main(String[] args) {
		try {
			// initializing DAOs
			InmetStateDataDAO inmetStateDataDA = InmetStateDataDAO.getInstanceOf();
			InmetCityDataDAO inmetCityDataDAO = InmetCityDataDAO.getInstanceOf();
			InmetStationDAO inmetStationDAO = InmetStationDAO.getInstanceOf();
			InmetHourlyDataDAO hourlyDataDAO = InmetHourlyDataDAO.getInstanceOf();
			InmetDiarlyDataDAO diarlyDataDAO = InmetDiarlyDataDAO.getInstanceOf();

			// initializing services
			TimeService.getInstanceOf();
			InmetService.getInstanceOf();

			// starting variables
			List<InmetStationEntity> stationEntities;
			List<InmetHourlyDataEntity> hourlyData;
			List<InmetDiarlyDataEntity> diarlyData;
			List<Period> periods;
			LocalDate startDate;
			LocalDate endDate;
			LocalDate dataCollected;

			// starting
			stationEntities = inmetStationDAO.list();
			LocalDate yesterday = TimeService.getInstanceOf().getDate().minusDays(1);
			stationEntities = inmetStationDAO.list();

			for (int i = 0; i < stationEntities.size(); i++) {

				// Generating periods
				periods = TimeService.getInstanceOf().intervalos(stationEntities.get(i).getStartDate(), yesterday);

				for (int j = 0; j < periods.size(); j++) {
					// Getting date
					hourlyData = InmetService.getInstanceOf().getHourlyData(stationEntities.get(i),
							periods.get(j).getStart(), periods.get(j).getEnd());

					if (hourlyData != null) {
						// Obtendo dados diarios
						diarlyData = InmetService.getInstanceOf().getDailyData(hourlyData);

						// Inserindo dados horarios
						for (int k = 0; k < hourlyData.size(); k++) {
								hourlyDataDAO.save(hourlyData.get(k));
						}
						// Inserindo dados diarios
						for (int k = 0; k < diarlyData.size(); k++) {
								diarlyDataDAO.save(diarlyData.get(k));
						}
					}
				}
			}

		} catch (GenericException e) {
			e.printStackTrace();
		}

		// Finalizing application
		System.exit(0);
	}

}
