package com.udacity.jwdnd.course1.cloudstorage.mapper;
//import com.udacity.jwdnd.c1.review.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE username = #{userId")
    User getUserId(Integer userId);

    @Insert("INSERT INTO CREDENTIALS (username, salt, password, key) VALUES(#{username}, #{salt}, #{password}, #{key}")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(Credential user);


    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    Credential getCredential(Integer credentialId);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    void deleteCredential(Integer credentialId);

    @Update("UPDATE CREDENTIALS SET url = #{url}, key = #{key}, password = #{password}, username = #{newUserName} WHERE credentialid = #{credentialid}")
    void updateCredential(Integer credentialId, String newUserName, String url, String key, String password);
}


