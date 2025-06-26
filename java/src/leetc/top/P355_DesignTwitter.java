package leetc.top;

import java.util.*;

/**
 * LeetCode 355. 设计推特 (Design Twitter)
 * 
 * 问题描述：
 * 设计一个简化版的推特(Twitter)，可以让用户实现发送推文，关注/取消关注其他用户，
 * 能够看见关注人（包括自己）的最近十条推文。你的设计需要支持以下的几个功能：
 * 
 * 1. postTweet(userId, tweetId): 创建一条新的推文
 * 2. getNewsFeed(userId): 检索最近的十条推文。每个推文都必须是由此用户关注的人或者是用户自己发出的。
 *    推文必须按照时间顺序由最近的开始排序。
 * 3. follow(followerId, followeeId): 关注一个用户
 * 4. unfollow(followerId, followeeId): 取消关注一个用户
 * 
 * 示例：
 * Twitter twitter = new Twitter();
 * twitter.postTweet(1, 5);     // 用户1发送了一条新推文 (用户id = 1, 推文id = 5).
 * twitter.getNewsFeed(1);      // 用户1的获取推文应当返回一个列表，其中包含一个id为5的推文.
 * twitter.follow(1, 2);        // 用户1关注了用户2.
 * twitter.postTweet(2, 6);     // 用户2发送了一条新推文 (用户id = 2, 推文id = 6).
 * twitter.getNewsFeed(1);      // 用户1的获取推文应当返回一个列表，其中包含两个推文，id分别为 -> [6, 5].
 *                              // 推文id6应当在推文id5之前，因为它是在5之后发送的.
 * twitter.unfollow(1, 2);      // 用户1取消关注了用户2.
 * twitter.getNewsFeed(1);      // 用户1的获取推文应当返回一个列表，其中包含一个id为5的推文.
 *                              // 因为用户1已经不再关注用户2.
 * 
 * 解法思路：
 * 哈希表 + 优先队列（堆）+ 链表：
 * 
 * 1. 数据结构设计：
 *    - 用户关注关系：HashMap<Integer, Set<Integer>>
 *    - 用户推文链表：HashMap<Integer, Tweet> - 链表头节点
 *    - 推文节点：包含推文ID、时间戳、下一个推文的引用
 * 
 * 2. 核心思想：
 *    - 用链表存储每个用户的推文，便于按时间顺序访问
 *    - 用优先队列（最大堆）合并多个用户的推文流
 *    - 时间戳确保推文的时序性
 * 
 * 3. 关键操作：
 *    - postTweet: 在用户推文链表头部插入新推文
 *    - getNewsFeed: 使用k路归并算法合并关注用户的推文
 *    - follow/unfollow: 维护关注关系集合
 * 
 * 4. 优化策略：
 *    - 懒加载：只在需要时才处理推文合并
 *    - 链表结构：便于获取最新推文
 *    - 堆优化：高效合并多个有序流
 * 
 * 核心思想：
 * - 社交网络模型：用户、关注关系、推文流的抽象
 * - 多路归并：将多个用户的推文流合并为一个时间有序的流
 * - 数据结构组合：哈希表、集合、链表、堆的有机结合
 * 
 * 关键技巧：
 * - 时间戳递增：保证推文的全局时序
 * - 链表头插入：新推文总是在链表头部
 * - 堆维护：始终取时间戳最大的推文
 * 
 * 时间复杂度：
 * - postTweet: O(1)
 * - getNewsFeed: O(k log k) - k为关注的用户数，最多取10条推文
 * - follow/unfollow: O(1)
 * 
 * 空间复杂度：O(U + T) - U为用户数，T为推文总数
 * 
 * LeetCode链接：https://leetcode.com/problems/design-twitter/
 */
public class P355_DesignTwitter {
    
    /**
     * 推文节点类
     * 使用链表结构存储用户的推文，按时间倒序排列
     */
    class Tweet {
        int id;          // 推文ID
        int timestamp;   // 发布时间戳
        Tweet next;      // 下一条推文（时间更早）
        
        public Tweet(int id, int timestamp) {
            this.id = id;
            this.timestamp = timestamp;
            this.next = null;
        }
    }
    
