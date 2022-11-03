package client.nettyclient.controller;

/**
 * @ClassName: TestController
 * @Description:
 * @Author: xue
 * @Date: 2022/8/10
 */
public class TestController {



    public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        if (null == list1) return list2;
        if (null == list2) return list1;
        ListNode node = list1.val<list2.val?list1:list2;
        node.next = mergeTwoLists(node.next,list1.val>=list2.val?list1:list2);
        return node;
    }
    public static void main(String[] args) {
        strStr("hello","ll");
    }
    public static int strStr(String haystack, String needle) {
        return haystack.indexOf(needle);
    }
}


class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}
