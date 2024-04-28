package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreditPage {
    private final SelenideElement heading;
    private SelenideElement cardNumber = $(".input [placeholder = '0000 0000 0000 0000']");
    private SelenideElement monthField = $(".input [placeholder = '08']");
    private SelenideElement yearField = $(".input [placeholder = '22']");
    private SelenideElement ownerField = $$(".input__control").get(3);
    private SelenideElement CVCField = $(".input [placeholder = '999']");

    private final SelenideElement successMessage;
    private final SelenideElement errorMessage;

    private SelenideElement incorrectFormat = $(byText("Неверный формат"));
    private SelenideElement validityError =$(byText("Неверно указан срок действия карты"));
    private SelenideElement cardExpiredError = $(byText("Истёк срок действия карты"));
    private SelenideElement emptyOwnerError = $(byText("Поле обязательно для заполнения"));
    private SelenideElement continueButton = $$("button span.button__text").find(exactText("Продолжить"));

    public CreditPage() {
        heading = $$("h3").get(1).shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Кредит по данным карты"));
        successMessage = $$(".notification__content").find(text("Операция одобрена Банком."));
        errorMessage = $$(" .notification__content").find(text("Ошибка! Банк отказал в проведении операции."));
        heading.shouldBe(visible);
    }
    public void putData(String number, String month, String year, String owner, String CVC) {
        cardNumber.setValue(number);
        monthField.setValue(month);
        yearField.setValue(year);
        ownerField.setValue(owner);
        CVCField.setValue(CVC);
        continueButton.click();
    }
    public void successNotificationWait(){
        successMessage.shouldBe(visible,Duration.ofSeconds(20));
    }
    public void errorNotificationWait(){
        errorMessage.shouldBe(visible,Duration.ofSeconds(20));
    }
    public void wrongCardNumberNotificationWait(){
        incorrectFormat.shouldBe(visible);
    }
    public void wrongMonthNotificationWait(){
        incorrectFormat.shouldBe(visible);
    }
    public void wrongYearNotificationWait(){
        incorrectFormat.shouldBe(visible);
    }
    public void validityErrorNotificationWait(){
        validityError.shouldBe(visible);
    }
    public void expiredCardNotificationWait(){
        cardExpiredError.shouldBe(visible);
    }
    public void ownerEmptyNotificationWait(){
        emptyOwnerError.shouldBe(visible);
    }
    public void incorrectFormatOwnerNotificationWait(){
        incorrectFormat.shouldBe(visible);
    }
    public void wrongFormatCVVNotificationWait(){
        incorrectFormat.shouldBe(visible);
    }
}