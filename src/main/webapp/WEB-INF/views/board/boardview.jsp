<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${boardVO.id}번 게시글</title>
<style>
    * {
        color: #333;
        font-size: 17px;
    }
    a:link, a:hover, a:visited, a:active {
        color: #333;
        text-decoration: none;
    }
    div.grid {
        display: grid;
        grid-template-columns: 80px 1fr;
        grid-template-rows: 28px 28px 28px 28px 28px 28px 320px 1fr;
        row-gap: 10px;
    }
    div.grid > div.btn-group {
        display: grid;
        grid-column: 1 / 3;
    }
    div.grid div.right-align {
        text-align: right;
    }
    label {
        padding-left: 10px;
    }
    button, input, textarea {
        padding: 10px;
    }
    textarea {
        resize: none;
    }
	ul.horizontal-list {
		padding: 0px;
		margin: 0px;
	}
	ul.horizontal-list li {
		display: inline;
	}
    .membermenu {
        text-align: right;
    }
    div.replies {
        display: grid;
        grid-column: 1 / 3;
    }
    div.replies > .write-reply {
        margin-top: 10px;
        display: grid;
        grid-template-columns: 1fr 80px;
        grid-template-rows: 1fr 40px;
        row-gap: 10px;
        column-gap: 10px;
        align-items: center;
    }
    div.replies > .write-reply > textarea {
        height: 150px;
        display: grid;
        grid-column: 1 / 3;
    }
    div.replies > .reply-items {
        display: grid;
        grid-template-columns: 1fr;
        grid-template-rows: 1fr;
        row-gap: 10px;
    }
    pre.content {
        margin: 0px;
    }
</style>
<script src="/js/lib/jquery-3.7.1.js"></script>
</head>
<body>
    <div class="membermenu">
        <jsp:include page="../member/membermenu.jsp"></jsp:include>
    </div>
    <h1>게시글 조희</h1>
    <div class="grid">
        <label for="subject">제목</label>
        <div>${boardVO.subject}</div>
        <label for="email">이메일</label>
        <div>${boardVO.memberVO.name} (${boardVO.email})</div>
        <label for="viewCnt">조회수</label>
        <div>${boardVO.viewCnt}</div>
        <label for="originFileName">첨부파일</label>
        <div>
            <a href="/board/file/download/${boardVO.id}">${boardVO.originFileName}</a>
        </div>
        <label for="crtDt">등록일</label>
        <div>${boardVO.crtDt}</div>
        <label for="mdfyDt">수정일</label>
        <div>${boardVO.mdfyDt}</div>
        <label for="content">내용</label>
        <div>${boardVO.content}</div>
        <div class="replies">
            <div class="reply-items"></div>
            <div class="write-reply">
                <textarea id="txt-reply"></textarea>
                <button id="btn-save-reply">등록</button>
                <button id="btn-cancel-reply">취소</button>
            </div>
        </div>

        <div class="btn-group">
            <div class="right-align">
                <a href="/board/list">목록으로</a>
                <c:if test="${sessionScope._LOGIN_USER_.email eq boardVO.email}">
                    <a href="/board/modify/${boardVO.id}">수정</a>
                    <a href="/board/delete/${boardVO.id}">삭제</a>
                </c:if>
            </div>
        </div>
    </div>
</body>
<script>
    let modifyReply = function(e) {
        let reply = $(e.currentTarget).closest('.reply')
        let replyId = reply.data('reply-id')
        let content = reply.find('.content').text()

        $('#txt-reply').val(content)
        $('#txt-reply').focus()
        $('#txt-reply').data('mode', 'modify')
        $('#txt-reply').data('target', replyId)
    }

    let deleteReply = function(e) {
        let reply = $(e.currentTarget).closest('.reply')
        let replyId = reply.data('reply-id')
        
        $('#txt-reply').removeData('mode')
        $('#txt-reply').removeData('target')

        if (confirm('댓글을 삭제하시겠습니까?')) {
            $.get(`/board/reply/delete/\${replyId}`, function(response) {
                let result = response.result

                if (result) {
                    loadReplies()
                    $('#txt-reply').val('')
                }
            })
        }
    }

    let reReply = function(e) {
        let reply = $(e.currentTarget).closest('.reply')
        let replyId = reply.data('reply-id')

        $('#txt-reply').data('mode', 're-reply')
        $('#txt-reply').data('target', replyId)
        $('#txt-reply').focus()
    }

    let recommendReply = function(e) {
        let reply = $(e.currentTarget).closest('.reply')
        let replyId = reply.data('reply-id')

        $('#txt-reply').removeData('mode')
        $('#txt-reply').removeData('target')

        $.get(`/board/reply/recommend/\${replyId}`, function(response) {
            let result = response.result
            if (result) {
                loadReplies()
                $('#txt-reply').val('')
            }
        })
    }

    let loadReplies = function() {
        $('.reply-items').html('')

        $.get('/board/reply/${boardVO.id}', function(response) {
            let replies = response.replies

            for (let i = 0; i < replies.length; i++) {
                let reply = replies[i]
                let replyTemplate = `
                <div class="reply" data-reply-id='\${reply.replyId}' style="padding-left: \${(reply.level - 1) * 40}px">
                    <div class="author">
                        \${reply.memberVO.name} (\${reply.email})
                    </div>
                    <div class="recommend-count">
                        추천수: \${reply.recommendCnt}
                    </div>
                    <div class="datetime">
                        <span class="crtdt">(등록: \${reply.crtDt})</span>
                        \${reply.mdfyDt != reply.crtDt ? `<span class="mdfydt">(수정: \${reply.mdfyDt})<span>` : ""}
                    </div>
                    <pre class="content">\${reply.content}</pre>
                    \${reply.email == "${sessionScope._LOGIN_USER_.email}" ?
                    `<div>
                        <span class="modify-reply">수정</span>
                        <span class="delete-reply">삭제</span>
                        <span class="re-reply">답변하기</span>
                    </div>` : 
                    `<div>
                        <span class="recommend-reply">추천하기</span>
                        <span class="re-reply">답변하기</span>
                    </div>`}
                </div>`

                let replyDom = $(replyTemplate)
                replyDom.find('.modify-reply').click(modifyReply)
                replyDom.find('.delete-reply').click(deleteReply)
                replyDom.find('.recommend-reply').click(recommendReply)
                replyDom.find('.re-reply').click(reReply)

                $('.reply-items').append(replyDom)
            }
        })
    }

    loadReplies()

    $('#btn-save-reply').click(function() {
        let reply = $('#txt-reply').val()
        let mode = $('#txt-reply').data('mode')
        let target = $('#txt-reply').data('target')

        if (reply.trim() != '') {
            let body = {'content': reply}
            let url = '/board/reply/${boardVO.id}'

            if (mode === 're-reply') {
                body.parentReplyId = target
            }

            if (mode === 'modify') {
                url = `/board/reply/modify/\${target}`
            }

            $.post(url, body, function(response) {
                let result = response.result
                if (result) {
                    loadReplies()
                    $('#txt-reply').val('')
                }
            })
        }
    })
</script>
</html>