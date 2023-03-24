package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import ru.netology.delivery.data.DataGenerator;

import java.time.*;
import java.time.format.DateTimeFormatter;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class DeliveryTest {
    @BeforeEach
    void setupTest() {

        open("http://localhost:9999");
    }
    LocalDate initDate = DataGenerator.generateDate();
    String date = DataGenerator.getFormatDate(initDate);
    String date2 = DataGenerator.getFormatDate(initDate.plusDays(3));
    SelenideElement data = $("[data-test-id='date'] input");




    @Test
    void shouldTestFormFirstAgain() {
        $("[data-test-id='city'] input").setValue(DataGenerator.generateCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL,"a"),Keys.BACK_SPACE);
        data.setValue(date);
        $("[data-test-id='name'] input").setValue(DataGenerator.generateName());
        $("[data-test-id='phone'] input").setValue(DataGenerator.generatePhone());
        $("[data-test-id='agreement']").click();
        $$("button").find(Condition.exactText("Запланировать")).click();
        $("[data-test-id='success-notification'] button").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + date), Duration.ofSeconds(15))

                .shouldBe(Condition.visible);

        $("[data-test-id='success-notification'] button").click();
        data.sendKeys(Keys.chord(Keys.CONTROL,"a"),Keys.BACK_SPACE);
        data.click();
        data.setValue(date2);
        $$("button").find(Condition.exactText("Запланировать")).click();
        $("[data-test-id='replan-notification'] button").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='replan-notification'] .notification__content").shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id='success-notification'] button").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + date2), Duration.ofSeconds(15))

                .shouldBe(Condition.visible);
    }
}
