package com.yanhuanxy.multifunexport.demo.designpattern.leetcode.dao100;

public class ThirdDemo {

    public static void main(String[] args) {
        int[] nums1 = {0,0,1,1,1,2,2,3,3,4};
        ThirdDemo firstDemo = new ThirdDemo();
        int i = firstDemo.removeDuplicates(nums1);
        System.out.println(i);
    }

    public int removeDuplicates(int[] nums) {
        int p1 = 0;
        for (int p2 = 0; p2 < nums.length; p2++) {
            if(nums[p2] != nums[p1]){
                nums[p1 + 1] = nums[p2];
                p1++;
            }
        }
        return p1+1;
    }

}
