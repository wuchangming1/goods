package cn.itcast.book.service;

import cn.itcast.book.dao.BookDao;
import cn.itcast.book.domin.Book;
import cn.itcast.pager.PageBean;

import java.sql.SQLException;

/**
 * Created by Administrator on 2018/10/11.
 */
public class BookService {
    private BookDao bookDao = new BookDao();
    //按书名查找
    public PageBean<Book> findByName(String bname,int pageCode){
        try {
            return bookDao.findByName(bname,pageCode);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //按作者查找
    public PageBean<Book> findByAuthor(String author,int pageCode){
        try {
            return bookDao.findByAuthor(author,pageCode);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //按出版社查找
    public PageBean<Book> findByPress(String Press,int pageCode){
        try {
            return bookDao.findByPress(Press,pageCode);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public PageBean<Book> findByCategory(String cid,int pageCode){
        try {
            return bookDao.findByCategory(cid,pageCode);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PageBean<Book> findByCombination(Book criteria, int pc) {
        try {
            return bookDao.findByCombination(criteria, pc);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
