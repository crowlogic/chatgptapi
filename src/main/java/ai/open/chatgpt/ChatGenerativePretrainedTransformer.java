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
    options.addArguments(String.format("-b /usr/bin/firefox-esr --connect-existing --marionette-port %s --start-debugger-server=", port));
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
    System.out.println("source=" + driver.getPageSource());
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
    return responseElements.get(responseElements.size() - 1).getText();
  }

  public void quit()
  {
    System.out.println("Closing the browser...");
    this.driver.close();
    this.driver.quit();
  }
}
