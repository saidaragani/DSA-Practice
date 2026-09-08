package Hashing;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class P01_ContainsDuplicate {
    public boolean ContainsDuplicate(int[] arr){
        HashSet<Integer> seen = new HashSet<>();
        for(int x : arr){
            if (seen.contains(x)) {
                return true;
            }
            seen.add(x);
        }
        return false;
    }

    public static void main(String[] args){
        P01_ContainsDuplicate obj = new P01_ContainsDuplicate();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the elements of array split by comma : ");
        //{ 1,2,3,2,4)
        int[] arr = Arrays.stream(sc.nextLine().split(","))
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .toArray();

        boolean result = obj.ContainsDuplicate(arr);
        System.out.println("Array : "+Arrays.toString(arr));
        System.out.println("CONTAINS DUPLICATE : "+result);

    }
}
