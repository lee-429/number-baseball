package com.hyunhak.numberbaseball.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    // 0~9 중에서 중복 없는 4자리 랜덤 숫자 생성
    public String createAnswer() {

        // 0부터 9까지 숫자를 리스트로 생성
        // IntStream.range(0,10) -> 0,1,2,3,4,5,6,7,8,9
        List<Integer> numbers = IntStream.range(0, 10)
            .boxed() // int -> Integer (List에 담기 위해)
            .collect(Collectors.toList());

        // 리스트를 무작위로 섞음
        Collections.shuffle(numbers);

        // 첫 자리가 0이면 안 되므로
        // 0이 맨 앞에 있으면 두 번째 숫자와 자리 교체
        if (numbers.get(0) == 0) {
            Collections.swap(numbers, 0, 1);
        }

        // 앞에서부터 4개만 선택
        // 숫자를 문자열로 변환 후 이어붙여서 4자리 문자열 생성
        return numbers.stream()
            .limit(4) // 앞에서 4개만
            .map(String::valueOf) // 숫자 -> 문자열 변환
            .collect(Collectors.joining()); // 하나의 문자열로 합치기
    }

    public boolean isUnique(String input) {
        // 1. null이나 4자리가 아닌 경우 바로 탈락
        if (input == null || input.length() != 4) {
            return false;
        }

        // 2. 첫 번째 자리가 '0'인지 확인
        if (input.charAt(0) == '0') {
            return false;
        }

        // 3. 입력값 중복 체크 (Set 활용)
        Set<Character> chars = new HashSet<>();
        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c)) return false;
            chars.add(c);
        }

        // 4. 중복이 없다면 4개가 들어있어야 함
        return chars.size() == 4;
    }

    // 사용자가 입력한 값과 정답을 비교해서 스트라이크와 볼 개수를 계산
    public int[] check(String answer, String input) {

        int strike = 0;
        int ball = 0;

        for (int i = 0; i < 4; i++) {

            // 자리도 같고 숫자도 같으면 -> strike
            if (answer.charAt(i) == input.charAt(i)) {
                strike++;
            // 숫자는 존재하지만 자리가 다르면 -> ball
            } else if (answer.contains(String.valueOf(input.charAt(i)))) {
                ball++;
            }
        }

        // strike와 ball 값을 배열로 반환
        return new int[]{strike, ball};
    }
}
