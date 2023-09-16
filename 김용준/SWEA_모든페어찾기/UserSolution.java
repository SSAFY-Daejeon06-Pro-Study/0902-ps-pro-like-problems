package SWEA_모든페어찾기;

import java.util.ArrayDeque;
import java.util.HashSet;

/**
 * 풀이 시작 : 3:29
 * 풀이 완료 :
 * 풀이 시간 :
 *
 * 문제 해석
 * 총 2N개의 카드가 있다
 * 카드는 1 ~ N까지 수가 적혀 있다
 * 페어는 동일한 숫자가 적힌 서로 다른 카드 한 쌍을 의미한다
 * 모든 카드를 N쌍의 페어로 나눌 수 있다
 * 페어 찾기 게임 실행 순서
 * 1. 모든 카드가 뒤집힌 채로 주어짐
 * 2. 나열된 카드 중 2개를 선택하고 각 카드 숫자 차이 상한 값을 추측해 심판에게 알림
 * 3. 심판은 선택한 두 카드에 적힌 숫자의 차이가 추측 상한 값 이하인지, 초과인지를 알려줌
 * 4. 맞약 추측한 상한 값이 0이고 두 카드의 숫자가 같은 경우 해당 페어를 찾은 것으로 판정
 * 5. 아직 찾지 못한 페어가 있는 경우 2 ~ 4 반복
 *
 * 구해야 하는 것
 * 페어의 총 갯수가 주어졌을 때 모든 페어를 찾는 함수를 작성
 *
 * 문제 입력
 * 1. playGame(int N)함수 수행
 *  - N은 나열된 카드에 존재하는 서로 다른 페어의 개수
 *
 * 2. User Code에서 사용 가능한 Main API
 *  - boolean checkCard(int mIndexA. intIndexB, int mDiff) => A와 B의 숫자 차이가 mDiff 이하인지? 맞으면 t, 아니면 f
 *
 * 제한 요소
 * 1 <= N <= 50000
 * 카드 개수 = 2N
 *
 * 생각나는 풀이
 * 1. 아직 페어를 찾지 못한 카드 기준으로 범위를 반씩 잘라가면서 남은 카드들을 분류함
 * 2. 만약 true인 카드가 단 1장일 경우 페어를 찾은 것 (기준 카드와 true인 카드가 페어임)
 * 3. 만약 false인 카드가 단 2장일 경우 페어를 찾은 것 (false에 들어 있는 2장의 카드가 페어임)
 * 4. 기준 카드가 페어가 되었다면 true에 들어 있는 카드들에 대해 페어 찾기를 실시함
 *  - 얘네를 분류하는 리스트는 어떻게 해야하지?
 *  logN + 1개?
 *  - 얘네는 범위를 어디서부터 시작해야 하지?
 *
 * 구현해야 하는 기능
 * 1. 다음 탐색 범위 range = (int) (기존 범위 * 0.75)
 *  1.1 if (range == 0) A = true, B = true
 * 2. 탐색하는 요소 = log2(N)번째 리스트에서 true에 있는 애들
 * 3. checkCard(A, B, 0) = true가 되었다면
 *  - tList1에서 B를 삭제, A와 B가 페어라고 체크
 *  - tList1에 요소가 2개라면 해당 요소들도 check(C, D, 0) 수행 후 true 체크, C, D를 tList1에서 삭제
 *  - 그러면 tList1은 비게 됨
 *  - fList1(0)을 새로운 기준으로 삼고 fList1에서 범위 1에 대해 탐색
 *  - true인 애들을 tList0에 담고, false인 애들을 fList0에 담음
 *  - 나머지 과정은 true일때와 같음
 */
public class UserSolution {
    // checkList[0] = true, checkList[1] = false
    // checkList[1][i] = range[i]로 checkCards() 수행했을 때 false가 된 친구들
    static HashSet<Integer>[][] checkList = new HashSet[2][24];
    static int[] range;
    static int[] temp = new int[6]; // 임시로 저장할 배열

