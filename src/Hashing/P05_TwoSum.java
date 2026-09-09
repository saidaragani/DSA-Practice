package Hashing;

import java.util.HashMap;

public class P05_TwoSum {
    public static void main(String[] args){
        int[] arr = {4, 5, 1, 4, 5, 16};
        int target = 20;
        HashMap<Integer,Integer> dict = new HashMap<>();
        for(int x=0;x<arr.length;x++){
            int complement = target-arr[x];
            if(dict.containsKey(complement)){
                System.out.println("{ "+dict.get(complement)+" , "+x+" }");
                break;
            }
            if (!dict.containsKey(arr[x])) {
                dict.put(arr[x], x);
            }
        }
    }
}
