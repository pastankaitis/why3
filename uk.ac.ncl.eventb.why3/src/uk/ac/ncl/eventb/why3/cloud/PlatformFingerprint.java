package uk.ac.ncl.eventb.why3.cloud;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.ncl.eventb.why3.main.Why3Plugin;

public class PlatformFingerprint {

	private static final Pattern MAC_ADDRESS = Pattern
			.compile(
					"((?:[A-F0-9]{1,2}[:-]){5}[A-F0-9]{1,2})|(?:0x)(\\d{12})(?:.+ETHER)",
					Pattern.CASE_INSENSITIVE);
					
	private static String macAddress = null;
	private static long uuid = 0;
	private static int bits = 0;

	static {

		try {
			Class.forName("java.net.InterfaceAddress");
			macAddress = Class
					.forName("com.eaio.uuid.UUIDGen$HardwareAddressLookup")
					.newInstance().toString();
		} catch (ExceptionInInitializerError err) {
			// Ignored.
		} catch (ClassNotFoundException ex) {
			// Ignored.
		} catch (LinkageError err) {
			// Ignored.
		} catch (IllegalAccessException ex) {
			// Ignored.
		} catch (InstantiationException ex) {
			// Ignored.
		} catch (SecurityException ex) {
			// Ignored.
		}

		if (macAddress == null) {

			Process p = null;
			BufferedReader in = null;

			try {
				String osname = System.getProperty("os.name", ""), osver = System
						.getProperty("os.version", "");

				if (osname.startsWith("Windows")) {
					p = Runtime.getRuntime().exec(
							new String[] { "ipconfig", "/all" }, null);
				}

				// Solaris code must appear before the generic code
				else if (osname.startsWith("Solaris")
						|| osname.startsWith("SunOS")) {
					if (osver.startsWith("5.11")) {
						p = Runtime.getRuntime().exec(
								new String[] { "dladm", "show-phys", "-m" },
								null);
					} else {
						String hostName = getFirstLineOfCommand("uname", "-n");
						if (hostName != null) {
							p = Runtime.getRuntime().exec(
									new String[] { "/usr/sbin/arp", hostName },
									null);
						}
					}
				} else if (new File("/usr/sbin/lanscan").exists()) {
					p = Runtime.getRuntime().exec(
							new String[] { "/usr/sbin/lanscan" }, null);
				} else if (new File("/sbin/ifconfig").exists()) {
					p = Runtime.getRuntime().exec(
							new String[] { "/sbin/ifconfig", "-a" }, null);
				}

				if (p != null) {
					in = new BufferedReader(new InputStreamReader(
							p.getInputStream()), 128);
					String l = null;
					while ((l = in.readLine()) != null) {
						macAddress = parse(l);
						if (macAddress != null
								&& Hex.parseShort(macAddress) != 0xff) {
							break;
						}
					}
				}

			} catch (SecurityException ex) {
				// Ignore it.
			} catch (IOException ex) {
				// Ignore it.
			} finally {
				if (p != null) {
					try {
						in.close();
						p.getErrorStream().close();
						p.getOutputStream().close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					p.destroy();
				}
			}

		}

		if (macAddress != null)
			mixinValue(macAddress, 32);

		mixinProperty("user.name", 6);
		mixinProperty("user.home", 6);
		mixinProperty("user.dir", 6);
		mixinProperty("java.home", 2);
		mixinProperty("java.class.path", 2);
		mixinProperty("os.version", 2);
		try {
			mixinValue(InetAddress.getLocalHost().getHostName(), 4);
		} catch (Throwable e) {
		}
		mixinValue(Why3Plugin.getWhy3LocalPath(), 2);
	}
	
	public static long getUUID() {
		return uuid;
	}

	public static int getBits() {
		return bits;
	}

	private static void mixinProperty(String property, int weight) {
		
		if (System.getProperty(property) != null) {
			long mask = (~0L) >>> (64 - weight);
			try {
				uuid |= (((long) System.getProperty(property).hashCode()) & mask) << bits;
			} catch (Throwable e) {
				// suppress
			}
			bits += weight;
		}
	}

	private static void mixinValue(String value, int weight) {
		if (value != null) {
			long mask = (~0L) >>> (64 - weight);
			try {
				uuid |= ( ((long) value.hashCode()) & mask) << bits;
			} catch (Throwable e) {
				// suppress
			}
			bits += weight;
		}
	}

	/**
	 * Returns the first line of the shell command.
	 * 
	 * @param commands
	 *            the commands to run
	 * @return the first line of the command
	 * @throws IOException
	 */
	static String getFirstLineOfCommand(String... commands) throws IOException {

		Process p = null;
		BufferedReader reader = null;

		try {
			p = Runtime.getRuntime().exec(commands);
			reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()), 128);

			return reader.readLine();
		} finally {
			if (p != null) {
				try {
					reader.close();
					p.getErrorStream().close();
					p.getOutputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				p.destroy();
			}
		}

	}

					
	/**
	 * Attempts to find a pattern in the given String.
	 *
	 * @param in
	 *            the String, may not be <code>null</code>
	 * @return the substring that matches this pattern or <code>null</code>
	 */
	private static String parse(String in) {
		Matcher m = MAC_ADDRESS.matcher(in);
		if (m.find()) {
			String g = m.group(2);
			if (g == null) {
				g = m.group(1);
			}
			return g == null ? g : g.replace('-', ':');
		}
		return null;
	}
}
