package ai.open.chatgpt;

public class MainApp
{

  public static void main(String[] args)
  {
    // launch(args);

    // Create an instance
    ChatGenerativePretrainedTransformer chatgpt = new ChatGenerativePretrainedTransformer("/usr/bin/firefox-esr",
                                                      "/usr/bin/geckodriver");

    // Define a prompt and send it to chatgpt
    String            prompt  = "What are the benefits of exercise?";
    chatgpt.sendPromptToChatGPT(prompt);

    // Retrieve the last response from ChatGPT
    String response = chatgpt.returnLastResponse();
    System.out.println(response);

    // Save the conversation to a text file
    String fileName = "conversation.txt";
    chatgpt.saveConversation(fileName);

    // Close the browser and terminate the WebDriver session
    chatgpt.quit();
  }
}
