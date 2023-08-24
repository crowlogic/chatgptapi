package ai.open.chatgpt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class LatexUnfuckerTest
{

  public static void main( String args[] )
  {
    LatexUnfucker unfucker = new LatexUnfucker();
    String unfuck = LatexUnfucker.unfuck("## What Does Nilpotent Mean?\n"
                  + "\n"
                  + "In mathematics, the term \"nilpotent\" is commonly used in the context of groups, matrices, and elements of rings to describe a certain type of \"vanishing\" behavior.\n"
                  + "\n"
                  + "### Nilpotent Matrix\n"
                  + "\n"
                  + "A square matrix \\( A \\) is said to be nilpotent if there exists a positive integer \\( k \\) such that \\( A^k = 0 \\), where \\( 0 \\) is the zero matrix of the same size as \\( A \\), and \\( A^k \\) denotes the \\( k \\)-th power of \\( A \\).\n"
                  + "\n"
                  + "### Nilpotent Element in a Ring\n"
                  + "\n"
                  + "An element \\( a \\) in a ring \\( R \\) is said to be nilpotent if there exists a positive integer \\( n \\) such that \\( a^n = 0 \\), where \\( 0 \\) is the additive identity element in \\( R \\).\n"
                  + "\n"
                  + "### Nilpotent Group\n"
                  + "\n"
                  + "In the context of group theory, a group \\( G \\) is nilpotent if it has a central series that terminates at \\( G \\). A central series for a group \\( G \\) is a sequence of subgroups:\n"
                  + "\n"
                  + "\\[\n"
                  + "\\{e\\} = G_0 \\triangleleft G_1 \\triangleleft \\cdots \\triangleleft G_n = G\n"
                  + "\\]\n"
                  + "\n"
                  + "where each \\( G_i \\) is a normal subgroup of \\( G_{i+1} \\), and the quotient \\( G_{i+1}/G_i \\) is contained in the center of \\( G/G_i \\).\n"
                  + "\n"
                  + "Nilpotent objects often arise in settings where approximation or limiting procedures are important, as their \"vanishing\" behavior can be helpful in simplifying expressions or solving equations. They also play an important role in various areas of mathematics, including representation theory, Lie theory, and algebraic geometry.\n"
                  + "");
    System.out.println( unfuck );
  }

}
