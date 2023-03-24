// Модель

public class Car {

    // Идентификатор будет генерироваться автоматически
    @Id
    private long id;

    private String model;

    private String color;

}

// Репозиторий

@Repository
// Реализуем интерфейс ReactiveCrudRepository
public interface CarRepository extends ReactiveCrudRepository<Car, Integer> {
    // Все необходимые методы уже содержатся в интерфейсе ReactiveCrudRepository
}

// Сервис

@Service
public class CarService {

    // Сервис использует репозиторий
    @Autowired
    CarRepository carRepository;

    // Из методов сервиса возвращаем типы Reactor Flux<T> или Mono<T>
    public Flux<Car> findAll() {
        return carRepository.findAll();
    }

    public Mono<Car> create(Car car) {
        return carRepository.save(car);
    }

    public Mono<Car> findById(int carId) {
        return carRepository.findById(carId);
    }
}

// Контроллер

@RestController
@RequestMapping("/cars")
public class CarController {

    // В контроллере используем методы сервиса
    @Autowired
    private CarService carService;

    @GetMapping(path = "")
    public Flux<Car> getCars() {
        return carService.findAll();
    }

    @GetMapping(path = "/{id}")
    public Mono<Car> getCar(@PathVariable int id) {
        return carService.findById(id);
    }

    @PostMapping(path = "")
    public Mono<Car> createCar(@RequestBody Car car) {
        return carService.create(car);
    }
}
