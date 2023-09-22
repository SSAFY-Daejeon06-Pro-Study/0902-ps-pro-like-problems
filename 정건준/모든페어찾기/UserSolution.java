package 모든페어찾기;

/***
 * [문제]
 * 2N개의 카드 있음. (2 <= N <= 50,000)
 * 카드의 앞면에는 1, N까지의 숫자가 적혀 있음
 *
 * [변수]
 * class Node {
 *     int index;
 *     Node next;
 * }
 */
public class UserSolution {

    static class Node {
        int index;
        Node next;

        Node(int index, Node next) {
            this.index = index;
            this.next = next;
        }
    }

    void playGame(int N) {
        Node head = null;

        //연결 리스트 생성, 리스트는 카드 인덱스를 저장
        for(int i=0; i<2*N; i++) {
            head = new Node(i, head);
        }

        //임의로 정함
        int mDiff = (N / 3) / 2;
        dac(head, 2*N, mDiff);
    }

    private void dac(Node head, int size, int mDiff) {
        //페어 찾음, 정복
        if(size == 2) {
            Solution.checkCards(head.index, head.next.index, 0);
            return;
        }

        //리스트 맨 앞의 인덱스가 기준
        int A = head.index;
        int subA = -1;

        //리스트는 기준 인덱스(A)와 mDiff로 두 개의 리스트로 분할됨
        //listOne - 범위 => (A 인덱스 카드 번호 - mDiff) ~ (A 인덱스 카드 번호 + mDiff), 해당 범위에 포함되는 모든 카드의 인덱스를 담고 있음
        //listTwo - listOne에 포함되지 않은 모든 카드의 인덱스를 담고 있음
        //두 개의 리스트로 분할하고 정복하는 문제

        Node listOne = null;
        Node listTwo = null;
        int listOneSize = 0;
        int listTwoSize = 0;

        for(Node cur = head; cur != null;) {
            Node curNext = cur.next;

            if(Solution.checkCards(A, cur.index, mDiff)) {
                cur.next = listOne;
                listOne = cur;
                listOneSize++;
            }
            else {
                cur.next = listTwo;
                listTwo = cur;
                listTwoSize++;
            }
            cur = curNext;
        }

        /*
        //디버깅 : listOneSize + listTwoSize = size
        System.out.println("headIndex : " + head.index);
        System.out.println("listOne : " + listOneSize);
        System.out.println("listTwo : " + listTwoSize);
         */

        //mDiff는 listOne 범위의 절반 크기이므로 listOne은 mDiff/2로 설정
        if(listOne != null) dac(listOne, listOneSize, mDiff/2);
        if(listTwo != null) dac(listTwo, listTwoSize, mDiff);
    }
}
