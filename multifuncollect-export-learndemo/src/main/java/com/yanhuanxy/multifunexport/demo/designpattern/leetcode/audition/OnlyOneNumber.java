package com.yanhuanxy.multifunexport.demo.designpattern.leetcode.audition;

/**
 * 只出现一次的数字
 *
 * 给你一个 非空 整数数组 nums ，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
 *
 * 你必须设计并实现线性时间复杂度的算法来解决此问题，且该算法只使用常量额外空间。
 *
 *  
 *
 * 示例 1 ：
 *
 * 输入：nums = [2,2,1]
 * 输出：1
 * 示例 2 ：
 *
 * 输入：nums = [4,1,2,1,2]
 * 输出：4
 * 示例 3 ：
 *
 * 输入：nums = [1]
 * 输出：1
 *  
 *
 * 提示：
 *
 * 1 <= nums.length <= 3 * 104
 * -3 * 104 <= nums[i] <= 3 * 104
 * 除了某个元素只出现一次以外，其余每个元素均出现两次。
 *
 * 作者：LeetCode
 * 链接：https://leetcode.cn/leetbook/read/top-interview-questions/xm0u83/
 * 来源：力扣（LeetCode）
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class OnlyOneNumber {

    public static void main(String[] args) {
        //  0001 1  4^1->5 0101
        //  0010 2  10^2->4 0100
        //  0011 3  5^3->10 0110
        //  0100 4  1^4->5 0101
        //  0101 5  4^5->1
        //  异或运算规则 -> 相同取0，相异取1


        int[] arr = {1,2,3,4,5,4,3,2,1};
        int result = 0;
        for (int i = 0; i < arr.length; i++) {
            result ^= arr[i];
        }
        System.out.println(4^5);
        System.out.println(result);
    }

}
