package com.yapengren.springcloud.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.yapengren.springcloud.entities.Dept;
import com.yapengren.springcloud.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;

    @PostMapping(value = "/dept/add")
    public boolean add(@RequestBody Dept dept) {
        return deptService.add(dept);
    }

    @GetMapping(value = "/dept/get/{id}")
    //一旦调用服务方法失败并抛出了错误信息后，会自动调用@HystrixCommand标注好的fallbackMethod调用类中的指定方法
    @HystrixCommand(fallbackMethod = "processHystrix_Get")
    public Dept get(@PathVariable("id") Long id) {
        Dept dept = deptService.get(id);
        if (null == dept) {
            throw new RuntimeException("该ID " + id + "没有对应的数据");
        }
        return dept;
    }

    public Dept processHystrix_Get(@PathVariable("id") Long id) {
        return new Dept().setDeptno(id).setDname("该id " + id + "没有对应的数据--@HystrixCommand").setDb_source("no database");
    }

    @GetMapping(value = "/dept/list")
    public List<Dept> list() {
        return deptService.list();
    }
}
