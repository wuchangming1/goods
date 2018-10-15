package cn.itcast.pager;

import java.util.List;

/**
 * Created by Administrator on 2018/10/12.
 */
public class PageBean <T>{
    private int pageCode;
    private int pageSize;
    private int totalRecord;
    private String url;
    private List<T> beanList;

    public int getTotalPage(){
        int totalPage = totalRecord/pageSize;
        return totalRecord % pageSize ==0? totalPage :totalPage+1;
    }
    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }
    public int getPageCode() {
        return pageCode;
    }

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<T> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<T> beanList) {
        this.beanList = beanList;
    }
}
