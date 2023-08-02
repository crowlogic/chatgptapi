package ai.open.chatgpt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

public class ChatGenerativePretrainedTransformer
{

  private WebDriver driver;
  private String    firefoxPath;
  private String    geckoDriverPath;

  public ChatGenerativePretrainedTransformer(String firefoxPath,
                           String geckoDriverPath)
  {
    this.firefoxPath     = firefoxPath;
    this.geckoDriverPath = geckoDriverPath;
    System.setProperty("webdriver.gecko.driver", this.geckoDriverPath);

    String url = "https://chat.openai.com";
    int    freePort;
    try
    {
      freePort = findAvailablePort();

      launchFirefoxWithRemoteDebugging(freePort, url);
      waitForHumanVerification();
      this.driver = setupWebDriver(freePort);
    }
    catch (IOException | InterruptedException e)
    {
      throw new UnsupportedOperationException(e.getMessage(),
                                              e);
    }
  }

  private int findAvailablePort() throws IOException
  {
    ServerSocket s    = new ServerSocket(0);
    int          port = s.getLocalPort();
    s.close();
    return port;
  }

  @SuppressWarnings("deprecation")
  private void launchFirefoxWithRemoteDebugging(int port, String url)
  {
    new Thread(() ->
    {
      try
      {
        String command = this.firefoxPath + " --remote-debugging-port=" + port + " --user-data-dir=remote-profile "
                      + url;
        Runtime.getRuntime().exec(command);
      }
      catch (IOException e)
      {
        throw new RuntimeException(e.getMessage(),
                                   e);
      }
    }).start();
  }

  private WebDriver setupWebDriver(int port)
  {
    FirefoxOptions options = new FirefoxOptions();
    options.addArguments("--start-debugger-server=" + port);
    FirefoxProfile profile = new FirefoxProfile();
    options.setProfile(profile);
    return new FirefoxDriver(options);
  }

  private void waitForHumanVerification() throws InterruptedException
  {
    try ( Scanner input = new Scanner(System.in))
    {
      System.out.println("You need to manually complete the log-in or the human verification if required.");

      while (true)
      {
        System.out.println("Enter 'y' if you have completed the log-in or the human verification, or 'n' to check again: ");
        String userInput = input.nextLine().toLowerCase();

        if (userInput.equals("y"))
        {
          System.out.println("Continuing with the automation process...");
          break;
        }
        else if (userInput.equals("n"))
        {
          System.out.println("Waiting for you to complete the human verification...");
        }
        else
        {
          System.out.println("Invalid input. Please enter 'y' or 'n'.");
        }
      }
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
      throw new UnsupportedOperationException(e.getMessage(),
                                              e);
    }
  }

  public String returnLastResponse()
  {
    List<WebElement> responseElements = this.driver.findElements(By.cssSelector("div.text-base"));
    return responseElements.get(responseElements.size() - 1).getText();
  }

  public void quit()
  {
    System.out.println("Closing the browser...");
    this.driver.close();
    this.driver.quit();
  }
}
