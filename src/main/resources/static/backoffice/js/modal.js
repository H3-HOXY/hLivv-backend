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

    $('.dropdown-item').click(function() {
        // 클릭된 항목의 텍스트를 가져옴
        var statusText = $(this).text();
        // 클릭된 항목으로부터 가장 가까운 .unit_content 요소 내의 <p> 태그를 찾아 텍스트를 업데이트
        $(this).closest('.unit_content').find('p.navbar-brand').text(statusText);
    });
    $('.inspected-grade-item').click(function() {
        // 클릭된 항목의 텍스트를 가져옴
        // $('#restorePayback').text()
        //
        // if (confirm("검수 등급이 변경되었습니다. Re-Store의 진행상태를 검수완료 상태로 변경하시겠습니까?")) {
        //     $('#restoreStatus').text("검수완료");
        //
        //     alert("수정 반영 버튼을 클릭하면 검수완료로 적용됩니다.");
        // }
    });

    $('.transferRestoreMessage').click(function () {
        var resId = $(this).attr('data-restoreId')
        window.location.href="/backoffice/transfer?restoreId="+resId;
    })
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
        var grade = $(this).data('data-grade') || '';
        // 모달의 각 필드에 값을 설정
        console.log(points.toLocaleString())
        $('#userDetailModal #loginId').text(loginId);
        $('#userDetailModal #name').text(name);
        $('#userDetailModal #email').text(email);
        $('#userDetailModal #hp').text(phone);
        $('#userDetailModal #point').text(parseInt(points, 10).toLocaleString());
        $('#userDetailModal #signupDate').text(signupDate);

        // 등급 라디오 버튼 설정
        // 모달 내의 모든 라디오 버튼을 먼저 체크 해제
        $('#userDetailModal .radio_buttons input[type="radio"]').prop('checked', false);
        // 추출한 grade 값에 해당하는 라디오 버튼을 체크
        $('#userDetailModal .radio_buttons input[type="radio"][value="' + grade + '"]').prop('checked', true);

        // 모달 표시
        $('#userDetailModal').modal('show');
    });

    $('.restore-member-modal').on('click', function() {
        var memberId = $(this).attr('data-memberId') || '0';
        $.ajax({
            type: 'get',
            url: '/backoffice/api/member/' + memberId,
            success: function(response) {
                $('#userDetailModal #loginId').text(response.loginId);
                $('#userDetailModal #name').text(response.name);
                $('#userDetailModal #email').text(response.email);
                $('#userDetailModal #hp').text(response.phone);
                $('#userDetailModal #point').text(parseInt(response.points, 10).toLocaleString());
                $('#userDetailModal #signupDate').text(response.signupDate);

                $('#userDetailModal .radio_buttons input[type="radio"]').prop('checked', false);
                // 추출한 grade 값에 해당하는 라디오 버튼을 체크
                $('#userDetailModal .radio_buttons input[type="radio"][value="' + response.grade + '"]').prop('checked', true);

                // 모달 표시
                $('#userDetailModal').modal('show');
            },
            error: function() {
                alert('get member failed');
            }
        });
    });


    $('#updateMember').on('click', function(e) {
        e.preventDefault(); // 기본 앵커 동작 방지

        var loginId = $('#userDetailModal #loginId').text();
        var name = $('#userDetailModal #name').text();
        var email = $('#userDetailModal #email').text();
        var phone = $('#userDetailModal #hp').text();
        var points = $('#userDetailModal #point').text().replace(/,/g, '');
        // 라디오 버튼 값 가져오기
        var grade = $('#userDetailModal .radio_buttons input[type="radio"]:checked').val();


        // AJAX 요청을 통해 서버에 로그아웃을 알림 (옵션)
        $.ajax({
            type: 'POST',
            url: '/backoffice/api/updateMember',
            contentType: "application/json",
            data: JSON.stringify({ loginId: loginId, name: name, email: email, phone: phone, points: points,grade: grade }), // 데이터를 JSON 문자열로 변환
            // 성공적으로 로그아웃 처리 시 쿠키 삭제 및 페이지 이동
            success: function(response) {
                alert('수정이 완료되었습니다.');
                location.reload();
            },
            error: function() {
                alert('update member failed');
            }
        });
    });
});