    /**
     * 推特系统主类
     */
    class Twitter {
        
        private static final int MAX_TWEETS = 10; // 最多返回10条推文
        
        private int timestamp;                              // 全局时间戳
        private Map<Integer, Set<Integer>> followMap;       // 用户关注关系 userId -> Set<followeeId>
        private Map<Integer, Tweet> tweetMap;              // 用户推文链表 userId -> 推文链表头节点

        /**
         * 初始化推特系统
         */
        public Twitter() {
            timestamp = 0;
            followMap = new HashMap<>();
            tweetMap = new HashMap<>();
        }

        /**
         * 用户发布推文
         * 
         * 将新推文插入到用户推文链表的头部，保证最新推文在前
         * 
         * @param userId 用户ID
         * @param tweetId 推文ID
         */
        public void postTweet(int userId, int tweetId) {
            // 创建新推文节点
            Tweet newTweet = new Tweet(tweetId, timestamp++);
            
            // 插入到用户推文链表的头部
            newTweet.next = tweetMap.get(userId);
            tweetMap.put(userId, newTweet);
        }

        /**
         * 获取用户的新闻动态
         * 
         * 返回用户关注的人（包括自己）最近的10条推文，按时间倒序排列
         * 使用优先队列（最大堆）实现多路归并
         * 
         * @param userId 用户ID
         * @return 最近10条推文的ID列表
         */
        public List<Integer> getNewsFeed(int userId) {
            List<Integer> result = new ArrayList<>();
            
            // 优先队列（最大堆），按推文时间戳降序排列
            PriorityQueue<Tweet> maxHeap = new PriorityQueue<>((a, b) -> b.timestamp - a.timestamp);
            
            // 获取用户关注的所有人（包括自己）
            Set<Integer> followees = followMap.getOrDefault(userId, new HashSet<>());
            followees.add(userId); // 包括自己
            
            // 将每个关注用户的最新推文加入堆中
            for (int followeeId : followees) {
                Tweet tweet = tweetMap.get(followeeId);
                if (tweet != null) {
                    maxHeap.offer(tweet);
                }
            }
            
            // 从堆中取出最多10条最新推文
            while (!maxHeap.isEmpty() && result.size() < MAX_TWEETS) {
                Tweet tweet = maxHeap.poll();
                result.add(tweet.id);
                
                // 如果该用户还有更早的推文，将其加入堆中
                if (tweet.next != null) {
                    maxHeap.offer(tweet.next);
                }
            }
            
            return result;
        }

        /**
         * 用户关注另一个用户
         * 
         * @param followerId 关注者ID
         * @param followeeId 被关注者ID
         */
        public void follow(int followerId, int followeeId) {
            // 用户不能关注自己（在getNewsFeed中已自动包含自己）
            if (followerId == followeeId) {
                return;
            }
            
            // 获取或创建关注者的关注列表
            followMap.computeIfAbsent(followerId, k -> new HashSet<>()).add(followeeId);
        }

        /**
         * 用户取消关注另一个用户
         * 
         * @param followerId 关注者ID
         * @param followeeId 被关注者ID
         */
        public void unfollow(int followerId, int followeeId) {
            // 用户不能取消关注自己
            if (followerId == followeeId) {
                return;
            }
            
            // 从关注列表中移除被关注者
            Set<Integer> followees = followMap.get(followerId);
            if (followees != null) {
                followees.remove(followeeId);
            }
        }
    }
    
    /**
     * 优化版本：使用数组优化的推特系统
     * 
     * 当推文数量很大时，可以考虑使用循环数组等数据结构进行优化
     */
    class TwitterOptimized {
        
        private static final int MAX_TWEETS = 10;
        private static final int MAX_RECENT_TWEETS = 10; // 每个用户最多保存最近10条推文
        
        private int timestamp;
        private Map<Integer, Set<Integer>> followMap;
        private Map<Integer, List<Tweet>> tweetMap;      // 使用List存储推文

        public TwitterOptimized() {
            timestamp = 0;
            followMap = new HashMap<>();
            tweetMap = new HashMap<>();
        }

