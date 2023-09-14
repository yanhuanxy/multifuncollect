package com.yanhuanxy.multifunexport.demo.designpattern.leetcode.first;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 给你一个披萨，它由 3n 块不同大小的部分组成，现在你和你的朋友们需要按照如下规则来分披萨：
 *
 * 你挑选 任意 一块披萨。
 * Alice 将会挑选你所选择的披萨逆时针方向的下一块披萨。
 * Bob 将会挑选你所选择的披萨顺时针方向的下一块披萨。
 * 重复上述过程直到没有披萨剩下。
 * 每一块披萨的大小按顺时针方向由循环数组 slices 表示。
 *
 * 请你返回你可以获得的披萨大小总和的最大值。
 *
 输入：slices = [1,2,3,4,5,6]
 输出：10
 解释：选择大小为 4 的披萨，Alice 和 Bob 分别挑选大小为 3 和 5 的披萨。然后你选择大小为 6 的披萨，Alice 和 Bob 分别挑选大小为 2 和 1 的披萨。你获得的披萨总大小为 4 + 6 = 10 。
 *
 */
public class firstDemo {

    public static void main(String[] args) {
        int[] slices = {4,1,2,5,8,3,1,9,7};
        Integer maxVal = doCalc(slices, slices.length);
        System.out.println(maxVal);
    }

    private static Integer doCalc(int[] slices, int length){
        int[] maxValList = new int[length];
        int round = length / 3;
        for (int i = 0; i < length; i++) {
            int[] clone = slices.clone();
            int tmpVal = doMaxVal(clone, length, round, i);
            maxValList[i] = tmpVal;
        }

        return Collections.max(Arrays.stream(maxValList).boxed().collect(Collectors.toList()));
    }

    private static int doMaxVal(int[] clone, int length, int round, int i){
        int[] maxValList = {0,0,0};

        int after = 0, next = 0, cr = 1, maxIndex  = length-1;
        while (cr <= round){
            while (clone[i] == 0){
                i++;
                if(i>maxIndex){
                    i=0;
                }
            }
            maxValList[1] += clone[i];
            clone[i] = 0;

            after = i-1;
            if(after < 0){
                after = maxIndex;
            }
            while (clone[after] == 0){
                after--;
                if(after < 0){
                    after = maxIndex;
                }
            }
            maxValList[0] += clone[after];
            clone[after] = 0;

            next = i+1;
            if(next > maxIndex){
                next = 0;
            }
            while (clone[next] == 0){
                next++;
                if(next > maxIndex){
                    next = 0;
                }
            }
            maxValList[2] += clone[next];
            clone[next] = 0;

            cr++;
        }

        Integer maxVal = Collections.max(Arrays.stream(maxValList).boxed().collect(Collectors.toList()));
        if(Objects.equals(maxValList[1], maxVal)){
            return maxVal;
        }
        return 0;
    }

}
