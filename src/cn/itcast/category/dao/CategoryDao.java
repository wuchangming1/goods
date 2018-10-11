package cn.itcast.category.dao;

import cn.itcast.category.domin.Category;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/10/11.
 */
public class CategoryDao {
    private QueryRunner qr = new TxQueryRunner();

    public List<Category> findAll() throws SQLException {
    String sql = "select * from t_category where pid is null";
    List<Map<String,Object>> mapList = qr.query(sql,new MapListHandler());

    List<Category> parents = toCategoryList(mapList);
    for (Category parent:parents){
        List<Category> children = findByParent(parent.getCid());
        parent.setChildren(children);
    }
    return parents;
    }

    private List<Category> findByParent(String pid) throws SQLException {
        String sql = "select * from t_category where pid=?";
        List<Map<String,Object>> children = qr.query(sql,new MapListHandler(),pid);
        return toCategoryList(children);
    }

    public List<Category> toCategoryList(List<Map<String, Object>> mapList) {
        List<Category> categoryList = new ArrayList<Category>();
       for (Map<String,Object> map:mapList){
           Category c = toCategory(map);
           categoryList.add(c);

           }
           return categoryList;
       }

    public Category toCategory(Map<String, Object> map) {
        Category category = CommonUtils.toBean(map,Category.class);
        String pid = (String) map.get("pid");
        if (pid!=null){
            Category parent = new Category();
            parent.setCid(pid);
            category.setParent(parent);
        }
        return category;
    }

}

