package B형4주차_자유;

import java.util.ArrayList;
import java.util.List;

/**
 * 설계 시작 시간: 오후 7시 32분
 * 
 * 2N개의 카드가 있다. (2<= N <= 50000)
 * 페어는 동일한 숫자가 적힌 서로다른 카드 한쌍
 * 
 * 
 * 1. 나열된 카드 중 2개를 선택하고, 
 * 		각 카드에 적힌 숫자 차이의 상한값 추측
 * 2. 심판은 두 카드에 적힌 숫자의 차이가 추측한 상한값과
 * 		 같거나 작은지 여부를 확인 후 알려줌
 * 3. 만약 추측한 상한 값이 0이고, 두 카드의 숫자가 같은 경우 페어찾음
 * 4. 아직 찾지 못한 페어가 있는 경우, 1번부터 다시 진행
 * 
 * playGame(N) => 서로 다른 페어의 개수 N이 주어짐과 게임시작
 * 
 * checkCard(int mIndexA, int mIndexB, int mDiff){
 * 
 * } => 0<=A, B <= 2N-1
 * mDiff == 0 && arr[mIndexA] == arr[mIndexB]
 * => 페어찾음
 * 
 * 문제 해결을 위한 고민)
 * checkCard를 한다 한들 값을 알려주지 않는다.
 * 단지 true/false만 알려줌
 * => 모든 수가 미지수이므로 기준을 먼저 제시하는게 포인트
 * 
 * 아이디어)
 * 처음 값(a)이 들어오면, 맨처음값으로 한번 전체 순회한다.
 * => checkCard(a, x_i, N/2 or N/4)
 * 
 * 이 과정을 거치면 기준이 생긴다.
 * 
 * a 쁠마 N/4로 TRUE 혹은 false로 전체 수가 나뉜다 => 미지수들에 값에 대한 기준 부여
 * 그럼 이제 true인 집단에서 또 다시 N/(4*2) = N/8로 다시 값에 대한 기준을 부여한다.
 * 
 * 분할 정복을 진행하다보면 이렇게됨
 * 1~12500, 12501~25000, 25001 ~37500, 37501 ~ 50000
 * 여기서 범위를 /2 씩 깎으면 이렇게 줄어든다
 * 
 * 1) /2씩 깎을떄
 * 1~6250, 6251~12500, ...
 * 1~3125, 3126~6250,
 * 1~1600,
 * 1~800
 * 
 * 이렇게해서 자기자신만 남을때까지 계속 나누면 됨 
 * 예제에 대입)
 * 
 * 4 3 2 4 2 1 1 3
 * => N=4
 * check(4, x, 2)
 * => true true true true true false false true
 * => 4, 3, 2, 4, 2, 3
 * => 1, 1
 * 4, 3, 2, 4, 2, 3, => 1
 * => true true false true false true
 * 
 * 
 */
public class 모든페어찾기
{
	// 주석 처리 하기 
	static int N, K, foundCnt;
	static boolean ok = true;
	static boolean found[];
	static int[] cards = new int[] {3, 2, 4, 7, 8, 1, 6, 7, 8, 6, 1, 5, 2, 5, 3, 4};
    public static void playGame()
    {
        // 두 카드의 숫자를 비교하기 위해 아래와 같이 checkCards 함수를 사용합니다.
        // <영문>
        //
        // Solution.checkCards(mIndexA, mIndexB, mDiff);	
    	
    	// 값 초기화 (주석처리하기)
    	N = 8;
    	foundCnt = 0;
    	found = new boolean[N+1];
    	
    	// 기준 부여 List
    	List<Integer> lowerThanCriterion = new ArrayList<Integer>();
    	List<Integer> biggerThanCriterion = new ArrayList<Integer>();

    	
    	// 0은 당연히 lower 그룹에 들어감
    	lowerThanCriterion.add(0);
    	
    	// 기준값 부여
    	for(int i=1; i<2*N; i++) {
    		if(checkCards(0, i, N/2)) {
    			lowerThanCriterion.add(i);
    		} else {
    			biggerThanCriterion.add(i);
    		}
    	}
    	
    	// 분할 정복
    	divideAndConquer(0, N-1, lowerThanCriterion);
    	if(biggerThanCriterion.size() > 0) divideAndConquer(biggerThanCriterion.get(0), N-1, biggerThanCriterion);
    	
    }
    
    
    
    private static void divideAndConquer(int startIdx, int criterion, List<Integer> criterionList) {
    	
    	if(criterionList.size() == 2) {
    		checkCards(startIdx, criterionList.get(1), 0);
    		return;
    	}
    	
    	List<Integer> lowerThanCriterion = new ArrayList<Integer>();
    	List<Integer> biggerThanCriterion = new ArrayList<Integer>();
		
    	// 처음값은 항상 lower 그룹
    	lowerThanCriterion.add(startIdx);
    	
    	// 기준값 부여
    	for(int i=1; i<criterionList.size(); i++) {
    		int idx = criterionList.get(i);
    		if(checkCards(startIdx, idx, criterion-1)) {
    			lowerThanCriterion.add(idx);
    		} 
    		else{
    			biggerThanCriterion.add(idx);
    		}
    	}
    	
    	// 분할 정복
		divideAndConquer(startIdx, criterion-1, lowerThanCriterion);
		if(biggerThanCriterion.size() > 0) divideAndConquer(biggerThanCriterion.get(0), criterion-1, biggerThanCriterion);
    	
	}



	public static void main(String[] args) {
		playGame();
	}
    
    
    public static boolean checkCards(int mIndexA, int mIndexB, int mDiff)
    {
        if (!ok || mIndexA < 0 || mIndexA >= N * 2 || mIndexB < 0 || mIndexB >= N * 2)
        {
            ok = false;
            return false;
        }

        if (abs(cards[mIndexA] - cards[mIndexB]) > mDiff)
        {
            return false;
        }

        int val = cards[mIndexA];
        if (mDiff == 0 && mIndexA != mIndexB && !found[val])
        {
            foundCnt += 1;
            found[val] = true;
        }

        return true;
    }

	private static int abs(int i) {
		return Math.abs(i);
	}

}
