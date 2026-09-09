package Hashing;

import java.util.HashMap;
import java.util.HashSet;

public class P04_FirstNonRepeatingElement {

    public static void main(String[] args){
        int[] arr = {4, 5, 1, 4, 5, 6};
        HashMap<Integer,Integer> freq = new HashMap<>();
        for(int x : arr){
            freq.put(x,freq.getOrDefault(x,0)+1);
        }
        for(int x : arr) {
            if (freq.get(x) == 1) {
                System.out.println(x);
                break;
            }
        }
    }
}
