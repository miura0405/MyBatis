package com.example.demo.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.dao.UserInfoMapper;
import com.example.demo.dto.UserAddRequest;
import com.example.demo.dto.UserSearchRequest;
import com.example.demo.dto.UserUpdateRequest;
import com.example.demo.entity.UserInfo;

/**
 * ユーザー情報 Service
 */
@Service
public class UserInfoService {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoService.class);

    /**
     * ユーザー情報 Mapper
     */
    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * ユーザー情報全件検索
     * 
     * @return 検索結果
     */
    public List<UserInfo> findAll() {
        logger.debug("ユーザー情報全件検索開始");
        List<UserInfo> result = userInfoMapper.findAll();
        logger.debug("ユーザー情報全件検索終了");
        return result;
    }

    /**
     * ユーザー情報主キー検索
     * 
     * @return 検索結果
     */
    public UserInfo findById(Long id) {
        logger.debug("ユーザー情報主キー検索開始 id: {}", id);
        UserInfo result = userInfoMapper.findById(id);
        logger.debug("ユーザー情報主キー検索終了 id: {}", id);
        if (result == null) {
            throw new NoSuchElementException("ユーザー情報が見つかりませんでした。id: " + id);
        }
        return result;
    }

    /**
     * ユーザー情報検索
     * 
     * @param userSearchRequest リクエストデータ
     * @return 検索結果
     */
    public List<UserInfo> search(UserSearchRequest userSearchRequest) {
        logger.debug("ユーザー情報検索開始 request: {}", userSearchRequest);
        List<UserInfo> result = userInfoMapper.search(userSearchRequest);
        logger.debug("ユーザー情報検索終了 request: {}", userSearchRequest);
        return result;
    }

    /**
     * ユーザ情報登録
     * 
     * @param userAddRequest リクエストデータ
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void save(UserAddRequest userAddRequest) {
        logger.debug("ユーザー情報登録開始 request: {}", userAddRequest);
        try {
            userInfoMapper.save(userAddRequest);
            logger.debug("ユーザー情報登録終了 request: {}", userAddRequest);
        } catch (DataAccessException e) {
            logger.error("ユーザー情報登録中にエラーが発生しました。", e);
            throw new RuntimeException("ユーザー情報登録に失敗しました。", e);
        }
    }

    /**
     * ユーザ情報更新
     * 
     * @param userEditRequest リクエストデータ
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void update(UserUpdateRequest userUpdateRequest) {
        logger.debug("ユーザー情報更新開始 request: {}", userUpdateRequest);
        try {
            userInfoMapper.update(userUpdateRequest);
            logger.debug("ユーザー情報更新終了 request: {}", userUpdateRequest);
        } catch (DataAccessException e) {
            logger.error("ユーザー情報更新中にエラーが発生しました。", e);
            throw new RuntimeException("ユーザー情報更新に失敗しました。", e);
        }
    }

    /**
     * ユーザー情報論理削除
     * 
     * @param id
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void delete(Long id) {
        logger.debug("ユーザー情報論理削除開始 id: {}", id);
        try {
            userInfoMapper.delete(id);
            logger.debug("ユーザー情報論理削除終了 id: {}", id);
        } catch (DataAccessException e) {
            logger.error("ユーザー情報論理削除中にエラーが発生しました。", e);
            throw new RuntimeException("ユーザー情報論理削除に失敗しました。", e);
        }
    }

    @Autowired
    private DataSource dataSource;

    public void testConnection() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            logger.info("DB接続成功: {}", conn.getMetaData().getURL());
        }
    }
}