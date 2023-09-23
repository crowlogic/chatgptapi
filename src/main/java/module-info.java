module chatgptapi
{
  requires java.base;

  requires transitive java.desktop;
  requires transitive java.instrument;
  requires transitive java.logging;
  requires transitive javafx.controls;
  requires transitive com.google.common;
  requires transitive org.seleniumhq.selenium.api;
  requires transitive org.seleniumhq.selenium.firefox_driver;
  requires transitive dev.failsafe.core;
  requires transitive javafx.graphics;
  
  exports ai.open.chatgpt;
}