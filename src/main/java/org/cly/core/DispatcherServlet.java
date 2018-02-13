package org.cly.core;

import org.cly.bean.Data;
import org.cly.bean.Handler;
import org.cly.bean.Param;
import org.cly.bean.View;
import org.cly.helper.*;
import org.cly.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 请求转发器
 * Created by chen on 2017/5/24.
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet{

    @Override
    public void init(ServletConfig config) throws ServletException {
        //初始化相关 Helper 类
        HelperLoader.init();
        //获取 ServletContext 对象（用于注册 Servlet）
        ServletContext servletContext = getServletConfig().getServletContext();
        //注册处理 JSP 的 Servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAPPJspPath() + "*");
        //注册处理器静态资源的默认 Servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
        UploadHelper.init(servletContext);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取请求方法与请求路径
        String requestMethod = request.getMethod().toLowerCase();
        String requestPath = request.getPathInfo();
        if(requestPath.equals("/favicon.ico")){
            return;
        }
        //获取 Action 处理器
        Handler handler = ControllerHelper.getHandler(requestMethod,requestPath);
        if(handler != null){
            //获取 Controller 类及其 Bean 实例
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);
            Param param;
            if(UploadHelper.isMultipart(request)){
                param = UploadHelper.createParam(request);
            }else{
                param = RequestHelper.createParam(request);
            }

            //调用 Action 方法
            Object result;
            Method actionMethod = handler.getActionMethod();
            if(param.isEmpty()){
                result = ReflectionUtil.invokeMethod(controllerBean,actionMethod);
            }else{
                result = ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);
            }
            //处理 Action 方法返回值
            if(result instanceof View){
                //返回 JSP 页面
                handleViewResult((View) result,request,response);
            }else if(result instanceof Data){
                //返回 JSON 数据
                handleDataResult((Data) result,response);
            }
        }
    }

    private void handleViewResult(View view,HttpServletRequest request,HttpServletResponse response) throws IOException,
            ServletException {
        String path = view.getPath();
        if (StringUtil.isNotEmpty(path)) {
            if (path.startsWith("/")) {
                response.sendRedirect(request.getContextPath() + path);
            } else {
                Map<String, Object> model = view.getModel();
                for (Map.Entry<String, Object> entry : model.entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
                request.getRequestDispatcher(ConfigHelper.getAPPJspPath() + path).forward(request, response);
            }
        }
    }

    private void handleDataResult (Data data, HttpServletResponse response) throws IOException {
        Object model = data.getModel();
        if (model != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            String json = JsonUtil.toJson(model);
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }

}
