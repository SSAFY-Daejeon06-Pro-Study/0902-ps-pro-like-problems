package 리스트복사;
import java.util.*;

/***
 * [관련 변수]
 * static class Node {
 *     int value,
 *     int index,
 *     Node next
 * }
 * Map<String, String> arrMap - 얕은 복사 배열 이름으로 맵핑된 배열 키를 찾음
 * Map<String, Node> updateData - 키 노드가 가지는 연결리스트 (변경 내역을 최신순으로 저장)
 * Map<String, String> parent -  키 노드의 부모 노드를 찾음
 * int count = 0 - 이름 가변 생성
 *
 * [풀이]
 * 각 배열을 트리의 노드로 봄, 각 노드는 변경 이력을 [최근순으로 저장하는 연결리스트]를 가짐
 * 노드의 키는 String(배열 이름)
 * 트리 노드는 내부 노드와 리프 노드로 구분, 내부 노드에는 더 이상 변경 이력을 저장할 수 없고, 복사되지 않음
 * 리프 노드는 변경 이력을 저장할 수 있고, 복사(깊은 복사, 얕은 복사)의 대상이 됨 (핵심 - 입력으로 주어지는 배열 이름들은 모두 리프 노드가 됨)
 */

public class UserSolution {

    static class Node {
        int value;
        int index;
        Node next;
        Node(int value, int index, Node next) {
            this.value = value;
            this.index = index;
            this.next = next;
        }
    }

    Map<String, String> arrMap;
    Map<String, Node> updateData;
    Map<String, String> parent;
    int count;

    private String getArrName(char[] mName) {
        StringBuilder sb = new StringBuilder();
        String arrName;

        for(int i=0; i<mName.length-1; i++) {
            if(mName[i] == '\0') break;
            sb.append(mName[i]);
        }

        arrName = sb.toString();
        return arrMap.getOrDefault(arrName, arrName);
    }

    private String getParentNodeName (String arrName) {
        return arrName + "#" + count++;
    }

    private int find(String arrName, int index) {
        Node head = updateData.get(arrName);
        for(Node cur = head; cur != null; cur = cur.next) {
            if(cur.index == index) return cur.value;
        }
        return find(parent.get(arrName), index);
    }

    public void init() {
        arrMap = new HashMap<>();
        updateData = new HashMap<>();
        parent = new HashMap<>();
        count = 0;
    }

    void makeList(char[] mName, int mLength, int[] mListValue) {
        String arrName = getArrName(mName);
        String parentName = getParentNodeName(arrName);

        //부모 노드와 리프 노드 생성
        updateData.put(arrName, null);
        updateData.put(parentName, null);

        for(int i=0; i<mLength; i++) {
            Node head = updateData.get(parentName);
            updateData.put(parentName, new Node(mListValue[i], i, head));
        }

        //리프 노드를 부모 노드와 연결
        parent.put(arrName, parentName);
    }

    void copyList(char mDest[], char mSrc[], boolean mCopy) {
        String destArrName = getArrName(mDest);
        String srcArrName = getArrName(mSrc);

        //얕은 복사
        if (!mCopy) {
            arrMap.put(destArrName, srcArrName);
            return;
        }

        //리프 노드의 변경 내역이 없는 경우, des 리프 노드를 생성하고 부모 노드와 연결
        if (updateData.get(srcArrName) == null) {
            updateData.put(destArrName, null);
            parent.put(destArrName, parent.get(srcArrName));
            return;
        }

        //부모 노드를 생성하고 src 리프 노드의 변경 내역과, 부모 노드를 복사
        String parentName = getParentNodeName(srcArrName);
        updateData.put(parentName, updateData.get(srcArrName));
        parent.put(parentName, parent.get(srcArrName));

        //src 리프 노드 재생성, des 리프 노드 생성, 부모 노드와 연결
        updateData.put(srcArrName, null);
        updateData.put(destArrName, null);
        parent.put(srcArrName, parentName);
        parent.put(destArrName, parentName);
    }

    void updateElement(char mName[], int mIndex, int mValue) {
        String arrName = getArrName(mName);

        Node head = updateData.get(arrName);
        updateData.put(arrName, new Node(mValue, mIndex, head));
    }

    int element(char mName[], int mIndex) {
        //트리를 올라가면서 노드가 가진 연결 리스트에 해당 Index가 있는지 체크
        String arrName = getArrName(mName);
        return find(arrName, mIndex);
    }
}
