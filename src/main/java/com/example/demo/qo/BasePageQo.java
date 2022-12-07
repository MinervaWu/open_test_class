package com.example.demo.qo;

public class BasePageQo {

    //当前页
    private int pageNum;
    //每页的数量
    private int pageSize;

    public static int NORMAL_SISE = 10;

    public int getPageNum() {
        return this.pageNum == 0 ? 1 : this.pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return this.pageSize == 0 ? Integer.MAX_VALUE : this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
