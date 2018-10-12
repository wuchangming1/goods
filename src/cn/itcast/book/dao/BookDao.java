package cn.itcast.book.dao;

import cn.itcast.book.domin.Book;
import cn.itcast.jdbc.TxQueryRunner;
import cn.itcast.pager.Expression;
import cn.itcast.pager.PageBean;
import cn.itcast.pager.PageConstants;
import org.apache.commons.dbutils.QueryRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/10/11.
 */
public class BookDao {
    private QueryRunner qr = new TxQueryRunner();

    private PageBean<Book> findByCriteria(List<Expression> expressionList,int pageCode){

        int pageSize = PageConstants.BOOK_PAGE_SIZE;
        List<Object> params = new ArrayList<>();
        StringBuffer whereSql = new StringBuffer("where 1=1");
        for (Expression expression : expressionList) {
            if (!expression.getOperator().equals("is null")){
                whereSql.append(" and ").append(expression.getName()).append("= ?");
                params.add(expression.getValue());


            }

        }
        System.out.println(whereSql);
        System.out.println(params);
        return null;

    }

    public static void main(String[] args) {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(new Expression("bname","=","java"));
        expressions.add(new Expression("bid","is null",""));
        expressions.add(new Expression("image_w","=","....."));
        BookDao dao = new BookDao();
        dao.findByCriteria(expressions,10);


    }
}
