package exercise.controller;
import exercise.model.City;
import exercise.repository.CityRepository;
import exercise.service.WeatherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
public class CityController {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private WeatherService weatherService;

    // BEGIN
    @GetMapping(path = "/cities/{id}")
    public Map<String, String> getCity(@PathVariable Long id) {
        return weatherService.getWeather(id);
    }



    @GetMapping("search")
    public List<Map<String, String>> getCities(@RequestParam(value = "name", required = false) String name) {

        List<City> cities = name == null ?
                cityRepository.findAllByOrderByNameAsc() :
                cityRepository.findByNameStartingWithIgnoreCase(name);

        return cities.stream()
                .map(x -> {
                    Map<String, String> fullWeatherData = weatherService.getWeather(x.getId());
                    Map<String, String> weatherData = new HashMap<>();
                    weatherData.put("temperature", fullWeatherData.get("temperature"));
                    weatherData.put("name", fullWeatherData.get("name"));
                    return weatherData;
                })
                .sorted(Comparator.comparing(x -> x.get("name")))
                .toList();

    }
    // END
}

