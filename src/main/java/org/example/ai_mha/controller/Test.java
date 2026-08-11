package org.example.ai_mha.controller;
import org.example.ai_mha.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController//返回的格式
@RequestMapping("/api")//请求路径
public class Test {
    @GetMapping("/test")//请求类型
    public Result<String> test() {
        //Result result = new Result();
        //result.setCode("200");
        //System.out.println(result.getCode());
        return Result.success("hello");
    }
}
