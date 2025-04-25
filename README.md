# EVO_IAM SERVICE 1
--- 

1 Sử dụng cặp khóa bất đối xứng private key và public key để tạo jwt token 

2 có chức năng: 

 + đăng nhập 

 + đăng ký 

 + xem thông tin người dùng 

 + update thông tin người dùng 

 + thay đổi mật khẩu 

 + quên mật khẩu 

 + upload ảnh 

 + đăng xuất 

 + Log ghi lại hoạt động người dùng

3 Sử dụng xác thực OTP gửi email qua lớp emailsender khi đăng ký, quên mật khẩu - có tìm hiểu và sử dụng redis để lưu xác nhận otp+ chống spam email gửi nhiều lần 

4 Chức năng thay đổi mật khẩu cần nhập mk cũ, thay đổi thành công sẽ back về log in để xác thực lại( xóa 2 token đang hoạt động, tạo phiên mới )

5 Chức năng log ghi thì em có tìm hiểu và tích hợp thêm spring audit để đỡ phải thủ công 

6 Up load image sử dụng cloundinary -> trả về 1 đường dẫn và lưu vào bảng thông tin url của user 

7 logout ra xóa cả 2 token ( lưu vào blacklist)  và có sử dụng Cronjob lập lịch schedule để xóa các bản ghi trong db blacklist  
