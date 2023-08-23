package ai.open.chatgpt;

public class LatexUnfucker
{
  /**
   * Necessary because of chatgpts half-assed bullshit
   * 
   * @param fuckedResponse
   * 
   * @return unfuckedResponse with the fucking \[, and \] replace with fucking $$
   *         and fucking \(, and \) replaced with fucking $$
   */
  public static String unfuck(String fuckedResponse)
  {
    String unfuckedResponse = fuckedResponse.replace("\\[", "$$")
                                            .replace("\\]", "$$'")
                                            .replace("\\(", "$")
                                            .replace("\\)", "$");
    return unfuckedResponse;
  }
}
