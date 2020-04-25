package br.embrapa.cnpaf.inmetdata.util;

/**
 * <p>
 * <b>This class implements the utility functions.</b>
 * </p>
 * 
 * @author Rubens de Castro Pereira and Sergio Lopes Jr.
 * @version 0.1
 * @since 06/09/2017 (creation date)
 */

public class StringUtil {

	/**
	 * Removes the accent of the text.
	 * 
	 * @param text
	 *            The text to remove accents.
	 * @return The text without accents.
	 */
	public static String removeAccent(String text) {
		text = text.replaceAll("[àâ]", "a");
		text = text.replaceAll("[èéêë]", "e");
		text = text.replaceAll("[ïî]", "i");
		text = text.replaceAll("Ô", "o");
		text = text.replaceAll("[ûù]", "u");

		text = text.replaceAll("[ÀÂ]", "A");
		text = text.replaceAll("[ÈÉÊË]", "E");
		text = text.replaceAll("[ÏÎ]", "I");
		text = text.replaceAll("Ô", "O");
		text = text.replaceAll("[ÛÙ]", "U");

		return text;
	}

	/**
	 * Removes the tag "pre" of the text.
	 * 
	 * @param text
	 *            The text to remove the tag "pre".
	 * @return The text without tag "pre".
	 */
	public static String removeTagPre(String text) {

		text = text.replaceAll("<[/]?pre[^>]*>", "");

		// text = text.replaceAll("#\\<\\s*code(.*?)>(.+?)<\\s*\\/code\\s*>#", "");

		// txt = text.preg_replace_callback('#\<\s*code(.*?)>(.+?)<\s*\/code\s*>#', $callback, $txt);
		return text;
	}

	/**
	 * Formats a decimal number according by the decimals places desired.
	 * 
	 * @param number
	 *            The decimal number to be formatted.
	 * @param decimalsPlacesNumber
	 *            The number of decimals places used in the formatting.
	 * @return The decimal number formatted.
	 */
	public static String formattedDecimalNumber(double number, int decimalsPlacesNumber) {
		String decimalsPlacesNumberString = decimalsPlacesNumber < 10 ? String.valueOf(decimalsPlacesNumber) : "10";
		String formattedNumber = String.format("%." + decimalsPlacesNumberString + "f", number);
		return formattedNumber.equals("-0,00") ? "0.00" : formattedNumber;
	}

	/**
	 * Formats a integer number according by the digits number desired.
	 * 
	 * @param number
	 *            The number to be formatted.
	 * @param digitsNumber
	 *            The number of digits used in the formatting.
	 * @return The number formatted.
	 */
	public static String formattedIntegerNumber(long number, int digitsNumber) {
		return String.format("%0" + digitsNumber + "d", number);
	}

	/**
	 * Formats a integer number according by the digits number desired.
	 * 
	 * @param number
	 *            The number to be formatted.
	 * @param digitsNumber
	 *            The number of digits used in the formatting.
	 * @return The number formatted.
	 */
	public static String formattedIntegerNumber(int number, int digitsNumber) {
		return StringUtil.formattedIntegerNumber((long) number, digitsNumber);
	}

}