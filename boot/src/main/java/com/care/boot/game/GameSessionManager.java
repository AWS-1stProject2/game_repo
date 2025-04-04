package com.care.boot.game;

import org.springframework.stereotype.Component;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class GameSessionManager {
    private final Set<String> onlinePlayers = ConcurrentHashMap.newKeySet();
    private final ConcurrentLinkedQueue<String> matchQueue = new ConcurrentLinkedQueue<>();
    private final Set<String> matchedPlayers = ConcurrentHashMap.newKeySet(); // ✅ 매칭된 플레이어 관리

    /**
     * ✅ 플레이어를 온라인 목록에 추가
     */
    public void addPlayer(String playerId) {
        onlinePlayers.add(playerId);
    }

    /**
     * ✅ 플레이어가 접속을 종료하거나 뒤로 가기 했을 때 모든 목록에서 제거
     */
    public void removePlayer(String playerId) {
        onlinePlayers.remove(playerId);
        removeFromQueue(playerId);  // ✅ 대기열에서 제거
        clearMatchedPlayer(playerId); // ✅ 매칭된 상태에서도 제거
    }

    /**
     * ✅ 현재 온라인 플레이어 목록 조회
     */
    public Set<String> getOnlinePlayers() {
        return Set.copyOf(onlinePlayers);
    }

    /**
     * ✅ 매칭 대기열에 추가 (중복 방지)
     */
    public void addToMatchQueue(String playerId) {
        if (!matchQueue.contains(playerId) && !matchedPlayers.contains(playerId)) {
            matchQueue.add(playerId);
        }
    }

    /**
     * ✅ 매칭할 상대 찾기 (동기화 처리)
     */
    public synchronized String findMatch(String playerId) {
        Iterator<String> iterator = matchQueue.iterator();
        while (iterator.hasNext()) {
            String opponent = iterator.next();
            if (!opponent.equals(playerId) && !matchedPlayers.contains(opponent)) {
                iterator.remove(); // ✅ 대기열에서 제거
                matchedPlayers.add(playerId);
                matchedPlayers.add(opponent);
                return opponent;
            }
        }
        return null;
    }

    /**
     * ✅ 대기열에서 플레이어 제거 (존재하면)
     */
    public synchronized void removeFromQueue(String playerId) {
        matchQueue.remove(playerId);
    }

    /**
     * ✅ 플레이어가 이미 대기열에 있는지 확인
     */
    public boolean isInQueue(String playerId) {
        return matchQueue.contains(playerId);
    }

    /**
     * ✅ 매칭이 끝나면 플레이어 매칭 상태 초기화 (자동 재매칭 가능하도록 수정)
     */
    public void clearMatchedPlayer(String playerId) {
        matchedPlayers.remove(playerId);
    }

    /**
     * ✅ 강제 종료된 플레이어가 다시 매칭될 수 있도록 초기화
     */
    public void resetPlayerState(String playerId) {
        removeFromQueue(playerId);
        clearMatchedPlayer(playerId);
    }
}
