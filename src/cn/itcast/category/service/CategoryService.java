package cn.itcast.category.service;

import cn.itcast.category.dao.CategoryDao;
import cn.itcast.category.domin.Category;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2018/10/11.
 */
public class CategoryService {
    private CategoryDao categoryDao = new CategoryDao();

    //查询所有分类
    public List<Category> findAll() {
        try {
            return categoryDao.findAll();
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }
}
