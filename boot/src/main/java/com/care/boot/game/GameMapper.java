package com.care.boot.game;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.care.boot.gamedto.GameDTO;
import com.care.boot.gamedto.PlayerStatsDTO;

@Mapper
public interface GameMapper {

    void insertGameResult(GameDTO gameDTO);

    PlayerStatsDTO getPlayerStats(@Param("playerId") String playerId);

    int createPlayerStats(@Param("playerId") String playerId);

    // ✅ 점수 업데이트 추가
    int updatePlayerStats(@Param("playerId") String playerId, @Param("result") String result, @Param("scoreChange") int scoreChange);

    // ✅ `score` 기준으로 내림차순 정렬
    List<PlayerStatsDTO> getRanking();

    List<GameDTO> getGameHistory(@Param("playerId") String playerId);

    int deleteGameRecord(@Param("gameId") int gameId);
}
