$(document).ready(function() {
    $('#login').click(function(e) {
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

    $('#register').click(function(e) {
        e.preventDefault(); // 폼의 기본 제출 동작 방지

        // 이메일과 비밀번호 입력 필드에서 값 가져오기
        var loginId = $('#loginId').val();
        var loginPw = $('#loginPw').val();
        var loginPwRepeat = $('#loginPwRepeat').val();
        var name = $('#name').val();

        if (loginPw !== loginPwRepeat){
            alert("비밀번호가 일치하지 않습니다.");
            return;
        }
        // AJAX 요청 설정
        $.ajax({
            type: "POST",
            url: "/backoffice/register",
            contentType: "application/json",
            data: JSON.stringify({ loginId: loginId, loginPw: loginPw, name: name }), // 데이터를 JSON 문자열로 변환
            success: function(response) {
                // 로그인 성공 시 처리, 예: 홈페이지로 리다이렉트
                // window.location.href = "home.html";
                alert("관리자 등록 요청이 완료되었습니다.")
                window.location.href = "http://localhost:8080/backoffice/login";

            },
            error: function(xhr, status, error) {
                // 실패 시 처리, 예: 에러 메시지 표시
                alert("Register failed: " + xhr.responseText);
            }
        });
    });

    $('#logoutModal').on('shown.bs.modal', function() {
        // #logout 버튼에 클릭 이벤트 핸들러 등록
        $('#logout').on('click', function(e) {
            e.preventDefault(); // 기본 앵커 동작 방지

            // AJAX 요청을 통해 서버에 로그아웃을 알림 (옵션)
            $.ajax({
                type: 'POST', // 또는 GET, 서버 구현에 따라 다름
                url: '/backoffice/logout', // 서버의 로그아웃 처리 URL
                // 성공적으로 로그아웃 처리 시 쿠키 삭제 및 페이지 이동
                success: function() {
                    // JWT 토큰 삭제

                    // document.cookie = 'Authorization=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';

                    // 로그인 페이지로 리다이렉트
                    window.location.href = '/backoffice/login';
                },
                error: function() {
                    alert('Logout failed');
                }
            });
        });
    });
    function deleteCookie(name) {
        document.cookie = name + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    }
});