package com.zhihu.demo.config;


import com.zhihu.demo.dao.CommentDao;
import com.zhihu.demo.dao.QuestionDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.naming.Name;
import java.io.IOException;
import java.io.Reader;


/**
 * 访问数据库
 */
@Configuration  //告诉spring要扫描这个类
public class MybaitsConfiguration {
    @Resource(name = "sqlSession")
    SqlSession sqlSession;

    @Bean("CommentMapper")
    public CommentDao getCommentSqlSession() throws IOException {
        CommentDao mapper = sqlSession.getMapper(CommentDao.class);
        return mapper;
    }
    @Bean("sqlSession")
    public SqlSession getSqlSession() throws IOException {
        //通过配置文件获取数据库连接信息
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        //通过配置信息构建一个sqlSeesionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        //通过sqlSessionFactory打开一个数据库会话
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        return sqlSession;
    }
    @Bean("QuestionMapper")
    public QuestionDao getQuestionSqlSession() throws IOException {
        QuestionDao mapper = sqlSession.getMapper(QuestionDao.class);
        return mapper;
    }
//
}
