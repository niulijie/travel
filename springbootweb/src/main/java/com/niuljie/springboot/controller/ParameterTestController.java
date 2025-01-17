package com.niuljie.springboot.controller;

import com.niuljie.springboot.dto.Person;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ParameterTestController {

    /**
     * @PathVariable 路径变量
     * @RequestHeader 获取请求头
     * @RequestParam 获取请求参数
     * @return
     */
    @GetMapping("/car/{id}/owner/{username}")
    public Map<String, Object> getCar(@PathVariable("id") Integer id,
                                      @PathVariable("username") String name,
                                      @PathVariable Map<String,String> pv,
                                      @RequestHeader("User-Agent") String userAgent,
                                      @RequestHeader Map<String,String> header,
                                      @RequestParam("age") Integer age,
                                      @RequestParam("inters") List<String> inters,
                                      @RequestParam Map<String,String> params){

        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("name",name);
        map.put("pv",pv);
        map.put("userAgent",userAgent);
        map.put("headers",header);
        map.put("age",age);
        map.put("inters",inters);
        map.put("params",params);
        return map;
    }

    /**
     * @CookieValue 获取cookie值
     * @return
     */
    @GetMapping("/car/{id}/other/{username}")
    public Map<String, Object> getCar2(@PathVariable("id") Integer id,
                                       @PathVariable("username") String name,
                                       @PathVariable Map<String,String> pv,
                                       @RequestHeader("User-Agent") String userAgent,
                                       @RequestHeader Map<String,String> header,
                                       @RequestParam("age") Integer age,
                                       @RequestParam("inters") List<String> inters,
                                       @RequestParam Map<String,String> params,
                                       @CookieValue("UM_distinctid") String _ga,
                                       @CookieValue(required = false) Cookie cookie){

        Map<String,Object> map = new HashMap<>();
        map.put("age",age);
        map.put("inters",inters);
        map.put("params",params);
        map.put("_ga",_ga);
        System.out.println(cookie.getName()+"===>"+cookie.getValue());
        return map;
    }

    /**
     * @RequestBody 获取请求体
     * @return
     */
    @PostMapping("/save")
    public Map postMethod(@RequestBody String content){
        Map<String,Object> map = new HashMap<>();
        map.put("content",content);
        return map;
    }

    /**
     * @MatrixVariable 获取矩阵变量
     * 1、语法： 请求路径：/cars/sell;low=34;brand=byd,audi,yd
     *                  /cars/sell;low=34;brand=byd,brand=audi,brand=yd
     * 2、SpringBoot默认是禁用了矩阵变量的功能
     *      手动开启：原理。对于路径的处理。UrlPathHelper进行解析。removeSemicolonContent（移除分号内容）支持矩阵变量的
     * 3、矩阵变量必须有url路径变量才能被解析
     * @return
     */
    @GetMapping("/cars/{path}")
    public Map carsSell(@MatrixVariable("low") Integer low,
                        @MatrixVariable("brand") List<String> brand,
                        @PathVariable("path") String path){
        Map<String,Object> map = new HashMap<>();

        map.put("low",low);
        map.put("brand",brand);
        map.put("path",path);
        return map;
    }

    /**
     *  @MatrixVariable 获取矩阵变量-变量名有相同名字
     *  请求路径：/boss/1;age=20/2;age=10
     */
    @GetMapping("/boss/{bossId}/{empId}")
    public Map boss(@MatrixVariable(value = "age",pathVar = "bossId") Integer bossAge,
                    @MatrixVariable(value = "age",pathVar = "empId") Integer empAge){
        Map<String,Object> map = new HashMap<>();

        map.put("bossAge",bossAge);
        map.put("empAge",empAge);
        return map;

    }

    /**
     * 如果通过页面表单发送，不加@RequestBody注解，解析器为ServletModelAttributeMethodProcessor；
     * 1.先创建一个空attribute对象
     * 2.WebDataBinder binder = binderFactory.createBinder(webRequest, attribute, name);
     *   WebDataBinder :web数据绑定器，将请求参数的值绑定到指定的JavaBean里面
     *   WebDataBinder利用自身conversionService转换服务中的converter转换器(124个)，将请求数据转成指定的数据类型。再次封装到JavaBean中
     * --------------------------------------------------------------------------------------
     * 加了@RequestBody注解，解析器为RequestResponseBodyMethodProcessor
     * @param person
     * @return
     */
    @PostMapping("/save/person")
    public Person savePerson(@RequestBody Person person){

        return person;

    }
}
