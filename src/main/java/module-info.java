module chatgptapi
{
  requires java.base;

  requires transitive java.desktop;
  requires transitive javafx.controls;
  requires transitive javafx.graphics;
  
  exports ai.open.chatgpt;
}