package com.atguigu.admin.controller;

import com.atguigu.admin.bean.DaPrLocation;
import com.atguigu.admin.bean.User;
import com.atguigu.admin.exception.UserTooManyException;
import com.atguigu.admin.service.DaPrLocationService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
public class TableController {

    @Autowired
    DaPrLocationService daPrLocationService;

    /**
     * 1、 ErrorMvcAutoConfiguration  自动配置异常处理规则
     *   ○ 容器中的组件：类型：DefaultErrorAttributes -> id：errorAttributes
     *     ■ public class DefaultErrorAttributes implements ErrorAttributes, HandlerExceptionResolver
     *     ■ DefaultErrorAttributes：定义错误页面中可以包含哪些数据。
     *   ○ 容器中的组件：类型：BasicErrorController --> id：basicErrorController（json+白页 适配响应）
     *     ■ 处理默认 /error 路径的请求；页面响应 new ModelAndView("error", model)；
     *     ■ 容器中有组件 View->id是error；（响应默认错误页）
     *     ■ 容器中放组件 BeanNameViewResolver（视图解析器）；按照返回的视图名作为组件的id去容器中找View对象。
     *   ○ 容器中的组件：类型：DefaultErrorViewResolver -> id：conventionErrorViewResolver
     *     ■ 如果发生错误，会以HTTP的状态码 作为视图页地址（viewName），找到真正的页面
     *     ■ error/404、5xx.html
     * ------------------------------------------------------------------------------------------------
     * 2、默认规则
     * ● 默认情况下，Spring Boot提供/error处理所有错误的映射
     * ● 要完全替换默认行为，可以实现 ErrorController 并注册该类型的Bean定义，或添加ErrorAttributes类型的组件以使用现有机制但替换其内容。
     * ● error/下的4xx，5xx页面会被自动解析；
     * ------------------------------------------------------------------------------------------------
     * 3、异常处理步骤流程
     *      4.1、执行目标方法，目标方法运行期间有任何异常都会被catch、而且标志当前请求结束；并且用 dispatchException
     *      4.2、进入视图解析流程（页面渲染？）
     *          processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
     *      4.3、mv = processHandlerException；处理handler发生的异常，处理完成返回ModelAndView；
     *          ● 1、遍历所有的 handlerExceptionResolvers，看谁能处理当前异常【HandlerExceptionResolver处理器异常解析器】
     *             ○ 1、DefaultErrorAttributes先来处理异常。把异常信息保存到request域，并且返回null；
     *             ○ 2、默认没有任何人能处理异常，所以异常会被抛出
     *          ● 2、如果没有任何人能处理最终底层就会发送 /error 请求。会被底层的BasicErrorController处理
     *          ● 3、解析错误视图；遍历所有的  ErrorViewResolver  看谁能解析。
     *          ● 4、默认的 DefaultErrorViewResolver ,作用是把响应状态码作为错误页的地址，error/500.html
     *          ● 5、模板引擎最终响应这个页面 error/500.html
     *  4、定制错误处理逻辑
     *      4.1、自定义错误页
     *          ○ error/404.html   error/5xx.html；有精确的错误状态码页面就匹配精确，没有就找 4xx.html；如果都没有就触发白页
     *      4.2、@ControllerAdvice+@ExceptionHandler处理全局异常；
     *           底层是 ExceptionHandlerExceptionResolver 支持的
     *      4.3、@ResponseStatus+自定义异常 ；
     *           底层是 ResponseStatusExceptionResolver，把responseStatus注解的信息底层调用 response.sendError(statusCode, resolvedReason)--此次请求立即停止，tomcat发送的/error
     *      4.4、Spring底层的异常，如 参数类型转换异常；DefaultHandlerExceptionResolver 处理框架底层的异常。
     *          ○ response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
     *      4.5、自定义实现 HandlerExceptionResolver处理异常；可以作为默认的全局异常处理规则
     *      4.6、 ErrorViewResolver  实现自定义处理异常；
     *           ○ response.sendError 。error请求就会转给controller
     *           ○ 你的异常没有任何人能处理。tomcat底层 response.sendError。error请求就会转给controller
     *           ○ basicErrorController 要去的页面地址是 ErrorViewResolver  ；
     * @return
     */
    @GetMapping("/basic_table")
    public String basic_table(@RequestParam("a") int a){
        int i = 10/0;
        return "table/basic_table";
    }

    @GetMapping("/dynamic_table")
    public String dynamic_table(@RequestParam(value = "pn", defaultValue = "1")Integer pn, Model model){
        //表格内容的遍历
        /*List<User> users = Arrays.asList(new User("zhangsan", "123456"),
                new User("lisi", "123444"),
                new User("haha", "aaaaa"),
                new User("hehe ", "aaddd"));
        model.addAttribute("users",users);
        if (users.size()>3) {
            throw new UserTooManyException();
        }*/
        List<DaPrLocation> list = daPrLocationService.list();
        //model.addAttribute("users", list);

        //分页查询, 构造分页参数
        Page<DaPrLocation> page = new Page<>(pn, 10);
        //调用page进行分页
        Page<DaPrLocation> daPrLocationPage = daPrLocationService.page(page, null);

        model.addAttribute("page", daPrLocationPage);
        return "table/dynamic_table";
    }

    @GetMapping("/responsive_table")
    public String responsive_table(){
        return "table/responsive_table";
    }

    @GetMapping("/editable_table")
    public String editable_table(){
        return "table/editable_table";
    }

    @GetMapping("/daPrLocation/delete/{locId}")
    public String deleteDaPrLocation(@PathVariable("locId") Integer locId, @RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                     RedirectAttributes redirectAttributes){
        //daPrLocationService.removeById(locId);
        redirectAttributes.addAttribute("pn", pn);
        return "redirect:/dynamic_table";
    }
}
