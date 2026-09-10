package Hashing;

import java.util.Arrays;
import java.util.HashSet;

public class P07_IntersectionOfTwoArrays {

    public static void main(String[] args){
        int[] nums1 = {4,9,5};
        int[] nums2 = {9,4,9,8,4};
        HashSet<Integer> set1 = new HashSet<>();
        HashSet<Integer> result = new HashSet<>();

        for (int x : nums1) {
            set1.add(x);
        }

        for (int x : nums2) {
            if (set1.contains(x)) {
                result.add(x);
            }
        }

        int[] ans = new int[result.size()];
        int i = 0;

        for (int x : result) {
            ans[i++] = x;
        }

        System.out.println(Arrays.toString(ans));

    }
}
