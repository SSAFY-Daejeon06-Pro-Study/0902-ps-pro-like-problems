package SWEA_가게관리;

import java.util.HashMap;
import java.util.HashSet;

/**
 * 풀이 시작 : 9:56
 * 풀이 완료 :
 * 풀이 시간 :
 *
 * 문제 해석
 * 가게 관리 시스템 API 구현
 *
 * - 상품 구매
 * int buy(int bId, int mProduct, int mPrice, int mQuantity)
 *
 * mProduct 상품 mPrice가격으로 mQuantity개 구매
 * 구매 id는 bId
 * 1. 1 ≤ bId ≤ 10억
 * 2. 1 ≤ mProduct ≤ 10억
 * 3. 1000 ≤ mPrice ≤ 300000
 * 4. 10 ≤ mQuantity ≤ 100
 *
 * - 구매 취소
 * int cancel(int bId)
 *
 * 구매 id가 bId인 구매를 취소
 * bId로 구매했던 상품 수량이 모두 가게에 남아 있는 경우에만 취소 가능, bId의 재고가 bId의 주문 수량보다 적거나 bId가 없는 경우 -1 반환
 * 취소가 가능하면 재고에서 bId로 구매했던 상품을 삭제, 가게에 남아 있는 동일 상품 개수를 반환
 *
 * - 상품 판매
 * int sell(int sId, int mProduct, int mPrice, int mQuantity)
 *
 * mProduct 상품을 mPrice 가격으로 mQuantity개 판매
 * 판매 id는 sId
 * mQuantity보다 보유 수량이 적으면 판매 실패, -1 반환
 * 판매가 가능하다면 가장 싸게 구매한 상품, 가격이 동일하다면 구매 id 값이 작은 상품부터 판매
 * 판매 후에 총 판매 수익을 반환, 개당 판매 수익은 (판매 가격 - 구매 가격)
 * 1. 1 ≤ sId ≤ 10억
 * 2. 1 ≤ mProduct ≤ 10억
 * 3. 2000 ≤ mPrice ≤ 300000
 * 4. 1 ≤ mQuantity ≤ 500
 *
 * - 상품 환불
 * int refund(int sId)
 *
 * sId로 판매한 상품에 대해 환불
 * 환불해 준 상품의 총 개수를 반환
 * 환불해 준 상품은 재고로 쌓임
 * sId로 판매한 내역이 없거나, 이미 환불해 준 판매 id라면 환불에 실패, -1반환
 * 환불에 실패했다면 -1 return, 환불 성공했다면 상품의 총 개수 반환
 *
 * 제약 조건
 * 1. 각 tc 시작 시 init() 함수 호출
 * 2. 상품 종류는 600 이하
 * 3. buy()의 호출 횟수는 30000 이하
 * 4. sell()의 호출 횟수는 30000 이하'
 * 5. 모든 함수 호출 횟수 총합은 80000번 이하
 *
 * 생각나는 풀이
 *
 *
 * 구현해야 하는 기능
 *
 */
class UserSolution {
	static int productIdx, poolIdx; // 현재 가진 상품 종류
	static HashMap<Integer, Integer> productNumToIdx = new HashMap<>(900); // mProduct -> numOfProducts 인덱스로 변환
	static HashMap<Integer, Integer> bIdToIdx = new HashMap<>(45000); // bId -> receiptPool의 인덱스로 변환
//	static HashMap<Integer, Integer> sIdToIdx = new HashMap<>(45000);
	static HashSet<Integer>[] bIdInProduct = new HashSet[600]; // 각 mProduct에 해당하는 구매 내역
	static int[] numOfProducts = new int[600]; // 상품 종류별로 총 갯수
	static Receipt[] receiptPool = new Receipt[30000];

	public UserSolution() {
		for (int i = 0; i < 30000; i++) {
			receiptPool[i] = new Receipt(); // 메모리 풀
		}

		for (int i = 0; i < 600; i++) {
			bIdInProduct[i] = new HashSet<>(); // pId 값이 들어있는 set
		}
	}

	static class Receipt { // 구매 정보 저장할 클래스
		int buyAmount, curAmount, price;
	}

	public void init() {
		// 이전에 쓴 만큼 다시 초기값 할당
		// new 키워드로 init()마다 다시 메모리 할당해주면 시간 & 메모리 낭비됨
		for (int i = 0; i < productIdx; i++) {
			bIdInProduct[i].clear();
			numOfProducts[i] = 0;
		}

		productNumToIdx.clear(); // 값 초기화
		bIdToIdx.clear(); // 값 초기화
		poolIdx = 0; // 재활용
		productIdx = 0; // 재활용
	}

	public int buy(int bId, int mProduct, int mPrice, int mQuantity) {
		if (!productNumToIdx.containsKey(mProduct)) { // mProduct를 처음 구매했다면
			productNumToIdx.put(mProduct, productIdx++); // 인덱스 변환 저장
		}

		int nowProductIdx = productNumToIdx.get(mProduct);

		Receipt nowReceipt = receiptPool[poolIdx]; // 메모리풀에서 하나 꺼내서 현재 정보 저장
		bIdToIdx.put(bId, poolIdx); // bId -> 메모리 풀의 인덱스 변환값이 저장된 set
		nowReceipt.buyAmount = nowReceipt.curAmount = mQuantity; // 구매 정보 저장
		nowReceipt.price = mPrice; // 구매 정보 저장

		bIdInProduct[nowProductIdx].add(poolIdx++); // bIdInProduct[mProduct->배열인덱스] 에다가 현재 bId의 변환된 인덱스 저장
		numOfProducts[nowProductIdx] += mQuantity;
		System.out.println("상품코드"+mProduct+" 개수 : "+numOfProducts[nowProductIdx]);
		return numOfProducts[nowProductIdx];
	}

	public int cancel(int bId) {
		return 0;
	}

	public int sell(int sId, int mProduct, int mPrice, int mQuantity) {
		return 0;
	}

	public int refund(int sId) {
		return 0;
	}
}