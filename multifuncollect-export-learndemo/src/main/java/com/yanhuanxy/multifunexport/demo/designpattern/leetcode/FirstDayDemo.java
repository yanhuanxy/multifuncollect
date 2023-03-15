package com.yanhuanxy.multifunexport.demo.designpattern.leetcode;

/**
 * 你正在参加一场比赛，给你两个 正 整数 initialEnergy 和 initialExperience 分别表示你的初始精力和初始经验。
 *
 * 另给你两个下标从 0 开始的整数数组 energy 和 experience，长度均为 n 。
 *
 * 你将会 依次 对上 n 个对手。第 i 个对手的精力和经验分别用 energy[i] 和 experience[i] 表示。当你对上对手时，需要在经验和精力上都 严格 超过对手才能击败他们，然后在可能的情况下继续对上下一个对手。
 *
 * 击败第 i 个对手会使你的经验 增加 experience[i]，但会将你的精力 减少  energy[i] 。
 *
 * 在开始比赛前，你可以训练几个小时。每训练一个小时，你可以选择将增加经验增加 1 或者 将精力增加 1 。
 *
 * 返回击败全部 n 个对手需要训练的 最少 小时数目。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：initialEnergy = 5, initialExperience = 3, energy = [1,4,3,2], experience = [2,6,3,1] 输出：8 解释：在 6 小时训练后，你可以将精力提高到 11 ，并且再训练 2 个小时将经验提高到 5 。 按以下顺序与对手比赛： - 你的精力与经验都超过第 0 个对手，所以获胜。 精力变为：11 - 1 = 10 ，经验变为：5 + 2 = 7 。 - 你的精力与经验都超过第 1 个对手，所以获胜。 精力变为：10 - 4 = 6 ，经验变为：7 + 6 = 13 。 - 你的精力与经验都超过第 2 个对手，所以获胜。 精力变为：6 - 3 = 3 ，经验变为：13 + 3 = 16 。 - 你的精力与经验都超过第 3 个对手，所以获胜。 精力变为：3 - 2 = 1 ，经验变为：16 + 1 = 17 。 在比赛前进行了 8 小时训练，所以返回 8 。 可以证明不存在更小的答案。
 *
 * 示例 2：
 *
 * 输入：initialEnergy = 2, initialExperience = 4, energy = [1], experience = [3] 输出：0 解释：你不需要额外的精力和经验就可以赢得比赛，所以返回 0 。
 *
 *  
 *
 * 提示：
 *
 * n == energy.length == experience.length
 *
 * 1 <= n <= 100
 *
 * 1 <= initialEnergy, initialExperience, energy[i], experience[i] <= 100
 */
public class FirstDayDemo {
    /**
     * 精力
     */
    private int currentEnergy;

    /**
     * 经验
     */
    private int currentExperience;

    /**
     * n -> energy[i] 表示第i位对手的精力
     * 对手精力
     */
    private int[] energys;

    /**
     * n -> experience[i] 表示第i位对手的经验
     * 对手经验
     */
    private int[] experiences;

    /**
     * 锻炼系数
     */
    private static final int exerciseCoefficient = 1;


    public FirstDayDemo(int currentEnergy, int currentExperience) throws Exception {

        checkMaxNum(currentEnergy);
        this.currentEnergy = currentEnergy;

        checkMaxNum(currentExperience);
        this.currentExperience = currentExperience;
    }

    /**
     * 初始化敌人信息
     * @param energy 精力
     * @param experience 经验
     */
    public void initEnemyInfo(int[] energy, int[] experience) throws Exception{
        if(energy.length != experience.length){
            throw new Exception("敌人基本信息量不匹配");
        }

        checkMaxNum(energy.length);
        this.energys = energy;

        checkMaxNum(experience.length);
        this.experiences = experience;
    }

    /**
     * 校验值的区间
     * @param n 数值
     */
    private void checkMaxNum(int n) throws Exception{
        if(n <= 0 || n > 100){
            throw new Exception("数值区间必须在 1 <= n <= 100");
        }
    }


    public static void main(String[] args) throws Exception {
        int initialEnergy = 5, initialExperience = 3;
        FirstDayDemo firstDayDemo = new FirstDayDemo(initialEnergy, initialExperience);
        int[] energys = {1,4,3,2}, experiences = {2,6,3,1};
        firstDayDemo.initEnemyInfo(energys, experiences);
        int minHours = firstDayDemo.tryKillAllEnemySpendHours();
        System.out.println(minHours);
    }

    /**
     * 尝试击败所有对手 花费时间数
     */
    public int tryKillAllEnemySpendHours(){
        int maxEnumyEnergyNum = 0, maxEnumyExperienceNum = 0;
        int n = this.experiences.length;
        for (int i = 0; i < n; i++) {
            maxEnumyEnergyNum += this.energys[i];
            maxEnumyExperienceNum += this.experiences[i];
        }
        boolean firstKill = checkKillEnemy(this.currentEnergy, this.currentExperience, maxEnumyEnergyNum, maxEnumyExperienceNum);
        if(firstKill){
            return 0;
        }
        // 需要的精力
        int energyDifferVal = maxEnumyEnergyNum - this.currentEnergy + 1;
        // 锻炼精力次数
        int maxTrainEnergyNum = energyDifferVal / exerciseCoefficient;
        // 锻炼后的精力
        this.currentEnergy += energyDifferVal;
        // 需要的经验
        // 最多锻炼经验次数
        int maxTrainExperienceNum = 0;
        for (int i = 0; i < n; i++) {
            int enemyEnergy = this.energys[i];
            int enemyExperience = this.experiences[i];
            if(this.currentExperience > enemyExperience){
                // 击败
                killEnemy(enemyEnergy, enemyExperience);
            }else {
                // 锻炼经验次数
                int experienceDifferVal = enemyExperience - this.currentExperience + 1;
                maxTrainExperienceNum += experienceDifferVal / exerciseCoefficient;
                // 击败
                killEnemy(enemyEnergy, experienceDifferVal);
            }
        }
        return maxTrainEnergyNum + maxTrainExperienceNum;
    }


    /**
     * 击败敌人 失去精力 获得经验
     * @param enemyEnergy 精力
     * @param enemyExperience 经验
     */
    private void killEnemy(int enemyEnergy, int enemyExperience){
        this.currentEnergy -= enemyEnergy;
        this.currentExperience += enemyExperience;
    }

    /**
     * 击败对手
     * @param meEnergy 自己精力
     * @param meExperience 自己经验
     * @param enemyEnergy 敌人精力
     * @param enemyExperience 敌人经验
     */
    private boolean checkKillEnemy(int meEnergy, int meExperience, int enemyEnergy, int enemyExperience){
        return meEnergy > enemyEnergy && meExperience > enemyExperience;
    }
}
