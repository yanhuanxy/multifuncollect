package com.yanhuanxy.multifunexport.demo.designpattern.leetcode;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 给你一个长度为 n 的数组 nums ，该数组由从 1 到 n 的 不同 整数组成。另给你一个正整数 k 。
 *
 * 统计并返回 nums 中的 中位数 等于 k 的非空子数组的数目。
 *
 * 注意：
 *
 * 数组的中位数是按 递增 顺序排列后位于 中间 的那个元素，如果数组长度为偶数，则中位数是位于中间靠 左 的那个元素。
 *
 * 例如，[2,3,1,4] 的中位数是 2 ，[8,4,3,5,1] 的中位数是 4 。
 *
 * 子数组是数组中的一个连续部分。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：nums = [3,2,1,4,5], k = 4 输出：3 解释：中位数等于 4 的子数组有：[4]、[4,5] 和 [1,4,5] 。
 *
 * 示例 2：
 *
 * 输入：nums = [2,3,1], k = 3 输出：1 解释：[3] 是唯一一个中位数等于 3 的子数组。
 *
 *  
 *
 * 提示：
 *
 * n == nums.length
 *
 * 1 <= n <= 105
 *
 * 1 <= nums[i], k <= n
 *
 * nums 中的整数互不相同
 */
public class FourthDayDemo {

    private final int[] nums;

    private final int k;

    private final List<List<Integer>> resultList = new ArrayList<>();

    public FourthDayDemo(int[] nums, int k) {
        int n = nums.length;
        if(1 > n || n > 105){
            throw new RuntimeException("数组长度在 1 <= n <= 105 ");
        }
        boolean error = Arrays.stream(nums).anyMatch(item -> item < 1);
        if(error){
            throw new RuntimeException("数组元素 1 <= nums[i]");
        }
        if(k > n){
            throw new RuntimeException("数组长度在 k <= n ");
        }

        this.nums = nums;
        this.k = k;
    }

    public static void main(String[] args) throws InterruptedException {
        int[] nums = {2,3,1};
        int k = 3;
        FourthDayDemo fourthDayDemo = new FourthDayDemo(nums, k);
        int medianNoNullSubSetNums = fourthDayDemo.getMedianNoNullSubSetNums();
        System.out.println(medianNoNullSubSetNums);
    }

    /**
     * 获取 中位数 的子集数组 数量
     */
    public int getMedianNoNullSubSetNums(){
        // 定位 中位数的下标
        // 获取 中位数最大的数组 中位数数组 定义 由小到大递增
        List<Integer> subList = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            int tmpVal = nums[i];
            if(k == tmpVal){
                int j = i,l = i;
                subList.add(k);
                while (j+1 < nums.length){
                    int jval =  nums[j++];
                    int tmpjval = nums[j];
                    if(tmpjval < jval){
                        break;
                    }
                    int lval =  nums[l--];
                    int tmplval =  nums[l];
                    if(tmplval > lval){
                        break;
                    }
                    subList.add(0, tmplval);
                    subList.add(tmpjval);
                }
                break;
            }
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(subList));

        // 数组长度 偶数-> 中位数 中间位置靠左元素
        // 数组长度 奇数-> 中位数 中间位置
        getSubList(subList, new ArrayList<>(),0);

        System.out.println(gson.toJson(resultList));
        return resultList.size();
    }

    private void getSubList(List<Integer> sourceList, List<Integer> tmpList, int level){
        if(level == sourceList.size()){
            if(tmpList.isEmpty()){
                return;
            }
            int size = tmpList.size();
            int halfIndex;
            if(size % 2 == 0){
                // 数组长度 偶数-> 中位数 中间位置靠左元素
                halfIndex = size/2 - 1;
            }else{
                //数组长度 奇数-> 中位数 中间位置
                halfIndex = size/2;
            }
            int median = tmpList.get(halfIndex);
            if(median == k){
                resultList.add(tmpList);
            }
        }else{
            getSubList(sourceList, new ArrayList<>(tmpList), (level + 1));
            tmpList.add(sourceList.get(level));
            getSubList(sourceList, new ArrayList<>(tmpList), (level + 1));
        }
    }


}
