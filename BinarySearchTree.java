import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class BinarySearchTree {
	public Node root;
	
    public static void main(String[] args) throws Exception{
        
        if (args.length == 1 && args[0].equals("--help")){
            System.out.println("java BinarySearchTree <input file name> <output file name>");
            System.exit(1);
        }
        else if (args.length != 2){
            System.out.println("invalid input, enter java BinarySearchTree --help for help");
            System.exit(1);
        }
        
        //Scanner input = null;
        //PrintWriter output = null;
        try {
            File inFile = new File(args[0]);
            //System.out.println(inFile);
            Scanner input = new Scanner(inFile);
            //System.out.println(inFile);
            File outFile = new File(args[1]);
            PrintWriter output = new PrintWriter(outFile);
            String actions[];
            String items[];
            BinarySearchTree bst = new BinarySearchTree();
            //set array position, position[0] is the depth, position[1] is the value
            int[] position = new int[2];
            while (input.hasNextLine()){
                String str = input.nextLine();
                //System.out.println(str);
                //sperate the command and the item
                actions = str.split(" ");
                actions[0] = actions[0].strip();
                //sperate the key and the value into items[0] and items[1]
                items = actions[1].split(",");
                items[0] = items[0].strip();
                //start to check add/search/delete
                if (actions[0].equals("ADD")){
                    //check the commond valid or not
                    if (items.length != 2){
                        throw new IllegalArgumentException("invalid input included, please check input file");
                    }
                    else{
                        //System.out.println("1");
                        position = bst.insert(items[0], Integer.valueOf(items[1]));
                        output.println("Added (" + actions[1] +") at depth " + position[0] + ".");
                    }
                }
                else if (actions[0].equals("SEARCH")){
                    //for search and delete, actions[1] is the key, no need for items
                    if (items.length != 1){
                        throw new IllegalArgumentException("invalid input included, please check input file");
                    }
                    else{
                        //System.out.println("2");
                        position = bst.search(actions[1]);
                        if(position[0] == -1){
                            output.println("Not found: " + actions[1] + ".");
                        }
                        else{
                            output.println("Found (" + actions[1] + "," + position[1] + ") at depth "+ position[0] + ".");                   
                        }
                    }
                
                }
                else if (actions[0].equals("DELETE")){
                    if (items.length != 1){
                        throw new IllegalArgumentException("invalid input included, please check input file");
                    }
                    else{
                        //System.out.println("3");
                        position = bst.delete(actions[1]);
                        if(position[0] == -1){
                            output.println("Cannot delete: " + actions[1] + ".");
                        }
                        else{
                            output.println("Deleted (" + actions[1]  + "," + position[0] + ").");             
                        }
                    }
                }
                else
                    throw new IllegalArgumentException("invalid input included, please check input file");
            }            
            input.close();
            output.close();
            System.exit(0);
        }

        catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("invalid input, please try again");
        }

        catch (FileNotFoundException ex){
            throw new FileNotFoundException("file not found");
        }

        catch(Exception e)
        {
            System.out.println(e.getMessage());
            throw e;
        }
        /*finally{
            if (input != null)
                input.close();
            if (output != null)
                output.close();
        }*/      
    }

    class Node {
        String key;
        int value;
        Node left;
        Node right;
        Node parent;

        public Node(String name, int count) {
            this.key = name;
            this.value = count;
            left = null;
            right = null;
        }
    }

    //insert node and return the position of the node
    public int[] insert(String key, int value) {
        Node node = new Node(key, value);
        int[] position= new int[2];
        position[0] = 1;
        if (root == null) {
            root = node;
            return position;
        }

        Node current = root;
        //after each inssertion, position will be increased by 1
        while (current != null) {
            if (key.compareTo(current.key) < 0) {
                if (current.left == null) {
                    current.left = node;
                    node.parent = current;
                    position[0]++;
                    break;
                } 
                current = current.left;
                position[0]++;
            }
            else if (key.compareTo(current.key) > 0) {
                if (current.right == null) {
                    current.right = node;
                    node.parent = current;
                    position[0]++;
                    break;                   
                }
                current = current.right;
                position[0]++;
            }
            else
                throw new IllegalArgumentException("Duplicate key, please check input file");
        }
        return position;
    }

    //search return an array which hold the depth and value
    public int[] search(String key){
        int[] position = new int[2];
        //nothing is bst
        if (root == null){
            position[0] = -1;
            return position;
        }

        Node current = root;
        position[0] = 1;
        while (current != null){
            //position[0] = 1;
            if (key.compareTo(current.key) == 0){
                position[1] = current.value;
                return position;
            }
            else if (key.compareTo(current.key) < 0){
                current = current.left;
                position[0]++;
            }
            else{
                current = current.right;
                position[0]++;        
            }
        }
        //didn't find the key
        if (current == null){
            position[0] = -1;
        }

        return position;
    }

    public int[] delete(String key){
        int[] position= new int[2];

        if (root == null){
            position[0] = -1;
            return position;       
        }

        Node current = root;
        position[0] = 1;
        //same as search, to locate the node
        //when the bst is not empty
        while (current != null){
            //if found the key
            if (key.compareTo(current.key) == 0){
                //if the node has no children
                if (current.left == null && current.right == null){
                    //if current is the root
                    if (root == current)
                        root = null;
                    else if (current.parent.left == current)
                        current.parent.left = null;
                    else
                    current.parent.right = null;
                return position;
                }

                //if the node has only right child
                else if (current.left == null){
                    if (root == current){
                        root = current.right;
                        current.right.parent = null;
                    }    
                    else if (current.parent.left == current){
                        current.parent.left = current.right;
                        current.right.parent = current.parent;
                    }
                    else{
                        current.parent.right = current.right;
                        current.right.parent = current.parent;
                    }
                    return position;
                }

                //if the node has only left child
                else if (current.right == null){
                    if (root == current){
                        root = current.left;
                        current.left.parent = null;
                    }
                    else if (current.parent.left == current){
                        current.parent.left = current.left;
                        current.left.parent = current.parent;
                    }
                    else{
                        current.parent.right = current.left;
                        current.left.parent = current.parent;
                    }
                    return position;
                }

                //if the node has two children
                else if (current.left != null && current.right != null){
                    Node min = current.right;
                    //Node parent = current.parent;
                    //min has no left child, min is the smallest
                    if (min.left == null){
                        if (root == current){
                            root = min;
                            min.parent = null;
                        }
                        else if (current.parent.left == current){
                            current.parent.left = min;
                            min.parent = current.parent;
                        }
                        else{
                            current.parent.right = min;
                            min.parent = current.parent;
                        }
                        return position;
                    }
                    //min has left child
                    else if (min.left != null){
                        Node tmp = current.right;
                        //make min the smallest in the bst
                        while (min.left != null){
                            min = min.left;
                        }
                        if (root == current){
                            min.parent = null;
                            tmp.left = min.right;
                            if (min.right != null)
                                min.right.parent = tmp;
                            min.right = tmp;
                            tmp.parent = min;
                            min.left = current.left;
                            min.left.parent = min;
                            root = min;
                        }
                        else if (current.parent.left == current){
                            if (min.right != null){
                                min.parent.left = min.right;
                                min.right.parent = min.parent;
                            }
                            current.parent.left = min;
                            min.parent = current.parent;
                            min.right = tmp;
                            tmp.parent = min;
                        }
                        else{
                            //connect min's parent and min's right child
                            if (min.right != null){
                                min.parent.left = min.right;
                                min.right.parent = min.parent;
                            }
                            current.parent.right = min;
                            min.parent = current.parent;
                            min.right = tmp;
                            tmp.parent = min;
                        }
                    }
                    return position;
                }
            }
            //continue to search if the key is not found
            else if (key.compareTo(current.key) < 0){
                current = current.left;
                position[0]++;
            }
            else{
                current = current.right;
                position[0]++;        
            }
        }
        //not found
        if (current == null){
            position[0] = -1;
            return position;
        }
        return position;
    }

}
