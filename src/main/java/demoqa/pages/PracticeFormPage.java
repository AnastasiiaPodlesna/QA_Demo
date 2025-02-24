package demoqa.pages;

import demoqa.core.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;

import java.util.Arrays;
import java.util.List;

public class PracticeFormPage extends BasePage {
    public PracticeFormPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @FindBy(id = "firstName")
    WebElement firstName;
    @FindBy(id = "lastName")
    WebElement lastName;
    @FindBy(id = "userEmail")
    WebElement userEmail;
    @FindBy(id = "userNumber")
    WebElement userNumber;

    public PracticeFormPage enterPersonalData(String name, String surName, String email, String number) {

        try {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("❌ First name is invalid: " + name);
            }
            if (surName == null || surName.trim().isEmpty()) {
                throw new IllegalArgumentException("❌ Last name is invalid: " + surName);
            }
            if (email == null || !email.contains("@")) { // простая проверка email
                throw new IllegalArgumentException("❌ Email is invalid: " + email);
            }
            if (number == null || !number.matches("\\d{10}")) { // проверка, что номер состоит из 10 цифр
                throw new IllegalArgumentException("❌ Phone number is invalid: " + number);
            }

            type(firstName, name);
            type(lastName, surName);
            type(userEmail, email);
            type(userNumber, number);

            System.out.printf("✅ Personal data: [%s], [%s], [%s], [%s]%n",name,surName,email, number);

        } catch (IllegalArgumentException e) {
            System.out.println("⛔ Error:"  + e.getMessage());
            throw new RuntimeException(e);
        }

