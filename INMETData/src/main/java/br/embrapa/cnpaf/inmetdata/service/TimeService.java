package br.embrapa.cnpaf.inmetdata.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;

import br.embrapa.cnpaf.classes.Period;
import br.embrapa.cnpaf.inmetdata.exception.ServiceException;
import br.embrapa.cnpaf.inmetdata.util.TimeUtil;

/**
 * <br>
 * <p>
 * <b>Singleton class responsible for controlling the time for the system.</b>
 * </p>
 * <p>
 * It can be configured to use the system clock or provide a time accelerating
 * mechanism. This improves the testability of the code and allows to reduce the
 * time required for the execution of test cases.
 * </p>
 * <p>
 * To retrieve an instance of this class use the static method getInstanceOf
 * ():<br>
 * <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;
 * <tt> TimeService timeService = TimeService.getInstanceOf();</tt>
 * </p>
 * <br>
 * 
 * @author Rubens de Castro Pereira and Sergio Lopes Jr.
 * @version 0.2
 * @since 03/03/2020 (creation date)
 * 
 */
public class TimeService implements Runnable {

	public static final String SQL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String SQL_DATE_FORMAT = "yyyy-MM-dd";
	public static final String SQL_TIME_FORMAT = "HH:mm:ss";
	public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	public static final String TIME_FORMAT_HHMM = "HH:mm";
	public static final String TIME_FORMAT_HHMMSS = "HH:mm:ss";

	public static final String THREAD_NAME = "TimeService";
	public static final int TIME_BETWEEN_THREADS = 10;

	private ZoneId timeZone;
	private DateTimeFormatter formatterSqlDateTime;
	private DateTimeFormatter formatterSqlDate;
	private DateTimeFormatter formatterSqlTime;
	private DateTimeFormatter formatterDateTime;
	private DateTimeFormatter formatterDate;
	private DateTimeFormatter formatterTime;
	private LocalDate unixEpochDate;
	private ZoneOffset zoneOffSet;

	private static TimeService instance;
	private double rate;
	private double intervalBetweenJumps;
	private Instant currentDate;
	private Thread timeAccelerationThread;

	/**
	 * Private class constructor.
	 * 
	 * @throws ServiceException Occurrence of any problems at start of system
	 *                          service.
	 */
	private TimeService() throws ServiceException {
		super();

		// initializing attributes
		this.timeZone = ConfigurationService.getInstanceOf().getTimeZone();
		this.formatterSqlDateTime = DateTimeFormatter.ofPattern(SQL_DATE_TIME_FORMAT).withZone(this.getTimeZone());
		this.formatterSqlDate = DateTimeFormatter.ofPattern(SQL_DATE_FORMAT).withZone(this.getTimeZone());
		this.formatterSqlTime = DateTimeFormatter.ofPattern(SQL_TIME_FORMAT).withZone(this.getTimeZone());
		this.formatterDateTime = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).withZone(this.getTimeZone());
		this.formatterDate = DateTimeFormatter.ofPattern(DATE_FORMAT).withZone(this.getTimeZone());
		this.formatterTime = DateTimeFormatter.ofPattern(TIME_FORMAT_HHMMSS).withZone(this.getTimeZone());
		this.unixEpochDate = LocalDate.parse("01/01/1970", this.getFormatterDate());
		this.zoneOffSet = ZoneOffset.of(this.getTimeZone().getId());

		// initializing attributes
		this.rate = 0.0;
		this.intervalBetweenJumps = 0.0;
		this.currentDate = ZonedDateTime.now(this.getTimeZone()).toInstant();

