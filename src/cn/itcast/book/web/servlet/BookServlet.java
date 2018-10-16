package cn.itcast.book.web.servlet;

import cn.itcast.book.domin.Book;
import cn.itcast.book.service.BookService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.pager.PageBean;
import cn.itcast.servlet.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2018/10/11.
 */
@WebServlet(name = "BookServlet",urlPatterns = "/BookServlet")
public class BookServlet extends BaseServlet {
    private BookService bookService = new BookService();
    private int getPageCode(HttpServletRequest request){
        int pc = 1;
        String param = request.getParameter("pageCode");
        if (param!=null&&!param.trim().isEmpty()) {
            pc=Integer.valueOf(param);
        }
        return pc;
    }

    private String getUrl(HttpServletRequest request){
        String url = request.getRequestURI()+"?"+request.getQueryString();
        int index = url.lastIndexOf("&pageCode");
        if(index != -1) {
            url = url.substring(0, index);
        }
        return url;
    }
    public String findByCriteria(HttpServletRequest request,HttpServletResponse response){
        int pageCode = getPageCode(request);

        String url = getUrl(request);

        String cid = request.getParameter("cid");

        PageBean<Book> pb = bookService.findByCategory(cid,pageCode);
        pb.setUrl(url);
        request.setAttribute("pb",pb);


        return "f:/jsps/book/list.jsp";
    }

    public String findByName(HttpServletRequest request,HttpServletResponse response){
        int pageCode = getPageCode(request);

        String url = getUrl(request);

        String bname = request.getParameter("bname");

        PageBean<Book> pb = bookService.findByName(bname,pageCode);
        pb.setUrl(url);
        request.setAttribute("pb",pb);


        return "f:/jsps/book/list.jsp";
    }
    public String findByPress(HttpServletRequest request,HttpServletResponse response){
        int pageCode = getPageCode(request);

        String url = getUrl(request);
       
        String press = request.getParameter("press");

//        try {
//            byte[] s=press.getBytes("utf-8");
//            String a =s.toString();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        PageBean<Book> pb = bookService.findByPress(press,pageCode);
        pb.setUrl(url);
        request.setAttribute("pb",pb);


        return "f:/jsps/book/list.jsp";
    }

    public String findByCombination(HttpServletRequest request,HttpServletResponse response){
        int pageCode = getPageCode(request);

        String url = getUrl(request);

//        String press = request.getParameter("press");
        Book criteria = CommonUtils.toBean(request.getParameterMap(),Book.class);

        PageBean<Book> pb = bookService.findByCombination(criteria,pageCode);
        pb.setUrl(url);
        request.setAttribute("pb",pb);


        return "f:/jsps/book/list.jsp";
    }


}
