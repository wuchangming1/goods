package cn.itcast.book.dao;

import cn.itcast.book.domin.Book;
import cn.itcast.jdbc.TxQueryRunner;
import cn.itcast.pager.Expression;
import cn.itcast.pager.PageBean;
import cn.itcast.pager.PageConstants;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/10/11.
 */
public class BookDao {
    private QueryRunner qr = new TxQueryRunner();

    private PageBean<Book> findByCriteria(List<Expression> expressionList,int pageCode) throws SQLException {

        int pageSize = PageConstants.BOOK_PAGE_SIZE;
        List<String> params = new ArrayList<>();
        StringBuffer whereSql = new StringBuffer(" where 1=1 ");
        for (Expression expression : expressionList) {
            if (!expression.getOperator().equals("is null")){
                whereSql.append(" and ").append(expression.getName()).append(" "+expression.getOperator()+" ?");
                params.add(expression.getValue());


            }

        }
        String sql = "select count(*) from t_book"+whereSql;
        Number number = (Number) qr.query(sql,new ScalarHandler(),params.toArray());
        System.out.println(whereSql);
        System.out.println(params);
        return null;

    }

    public static void main(String[] args) {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(new Expression("bname","like","%Java%"));
        expressions.add(new Expression("bid","is null",""));
//        expressions.add(new Expression("image_w","=","....."));
        BookDao dao = new BookDao();

        try {

            System.out.println(dao.findByCriteria(expressions,10));
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
