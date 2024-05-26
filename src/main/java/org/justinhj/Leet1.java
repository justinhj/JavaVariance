package org.justinhj;

import java.util.Arrays;

public class Leet1 {
// try some example
// aaaa       ddddddd
//    bbbb ccccc
// how many combinations?
// 4 ways to hit a, 4 to hit b, 5 to hit c and 7 to hit d
// we can make a map of x coord to shoot at as a key
// value is the set of balloons at that point
// up to 100000 balloons
// one solution is to brute force the combinations, all the ways to pop
// a, b then c and so on.

    // Iteration should be for each balloon
    //   least shots = inf
    //   for each point of the balloon
    //      shots = calculate recursively
    //      least shots = min(shots, least shots)

    // observe... at each point we know all subsequent balloons popped
    // we still need to run through all the balloons after each shoot and remove them
    // we need an active set of balloons at each point

    private static int solve(int [] balloon, int[][] remaining) {
        int leastShots = Integer.MAX_VALUE;
        for(int i=balloon[0]; i<=balloon[1]; i++) {
            //
        }
        return leastShots;
    }

    public static int findMinArrowShots(int[][] points) {
        return solve(points[0], Arrays.copyOfRange(points, 1, points.length));
    }

    public static void main(String args[]) {
        int [][] caseOne = {{0,16},{2,8},{1,6},{7,12}};
        int result = findMinArrowShots(caseOne);
        System.out.println(result);
    }
}