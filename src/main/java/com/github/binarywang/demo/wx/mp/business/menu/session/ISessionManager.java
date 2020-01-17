package com.github.binarywang.demo.wx.mp.business.menu.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 自定义会话管理器
 */
@Component
@Slf4j
public class ISessionManager {

    //默认最多维持100个会话
    private static final int DEFALUT_CAP = 100;
    /**
     * 用链表记录session的创建先后顺序
     */
    private Node head;

    private Node tail;
    /**
     * 记录链表长度
     */
    private int size;

    public static class Node {
        private ISession session;
        private Node pre;
        private Node next;

        public Node(ISession session, Node pre, Node next) {
            this.session = session;
            this.pre = pre;
            this.next = next;
        }
    }

    public ISession getSession(String openID) {
        ISession session = findSession(openID);
        if (session == null) {
            //如果会话数量大于默认会话池数量，则采用LRU算法删除一个会话
            if (sessionSize() >= DEFALUT_CAP) {
                expireSession();
            }
            session = createSession(openID);
            LinkLast(session);
            log.info("创建会话session={}", session);
        }
        refreshSession(session.getSessionID());
        return session;
    }

    private ISession createSession(String openID) {
        ISession iSession = new ISession();
        long now = System.currentTimeMillis();
        iSession.setSessionID(openID);
        iSession.setCreateTime(now);
        iSession.setLastActivityTime(now);
        return iSession;
    }

    /**
     * 采用LRU算法淘汰掉末端session
     */
    private void expireSession() {
        if (tail == null)
            return;
        Node last = tail;
        final Node pre = last.pre;
        log.info("当前会话失效 session={}", last.session);
        last.session = null;

        if (pre == null) {
            last = null;
        } else {
            tail = pre;
            pre.next = null;
            last = null;
        }
        size--;
    }

    /**
     * 采用LRU算法将最近使用的session插入头部
     */
    public void refreshSession(String sessionID) {
        final Node first = head;
        Node node = head;
        while (node != null) {
            if (node.session.getSessionID()
                .equals(sessionID)) {

                Node next = node.next;
                Node pre = node.pre;
                //当前node节点是头节点，直接返回
                if (pre == null) {
                    return;
                } else {
                    if (next == null) {
                        //当前节点是末端节点,末端节点哨兵改变
                        pre.next = null;
                        tail = pre;
                    } else {
                        //当前节点是中间节点
                        pre.next = next;
                        next.pre = pre;
                    }
                    node.pre = null;
                    node.next = first;
                    first.pre = node;
                    //头节点哨兵改变
                    head = node;
                }
                return;
            }
            node = node.next;
        }
    }

    /**
     * 插入到链表尾部
     *
     * @param session
     */
    private void LinkLast(ISession session) {
        final Node last = tail;
        Node newNode = new Node(session, last, null);
        //末端哨兵指向新添加的节点
        tail = newNode;
        //头节点哨兵未初始化，头节点哨兵指向新节点
        if (last == null)
            head = newNode;
        else
            last.next = newNode;
        size++;
    }


    private ISession findSession(String sessionID) {
        Node node = head;
        while (node != null) {
            if (node.session.getSessionID()
                .equals(sessionID)) {
                return node.session;
            }
            node = node.next;
        }
        return null;
    }


    public int sessionSize() {
        return size;
    }

    public static void main(String[] args) {
        ISessionManager iSessionManager = new ISessionManager();
        for (int i = 0; i < 11; i++) {
            ISession iSession = iSessionManager.getSession((i) + "");
            System.out.println("成功添加session:" + iSession.getSessionID());
            System.out.println("当前session数:" + iSessionManager.sessionSize());
            if (i == 4) {
                iSessionManager.refreshSession(i + "");
            }
            System.out.println(iSession);
        }

    }
}
