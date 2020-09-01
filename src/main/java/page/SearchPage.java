package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import entity.Car;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import sort.SortingType;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;

@Slf4j
public class SearchPage extends AbstractPage {

    private final SelenideElement pageSize = $(By.xpath("//select[@data-qa-selector='select' and @name='pageSize']"));
    private final SelenideElement resultAmount = $(By.xpath("//div[@class='resultsAmount___3OrV7']"));
    private final SelenideElement registrationYearFilter = $(By.xpath("//span[contains(text(),'Erstzulassung ab')]"));
    private final ElementsCollection yearSelector = $$(By.xpath("//select[@data-qa-selector='select' and @name='yearRange.min']//option"));
    private final SelenideElement sortListSelector = $(By.xpath("//select[@data-qa-selector='select' and @name='sort']"));
    private final ElementsCollection carDetailsTitle = $$(By.xpath("//div[@data-qa-selector='results-found']//*//div[@data-qa-selector='title']")); //this is redundant - used for checking if cars were properly loaded
    private final ElementsCollection carDetailsRegistrationYear = $$(By.xpath("//div[@data-qa-selector='results-found']//*//ul[@data-qa-selector='spec-list']//li[1]"));
    private final ElementsCollection carDetailsPrice = $$(By.xpath("//div[@data-qa-selector='results-found']//*//span[@data-qa-selector='price']"));
    private final SelenideElement goLastPage = $(By.xpath("//ul[@class='pagination']//li[last()]"));
    private final SelenideElement goNextPage = $(By.xpath("//span[@aria-label='Next']"));

    public SearchPage(final String pageURL) {
        super(pageURL);
    }

    public void filterByRegistrationYear(final String registrationYear) {
        registrationYearFilter.click();
        SelenideElement element = yearSelector.filter(Condition.text(registrationYear)).first();
        element.click();
        waitForElements();
    }

    public void chooseSortingType(final SortingType sortingType) {
        sortListSelector.click();
        $(sortingType.value).click();
        waitForElements();
    }

    private void waitForElements() {
        resultAmount.shouldNotHave(Condition.text("Lädt..."));
    }

    private int getFilterResultAmount() {
        return Integer.parseInt(resultAmount.text().replace(" Treffer", ""));
    }

    public List<Car> getFilteredCars() {
        List<Car> matchingCars = new ArrayList<>();
        final int filtersResult = getFilterResultAmount();
        log.info("All items = {}", filtersResult);
        int pageSizeVal = Integer.parseInt(pageSize.text());
        log.info("Page size: {}", pageSizeVal);
        addMatchingCars(carDetailsTitle, carDetailsRegistrationYear, carDetailsPrice, matchingCars);
        //if result amount is less than pageSize we do not want to use pagination mechanism, all matching elements should be loaded.
        if (filtersResult > pageSizeVal) {
            while (!goLastPage.getAttribute("class").equals("disabled")) {
                log.info("Switching page!");
                goNextPage.click();

                //TODO change this to non static wait based on page loading or some element
                sleep(500);

                addMatchingCars(carDetailsTitle, carDetailsRegistrationYear, carDetailsPrice, matchingCars);
            }
        }
        if (filtersResult != matchingCars.size()) {
            log.error("Something gone wrong... Matching cars amount is different than filtered result amount!");
        }
        return matchingCars;
    }

    private void addMatchingCars(final ElementsCollection title, final ElementsCollection year, final ElementsCollection price, final List<Car> cars) {
        String titleVal;
        String registrationYearVal;
        double priceVal;
        for (int x = 0; x < year.size(); x++) {
            titleVal = title.get(x).text(); // this is redundant - used for checking if cars were properly loaded
            registrationYearVal = year.get(x).text().substring(5);
            priceVal = Double.parseDouble(price.get(x).text().replace(" €", ""));
            cars.add(new Car(titleVal, registrationYearVal, priceVal));
        }
    }
}
