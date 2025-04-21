package com.gongdaeoppa.demo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Email {
    private int id;
    private String name;
    private String email;
    private String datetime;
}
