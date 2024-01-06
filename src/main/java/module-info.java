module chatgptapi
{
  requires java.base;

  requires transitive java.desktop;
  requires transitive javafx.controls;
  requires transitive javafx.graphics;
  requires api;
  requires service;
  
  exports ai.open.chatgpt;
}