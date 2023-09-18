package B형4주차_자유;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * 
 * 촌수 계산 프로그램 작성
 * 
 * 입력)
 * 전체 사람 수 n
 * 서로 다른 두 사람의 번호
 * 셋째 줄엔 부모 자식관계 개수 m
 * 
 * 각 사람의 부모는 최대 한명만 주어짐.
 * => 자식은 여러명일 수 있다는 얘기.
 * 
 * 
 * 예제)
 *   1
 *  2  3
 * 7 
 * => 3촌
 * 
 * 
 * 유니온 파인드? 트리?
 * 
 */
public class 촌수계산 {
	
	private static int n;
	private static int[] parents, visited;
	
	private static int find(int a) {

		if(parents[a] == a) return a;
		
		return find(parents[a]);
		
	}
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		
		if(n==1) {
			System.out.println(0);
			return;
		}
		
		parents = new int[n+1];
		visited = new int[n+1];
		Arrays.fill(visited, -1);
		
		for(int i=1; i<=n; i++) parents[i] = i;
		
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int a = Integer.parseInt(st.nextToken());
		int b = Integer.parseInt(st.nextToken());
		
		// 방문 처리
		visited[a] = 0;
		visited[b] = 0;
		
		int m = Integer.parseInt(br.readLine());
		
		for(int i=0; i<m; i++) {
			st = new StringTokenizer(br.readLine());
			
			int parent = Integer.parseInt(st.nextToken());
			int child = Integer.parseInt(st.nextToken());
			
			parents[child] = parent;
			
		}
		
		int aRoot = find(a);
		int bRoot = find(b);
		
		if(aRoot != bRoot) System.out.println(-1);
		else if(parents[a] == b || parents[b] == a) System.out.println(1);
		else {
			// 서브 트리의 루트노드에서 만나는 경우 포함
			int aCount = 1;
			int parent = parents[a];
			
			while(parent != aRoot) {
				if(parent == b) {
					System.out.println(aCount);
					return;
				}
				visited[parent] = aCount++;
				parent = parents[parent];
			}
			
			int bCount = 1;
			parent = parents[b];
			while(parent != bRoot) {
				if(parent == a) {
					System.out.println(bCount);
					return;
				}
				else if(visited[parent] >= 0) 
				{
					System.out.println(visited[parent]+bCount);
					return;
				}
				
				bCount++;
				parent = parents[parent];
			}
			System.out.println(aCount + bCount);
		}
	}	
	
}
