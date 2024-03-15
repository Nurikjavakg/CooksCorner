package neo.cookscorner.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        String cloudName = "dpci5s3tm";
        String apiKey = "357384328671388";
        String apiSecret = "r0bJL2Y4Leu68Hcm31F_mzfbQ9U";

        if (cloudName == null || apiKey == null || apiSecret == null) {
            throw new IllegalArgumentException("Cloudinary environment variables not set");
        }

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        return new Cloudinary(config);
    }
}
