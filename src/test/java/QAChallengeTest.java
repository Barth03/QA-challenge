import entity.Car;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import page.SearchPage;
import sort.SortingType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class QAChallengeTest {

    private final String REGISTRATION_YEAR = "2015";
    private final SortingType SORTING_TYPE = SortingType.ORDER_BY_PRICE_DESC;
    private final String SEARCH_PAGE_URL = "de/search/";

    @Test
    public void qaChallengeTest() {

        SearchPage page = new SearchPage(SEARCH_PAGE_URL);
        page.openPage();
        page.filterByRegistrationYear(REGISTRATION_YEAR);
        page.chooseSortingType(SORTING_TYPE);
        List<Car> cars = page.getFilteredCars();
        //cars.forEach(x -> log.info(x.toString()));
        assertThat(checkIfYearsUnderValue(REGISTRATION_YEAR, cars)).isFalse();
        assertThat(checkIfCarsAreSorted(cars)).isTrue();

    }

    private boolean checkIfYearsUnderValue(final String registrationYear, final List<Car> cars) {
        return cars.stream()
                .anyMatch(x -> (Integer.parseInt(x.getRegistrationYear()) < Integer.parseInt(registrationYear)));
    }

    private boolean checkIfCarsAreSorted(final List<Car> cars) {
        List<Car> copyToCompare = new ArrayList<>(cars);
        cars.sort(Comparator.comparing(Car::getPrice).reversed());
        return copyToCompare.equals(cars);
    }
}
