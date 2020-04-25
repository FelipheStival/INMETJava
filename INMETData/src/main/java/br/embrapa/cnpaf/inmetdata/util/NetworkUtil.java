package br.embrapa.cnpaf.inmetdata.util;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * <br>
 * <p>
 * <b>Utility class with methods for manipulating network functions.</b>
 * </p>
 * <br>
 * 
 * @author Sergio Lopes Jr. and Rubens de Castro Pereira.
 * @version 0.1
 * @since 03/03/2020 (creation date)
 * 
 */
public class NetworkUtil {

	private static String localIpAddress = null;
	private static String localMacAddress = null;

	/**
	 * Retrieves the IP address of the local host.<br>
	 * Source: https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java<br>
	 * Consulted in 25/05/20017.
	 * 
	 * @return IP address of the local host.
	 */
	public static String getLocalIpAddress() {
		if (localIpAddress != null) {
			return localIpAddress;
		}
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				for (InterfaceAddress interfaceAddress : networkInterfaces.nextElement().getInterfaceAddresses())
					if (interfaceAddress.getAddress().isSiteLocalAddress()) {
						localIpAddress = interfaceAddress.getAddress().getHostAddress();
						return localIpAddress;
					}
			}
		} catch (SocketException e) {
		}
		return null;
	}

	/**
	 * Retrieves the mac address of the local host.<br>
	 * Source: https://www.mkyong.com/java/how-to-get-mac-address-in-java/<br>
	 * Consulted in 02/06/20017.
	 * 
	 * @return IP address of the local host.
	 */
	public static String getLocalMacAddress() {
		if (localMacAddress != null) {
			return localMacAddress;
		}
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			NetworkInterface networkInterface;
			while (networkInterfaces.hasMoreElements()) {
				networkInterface = networkInterfaces.nextElement();
				for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses())
					if (interfaceAddress.getAddress().isSiteLocalAddress()) {

						byte[] macAddress = networkInterface.getHardwareAddress();
						StringBuilder formattedMacAddress = new StringBuilder();
						for (int i = 0; i < macAddress.length; i++) {
							formattedMacAddress.append(String.format("%02X%s", macAddress[i], (i < macAddress.length - 1) ? "-" : ""));
						}
						localMacAddress = formattedMacAddress.toString();
						return localMacAddress;

					}
			}
		} catch (SocketException e) {
		}
		return null;
	}
}