$(document).ready(function() {
    var id = null;
    // 모달 아이템 클릭 이벤트 핸들러
    $('.product-modal').on('click', function() {
        // 클릭된 행에서 데이터 속성 값 추출
        id = $(this).attr('data-id') || 'N/A';
        var name = $(this).attr('data-name') || 'N/A';
        var price = $(this).attr('data-price') || '0';
        var discountPercent = $(this).attr('data-discountPercent') || '0';
        var productType = $(this).attr('data-productType') || 'PRODUCT';
        var category = $(this).attr('data-category') || '';
        var stockQuantity = $(this).attr('data-stockQuantity') || '0';
        var productBrand = $(this).data('data-productBrand') || 'livart';
        var sellingPrice = price - price*(discountPercent/100)
        // 모달의 각 필드에 값을 설정
        $('#productDetailModal #productName').text(name);
        $('#productDetailModal #productPrice').text(price);
        $('#productDetailModal #discountPercent').text(discountPercent);
        $('#productDetailModal #category').text(category);
        $('#productDetailModal #stockQuantity').text(stockQuantity);
        $('#productDetailModal #productBrand').text(productBrand);
        $('#productDetailModal #sellingPrice').text(sellingPrice);

        $.ajax({
            type: 'get',
            url: '/backoffice/api/getProductImageUrls',
            data: {
                id: id,
            },
            success: function(response) {
                var $carouselInner = $('.carousel-inner');
                var $carouselIndicators = $('.carousel-indicators');
                $carouselInner.empty();
                $carouselIndicators.empty();

                response.forEach(function(imageDto, index) {
                    var $img = $('<div class="carousel-item"><img src="' + imageDto.imageUrl + '" class="d-block w-100"></div>');
                    var $indicator = $('<li data-target="#imageCarousel" data-slide-to="' + index + '"></li>');

                    if (index === 0) {
                        $img.addClass('active');
                        $indicator.addClass('active');
                    }

                    $carouselInner.append($img);
                    $carouselIndicators.append($indicator);
                });
            },
            error: function() {
                alert('get ProductImageUrls failed');
            }
        });

        // 등급 라디오 버튼 설정
        // 모달 내의 모든 라디오 버튼을 먼저 체크 해제
        $('#productDetailModal .radio_buttons input[type="radio"]').prop('checked', false);
        // 추출한 grade 값에 해당하는 라디오 버튼을 체크
        $('#productDetailModal .radio_buttons input[type="radio"][value="' + productType + '"]').prop('checked', true);

        // 모달 표시
        $('#productDetailModal').modal('show');
    });

    var productName = null
    $('.restore-product-modal').on('click', function() {
        // 클릭된 행에서 데이터 속성 값 추출
        //
        // 모달의 각 필드에 값을 설정
        var productId = $(this).attr('data-productId') || '0';
        $.ajax({
            type: 'get',
            url: '/api/product/' + productId,
            success: function(response) {
                id = productId
                productName = response.name
                var sellingPrice = response.price - response.price*(response.discountPercent/100)

                $('#productDetailModal #productName').text(response.name);
                $('#productDetailModal #productPrice').text(response.price);
                $('#productDetailModal #discountPercent').text(response.discountPercent);
                $('#productDetailModal #category').text(response.category.name);
                $('#productDetailModal #stockQuantity').text(response.stockQuantity);
                $('#productDetailModal #productBrand').text(response.productBrand);
                $('#productDetailModal #sellingPrice').text(sellingPrice);

                $('#productDetailModal .radio_buttons input[type="radio"]').prop('checked', false);
                // 추출한 grade 값에 해당하는 라디오 버튼을 체크
                $('#productDetailModal .radio_buttons input[type="radio"][value="' + response.productType + '"]').prop('checked', true);

                var $carouselInner = $('.carousel-inner');
                var $carouselIndicators = $('.carousel-indicators');
                $carouselInner.empty();
                $carouselIndicators.empty();

                response.productImages.forEach(function(imageDto, index) {
                    var $img = $('<div class="carousel-item"><img src="' + imageDto.imageUrl + '" class="d-block w-100"></div>');
                    var $indicator = $('<li data-target="#imageCarousel" data-slide-to="' + index + '"></li>');

                    if (index === 0) {
                        $img.addClass('active');
                        $indicator.addClass('active');
                    }
                    $carouselInner.append($img);
                    $carouselIndicators.append($indicator);
                });
                // 모달 표시
                $('#productDetailModal').modal('show');
            },
            error: function() {
                alert('get product failed');
            }
        });
    });

    $('#updateProduct').on('click', function(e) {
        e.preventDefault(); // 기본 앵커 동작 방지

        var name = $('#productDetailModal #productName').text();
        var price = $('#productDetailModal #productPrice').text();
        var discountPercent = $('#productDetailModal #discountPercent').text();
        var category = $('#productDetailModal #category').text();
        var stockQuantity = $('#productDetailModal #stockQuantity').text();
        var productBrand = $('#productDetailModal #productBrand').text();
        var productType = $('#productDetailModal .radio_buttons input[type="radio"]:checked').val();
        $.ajax({
            type: 'POST',
            url: '/backoffice/api/updateProduct',
            contentType: "application/json",
            data: JSON.stringify({ id: id, name: name, price: price, discountPercent: discountPercent, stockQuantity: stockQuantity }), // 데이터를 JSON 문자열로 변환
            // 성공적으로 로그아웃 처리 시 쿠키 삭제 및 페이지 이동
            success: function(response) {
                alert('수정이 완료되었습니다.');
                location.reload();
            },
            error: function() {
                alert('update failed');
            }
        });
    });
    var originStatus = null
    var restoreId = null
    var productPrice = null
    $('.restore-modal').on('click', function () {
        // 클릭된 행에서 데이터 속성 값 추출
        restoreId = $(this).attr('data-restoreId') || '0';
        var productId = $(this).attr('data-productId') || '0';

        $.ajax({
            type: 'get',
            url: '/backoffice/api/getRestore',
            data: {
                id: restoreId,
            },
            success: function (response) {
                var $carouselInner = $('.restore-carousel-inner');
                var $carouselIndicators = $('.restore-carousel-indicators');
                $carouselInner.empty();
                $carouselIndicators.empty();
                response.restoreImageUrls.forEach(function (imageUrls, index) {
                    var $img = $('<div class="carousel-item"><img src="' + imageUrls + '" class="d-block w-100"></div>');
                    var $indicator = $('<li data-target="#imageCarousel" data-slide-to="' + index + '"></li>');

                    if (index === 0) {
                        $img.addClass('active');
                        $indicator.addClass('active');
                    }

                    $carouselInner.append($img);
                    $carouselIndicators.append($indicator);
                });

                var inspectedGrade = response.inspectedGrade || '검수전'
                var payback = response.payback || '0'
                originStatus = response.restoreStatus
                $('#restoreDetailModal #memberId').text(response.memberId);
                $('#restoreDetailModal #productId').text(response.productId);
                $('#restoreDetailModal #restoreStatus').text(response.restoreStatus);
                $('#restoreDetailModal #regDate').text(response.regDate);
                $('#restoreDetailModal #pickupDate').text(response.pickUpDate);
                $('#restoreDetailModal #requestGrade').text(response.requestGrade);
                $('#restoreDetailModal #inspectedGrade').text(inspectedGrade);
                $('#restoreDetailModal #restorePayback').text(parseInt(payback, 10).toLocaleString()); // 여기!!
                $('#restoreDetailModal #desc').text(response.restoreDesc);
                $('#restoreDetailModal #rejectedMsg').text(response.rejectMsg);
                $('#restoreDetailModal .radio_buttons input[type="radio"]').prop('checked', false);
                // 추출한 grade 값에 해당하는 라디오 버튼을 체크
                $('#restoreDetailModal .radio_buttons input[type="radio"][value="' + response.whenRejected + '"]').prop('checked', true);

            },
            error: function () {
                alert('get RestoreImageUrls failed');
            }
        });

        $.ajax({
            type: 'get',
            url: '/api/product/' + productId,
            success: function(response) {
                $('#restoreDetailModal #productName').text(response.name);
                productPrice = response.price;
            },
            error: function() {
                alert('get product failed');
            }
        });
        // 모달 표시
        $('#restoreDetailModal').modal('show');
    });

    $('#updateRestore').on('click', function(e) {
        e.preventDefault();

        var inspectedGrade = $('#restoreDetailModal #inspectedGrade').text()[0];
        if (inspectedGrade === "검") {
            inspectedGrade = null
        }
        var requestGrade = $('#restoreDetailModal #requestGrade').text()[0];
        var restoreStatus = $('#restoreDetailModal #restoreStatus').text();

        var payback;
        if (requestGrade === 'S') {
            payback = Math.floor(productPrice / 2);
        } else if (requestGrade === 'A'){
            payback = Math.floor((productPrice / 10) * 4);
        } else if (requestGrade === 'B'){
            payback = Math.floor((productPrice / 10) * 3);
        } else {
            payback = 0;
        }
        var rejectedMsg = $('#restoreDetailModal #rejectedMsg').val();
        var whenRejected = $('#restoreDetailModal .radio_buttons input[type="radio"]:checked').val();
        $.ajax({
            type: 'POST',
            url: '/backoffice/api/updateRestore',
            contentType: "application/json",
            data: JSON.stringify({ restoreId: restoreId, restoreStatus: restoreStatus, inspectedGrade: inspectedGrade, requestGrade: requestGrade, payback: payback, rejectMsg: rejectedMsg, whenRejected: whenRejected }), // 데이터를 JSON 문자열로 변환
            // 성공적으로 로그아웃 처리 시 쿠키 삭제 및 페이지 이동
            success: function(response) {
                location.reload();
                alert('수정이 완료되었습니다.');
            },
            error: function() {
                alert('Restore 업데이트에 실패했습니다.');
            }
        });
    });


    $('#transferMessage').on('click', function(e) {
        e.preventDefault();
        window.location.href="/backoffice/transfer?restoreId=" + restoreId;
    });


});