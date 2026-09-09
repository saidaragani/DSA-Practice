package Hashing;

import java.util.HashSet;
import java.util.Scanner;

public class P03_FirstRepeatingElement {
    public int FirstRepeatingElement(int[] arr){
        HashSet<Integer> dict = new HashSet<>();
        for(int x : arr){
            if(dict.contains(x)) {
                return x;
            }
            dict.add(x);
        }
        return -1;
    }
    public static void main(String[] args){
        P03_FirstRepeatingElement obj = new P03_FirstRepeatingElement();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the elements separated by comma");
        String[] s = sc.nextLine().split(",");
        int[] arr = new int[s.length];
        for(int i = 0;i < s.length;i++){
            arr[i] = Integer.parseInt(s[i].trim());
        }
        int result = obj.FirstRepeatingElement(arr);
        System.out.println("The first repeating element from given array : " + result);
    }
}
