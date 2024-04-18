package ru.netology.data;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataHelper {

    private static final Faker fakerRu = new Faker(new Locale("ru"));
    private static final Faker fakerEn = new Faker(new Locale("en"));

    private DataHelper() {
    }

    public static String getFirstCardInfo() {return ("4444 4444 4444 4441");
    }

    public static String getSecondCardInfo() {return ("4444 4444 4444 4442");
    }


    //public static String generateRandomCardNumber(){
      //  return (fakerEn.numerify("################"));
  //  }
    public static String month() {
        Random random = new Random();
        int monthValue = random.nextInt(12) + 1;
        String formattedMonth = String.format("%02d", monthValue);
        return formattedMonth;
    }
    //public static String generateMonth() {
       // Random random = new Random();
        //int monthNumber = random.nextInt(3) + 1;
       // return String.valueOf(monthNumber);
    //}
     //public static String lastMonth(String pattern){
        //LocalDate lastMonth= LocalDate.now().minusMonths(2);
        //return lastMonth.format(DateTimeFormatter.ofPattern(pattern));
    //}

    //public static String thisYear(String pattern) {
       // return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
   // }

    public static String lastYear(String pattern) {
        LocalDate lastYear = LocalDate.now().minusYears(1);
        return lastYear.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String generateYear(String pattern) {
        Year currentYear = Year.now();
        Year futureYear = currentYear.plusYears(5);
        return futureYear.format(DateTimeFormatter.ofPattern(pattern));
    }
   // public static String futureMoreThanFiveYears(String pattern) {
        //Year currentYear = Year.now();
       // Year futureYear = currentYear.plusYears(10);
       // return futureYear.format(DateTimeFormatter.ofPattern(pattern));
   // }

   // public static String randomYear() {
        //return fakerEn.numerify("#");
   // }

    public static String ownerInfo() {
        String firstname = fakerRu.name().firstName();
        String lastname = fakerRu.name().lastName();
        return firstname + " " + lastname;
    }
    public static String ownerInfoEng() {
        String firstname = fakerEn.name().firstName();
        String lastname = fakerEn.name().lastName();
        return firstname + " " + lastname;
    }
    public static String generateOwnerInfo() {
        return  fakerRu.numerify("### ##");

    }

    public static String cvcInfo() {
        return fakerRu.numerify("###");
    }
    //public static String cvcNumber(){
      //  return fakerRu.numerify("#");
   // }
    public static String cvcDoubleNumber(){
        return fakerRu.numerify("##");
    }


}











      