package com.xunhang.utils;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 敏感词过滤器——SensitiveFilter
 *
 * @author Andrews
 * @date 2023/12/4 11:12
 * @return null
 */
@Slf4j
@Component
@NoArgsConstructor
public final class SensitiveFilterUtil {

    /**
     * 替换符
     */
    private static final String REPLACE_MENT = "***";

    /**
     * 根节点
     */
    private static final TrieNode ROOT_NODE = new TrieNode();

    /**
     * 1、 前缀树  前缀树某一个节点
     *
     * @author NXY
     * @date 2023/12/4 11:17
     * @return null
     */
    private static class TrieNode {
        // 关键词结束标识
        private boolean isKeywordEnd = false;

        // 子节点(key是下级字符,value是下级节点)
        // 当前节点的子节点
        private final Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        // 添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        // 获取子节点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }

    }

    /**
     * 2、初始化方法，服务器启动时初始化
     *
     * @author NXY
     * @date 2023/12/4 11:18
     */
    @PostConstruct
    public void init() {
        try {
            // 类加载器
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                // 添加到前缀树
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            log.error("加载敏感词文件失败: " + e.getMessage());
        }
    }

    /**
     * 3、将一个敏感词添加到前缀树中
     *
     * @param keyword
     * @author NXY
     * @date 2023/12/4 11:15
     */
    private void addKeyword(String keyword) {
        TrieNode tempNode = ROOT_NODE;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if (subNode == null) {
                // 初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }
            // 指向子节点,进入下一轮循环
            tempNode = subNode;
            // 设置结束标识
            if (i == keyword.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     *
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        // 结果
        StringBuilder sb = new StringBuilder();
        try {
            // 指针1
            TrieNode tempNode = ROOT_NODE;
            // 指针2
            int begin = 0;
            // 指针3
            int position = 0;
            while (begin < text.length()) {
                if (position < text.length()) {
                    char c = text.charAt(position);
                    // 跳过符号
                    if (isSymbol(c)) {
                        // 若指针1处于根节点,将此符号计入结果,让指针2向下走一步
                        if (tempNode == ROOT_NODE) {
                            sb.append(c);
                            begin++;
                        }
                        // 无论符号在开头或中间,指针3都向下走一步
                        position++;
                        continue;
                    }
                    // 检查下级节点
                    tempNode = tempNode.getSubNode(c);
                    if (tempNode == null) {
                        // 以begin开头的字符串不是敏感词
                        sb.append(text.charAt(begin));
                        // 进入下一个位置
                        position = ++begin;
                        // 重新指向根节点
                        tempNode = ROOT_NODE;
                    } else if (tempNode.isKeywordEnd()) {
                        // 发现敏感词,将begin~position字符串替换掉
                        sb.append(REPLACE_MENT);
                        // 进入下一个位置
                        begin = ++position;
                        // 重新指向根节点
                        tempNode = ROOT_NODE;
                    } else {
                        // 检查下一个字符
                        position++;
                    }
                }
                // position遍历越界仍未匹配到敏感词
                else {
                    sb.append(text.charAt(begin));
                    position = ++begin;
                    tempNode = ROOT_NODE;
                }
            }
        } catch (Exception e) {
            sb = new StringBuilder(text);
        }
        return sb.toString();
    }

    /**
     * 判断是否为符号 ——特殊符号
     *
     * @param c
     * @return boolean
     * @author NXY
     * @date 2023/12/4 11:17
     */
    private boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }
}


