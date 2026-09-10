package Hashing;

import java.util.HashSet;

public class P08_LongestConsecutiveSequence {

    public int longestconsec(int[] nums){
        if (nums.length == 0) {
            return 0;
        }

        HashSet<Integer> set = new HashSet<>();

        // Store all unique numbers
        for (int x : nums) {
            set.add(x);
        }

        int maxCount = 0;

        for (int x : set) {

            // x is the start of a sequence
            if (!set.contains(x - 1)) {

                int current = x;
                int count = 1;

                // Count consecutive numbers
                while (set.contains(current + 1)) {
                    current++;
                    count++;
                }

                if (count > maxCount) {
                    maxCount = count;
                }
            }
        }

        return maxCount;
    }

    public static void main(String[] args){
        int[] nums = {1};
        P08_LongestConsecutiveSequence obj = new P08_LongestConsecutiveSequence();
        int res = obj.longestconsec(nums);
        System.out.println(res);

    }
}
