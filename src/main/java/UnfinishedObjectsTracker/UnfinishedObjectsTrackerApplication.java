package UnfinishedObjectsTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableConfigurationProperties({
//		FileStorageProperties.class
//})
public class UnfinishedObjectsTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnfinishedObjectsTrackerApplication.class, args);
	}

}
