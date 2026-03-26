package com.miqroera.biosensor.util;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.Controller;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Collections;

/**
 * 代码生成器
 */
public class CodeGenerator {

    public static void main(String[] args) {
        String ip = "10.36.160.33";
        // String ip = "localhost";
        String url = "jdbc:mysql://" + ip + ":3306/biosensor";
        String username = "root";
        // String password = "root";
        String password = "EybTcTBcJCdjwOFh";
        String module = ".";
        String[] tableNames = {
                "sys_config"
        };

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("dongjingxiang") // 设置作者
                            .enableSpringdoc() // 开启 swagger 模式
                            .outputDir(module + "\\src\\main\\java")
                    ; // 指定输出目录
                })
                .packageConfig(builder -> builder
                        .parent("com.miqroera.biosensor")
                        .moduleName("domain")
                        .service("service")
                        .serviceImpl("service.impl")
                        .entity("model")
                        .mapper("mapper")
                        .xml("mapper")
                        .pathInfo(Collections.singletonMap(OutputFile.xml,
                                module + "\\src\\main\\resources\\mapper")))
                .strategyConfig(builder -> {
                    for (String tableName : tableNames) {
                        builder.addInclude(tableName).addTablePrefix("t_"); // 设置需要生成的表名
                    }
                    // 禁用生成 controller
                    Controller.Builder controllerBuilder = builder.controllerBuilder();
                    controllerBuilder.disable();

                    Entity.Builder entityBuilder = builder.entityBuilder();
                    entityBuilder.enableLombok()
                            .enableRemoveIsPrefix()
                            .fieldUseJavaDoc(true)
                            .addTableFills(new Column("create_time", FieldFill.INSERT))
                            .addTableFills(new Column("update_time",
                                    FieldFill.INSERT_UPDATE));
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
}
