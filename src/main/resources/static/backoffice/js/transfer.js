$(document).ready(function() {

    $('#transfer').on('click', function(e) {
        e.preventDefault(); // 기본 앵커 동작 방지
        var lmsChecked = $('#lms').is(':checked');
        var mailChecked = $('#mail').is(':checked');

        if (!lmsChecked && !mailChecked) {
            alert("문자 / 이메일 전송타입을 체크해주세요.");
            return;
        }

        var memberName = $('.modal-body #memberName').text();
        var toNumber = $('.modal-body #memberPhone').text();
        var toEmail = $('.modal-body #memberEmail').text();
        var contents = $('.modal-body #rejectedMsg').val();
        // var payback = $('.modal-body #restorePayback').text();
        var productName = $(this).attr('data-productName') || '';
        var payback = $(this).attr('data-restorePayback') || '0';
        var requestGrade = $(this).attr('data-requestGrade') || 'A';
        var inspectedGrade = $(this).attr('data-inspectedGrade') || 'A';
        var rejectMsg = $(this).attr('data-rejectMsg') || '';
        var restoreId = $(this).attr('data-restoreId') || '';

        var isLmsSuccess = false;
        var isEmailSuccess = false;
        if (lmsChecked) {
            $.ajax({
                type: 'post',
                url: '/api/messageTransfer',
                data: {
                    toNumber: toNumber,
                    contents: contents,
                },
                success: function(response) {
                    isLmsSuccess = true;
                    checkSuccessAndReward();
                },
                error: function() {
                    alert('문자 전송 요청 실패');
                }
            });
        }
        if (mailChecked) {
            $.ajax({
                type: 'post',
                url: '/api/restore/email',
                contentType: "application/json",
                data: JSON.stringify({
                    toEmail: toEmail,
                    subject: "[H.Livv] Re-Store 검수 완료 알림",
                    rejectMsg: rejectMsg,
                    productName: productName,
                    requestGrade: requestGrade,
                    inspectedGrade: inspectedGrade,
                    payback: payback,
                }),
                success: function(response) {
                    isEmailSuccess = true;
                    checkSuccessAndReward()
                },
                error: function() {
                    alert('메일 전송 요청 실패');
                }
            });
        }
        var request_already = false;
        function checkSuccessAndReward() {
            if (request_already === false) {
                if (isEmailSuccess || isLmsSuccess) {
                    request_already = true;
                    $.ajax({
                        type: 'post',
                        url: '/backoffice/api/restore/rewarded/' + restoreId,
                        data: {

                        },
                        success: function(response) {
                            alert(response);
                            window.location.href="/backoffice/restores";
                        },
                        error: function() {
                            alert('리스토어 완료 요청 실패, 포인트가 지급되지 않았습니다.');
                        }
                    });
                }
            }

        }
        if (lmsChecked && mailChecked) {
            alert('문자/이메일 전송을 요청했습니다.');
        } else if (lmsChecked && !mailChecked) {
            alert('문자 전송을 요청했습니다.');
        } else if (!lmsChecked && mailChecked) {
            alert('이메일 전송을 요청했습니다.');
        }
    });
});