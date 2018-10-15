package cn.itcast.book.dao;

import cn.itcast.book.domin.Book;
import cn.itcast.jdbc.TxQueryRunner;
import cn.itcast.pager.Expression;
import cn.itcast.pager.PageBean;
import cn.itcast.pager.PageConstants;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/10/11.
 */
public class BookDao {
    private QueryRunner qr = new TxQueryRunner();
    //分类查询
    public PageBean<Book> findByCategory(String cid,int pageCode) throws SQLException {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(new Expression("cid","=",cid));
        return findByCriteria(expressions,pageCode);
    }
    //按书名模糊查询
    public PageBean<Book> findByName(String bname,int pageCode) throws SQLException {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(new Expression("bname","like","%"+bname+"%"));
        return findByCriteria(expressions,pageCode);
    }
    //按作者查询
    public PageBean<Book> findByAuthor(String author,int pageCode) throws SQLException {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(new Expression("author","like","%"+author+"%"));
        return findByCriteria(expressions,pageCode);
    }

    //按出版社查询
    public PageBean<Book> findByPress(String press,int pageCode) throws SQLException {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(new Expression("press","like","%"+press+"%"));
        return findByCriteria(expressions,pageCode);
    }

    //多条件组合查询
    public PageBean<Book> findByCombination(Book criteria,int pageCode) throws SQLException {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(new Expression("bname","like","%"+criteria.getBname()+"%"));
        expressions.add(new Expression("author","like","%"+criteria.getAuthor()+"%"));
        expressions.add(new Expression("press","like","%"+criteria.getPress()+"%"));
        return findByCriteria(expressions,pageCode);
    }

    public PageBean<Book> findByCriteria(List<Expression> expressionList,int pageCode) throws SQLException {

        int pageSize = PageConstants.BOOK_PAGE_SIZE;
        List<Object> params = new ArrayList<>();
        StringBuffer whereSql = new StringBuffer(" where 1=1 ");
        for (Expression expression : expressionList) {
            whereSql.append(" and ").append(expression.getOperator());
            if (!expression.getOperator().equals("is null")){

               whereSql.append(" "+expression.getOperator()+" ?");
               params.add(expression.getValue());
            }

        }
        String sql = "select count(*) from t_book"+whereSql;
        Number number = (Number) qr.query(sql,new ScalarHandler(),params.toArray());
        System.out.println(whereSql);
        System.out.println(params);

        sql="select * from t_book" + whereSql+" order By orderBy limit ?,?";
        params.add((pageCode-1)*pageSize);
        params.add(pageSize);
        List<Book> beanList = qr.query(sql,new BeanListHandler<Book>(Book.class),params.toArray());

        PageBean<Book> pageBean = new PageBean<>();
        pageBean.setPageCode(pageCode);
        pageBean.setPageSize(pageSize);
        pageBean.setBeanList(beanList);


        return pageBean;

    }

    public static void main(String[] args) {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(new Expression("bname","like","%Java%"));
//        expressions.add(new Expression("bid","is null",""));
//        expressions.add(new Expression("image_w","=","....."));
        BookDao dao = new BookDao();

        try {

            System.out.println(dao.findByCriteria(expressions,10));
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
