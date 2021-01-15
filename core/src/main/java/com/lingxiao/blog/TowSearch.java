package com.lingxiao.blog;

public class TowSearch {

    public static void main(String[] args) {
        int[] nums = new int[]{-1, 0, 3, 5, 9, 12};
        TowSearch towSearch = new TowSearch();
        //int searchIndex = towSearch.search(nums, 12);
        int searchIndex = towSearch.guessNumber(2126753400);
        System.out.println(searchIndex);
    }

    public int search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int middleIndex = left + (right - left) / 2;
            int middle = nums[middleIndex];
            if (middle > target) {
                right = middleIndex - 1;
            } else if (middle < target) {
                left = middleIndex + 1;
            } else {
                return middleIndex;
            }
        }
        return -1;
    }

    public int mySqrt(int x) {
        int left = 0;
        int right = x;
        int ans = -1;
        while (left <= right){
            int middle = left + (right - left) / 2;
            if((long)middle*middle <= x){
                ans = middle;
                left = middle + 1;
            }else {
                right = middle - 1;
            }
        }
        return ans;
    }

    public int guessNumber(int n) {
        int left = 1;
        int right = n;
        while (left <= right){
            int middle = left + (right - left)/2;
            int result = guess(middle);
            if (result == 0){
                return middle;
            }else if (result == -1){
                right = middle -1;
            }else if (result == 1){
                left = middle + 1;
            }
        }
        return 1;
    }

    private int target = 2126753390;
    private int guess(int num){
        if (num > target){
            return -1;
        }else if (num < target){
            return 1;
        }
        return 0;
    }
}