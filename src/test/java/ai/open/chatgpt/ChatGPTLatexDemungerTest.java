package ai.open.chatgpt;

import junit.framework.TestCase;

public class ChatGPTLatexDemungerTest extends
                                      TestCase
{

  public void testFilterLast()
  {
    String disorganized = "This theorem extends immediately to the case where all but a finite number of the \\( \\mu \\neq 0 \\) are of the same sign, positive or negative. We observe first that since the kernel \\( A(x, y) \\) is continuous, all the image functions\n"
                  + "$$\n" + "A(f)(x) = \\int A(x, y) f(y) \\, dy\n" + "$$\n"
                  + "are continuous; therefore, in particular, all the characteristic functions \\( \\varphi_n(x) = \\frac{1}{\\mu_n} A(\\varphi_n)(x) \\) are continuous. Consequently the \"remainders\"\n"
                  + "$$\n";

    System.out.println("-----\nDisorganized\n-----\n " + disorganized + "-----\n-----\n");

    String organized = ChatGPTLatexDemunger.filterLast(disorganized);

    System.out.println("-----\nOrganized\n-----\n " + organized + "-----\n-----\n");

    String expected = "This theorem extends immediately to the case where all but a finite number of the \\( \\mu \\neq 0 \\) are of the same sign, positive or negative. We observe first that since the kernel \\( A(x, y) \\) is continuous, all the image functions\n"
                  + "\n"
                  + "$$A(f)(x) = \\int A(x, y) f(y) \\, dy$$\n"
                  + "\n"
                  + "are continuous; therefore, in particular, all the characteristic functions \\( \\varphi_n(x) = \\frac{1}{\\mu_n} A(\\varphi_n)(x) \\) are continuous. Consequently the \"remainders\"\n"
                  + "\n"
                  + "$$";

    System.out.println("-----\nExpected\n-----\n " + expected + "-----\n-----\n");

    assertEquals(expected, organized);
    

    
  }

}
