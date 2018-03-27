package com.kotei.config;

import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/19
 * 修改历史：
 * 1. [2018/3/19]创建文件
 *
 * @author chunl
 */
@Configuration
@EnableTransactionManagement
//@AutoConfigureAfter({MybatisConfiguration.class})
public class MybatisConfiguration implements TransactionManagementConfigurer {

    private static Logger logger = LoggerFactory.getLogger(MybatisConfiguration.class);

    /**
     * 配置类型别名
     */
    @Value("${mybatis.typeAliasesPackage}")
    private String typeAliasesPackage;

    /**
     * 配置mapper的扫描，找到所有的mapper.xml映射文件
     */
    @Value("${mybatis.mapperLocations}")
    private String mapperLocations;

    /**
     * 加载全局的配置文件
     */
    @Value("${mybatis.configLocation}")
    private String configLocation;

    /**
     * 加载BasePackage
     */
    //@Value("${mybatis.basePackage}")
   // private String basePackage;


    @Autowired
    private DataSource dataSource;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {
        try {
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource);

            // 读取配置
            sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);

            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(mapperLocations);

            sqlSessionFactoryBean.setMapperLocations(resources);

            sqlSessionFactoryBean.setConfigLocation(new DefaultResourceLoader().getResource(configLocation));

            //添加插件  （改为使用配置文件加载了）
            sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageHelper()});

            return sqlSessionFactoryBean.getObject();

        } catch (IOException e) {
            logger.warn("mybatis reolver mapper*xml is error");
            return null;

        } catch (Exception e) {
            logger.warn("mybatis sqlSessionFactoryBean create error");
            return null;

        }

    }


    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public PageHelper pageHelper() {
        logger.info("MyBatis分页插件PageHelper");
        // 分页插件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {

        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();

        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");

        mapperScannerConfigurer.setBasePackage("com.kotei.web.dao");

        return mapperScannerConfigurer;
    }

}

