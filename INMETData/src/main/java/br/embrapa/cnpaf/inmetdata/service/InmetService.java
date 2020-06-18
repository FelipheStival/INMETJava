package br.embrapa.cnpaf.inmetdata.service;

import java.nio.file.DirectoryStream.Filter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.log4j.Level;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.google.gson.reflect.TypeToken;

import br.embrapa.cnpaf.inmetdata.entity.InmetDiarlyDataEntity;
import br.embrapa.cnpaf.inmetdata.entity.InmetHourlyDataEntity;
import br.embrapa.cnpaf.inmetdata.entity.InmetStationEntity;
import br.embrapa.cnpaf.inmetdata.enumerate.MessageEnum;
import br.embrapa.cnpaf.inmetdata.exception.PersistenceException;
import br.embrapa.cnpaf.inmetdata.exception.ServiceException;
import br.embrapa.cnpaf.inmetdata.util.JsonUtil;
import br.embrapa.cnpaf.inmetdata.util.NetworkUtil;
import br.embrapa.cnpaf.inmetdata.util.TimeUtil;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.FloatColumn;
import tech.tablesaw.api.LongColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;
import tech.tablesaw.selection.Selection;

/**
 * <br>
 * <p>
 * <b> Singleton Class responsible for by perform invocations to web services
 * from INMET.</b>
 * </p>
 * <p>
 * To retrieve an instance of this class use the static method getInstanceOf
 * ():<br>
 * <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;
 * <tt> InmetService inmetService = InmetService.getInstanceOf();</tt>
 * </p>
 * <br>
 * 
 * @author Sergio Lopes Jr. and Rubens de Castro Pereira.
 * @version 0.2
 * @since 03/03/2020 (creation date)
 * 
 */
public class InmetService extends GenericService<InmetService> {

	public static final String INMET_WEB_SERVICE_URL = "https://apitempo.inmet.gov.br/estacao";

	public static final int REQUEST_ATTEMPTS = 6;
	public static final int RANDOMIZE_TIME_BETWEEN_ATTEMPTS = 100;
	public static final int MINIMUM_TIME_BETWEEN_ATTEMPTS = 20;

	private static InmetService instance;
	private final Client moduleClient;

	/**
	 * Private class constructor.
	 * 
	 * @param logClientName Name of the client object of the logging service.
	 * @param logLevel      Log level to be used in log service.
	 * @throws ServiceException Occurrence of any problems at start of system
	 *                          service.
	 */
	private InmetService(String logClientName, Level logLevel) throws ServiceException {
		super(logClientName, logLevel);

		// initializing parameters configuration
		this.moduleClient = ClientBuilder.newClient();
		this.moduleClient.register(JacksonFeature.class);

		// writing of the service creation details in log
		this.success(MessageEnum.INMET_SERVICE_INFO_SUCCESS_CREATING_SERVICE, NetworkUtil.getLocalIpAddress());
	}

	/**
	 * Method to retrieve the instance of service. This class has a single instance
	 * for any application (Singleton).
	 * 
	 * @param logClientName Name of the client object of the logging service.
	 * @param logLevel      Log level to be used in log service.
	 * @return Returns the instance of service.
	 * @throws PersistenceException Occurrence of any problems in creating of the
	 *                              service.
	 */
	public static synchronized InmetService getInstanceOf(String logClientName, Level logLevel)
			throws ServiceException {
		if (InmetService.instance == null) {
			InmetService.instance = new InmetService(logClientName, logLevel);
		}
		return InmetService.instance;
	}

	/**
	 * Method to retrieve the instance of service. This class has a single instance
	 * for any application (Singleton).
	 * 
	 * @return Returns the instance of service.
	 * @throws ServiceException Occurrence of any problems in creating of the
	 *                          service.
	 */
	public static synchronized InmetService getInstanceOf() throws ServiceException {
		return InmetService.getInstanceOf(InmetService.class.getSimpleName(), null);
	}

