package ai.open.chatgpt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ChatGPTLatexDemunger extends
                                  Application
{

  public static String demunge(String input)
  {
    StringBuilder output             = new StringBuilder();
    StringBuilder buffer             = new StringBuilder();
    boolean       insideDollar       = false;
    boolean       insideDoubleDollar = false;

    filterFirst(input, output, buffer, insideDollar, insideDoubleDollar);

    String wad = filterSecond(output);

    return filterLast(wad);
  }

  /**
   * TODO: transform
   * 
   * <pre>
   $$
    xyz
   $$
   
   to
   
   $$xyx$$
   * </pre>
   * 
   * @param wad
   * @return
   */
  static String filterLast(String wad)
  {
    StringBuilder  builder = new StringBuilder();

    BufferedReader reader  = new BufferedReader(new StringReader(wad));
    String         line    = null;

    try
    {
      boolean insideDollarBlock = false;

      // Pattern to match standalone $$
      Pattern pattern           = Pattern.compile("^\\s*\\$\\$\\s*$");

      while ((line = reader.readLine()) != null)
      {
        Matcher matcher = pattern.matcher(line);

        if (matcher.find())
        {
          if (insideDollarBlock)
          {
            // Closing $$
            builder.append(line).append("\n");
          }
          else
          {
            // Opening $$
            builder.append("\n").append(line);
          }
          insideDollarBlock = !insideDollarBlock; // Toggle the state
        }
        else
        {
          builder.append(line);
        }

        if (!insideDollarBlock || matcher.find())
        {
          builder.append("\n");
        }
      }
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage(),
                                 e);
    }
    return builder.toString();
  }

  private static String filterSecond(StringBuilder output)
  {
    String result = Pattern.compile("^[ \\t]+(?=```math\\n)", Pattern.MULTILINE)
                           .matcher(output.toString())
                           .replaceAll("");
    String wad    = result.replace("\\, dt", "dt");
    return wad;
  }

  private static void filterFirst(String input,
                                  StringBuilder output,
                                  StringBuilder buffer,
                                  boolean insideDollar,
                                  boolean insideDoubleDollar)
  {
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
  }

  public static boolean containsWhitespaceBeforeDollar(String input)
  {
    // Check if the string matches the pattern: zero or more whitespaces followed by
    // $$
    return input.matches("\\s+\\$\\$.*");
  }

  public void start(Stage primaryStage)
  {
    primaryStage.setTitle("ChatGPT Latex Demunger");

    TextArea inputArea  = new TextArea();

    TextArea outputArea = new TextArea();
    outputArea.setText("Demunged LaTeX suitable for use with markdown will appear here...");
    outputArea.setEditable(false);

    Button processButton = new Button("Process");
    processButton.setOnAction(e ->
    {
      outputArea.setText(demunge(inputArea.getText()));
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

    Button copyButton = new Button("Copy");
    copyButton.setOnAction(e ->
    {
      ClipboardContent content = new ClipboardContent();
      content.putString(outputArea.getText());
      Clipboard.getSystemClipboard().setContent(content);
    });

    Button exhortButton = new Button("Exhort");
    exhortButton.setOnAction(e ->
    {
      ClipboardContent content = new ClipboardContent();
      content.putString("restate that verbatim as github flavored markdown with $ and $$ demarcating mathematical expressions so that they are interpreted as latex propery and return the response in a single code block");
      Clipboard.getSystemClipboard().setContent(content);

      // Create tooltip for notification
      Tooltip tooltip = new Tooltip("Exhortation copied");
      tooltip.setAutoHide(true);
      tooltip.setShowDelay(Duration.ZERO);
      tooltip.setHideDelay(Duration.seconds(2));
      tooltip.setStyle("-fx-background-color: black; -fx-font-size: 12; -fx-padding: 5 10 5 10;");

      Point2D p = exhortButton.localToScreen(exhortButton.getBoundsInLocal().getMaxX(),
                                             exhortButton.getBoundsInLocal().getMaxY());
      tooltip.show(exhortButton, p.getX() - 50, p.getY() - 30);

      PauseTransition delay = new PauseTransition(Duration.seconds(2));
      delay.setOnFinished(event -> tooltip.hide());
      delay.play();
    });

    HBox buttons = new HBox(exhortButton,
                            pasteButton,
                            processButton,
                            copyButton,
                            clearButton);
    buttons.setSpacing(10.0);
    buttons.setAlignment(Pos.CENTER);
    VBox vbox = new VBox(inputArea,
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
      scene.getStylesheets().add("dark-theme.css");
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
