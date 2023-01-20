package online.mdfactory.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class BackendApplication {
	public static void main(String[] args) {
		logConnectionInfo();
		/*List<String> arguments = new ArrayList<>(Arrays.asList(args));
		arguments.add("--add-opens java.base/java.time=ALL-UNNAMED");*/
		SpringApplication.run(BackendApplication.class, args);
	}

	private static void logConnectionInfo() {
		try {
			Logger logger = LoggerFactory.getLogger(BackendApplication.class);
			logger.info(InetAddress.getLocalHost().toString());
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

}
