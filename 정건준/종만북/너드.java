package 종만북;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class 너드 {
    static TreeMap<Integer, Integer> treeMap;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int testCase = Integer.parseInt(br.readLine());

        for(int tc = 1; tc <= testCase; tc++) {
            int N = Integer.parseInt(br.readLine());
            int answer = 0;
            treeMap = new TreeMap<>();

            for(int i=0; i<N; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                int p = Integer.parseInt(st.nextToken());
                int q = Integer.parseInt(st.nextToken());

                if(checkVaild(p, q)) {
                    treeMap.put(p, q);
                    destroy(p, q);
                }
                answer += treeMap.size();
            }
            sb.append(answer).append('\n');
        }

        System.out.print(sb);
    }

    static boolean checkVaild(int p, int q) {
        Integer higherP = treeMap.higherKey(p);
        
        if(higherP == null) return true;
        
        return treeMap.get(higherP) < q;
    }

    static void destroy(int p, int q) {
        Integer lowerP = treeMap.lowerKey(p);

        while (lowerP != null) {
            if (treeMap.get(lowerP) > q) break;
            treeMap.remove(lowerP);
            lowerP = treeMap.lowerKey(p);
        }
    }
}
