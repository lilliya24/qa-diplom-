package ru.netology.test;


import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.CreditPage;
import ru.netology.page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.SQLHelper.cleanDatabase;

public class TicketBuyByCreditTest {

    MainPage mainPage;
    CreditPage creditPage;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
        mainPage = new MainPage();
        creditPage = mainPage.goToCreditPage();
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
    @DisplayName("Покупка в кредит картой APPROVED")
    void shouldTestBuyCardForStatusApproved() {
        creditPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfoEng(), DataHelper.cvcInfo());
        creditPage.successNotificationWait();
        Assertions.assertEquals("APPROVED", SQLHelper.getStatusForCreditForm());
    }




}
