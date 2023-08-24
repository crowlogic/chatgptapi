package ai.open.chatgpt;

import java.util.regex.Matcher;

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
    String unfuckedResponse = fuckedResponse.replace("\\[", "$$ ")
                                            .replace("\\]", " $$")
                                            .replace("\\(", " $")
                                            .replace("\\)", "$ ")
                                            .replace("\\{", "\\lbrace ")
                                            .replace("\\}", " \\rbrace");

    // Remove extra whitespaces between $ and content, but only at the very inside
    // part
    unfuckedResponse = unfuckedResponse.replaceAll(" \\$\\s+([^$]+)\\s+\\$ ",
                                                   Matcher.quoteReplacement(" \\$") + "$1"
                                                                 + Matcher.quoteReplacement("\\$ "));

    // Ensure there is always a space after $$
    unfuckedResponse = unfuckedResponse.replaceAll("\\$\\$([^ ])", "\\$\\$ $1");

    // Ensure the closing $$ is on the same line as the opening $$
    String[]      lines = unfuckedResponse.split("\n");
    StringBuilder sb    = new StringBuilder();
    for (String line : lines)
    {
      if (line.contains("$$"))
      {
        line = line.replaceAll("\\s+\\$\\$", "$$");
      }
      sb.append(line).append("\n");
    }
    unfuckedResponse = sb.toString().trim();

    return unfuckedResponse;
  }

  @Override
  public void start(Stage primaryStage)
  {
    primaryStage.setTitle("Chatgpt Latex Unfucker");

    TextArea inputArea  = new TextArea();

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

    VBox  vbox  = new VBox(inputArea,
                           processButton,
                           outputArea,
                           copyButton);

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
