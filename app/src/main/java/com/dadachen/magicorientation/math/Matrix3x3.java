package com.dadachen.magicorientation.math;

/**
 * Created with IntelliJ IDEA.
 * User: Majid Golshadi
 * Date: 2/10/14
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class Matrix3x3 {

    public static float[] multiplication(float[] A, float[] B) {
        float[] result = new float[9];

        result[0] = A[0] * B[0] + A[1] * B[3] + A[2] * B[6];
        result[1] = A[0] * B[1] + A[1] * B[4] + A[2] * B[7];
        result[2] = A[0] * B[2] + A[1] * B[5] + A[2] * B[8];

        result[3] = A[3] * B[0] + A[4] * B[3] + A[5] * B[6];
        result[4] = A[3] * B[1] + A[4] * B[4] + A[5] * B[7];
        result[5] = A[3] * B[2] + A[4] * B[5] + A[5] * B[8];

        result[6] = A[6] * B[0] + A[7] * B[3] + A[8] * B[6];
        result[7] = A[6] * B[1] + A[7] * B[4] + A[8] * B[7];
        result[8] = A[6] * B[2] + A[7] * B[5] + A[8] * B[8];

        return result;
    }

    public static float[] mul3x3x3x1(float[] matrix3x3, float[] matrix3x1){
        float[] res = new float[3];
        res[0] = matrix3x3[0]* matrix3x1[0] + matrix3x3[1]* matrix3x1[1] + matrix3x3[2]*matrix3x1[2];
        res[1] = matrix3x3[3]* matrix3x1[0] + matrix3x3[4]* matrix3x1[1] + matrix3x3[5]*matrix3x1[2];
        res[2] = matrix3x3[6]* matrix3x1[0] + matrix3x3[7]* matrix3x1[1] + matrix3x3[8]*matrix3x1[2];
        return res;
    }
}
