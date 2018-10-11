package cn.itcast.category.web;

import cn.itcast.category.domin.Category;
import cn.itcast.category.service.CategoryService;
import cn.itcast.servlet.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2018/10/11.
 */
public class CategoryServlet extends BaseServlet {
    private CategoryService categoryService = new CategoryService();


    public String findAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> parents = categoryService.findAll();
        req.setAttribute("parents",parents);
        return "f:/jsps/left.jsp";
    }
}
