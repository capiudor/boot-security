package top.capiudor.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WebController {

    private final String PREFIX = "pages/";

    /**
     * 欢迎页
     * @return
     */
    @RequestMapping("/")
    public String index(){
        return "welcome";
    }


    /**
     * 去登录页面
     * @return
     */
    @RequestMapping(value = "/toLogin",method = RequestMethod.GET)
    public String toLogin(){
        return PREFIX + "login";
    }


    /**
     * level1页面映射
     * @param path
     * @return
     */
    @RequestMapping("/level1/{path}")
    public String level1(@PathVariable("path") String path){
        return PREFIX + "level1/" + path;
    }

    /**
     * level1页面映射
     * @param path
     * @return
     */
    @RequestMapping("/level2/{path}")
    public String level2(@PathVariable("path") String path){
        return PREFIX + "level2/" + path;
    }

    /**
     * level1页面映射
     * @param path
     * @return
     */
    @RequestMapping("/level3/{path}")
    public String level3(@PathVariable("path") String path){
        return PREFIX + "level3/" + path;
    }














}
