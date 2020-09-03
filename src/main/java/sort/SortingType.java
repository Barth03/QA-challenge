package sort;

import lombok.Getter;
import org.openqa.selenium.By;

public enum SortingType {
    ORDER_BY_PRICE_DESC(By.xpath("//option[@data-qa-selector-value='offerPrice.amountMinorUnits.desc']")),
    ORDER_BY_PRICE_ASC(By.xpath("//option[@data-qa-selector-value='offerPrice.amountMinorUnits.asc']")),
    ORDER_BY_MILLEAGE_DESC(By.xpath("//option[@data-qa-selector-value='mileage.distance.desc']")),
    ORDER_BY_MILLEAGE_ASC(By.xpath("//option[@data-qa-selector-value='mileage.distance.asc']")),
    ORDER_BY_NAME_DESC(By.xpath("//option[@data-qa-selector-value='manufacturer.desc']")),
    ORDER_BY_NAME_ASC(By.xpath("//option[@data-qa-selector-value='manufacturer.asc']"));

    @Getter
    private final By value;

    SortingType(final By value) {
        this.value = value;
    }
}
