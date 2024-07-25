package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.apache.ibatis.annotations.*;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    static Notes[] getNotesForUser(Integer userId) {
        return null;
    }


    @Insert("INSERT INTO NOTES (title, description, userid) " +
            "VALUES(#{title}, #{description}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Notes notes);

    @Select("SELECT * FROM NOTES")
    Notes[] getNoteListings();

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    static Notes getNote(Integer noteId) {
        return null;
    }


    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    void deleteNote(Integer noteId);

    @Update("UPDATE NOTES SET title = #{title}, description = #{description} WHERE noteid = #{noteId}")
    void updateNote(Integer noteId, String title, String description);
}
