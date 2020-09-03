package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import entity.Car;
import exception.FailedLoadResultException;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import sort.SortingType;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


@Slf4j
public class SearchPage extends AbstractPage {

    private final SelenideElement pageSize = $(By.xpath("//select[@data-qa-selector='select' and @name='pageSize']"));
    private final SelenideElement resultAmount = $(By.xpath("//div[@data-qa-selector='results-amount']"));
    private final SelenideElement registrationYearFilter = $(By.xpath("//div[@data-qa-selector='filter-year']"));
    private final SelenideElement sortListSelector = $(By.xpath("//select[@data-qa-selector='select' and @name='sort']"));
    private final SelenideElement goNextPage = $(By.xpath("//span[@aria-label='Next']"));
    private final SelenideElement goLastPage = $(By.xpath("//ul[@class='pagination']//li[last()]"));
    private final ElementsCollection carsRegistrationYear = $$(By.xpath("//div[@data-qa-selector='results-found']//*//ul[@data-qa-selector='spec-list']//li[1]"));
    private final ElementsCollection carsPrice = $$(By.xpath("//div[@data-qa-selector='results-found']//*//span[@data-qa-selector='price']"));

    public SearchPage(final String pageURL) {
        super(pageURL);
    }

    public void filterByRegistrationYear(final String registrationYear) {
        registrationYearFilter.click();
        SelenideElement yearSelector = $(By.xpath("//option[@data-qa-selector-value='" + registrationYear + "']"));
        yearSelector.click();
        waitForFilters();
    }

    public void chooseSortingType(final SortingType sortingType) {
        sortListSelector.click();
        $(sortingType.getValue()).click();
        waitForFilters();
    }

    private void waitForFilters() {
        resultAmount.shouldNotHave(Condition.text("Lädt..."));
    }

    private int getFilterResultAmount() {
        return Integer.parseInt(resultAmount.text().split(" ")[0]);
    }

    public List<Car> getFilteredCars() throws FailedLoadResultException {

        List<Car> matchingCars = new ArrayList<>();
        final int filtersResult = getFilterResultAmount();
        int pageSizeVal = Integer.parseInt(pageSize.text());
        log.info("Page size: {}", pageSizeVal);

        addMatchingCars(carsRegistrationYear, carsPrice, matchingCars);

        if (filtersResult > pageSizeVal) {
            while (!goLastPage.getAttribute("class").equals("disabled")) {
                goNextPage.click();
                waitForPage();
                addMatchingCars(carsRegistrationYear, carsPrice, matchingCars);
            }
        }
        if (filtersResult != matchingCars.size()) {
            throw new FailedLoadResultException("Getting result from page failed");
        } else {
            log.info("All cars = {}", filtersResult);
        }
        return matchingCars;
    }

    private void addMatchingCars(final ElementsCollection year, final ElementsCollection price, final List<Car> cars) {
        String registrationYearVal;
        double priceVal;
        for (int x = 0; x < year.size(); x++) {
            registrationYearVal = year.get(x).text().split("/")[1];
            priceVal = Double.parseDouble(price.get(x).text().split(" ")[0]);
            cars.add(new Car(registrationYearVal, priceVal));
        }
    }
}
