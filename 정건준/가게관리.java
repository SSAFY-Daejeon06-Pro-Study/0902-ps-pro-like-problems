import java.util.HashMap;
import java.util.PriorityQueue;

/***
 * [변수]
 * static class Node {
 *   int bid;
 *   int quantity;
 *   int price;
 *   Node(int bid, int quantity, int price) {
 *       this.bid = bid;
 *       this.quantity = quantity;
 *       this.price = price;
 *   }
 * }
 *
 * class BData {
 *     int product;
 *     int price;
 *     int originQuantity
 *     int quantity;
 *     boolean cancle;
 * }
 *
 * class SData {
 *     int product;
 *     int price;
 *     int quantity;
 *     boolean refund;
 * }
 *
 * HashMap<Integer, PriorityQueue<BNode>> queueHash = 상품 ID에 대한 우선순위 큐
 * HashMap<Integer, Integer> productHash = 상품 ID에 대한 총 재고
 * HashMap<Integer, BData> BDataHash = Bid에 대한 구매 정보
 * HashMap<Integer, SData> SDataHash = Sid에 대한 구매 정보
 */

class UserSolution {
    static class Node {
        int bid;
        int quantity;
        int price;
        Node(int bid, int quantity, int price) {
            this.bid = bid;
            this.quantity = quantity;
            this.price = price;
        }
    }

    static class ListNode {
        Node data;
        ListNode next;
        ListNode(Node data, ListNode next) {
            this.data = data;
            this.next = next;
        }
    }

    static class BData {
        int product;
        int price;
        int originQuantity;
        int quantity;
        boolean cancle;
        BData(int product, int price, int originQuantity, int quantity) {
            this.product = product;
            this.price = price;
            this.originQuantity = originQuantity;
            this.quantity = quantity;
            this.cancle = false;
        }
    }

    static class SData {
        int product;
        int price;
        int quantity;
        ListNode soldedBList;
        boolean refund;
        SData(int product, int price, int quantity, ListNode soldedBList) {
            this.product = product;
            this.price = price;
            this.quantity = quantity;
            this.soldedBList = soldedBList;
            refund = false;
        }
    }

    HashMap<Integer, PriorityQueue<Node>> queueHash;
    HashMap<Integer, BData> BDataHash;
    HashMap<Integer, SData> SDataHash;
    HashMap<Integer, Integer> productHash;

    public void init() {
        queueHash = new HashMap<>();
        BDataHash = new HashMap<>();
        SDataHash = new HashMap<>();
        productHash = new HashMap<>();
    }

    public int buy(int bId, int mProduct, int mPrice, int mQuantity) {
        if(!BDataHash.containsKey(bId)) {
            BDataHash.put(bId, new BData(mProduct, mPrice, mQuantity, mQuantity));
        }

        if(!productHash.containsKey(mProduct)) {
            productHash.put(mProduct, 0);
            queueHash.put(mProduct, new PriorityQueue<>((a, b)-> {
                if(a.price == b.price) return a.bid - b.bid;
                return a.price - b.price;
            }));
        }

        queueHash.get(mProduct).offer(new Node(bId, mQuantity, mPrice));
        int sum = productHash.get(mProduct) + mQuantity;
        productHash.put(mProduct, sum);

        //System.out.println("bId : " + bId + ", " + "mProduct : " + mProduct + ", " + "mPrice : " + mPrice + ", " + "mQuantity : " + mQuantity);
        //System.out.println("buy : " + sum);
        return sum;
    }

    public int cancel(int bId) {
        if(!BDataHash.containsKey(bId)) return -1;

        BData bData = BDataHash.get(bId);
        if(bData.cancle) return -1;
        if(bData.originQuantity != bData.quantity) return -1;

        bData.cancle = true;
        int newSum = productHash.get(bData.product) - bData.quantity;
        productHash.put(bData.product, newSum);

        //System.out.println("cancle : " + newSum);
        return newSum;
    }

    public int sell(int sId, int mProduct, int mPrice, int mQuantity) {
        if(productHash.get(mProduct) < mQuantity) return -1;

        int profit = 0;
        int quantity = mQuantity;
        PriorityQueue<Node> pq = queueHash.get(mProduct);
        ListNode soldedBList = null;

        while(quantity != 0) {
            //캔슬된 상품은 무시
            BData Bdata = BDataHash.get(pq.peek().bid);
            if(Bdata.cancle) {
                pq.poll();
                continue;
            }

            int money = mPrice - Bdata.price;
            if(pq.peek().quantity <= quantity) {
                Node node = pq.poll();
                quantity -= node.quantity;
                Bdata.quantity -= node.quantity;

                soldedBList = new ListNode(node, soldedBList);
                profit += (node.quantity) * money;
            }
            else {
                profit += quantity * money;
                pq.peek().quantity -= quantity;
                Bdata.quantity -= quantity;

                Node newNode = new Node(pq.peek().bid, quantity, pq.peek().price);
                soldedBList = new ListNode(newNode, soldedBList);
                quantity = 0;
            }
        }

        SDataHash.put(sId, new SData(mProduct, mPrice, mQuantity, soldedBList));
        productHash.put(mProduct, productHash.get(mProduct) - mQuantity);
        //System.out.println("sell : " + profit);
        return profit;
    }

    public int refund(int sId) {
        if(!SDataHash.containsKey(sId)) return -1;

        SData Sdata = SDataHash.get(sId);
        if(Sdata.refund) return -1;

        Sdata.refund = true;
        for(ListNode listNode = Sdata.soldedBList; listNode != null; listNode = listNode.next) {
            Node node = listNode.data;

            BDataHash.get(node.bid).quantity += node.quantity;
            queueHash.get(Sdata.product).add(node);
            productHash.put(Sdata.product, productHash.get(Sdata.product) + node.quantity);
        }
        //System.out.println("retund : " + Sdata.quantity);
        return Sdata.quantity;
    }
}
