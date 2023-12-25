package ai.open.chatgpt;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ChatGPTLatexDemungerAndBeautifer extends
                                              Application
{

  public static String demunge(String input)
  {
    return input.replaceAll(",(?=\\s*\\$\\$)", "")
                .replaceAll(",\\s*(?=\\\\quad)", "")
                .replaceAll("\\\\\\(\\s*(.*?)\\s*\\\\\\)", "\\$$1\\$")
                .replaceAll("\\\\\\[([\\s\\S]*?)\\\\\\]", "\n\n\\$\\$$1\\$\\$\n\n")
                .replaceAll(",(?:\\s|\n)+\\$\\$", "\\$\\$");

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
    scene.setOnKeyPressed(event ->
    {
      if (event.getCode() == KeyCode.ESCAPE)
      {
        primaryStage.close(); // Close the application
      }
    });

    if (darkStyle)
    {
      scene.getStylesheets().add("dark-theme.css");
    }
    primaryStage.setMinWidth(800);
    primaryStage.setMinHeight(600);
    primaryStage.setWidth(1280);
    primaryStage.setHeight(960);
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.centerOnScreen();
  }

  static boolean darkStyle = false;

  public static void main(String[] args)
  {
    darkStyle = (args.length > 0 && "dark".equals(args[0]));
    launch(args);
  }
}
