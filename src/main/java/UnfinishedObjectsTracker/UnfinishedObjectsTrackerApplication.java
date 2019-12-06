package UnfinishedObjectsTracker;

import UnfinishedObjectsTracker.configuration.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class UnfinishedObjectsTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnfinishedObjectsTrackerApplication.class, args);
	}

}
