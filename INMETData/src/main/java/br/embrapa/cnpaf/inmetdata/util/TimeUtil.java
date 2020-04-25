package br.embrapa.cnpaf.inmetdata.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

import br.embrapa.cnpaf.inmetdata.service.TimeService;

/**
 * This class contains static methods for working with date and time values.
 * 
 * @author Rubens de Castro Pereira and Sergio Lopes Jr.
 * @version 0.2
 * @since 03/03/2020 (creation date)
 * 
 */

public class TimeUtil {

	/**
	 * This method formats a date using the Brazilian convention
	 * 
	 * @param date
	 * @return formatted date
	 */
	public static String formatterDate(Date date, byte format) {
		// checking date
		if (date == null) {
			return "";
		}

		// creating simple date formatter instance
		SimpleDateFormat simpleDateFormat;
		switch (format) {
		case 1:
			simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			break;

		case 2:
			simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			break;

		case 3:
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			break;

		case 4:
			simpleDateFormat = new SimpleDateFormat("yyyy");
			break;

		default:
			simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			break;
		}

		// formatting date
		String formattedDate = simpleDateFormat.format(date);

		// return formatted date
		return formattedDate;
	}

	/**
	 * Formatter of the value according to the mask.
	 * 
	 * @param value The value to be formatted.
	 * @param mask  The mask used by formatter.
	 * @return String The value formatted.
	 */
	public static String formatterValue(Object value, String mask) {
		// defining auxiliary variable
		String result = "";

		// checking zip code
		if (value == null) {
			return "";
		}

		try {
			// creating mask formatter
			MaskFormatter maskFormatter = new MaskFormatter();
			maskFormatter.setMask(mask);
			maskFormatter.setPlaceholderCharacter('_');
			JFormattedTextField jftZipCode = new JFormattedTextField(maskFormatter);
			jftZipCode.setText(String.valueOf(value));
			result = jftZipCode.getText();

		} catch (ParseException e) {
			// nothing to do
		}

		// return value formatted
		return result;
	}

	/**
	 * Add or subtract days from one reference date.
	 * 
	 * @param date The reference date.
	 * @param days Number of days.
	 * @return Date - The new date resulting of the operation.
	 */
	public static Date addDays(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}

	/**
	 * Add or subtract days from one reference date.
	 * 
	 * @param date The reference date.
	 * @param days Number of days.
	 * @return Date - The new date resulting of the operation.
	 */
	public static LocalDate addDays(LocalDate date, int days) {
		return date.plusDays(days);
	}

	/**
	 * Add or subtract years from one reference date.
	 * 
	 * @param date   The reference date.
	 * @param Number of days.
	 * @return Date - The new date resulting of the operation.
	 */
	public static Date addYears(Date date, int years) {
		return TimeUtil.addDays(date, years * 365);
	}

	/**
	 * Retrieve a year of a full date
	 * 
	 * @param date The full date
	 * @return The year of the date
	 */
	public static int getYear(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		return year;
	}

