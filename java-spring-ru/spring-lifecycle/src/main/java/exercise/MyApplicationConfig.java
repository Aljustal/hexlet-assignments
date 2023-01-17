package exercise;

import java.time.LocalDateTime;

import exercise.daytimes.Daytime;
import exercise.daytimes.Morning;
import exercise.daytimes.Day;
import exercise.daytimes.Evening;
import exercise.daytimes.Night;

// BEGIN
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyApplicationConfig {
    @Bean
    public Daytime currentDayTime() {
        int currentTime = LocalDateTime.now().getHour();

        boolean isMorning = currentTime >= 6 && currentTime < 12;
        boolean isDay = currentTime >= 12 && currentTime < 18;
        boolean isEvening = currentTime >= 18 && currentTime < 23;

        if (isMorning) {
            return new Morning();
        }
        if (isDay) {
            return new Day();
        }
        if (isEvening) {
            return new Evening();
        }
        return new Night();
    }
}
// END
