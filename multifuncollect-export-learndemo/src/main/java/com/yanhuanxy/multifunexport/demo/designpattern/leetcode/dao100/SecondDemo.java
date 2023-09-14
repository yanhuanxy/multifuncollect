package com.yanhuanxy.multifunexport.demo.designpattern.leetcode.dao100;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SecondDemo {

    public static void main(String[] args) {
        int[] nums1 = {0,1,2,2,3,0,4,2};
        int m = 2;
        SecondDemo firstDemo = new SecondDemo();
        int i = firstDemo.removeElement1(nums1, m);
        System.out.println(i);
    }

    public int removeElement(int[] nums, int val) {
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            if(nums[i] == val){
                count++;
                int j = i;
                while (j+1 < nums.length){
                    nums[j] = nums[++j];
                }
                nums[j] = 0;
                i--;
            }
        }

        return nums.length - count;
    }


    public int removeElement1(int[] nums, int val) {
        int left = 0;
        int right = nums.length;
//        while (left < right){
//            if(nums[left] == val){
//                nums[left] = nums[--right];
//            }else{
//                left++;
//            }
//        }

        for (right = 0; right < nums.length; right++) {
            if(nums[right] != val){
                nums[left] = nums[right];
                left++;
            }
        }

        return left;
    }

}
