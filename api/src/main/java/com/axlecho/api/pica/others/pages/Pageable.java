package com.axlecho.api.pica.others.pages;

/**
 * 代表一个拥有页面的结果
 * 能翻页的任何结果都需要继承此类
 */
public interface Pageable {
    PageInfo getPageInfo();
}
