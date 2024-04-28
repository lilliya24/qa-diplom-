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

    @Test
    @DisplayName("Покупка в кредит картой DECLINED")
    void shouldTestBuyCardForStatusDeclined() {
        creditPage.putData(DataHelper.getSecondCardInfo(), DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfoEng(), DataHelper.cvcInfo());
        creditPage.errorNotificationWait();
        Assertions.assertEquals("DECLINED", SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Тестирование формы с пустыми полями")
    public void shouldTestEmptyForm() {
        creditPage.putData("", "", "", "", "");
        creditPage.wrongCardNumberNotificationWait();
        creditPage.wrongMonthNotificationWait();
        creditPage.wrongYearNotificationWait();
        creditPage.ownerEmptyNotificationWait();
        creditPage.wrongFormatCVVNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    //Номер карты

    @Test
    @DisplayName("В поле номер карты ввести 0000 0000 0000 0000")
    public void shouldTestCardNumberNull() {
        creditPage.putData("0000 0000 0000 0000", DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfo(), DataHelper.cvcInfo());
        creditPage.errorNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());

    }

    @Test
    @DisplayName("В поле ввода номера карты ввести менее 16 символов.")
    public void shouldTestTheBuyWithA14DigitCard() {
        creditPage.putData("4444 4444 4444 444", DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfo(), DataHelper.cvcInfo());
        creditPage.wrongCardNumberNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Поле ввода «Номер карты» оставить пустым")
    public void shouldTestThePurchaseWithAnEmptyCardNumberField() {
        creditPage.putData("", DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfo(), DataHelper.cvcInfo());
        creditPage.wrongCardNumberNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());

    }

    //Месяц
    @Test
    @DisplayName("Ввести в поле  месяц значение больше 12")
    public void shouldTestMonthFieldForOverUpperBoundaryValue() {
        creditPage.putData(DataHelper.getFirstCardInfo(), "13", DataHelper.generateYear("yy"), DataHelper.ownerInfoEng(), DataHelper.cvcInfo());
        creditPage.validityErrorNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("В поле месяц ввести  00")
    public void shouldTestMonthNumber00ForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(), "00", DataHelper.generateYear("yy"), DataHelper.ownerInfoEng(), DataHelper.cvcInfo());
        creditPage.validityErrorNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());

    }
    @Test
    @DisplayName("Поле ввода «Месяц» оставить пустым")
    public void shouldTestEmptyMonthField() {
        creditPage.putData(DataHelper.getFirstCardInfo(), "", DataHelper.generateYear("yy"), DataHelper.ownerInfoEng(), DataHelper.cvcInfo());
        creditPage.wrongMonthNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    //Год
    @Test
    @DisplayName("Ввести истёкший срок действия карты.")
    public void shouldTestPatsValueForYearField() {
        creditPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), DataHelper.lastYear("yy"), DataHelper.ownerInfo(), DataHelper.cvcInfo());
        creditPage.expiredCardNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    @Test
    @DisplayName("Поле ввода «Год» оставить пустым")
    public void shouldTestEmptyYearField() {
        creditPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), "", DataHelper.ownerInfo(), DataHelper.cvcInfo());
        creditPage.wrongYearNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    //Владелец
    @Test
    @DisplayName("Ввести в поле ввода «Владелец» цифры")
    public void shouldTestHolderForDigits() {
        creditPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),DataHelper.generateOwnerInfo(),DataHelper.cvcInfo());
        creditPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    @Test
    @DisplayName("Поле ввода «Владелец» оставить пустым")
    public void shouldTestEmptyHolderField() {
        creditPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),"",DataHelper.cvcInfo());
        creditPage.ownerEmptyNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    @Test
    @DisplayName("В поле владелец ввести первую букву имени и фамилии")
    public void shouldTestOwnerWithTwoLetters() {
        creditPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),"I I",DataHelper.cvcInfo());
        creditPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    @Test
    @DisplayName("Значение владелец указывается через дефис вначале")
    public void shouldTestOwnerWithHyphenAtTheBeginning() {
        creditPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),"-" + DataHelper.ownerInfoEng() ,DataHelper.cvcInfo());
        creditPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    @Test
    @DisplayName("Значение владелец указывается через дефис в конце")
    public void shouldTestOwnerWithHyphenAtTheEnd() {
        creditPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),DataHelper.ownerInfoEng() + "-",DataHelper.cvcInfo());
        creditPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    @Test
    @DisplayName("Ввести в поле ввода «Владелец» спецсимволы")
    void shouldTestHolderForSpecialCharacters() {
        creditPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),"% &",DataHelper.cvcInfo());
        creditPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    //CVC
    @Test
    @DisplayName("В поле «CVC/CVV» ввести две цифры.")
    public void shouldTestCVCTwoDigits() {
       creditPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfo(), DataHelper.cvcDoubleNumber());
        creditPage.wrongFormatCVVNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }
    @Test
    @DisplayName("В поле «CVC/CVV» ввести 000.")
    public void shouldTestCVCNumberNull() {
        creditPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfo(), "000");
        creditPage.wrongFormatCVVNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());

    }
    @Test
    @DisplayName("Поле «CVC/CVV» оставить пустым.")
    public void shouldTestEmptyCVCField() {
        creditPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfo(), "");
        creditPage.wrongFormatCVVNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }





}

