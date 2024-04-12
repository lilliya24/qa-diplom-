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
        buyPage.successNotificationWait();
        Assertions.assertEquals("DECLINED", SQLHelper.geStatusInData());
    }
    @Test
    @DisplayName("Тестирование формы с пустыми полями")
    public void shouldTestEmptyForm() {
        buyPage.putData("","","","","");
        buyPage.wrongCardNumberNotificationWait();
        buyPage.wrongMonthNotificationWait();
        buyPage.wrongYearNotificationWait();
        buyPage.ownerEmptyNotificationWait();
        buyPage.wrongFormatCVVNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    //Номер карты

    @Test
    @DisplayName("В поле номер карты ввести 0000 0000 0000 0000")
    public void shouldTestCardNumberNull() {
        buyPage.putData("0000 0000 0000 0000", DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfo(), DataHelper.cvcInfo());
        buyPage.errorNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());

    }
    @Test
    @DisplayName("В поле ввода номера карты ввести менее 16 символов.")
    public void shouldTestTheBuyWithA14DigitCard() {
        buyPage.putData("4444 4444 4444 444", DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfo(), DataHelper.cvcInfo());
        buyPage.wrongCardNumberNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    @Test
    @DisplayName("Поле ввода номера карты оставить пустым")
    void shouldTestThePurchaseWithAnEmptyCardNumberField() {
        buyPage.putData("", DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfo(), DataHelper.cvcInfo());
        buyPage.wrongCardNumberNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }


    //Месяц
    @Test
    @DisplayName("Ввести в поле  месяц значение больше 12.")
    public void shouldTestMonthFieldForOverUpperBoundaryValue() {
        buyPage.putData(DataHelper.getFirstCardInfo(),"13",DataHelper.generateYear("yy"),DataHelper.ownerInfoEng(),DataHelper.cvcInfo());
        buyPage.validityErrorNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    @Test
    @DisplayName("Month value is 00")
    public void shouldTestMonthNumber00() {
        buyPage.putData(DataHelper.getFirstCardInfo(),"00",DataHelper.generateYear("yy"),DataHelper.ownerInfoEng(),DataHelper.cvcInfo());
        buyPage.errorNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    @Test
    @DisplayName("Поле Месяц оставить пустым.")
    public void shouldTestMonthValueNull() {
        buyPage.putData(DataHelper.getFirstCardInfo(), "", DataHelper.generateYear("yy"), DataHelper.ownerInfoEng(), DataHelper.cvcInfo());
        buyPage.wrongMonthNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    //Год
    @Test
    @DisplayName("Ввести истёкший срок действия карты.")
    public void shouldTestPatsValueForYearField() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.lastYear("yy"),DataHelper.ownerInfo(),DataHelper.cvcInfo());
        buyPage.expiredCardNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    @Test
    @DisplayName("Поле ввода «Год» оставить пустым")
    void shouldTestEmptyYearField() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.lastYear(""),DataHelper.ownerInfo(),DataHelper.cvcInfo());
        buyPage.wrongYearNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    //Владелец
    @Test
    @DisplayName("Поле ввода «Владелец» оставить пустым")
    void shouldTestEmptyHolderField() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),"",DataHelper.cvcInfo());
        buyPage.ownerEmptyNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    @Test
    @DisplayName("Ввести в поле ввода «Владелец» цифры")
    void shouldTestHolderForDigits() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),DataHelper.generateOwnerInfo(),DataHelper.cvcInfo());
        buyPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    @Test
    @DisplayName("В поле владелец ввести первую циыру имени и фамилии")
    public void shouldTestOwnerWithTwoLetters() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),"S H",DataHelper.cvcInfo());
        buyPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    @Test
    @DisplayName("Значение владелец указывается через дефис вначале")
    public void shouldTestOwnerWithHyphenAtTheBeginning() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),"-" + DataHelper.ownerInfoEng() ,DataHelper.cvcInfo());
        buyPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    @Test
    @DisplayName("Значение владелец указывается через дефис в конце")
    public void shouldTestOwnerWithHyphenAtTheEnd() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),DataHelper.ownerInfoEng() + "-",DataHelper.cvcInfo());
        buyPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    @Test
    @DisplayName("Ввести в поле ввода «Владелец» спецсимволы")
    void shouldTestHolderForSpecialCharacters() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),"% &",DataHelper.cvcInfo());
        buyPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }


    //CVC
    @Test
    @DisplayName("В поле «CVC/CVV» ввести две цифры.")
    public void shouldTestCVCTwoDigits() {
        buyPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfo(), DataHelper.cvcDoubleNumber());
        buyPage.wrongFormatCVVNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }


    @Test
    @DisplayName("В поле «CVC/CVV» ввести 000.")
    public void shouldTestCVCNumberNull() {
        buyPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfo(), "000");
        buyPage.wrongFormatCVVNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());

    }

    @Test
    @DisplayName("Поле «CVC/CVV» оставить пустым.")
    public void shouldTestEmptyCVCField() {
        buyPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfo(), "");
        buyPage.wrongFormatCVVNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
}

