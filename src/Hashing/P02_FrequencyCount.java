package Hashing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class P02_FrequencyCount {
    public HashMap<Integer,Integer> FrequencyCount(int[] arr){
        HashMap<Integer,Integer> res = new HashMap<>();
        for(int x : arr){
            res.put(x,res.getOrDefault(x,0)+1);
        }
        return res;
    }
    public static void main(String[] args) {
        P02_FrequencyCount obj = new P02_FrequencyCount();
        Scanner sc = new Scanner(System.in);
        int[] arr = Arrays.stream(sc.nextLine().split(","))
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .toArray();
        HashMap<Integer,Integer> result = obj.FrequencyCount(arr);
        System.out.println("Array : "+Arrays.toString(arr));
        for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
            System.out.println("value : "+entry.getKey()+ " count : "+entry.getValue());
        }
    }
}