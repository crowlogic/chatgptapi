package ai.open.chatgpt;

import java.io.File;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;


public class FirefoxCanKissMyAss
{
  public static void main(String[] args)
  {
    // Set the path to GeckoDriver executable, if it's not on your PATH
    // System.setProperty("webdriver.gecko.driver", "path/to/geckodriver");

    FirefoxOptions options = new FirefoxOptions();

    // Uncomment this line to use an existing Firefox profile
    options.setProfile(new FirefoxProfile(new File("/home/crow/.mozilla/firefox-esr/tzxszgq4.default-esr102/")));

    // Uncomment this line to attach to a running Firefox instance
   // options.setCapability("moz:debuggerAddress", "localhost:9222");

    WebDriver driver = new FirefoxDriver(options);

    // Navigate to a URL
    driver.get("https://www.google.com");

    // Perform any further actions, then close
    // driver.quit();
  }
}
