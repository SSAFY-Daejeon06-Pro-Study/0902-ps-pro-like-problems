package kr.ac.lecture.baekjoon.Num1001_10000;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
* [문제 요약]
* 히스토그램에서 최대 직사각형 넓이를 구해라
*
* [제약 조건]
* N (1 ≤ N ≤ 100,000)
* 각 칸의 높이는 1,000,000,000보다 작거나 같은 자연수 또는 0이
*
* [문제 설명]
* 분할 정복 이용
* 중간을 짜르가
* 중간에서 좌 우측 범위 만큼 큰 쪽으로 이동
*
*
* */
public class BOJ_1725_히스토그램 {

    private static int n;
    private static int[] arr;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        n = Integer.parseInt(br.readLine());
        arr = new int[n];
        for(int i=0; i<n; i++){
            arr[i] = Integer.parseInt(br.readLine());
        }

        System.out.println(partition(0, n-1));

        br.close();
    }

    private static long partition(int left, int right) {
        if(left == right){
            return arr[left];
        }

        int mid = (left+right) / 2;
        long leftSum = partition(left, mid);
        long rightSum = partition(mid+1, right);

        return Math.max(Math.max(leftSum, rightSum), midSum(left, right, mid));
    }

    // 중앙에서부터 확인
    private static long midSum(int lo, int hi, int mid) {

        int toLeft = mid;
        int toRight = mid;

        int minHeight = arr[mid];
        int maxArea = arr[mid];

        while (lo < toLeft && toRight < hi){
            if(arr[toLeft-1] < arr[toRight + 1]){
               minHeight = Math.min(minHeight, arr[++toRight]); // 오른쪽으로
            }else{
                minHeight = Math.min(minHeight, arr[--toLeft]);
            }

            maxArea = Math.max(maxArea, minHeight * (toRight - toLeft + 1));
        }

        while (lo < toLeft){
            minHeight = Math.min(minHeight, arr[--toLeft]);
            maxArea = Math.max(maxArea, minHeight * (toRight - toLeft + 1));
        }

        while (toRight < hi){
            minHeight = Math.min(minHeight, arr[++toRight]);
            maxArea = Math.max(maxArea, minHeight * (toRight - toLeft + 1));
        }

        return maxArea;
    }
}
