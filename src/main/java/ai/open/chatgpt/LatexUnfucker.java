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

  public static String unfuck(String input)
  {
    StringBuilder output             = new StringBuilder();
    StringBuilder buffer             = new StringBuilder();
    boolean       insideMath         = false;
    boolean       insideDoubleDollar = false;

    for (int i = 0; i < input.length(); i++)
    {
      char c = input.charAt(i);
      if (insideMath || insideDoubleDollar)
      {
        if ((insideMath && i < input.length() - 1 && c == '\\' && input.charAt(i + 1) == ')')
                      || (insideDoubleDollar && i < input.length() - 1 && c == '\\' && input.charAt(i + 1) == ']'))
        {
          if (insideDoubleDollar)
          {
            insideDoubleDollar = false;
            output.append(buffer.toString().trim()).append("$$");
          }
          else
          {
            insideMath = false;
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
            insideMath = true;
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

    String result = output.toString();
    result = result.replace("\\{", "\\lbrace ");
    result = result.replace("\\}", " \\rbrace");

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
