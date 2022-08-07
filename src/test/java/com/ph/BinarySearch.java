package com.ph;


public class BinarySearch {

    public static void main(String[] args) {
        int[] array = {0,1,2,3,5,6,7,8,9};
        int target = 4;
        int left = 0,right = array.length - 1;
        System.out.println(binarySearch(array,target,left,right));
    }

    private static int binarySearch(int[] array,int target,int left,int right) {
        if(left > right){
            return -1;
        }

        int half = left + (right - left)/2;
        if(array[half] == target){
            return half;
        }else if(array[half] < target){
            left = half + 1;
        }else{
            right = half - 1;
        }
        return binarySearch(array,target,left,right);
    }


}