    public UserSolution() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < checkList[0].length; j++) {
                checkList[i][j] = new HashSet<>();
            }
        }
    }

    void playGame(int N) {
        int numOfCards = N << 1;

        int rangeNum = (N + 1) / 2;
        ArrayDeque<Integer> stack = new ArrayDeque<>();

        while (rangeNum != 0) {
            stack.push(rangeNum);
            rangeNum = (int) ((double) rangeNum * 0.66); // 이거 대체 최적값이 뭔지 모르겠다
        }
        range = new int[stack.size() + 1];
        for (int i = 1; i < range.length; i++) {
            range[i] = stack.pop();
        }


        int nowRangeIdx = range.length - 1;

        for (int i = 1; i < numOfCards; i++) {
            if (Solution.checkCards(0, i, range[nowRangeIdx])) {
                checkList[0][nowRangeIdx].add(i); // nowRangeIdx번째 기준으로 나눴을 때 range 이내에 들어온 애들
            } else {
                checkList[1][nowRangeIdx].add(i); // nowRangeIdx번째 기준으로 나눴을 때 range 밖에 있는 애들
            }
        }

        makePair(0, nowRangeIdx);
    }

    private void makePair(int standardIdx, int nowRangeIdx) {
        // 종료 조건
        if (nowRangeIdx == 0) {
            // 1에서 분류가 끝났다는건
            // checkList[0][0] = 답 +- 1이내인 애들이 들어 있음 (최대 5개)
            HashSet<Integer> trueSet = checkList[0][nowRangeIdx];
            int idx = 0;
            for (int value : trueSet) {
                if (!Solution.checkCards(standardIdx, value, 0)) {
                    temp[idx++] = value; // 페어가 아닌 4개 중 하나
                }
            }

            if (idx == 2) { // 차이 1나는게 한쪽만 있는 경우
                Solution.checkCards(temp[0], temp[1], 0);
            } else if (idx == 4) { // 차이 1나는게 두쪽 다 있는 경우
                for (int i = 0; i < 3; i++) {
                    for (int j = i + 1; j < 4; j++) {
                        Solution.checkCards(temp[i], temp[j], 0);
                    }
                }
            }
            checkList[0][0].clear();

            // 얘네는 무조건 6개 이하임
            // 기존 범위의 0.66씩 곱하는데 최대가 3이라서 양쪽 다해도 6개가 끝
            HashSet<Integer> falseSet = checkList[1][0];
            idx = 0;
            for (int value : falseSet) {
                temp[idx++] = value;
            }

            for (int i = 0; i < idx - 1; i++) {
                for (int j = i + 1; j < idx; j++) {
                    Solution.checkCards(temp[i], temp[j], 0);
                }
            }
            checkList[1][0].clear();
            return;
        }

        // 일단 range 안에 있는 애들을 또 range 줄여서 분류
        int halfRangeIdx = nowRangeIdx - 1;
        for (int value : checkList[0][nowRangeIdx]) {
            if (Solution.checkCards(standardIdx, value, range[halfRangeIdx])) {
                checkList[0][halfRangeIdx].add(value); // 범위에 속했다면 그 아래에 저장
            } else {
                checkList[1][halfRangeIdx].add(value); // 범위에 속하지 않았다면 다른 곳 저장
            }
        }
        checkList[0][nowRangeIdx].clear(); // 현재 위치는 모두 분류 완료했으므로 clear시킴

        // 분류 완료 했으니 True 방향 재귀
        makePair(standardIdx, halfRangeIdx);

        // True 방향 재귀가 끝났다는 뜻은 현재 기준(standardIdx) 수의 현재 range보다 작은 분류 애들은 pair가 전부 매칭되었다는 뜻
        // 현재 range에서 False방향을 분류하려면 False에 있는 하나의 원소를 새로운 기준으로 삼고 재귀를 해야 함
        HashSet<Integer> nowSet = checkList[1][nowRangeIdx];
        if (!nowSet.isEmpty()) {
            int falseStandardIdx = -1;
            for (int value : checkList[1][nowRangeIdx]) {
                falseStandardIdx = value;
                checkList[1][nowRangeIdx].remove(value);
                break;
            }
            // 현재 기준으로 재귀는 checkList[1][halfRangeIdx]가 아니라 checkList[0][halfRangeIdx]를 돌게 되어 있음
            // 그렇게 하려면 주소를 현재 이걸 바라보게 해야 함
            checkList[0][nowRangeIdx] = nowSet;
            checkList[1][nowRangeIdx] = new HashSet<>();
            makePair(falseStandardIdx, nowRangeIdx);
        }
    }
}