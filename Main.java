import syntaxtree.*;
import visitor.*;

import java.util.*;

public class Main {
   public static void main(String [] args) {
      try {

         Node root = new FriendTJ(System.in).Goal();
         CFGGen cfgGen = new CFGGen();
         root.accept(cfgGen);

         ProgramCFG programCFG = cfgGen.getCFG();
         // BB.printBBDOT(programCFG);

         RunAnalysis ra = new RunAnalysis(programCFG);
         ra.startAnalysisBackward();

         // Assignment Starts here
         // Result Map contains a mapping from statements to live variables at that statement
         HashMap<Node, Set<String>> resultMap = ra.getResultMap();
         GraphMakeVisitor graphMakeVisitor = new GraphMakeVisitor(resultMap);
         root.accept(graphMakeVisitor);
         HashMap<Node,InterferenceGraph> graphHashMap = graphMakeVisitor.getGraphHashMap();
         HashMap<Node,HashMap<String,Integer>> methodAndColorMap = new HashMap<>();
         graphMakeVisitor.removeParametersFromGraph(graphHashMap);
         for(Node n : graphHashMap.keySet()){
            HashMap map = graphHashMap.get(n).trycolor(graphMakeVisitor.registerLimit);
            methodAndColorMap.put(n,map);
         }
         CodeGeneratorVisitor codeGeneratorVisitor = new CodeGeneratorVisitor(methodAndColorMap, graphMakeVisitor.registerLimit);
         root.accept(codeGeneratorVisitor);
         System.out.println(codeGeneratorVisitor.finalOutput);
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
}
