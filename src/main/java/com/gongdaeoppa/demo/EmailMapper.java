package com.gongdaeoppa.demo;

import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface EmailMapper {
    
    @Results(id = "EmailMap", value = {
            @Result(property = "id", column = "em_idx"),
            @Result(property = "name", column = "em_name"),
            @Result(property = "email", column = "em_email"),
            @Result(property = "datetime", column = "em_datetime")
    })
    @Select(" SELECT * FROM `email` ORDER BY RAND() LIMIT 1 ")
    Optional<Email> findByRAND();
    
//    @Select(" SELECT * FROM `email` WHERE (1) and em_idx = #{id} ")
//    @ResultMap("EmailMap")
//    Optional<Email> findById(@Param("id") int id);
//
//    @Select(" SELECT * FROM `email` WHERE (1) and em_email = #{email} ")
//    @ResultMap("EmailMap")
//    Optional<Email> findByEmail(@Param("email") String email);
    

    
    
}
