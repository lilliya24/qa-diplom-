package ru.netology.test;


import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.BuyPage;
import ru.netology.page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.SQLHelper.cleanDatabase;


public class TicketBuyerTest {
    MainPage mainPage;
    BuyPage buyPage;


    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
        mainPage = new MainPage();
        buyPage = mainPage.goToBuyPage();
    }

    @AfterEach
    void TearDownAll() {
        cleanDatabase();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Покупка картой «APPROVED»")
    public void shouldTestBuyCardForStatusApproved() {
        buyPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfoEng(), DataHelper.cvcInfo());
        buyPage.successNotificationWait();
        Assertions.assertEquals("APPROVED", SQLHelper.geStatusInData());
    }


    @Test
    @DisplayName("Покупка картой «DECLINED»")
    public void shouldTestNegativeForCardNumber() {
        buyPage.putData(DataHelper.getSecondCardInfo(), DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfoEng(), DataHelper.cvcInfo());
        buyPage.errorNotificationWait();
        Assertions.assertEquals("DECLINED", SQLHelper.geStatusInData());

    }
}