        public void postTweet(int userId, int tweetId) {
            Tweet newTweet = new Tweet(tweetId, timestamp++);
            
            // 获取或创建用户的推文列表
            List<Tweet> tweets = tweetMap.computeIfAbsent(userId, k -> new ArrayList<>());
            
            // 插入到列表头部
            tweets.add(0, newTweet);
            
            // 限制推文数量，只保留最近的推文
            if (tweets.size() > MAX_RECENT_TWEETS) {
                tweets.remove(tweets.size() - 1);
            }
        }

        public List<Integer> getNewsFeed(int userId) {
            List<Integer> result = new ArrayList<>();
            PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> b[1] - a[1]); // [userIndex, timestamp, tweetIndex]
            
            Set<Integer> followees = followMap.getOrDefault(userId, new HashSet<>());
            followees.add(userId);
            
            // 将每个用户的第一条推文加入堆中
            for (int followeeId : followees) {
                List<Tweet> tweets = tweetMap.get(followeeId);
                if (tweets != null && !tweets.isEmpty()) {
                    Tweet firstTweet = tweets.get(0);
                    maxHeap.offer(new int[]{followeeId, firstTweet.timestamp, 0, firstTweet.id});
                }
            }
            
            while (!maxHeap.isEmpty() && result.size() < MAX_TWEETS) {
                int[] current = maxHeap.poll();
                int followeeId = current[0];
                int tweetIndex = current[2];
                int tweetId = current[3];
                
                result.add(tweetId);
                
                // 添加该用户的下一条推文
                List<Tweet> tweets = tweetMap.get(followeeId);
                if (tweetIndex + 1 < tweets.size()) {
                    Tweet nextTweet = tweets.get(tweetIndex + 1);
                    maxHeap.offer(new int[]{followeeId, nextTweet.timestamp, tweetIndex + 1, nextTweet.id});
                }
            }
            
            return result;
        }

        public void follow(int followerId, int followeeId) {
            if (followerId != followeeId) {
                followMap.computeIfAbsent(followerId, k -> new HashSet<>()).add(followeeId);
            }
        }

        public void unfollow(int followerId, int followeeId) {
            if (followerId != followeeId) {
                Set<Integer> followees = followMap.get(followerId);
                if (followees != null) {
                    followees.remove(followeeId);
                }
            }
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P355_DesignTwitter designer = new P355_DesignTwitter();
        Twitter twitter = designer.new Twitter();
        
        // 测试基本功能
        System.out.println("=== 测试基本功能 ===");
        
        // 用户1发布推文
        twitter.postTweet(1, 5);
        System.out.println("用户1的推文: " + twitter.getNewsFeed(1)); // [5]
        
        // 用户1关注用户2
        twitter.follow(1, 2);
        
        // 用户2发布推文
        twitter.postTweet(2, 6);
        System.out.println("用户1的推文（关注用户2后）: " + twitter.getNewsFeed(1)); // [6, 5]
        
        // 用户1取消关注用户2
        twitter.unfollow(1, 2);
        System.out.println("用户1的推文（取消关注后）: " + twitter.getNewsFeed(1)); // [5]
        
        // 测试多用户多推文场景
        System.out.println("\n=== 测试多用户多推文 ===");
        Twitter twitter2 = designer.new Twitter();
        
        // 用户1发布多条推文
        twitter2.postTweet(1, 1);
        twitter2.postTweet(1, 2);
        twitter2.postTweet(1, 3);
        
        // 用户2发布推文
        twitter2.postTweet(2, 4);
        twitter2.postTweet(2, 5);
        
        // 用户1关注用户2
        twitter2.follow(1, 2);
        
        System.out.println("用户1的综合推文流: " + twitter2.getNewsFeed(1)); // 应该是按时间倒序的混合
        System.out.println("用户2的推文: " + twitter2.getNewsFeed(2)); // [5, 4]
        
        // 测试边界情况
        System.out.println("\n=== 测试边界情况 ===");
        
        // 新用户没有推文
        System.out.println("新用户3的推文: " + twitter2.getNewsFeed(3)); // []
        
        // 用户关注自己
        twitter2.follow(1, 1);
        System.out.println("用户1关注自己后的推文: " + twitter2.getNewsFeed(1)); // 应该没有重复
    }
} 