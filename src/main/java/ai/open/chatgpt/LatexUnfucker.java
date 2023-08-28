package ai.open.chatgpt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LatexUnfucker extends
                           Application
{

  public static String unfuck(String input)
  {
    StringBuilder output             = new StringBuilder();
    StringBuilder buffer             = new StringBuilder();
    boolean       insideDollar       = false;
    boolean       insideDoubleDollar = false;

    for (int i = 0; i < input.length(); i++)
    {
      char c = input.charAt(i);
      if (insideDollar || insideDoubleDollar)
      {
        if ((insideDollar && i < input.length() - 1 && c == '\\' && input.charAt(i + 1) == ')')
                      || (insideDoubleDollar && i < input.length() - 1 && c == '\\' && input.charAt(i + 1) == ']'))
        {
          if (insideDoubleDollar)
          {
            insideDoubleDollar = false;
            output.append(buffer.toString().trim()).append("$$");
          }
          else
          {
            insideDollar = false;
            output.append(buffer.toString().trim()).append("$");
          }
          buffer.setLength(0);
          i++; // Skip the next character as well
        }
        else
        {
          buffer.append(c);
        }
      }
      else
      {
        if (i < input.length() - 1 && c == '\\' && (input.charAt(i + 1) == '(' || input.charAt(i + 1) == '['))
        {
          if (input.charAt(i + 1) == '[')
          {
            insideDoubleDollar = true;
            output.append("$$");
          }
          else
          {
            insideDollar = true;
            if (output.length() > 0 && output.charAt(output.length() - 1) == ' ')
            {
              output.append("$");
            }
            else
            {
              output.append(" $");
            }
          }
          i++; // Skip the next character as well
        }
        else
        {
          output.append(c);
        }
      }
    }

    String  result  = output.toString()
                            .replace("\\{", "\\lbrace ")
                            .replace("\\}", " \\rbrace")
                            .replace(" \\, ", " ");
    Pattern pattern = Pattern.compile(",\\s*(?=$|\\n)");
    Matcher matcher = pattern.matcher(result);
    result = matcher.replaceAll("");
    return result;
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

    Button clearButton = new Button("Clear Input");
    clearButton.setOnAction(e ->
    {
      inputArea.clear(); // Assuming `inputArea` is the TextArea you want to clear
    });

    HBox   inputButtons = new HBox(processButton,
                                   clearButton);

    Button copyButton   = new Button("Copy to Clipboard");
    copyButton.setOnAction(e ->
    {
      String                 output    = outputArea.getText();
      final Clipboard        clipboard = Clipboard.getSystemClipboard();
      final ClipboardContent content   = new ClipboardContent();
      content.putString(output);
      clipboard.setContent(content);
    });

    VBox vbox = new VBox(inputArea,
                         inputButtons,
                         outputArea,
                         copyButton);

    VBox.setVgrow(inputArea, Priority.ALWAYS);
    VBox.setVgrow(outputArea, Priority.ALWAYS);
    inputArea.setMinHeight(150); // Replace 150 with half of your VBox height
    outputArea.setMinHeight(150); // Replace 150 with half of your VBox height
    vbox.setFillWidth(true);

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
