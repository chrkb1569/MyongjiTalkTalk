package com.example.demo.service.crawling;

import com.example.demo.dto.crawling.NoticeDto;
import com.example.demo.dto.crawling.ScheduleDto;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

@Service
public class CrawlingService {

    private final String noticeURL = "https://www.mju.ac.kr/mjukr/255/subview.do";
    private final String scheduleURL = "https://www.mju.ac.kr/gs/1785/subview.do";

    Path path = Paths.get("/usr/bin/chromedriver");

    @Transactional(readOnly = true)
    public List<NoticeDto> getNotice() {
        List<NoticeDto> noticeDtoList = new LinkedList<>();

        System.setProperty("webdriver.chrome.driver", path.toString());

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");   // 팝업 안띄움
        options.addArguments("headless");   // 브라우저 안띄움
        options.addArguments("--disable-gpu");  // gpu 비활성화
        options.addArguments("--blink-settings=imagesEnabled=false");   // 이미지 다운 안받음

        WebDriver driver = new ChromeDriver(options);

        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));    // 드라이버가 실행된 후 10초 기다림

        driver.get(noticeURL);

        webDriverWait.until(
                ExpectedConditions.
                        presenceOfElementLocated(
                                By.cssSelector(
                                        "#menu255_obj686 > div._fnctWrap._articleTable > form:nth-child(2) > table > tbody > tr"
                                )
                        )
        );

        List<WebElement> contents = driver.
                findElements(
                        By.cssSelector(
                                "#menu255_obj686 > div._fnctWrap._articleTable > form:nth-child(2) > table > tbody > tr"
                        )
                );

        if(contents.size() > 0) {
            for (WebElement content : contents) {
                String title = content.findElement(By.cssSelector("td._artclTdTitle > a > strong")).getText();
                String date = content.findElement(By.cssSelector("td._artclTdRdate")).getText();
                WebElement url = content.findElement(By.cssSelector("td._artclTdTitle > a"));

                noticeDtoList.add(NoticeDto.builder()
                        .title(title)
                        .date(date)
                        .url(url.getAttribute("href"))
                        .build());
            }
        }

        driver.quit();

        return noticeDtoList;
    }

    @Transactional(readOnly = true)
    public List<ScheduleDto> getSchedule() {
        List<ScheduleDto> scheduleDtoList = new LinkedList<>();

        System.setProperty("webdriver.chrome.driver", path.toString());

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");   // 팝업 안띄움
        options.addArguments("headless");   // 브라우저 안띄움
        options.addArguments("--disable-gpu");  // gpu 비활성화
        options.addArguments("--blink-settings=imagesEnabled=false");   // 이미지 다운 안받음

        WebDriver driver = new ChromeDriver(options);

        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));    // 드라이버가 실행된 후 10초 기다림

        driver.get(scheduleURL);

        webDriverWait.until(
                ExpectedConditions.
                        presenceOfElementLocated(
                                By.cssSelector(
                                        "#schdulWrap > div.calendarWrap > div.list > ul > li"
                                )
                        )
        );

        List<WebElement> contents = driver.
                findElements(
                        By.cssSelector(
                                "#schdulWrap > div.calendarWrap > div.list > ul > li"
                        )
                );

        if(contents.size() > 0) {
            for (WebElement content : contents) {
                String value = content.getText();

                String date = value.substring(0,17);
                String schedule = value.substring(18, value.length());

                scheduleDtoList.add(ScheduleDto.builder()
                        .date(date)
                        .schedule(schedule)
                        .build());
            }
        }

        driver.quit();

        return scheduleDtoList;
    }
}
