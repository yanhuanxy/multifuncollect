package com.yanhuanxy.multifunexport.demo.designpattern.leetcode.dao100;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FirstDemo {

    public static void main(String[] args) {
        int[] nums1 = {1,2,3,0,0,0}, nums2 = {2,5,6};
        int m = 3,  n = 3;
        FirstDemo firstDemo = new FirstDemo();
        firstDemo.merge(nums1, m,nums2, n);
    }

    public void merge(int[] nums1, int m, int[] nums2, int n) {
        for (int j : nums2) {
            nums1[m++] = j;
        }
        for (int i = 0; i < nums1.length; i++) {
            for (int i1 = nums1.length -1; i1 > i ; i1--) {
                int tmp = nums1[i];
                int tmp2 = nums1[i1];
                if(tmp2 < tmp){
                    nums1[i] = tmp2;
                    nums1[i1] = tmp;
                }
            }
        }

        System.out.println(Arrays.stream(nums1, 0, nums1.length ).mapToObj(String::valueOf).collect(Collectors.joining(",")));
    }

    /**
     * 逆向双指针
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     */
    public void merge1(int[] nums1, int m, int[] nums2, int n) {
        int max = nums1.length -1;
        int p1 = m-1, p2 = n-1;

        while (p2>=0){
            if(p1<0 || nums1[p1] <= nums2[p2]){
                nums1[max--] = nums2[p2--];
            }else {
                nums1[max--] = nums1[p1--];
            }
        }
    }
}
