package SWEA_가게관리;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

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
	static int productIdx, bIdIdx, sIdIdx; // 현재 가진 상품 종류, 현재 나온 bId 개수, 현재 나온 sId 개수
	static HashMap<Integer, Integer> productNumToIdx = new HashMap<>(900); // mProduct -> numOfProducts 인덱스로 변환
	static HashMap<Integer, Integer> bIdToIdx = new HashMap<>(45000); // bId -> buyReceiptPool의 인덱스로 변환
	static HashMap<Integer, Integer> sIdToIdx = new HashMap<>(45000); // sId -> sellReceipts의 인덱스로 변환
	static HashSet<Integer>[] bIdInProduct = new HashSet[600]; // 각 mProduct에 해당하는 구매 내역
	static HashMap<Integer, Integer> sIdToProduct = new HashMap<>(); // sId -> numOfProducts 인덱스로 변환
	static int[] numOfProducts = new int[600]; // 상품 종류별로 총 갯수
	static BuyReceipt[] buyReceiptPool = new BuyReceipt[30000]; // 메모리풀
	static SellReceipt[] sellReceipts = new SellReceipt[30000]; // 링크드리스트 배열
	static PriorityQueue<BuyReceipt> pq = new PriorityQueue<>(); // sell때 필요한 우선순위큐

	public UserSolution() { // 가장 처음 한 번만 하면 되는 작업들
		for (int i = 0; i < 30000; i++) {
			buyReceiptPool[i] = new BuyReceipt(); // 객체 생성해놓음
		}

		for (int i = 0; i < 600; i++) {
			bIdInProduct[i] = new HashSet<>(); // 상품당 가진 bId set 메모리에 올림
		}
	}

	static class BuyReceipt implements Comparable<BuyReceipt> { // 구매 정보 저장할 클래스
		int buyAmount, curAmount, price, bId;

		public int compareTo(BuyReceipt o) {
			if (this.price == o.price) return this.bId - o.bId;
			return this.price - o.price;
		}

	}

	static class SellReceipt { // 판매 정보
		int buyReceiptIdx, amount; // 현재 bId에서 얼만큼 샀는지
		SellReceipt next;

		public SellReceipt(int buyReceiptIdx, int amount, SellReceipt next) {
			this.buyReceiptIdx = buyReceiptIdx;
			this.amount = amount;
			this.next = next;
		}
	}


	public void init() {
		// 이전에 쓴 만큼 다시 초기값 할당
		// new 키워드로 init()마다 다시 메모리 할당해주면 시간 & 메모리 낭비됨
		for (int i = 0; i < productIdx; i++) {
			bIdInProduct[i].clear();
			numOfProducts[i] = 0;
		}

		for (int i = 0; i < sIdIdx; i++) {
			sellReceipts[i] = null;
		}

		productNumToIdx.clear(); // 값 초기화
		bIdToIdx.clear(); // 값 초기화
		sIdToIdx.clear();
		sIdToProduct.clear();
		bIdIdx = 0; // 재활용
		sIdIdx = 0;
		productIdx = 0; // 재활용
	}

	public int buy(int bId, int mProduct, int mPrice, int mQuantity) {
		if (!productNumToIdx.containsKey(mProduct)) { // mProduct를 처음 구매했다면
			productNumToIdx.put(mProduct, productIdx++); // 인덱스 변환 저장
		}

		int nowProductIdx = productNumToIdx.get(mProduct);

		BuyReceipt nowBuyReceipt = buyReceiptPool[bIdIdx]; // 메모리풀에서 하나 꺼내서 현재 정보 저장
		bIdToIdx.put(bId, bIdIdx); // bId -> 메모리 풀의 인덱스 변환값이 저장된 set
		nowBuyReceipt.buyAmount = nowBuyReceipt.curAmount = mQuantity; // 구매 정보 저장
		nowBuyReceipt.price = mPrice; // 구매 정보 저장
		nowBuyReceipt.bId = bId; // 구매 정보 저장

		bIdInProduct[nowProductIdx].add(bIdIdx++); // bIdInProduct[mProduct->배열인덱스] 에다가 현재 bId의 변환된 인덱스 저장
		numOfProducts[nowProductIdx] += mQuantity;
		return numOfProducts[nowProductIdx];
	}

	public int cancel(int bId) {
		int idx = bIdToIdx.getOrDefault(bId, -1); // bId가 없거나 이미 취소한 경우면 -1
		if (idx == -1) {
			return -1;
		}
		BuyReceipt now = buyReceiptPool[idx]; // 취소할 구매 내역

		if (now.curAmount == now.buyAmount) { // 재고가 그대로 있다면
			for (int i = 0; i < productIdx; i++) { // 상품 목록에서 탐색해서 무슨 상품인지 찾음
				if (bIdInProduct[i].contains(idx)) { // 무슨 상품에 속했는지 찾았다면
					numOfProducts[i] -= now.curAmount; // 그 상품 재고 줄여줌
					bIdInProduct[i].remove(idx); // 상품에서 이번 구매 내역 삭제
					return numOfProducts[i]; // 남은 상품 재고 리턴
				}
			}
		}

		return -1;
	}

	public int sell(int sId, int mProduct, int mPrice, int mQuantity) {
		if (!productNumToIdx.containsKey(mProduct)) return -1; // 판매할 상품이 구매한 적 없다면 -1 리턴
		int nowProductIdx = productNumToIdx.get(mProduct); // 상품 배열 인덱스
		if (numOfProducts[nowProductIdx] < mQuantity) return -1; // 상품의 총 개수가 판매해야 하는 수량보다 적다면 -1 리턴
		int sIdx = sIdIdx; // 링크드리스트 배열의 인덱스
		sIdToIdx.put(sId, sIdIdx++); // sId -> 배열 인덱스로 변환하는 맵에 추가
		sIdToProduct.put(sId, productNumToIdx.get(mProduct)); // sId -> 상품 배열 인덱스를 찾는 맵에 추가
		pq.clear(); // 우선순위 큐 비워줌
		for (int bIdx : bIdInProduct[nowProductIdx]) { // 판매하려는 상품에 들어 있는 모든 구매 내역
			if (buyReceiptPool[bIdx].curAmount == 0) continue; // 현재 재고가 0이라면 continue
			pq.offer(buyReceiptPool[bIdx]); // pq에 추가
		}

		int amount; // 각 bId에서 얼만큼 샀는지 저장할 변수
		int totalIncome = 0; // 총 판매 수익

		while (mQuantity > 0) {
			BuyReceipt now = pq.poll();

			if (now.curAmount >= mQuantity) { // 마지막 상품
				totalIncome += (mPrice - now.price) * mQuantity;
				amount = mQuantity;
				now.curAmount -= mQuantity;
				numOfProducts[nowProductIdx] -= mQuantity; // 재고에서 줄임
				mQuantity = 0;
			} else {
				totalIncome += (mPrice - now.price) * now.curAmount;
				amount = now.curAmount;
				mQuantity -= now.curAmount;
				numOfProducts[nowProductIdx] -= now.curAmount; // 재고에서 줄임
				now.curAmount = 0;
			}
			sellReceipts[sIdx] = new SellReceipt(bIdToIdx.get(now.bId), amount, sellReceipts[sIdx]); // 이번 판매 상품을 구성하는 모든 bId들
		}

		return totalIncome;
	}

	public int refund(int sId) {
		if (!sIdToIdx.containsKey(sId)) return -1; // 판매 목록에 sId가 없으면 -1 리턴 (환불 후에는 여기서 삭제하므로 중복 환불도 판별)
		int sIdx = sIdToIdx.get(sId); // sId -> 배열의 인덱스로 변환
		sIdToIdx.remove(sId); // 판매 목록에서 삭제
		int nowProductIdx = sIdToProduct.get(sId); // sId에서 구매한 제품 번호(배열 인덱스) 반환
		int totalRefundAmount = 0; // 환불한 총 개수
		for (SellReceipt s = sellReceipts[sIdx]; s != null; s = s.next) { // 링크드리스트로 sId로 판매한 상품들 전부 환불
			buyReceiptPool[s.buyReceiptIdx].curAmount += s.amount;
			totalRefundAmount += s.amount;
		}
		numOfProducts[nowProductIdx] += totalRefundAmount; // 환불한 총 개수만큼 환불 대상 상품 재고에 더해줌
		return totalRefundAmount;
	}
}