	/**
	 * Retrieves the hourly data from INMET.
	 * 
	 * @param station  The station date for data recovery.
	 * @param initDate The start date for data recovery.
	 * @param endDate  The end date for data recovery.
	 * @return The list of the hourly data from INMET.
	 * @throws ServiceException Occurrence of any problems in creating of the
	 *                          service.
	 */
	public List<InmetHourlyDataEntity> getHourlyData(InmetStationEntity station, LocalDate initDate, LocalDate endDate)
			throws ServiceException {

		// http://apitempo.inmet.gov.br/estacao + datainicio + datafim + codigo estacao
		// http://apitempo.inmet.gov.br/estacao/2019-10-23/2019-10-23/A301
		Random randomGenerator = new Random();
		WebTarget serviceUrl;
		String response = null;
		List<InmetHourlyDataEntity> result = null;

		ServiceException error = this.error(NetworkUtil.getLocalIpAddress(),
				MessageEnum.INMET_SERVICE_ERROR_GET_HOURLY_DATA, this.getClass().getSimpleName(), "getHourlyData", null,
				null, false, NetworkUtil.getLocalIpAddress(), station.getCode(),
				TimeUtil.formatterLocalDateToDDMMYYYY(initDate), TimeUtil.formatterLocalDateToDDMMYYYY(endDate));
		for (int ctAttempts = 0; ctAttempts < REQUEST_ATTEMPTS; ctAttempts++) {
			try {
				// random timeout to try again
				try {
					Thread.sleep(
							randomGenerator.nextInt(RANDOMIZE_TIME_BETWEEN_ATTEMPTS) * MINIMUM_TIME_BETWEEN_ATTEMPTS);
				} catch (InterruptedException e) {
				}

				// performing web serive
				serviceUrl = this.moduleClient.target(INMET_WEB_SERVICE_URL)//
						.path("{initDate}")//
						.path("{endDate}")//
						.path("{stationCode}")//
						.resolveTemplate("initDate", this.getTimeService().getFormatterSqlDate().format(initDate))//
						.resolveTemplate("endDate", this.getTimeService().getFormatterSqlDate().format(endDate))//
						.resolveTemplate("stationCode", station.getCode())//
				;
				
				response = serviceUrl.request().get(String.class);

				// retrieving result from response
				result = JsonUtil.getJsonConverter().fromJson(response, new TypeToken<List<InmetHourlyDataEntity>>() {
				}.getType());

				// setting station id
				for (int i = 0; i < result.size(); i++) {
					result.get(i).setEntilyStation(station);
				}

				// checking if the execution successfully
				if (result != null) {
					this.success(MessageEnum.INMET_SERVICE_INFO_SUCCESS_GET_HOURLY_DATA,
							NetworkUtil.getLocalIpAddress(), station.getCode(),
							TimeUtil.formatterLocalDateToDDMMYYYY(initDate),
							TimeUtil.formatterLocalDateToDDMMYYYY(endDate));
					return result;
				} else {
					error.addCause(this.error(NetworkUtil.getLocalIpAddress(),
							MessageEnum.INMET_SERVICE_ERROR_EMPTY_HOURLY_DATA, this.getClass().getSimpleName(),
							"getHourlyData", null, null, true, NetworkUtil.getLocalIpAddress(), station.getCode(),
							TimeUtil.formatterLocalDateToDDMMYYYY(initDate),
							TimeUtil.formatterLocalDateToDDMMYYYY(endDate)));
					return null;
				}

			} catch (Exception e) {
				error.addCause(this.error(NetworkUtil.getLocalIpAddress(),
						MessageEnum.INMET_SERVICE_ERROR_GET_HOURLY_DATA, this.getClass().getSimpleName(),
						"getHourlyData", e.getMessage(), null, true, NetworkUtil.getLocalIpAddress(), station.getCode(),
						TimeUtil.formatterLocalDateToDDMMYYYY(initDate),
						TimeUtil.formatterLocalDateToDDMMYYYY(endDate)));

				return null;
			}
		}

		// error in execute action on remote module
		throw error;
	}

	/**
	 * Transform hourly to daily data
	 * 
	 * @param HourlyData Daily data achieved by INMET
	 * @return list with daily data
	 */

