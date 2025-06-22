package base.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

/**
 * 哈希算法工具类
 * 
 * 功能描述：
 * 封装Java安全框架中的MessageDigest类，提供常用哈希算法的使用
 * 支持MD5、SHA-1、SHA-256等多种哈希算法
 * 
 * 哈希算法特点：
 * 1. 确定性：相同输入总是产生相同输出
 * 2. 高效性：能快速计算任意大小数据的哈希值
 * 3. 雪崩效应：输入的微小改变会导致输出剧烈变化
 * 4. 不可逆性：从哈希值推导原始数据在计算上不可行
 * 5. 抗碰撞性：找到两个产生相同哈希值的不同输入极其困难
 * 
 * 应用场景：
 * - 数据完整性校验
 * - 密码存储
 * - 数字签名
 * - 区块链中的工作量证明
 */
public class Hash {
    private MessageDigest hash;     // MessageDigest实例，用于执行哈希计算
    
    /**
     * 构造函数 - 初始化指定的哈希算法
     * @param algorithm 哈希算法名称（如"MD5", "SHA-1", "SHA-256"等）
     */
    public Hash(String algorithm) {
        try {
            hash = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将字节数组转换为十六进制字符串
     * @param bytes 字节数组
     * @return 十六进制字符串（大写）
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }

    /**
     * 计算字符串的哈希值
     * 
     * 步骤：
     * 1. 将输入字符串转换为字节数组
     * 2. 使用MessageDigest计算哈希值（返回字节数组）
     * 3. 将字节数组转换为十六进制字符串表示
     * 
     * @param input 待计算哈希的输入字符串
     * @return 十六进制格式的哈希值字符串（大写）
     */
    public String hashCode(String input) {
        return bytesToHex(hash.digest(input.getBytes()));
    }

    /**
     * 测试方法 - 演示哈希算法的使用
     */
    public static void main(String[] args) {
        // 打印当前Java环境支持的所有哈希算法
        for (String s : Security.getAlgorithms("MessageDigest")) {
            System.out.println(s);
        }
        System.out.println("=====");
        
        // 使用MD5算法计算"abc"的哈希值
        String algorithm = "MD5";
        Hash hash = new Hash(algorithm);
        System.out.println(hash.hashCode("abc"));   // 应该输出: 900150983CD24FB0D6963F7D28E17F72
    }
}
