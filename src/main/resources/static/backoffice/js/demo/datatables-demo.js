// Call the dataTables jQuery plugin
$(document).ready(function() {
  $('#dataTable').DataTable({
    // "processing": true, // 처리 중 상태 표시 활성화
    // "serverSide": true, // 서버 측 처리 활성화
    // "ajax": "/backoffice/members2", // 서버 측 데이터 소스 URL
    "pageLength": 30 // 페이지당 표시할 행의 수
  });
});