	public List<InmetDiarlyDataEntity> getDailyData(List<InmetHourlyDataEntity> HourlyData) {
		
		//Starting variables
		InmetDiarlyDataEntity  DiarlyData = null ;
		List<InmetDiarlyDataEntity> diarlyData = new ArrayList<InmetDiarlyDataEntity>();

		// Creating dataframe
		Table dataFrameHourly = Table.create("Data").addColumns(StringColumn.create("CD_ESTACAO"),
				LongColumn.create("idStation"), StringColumn.create("DT_MEDICAO"), FloatColumn.create("TEM_MIN"),
				FloatColumn.create("TEM_MAX"), FloatColumn.create("PRE_MIN"), FloatColumn.create("PRE_MAX"),
				FloatColumn.create("UMD_MIN"), FloatColumn.create("UMD_MAX"), FloatColumn.create("VENT_VEL"),
				FloatColumn.create("VENT_DIR"), FloatColumn.create("RAD_GLO"), FloatColumn.create("PTO_MIN"),
				FloatColumn.create("PTO_MAX"), FloatColumn.create("CHUVA"));

		// populating dataframe
		for (InmetHourlyDataEntity data : HourlyData) {
			dataFrameHourly.stringColumn(0).append(data.getEntilyStation().getCode());
			dataFrameHourly.longColumn(1).append(data.getEntilyStation().getId());
			dataFrameHourly.stringColumn(2).append(data.getMeasurementDate().toString());
			dataFrameHourly.floatColumn(3).append(data.getMinimumTemperature());
			dataFrameHourly.floatColumn(4).append(data.getMaximumTemperature());
			dataFrameHourly.floatColumn(5).append(data.getMinimumPrecipitation());
			dataFrameHourly.floatColumn(6).append(data.getMaximumPrecipitation());
			dataFrameHourly.floatColumn(7).append(data.getMinimumRelativeAirHumidity());
			dataFrameHourly.floatColumn(8).append(data.getMaximumRelativeAirHumidity());
			dataFrameHourly.floatColumn(9).append(data.getWindSpeed());
			dataFrameHourly.floatColumn(10).append(data.getWindDirection());
			dataFrameHourly.floatColumn(11).append(data.getGlobalRadiation());
			dataFrameHourly.floatColumn(12).append(data.getMinimumDewPoint());
			dataFrameHourly.floatColumn(13).append(data.getMaximumDewPoint());
			dataFrameHourly.floatColumn(14).append(data.getRain());
		}

		Column<?> uniqueDates = dataFrameHourly.column("DT_MEDICAO").unique();

		for (int i = 0; i < uniqueDates.size(); i++) {
			
			Table FiltredByDate = dataFrameHourly.where(dataFrameHourly.stringColumn("DT_MEDICAO")//
					.isEqualTo(uniqueDates.getString(i)));//
			
			FiltredByDate = FiltredByDate.sortDescendingOn("DT_MEDICAO");

			LocalDate date = TimeUtil.stringToLocalDate(FiltredByDate.getString(0, 2));

			 DiarlyData = new InmetDiarlyDataEntity( //
					null, //
					HourlyData.get(i).getEntilyStation(),
					date, //
					findSmaller(FiltredByDate.floatColumn(3)), //
					findBigger(FiltredByDate.floatColumn(4)), //
					findSmaller(FiltredByDate.floatColumn(5)), //
					findBigger(FiltredByDate.floatColumn(6)), //
					findSmaller(FiltredByDate.floatColumn(7)),
					findBigger(FiltredByDate.floatColumn(8)), //
					mean(FiltredByDate.floatColumn(9)), //
					mean(FiltredByDate.floatColumn(10)), //
					positiveNumbers(FiltredByDate.floatColumn(11)), //
					findSmaller(FiltredByDate.floatColumn(12)), //
					findBigger(FiltredByDate.floatColumn(13)), //
					addAll(FiltredByDate.floatColumn(14)));//
			 
			 diarlyData.add(DiarlyData);
		}
		
		return diarlyData;
	}

	/**
	 * find higher column value
	 * 
	 * @param column dataframe column
	 * @return returns highest column value
	 */

	private Float findBigger(FloatColumn column) {
		if (column.countMissing() <= 12) {
			return (float) column.max();
		}
		return null;
	}

	/**
	 * find lower value
	 * 
	 * @param column dataframe column
	 * @return returns the lowest column value
	 */

	private Float findSmaller(FloatColumn column) {
		if (column.countMissing() <= 12) {
			return (float) column.min();
		}
		return null;
	}

	/**
	 * find mean
	 * 
	 * @param column dataframe column
	 * @return returns highest column value
	 */

	private Float mean(FloatColumn column) {
		if (column.countMissing() <= 12) {
			return (float) column.mean();
		}
		return null;
	}

	/**
	 * add positive column numbers
	 * 
	 * @param column dataframe column
	 * @return returns sum of positive numbers
	 */

	private Float positiveNumbers(FloatColumn column) {
		Selection filter = column.isPositive();
		if (column.countMissing() <= 12) {
			return (float) column.where(filter).sum();
		}
		return null;
	}

	/**
	 * add up all column values
	 * 
	 * @param column dataframe column
	 * @return returns sum of all column elements
	 */

	private Float addAll(FloatColumn column) {
		if (column.countMissing() <= 12) {
			return (float) column.sum();
		}
		return null;
	}
}
