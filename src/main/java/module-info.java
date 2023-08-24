module chatgptapi
{
  requires java.base;

  requires transitive java.desktop;
  requires transitive java.instrument;
  requires transitive java.logging;
  requires transitive javafx.controls;
  requires transitive org.seleniumhq.selenium.api;
  requires transitive org.seleniumhq.selenium.firefox_driver;
  
  exports ai.open.chatgpt;
}