package ai.open.chatgpt;

import java.io.*;
import java.net.ServerSocket;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;

public class ChatGenerativePretrainedTransformer
{

  private WebDriver driver;
  private String    geckoDriverPath;

  private String    profileDirectory = "/home/crow/.mozilla/firefox-esr/j80r0jp8.default-esr102";

  public ChatGenerativePretrainedTransformer(String geckoDriverPath)
  {
    this.geckoDriverPath = geckoDriverPath;
    System.setProperty("webdriver.gecko.driver", this.geckoDriverPath);

    String url      = "https://chat.openai.com";
    int    freePort = 31337;
    try
    {
      // freePort = findAvailablePort();

      this.driver = setupWebDriver(freePort);
      driver.get(url);
      waitForHumanVerification();
      System.err.println( "Driver currently at " + driver.getCurrentUrl() );
    }
    catch (Exception e)
    {
      throw new UnsupportedOperationException(e.getMessage(),
                                              e);
    }
  }

  private int findAvailablePort() throws IOException
  {
    try ( ServerSocket s = new ServerSocket(0))
    {
      return s.getLocalPort();
    }
  }

  private WebDriver setupWebDriver(int port)
  {
    FirefoxOptions options = new FirefoxOptions();
    options.addArguments(String.format("-b /usr/bin/firefox-esr --connect-existing --marionette-port %s --start-debugger-server=",
                                       port));
    FirefoxProfile profile = new FirefoxProfile(new File(profileDirectory));
    options.setProfile(profile);
    profile.setAcceptUntrustedCertificates(true);
    profile.setAssumeUntrustedCertificateIssuer(true);
    return new FirefoxDriver(options);
  }

  private void waitForHumanVerification() throws InterruptedException
  {
    System.err.println( "Waiting for URL redirect to https://chat.openai.com/");
    while (!driver.getCurrentUrl().equals("https://chat.openai.com/"))
    {
      Thread.sleep(1000);
    }
  }

  public void sendPromptToChatGPT(String prompt)
  {
    WebElement inputBox = driver.findElement(By.id("prompt-textarea"));
    ((JavascriptExecutor) driver).executeScript("arguments[0].value = '" + prompt + "';", inputBox);
    inputBox.sendKeys(Keys.RETURN);
  }

  public List<WebElement> returnChatGPTConversation()
  {
    return this.driver.findElements(By.cssSelector("div.text-base"));
  }

  public void saveConversation(String fileName)
  {
    File directory = new File("conversations");
    if (!directory.exists())
    {
      directory.mkdir();
    }

    String           delimiter           = "|^_^|";
    List<WebElement> chatGPTConversation = returnChatGPTConversation();
    FileWriter       fileWriter;
    try
    {
      fileWriter = new FileWriter(new File(directory,
                                           fileName),
                                  true);

      for (int i = 0; i < chatGPTConversation.size(); i += 2)
      {
        fileWriter.write("prompt: " + chatGPTConversation.get(i).getText() + "\nresponse: "
                      + chatGPTConversation.get(i + 1).getText() + "\n\n" + delimiter + "\n\n");
      }
      fileWriter.close();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage(),
                                 e);
    }
  }

  public String returnLastResponse()
  {
    List<WebElement> responseElements = this.driver.findElements(By.cssSelector("div.text-base"));

    return responseElements.isEmpty() ? null : responseElements.get(responseElements.size() - 1).getText();
  }

  public void quit()
  {
    System.out.println("Closing the browser...");
    this.driver.close();
    this.driver.quit();
  }
}