        return this;
    }


    public PracticeFormPage selectGender(String gender) {
        try {
            String xpathGender = "//label[.='" + gender + "']";
            WebElement genderLocator = driver.findElement(By.xpath(xpathGender));
            click(genderLocator);
            System.out.printf("✅ Gender selected: [%s]%n", gender);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("⛔ Gender element not found: [" + gender + "]. " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("❌ Error selecting gender: [" + gender + "]. " + e);
        }
        return this;
    }

    @FindBy(id = "dateOfBirthInput")
    WebElement dateOfBirthInput;

    public PracticeFormPage chooseDateAsString(String date) {
        //type(dateOfBirthInput, date);
        click(dateOfBirthInput);

        if (System.getProperty("os.name").contains("Mac")){
            dateOfBirthInput.sendKeys(Keys.COMMAND, "a");
        } else {
            dateOfBirthInput.sendKeys(Keys.CONTROL, "a");
        }
        dateOfBirthInput.sendKeys(date);
        dateOfBirthInput.sendKeys(Keys.ENTER);
        return this;
    }
    //----------------- передаем дату как три строковых значения------------------------------------

    public PracticeFormPage chooseDateAsThreeString(String day, String month, String year) {
        try {
            // Проверка на null или пустую строку
            if (day == null || day.isEmpty() || month == null || month.isEmpty() || year == null || year.isEmpty()) {
                throw new IllegalArgumentException("Date parameters cannot be null or empty");
            }

            // нужно убрать нуль в передаваемой строке даты "04"
            day = String.valueOf(Integer.parseInt(day));

            // день (от 1 до 31)
            if (!day.matches("^(0?[1-9]|[12][0-9]|3[01])$")) {
                throw new IllegalArgumentException("Day must be a valid number between 1 and 31");
            }

            //  месяц
            if (!month.matches("^(January|February|March|April|May|June|July|August|September|October|November|December)$")) {
                throw new IllegalArgumentException("Month must be a valid month name");
            }

            // год (например, от 1900 до текущего года)
            int currentYear = java.time.Year.now().getValue();
            if (!year.matches("^(19\\d{2}|20[0-9]{2})$") || Integer.parseInt(year) < 1900 || Integer.parseInt(year) > currentYear) {
                throw new IllegalArgumentException("Year must be between 1900 and " + currentYear);
            }

            click(dateOfBirthInput); // активируем поле для ввода даты

            // выбираем месяц
            WebElement monthSelect = driver.findElement(By.className("react-datepicker__month-select"));
            Select monthDropdown = new Select(monthSelect);
            monthDropdown.selectByVisibleText(month);

            // выбираем год
            WebElement yearSelect = driver.findElement(By.className("react-datepicker__year-select"));
            Select yearDropdown = new Select(yearSelect);
            yearDropdown.selectByVisibleText(year);

            // ищем и кликаем по нужному числу
            WebElement dayElement = driver.findElement(By.xpath("//div[contains(@class, 'react-datepicker__day') and text()='" + day + "']"));
            dayElement.click();

            return this;

        } catch (IllegalArgumentException e) {
            System.out.println("Error in date parameters: " + e.getMessage());
            throw e; // перекидываем исключение дальше, если нужно
        } catch (Exception e) {
            System.out.println("An error occurred while selecting the date: " + e.getMessage());
            throw new RuntimeException("Failed to select the date", e); // ловим другие ошибки
        }
    }
    //---------------------------------------------------------------------------------------------------


    @FindBy(id = "subjectsInput")
    WebElement subjectsInput;

    public PracticeFormPage enterSubjects(String[] subjects) {
        try {
            // проверяем на null или пустой массив
            if (subjects == null || subjects.length == 0) {
                throw new IllegalArgumentException("Subjects array cannot be null or empty");
            }

            // проверяем каждый элемент в массиве
            for (String subject : subjects) {
                if (subject == null || subject.isEmpty()) {
                    throw new IllegalArgumentException("Subject cannot be null or empty");
                }

                type(subjectsInput, subject);
                subjectsInput.sendKeys(Keys.ENTER);

                System.out.printf("✅ Subject: [%s]%n", subject);
            }

            return this;

        } catch (IllegalArgumentException e) {
            System.out.println("Error in subjects: " + e.getMessage());
            throw e; // перекидываем исключение дальше, если нужно
        } catch (Exception e) {
            System.out.println("An error occurred while entering subjects: " + e.getMessage());
            throw new RuntimeException("Failed to enter subjects", e); // ловим другие ошибки
        }
    }

    public PracticeFormPage chooseHobbies(String[] hobbies) {
        try {
            // проверяем на null или пустой массив
            if (hobbies == null || hobbies.length == 0) {
                throw new IllegalArgumentException("Hobbies array cannot be null or empty");
            }

            // создаем список допустимых хобби
            List<String> validHobbies = Arrays.asList("Sports", "Reading", "Music");

            // Проверка каждого хобби
            for (String hobby : hobbies) {
                // Выводим значение хобби, чтобы понять, что передается в метод
                System.out.printf("Checking hobby: [%s]%n", hobby);
            }

            // далее проверяем каждое хобби на null или пустую строку
            for (String hobby : hobbies) {
                if (hobby == null || hobby.isEmpty()) {
                    throw new IllegalArgumentException("Hobby cannot be null or empty");
                }
                // проверяем, что бы хобби было из допустимых значений
                if (!validHobbies.contains(hobby)) {
                    throw new IllegalArgumentException("Invalid hobby: " + hobby);
                }

                // локатор для выбора хобби
                By hobbyLocator = By.xpath("//label[.='" + hobby + "']");
                WebElement element = driver.findElement(hobbyLocator);
                element.click(); // кликаем на нужное хобби

                System.out.printf("✅ Hobby: [%s]%n", hobby);
            }

            return this;

        } catch (IllegalArgumentException e) {
            System.out.println("Error in hobbies: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("An error occurred while selecting hobbies: " + e.getMessage());
            throw new RuntimeException("Failed to select hobbies", e);
        }
    }

    @FindBy(id = "uploadPicture")
    WebElement uploadPicture;

    public PracticeFormPage uploadPicture(String imgPath) {
        try {
            // проверяем на null или пустую строку
            if (imgPath == null || imgPath.isEmpty()) {
                throw new IllegalArgumentException("Image path cannot be null or empty");
            }

            // проверяем, есть ли такой файл
            File file = new File(imgPath);
            if (!file.exists()) {
                throw new IllegalArgumentException("File does not exist at the specified path: " + imgPath);
            }

            uploadPicture.sendKeys(imgPath);
            System.out.printf("✅ Image: [%s]%n", imgPath);
            return this;

        } catch (IllegalArgumentException e) {
            System.out.println("Error in image path: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("An error occurred while uploading image: " + e.getMessage());
            throw new RuntimeException("Failed to upload image", e);
        }
    }

}





