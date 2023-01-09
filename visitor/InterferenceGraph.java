package visitor;

import java.util.*;

public class InterferenceGraph {
    HashMap<String , List<String>> adjlist;
    public void copyGraph(InterferenceGraph g){
        adjlist = new HashMap<>();
        for(String x: g.adjlist.keySet()){
            adjlist.put(x,g.adjlist.get(x));
        }
    }
    public InterferenceGraph(){
        adjlist = new HashMap<>();

    }
    public void addVertex(String v1){
        if(!vertexExist(v1))
            adjlist.put(v1,new LinkedList<>());
    }
    public void deleteVertex(String v2){
        if(vertexExist(v2)){
            List<String> temp = adjlist.get(v2);
            for(String e:temp){
                adjlist.get(e).remove(v2);
            }
            adjlist.remove(v2);
        }
    }
    public boolean vertexExist(String v1){
        return adjlist.containsKey(v1);
    }
    public void addEdge(String v1,String v2){
        if(!doesEdgeExist(v1,v2))
        {
            if(adjlist.containsKey(v1)){
                adjlist.get(v1).add(v2);
            }
            else{
                List<String> temp = new LinkedList<>();
                temp.add(v2);
                adjlist.put(v1,temp);
            }
            if(adjlist.containsKey(v2)){
                adjlist.get(v2).add(v1);
            }
            else{
                List<String> temp = new LinkedList<>();
                temp.add(v1);
                adjlist.put(v2,temp);
            }
        }
    }
    public void removeEdge(String v1,String v2){
        if(adjlist.containsKey(v1) && adjlist.containsKey(v2)){
            adjlist.get(v1).remove(v2);
            adjlist.get(v2).remove(v1);
        }
    }
    public boolean doesEdgeExist(String v1,String v2){
        if(adjlist.containsKey(v1)){
            return adjlist.get(v1).contains(v2);
        }
        return false;
    }
    public int getDegree(String v){
        if(vertexExist(v))
            return adjlist.get(v).size();
        else
            return 0;
    }
    public String getNodeWithDegreeLessThanK(int k){
        String ans = "NaN";
        for(String e : adjlist.keySet()){
            if(getDegree(e)<k)
            {
                if(ans!="NaN" && getDegree(e)<=getDegree(ans))
                ans =  e;
                else if(ans == "NaN")
                    ans = e;
                else
                    continue;;
            }
        }
        return ans;
    }
    public boolean isEmpty(){
        return adjlist.keySet().size()==0;
    }
    public void addEdgeBetweenAllOfThem(Set<String> list){
        for(String s : list) {
            if (!vertexExist(s))
                addVertex(s);
        }
        for(String s1:list){
            for(String s2:list){
                if(s1!=s2){
                    addEdge(s1,s2);
                }
            }
        }
    }

    public HashMap<String,Integer> trycolor(int k){
        InterferenceGraph graph = new InterferenceGraph();
        graph.copyGraph(this);
        Stack<String> stack = graph.kemepe(k);
        HashMap<String , Integer> colorMap = new HashMap<>();
        int min = -1;
        while (!stack.isEmpty()){
            List<Integer> list = new ArrayList<Integer>(Collections.nCopies(k, 0));
            String top = stack.pop();
            List<String> neighbours = adjlist.get(top);
            if(neighbours!=null) {
                for (String s : neighbours) {
                    if (colorMap.containsKey(s)) {
                        if(colorMap.get(s)>-1)
                        list.set(colorMap.get(s), 1);
                    }
                }
            }
            boolean flag = true;
            for(int i = 0;i<k;i++){
                if(list.get(i) == 0)
                {colorMap.put(top,i);flag=false;break;}
            }
            if(flag){
                colorMap.put(top,min);
                min--;
            }
        }
        return colorMap;
    }
    private Stack<String> kemepe(int k){
        Stack<String> stringStack = new Stack<>();
        do {
            do{
                String node = getNodeWithDegreeLessThanK(k);
                if(node == "NaN")
                    break;
                deleteVertex(node);
                stringStack.push(node);
            }while(true);
            if(!isEmpty()){
                for(String s: adjlist.keySet()){
                    deleteVertex(s);
                    stringStack.push(s);
                    break;
                }
            }
        }while(!isEmpty());
        return stringStack;
    }

}
