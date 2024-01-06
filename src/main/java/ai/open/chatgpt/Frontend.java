package ai.open.chatgpt;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Frontend extends
                      Application
{

  private VBox          mainContainer;
  private OpenAiService openAiService;

  @Override
  public void start(Stage primaryStage)
  {
    openAiService = new OpenAiService("your-api-key"); // Replace with your actual API key

    mainContainer = new VBox(5);
    mainContainer.setFillWidth(true);

    ScrollPane scrollPane = new ScrollPane(mainContainer);
    scrollPane.setFitToWidth(true);
    scrollPane.setPrefHeight(600);

    addNewInputField();

    Scene scene = new Scene(scrollPane,
                            1280,
                            1024);
    primaryStage.setTitle("GPT-4 Interaction");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private TextField addNewInputField()
  {
    TextField inputField = new TextField();
    inputField.setPromptText("Enter your query...");
    inputField.setOnAction(event ->
    {
      String input = inputField.getText();
      inputField.setEditable(false);
      sendToChatGPT(input);
    });
    mainContainer.getChildren().add(inputField);
    return inputField;
  }

  private void sendToChatGPT(String input)
  {
    new Thread(() ->
    {
      CompletionRequest request     = CompletionRequest.builder()
                                                       .prompt(input)
                                                       .model("text-davinci-004")                                    
                                                       .maxTokens(150)
                                                       .build();
      CompletionResult  response    = openAiService.createCompletion(request);
      String            gptResponse = response.getChoices().get(0).getText().trim();
      updateUIWithResponse(gptResponse);
    }).start();
  }

  private void updateUIWithResponse(String response)
  {
    javafx.application.Platform.runLater(() ->
    {
      Text output = new Text(response);
      mainContainer.getChildren().add(output);
      addNewInputField().requestFocus();
    });
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}
