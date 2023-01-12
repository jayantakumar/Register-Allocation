# Register-Allocation

Compilers play a vital role in the world of computer science, responsible not only for generating code but also for ensuring efficient and effective allocation of registers. The problem of register allocation can be complex, resembling the coloring problem which is known to be NP complete. In this assignment, we use Kempe's heuristic and tools such as JTB and the visitor pattern to come up with a nearly optimal register allocation for a given piece of Java code. Through this process, we gain a deeper understanding of the inner workings of compilers and the art of efficient code generation.

# Graph Coloring

![Graph](https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Petersen_graph_3-coloring.svg/1200px-Petersen_graph_3-coloring.svg.png)

Graph Coloring is a well know computational problem that belongs to the class of NP complete Problems. The Graph that is to be colored is constructed by performing a live variable analysis , an edge joining two variables if they have interfering liveness. Once constructed We will be using the Kempe's hueristic to perform a nearly optimal coloring of the obtained graph. While we can obtain a perfect answer by running an exponential algorithm , it wont be feasable for the use case of a compiler which will be dealing with large code bases supposedly.

# Kempe's Heuristic

```
repeat
  repeat
    Remove a node n and all its edges from G, such that the
    degree of n is less than K
    Push n onto a stack
  until G has no node with degree less than K
  // G is now either empty or all its nodes have degree ≥K
  if G is not empty then
    Take one node m out of G
    Push m onto the stack
  endif
until G is empty
Take one node at a time from stack and assign a non-conflicting
color if possible, else spill
```

After coloring the graph and assinging it registers we go on to generate register refereced code , thus eliminating the abstraction of variable names from our intermediate code. This is the required solution for the given problem statement.

# Input Specifications
First line of every testcase contains a special comment of the form /*k*/ where k represents the maximum
number of registers that can be used inside any given function. Your job is to replace all the variable declarations using a suitable register or memory reference. In order to do this you are given an implementation
of liveness analysis which you must use to create an interference graph and color it using the specified
number of registers allowed for that program. You are provided with the following files as a part of this
assignment:

assn/friendTJ.jj : The source grammar.

validator/friendTJMem.jj : The target grammar.

assn/* : Java project using friendTJ.jj that provides an API for liveness information.

validator/* : Java project using friendTJMem.jj that validates the obtained output.

testcase/* : Public test case input

testcase-output/* : Public test case output

## Liveness API

This api provides the result of liveness analysis as a hashmap (See assn/Main.java) that contains a
mapping from Node to Set<String>. The result contains the set of variables that are live at that node
i.e. the IN set. All the Statement Nodes can be used to query the hashmap, namely, PrintStatement,
VarDeclaration, AssignmentStatement, ArrayAssignmentStatement, FieldAssignmentStatement,
IfthenStatement, IfthenElseStatement, WhileStatement and LivenessQueryStatement. Additionally, for getting the liveness information at the return statement of a function, MethodDeclaration
node must be used.

## Example Input TestCase

```
/*2*/
class TC02 {
  public static void main(String[] args) {
    TestTC02 o;
    int res;
    o = new TestTC02();
    res = o.foo();
    System.out.println(res);
  }
}
class TestTC02 {
  public int foo() {
    int a;
    int b;
    int c;
    int d;
    int e;
    int t;
    a = 5;
    b = 6;
    c = a + b;
    d = c + a;
    e = a - c;
    t = d - e;
    return t;
  }
}

```
## Example Output TestCase

```
/*2*/
import static a5.Memory.*;
class TC02 {
  public static void main(String[] args) {
    Object r1;
    Object r2;
    alloca(0);
    r1 = new TestTC02();
    r1 = ((TestTC02)r1).foo();
    System.out.println(((int)r1));
  }
}
class TestTC02 {
  public int foo() {
    Object r1;
    Object r2;
    alloca(1);
    r1 = 5;
    r2 = 6;
    r2 = ((int) r1) + ((int) r2);
    store(0,((int) r2) + ((int) r1));
    r1 = ((int) r1) - ((int) r2);
    r1 = ((int) load(0)) - ((int) r1);
    return ((int)r1);
  }
}
```
