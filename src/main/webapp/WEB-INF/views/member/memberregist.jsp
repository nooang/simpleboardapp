<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<style>
    div.grid {
        display: grid;
        grid-template-columns: 120px 1fr;
        grid-template-rows: 28px 28px 28px 28px 1fr;
        row-gap: 10px;
    }
    div.grid > div.btn-group {
        display: grid;
        grid-column: 1 / 3;
    }
    div.grid div.right-align {
        text-align: right;
        padding: left 10px;
    }
    button, input {
        padding: 10px;
        border: 1px solid #ccc;
    }
    input[type=file] {
        padding: 0px;
    }
    div.errors {
        background-color: #ff00004a;
        opacity: 0.8;
        padding: 10px;
        color: #333;
    }
    div.errors:last-child {
        margin-bottom: 15px;
    }
    .available {
        background-color: #0f03;
    }
    .unusable {
        background-color: #f003;
    }
</style>
<script src="/js/lib/jquery-3.7.1.js"></script>
</head>
<body>
    <h1>회원가입</h1>
    <form:form ModelAttribute="memberVO" method="post">
        <div>
            <form:errors path="email" element="div" cssClass="errors" />
            <form:errors path="name" element="div" cssClass="errors" />
            <form:errors path="password" element="div" cssClass="errors" />
        </div>
        <div class="grid">
            <label for="email">이메일</label>
            <input type="email" name="email" id="email" value="${memberVO.email}">
            <label for="name">이름</label>
            <input type="text" name="name" id="name" value="${memberVO.name}">
            <label for="password">비밀번호</label>
            <input type="password" name="password" id="password" value="${memberVO.password}">
            <div class="btn-group">
                <div class="right-align">
                    <input id="btn-regist" disabled="disabled" type="submit" value="회원가입">
                </div>
            </div>
        </div>
    </form:form>
    <a href="/member/login">로그인하러가기</a>
    <script>
        $('#email').keyup(function() {
            $.get('/member/regist/available', {'email': $(this).val()}, function(response) {
                let email = response.email
                let available = response.available
                if (available) {
                    $('#email').addClass('available')
                    $('#email').removeClass('unusable')
                    $('#btn-regist').removeAttr('disabled')
                }
                else {
                    $('#email').addClass('unusable')
                    $('#email').removeClass('available')
                    $('#btn-regist').attr('disabled', 'disabled')
                }
            })
        })
    </script>
</body>
</html>