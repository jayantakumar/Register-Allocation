//
// Generated by JTB 1.3.2
//

package visitor;

import syntaxtree.*;

import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class CodeGeneratorVisitor implements GJNoArguVisitor<String> {
    HashMap<Node, HashMap<String,Integer>> colorMap = new HashMap<>();
    int registerLimit = 0;
    public String finalOutput = "";
    HashMap<String,String> symbolTable = new HashMap<>();
    HashMap<String,Integer> currentMethodColorMap = new HashMap<>();
    public CodeGeneratorVisitor(HashMap<Node, HashMap<String,Integer>> r,int registerLimit) {
        colorMap = r;
        this.registerLimit = registerLimit;
    }
    private String generateRegisterHeader(){
        String ans = "";
        for(int i = 1;i<=registerLimit;i++){
            ans+="Object r"+i+";\n";
        }
        return ans;
    }
    private String allocationHeaderGenerator(){
        int count = 0;
        for(String x :currentMethodColorMap.keySet()){
            if(currentMethodColorMap.get(x)<0)
                count++;
        }
        return "alloca("+count+");\n";
    }
    private String rhsHelper(String key){
        String ans = "";
        if(currentMethodColorMap.containsKey(key)){
            int color = currentMethodColorMap.get(key);
            if(color>=0){
                if(symbolTable.containsKey(key))
                return "("+"("+symbolTable.get(key)+")"+" r"+(color+1)+")";
                return key;
            }
            else{
                if(symbolTable.containsKey(key))
                return "(("+symbolTable.get(key)+")"+" load"+"("+(-color-1)+")"+")";
                return key;
            }
        }
        else
            return key;
    }
    private String lhsHelper(String key,Expression expression){
        String ans = "";
        if(currentMethodColorMap.containsKey(key)){
            int color = currentMethodColorMap.get(key);
            if(color>=0){
                return "r"+(color+1)+"="+expression.accept(this)+";";
            }
            else{

                return "store("+(-color-1)+","+expression.accept(this)+");";
            }
        }
        else
            return key;
    }
    String body = "";
    //
    // Auto class visitors--probably don't need to be overridden.
    //
    public String visit(NodeList n) {
        String _ret=null;
        int _count=0;
        for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this);
            _count++;
        }
        return _ret;
    }

    public String visit(NodeListOptional n) {
        if ( n.present() ) {
            String _ret=null;
            int _count=0;
            for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
                e.nextElement().accept(this);
                _count++;
            }
            return _ret;
        }
        else
            return null;
    }

    public String visit(NodeOptional n) {
        if ( n.present() )
            return n.node.accept(this);
        else
            return null;
    }

    public String visit(NodeSequence n) {
        String _ret=null;
        int _count=0;
        for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this);
            _count++;
        }
        return _ret;
    }

    public String visit(NodeToken n) { return n.tokenImage; }

    //
    // User-generated visitor methods below
    //

    /**
     * f0 -> ( <REGLIMIT> )?
     * f1 -> MainClass()
     * f2 -> ( TypeDeclaration() )*
     * f3 -> <EOF>
     */
    public String visit(Goal n) {
        String _ret="";
        // if (n.f0.present()) {
        //    System.out.println("Register Limit: " + n.f0.node);
        // }
        finalOutput+=n.f0.accept(this)+"\n";
        finalOutput+="import static a5.Memory.*;\n";
        finalOutput+=n.f1.accept(this);

        n.f2.accept(this);
        return _ret;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> "public"
     * f4 -> "static"
     * f5 -> "void"
     * f6 -> "main"
     * f7 -> "("
     * f8 -> "String"
     * f9 -> "["
     * f10 -> "]"
     * f11 -> Identifier()
     * f12 -> ")"
     * f13 -> "{"
     * f14 -> ( VarDeclaration() )*
     * f15 -> ( Statement() )*
     * f16 -> "}"
     * f17 -> "}"
     */

    public String visit(MainClass n) {
        String _ret=null;
        body = "";
        currentMethodColorMap = colorMap.get(n);
        symbolTable.clear();
        body+=n.f0.accept(this)+" ";
        body+=n.f1.accept(this);
        body+=n.f2.accept(this)+"\n";
        body+=n.f3.accept(this)+" ";
        body+=n.f4.accept(this)+" ";
        body+=n.f5.accept(this)+" ";
        body+=n.f6.accept(this)+" ";
        body+=n.f7.accept(this);
        body+=n.f8.accept(this);
        body+=n.f9.accept(this);
        body+= n.f10.accept(this)+" ";
        body+=n.f11.accept(this);
        body+=n.f12.accept(this);
        body+=n.f13.accept(this)+"\n";
        n.f14.accept(this);
        body+=generateRegisterHeader();
        body+=allocationHeaderGenerator();
        Vector<Node> statements = n.f15.nodes;
        for(Node statement : statements){
            String out=statement.accept(this)+"\n";
            body+=out;
        }
        body+=n.f16.accept(this)+"\n";
        body+=n.f17.accept(this);
        return body;
    }

    /**
     * f0 -> ClassDeclaration()
     *       | ClassExtendsDeclaration()
     */
    public String visit(TypeDeclaration n) {
        String _ret="";
        _ret = n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    public String visit(ClassDeclaration n) {
        String _ret=null;
        finalOutput+="\n";
        finalOutput+=n.f0.accept(this)+" ";
        finalOutput+= n.f1.accept(this);
        finalOutput+=n.f2.accept(this)+"\n";
        Vector<Node> vars = n.f3.nodes;
        for(Node var : vars)
            finalOutput+=var.accept(this)+"\n";
        Vector<Node> methods = n.f4.nodes;
        for(Node method : methods)
            finalOutput+=method.accept(this)+"\n";
        finalOutput+=n.f5.accept(this);
        return finalOutput;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "extends"
     * f3 -> Identifier()
     * f4 -> "{"
     * f5 -> ( VarDeclaration() )*
     * f6 -> ( MethodDeclaration() )*
     * f7 -> "}"
     */
    public String visit(ClassExtendsDeclaration n) {
        String _ret=null;
        finalOutput+="\n";
        finalOutput+=n.f0.accept(this)+" ";
        finalOutput+= n.f1.accept(this);
        finalOutput+=n.f2.accept(this)+" ";

        finalOutput+=n.f3.accept(this);
        finalOutput+=n.f4.accept(this)+"\n";
        finalOutput+="\n";
        Vector<Node> vars = n.f5.nodes;
        for(Node var : vars)
            finalOutput+=var.accept(this)+"\n";
        Vector<Node> methods = n.f6.nodes;
        for(Node method : methods)
            finalOutput+=method.accept(this)+"\n";
        finalOutput+=n.f7.accept(this);
        return finalOutput;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
    public String visit(VarDeclaration n) {
        String _ret="";
        _ret+=n.f0.accept(this)+" ";
        _ret+=n.f1.accept(this);
        _ret+=n.f2.accept(this);
        symbolTable.put(n.f1.f0.tokenImage,n.f0.accept(this));
        return _ret;
    }

    /**
     * f0 -> "public"
     * f1 -> Type()
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( FormalParameterList() )?
     * f5 -> ")"
     * f6 -> "{"
     * f7 -> ( VarDeclaration() )*
     * f8 -> ( Statement() )*
     * f9 -> "return"
     * f10 -> Identifier()
     * f11 -> ";"
     * f12 -> "}"
     */
    public String visit(MethodDeclaration n) {
        body = "";
        String _ret=null;
        currentMethodColorMap = colorMap.get(n);
        symbolTable.clear();
        body+=n.f0.accept(this)+" ";
        body+=n.f1.accept(this)+" ";
        body+=n.f2.accept(this);
        body+=n.f3.accept(this);
        if(n.f4.present())
        body+=n.f4.accept(this);
        body+=n.f5.accept(this);
        body+=n.f6.accept(this)+"\n";
        n.f7.accept(this);
        body+=generateRegisterHeader();
        body+=allocationHeaderGenerator();
        Vector<Node> statements = n.f8.nodes;
        for(Node statement : statements){
            String out=statement.accept(this)+"\n";
            body+=out;
        }
       // n.f8.accept(this);
        body+=n.f9.accept(this)+" ";
        body+=rhsHelper(n.f10.accept(this));
        body+=n.f11.accept(this)+"\n";
        body+=n.f12.accept(this)+"\n";
        return body;
    }

    /**
     * f0 -> FormalParameter()
     * f1 -> ( FormalParameterRest() )*
     */
    public String visit(FormalParameterList n) {
        String _ret="";
        _ret+=n.f0.accept(this);
        Vector<Node> nodes = n.f1.nodes;
        for(Node node:nodes)
            _ret+=node.accept(this);
        return _ret;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    public String visit(FormalParameter n) {
        String _ret="";
        _ret+=n.f0.accept(this)+" ";
        _ret+=n.f1.accept(this);
        return _ret;
    }

    /**
     * f0 -> ","
     * f1 -> FormalParameter()
     */
    public String visit(FormalParameterRest n) {
        String _ret="";
        _ret+=n.f0.accept(this);
        _ret+=n.f1.accept(this);
        return _ret;
    }

    /**
     * f0 -> ArrayType()
     *       | BooleanType()
     *       | StringType()
     *       | FloatType()
     *       | Identifier()
     */
    public String visit(Type n) {
        String _ret="";
        _ret+=n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> "int"
     * f1 -> "["
     * f2 -> "]"
     */
    public String visit(ArrayType n) {
        String _ret="";
        _ret+=n.f0.accept(this);
        _ret+=n.f1.accept(this);
        _ret+=n.f2.accept(this);
        return _ret;
    }

    /**
     * f0 -> "float"
     */
    public String visit(FloatType n) {
        String _ret=n.f0.tokenImage;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> "boolean"
     */
    public String visit(BooleanType n) {
        String _ret=n.f0.tokenImage;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> "int"
     */
    public String visit(IntegerType n) {
        String _ret=n.f0.tokenImage;

        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> Block()
     *       | AssignmentStatement()
     *       | ArrayAssignmentStatement()
     *       | FieldAssignmentStatement()
     *       | IfStatement()
     *       | WhileStatement()
     *       | PrintStatement()
     *       | LivenessQueryStatement()
     */
    public String visit(Statement n) {
        String _ret= n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> "{"
     * f1 -> ( Statement() )*
     * f2 -> "}"
     */
    public String visit(Block n) {
        String _ret="";
        _ret+=n.f0.accept(this);

        Vector<Node> statements = n.f1.nodes;
        for(Node statement : statements){
            _ret+=statement.accept(this)+"\n";
        }
        _ret+=n.f2.accept(this);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
    public String visit(AssignmentStatement n) {
        String _ret="";
        _ret+=lhsHelper(n.f0.f0.tokenImage,n.f2);
        n.f0.accept(this);
        n.f1.accept(this);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "["
     * f2 -> Identifier()
     * f3 -> "]"
     * f4 -> "="
     * f5 -> Identifier()
     * f6 -> ";"
     */
    public String visit(ArrayAssignmentStatement n) {
        String _ret="";
       _ret+= n.f0.accept(this);
        _ret+=n.f1.accept(this);
       _ret+= n.f2.accept(this);
        _ret+=n.f3.accept(this);
        _ret+=n.f4.accept(this);
        _ret+=n.f5.accept(this);
        _ret+=n.f6.accept(this);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "."
     * f2 -> Identifier()
     * f3 -> "="
     * f4 -> Identifier()
     * f5 -> ";"
     */
    public String visit(FieldAssignmentStatement n) {
        String _ret="";
        _ret+= n.f0.accept(this);
        _ret+=n.f1.accept(this);
        _ret+= n.f2.accept(this);
        _ret+=n.f3.accept(this);
        _ret+=n.f4.accept(this);
        _ret+=n.f5.accept(this);
        return _ret;
    }

    /**
     * f0 -> IfthenElseStatement()
     *       | IfthenStatement()
     */
    public String visit(IfStatement n) {
        String _ret=
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> "if"
     * f1 -> "("
     * f2 -> Identifier()
     * f3 -> ")"
     * f4 -> Statement()
     */
    public String visit(IfthenStatement n) {
        String _ret="";
        _ret+=n.f0.accept(this);
        _ret+=n.f1.accept(this);
        _ret+=rhsHelper(n.f2.accept(this));
        _ret+=n.f3.accept(this)+"\n";
        _ret+=n.f4.accept(this)+"\n";
        return _ret;
    }

    /**
     * f0 -> "if"
     * f1 -> "("
     * f2 -> Identifier()
     * f3 -> ")"
     * f4 -> Statement()
     * f5 -> "else"
     * f6 -> Statement()
     */
    public String visit(IfthenElseStatement n) {
        String _ret="";
        _ret+=n.f0.accept(this);
        _ret+=n.f1.accept(this);
        _ret+=rhsHelper(n.f2.accept(this));
        _ret+=n.f3.accept(this)+"\n";
        _ret+=n.f4.accept(this)+"\n";
        _ret+=n.f5.accept(this)+"\n";
        _ret+=n.f6.accept(this)+"\n";
        return _ret;
    }

    /**
     * f0 -> "while"
     * f1 -> "("
     * f2 -> Identifier()
     * f3 -> ")"
     * f4 -> Statement()
     */
    public String visit(WhileStatement n) {

        String _ret="";
        _ret+=n.f0.accept(this);
        _ret+=n.f1.accept(this);
        _ret+=rhsHelper(n.f2.accept(this));
        _ret+=n.f3.accept(this)+"\n";
        _ret+=n.f4.accept(this);
        return _ret;
    }

    /**
     * f0 -> "System.out.println"
     * f1 -> "("
     * f2 -> Identifier()
     * f3 -> ")"
     * f4 -> ";"
     */
    public String visit(PrintStatement n) {

        String _ret="";
        _ret+=n.f0.accept(this);
        _ret+=n.f1.accept(this);
        _ret+=rhsHelper(n.f2.accept(this));
        _ret+=n.f3.accept(this);
        _ret+=n.f4.accept(this);
        return _ret;
    }

    /**
     * f0 -> <SCOMMENT1>
     * f1 -> <LIVENESSQUERY>
     * f2 -> <SCOMMENT2>
     */
    public String visit(LivenessQueryStatement n) {
        String _ret="";
        _ret+=n.f0.accept(this);
        _ret+=n.f1.accept(this);
        _ret+=n.f2.accept(this);
        return _ret;
    }

    /**
     * f0 -> OrExpression()
     *       | AndExpression()
     *       | CompareExpression()
     *       | neqExpression()
     *       | PlusExpression()
     *       | MinusExpression()
     *       | TimesExpression()
     *       | DivExpression()
     *       | ArrayLookup()
     *       | ArrayLength()
     *       | MessageSend()
     *       | PrimaryExpression()
     */
    public String visit(Expression n) {
        String _ret = n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "&&"
     * f2 -> Identifier()
     */
    public String visit(AndExpression n) {
        String _ret="";
        _ret+=rhsHelper(n.f0.accept(this));
        _ret+=n.f1.accept(this);
        _ret+=rhsHelper(n.f2.accept(this));
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "||"
     * f2 -> Identifier()
     */
    public String visit(OrExpression n) {
        String _ret="";
        _ret+=rhsHelper(n.f0.accept(this));
        _ret+=n.f1.accept(this);
        _ret+=rhsHelper(n.f2.accept(this));
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "<="
     * f2 -> Identifier()
     */
    public String visit(CompareExpression n) {
        String _ret="";
        _ret+=rhsHelper(n.f0.accept(this));
        _ret+=n.f1.accept(this);
        _ret+=rhsHelper(n.f2.accept(this));
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "!="
     * f2 -> Identifier()
     */
    public String visit(neqExpression n) {
        String _ret="";
        _ret+=rhsHelper(n.f0.accept(this));
        _ret+=n.f1.accept(this);
        _ret+=rhsHelper(n.f2.accept(this));
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "+"
     * f2 -> Identifier()
     */
    public String visit(PlusExpression n) {
        String _ret="";
        _ret+=rhsHelper(n.f0.accept(this));
        _ret+=n.f1.accept(this);
        _ret+=rhsHelper(n.f2.accept(this));
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "-"
     * f2 -> Identifier()
     */
    public String visit(MinusExpression n) {
        String _ret="";
        _ret+=rhsHelper(n.f0.accept(this));
        _ret+=n.f1.accept(this);
        _ret+=rhsHelper(n.f2.accept(this));
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "*"
     * f2 -> Identifier()
     */
    public String visit(TimesExpression n) {
        String _ret="";
        _ret+=rhsHelper(n.f0.accept(this));
        _ret+=n.f1.accept(this);
        _ret+=rhsHelper(n.f2.accept(this));
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "/"
     * f2 -> Identifier()
     */
    public String visit(DivExpression n) {
        String _ret="";
        _ret+=rhsHelper(n.f0.accept(this));
        _ret+=n.f1.accept(this);
        _ret+=rhsHelper(n.f2.accept(this));
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "["
     * f2 -> Identifier()
     * f3 -> "]"
     */
    public String visit(ArrayLookup n) {
        String _ret="";
        _ret+= n.f0.accept(this);
        _ret+=n.f1.accept(this);
        _ret+= n.f2.accept(this);
        _ret+=n.f3.accept(this);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "."
     * f2 -> "length"
     */
    public String visit(ArrayLength n) {

        String _ret="";
        _ret+= n.f0.accept(this);
        _ret+=n.f1.accept(this);
        _ret+= n.f2.accept(this);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "."
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( ArgList() )?
     * f5 -> ")"
     */
    public String visit(MessageSend n) {
        String _ret="";
        _ret+=rhsHelper(n.f0.accept(this));
        _ret+=n.f1.accept(this);
        _ret+=n.f2.accept(this);
        _ret+=n.f3.accept(this);
        if(n.f4.present())
        _ret+=n.f4.accept(this);
        _ret+=n.f5.accept(this);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> ( ArgRest() )*
     */
    public String visit(ArgList n) {
        String _ret="";
        _ret+=rhsHelper(n.f0.accept(this));
        Vector<Node> nodes = n.f1.nodes;
        for(Node node:nodes)
            _ret+=node.accept(this);
        return _ret;
    }

    /**
     * f0 -> ","
     * f1 -> Identifier()
     */
    public String visit(ArgRest n) {
        String _ret="";
        _ret+=n.f0.accept(this);
        _ret+=rhsHelper(n.f1.accept(this));
        return _ret;
    }

    /**
     * f0 -> IntegerLiteral()
     *       | FloatLiteral()
     *       | TrueLiteral()
     *       | FalseLiteral()
     *       | Identifier()
     *       | ThisExpression()
     *       | ArrayAllocationExpression()
     *       | AllocationExpression()
     *       | NotExpression()
     */
    public String visit(PrimaryExpression n) {
        String _ret="";
        _ret+=rhsHelper(n.f0.accept(this));
        return _ret;
    }

    /**
     * f0 -> <Integer_LITERAL>
     */
    public String visit( IntegerLiteral n) {
        String _ret=n.f0.tokenImage;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> <FLOAT_LITERAL>
     */
    public String visit(FloatLiteral n) {
        String _ret=n.f0.tokenImage;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> "true"
     */
    public String visit(TrueLiteral n) {
        String _ret=n.f0.tokenImage;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> "false"
     */
    public String visit(FalseLiteral n) {
        String _ret=n.f0.tokenImage;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public String visit(Identifier n) {
        String _ret=n.f0.tokenImage;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> "this"
     */
    public String visit(ThisExpression n) {
        String _ret=n.f0.tokenImage;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> "new"
     * f1 -> "int"
     * f2 -> "["
     * f3 -> Identifier()
     * f4 -> "]"
     */
    public String visit(ArrayAllocationExpression n) {
        String _ret="";
        _ret+=n.f0.accept(this);
        _ret+=n.f1.accept(this);
        _ret+=n.f2.accept(this);
        _ret+=rhsHelper(n.f3.accept(this));
        _ret+=n.f4.accept(this);
        return _ret;
    }

    /**
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */
    public String visit(AllocationExpression n) {
        String _ret="";
        _ret+=n.f0.accept(this)+" ";
        _ret+=n.f1.accept(this);
        _ret+=n.f2.accept(this);
        _ret+=n.f3.accept(this);
        return _ret;
    }

    /**
     * f0 -> "!"
     * f1 -> Identifier()
     */
    public String visit(NotExpression n) {
        String _ret="";
        _ret+=n.f0.accept(this);
        _ret+=rhsHelper(n.f1.accept(this));
        return _ret;
    }

}
