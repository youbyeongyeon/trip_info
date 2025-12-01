package com.example.demo.repository;

import java.util.*;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.Continent;
import com.example.demo.dto.Tag;
import com.example.demo.dto.Country;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final SqlSessionTemplate sql;

    public List<BoardDTO> selectAllPosts() {
        return sql.selectList("Board.selectAllPosts");
    }

    public BoardDTO selectPostDetail(int id) {
        return sql.selectOne("Board.selectPostDetail", id);
    }

    public void insertPost(BoardDTO boardDTO) {
        sql.insert("Board.insertPost", boardDTO);
    }

    public void insertPostTag(int postId, int tagId) {        
        Map<String, Object> params = new HashMap<>();
        params.put("postId", postId);
        params.put("tagId", tagId);
        
        sql.insert("Board.insertPostTag", params);
    }

    public List<Continent> findAllContinents() {
        return sql.selectList("Board.findAllContinents");
    }

    public List<Tag> findAllTags() {
        return sql.selectList("Board.findAllTags");
    }

    public List<Country> findAllCountries() {
        return sql.selectList("Board.findAllCountries");
    }

    public List<BoardDTO> selectPostsByCountry(int countryId) {
        return sql.selectList("Board.selectPostsByCountry", countryId);
    }

    public List<BoardDTO> selectPostsByTag(int tagId) {
        return sql.selectList("Board.selectPostsByTag", tagId);
    }

    public List<BoardDTO> selectPostsByContinent(int continentId) {
        return sql.selectList("Board.selectPostsByContinent", continentId);
    }

    public List<BoardDTO> selectPostsByContinentAndTag(java.util.Map<String, Object> params) {
        return sql.selectList("Board.selectPostsByContinentAndTag", params);
    }

    public List<String> selectTagNamesByPostId(int postId) {
        return sql.selectList("Board.selectTagNamesByPostId", postId);
    }

    public void deletePost(int id) {
        sql.delete("Board.deletePost", id);
    }

    public void deletePostTags(int id) {
        sql.delete("Board.deletePostTags", id);
    }

    public void updatePost(BoardDTO boardDTO) {
        sql.update("Board.updatePost", boardDTO);
    }
}