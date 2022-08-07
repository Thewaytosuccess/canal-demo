package com.ph;

import java.util.Arrays;

public class QuickSort {

    public static void main(String[] args) {
        int[] array = {9,5,3,6,2,7,1,8};
        sort(array);
        Arrays.stream(array).forEach(e -> System.out.print(e + " "));
    }

    private static void sort(int[] array){
        int left = 0,right = array.length - 1;
        sort(left,right,array);
    }

    private static void sort(int left,int right,int[] array){
        if(left >= right){
            return ;
        }

        int i = left,j = right,key = array[left];
        while(i < j){
            while(i < j && array[j] > key) j--;
            if(i < j) array[i++] = array[j];

            while(i < j && array[i] <= key) i++;
            if(i < j) array[j--] = array[i];
        }
        array[i] = key;
        sort(left,i-1,array);
        sort(i+1,right,array);
    }
}
