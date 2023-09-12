package ai.open.chatgpt;

import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatGPTLatexUnfucker extends
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
            output.append(buffer.toString().trim()).append("\n```");
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
            output.append("```math\n");
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

    return Pattern.compile("^[ \\t]+(?=```math\\n)", Pattern.MULTILINE).matcher(output.toString()).replaceAll("");
  }

  public void start(Stage primaryStage)
  {
    primaryStage.setTitle("ChatGPT Latex Unfucker");

    TextArea inputArea  = new TextArea();

    TextArea outputArea = new TextArea();
    outputArea.setText("Unfucked LaTeX will appear here...");
    outputArea.setEditable(false);

    Button processButton = new Button("Process");
    processButton.setOnAction(e ->
    {
      outputArea.setText(unfuck(inputArea.getText()));
    });

    Button clearButton = new Button("Clear Input");
    clearButton.setOnAction(e ->
    {
      inputArea.clear();
    });

    Button pasteButton = new Button("Paste");
    pasteButton.setOnAction(e ->
    {
      final Clipboard clipboard = Clipboard.getSystemClipboard();
      if (clipboard.hasContent(DataFormat.PLAIN_TEXT))
      {
        inputArea.appendText(clipboard.getContent(DataFormat.PLAIN_TEXT).toString());
      }
    });

    Button copyButton = new Button("Copy to Clipboard");
    copyButton.setOnAction(e ->
    {
      ClipboardContent content = new ClipboardContent();
      content.putString(outputArea.getText());
      Clipboard.getSystemClipboard().setContent(content);
    });

    HBox buttons = new HBox(processButton,
                            clearButton,
                            pasteButton,
                            copyButton);

    VBox vbox    = new VBox(inputArea,
                            buttons,
                            outputArea);

    VBox.setVgrow(inputArea, Priority.ALWAYS);
    VBox.setVgrow(outputArea, Priority.ALWAYS);
    inputArea.setMinHeight(inputArea.getHeight() / 2);
    outputArea.setMinHeight(outputArea.getHeight() / 2);
    vbox.setFillWidth(true);

    Scene scene = new Scene(vbox,
                            400,
                            300);
    if (darkStyle)
    {
      scene.getStylesheets().add("darkStyle-theme.css");
    }
    primaryStage.setMinWidth(800);
    primaryStage.setMinHeight(600);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  static boolean darkStyle = false;

  public static void main(String[] args)
  {
    darkStyle = (args.length > 0 && "dark".equals(args[0]));
    launch(args);
  }
}
