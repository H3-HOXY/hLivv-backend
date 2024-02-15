$(document).ready(function() {
    $('.btn-primary.btn-user.btn-block').click(function(e) {
        e.preventDefault(); // 폼의 기본 제출 동작 방지

        // 이메일과 비밀번호 입력 필드에서 값 가져오기
        var loginId = $('#loginId').val();
        var loginPw = $('#loginPw').val();

        // AJAX 요청 설정
        $.ajax({
            type: "POST",
            url: "/backoffice/login",
            contentType: "application/json",
            data: JSON.stringify({ loginId: loginId, loginPw: loginPw }), // 데이터를 JSON 문자열로 변환
            success: function(response) {
                // 로그인 성공 시 처리, 예: 홈페이지로 리다이렉트
                // window.location.href = "home.html";
                window.location.href = "http://localhost:8080/backoffice/home";
                // alert("일단 성공함")
            },
            error: function(xhr, status, error) {
                // 로그인 실패 시 처리, 예: 에러 메시지 표시
                alert("Login failed: " + xhr.responseText);
            }
        });
    });
});