	/**
	 * Retrieve a month of a full date
	 * 
	 * @param date The full date
	 * @return The month of the date
	 */
	public static int getMonth(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH) + 1;
		return month;
	}

	/**
	 * Retrieve a day of a full date
	 * 
	 * @param date The full date
	 * @return The day of the date
	 */
	public static int getDay(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return day;
	}

	/**
	 * Creates a new date object from day, month and year separates.
	 * 
	 * @param day   The day
	 * @param month The month
	 * @param year  The year
	 * @return Date - The new date from day, month and year.
	 */
	public static Date newDate(int day, int month, int year) {
		Calendar calendar = new GregorianCalendar(year, month - 1, day);
		return calendar.getTime();
	}

	/**
	 * Validates a date in string format
	 * 
	 * @param dateToValidate
	 * @param dateFormat
	 * @return
	 */
	public static boolean isThisDateValid(String dateToValidate, String dateFormat) {

		if (dateToValidate == null) {
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);
		try {
			// if not valid, it will throw ParseException
			Date date = sdf.parse(dateToValidate);
			System.out.println(date);

		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public static String formatterLocalTimeToHHMM(LocalTime localTime) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(TimeService.TIME_FORMAT_HHMM);
		return localTime.format(dtf);
	}

	public static String formatterLocalTimeToHHMMSS(LocalTime localTime) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(TimeService.TIME_FORMAT_HHMMSS);
		return localTime != null ? localTime.format(dtf) : "";
	}

	public static String formatterLocalDateTimeToTime(LocalDateTime localDateTime) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		return localDateTime != null ? localDateTime.format(dtf) : "";
	}

	public static String formatterLocalDateTimeToDDMMYYYYHHMM(LocalDateTime localDateTime) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		return localDateTime != null ? localDateTime.format(dtf) : "";
	}

	public static String formatterLocalDateTimeToDDMMYYYYHHMMSS(LocalDateTime localDateTime) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		return localDateTime != null ? localDateTime.format(dtf) : "";
	}

	public static String formatterLocalDateTimeToDD_MM_YYYY_HH_MM_SS(LocalDateTime localDateTime) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
		return localDateTime != null ? localDateTime.format(dtf) : "";
	}

	public static String formatterLocalDateToDDMMYYYY(LocalDate localDate) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return localDate != null ? localDate.format(dtf) : "";
	}

	public static String formatterLocalDateToYYYYMMDD(LocalDate localDate) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		return localDate != null ? localDate.format(dtf) : "";
	}

	public static boolean isValidLocalTime(String value) {
		try {
			LocalTime.parse(value);
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}

	public static long daysBetweenLocalDate(LocalDate firstLocalDate, LocalDate secondLocalDate) {
		return ChronoUnit.DAYS.between(firstLocalDate, secondLocalDate);
	}

	public static long timeBetweenLocalDateTime(LocalDateTime firstLocalDateTime, LocalDateTime secondLocalDateTime) {
		if (firstLocalDateTime != null && secondLocalDateTime != null) {
			return ChronoUnit.MILLIS.between(firstLocalDateTime, secondLocalDateTime);
		} else {
			return 0;
		}
	}

	public static LocalDate stringToLocalDate(String date) {
		LocalDate dateTime = LocalDate.parse(date);
		return dateTime;
	}

	public static String formatMilisecondsIntoHoursMinutesSeconds(Integer timeMiliseconds) {
		// defining variable
		String result = "";

		// converting time in milliseconds to formatted time
		long hours = TimeUnit.MILLISECONDS.toHours(timeMiliseconds);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(timeMiliseconds)
				- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeMiliseconds));
		long seconds = TimeUnit.MILLISECONDS.toSeconds(timeMiliseconds)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeMiliseconds));
		result += hours > 0 ? hours + " h " : "";
		result += minutes > 0 ? minutes + " min " : "";
		result += seconds > 0 ? seconds + " s" : "";

		// returning the result of formatting
		return result;
	}

	public static String formatSecondsIntoHoursMinutesSeconds(Integer timeSeconds) {
		// defining variable
		String result = "";

		// converting time in milliseconds to formatted time
		long hours = TimeUnit.SECONDS.toHours(timeSeconds);
		long minutes = TimeUnit.SECONDS.toMinutes(timeSeconds)
				- TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(timeSeconds));
		long seconds = TimeUnit.SECONDS.toSeconds(timeSeconds)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(timeSeconds));
		result += hours > 0 ? hours + " h " : "";
		result += minutes > 0 ? minutes + " min " : "";
		result += seconds > 0 ? seconds + " s" : "";

		// returning the result of formatting
		return result;
	}

	/**
	 * Remove time from full date.
	 * 
	 * @param date The date.
	 * @return The date without the time part.
	 */
	public static Date removeTimeFromDate(Date date) {
		int day = TimeUtil.getDay(date);
		int month = TimeUtil.getMonth(date);
		int year = TimeUtil.getYear(date);
		return TimeUtil.newDate(day, month, year);
	}

	/**
	 * Returns the boolean indicator if is time to run anything based n frequency
	 * expressed in seconds.
	 * 
	 * @param startTime             The start time of the window.
	 * @param endTime               The end time of the window.
	 * @param frequency             The frequency desired expressed in seconds.
	 * @param windowTimeInFrequency The window time used in the frequency
	 *                              calculation expressed in seconds.
	 * @return The boolean indicator if is time to run anything based in its
	 *         frequency expressed in seconds.
	 */
	public static boolean isTimeToRun(LocalTime startTime, LocalTime endTime, int frequency,
			int windowTimeInFrequency) {
		// getting the now time
		LocalTime now = LocalTime.now();

		// evaluating if the now time is in the runtime window to run the task
		if (now.isAfter(startTime) && now.isBefore(endTime)) {

			// checking if the now time is according by the frequency
			boolean loop = true;
			LocalTime instantWindowTime = startTime;
			while (loop) {
				// calculating the difference between now and instant window time
				long difference = now.getSecond() - instantWindowTime.getSecond();

				// checking if now is near of window start time
				if (difference < 0) {
					// now is not the time to run anything.
					return false;
				} else {
					if (difference <= windowTimeInFrequency) {
						// now is the time to run
						return true;
					}
				}

				// calculating next instant window
				instantWindowTime.plusSeconds(frequency);
			}
		}

		// now is not the time to run anything.
		return false;
	}

	/**
	 * Return the duration using the start and finish date/time inclusive.
	 */
	public static String getDurationString(LocalDateTime actionExecutionStartDate,
			LocalDateTime actionExecutionEndDate) {
		if (actionExecutionStartDate == null || actionExecutionEndDate == null) {
			return "";
		}
		String duration = TimeUtil.formatMilisecondsIntoHoursMinutesSeconds( //
				(int) TimeUtil.timeBetweenLocalDateTime(actionExecutionStartDate, actionExecutionEndDate) //
		);
		return (!duration.isEmpty() ? " (" + duration + ") " : "");
	}

	/**
	 * Return a list with periods of data
	 * 
	 * @param start of data period.
	 * @param end   of data period.
	 */
	public static ArrayList<String> intervalos(LocalDate inicio,LocalDate fim) {
		Calendar inicio_data = Calendar.getInstance();
		Calendar fim_data = Calendar.getInstance();
		SimpleDateFormat formatar = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<String> url_estacao = new ArrayList<String>();
		// Convetendo para data
		try {
			inicio_data.setTime(formatar.parse(inicio.toString()));
			fim_data.setTime(formatar.parse(fim.toString()));
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
				url_estacao.add(data_backup + "/" + nova_data);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return url_estacao;
	}
}