package kr.ac.lecture.samsung.b.find_all_pair;

import java.util.ArrayList;
import java.util.List;

/*
 * [문제 요약]
 * 모든 페이를 찾는 api를 만들어라
 *
 * [제약 조건]
 * 2N개의 카드 (2 ≤ N ≤ 50,000) -> 최대 10만개 존재 가능
 * 같은 카드가 정확히 2개씩 있음
 *
 * [문제 설명]
 * 페어 찾기는 아래 순서로 진행
 * 1. 나열된 카드 2개를 선택하고 각 차이의 상한값을 추측하여 심판에게 알림
 * 2. 심판은 상한값이 맞는지 확인해서 true, false로 알려줌
 * 3. 상한값이 0이면 페어를 찾는 것으로 판정함
 * 4. 아직 페어를 못찾았으면 1번 반복
 * 5. 모든 페어를 찾으면 게임 종료
 *
 * 우선 모든 원소 인덱스를 리스트에 담음 (0~2n-1)
 * 맨 처음 요소와 나머지 요소들을 분할
 *   - 분할 기준은 (전체길이)/(2^depth) - 1
 *   - 분할 기준을 바탕으로 Solution.checkCards(mIndexA, mIndexB, mDiff); 호출
 *       - a 는 리스트 첫 번째
 *       - b는 리스트 1번부터 마지막까지
 *       - mDiff는 분할 기준
 *   - 인덱스를 기준으로 구분해야하기 때문에 실제는 인덱스를 가지고 있을 것임
 * true인 것과 false인 것을 구분함
 *   - 기준이 되는 맨 처음 요소는 자동으로 true임
 * 원소가 2개만 남았으면 페어를 찾은 것임
 *
 * 역순으로 정렬 되었을 경우는?
 * 1,1,2,2,3,3,4,4 가 있다고 가정
 *
 * 일단 정렬 자체는 불가능 -> 내가 가지고 있지 않기 때문에
 *
 *
 *
 * */
public class SWE_모든페어찾기 {

    private static int n;

    public void playGame(int N) {
        List<Integer> list = init(N);

        findPair(list, 1);
    }

    private List<Integer> init(int N) {
        n = N;
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < (2 * n); i++) {
            list.add(i);
        }
        return list;
    }


    private void findPair(List<Integer> list, int depth) {
        if(list.size() == 0) return;
        if (list.size() == 2) {
            Solution.checkCards(list.get(0), list.get(1), 0);
            return;
        }

        int diff = (n / (int) Math.pow(2, depth));
        diff = (diff == 0) ? 0 : diff - 1;

        List<Integer> diffTrues = new ArrayList<>();
        List<Integer> diffFalses = new ArrayList<>();

        diffTrues.add(list.get(0));

        for (int i = 1; i < list.size(); i++) {
            if (Solution.checkCards(list.get(0), list.get(i), diff)) {
                diffTrues.add(list.get(i));
            } else {
                diffFalses.add(list.get(i));
            }
        }

        findPair(diffTrues, depth + 1);
        findPair(diffFalses, depth + 1);
    }
}
