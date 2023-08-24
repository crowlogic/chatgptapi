module chatgptapi
{
  requires java.base;

  requires transitive java.desktop;
  requires transitive java.instrument;
  requires transitive java.logging;
  requires transitive javafx.controls;
  requires transitive selenium.api;
  
  exports ai.open.chatgpt;
}