import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.lang.model.util.ElementScanner14;

import java.util.Comparator;
import java.util.Queue;

class Entry
{
    private final String name;
    private final Integer count;

    public Entry(String name, Integer count)
    {
        this.name = name;
        this.count = count;
    }

    public String getName()
    {
        return name;
    }

    public Integer getCount()
    {
        return count;
    }

    public String toString()
    {
        return String.format("%s,%d", this.name, this.count);
    }

    public static Entry[] fromFile(String fileName, int numEntries)
    throws IllegalArgumentException, FileNotFoundException
    {
        /* TODO: 
         * Read the entries from a text file and return an array of Entry objects 
         */

        Entry entries[] = new Entry[numEntries];

        try {
            File file = new File(fileName);
            Scanner input = new Scanner(file);
            String items[];
            for (int i = 0; i < numEntries; i++){
                String str = input.nextLine();
                items = str.split(",");
                entries[i] = new Entry(items[0], Integer.parseInt(items[1]));
            }
            input.close();

        }
        catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("invalid input, please try again");
        }

        catch (FileNotFoundException ex){
           throw new FileNotFoundException("file not found");
        }

        return entries;
        
    }

    public static void toFile(String fileName, Entry[] entries, int numEntries)
    throws IllegalArgumentException, FileNotFoundException
    {
        /* TODO: 
         * Write an array of Entry objects into a text file 
         */
        try{
            File file = new File(fileName);
            PrintWriter output = new PrintWriter(file);
            for(int i = 0; i < numEntries; i++){           
                output.println(entries[i]);
            }
            output.close();
        }
        catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("invalid input, please try again");
        }
        catch (FileNotFoundException ex){
            throw new FileNotFoundException("file not found");
        }

        
    }
}

class NameComparator implements Comparator<Entry>
{
    /* TODO: 
     * Compare two Entry objects based on their names (alphabetic order)
     */
    public int compare(Entry e1, Entry e2){
        return e1.getName().compareTo(e2.getName());
    }
}

class CountComparator implements Comparator<Entry>
{
    /* TODO: 
     * Compare two Entry objects based on their counts (ascending order)
     */
    public int compare(Entry e1, Entry e2){
        return e1.getCount().compareTo(e2.getCount());
    }
}

class NameThenCountComparator implements Comparator<Entry>
{
    /* TODO: 
     * Compare two Entry objects based on their names and then based on their counts (if the names are same)
     */
    public int compare(Entry e1, Entry e2){
        int result = e1.getName().compareTo(e2.getName());
        if(result == 0)
            result = e1.getCount().compareTo(e2.getCount());
        return result;
    }
}

class CountThenNameComparator implements Comparator<Entry>
{
    /* TODO: 
     * Compare two Entry objects based on their counts and then based on their names (if the counts are same)
     */
    public int compare(Entry e1, Entry e2){
        int result = e1.getCount().compareTo(e2.getCount());
        if(result == 0)
            result = e1.getName().compareTo(e2.getName());
        return result;
    }
}

public class Sorter
{
    private void sort(Entry[] items, Comparator<Entry> comparator)
    {
        /* TODO: 
         * Sort the array of Entry objects based on the comparator using the Merge Sort algorithm.
         */
        //based on Skiena’s textbook
        mergeSort(items, 0, items.length - 1, comparator);
        
    }
    private void mergeSort(Entry[] items, int low, int high, Comparator<Entry> comparator){
        int middle;
        if (low < high) {
            middle = low + (high - low) / 2;
            mergeSort(items, low, middle, comparator);
            mergeSort(items, middle + 1, high, comparator);
            merge(items, low, middle, high, comparator);
        }   
    }
    private void merge(Entry[] items, int low, int middle, int high, Comparator<Entry> comparator){
            int i;
            //buffers to hold items;
            Queue<Entry> buffer1;
            Queue<Entry> buffer2;
            buffer1 = new java.util.LinkedList<Entry>();
            buffer2 = new java.util.LinkedList<Entry>();

            //add items to buffer1 and buffer2;
            for (i = low; i <= middle; i++)
                buffer1.offer(items[i]);
            for (i = middle + 1; i <= high; i++)
                buffer2.offer(items[i]);

            //merge buffer1 and buffer2;
            i = low;
            while (!buffer1.isEmpty() && !buffer2.isEmpty()) {
                if (comparator.compare(buffer1.peek(), buffer2.peek()) <= 0)
                    items[i++] = buffer1.poll();
                else
                    items[i++] = buffer2.poll();
            }
            while (!buffer1.isEmpty())
                items[i++] = buffer1.poll();
            while (!buffer2.isEmpty())
                items[i++] = buffer2.poll();
        
        }


    private Comparator<Entry> getComparator(String mode) throws IllegalArgumentException
    {
        /*
         * Return the appropriate comparator based on the value of mode.
         * Permitted values of mode are "name", "count", "nameThenCount", and "countThenName".
         */
        if (mode.equals("name"))
            return new NameComparator();

        else if (mode.equals("count"))
            return new CountComparator();
                
        else if (mode.equals("nameThenCount"))
            return new NameThenCountComparator();
                
        else if (mode.equals("countThenName"))
            return new CountThenNameComparator();

        else
            throw new IllegalArgumentException("invalid input, please try again");
        
    }

    public static void main(String[] args) throws Exception
    {
        /* TODO:
         * Ensure that the user can run this program only in the following two ways:
           
           OPTION 1:
           java Sorter <input file> <number of entries> <mode> <output file>

           OPTION 2:
           java Sorter --help
        */
        if (args.length == 1 && args[0].equals("--help")){
            System.out.println("To sort a file please enter the following: java Sorter <input file> <number of entries> <mode> <output file>");
            System.exit(1);
        }
        else if (args.length != 4){
            System.out.println("invalid input, enter java Sorter --help for help");
            System.exit(1);
        }
        
        
        try
        {
            int numEntries = Integer.valueOf(args[1]);
            Entry[] entries = Entry.fromFile(args[0], numEntries);
            
            Sorter sorter = new Sorter();
            Comparator<Entry> comparator = sorter.getComparator(args[2]);
 
            sorter.sort(entries, comparator);
            Entry.toFile(args[3], entries, numEntries);

            System.exit(0);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