		// creating the thread to the acceleration time
		this.timeAccelerationThread = new Thread(this);
		this.timeAccelerationThread.setName(THREAD_NAME);
		this.timeAccelerationThread.start();
	}

	/**
	 * Method to retrieve the instance of SitisEmbeddedTimeService. This class has a
	 * single instance for any application (Singleton).
	 * 
	 * @return Returns the instance of SitisEmbeddedTimeService.
	 * @throws ServiceException Occurrence of any problems at start of system
	 *                          service.
	 */
	public static synchronized TimeService getInstanceOf() throws ServiceException {
		if (TimeService.instance == null) {
			TimeService.instance = new TimeService();
		}
		return TimeService.instance;
	}

	/**
	 * Sets the start date to the time service in UTC time.
	 * 
	 * @param date Start date to the time service in UTC time.
	 * @return Entity instance.
	 */
	public TimeService setStartDate(LocalDateTime date) {
		if (date != null) {
			this.currentDate = date.atZone(this.getTimeZone()).toInstant();
		}
		return this;
	}

	/**
	 * Sets the time acceleration rate to the time service.
	 * 
	 * @param rate Time acceleration rate.
	 * @return Entity instance.
	 */
	public TimeService setTimeAcceleration(double rate) {
		if (rate >= 0) {
			this.rate = rate;
			this.intervalBetweenJumps = 1000 / rate;
			synchronized (this) {
				this.notifyAll();
			}
		}
		return this;
	}

	/**
	 * Retrieving the date calculated by the time service. It can be the clock of
	 * the system or the accelerated date.
	 * 
	 * @return The date calculated by the time service.
	 */
	public LocalDate getDate() {
		return LocalDateTime.ofInstant(this.getCurrentTime(), this.getTimeZone()).toLocalDate();
	}

	/**
	 * Retrieving the date calculated by the time service in LocalDateTime format.
	 * It can be the clock of the system or the accelerated date.
	 * 
	 * @return The date calculated by the time service in LocalDateTime format.
	 */
	public LocalDateTime getDateTime() {
		return LocalDateTime.ofInstant(this.getCurrentTime(), this.getTimeZone());
	}

	/**
	 * Retrieving the date calculated by the time service in number of milliseconds
	 * from the epoch of 1970-01-01T00:00:00Z. It can be the clock of the system or
	 * the accelerated date.
	 * 
	 * @return The date calculated by the time service in number of milliseconds
	 *         from the epoch of 1970-01-01T00:00:00Z.
	 */
	public long getDateMillis() {
		return this.getCurrentTime().toEpochMilli();
	}

	/**
	 * Retrieving the date calculated by the time service in format "dd/MM/yyyy
	 * HH:mm:ss". It can be the clock of the system or the accelerated date.
	 * 
	 * @return The date calculated by the time service in format "dd/MM/yyyy
	 *         HH:mm:ss".
	 */
	public String getDateString() {
		return this.getFormatterDateTime().format(this.getCurrentTime());
	}

	@Override
	public void run() {

		// variables to control the elapsed time since the last acceleration jump time
		long currentTime = 0;
		long timeLastJump = ZonedDateTime.now(this.getTimeZone()).toInstant().toEpochMilli();

		// infinite loop
		while (true) {

			// checking that the thread is disabled
			while (this.rate == 0) {
				try {
					synchronized (this) {
						this.wait();
					}
				} catch (Exception e) {
				}
			}

			// retrieving the current system date
			currentTime = ZonedDateTime.now(this.getTimeZone()).toInstant().toEpochMilli();

			if (this.rate == 1) {

				// calculating the time to be accomplished without its acceleration and
				// maintaining the test dates
				this.currentDate = this.currentDate.plusMillis(currentTime - timeLastJump);
				timeLastJump = currentTime;

			} else {

				// checking that it is time to accelerate time
				if (currentTime - timeLastJump > this.intervalBetweenJumps) {

					// calculating the jump time to be carried out
					this.currentDate = this.currentDate.plusMillis((long) ((currentTime - timeLastJump) * this.rate));
					timeLastJump = currentTime;
				}
			}

			// facilitating the scaling of the CPU to other threads
			try {
				Thread.sleep(TimeService.TIME_BETWEEN_THREADS);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Gets current instant time.
	 * 
	 * @return Current instant time.
	 */
	private Instant getCurrentTime() {
		return (this.rate > 0) ? this.currentDate : ZonedDateTime.now(this.getTimeZone()).toInstant();
	}

	/**
	 * @return the timeZone
	 */
	public ZoneId getTimeZone() {
		return timeZone;
	}

	/**
	 * @return the formatterSqlDateTime
	 */
	public DateTimeFormatter getFormatterSqlDateTime() {
		return formatterSqlDateTime;
	}

	/**
	 * @return the formatterSqlDate
	 */
	public DateTimeFormatter getFormatterSqlDate() {
		return formatterSqlDate;
	}

	/**
	 * @return the formatterSqlTime
	 */
	public DateTimeFormatter getFormatterSqlTime() {
		return formatterSqlTime;
	}

	/**
	 * @return the formatterDateTime
	 */
	public DateTimeFormatter getFormatterDateTime() {
		return formatterDateTime;
	}

	/**
	 * @return the formatterDate
	 */
	public DateTimeFormatter getFormatterDate() {
		return formatterDate;
	}

	/**
	 * @return the formatterTime
	 */
	public DateTimeFormatter getFormatterTime() {
		return formatterTime;
	}

	/**
	 * @return the unixEpochDate
	 */
	public LocalDate getUnixEpochDate() {
		return unixEpochDate;
	}

	/**
	 * @return the zoneOffSet
	 */
	public ZoneOffset getZoneOffSet() {
		return zoneOffSet;
	}

	public String toDateString(long millis) {
		return this.getFormatterDateTime().format(Instant.ofEpochMilli(millis));
	}

	public long toMillis(LocalDateTime localDateTime) {
		return localDateTime.toInstant(this.getZoneOffSet()).toEpochMilli();
	}

	public long toMillis(LocalDate localDate) {
		return LocalDateTime.of(localDate, LocalTime.MIN).toInstant(this.getZoneOffSet()).toEpochMilli();
	}

	public long toMillis(LocalTime localTime) {
		return LocalDateTime.of(this.getUnixEpochDate(), localTime).toInstant(this.getZoneOffSet()).toEpochMilli();
	}

	public LocalDateTime toLocalDateTime(long millis) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), this.getZoneOffSet());
	}

	public LocalDateTime toLocalDateTime(String dateTime) {
		return LocalDateTime.parse(dateTime, this.getFormatterDateTime());
	}

	public LocalDateTime toLocalDateTimeFromSql(String dateTime) {
		return LocalDateTime.parse(dateTime, this.getFormatterSqlDateTime());
	}

	public LocalDate toLocalDateFromSql(String date) {
		return LocalDate.parse(date, this.getFormatterSqlDate());
	}

	public LocalDate toLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public LocalTime toLocalTimeFromSql(String time) {
		return LocalTime.parse(time, this.getFormatterSqlTime());
	}

	public Date toDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public Date toDate(LocalDate localDate, LocalTime localTime) {
		LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public LocalDateTime toLocalDateTime(LocalTime localTime) {
		return LocalDateTime.of(this.getUnixEpochDate(), localTime);
	}

	public LocalDateTime toLocalDateTime(LocalDate localDate) {
		return LocalDateTime.of(localDate, LocalTime.MIN);
	}

	public LocalDateTime toLocalDateTimeMaxTime(LocalDate localDate) {
		return LocalDateTime.of(localDate, LocalTime.MAX);
	}

	public boolean isBefore(LocalDate firstLocalDate, Date secondDate) {
		return this.isAfter(secondDate, firstLocalDate);
	}

	public boolean isAfter(LocalDate firstLocalDate, Date secondDate) {
		return this.isBefore(secondDate, firstLocalDate);
	}

	public boolean isBefore(Date firstDate, LocalDate secondLocalDate) {
		LocalDate firstLocalDate = this.toLocalDate(firstDate);
		if (firstLocalDate.isBefore(secondLocalDate)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isAfter(Date firstDate, LocalDate secondLocalDate) {
		LocalDate firstLocalDate = this.toLocalDate(firstDate);
		if (firstLocalDate.isAfter(secondLocalDate)) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<Period> intervalos(LocalDate inicio,LocalDate fim) {
		Calendar inicio_data = Calendar.getInstance();
		Calendar fim_data = Calendar.getInstance();
		SimpleDateFormat formatar = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<Period> url_estacao = new ArrayList<Period>();
		
		// Convetendo para data
		try {
			inicio_data.setTime(formatar.parse(inicio.toString()));
			fim_data.setTime(formatar.parse(fim.toString()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// Construindo intervalos
		while (inicio_data.getTimeInMillis() <= fim_data.getTimeInMillis()) {
			String data_backup = formatar.format(inicio_data.getTime()).toString();
			// Acrecentando um ano
			inicio_data.set(Calendar.YEAR, inicio_data.get(Calendar.YEAR) + 1);
			String nova_data = formatar.format(inicio_data.getTime()).toString();
			// Verificando se data e maior que o limite
			if (inicio_data.getTimeInMillis() >= fim_data.getTimeInMillis()) {
				nova_data = formatar.format(fim_data.getTime()).toString();
			}
			inicio_data.set(Calendar.DAY_OF_MONTH, inicio_data.get(Calendar.DAY_OF_MONTH) + 1);
			// Populando arraylist
			url_estacao.add(new Period(TimeUtil.stringToLocalDate(data_backup),TimeUtil.stringToLocalDate(nova_data)));
		}
		return url_estacao;
	}

}
