import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;


public class Graph{
    
        
    private int numNode;
    private LinkedList<Integer> adjList[];
        
    //i tried to fix the warning but no progress, so i suppressed it, it runs fine
    @SuppressWarnings("unchecked")
    public Graph(int numNode) {
        this.numNode = numNode;
        this.adjList= new LinkedList[numNode];
        //this.adjList = (LinkedList<Integer>())[numNode];
        for (int i = 0; i < numNode; i++)
            adjList[i] = new LinkedList<>();    
    }

    public void addEdge(int n, int m) {
        adjList[n].add(m);
        adjList[m].add(n);
    }

    public static Graph fromFile(String fileName, int numNode) throws IllegalArgumentException, FileNotFoundException{
        Graph graph = new Graph(numNode);

        try {
            File file = new File(fileName);
            Scanner input = new Scanner(file);
            String nodes[];
            while (input.hasNextLine()){
                String str = input.nextLine();
                nodes = str.split(" ");
                graph.addEdge(Integer.parseInt(nodes[0]), Integer.parseInt(nodes[1]));
            }
            input.close();
        }

        catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("invalid input, please try again");
        }

        catch (FileNotFoundException ex){
            throw new FileNotFoundException("file not found");
        }

        return graph;        
    }

    public static void main(String[] args) throws Exception{
    if (args.length == 1 && args[0].equals("--help")){
        System.out.println("java Graph <input file name> <number of nodes> <starting node> <output file name>");
        System.exit(1);
    }
    else if (args.length != 4){
        System.out.println("invalid input, for help enter java Graph --help");
        System.exit(1);
    }

    try{
            int numNode = Integer.valueOf(args[1]) + 1;
            Graph graph = Graph.fromFile(args[0], numNode);
            graph.BFS(graph, Integer.valueOf(args[2]), args[3]);
            graph.DFS(graph, Integer.valueOf(Integer.valueOf(args[2])), args[3]);
            //output.close();

            System.exit(0);
    }

    catch(Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    
    }


    

    public void BFS(Graph graph, int startingNode, String fileName) throws IllegalArgumentException, FileNotFoundException{
        File file;
        PrintWriter output;
        try{
            file = new File(fileName);
            output = new PrintWriter(file);
            output.println("BFS:");}
        catch (FileNotFoundException ex){
            throw new FileNotFoundException("file not found");
        }
        catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("invalid input, please try again");
        }

        int numNode = graph.numNode;
        boolean[] processed= new boolean[numNode];
        //boolean[] discovered and int[] parent are here only because the slides has them. i didn't use them to print the result because i'm more confident to print the result once the code find a new node
        boolean[] discovered= new boolean[numNode];
        //int[] parent= new int[numNode];
        //to store the node to be listed
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(startingNode);
        processed[startingNode] = true;
        while (!queue.isEmpty()){
            int node = queue.poll();
            if (!discovered[node])
                discovered[node] = true;
            //check every neighbor
            for (int i = 0; i < graph.adjList[node].size(); i++){
                int neighbor = graph.adjList[node].get(i);
                //if haven't processed neighbor yet, add it to the queue
                if (!processed[neighbor]){
                    processed[neighbor] = true;
                    //parent[neighbor] = node;
                    queue.add(neighbor);
                    //print the edge
                    output.println(node + " " + neighbor);
                }
            }
        }
        output.close();

    }

    public void DFS(Graph graph, int startingNode, String fileName) throws IllegalArgumentException, FileNotFoundException{
        PrintWriter output;
        try{
            //continue with the file writen in BFS
            output = new PrintWriter(new FileOutputStream(new File(fileName),true));
            output.println("DFS:");}
        catch (FileNotFoundException ex){
            throw new FileNotFoundException("file not found");
        }
        catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("invalid input, please try again");
        }

        int numNode = graph.numNode;
        boolean[] processed= new boolean[numNode];
        boolean[] discovered= new boolean[numNode];
        //int[] parent= new int[numNode];
        LinkedList<Integer> stack = new LinkedList<>();
        stack.push(startingNode);
        processed[startingNode] = true;
        while (!stack.isEmpty()){
            int node = stack.pop();
            if (!discovered[node])
                discovered[node] = true;
            //first check the starting node, find a neighbor, then replace the node 
            //after 1st loop, check neighbors until find a neighbor haven't processed, replace node with neighbor, repeat the process
            for (int i = 0; i < graph.adjList[node].size(); i++){
                int neighbor = graph.adjList[node].get(i);
                if (!processed[neighbor]){
                    processed[neighbor] = true;
                    //parent[neighbor] = node;
                    //add the neighbor to the stack, waiting to be poped
                    stack.push(neighbor);
                    output.println(node + " " + neighbor);
                    node = neighbor;
                }
            }
        }
        output.close();

    }

}

