package me.hyekjung.springbootdeveloper.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Page {
    private int page;
    private int size;
}
