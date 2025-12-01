package com.example.demo.service;

import java.util.List;
import java.util.HashMap;
import java.util.Map;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.Continent;
import com.example.demo.dto.Tag;
import com.example.demo.dto.Country;
import com.example.demo.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private void populateTagNamesStr(BoardDTO post) {
        if (post != null && post.getTagNames() != null && !post.getTagNames().isEmpty()) {
            post.setTagNamesStr(String.join(", ", post.getTagNames()));
        } else if (post != null) {
            post.setTagNamesStr("");
        }
    }

    @Transactional 
    public void createPost(BoardDTO boardDTO) {
        
        boardRepository.insertPost(boardDTO);
        
        int newPostId = boardDTO.getPostId(); 

        List<Integer> tagIds = boardDTO.getTags();
        
        if (tagIds != null && !tagIds.isEmpty()) {
            for (int tagId : tagIds) {
                boardRepository.insertPostTag(newPostId, tagId);
            }
        }
    }

    public List<BoardDTO> getAllPosts() {
        List<BoardDTO> posts = boardRepository.selectAllPosts();
        if (posts != null) {
            for (BoardDTO p : posts) {
                p.setTagNames(boardRepository.selectTagNamesByPostId(p.getPostId()));
                populateTagNamesStr(p);
            }
        }
        return posts;
    }

    public List<BoardDTO> getFilteredPosts(Integer continentId, Integer tagId) {
        // 둘 다 없는 경우 전체 반환
        if (continentId == null && tagId == null) {
            return getAllPosts();
        }

        // 대륙만 있는 경우
        if (continentId != null && tagId == null) {
            List<BoardDTO> posts = boardRepository.selectPostsByContinent(continentId);
            if (posts != null) {
                for (BoardDTO p : posts) {
                    p.setTagNames(boardRepository.selectTagNamesByPostId(p.getPostId()));
                    populateTagNamesStr(p);
                }
            }
            return posts;
        }

        // 태그만 있는 경우
        if (continentId == null && tagId != null) {
            List<BoardDTO> posts = boardRepository.selectPostsByTag(tagId);
            if (posts != null) {
                for (BoardDTO p : posts) {
                    p.setTagNames(boardRepository.selectTagNamesByPostId(p.getPostId()));
                    populateTagNamesStr(p);
                }
            }
            return posts;
        }

        // 둘 다 있는 경우
        Map<String, Object> params = new HashMap<>();
        params.put("continentId", continentId);
        params.put("tagId", tagId);
        List<BoardDTO> posts = boardRepository.selectPostsByContinentAndTag(params);
        if (posts != null) {
            for (BoardDTO p : posts) {
                p.setTagNames(boardRepository.selectTagNamesByPostId(p.getPostId()));
                populateTagNamesStr(p);
            }
        }
        return posts;
    }

    public BoardDTO getPostDetail(int id) {
        BoardDTO post = boardRepository.selectPostDetail(id);
        if (post != null) {
            post.setTagNames(boardRepository.selectTagNamesByPostId(post.getPostId()));
            populateTagNamesStr(post);
        }
        return post;
    }

    public List<Continent> findAllContinents() {
        return boardRepository.findAllContinents();
    }

    public List<Tag> findAllTags() {
        return boardRepository.findAllTags();
    }

    public List<Country> findAllCountries() {
        return boardRepository.findAllCountries();
    }
    
    public void deletePost(int id) {
        boardRepository.deletePost(id);
    }

    public void deletePostTags(int id) {
        boardRepository.deletePostTags(id);
    }

    @Transactional
    public void removePost(int id) {
        boardRepository.deletePostTags(id);
        boardRepository.deletePost(id);
    }

    public void updatePost(BoardDTO boardDTO) {
        boardRepository.updatePost(boardDTO);
    }

    public boolean isPasswordValid(int id, String password) {
        BoardDTO post = boardRepository.selectPostDetail(id);
        if (post == null) return false;
        String stored = post.getPassword();
        if (stored == null) return false;
        return stored.equals(password);
    }
}
