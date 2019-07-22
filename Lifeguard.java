/* created by Dhairyya Agarwal
Started :15th July 2019
Ended:21st June 2019
The time complexity of this algorithm can be improved further
Except for 4.in it gives output of every other file within 5 mins
 */
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class Lifeguard {

    //class variable defined to keep track of the file we are processing between 1-10
    private  static final String name="10";
    
    public static void main(String[] args) throws Exception {

        //variable to store the number of inputs 
        int numberOfElements;
        //file to be read
        File file = new File(name+".in");
        //create scanner object that will help us read the file
        Scanner sc = new Scanner(file);
        //read and populate the number of elements
        numberOfElements = sc.nextInt();
        //arraylist to store the elements where a life guard shift begins
        ArrayList<Integer> start = new ArrayList<>(numberOfElements);
        //arraylist to store the elements where a life guard shift ends
        ArrayList<Integer> end = new ArrayList<>(numberOfElements);

        //create a set to sort the elements of both start and end in natural order 
        TreeSet<Integer> set = new TreeSet<>();
        //read the elements from the file populate the arrays and the set using the loop
        for (int i = 0; i < numberOfElements; i++) {
            //add to array
            start.add(sc.nextInt());
            end.add(sc.nextInt());
            //add to set
            set.add(start.get(i));
            set.add(end.get(i));
        }

        //create a map to map the start and end values to array indices as making an array of 1,000,000,000 gives heap error as memory is limited
        TreeMap<Integer, Integer> map = new TreeMap<>();
        int temp = 0;
        //for every element in the set add it to the map. The items are added to the map in ascending order
        for (int element : set)
            map.put(element, temp++);

        //array to store frequency. If a shift starts from here add +1 to that spot, if it ends there add -1 to that spot
        int[] freq = new int[map.size()];
        for (int i = 0; i < start.size(); i++) {
            freq[map.get(start.get(i))]++;
            freq[map.get(end.get(i))]--;
        }
        //to keep track of where do shift starts and ends

        System.out.println("Basic setup done");


        //now remove element one by one and find the maximum duration
       int max = 0;
       //variable to store the maximum duration
        for (int i = 0; i < numberOfElements; i++) {
            System.out.println(i);
            //remove the start and end element from frequency array
            freq[map.get(start.get(i))]--;
            freq[map.get(end.get(i))]++;
            //make cumulative frequency array to count the duration
            temp = makeCumulativeFrequencyArray(freq, map);
            max = Math.max(max, temp);
            //update max value
            //add the start and end element to the frequency array
            freq[map.get(start.get(i))]++;
            freq[map.get(end.get(i))]--;

        }

    //display maximum
        System.out.println(max);

        //write it to the output file
        PrintWriter out = new PrintWriter(new FileWriter(name+".out"));
        out.println(max);
        out.close();
        sc.close();
    }

    /* function to return corresponding key from the value in the map
     * input: map holding keys and values, value whose key is to be returned
     * output:key
     */
    private static int keyFromValue(int value, TreeMap<Integer, Integer> map) {
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            //check if the value of the key is equal to the value passed in
            if (entry.getValue() == value)
                //if yes return the key
                return entry.getKey();
        }
        //if no key is found return -1 indicating key with this particular value does not exist
        return -1;
    }

    /* function to return max length from cumulative frequency array
     * input: cumulative frequency array, map
     * output:length
     */
    private static int countMaxLength(int[] cumFreq, TreeMap<Integer, Integer> map) {

        //Calculate the max length in the array
        int max, on1, diff, temp;
        max = 0;
        on1 = -1;//local variables

        for (int i = 0; i < cumFreq.length; i++) {
            //iterate over the elements using the loop
            temp = cumFreq[i];
            //check to see if the element present is greater than 0. if yes and also if the starting index is not updated
            if (temp > 0 && on1 == -1)
                on1 = keyFromValue(i, map);//update it
            if (temp == 0 && on1 != -1) {//if the element present is equal to 0 and the starting index has been updated
                diff = keyFromValue(i, map) - on1;//calculate the diff in the starting and ending index and assign it to diff
                max += diff;//update max value
                on1 = -1;//making the starting position as -1 again
            }
        }
        //return the maximum length or duration from the array passed in
        return max;
    }


    /* function to make cumulative frequency array and return max length from that array
     * where maximum length is the sum of length of interval where the lifeguards are on duty
     * input: frequency array, map
     * output:maximum length
     */
    private static int makeCumulativeFrequencyArray(int[] freq, TreeMap<Integer, Integer> map) {
        // Make cumulative frequency.
        int[] cumFreq = freq.clone();
        //add the previous frequency including the current one to get the cumulative frequency at an index
        for (int i = 1; i < cumFreq.length; i++)
            cumFreq[i] += cumFreq[i - 1];
        //return the maximum length
        return countMaxLength(cumFreq, map);
    }

}