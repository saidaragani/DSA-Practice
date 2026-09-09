package Hashing;

import java.util.HashMap;

public class P06_CountPairsWithGivenSum {
    static public void main(String[] args){
        int[] arr = {1, 5, 7, -1, 5};
        int count = 0;
        int target = 6;
        HashMap<Integer,Integer> freq = new HashMap<>();
        for(int x=0;x<arr.length;x++){
            int complement = target - x;
            count += freq.getOrDefault(complement, 0);
            freq.put(x, freq.getOrDefault(x, 0) + 1);
        }
        System.out.println(count);
    }
}
