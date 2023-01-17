package exercise;

import exercise.daytimes.Daytime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// BEGIN
@RestController
public class WelcomeController {
    @Autowired
    private Daytime time;

    @Autowired
    private Meal meal;

    @GetMapping("/daytime")
    public String greeting() {
        String now = time.getName();
        String currentMeal = meal.getMealForDaytime(now);
        return String.format("It is %s now. Enjoy your %s", now, currentMeal);
    }
}
// END
