=========================================================
TÀI LIỆU HƯỚNG DẪN SỬ DỤNG
=========================================================

- Một số điều cơ bản cần biết:
- Nội dung của phần này nắm được một số vấn đề như sau:

* Thông tin căn bản về phần mềm
* Cách thức chạy chương trình
  Thông tin cơ bản về phần mềm phân cụm học sinh sử dụng K-Means
* Phần mềm hỗ trợ phân cụm học sinh giúp hoạt động tổ chức giảng dạy trong từng cụm sinh viên phù hợp từ đó giúp cho học sinh cảm thấy yêu thích cũng như phát triển thế mạnh cá nhân. Phần mềm chạy ở máy cá nhân trong giao diện console.
  Cách thức chạy phần mềm
* Bước 1: Download visual studio code tại trang web: https://code.visualstudio.com/download
  Tùy thuộc vào hệ điều hành thì sẽ chọn phiên bản phù hợp
* Bước 2: Cài JDK 8 tại trang web: (Nếu trên máy đã có java thì bỏ qua bước này)
  https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html
* Bước 3: Cài đặt các extension trong VScode

- Step1: Chọn menu extension ở menubar

* Step2: Gõ tìm kiếm: Java Extension Pack và click install. Ở đây em đã install (Cài đặt thành công giống như hình)

- Bước 4: Thầy có thể lấy source code theo 2 cách sau:

* Cách 1: Download zip tại github theo link: https://github.com/quochuy1508/ClusteringStudent_DataMining
  sau đó giải nén source code hoặc clone theo cú pháp:
  git clone https://github.com/quochuy1508/ClusteringStudent_DataMining.git

(Cách này chỉ sử dụng khi đã cài đặt git trên máy)

- Cách 2: Download tại drive theo link: https://drive.google.com/drive/folders/1zOBJzdLsTxQqsXj4a9H3ZmP69L_pCxj-?usp=sharing

* Bước 5: Mở folder ClusteringStudent_DataMining bằng Vscode

* Bước 6: Mở file Main trong thư mục src và click vào run

* Bước 7: Kết quả được lưu trong folder Result:
  Với ứng với mối phân cụm thứ k sẽ được lưu trong folder Result

Trong file csv sẽ liệt kê điểm trung tâm ,các điểm thuộc phân cụm với điểm trung tâm và khoảng cách từ nó đến trung tâm.

Ngoài ra phần mềm còn xuất ra một biểu đồ đường dùng để chọn k trong phương pháp Kmean bằng khuỷu tay sẽ được trình bày chi tiết ở báo cáo
