$(document).ready(function() {
    $('.btn_modify').click(function() {
        var $unitContent = $(this).closest('.unit_content');
        $unitContent.find('.desc_modify').hide();
        $unitContent.find('.input_txt').val($unitContent.find('.desc_modify').text()).show();
        // 라디오 버튼들을 활성화합니다.
        $unitContent.find('.radio_buttons input').prop('disabled', false);
        $unitContent.find('.btn_modify').hide();
        $unitContent.find('.btn_save, .btn_cancel').show();
    });

    $('.btn_save').click(function() {
        var $unitContent = $(this).closest('.unit_content');
        var newValue = $unitContent.find('.input_txt').val();
        // 라디오 버튼이 있는 경우, 선택된 값을 처리합니다.
        var selectedGrade = $unitContent.find('.radio_buttons input:checked').val();
        $unitContent.find('.desc_modify').text(newValue).show();
        $unitContent.find('.input_txt').hide();
        // 라디오 버튼들을 다시 비활성화합니다.
        $unitContent.find('.radio_buttons input').prop('disabled', true);
        $unitContent.find('.btn_save, .btn_cancel').hide();
        $unitContent.find('.btn_modify').show();
    });

    $('.btn_cancel').click(function() {
        var $unitContent = $(this).closest('.unit_content');
        $unitContent.find('.input_txt').hide();
        $unitContent.find('.desc_modify').show();
        // 라디오 버튼들을 다시 비활성화합니다.
        $unitContent.find('.radio_buttons input').prop('disabled', true);
        $unitContent.find('.btn_save, .btn_cancel').hide();
        $unitContent.find('.btn_modify').show();
    });
});

$(document).ready(function() {
    // 모달 아이템 클릭 이벤트 핸들러
    $('.member-modal').on('click', function() {
        // 클릭된 행에서 데이터 속성 값 추출
        var loginId = $(this).attr('data-id') || 'N/A';
        var name = $(this).attr('data-name') || 'N/A';
        var email = $(this).attr('data-email') || 'N/A';
        var phone = $(this).attr('data-phone') || 'N/A';
        var points = $(this).attr('data-points') || '0';
        var signupDate = $(this).attr('data-signupdate') || 'N/A';
        var interiorType = $(this).attr('data-interiortype') || 'N/A';
        var grade = $(this).data('grade') || '';
        // 모달의 각 필드에 값을 설정
        $('#userDetailModal #loginId').text(loginId);
        $('#userDetailModal #name').text(name);
        $('#userDetailModal #email').text(email);
        $('#userDetailModal #hp').text(phone);
        $('#userDetailModal #point').text(points);
        $('#userDetailModal #signupDate').text(signupDate);

        // 등급 라디오 버튼 설정
        // 모달 내의 모든 라디오 버튼을 먼저 체크 해제
        $('#userDetailModal .radio_buttons input[type="radio"]').prop('checked', false);
        // 추출한 grade 값에 해당하는 라디오 버튼을 체크
        $('#userDetailModal .radio_buttons input[type="radio"][value="' + grade + '"]').prop('checked', true);

        // 모달 표시
        $('#userDetailModal').modal('show');
    });

    $('#updateMember').on('click', function(e) {
        e.preventDefault(); // 기본 앵커 동작 방지

        var loginId = $('#userDetailModal #loginId').text();
        var name = $('#userDetailModal #name').text();
        var email = $('#userDetailModal #email').text();
        var phone = $('#userDetailModal #hp').text();
        var points = $('#userDetailModal #point').text();
        // 라디오 버튼 값 가져오기
        var grade = $('#userDetailModal .radio_buttons input[type="radio"]:checked').val();


        // AJAX 요청을 통해 서버에 로그아웃을 알림 (옵션)
        $.ajax({
            type: 'POST',
            url: '/backoffice/updateMember',
            contentType: "application/json",
            data: JSON.stringify({ loginId: loginId, name: name, email: email, phone: phone, points: points,grade: grade }), // 데이터를 JSON 문자열로 변환
            // 성공적으로 로그아웃 처리 시 쿠키 삭제 및 페이지 이동
            success: function(response) {
                window.location.href = "http://localhost:8080/backoffice/members";
            },
            error: function() {
                alert('update failed');
            }
        });
    });
});



$(document).ready(function() {
    // 모달 아이템 클릭 이벤트 핸들러
    $('.product-modal').on('click', function() {
        // 클릭된 행에서 데이터 속성 값 추출
        var id = $(this).attr('data-id') || 'N/A';
        var name = $(this).attr('data-name') || 'N/A';
        var price = $(this).attr('data-price') || '0';
        var discountPercent = $(this).attr('data-discountPercent') || '0';
        var productType = $(this).attr('data-productType') || 'PRODUCT';
        var category = $(this).attr('data-category') || '';
        var stockQuantity = $(this).attr('data-stockQuantity') || '0';
        var productBrand = $(this).data('productBrand') || 'livart';
        // 모달의 각 필드에 값을 설정
        $('#productDetailModal #productName').text(name);
        $('#productDetailModal #productPrice').text(price);
        $('#productDetailModal #discountPercent').text(discountPercent);
        $('#productDetailModal #category').text(category);
        $('#productDetailModal #stockQuantity').text(stockQuantity);
        $('#productDetailModal #productBrand').text(productBrand);

        // 등급 라디오 버튼 설정
        // 모달 내의 모든 라디오 버튼을 먼저 체크 해제
        $('#productDetailModal .radio_buttons input[type="radio"]').prop('checked', false);
        // 추출한 grade 값에 해당하는 라디오 버튼을 체크
        $('#productDetailModal .radio_buttons input[type="radio"][value="' + productType + '"]').prop('checked', true);

        // 모달 표시
        $('#productDetailModal').modal('show');
    });

    $('#updateProduct').on('click', function(e) {
        e.preventDefault(); // 기본 앵커 동작 방지

        var loginId = $('#productDetailModal #loginId').text();
        var name = $('#productDetailModal #name').text();
        var email = $('#productDetailModal #email').text();
        var phone = $('#productDetailModal #hp').text();
        var points = $('#productDetailModal #point').text();
        // 라디오 버튼 값 가져오기
        var grade = $('#productDetailModal .radio_buttons input[type="radio"]:checked').val();


        // AJAX 요청을 통해 서버에 로그아웃을 알림 (옵션)
        $.ajax({
            type: 'POST',
            url: '/backoffice/updateMember',
            contentType: "application/json",
            data: JSON.stringify({ loginId: loginId, name: name, email: email, phone: phone, points: points,grade: grade }), // 데이터를 JSON 문자열로 변환
            // 성공적으로 로그아웃 처리 시 쿠키 삭제 및 페이지 이동
            success: function(response) {
                window.location.href = "http://localhost:8080/backoffice/members";
            },
            error: function() {
                alert('update failed');
            }
        });
    });
});