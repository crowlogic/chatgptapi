package ai.open.chatgpt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LatexUnfucker extends
                           Application
{

  public static String unfuck(String fuckedResponse)
  {
    String unfuckedResponse = fuckedResponse.replace("\\[", "$$")
                                            .replace("\\]", "$$")
                                            .replace("\\(", "$")
                                            .replace("\\)", "$");
    return unfuckedResponse;
  }

  @Override
  public void start(Stage primaryStage)
  {
    primaryStage.setTitle("Latex Unfucker");

    TextArea inputArea = new TextArea();
    inputArea.setText("Enter LaTeX text here...");

    TextArea outputArea = new TextArea();
    outputArea.setText("Unfucked LaTeX will appear here...");
    outputArea.setEditable(false);

    Button processButton = new Button("Process");
    processButton.setOnAction(e ->
    {
      String input  = inputArea.getText();
      String output = unfuck(input);
      outputArea.setText(output);
    });

    Button copyButton = new Button("Copy to Clipboard");
    copyButton.setOnAction(e ->
    {
      String                 output    = outputArea.getText();
      final Clipboard        clipboard = Clipboard.getSystemClipboard();
      final ClipboardContent content   = new ClipboardContent();
      content.putString(output);
      clipboard.setContent(content);
    });

    VBox vbox = new VBox(inputArea,
                         processButton,
                         outputArea,
                         copyButton);
    vbox.getChildren().add(inputArea);
    Scene scene = new Scene(vbox,
                            400,
                            300);

    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}
