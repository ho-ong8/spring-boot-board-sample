# spring-boot-board-sample
> Spring Boot Board Sample.

<br>

## Development Environment
<div style="display: inline-block">
    <img src="https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=flat-square&logo=IntelliJIDEA&logoColor=white" />
    <img src="https://img.shields.io/badge/HTML-e34f26?style=flat-square&logo=HTML5&logoColor=white" />
    <img src="https://img.shields.io/badge/CSS-1572b6?style=flat-square&logo=CSS3&logoColor=white" />
    <img src="https://img.shields.io/badge/JavaScript-f7df1e?style=flat-square&logo=JavaScript&logoColor=white" />
    <img src="https://img.shields.io/badge/jQuery-0769ad?style=flat-square&logo=jQuery&logoColor=white" />
    <img src="https://img.shields.io/badge/Java-3a75b0?style=flat-square&logo=coffeescript&logoColor=white" />
    <img src="https://img.shields.io/badge/Spring%20Boot-6db33f?style=flat-square&logo=SpringBoot&logoColor=white" />
    <img src="https://img.shields.io/badge/Gradle-02303a?style=flat-square&logo=Gradle&logoColor=white" />
    <img src="https://img.shields.io/badge/MySQL-4479a1?style=flat-square&logo=MySQL&logoColor=white" />
</div>

<br>

- IDE: IntelliJ IDEA
- Language: HTML5, CSS3, JavaScript, JAVA (JDK 21)
- Library: jQuery
- Framework: Spring Boot
- Build: Gradle
- DBMS: MySQL

<br>

## Main Function
- 회원(Member)
    - 로그인: /member/login
    - 로그아웃: /member/logout
    - 회원가입: /member/join
    - 회원목록: /member/
    - 회원정보 조회: /member/{id}
    - 회원정보 수정: /member/update
    - 회원정보 삭제: /member/delete/{id}

- 게시판(Board)
    - 게시글 작성: /board/write
    - 게시글 목록: /board/
    - 게시글 조회: /board/{id}
    - 게시글 수정: /board/update/{id}, /board/update
    - 게시글 삭제: /board/delete/{id}
    - 게시글 페이징: /board/paging
    - 게시글 파일첨부: /board/write, /board/{id}

<br>

## Add Function
- 회원(Member)
    - 이메일 중복체크: /member/check-email

- 게시판(Board)
    - 댓글 작성: /comment/write
    - 댓글 목록: /board/{id}, /board/update

<br>

## Create MySQL Database
```SQL
create database spring_boot_board;
create user hoong@localhost identified by '8888';
grant all privileges on spring_boot_board.* to hoong@localhost;
```

<br>

## MySQL Database Table
```SQL
/* 회원 */
drop table if exists spring_boot_board.member;

create table spring_boot_board.member (
    id              bigint primary key not null auto_increment,
    member_email    varchar(30) unique null,
    member_password varchar(30) null,
    member_name     varchar(30) null
);


/* 게시판 */
drop table if exists spring_boot_board.board;

create table spring_boot_board.board (
    id             bigint primary key not null auto_increment,
    board_writer   varchar(30)            null,
    board_password varchar(30)            null,
    board_title    varchar(50)            null,
    board_contents varchar(500)           null,
    board_hits     int default 0          null,
    file_attached  int default 0          null,
    created_time   datetime default now() null,
    updated_time   datetime               null
);


/* 게시판 파일 */
drop table if exists spring_boot_board.board_file;

create table spring_boot_board.board_file (
    id                 bigint primary key not null auto_increment,
    original_file_name varchar(255)           null,
    stored_file_name   varchar(255)           null,
    board_id           bigint                 null,
    created_time       datetime default now() null,
    updated_time       datetime               null,
    constraint fk_board_file foreign key (board_id) references board (id) on delete cascade
);


/* 댓글 */
drop table if exists spring_boot_board.comment;

create table spring_boot_board.comment (
    id               bigint primary key not null auto_increment,
    comment_writer   varchar(30)            null,
    comment_contents varchar(300)           null,
    board_id         bigint                 null,
    created_time     datetime default now() null,
    updated_time     datetime               null,
    constraint fk_comment foreign key (board_id) references board (id) on delete cascade
);
```
