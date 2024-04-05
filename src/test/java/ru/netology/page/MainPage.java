package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {
    private SelenideElement heading = $$("h2").find(Condition.text("Путешествие дня"));
    private SelenideElement buyButton = $$("button span.button__text").find(exactText("Купить"));
    private SelenideElement creditButton = $$("button span.button__text").find(exactText("Купить в кредит"));

    public MainPage(){
        heading.shouldBe(visible);
    }

    public BuyPage goToBuyPage(){
        buyButton.click();
        return new BuyPage();

    }

    public CreditPage goToCreditPage(){
        creditButton.click();
        return new CreditPage();
    }



}

