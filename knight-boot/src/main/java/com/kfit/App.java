package com.kfit;

import cn.com.reformer.netty.communication.QrcodeTcpMessageSender;
import cn.com.reformer.netty.server.TCPServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * 大家也许会看到有些demo使用了3个注解： @Configuration；
 * 
 * @EnableAutoConfiguration
 * @ComponentScan
 * 
 * 	其实：@SpringBootApplication申明让spring boot自动给程序进行必要的配置，
 * 
 * 等价于以默认属性使用@Configuration，
 *  @EnableAutoConfiguration和@ComponentScan
 * 
 * 所以大家不要被一些文档误导了，让自己很迷茫了，希望本文章对您有所启发；
 * 
 * @author Angel(QQ:412887952)
 * @version v.0.1
 */
@Controller
@EnableAutoConfiguration
@SpringBootApplication
public class App {

	@Autowired
	private QrcodeTcpMessageSender qrcodeTcpMessageSender;

	@RequestMapping(value ="/hello", method = RequestMethod.GET)
	@ResponseBody
	public String hello(){
		return "hello world";
	}
	@RequestMapping(value ="/open", method = RequestMethod.GET)
	@ResponseBody
	public String open(){
		qrcodeTcpMessageSender.openDoor("0090C2501025","123456789");
		return "open success";
	}

	//这里指定是条状的jsp界面
	@RequestMapping(value = "/index1")
	public String index(Model model) {
		model.addAttribute("sb", "this is my fries测试不是好领导了副经理看fjldj 1123123");
		return "index";
	}

	@RequestMapping(value = "json")
	@ResponseBody
	public Map<String, Object> mytest() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "Ryan");
		map.put("age", "3");
		map.put("sex", "man");
		return map;
	}


	@Bean
	InternalResourceViewResolver internalResourceViewResolver () {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
