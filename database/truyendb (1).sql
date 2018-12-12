-- phpMyAdmin SQL Dump
-- version 4.8.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th10 29, 2018 lúc 05:34 AM
-- Phiên bản máy phục vụ: 10.1.33-MariaDB
-- Phiên bản PHP: 7.2.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `truyendb`
--

DELIMITER $$
--
-- Thủ tục
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `payChapter` (IN `payerID` BIGINT, IN `receiverID` BIGINT, IN `chapterID` BIGINT, IN `price` BIGINT, IN `payStatus` INT, OUT `result` BIT)  BEGIN
	DECLARE exit handler FOR SQLEXCEPTION
  	BEGIN
    	ROLLBACK;
        SET result=0;
  	END;
    START TRANSACTION;

	INSERT INTO pay(payerID,chID,receiverID,price,payStatus) VALUES (payerID,chapterID,receiverID,price,payStatus);
 	UPDATE user SET gold=gold-price WHERE uID = payerID;
    UPDATE user SET gold=gold+price WHERE uID = receiverID;
	
    
    SET result = 1;

	-- if no errors happened yet, commit transaction
	COMMIT;

SELECT result;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `category`
--

CREATE TABLE `category` (
  `cID` int(11) NOT NULL,
  `cName` varchar(150) CHARACTER SET utf8 NOT NULL COMMENT 'Tên Thể Loại',
  `cMetatitle` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT 'MetaTitle Thể Loại',
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày Tạo',
  `createBy` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Tên Người Tạo',
  `modifiedDate` datetime DEFAULT NULL COMMENT 'Ngày Sửa',
  `modifiedBy` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Người sửa',
  `cStatus` int(11) DEFAULT '1' COMMENT 'Trạng Thái Thể Loại'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Thể Loại';

--
-- Đang đổ dữ liệu cho bảng `category`
--

INSERT INTO `category` (`cID`, `cName`, `cMetatitle`, `createDate`, `createBy`, `modifiedDate`, `modifiedBy`, `cStatus`) VALUES
(1, 'Đô Thị', 'do-thi', '2018-09-13 09:22:08', 'administrator', NULL, NULL, 1),
(2, 'Tiên Hiệp', 'tien-hiep', '2018-09-13 09:25:14', 'administrator', NULL, NULL, 1),
(3, 'Huyền Ảo', 'huyen-ao', '2018-09-13 09:25:14', 'administrator', NULL, NULL, 1),
(4, 'Kiếm Hiệp', 'kiem-hiep', '2018-09-13 09:25:14', 'administrator', NULL, NULL, 1),
(5, 'Võng Du', 'vong-du', '2018-11-07 22:07:35', 'administrator', NULL, NULL, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chapter`
--

CREATE TABLE `chapter` (
  `chID` bigint(20) NOT NULL COMMENT 'ID Chapter',
  `chNumber` int(11) NOT NULL COMMENT 'Chương Thứ',
  `chSerial` float NOT NULL COMMENT 'Số thứ tự chương',
  `chName` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT 'Tên Chương',
  `chView` int(11) DEFAULT '0' COMMENT 'Số View Chương',
  `sID` bigint(20) DEFAULT NULL COMMENT 'ID Truyện',
  `content` text COLLATE utf8_unicode_ci NOT NULL COMMENT 'Nội dung chương',
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày Đăng',
  `uID` bigint(20) DEFAULT NULL COMMENT 'ID Người Đăng',
  `wordNumber` int(11) DEFAULT NULL COMMENT 'Số chữ của chương',
  `price` bigint(20) DEFAULT '0' COMMENT 'Giá Chương Trả Phí',
  `dealine` datetime DEFAULT NULL COMMENT 'Ngày Hết Hạn Trả Phí',
  `chStatus` int(11) DEFAULT '1' COMMENT 'Trạng Thái Chương'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Chapter Truyện';

--
-- Đang đổ dữ liệu cho bảng `chapter`
--

INSERT INTO `chapter` (`chID`, `chNumber`, `chSerial`, `chName`, `chView`, `sID`, `content`, `createDate`, `uID`, `wordNumber`, `price`, `dealine`, `chStatus`) VALUES
(1, 1, 1, 'c1', 14, 1, 'c1', '2018-09-10 22:52:08', 1, 2, 0, NULL, 1),
(2, 1, 1, 'c1', 20, 2, 'c1', '2018-09-10 22:52:09', 1, 2, 10, '2018-11-23 01:03:30', 1),
(3, 2, 2, 'c2', 20, 2, 'c2', '2018-09-11 09:44:09', 2, 2, 0, NULL, 2),
(4, 3, 3, 'c3', 10, 2, 'c3', '2018-11-07 22:30:00', 2, 2, 100, '2018-11-29 00:00:00', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `comment`
--

CREATE TABLE `comment` (
  `comID` bigint(20) NOT NULL COMMENT 'ID bình luận',
  `uID` bigint(20) DEFAULT NULL COMMENT 'ID User',
  `sID` bigint(20) DEFAULT NULL COMMENT 'ID Story',
  `content` text COLLATE utf8_unicode_ci NOT NULL COMMENT 'Nội dung comment',
  `createDate` datetime DEFAULT NULL COMMENT 'Ngày Bình Luận',
  `comStatus` int(11) DEFAULT '1' COMMENT 'Trạng Thái Bình Luận'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Bình Luận';

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `information`
--

CREATE TABLE `information` (
  `infoID` int(11) NOT NULL COMMENT 'ID Infomation',
  `introduce` text COLLATE utf8_unicode_ci COMMENT 'Thông tin giới Thiệu Web',
  `email` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Email WebSite',
  `phone` varchar(13) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Điện Thoại Liên Hệ',
  `skype` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Địa Chỉ Skype',
  `epass` varchar(250) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Mật khẩu Email',
  `logo` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Logo Website',
  `favicon` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Favicon WebSite'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Thông Tin Web';

--
-- Đang đổ dữ liệu cho bảng `information`
--

INSERT INTO `information` (`infoID`, `introduce`, `email`, `phone`, `skype`, `epass`, `logo`, `favicon`) VALUES
(1, 'TruyenOnline là website đọc truyện convert online cập nhật liên tục và nhanh nhất các truyện tiên hiệp, kiếm hiệp, huyền ảo được các thành viên liên tục đóng góp rất nhiều truyện hay và nổi bật.', 'apt.truyenonline@gmail.com', NULL, 'truyenonline', '8101a00c8bcb65062f4d4d3ca01bf854', 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', 'https://res.cloudinary.com/thang1988/image/upload/v1541176897/truyenmvc/favicon.ico');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `pay`
--

CREATE TABLE `pay` (
  `payID` bigint(20) NOT NULL COMMENT 'ID pay',
  `payerID` bigint(20) DEFAULT NULL COMMENT 'ID người trả phí',
  `chID` bigint(20) DEFAULT NULL COMMENT 'ID Chapter Trả',
  `receiverID` bigint(20) DEFAULT NULL COMMENT 'ID người Nhận',
  `price` bigint(20) DEFAULT NULL COMMENT 'Price Trả Phí',
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày Trả Phí',
  `payStatus` int(11) DEFAULT '1' COMMENT 'Trạng Thái Thanh Toán'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Thanh Toán Phí';

--
-- Đang đổ dữ liệu cho bảng `pay`
--

INSERT INTO `pay` (`payID`, `payerID`, `chID`, `receiverID`, `price`, `createDate`, `payStatus`) VALUES
(5, 1, 2, 2, 100, '2018-11-26 10:39:43', 1),
(6, 1, 2, 3, 200, '2018-11-26 10:48:09', 1),
(12, 1, 2, 1, 10, '2018-11-26 11:16:04', 1),
(13, 2, 2, 1, 10, '2018-11-26 11:17:06', 1),
(14, 1, 4, 2, 100, '2018-11-26 21:58:24', 1),
(15, 1, NULL, NULL, 100, NULL, 2),
(16, 1, NULL, NULL, 1000, NULL, 3),
(17, 1, NULL, NULL, 1000, NULL, 3),
(18, 1, NULL, NULL, 1000, NULL, 3),
(19, 1, NULL, NULL, 1000, NULL, 3),
(20, 1, NULL, NULL, 1000, NULL, 3),
(21, 1, NULL, NULL, 1000, NULL, 3),
(22, 1, NULL, NULL, 2000, NULL, 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `role`
--

CREATE TABLE `role` (
  `rID` int(11) NOT NULL COMMENT 'ID Phân QUyền ',
  `rName` varchar(150) CHARACTER SET utf8 NOT NULL COMMENT 'Tên Phân Quyền'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Phân Quyền Người Dùng';

--
-- Đang đổ dữ liệu cho bảng `role`
--

INSERT INTO `role` (`rID`, `rName`) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_SMOD'),
(3, 'ROLE_CONVERTER'),
(4, 'ROLE_USER');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `story`
--

CREATE TABLE `story` (
  `sID` bigint(20) NOT NULL COMMENT 'ID Truyện',
  `vnName` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT 'Tên truyện Tiếng Việt',
  `sMetaTitle` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên truyện MetaTitle',
  `cnName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Tên truyện tiếng trung',
  `cnLink` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Link truyện tiếng Trung',
  `sImages` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Hình ảnh truyện',
  `sAuthor` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT 'Tác giả truyện',
  `sInfo` text COLLATE utf8_unicode_ci NOT NULL COMMENT 'Thông tin truyện',
  `sView` int(11) DEFAULT '0' COMMENT 'Số lượt xem',
  `sRating` float DEFAULT '0' COMMENT 'Điểm đánh giá',
  `sConverter` bigint(20) NOT NULL COMMENT 'Người Đăng',
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày Tạo',
  `modifiedBy` datetime DEFAULT NULL COMMENT 'Ngày Sửa',
  `sPrice` int(11) DEFAULT '0' COMMENT 'Giá truyện',
  `sTimeDeal` int(11) DEFAULT NULL COMMENT 'Thời gian trả phí',
  `sDealStatus` int(11) DEFAULT '0' COMMENT 'Trạng Thái Trả Phí',
  `sUpdate` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày Cập Nhật',
  `sStatus` int(11) DEFAULT '1' COMMENT 'Trạng Thái Truyện'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Truyện';

--
-- Đang đổ dữ liệu cho bảng `story`
--

INSERT INTO `story` (`sID`, `vnName`, `sMetaTitle`, `cnName`, `cnLink`, `sImages`, `sAuthor`, `sInfo`, `sView`, `sRating`, `sConverter`, `createDate`, `modifiedBy`, `sPrice`, `sTimeDeal`, `sDealStatus`, `sUpdate`, `sStatus`) VALUES
(1, 'Tu Chân Nói Chuyện Phiếm Quần', 'tu-chan-noi-chuyen-phiem-quan', '修真聊天群', 'http://www.qidian.com/Book/3602691.aspx', 'https://res.cloudinary.com/thang1988/image/upload/v1539535310/truyenmvc/tu-chan-noi-chuyen-phiem-quan.jpg', 'Thánh kỵ sĩ truyền thuyết', 'Một ngày, Tống Thư Hàng ngoài ý muốn gia nhập một cái tiên hiệp chuunibyou thâm niên người bệnh giao lưu quần, bên trong quần bạn nhóm đều lấy \'Đạo hữu\' tương xứng, quần danh thiếp đều là các loại Phủ chủ, Động chủ, Chân nhân, Thiên Sư. Liền chủ nhóm lạc đường chó cảnh đều xưng là Đại yêu khuyển rời nhà trốn đi. Cả ngày nói chuyện là luyện đan, xông bí cảnh, luyện công kinh nghiệm cái gì.\r\n\r\nĐột nhiên có một ngày, lặn xuống nước hồi lâu hắn đột nhiên phát hiện... Trong đám mỗi một cái quần viên, vậy mà toàn bộ là Tu chân giả, có thể di sơn đảo hải, trường sinh ngàn năm cái chủng loại kia!', 14, 0, 1, '2018-09-01 22:48:35', NULL, 0, 0, 0, '2018-09-10 22:48:35', 1),
(2, 'Nhất Niệm Vĩnh Hằng', 'nhat-niem-vinh-hang', '一念永恒', 'http://www.qidian.com/Book/1003354631.aspx', 'https://res.cloudinary.com/thang1988/image/upload/v1539535551/truyenmvc/nhat-niem-vinh-hang.jpg', 'Nhĩ Căn', 'Nhất niệm thành thương hải , nhất niệm hóa tang điền . nhất niệm trảm thiên ma , nhất niệm tru vạn tiên . duy ngã niệm …… vĩnh hằng', 50, 0, 1, '2018-09-10 22:49:46', NULL, 0, 0, 0, '2018-11-07 22:30:00', 2),
(3, 'Dị Thế Giới Mỹ Thực Gia', 'di-the-gioi-my-thuc-gia', '异世界的美食家', 'https://www.lread.net/read/51823/', 'https://res.cloudinary.com/thang1988/image/upload/v1539535665/truyenmvc/di-the-gioi-my-thuc-gia.jpg', 'Lý Hồng Thiên', 'Ở Vũ Giả nhấc tay có thể Liệt Sơn xuyên, súy chân có thể đoạn trường hà Huyền Huyễn trên thế giới, tồn tại như vậy một nhà nhà hàng nhỏ . \r\n\r\nNhà hàng nhỏ không lớn, nhưng là vô số cường giả đỉnh cao xua như xua vịt chi địa . \r\n\r\nỞ đàng kia ngươi có thể thưởng thức được dùng trứng Phượng Hoàng cùng Long Huyết mét xào thành cơm xào trứng . \r\n\r\nỞ đàng kia ngươi có thể Hát đáo Sinh Mệnh Chi Tuyền xứng Chu Quả chế riêng Liệt Tửu . \r\n\r\nỞ đàng kia ngươi có thể ăn được Cửu Giai Thánh Thú nhục thân hợp với Hắc hồ tiêu thịt quay . \r\n\r\nCái gì ngươi nghĩ đem đầu bếp bắt về nhà ? Không có khả năng, bởi vì nhà hàng cửa nằm một con giữ cửa Thập Giai Thần Thú, Địa Ngục Khuyển . \r\n\r\nOh, người đầu bếp còn có một cái có thể một tay bóp chết Cửu Phẩm Chí Tôn cục sắt trợ thủ cùng một đám bị chinh phục dạ dày điên cuồng nữ nhân . ', 0, 0, 1, '2018-09-10 22:49:46', NULL, 0, 0, 0, '2018-09-10 22:49:47', 1),
(4, 'Mục Thần Ký', 'muc-than-ky', '牧神记', 'https://book.qidian.com/info/1009704712', 'https://res.cloudinary.com/thang1988/image/upload/v1539535810/truyenmvc/muc-than-ky.jpg', 'Trạch Trư', 'Đại khư tổ huấn nói, trời tối, đừng đi ra ngoài.\r\n\r\nĐại khư tàn lão thôn già yếu tàn tật bọn họ từ bờ sông nhặt được một đứa bé, lấy tên Tần Mục, ngậm đắng nuốt cay đem hắn nuôi lớn. Một ngày này màn đêm buông xuống, bóng tối bao trùm đại khư, Tần Mục đi ra cửa chính. . .\r\n\r\nLàm gió xuân bên trong nhộn nhạo nhân vật phản diện đi!\r\n\r\nNgười mù nói với hắn.\r\n\r\nTần Mục nhân vật phản diện chi lộ, ngay tại quật khởi!', 0, 0, 1, '2018-09-13 09:29:52', NULL, 0, 0, 0, '2018-09-13 09:29:52', 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user`
--

CREATE TABLE `user` (
  `uID` bigint(20) NOT NULL,
  `uName` varchar(30) CHARACTER SET utf8 NOT NULL,
  `uPass` varchar(60) COLLATE utf8_unicode_ci NOT NULL,
  `uDname` varchar(25) CHARACTER SET utf8 DEFAULT NULL,
  `uEmail` varchar(150) COLLATE utf8_unicode_ci NOT NULL,
  `notification` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `gold` double DEFAULT '0',
  `uAvatar` varchar(255) COLLATE utf8_unicode_ci DEFAULT 'avatar.jpg',
  `modifiedDate` datetime DEFAULT NULL,
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP,
  `uStatus` int(11) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Người dùng';

--
-- Đang đổ dữ liệu cho bảng `user`
--

INSERT INTO `user` (`uID`, `uName`, `uPass`, `uDname`, `uEmail`, `notification`, `gold`, `uAvatar`, `modifiedDate`, `createDate`, `uStatus`) VALUES
(1, 'administrator', '$2a$10$N0eqNiuikWCy9ETQ1rdau.XEELcyEO7kukkfoiNISk/9F7gw6eB0W', 'Chủ Quản', 'administrator@gmail.com', 'Đời Không Như Là Mơ', 1580, 'http://res.cloudinary.com/thang1988/image/upload/v1/truyenmvc/administrator-308016544095064', '2018-11-08 11:31:13', '2018-08-15 18:26:31', 1),
(2, 'nhubang8x', '$2a$10$F6AV3A2HvcfHGk4fUje35.caAKEDAJs0CAi4/APoGEWRR.JPkt2ve', 'Huy Thắng', 'tieupham304@gmail.com', NULL, 300, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', '2018-10-16 10:11:53', '2018-08-15 18:30:13', 1),
(3, 'hieu13', '$2a$10$JlnArVWUSRMccWGTGM5Xv.5DzWNEQRgSJWbhkQLTovNtv6oKEBhX.', 'hieu13', 'hieu13@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-09-23 19:22:24', 1),
(5, 'tieupham304', '$2a$10$F6AV3A2HvcfHGk4fUje35.caAKEDAJs0CAi4/APoGEWRR.JPkt2ve', 'tieupham304', 'tieupham1988@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 08:34:46', 1),
(6, 'tieupham1988', '$2a$10$dtGgOrabNjGVN1joaH6M7Ourdu/sC11DTlH8uw6Yea7eHjKbiq9Y6', 'tieupham1988', 'tieupham30488@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 08:35:49', 1),
(8, 'nttvirgos', '$2a$10$FyPDfA1IV8251Ztuj8dzCek8yMBaectgCNbjI7xBaFQ9ZyUFgSoYi', 'nttvirgos', 'nttvirgos@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 08:37:17', 1),
(9, 'ngocanh58', '$2a$10$ixQ06gs9YAqo.sq7rPTc6ujyLIhZ.mt.H7HrCgxCPadYtAXyLPjQ.', 'ngocanh58', 'ngocnha58@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 18:02:17', 1),
(10, 'hieu131', '$2a$10$Ctl/GBtacG7Xbk8Dn9zG8uGFqNk6BFdRz/QfOYtozwlZ6Mo55kWLG', 'hieu131', 'hieu131@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 18:10:48', 1),
(11, 'nhubang99x', '$2a$10$ETb09C4yyeAP7NaeMWcJ5e1d1LlqubudZHT7Yacuzn5NyLU6UlGKO', 'nhubang99x', 'nhubang9x@yahoo.com.vn', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 21:10:36', 1),
(13, 'aahaah', '$2a$10$JPpZHSBIMQT1k4aneplE2uaDVcRJfFz.kfkbFpPF5ldUADJ6UK9HS', 'aahaah', 'aahaah@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 22:07:11', 1),
(14, 'aahaah2', '$2a$10$njIa/OvEICKvCV06yVkrY.9H9BGUYyoAP0doZwvi/KsO.gzqBu8a6', 'aahaah2', 'aahaah2@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 22:10:34', 1),
(15, 'aahaah22', '$2a$10$N.AxvloIYKjkdYDCU.NBzeEjilHxqSv1uPyxJ.NA403NPetkL54mW', 'aahaah22', 'aahaah22@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 22:12:08', 1),
(16, 'aahaah221', '$2a$10$6tFyOuGD8LnAK/bkNST5m.S8uQNCh1ZCLMBnbZqI341i/8cIIhvdu', 'aahaah221', 'aahaah122@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 22:14:20', 1),
(17, 'aahaah2212', '$2a$10$HMjuC6/AgR2XR.jUG/DN0OglJV9LctXT3w/XAZN3IK2AJYJktH8Ey', 'aahaah2212', 'aahaah2212@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 22:16:23', 1),
(18, 'aahaah22122', '$2a$10$PNdQaj/23pQdxLbDlTezFOltMyS2HpBUKI9oo83s6gl/mTk1HGEVm', 'aahaah22122', 'aahaah22122@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 22:18:16', 1),
(19, 'hhhhhh', '$2a$10$TSQUJ3hg1XvuiPSbJQw2VeYSvwuik3e/.IybwcM.R0j/sRsyIfw76', 'hhhhhh', 'hhhhhh@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 22:52:22', 1),
(20, 'as22', '$2a$10$5NYlytPaKXNgyJk.ekH2JOl7jKguHH8rFzhK6gSBMkCLbo1bCKbHy', 'as22', 'as22@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 23:04:28', 1),
(21, 'asdasda35', '$2a$10$9RHeI5.xRP8vBYll/kLgsuWNa3emRqRn2w1BppGe7KwC60AIQtVHi', 'asdasda35', 'asdasda35@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 23:07:23', 1),
(22, '12311', '$2a$10$gUorG4gvQHwOyZyXCMNcnuLJ14at3g4Grsf3mGElkrQobBofQljtC', '12311', '12311@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 23:10:54', 1),
(23, 'nhubang8x11', '$2a$10$ODzw6XeVg8HzdedHtJJwde/CFDSnMU3kvy1lPt137xMz477bemMWm', 'nhubang8x11', 'nhubang8x11@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 23:21:48', 1),
(24, 't43t3t43', '$2a$10$Mv6m.7W98X89bsDu9i11ke4t9AQI7CdLi9uH/5UN9c9iC2TThIKXi', 't43t3t43', 't43t3t43@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 23:35:07', 1),
(25, '324234', '$2a$10$QfHy9FRvQINh/OI/1X4ZcuAcjFiHfISwpfHAzxj1yLpK8l4LKrnja', '324234', '324234@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 23:43:36', 1),
(26, '32r23r', '$2a$10$2O65AuKFxE7oO/io0nTkTeICZkFKNK0MywaR8orpgvI//euBKThCS', '32r23r', '32r23r@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-15 23:45:15', 1),
(27, 'tiupham1988', '$2a$10$erVd7K9uqFxq.0fnszkFdOmxsLESffbXPF2fS/8Rp8XJtuZ6RD7cG', 'tiupham1988', 'tiupham1988@gmail.com', NULL, 0, 'https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png', NULL, '2018-10-29 19:56:39', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `_scategory`
--

CREATE TABLE `_scategory` (
  `sID` bigint(20) NOT NULL COMMENT 'ID của Truyện',
  `cID` int(11) NOT NULL COMMENT 'ID Của Category'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Thể Loại Truyện';

--
-- Đang đổ dữ liệu cho bảng `_scategory`
--

INSERT INTO `_scategory` (`sID`, `cID`) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 2),
(2, 3),
(3, 1),
(3, 3),
(3, 4),
(4, 2),
(4, 3);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `_sconverter`
--

CREATE TABLE `_sconverter` (
  `uID` bigint(20) NOT NULL COMMENT 'ID Người Đăng',
  `sID` bigint(20) NOT NULL COMMENT 'ID Truyện',
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày Tạo',
  `status` int(11) DEFAULT '1' COMMENT 'Trạng Thái Converter'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Converter của truyện';

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `_seditor`
--

CREATE TABLE `_seditor` (
  `uID` bigint(20) NOT NULL COMMENT 'ID Người Duyệt',
  `sID` bigint(20) NOT NULL COMMENT 'ID Truyện',
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày Tạo',
  `status` int(11) DEFAULT '1' COMMENT 'Trạng Thái Editor'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `_srating`
--

CREATE TABLE `_srating` (
  `uID` bigint(20) NOT NULL COMMENT 'ID User',
  `sID` bigint(20) NOT NULL COMMENT 'ID Truyện',
  `locationID` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Địa chỉ IP',
  `rating` float NOT NULL COMMENT 'Đánh Giá',
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày Đánh Giá'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Đánh Giá Truyện';

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `_ufavorites`
--

CREATE TABLE `_ufavorites` (
  `ufID` bigint(20) NOT NULL COMMENT 'ID User Favorites',
  `uID` bigint(20) DEFAULT NULL COMMENT 'ID User',
  `chID` bigint(20) DEFAULT NULL COMMENT 'ID Chapter',
  `locationIP` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Địa chỉ máy đọc',
  `ufStatus` int(11) DEFAULT '1' COMMENT 'Trạng Thái Hiển Thị',
  `ufView` int(11) DEFAULT '1' COMMENT 'Trạng Thái Tính Lượt Đọc',
  `dateView` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời Gian Đọc'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Yêu Thích';

--
-- Đang đổ dữ liệu cho bảng `_ufavorites`
--

INSERT INTO `_ufavorites` (`ufID`, `uID`, `chID`, `locationIP`, `ufStatus`, `ufView`, `dateView`) VALUES
(54, NULL, 2, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-07 21:14:37'),
(55, NULL, 3, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-07 21:14:55'),
(56, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 21:14:56'),
(57, NULL, 1, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-07 21:20:03'),
(58, NULL, 1, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-07 21:20:14'),
(59, NULL, 1, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 21:21:40'),
(60, NULL, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 21:22:52'),
(61, NULL, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 21:23:11'),
(62, NULL, 1, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 21:23:47'),
(63, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 21:24:13'),
(64, NULL, 4, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-07 22:30:49'),
(65, NULL, 3, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-07 22:30:52'),
(66, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:31:02'),
(67, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:32:03'),
(68, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:32:12'),
(69, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:35:30'),
(70, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:35:32'),
(71, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:35:35'),
(72, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:37:10'),
(73, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:37:14'),
(74, NULL, 2, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-07 22:37:16'),
(75, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:37:19'),
(76, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:37:21'),
(77, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:37:24'),
(78, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:37:25'),
(79, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:51:03'),
(80, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:51:08'),
(81, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:54:32'),
(82, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:54:34'),
(83, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:55:16'),
(84, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:55:19'),
(85, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:57:36'),
(86, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:57:38'),
(87, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:57:41'),
(88, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:59:06'),
(89, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:59:08'),
(90, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:59:50'),
(91, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 22:59:53'),
(92, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:00:26'),
(93, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:00:30'),
(94, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:01:07'),
(95, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:01:11'),
(96, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:01:34'),
(97, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:01:40'),
(98, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:02:06'),
(99, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:02:10'),
(100, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:04:20'),
(101, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:04:24'),
(102, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:04:50'),
(103, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:04:54'),
(104, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:05:58'),
(105, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:06:01'),
(106, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:06:37'),
(107, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:06:41'),
(108, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:06:47'),
(109, NULL, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:06:51'),
(110, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:07:06'),
(111, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:07:08'),
(112, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:08:09'),
(113, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:08:09'),
(114, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:08:11'),
(115, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:08:29'),
(116, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:08:31'),
(117, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:10:23'),
(118, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:10:23'),
(119, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:10:24'),
(120, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:10:26'),
(121, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:10:27'),
(122, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:10:39'),
(123, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:10:41'),
(124, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:10:42'),
(125, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:10:44'),
(126, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:12:27'),
(127, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:12:29'),
(128, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:12:31'),
(129, 1, 3, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-07 23:15:38'),
(130, 1, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:15:41'),
(131, 1, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:16:31'),
(132, 1, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:16:35'),
(133, 1, 1, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-07 23:16:56'),
(134, 1, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:16:56'),
(135, 1, 2, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-07 23:16:59'),
(136, 1, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:17:49'),
(137, 1, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:17:50'),
(138, 1, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:17:59'),
(139, 1, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-07 23:18:00'),
(140, 1, 4, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-07 23:18:02'),
(141, NULL, 3, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-08 10:51:09'),
(142, NULL, 2, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-08 10:51:14'),
(143, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-08 10:51:16'),
(144, NULL, 4, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-08 10:51:18'),
(145, NULL, 2, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-08 20:12:38'),
(146, NULL, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-08 20:13:04'),
(147, NULL, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-08 20:18:26'),
(148, 1, 2, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-08 20:19:16'),
(149, 2, 2, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-08 20:21:55'),
(150, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-08 20:21:57'),
(151, NULL, 1, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-13 21:55:23'),
(152, NULL, 1, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-13 21:57:30'),
(153, NULL, 1, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-13 22:17:35'),
(154, NULL, 1, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-14 10:12:30'),
(155, NULL, 3, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-15 09:18:06'),
(156, NULL, 2, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-15 09:18:10'),
(157, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:18:12'),
(158, NULL, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:18:13'),
(159, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:18:14'),
(160, NULL, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:18:15'),
(161, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:18:27'),
(162, NULL, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:18:28'),
(163, NULL, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:19:02'),
(164, NULL, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:19:06'),
(165, NULL, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:19:44'),
(166, 2, 2, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-15 09:20:51'),
(167, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:22:00'),
(168, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:22:03'),
(169, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:25:29'),
(170, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:25:33'),
(171, NULL, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:26:20'),
(172, NULL, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:26:31'),
(173, 2, 3, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-15 09:26:43'),
(174, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:26:44'),
(175, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:30:05'),
(176, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:30:33'),
(177, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:30:52'),
(178, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:31:12'),
(179, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:33:20'),
(180, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:33:29'),
(181, NULL, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:34:27'),
(182, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:34:47'),
(183, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:35:34'),
(184, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:35:44'),
(185, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:35:46'),
(186, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:39:27'),
(187, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:39:54'),
(188, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:39:59'),
(189, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:40:19'),
(190, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:40:21'),
(191, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:43:08'),
(192, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:43:11'),
(193, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:43:50'),
(194, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:43:56'),
(195, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:44:06'),
(196, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:44:36'),
(197, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:44:39'),
(198, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:45:59'),
(199, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:50:54'),
(200, 2, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:50:57'),
(201, NULL, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:53:20'),
(202, NULL, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-15 09:53:25'),
(203, NULL, 1, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-15 09:54:22'),
(204, NULL, 1, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-20 11:04:44'),
(205, NULL, 1, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-20 11:07:38'),
(206, NULL, 3, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-20 11:07:48'),
(207, NULL, 4, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-20 11:07:53'),
(208, NULL, 2, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-20 11:11:39'),
(209, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-20 11:11:45'),
(210, NULL, 1, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-25 14:13:56'),
(211, NULL, 2, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-25 14:14:12'),
(212, NULL, 3, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-25 14:14:15'),
(213, NULL, 4, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-25 14:14:17'),
(214, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 14:14:48'),
(215, NULL, 2, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-25 15:06:00'),
(216, NULL, 3, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-25 15:06:03'),
(217, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 15:07:01'),
(218, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 15:07:19'),
(219, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 15:16:18'),
(220, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 15:16:53'),
(221, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 15:17:56'),
(222, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 15:18:25'),
(223, NULL, 2, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-25 22:13:05'),
(224, NULL, 3, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-25 22:13:09'),
(225, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 22:14:58'),
(226, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 22:15:18'),
(227, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 22:17:15'),
(228, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 22:23:28'),
(229, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 22:23:29'),
(230, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 22:23:31'),
(231, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 22:24:51'),
(232, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 22:27:44'),
(233, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 22:27:44'),
(234, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 22:28:55'),
(235, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 22:29:10'),
(236, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 22:29:55'),
(237, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 22:43:13'),
(238, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 22:53:03'),
(239, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 22:54:05'),
(240, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 22:54:09'),
(241, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 22:54:13'),
(242, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 22:56:20'),
(243, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-25 22:57:40'),
(244, NULL, 1, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-26 00:21:13'),
(245, NULL, 1, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 00:21:19'),
(246, NULL, 1, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 00:21:46'),
(247, NULL, 1, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 00:23:20'),
(248, NULL, 1, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 00:29:55'),
(249, NULL, 2, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-26 00:34:50'),
(250, NULL, 3, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-26 21:19:02'),
(251, NULL, 4, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-26 21:19:06'),
(252, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 21:19:31'),
(253, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 21:20:57'),
(254, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 21:25:38'),
(255, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 21:26:37'),
(256, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 21:28:48'),
(257, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 21:28:54'),
(258, NULL, 4, '127.0.0.1', 1, 1, '2018-11-26 21:34:23'),
(259, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 21:38:38'),
(260, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 21:38:44'),
(261, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 21:43:46'),
(262, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 21:45:49'),
(263, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 21:48:52'),
(264, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 21:52:34'),
(265, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 21:52:40'),
(266, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 21:55:24'),
(267, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 21:58:05'),
(268, 1, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 21:58:20'),
(269, 1, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 21:58:25'),
(270, 1, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 21:59:19'),
(271, 1, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 21:59:36'),
(272, 1, 3, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-26 22:00:44'),
(273, 1, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:00:48'),
(274, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:02:06'),
(275, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:08:27'),
(276, 1, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:08:54'),
(277, 1, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:08:58'),
(278, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:15:56'),
(279, 1, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:16:07'),
(280, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:23:14'),
(281, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:23:18'),
(282, 1, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:23:33'),
(283, 1, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:32:51'),
(284, 1, 3, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-26 22:32:55'),
(285, 1, 2, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-26 22:32:57'),
(286, 1, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:34:03'),
(287, 1, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:34:07'),
(288, 1, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:34:08'),
(289, 1, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:34:13'),
(290, 1, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:34:14'),
(291, 1, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:38:28'),
(292, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:38:34'),
(293, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:38:37'),
(294, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:38:40'),
(295, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:38:42'),
(296, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:39:50'),
(297, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:42:07'),
(298, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:42:09'),
(299, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:49:00'),
(300, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:49:02'),
(301, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:49:20'),
(302, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:51:29'),
(303, 3, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-26 22:55:56'),
(304, 2, 3, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-27 10:04:49'),
(305, 2, 4, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-27 10:04:51'),
(306, 3, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:05:11'),
(307, 3, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:05:12'),
(308, 3, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:05:15'),
(309, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:06:47'),
(310, NULL, 4, '127.0.0.1', 1, 1, '2018-11-27 10:06:48'),
(311, 3, 4, '127.0.0.1', 1, 0, '2018-11-27 10:07:01'),
(312, 3, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:08:31'),
(313, 3, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:10:24'),
(314, 3, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:14:58'),
(315, 3, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:15:04'),
(316, 3, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:20:04'),
(317, 3, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:20:36'),
(318, 3, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:21:45'),
(319, 3, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:23:29'),
(320, NULL, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:24:38'),
(321, 3, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:24:56'),
(322, 3, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:24:57'),
(323, 3, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:24:58'),
(324, 3, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:26:26'),
(325, 3, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:26:26'),
(326, 3, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:27:03'),
(327, 3, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:27:48'),
(328, 3, 4, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:28:37'),
(329, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:41:14'),
(330, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:42:55'),
(331, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-27 10:43:03'),
(332, 1, 3, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-27 20:05:54'),
(333, 1, 1, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-27 20:39:08'),
(334, NULL, 3, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-27 22:10:47'),
(335, NULL, 4, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-28 08:57:09'),
(336, NULL, 3, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-28 11:31:43'),
(337, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-28 11:31:44'),
(338, NULL, 2, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-28 11:32:28'),
(339, NULL, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-28 11:34:30'),
(340, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-28 11:35:20'),
(341, NULL, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-28 11:36:59'),
(342, NULL, 2, '127.0.0.1', 1, 1, '2018-11-28 11:37:25'),
(343, NULL, 2, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-28 11:39:07'),
(344, 2, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-28 12:05:12'),
(345, 2, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-28 12:05:14'),
(346, 1, 3, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-29 01:13:59'),
(347, 1, 3, '0:0:0:0:0:0:0:1', 1, 0, '2018-11-29 01:14:00'),
(348, 1, 3, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-29 10:02:50'),
(349, 1, 2, '0:0:0:0:0:0:0:1', 1, 1, '2018-11-29 10:02:56');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `_urole`
--

CREATE TABLE `_urole` (
  `uID` bigint(20) NOT NULL COMMENT 'ID User',
  `rID` int(11) NOT NULL COMMENT 'ID của Role',
  `createDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày Tạo',
  `createBy` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Người Tạo'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Table Quyền Của User';

--
-- Đang đổ dữ liệu cho bảng `_urole`
--

INSERT INTO `_urole` (`uID`, `rID`, `createDate`, `createBy`) VALUES
(1, 1, '2018-08-15 18:31:47', NULL),
(1, 4, '2018-08-15 18:31:47', NULL),
(2, 3, '2018-08-15 18:31:47', NULL),
(2, 4, '2018-08-15 18:31:47', NULL),
(11, 4, '2018-10-15 21:10:36', NULL),
(13, 4, '2018-10-15 22:07:11', NULL),
(14, 4, '2018-10-15 22:10:34', NULL),
(15, 4, '2018-10-15 22:12:08', NULL),
(16, 4, '2018-10-15 22:14:20', NULL),
(17, 4, '2018-10-15 22:16:23', NULL),
(18, 4, '2018-10-15 22:18:16', NULL),
(19, 4, '2018-10-15 22:52:24', NULL),
(20, 4, '2018-10-15 23:04:28', NULL),
(21, 4, '2018-10-15 23:07:23', NULL),
(22, 4, '2018-10-15 23:10:54', NULL),
(23, 4, '2018-10-15 23:21:48', NULL),
(24, 4, '2018-10-15 23:35:07', NULL),
(25, 4, '2018-10-15 23:43:36', NULL),
(26, 4, '2018-10-15 23:45:15', NULL),
(27, 4, '2018-10-29 19:56:39', NULL);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`cID`),
  ADD UNIQUE KEY `cateogry_cName_uindex` (`cName`),
  ADD UNIQUE KEY `cateogry_cMetatitle_uindex` (`cMetatitle`);

--
-- Chỉ mục cho bảng `chapter`
--
ALTER TABLE `chapter`
  ADD PRIMARY KEY (`chID`),
  ADD KEY `chapter_story_sID_fk` (`sID`),
  ADD KEY `chapter_user_uID_fk` (`uID`);

--
-- Chỉ mục cho bảng `comment`
--
ALTER TABLE `comment`
  ADD PRIMARY KEY (`comID`),
  ADD KEY `comment_user_uID_fk` (`uID`),
  ADD KEY `comment_story_sID_fk` (`sID`);

--
-- Chỉ mục cho bảng `information`
--
ALTER TABLE `information`
  ADD PRIMARY KEY (`infoID`);

--
-- Chỉ mục cho bảng `pay`
--
ALTER TABLE `pay`
  ADD PRIMARY KEY (`payID`),
  ADD KEY `chdeal_user_uID_fk` (`payerID`),
  ADD KEY `chdeal_chapter_chID_fk` (`chID`),
  ADD KEY `pay_user_uID_fk` (`receiverID`);

--
-- Chỉ mục cho bảng `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`rID`);

--
-- Chỉ mục cho bảng `story`
--
ALTER TABLE `story`
  ADD PRIMARY KEY (`sID`),
  ADD KEY `story_user_uID_fk` (`sConverter`);

--
-- Chỉ mục cho bảng `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`uID`),
  ADD UNIQUE KEY `user_uName_uindex` (`uName`),
  ADD UNIQUE KEY `user_uEmail_uindex` (`uEmail`),
  ADD UNIQUE KEY `UKq0357f7kx1k6ntp6bvus0x694` (`uEmail`),
  ADD UNIQUE KEY `UKstx4oc1652ecsjfu7ln8e9bmi` (`uName`),
  ADD UNIQUE KEY `user_uDname_uindex` (`uDname`),
  ADD UNIQUE KEY `UK6jmiamq45k36v745fac2gwwnu` (`uDname`);

--
-- Chỉ mục cho bảng `_scategory`
--
ALTER TABLE `_scategory`
  ADD PRIMARY KEY (`sID`,`cID`),
  ADD KEY `_scategory_cateogry_cID_fk` (`cID`);

--
-- Chỉ mục cho bảng `_sconverter`
--
ALTER TABLE `_sconverter`
  ADD PRIMARY KEY (`uID`,`sID`),
  ADD KEY `_sconverter_story_sID_fk` (`sID`);

--
-- Chỉ mục cho bảng `_seditor`
--
ALTER TABLE `_seditor`
  ADD PRIMARY KEY (`uID`,`sID`),
  ADD KEY `_seditor_story_sID_fk` (`sID`);

--
-- Chỉ mục cho bảng `_srating`
--
ALTER TABLE `_srating`
  ADD PRIMARY KEY (`uID`,`sID`),
  ADD KEY `_srating_story_sID_fk` (`sID`);

--
-- Chỉ mục cho bảng `_ufavorites`
--
ALTER TABLE `_ufavorites`
  ADD PRIMARY KEY (`ufID`),
  ADD KEY `ufavorites_user_uID_fk` (`uID`),
  ADD KEY `ufavorites_chapter_chID_fk` (`chID`);

--
-- Chỉ mục cho bảng `_urole`
--
ALTER TABLE `_urole`
  ADD PRIMARY KEY (`uID`,`rID`),
  ADD KEY `_urole_role_rID_fk` (`rID`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `category`
--
ALTER TABLE `category`
  MODIFY `cID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `chapter`
--
ALTER TABLE `chapter`
  MODIFY `chID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID Chapter', AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `information`
--
ALTER TABLE `information`
  MODIFY `infoID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID Infomation', AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `pay`
--
ALTER TABLE `pay`
  MODIFY `payID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID pay', AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT cho bảng `role`
--
ALTER TABLE `role`
  MODIFY `rID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID Phân QUyền ', AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `story`
--
ALTER TABLE `story`
  MODIFY `sID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID Truyện', AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `user`
--
ALTER TABLE `user`
  MODIFY `uID` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT cho bảng `_ufavorites`
--
ALTER TABLE `_ufavorites`
  MODIFY `ufID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID User Favorites', AUTO_INCREMENT=350;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `chapter`
--
ALTER TABLE `chapter`
  ADD CONSTRAINT `FKoiqsft4egp7cxq41euj56hglu` FOREIGN KEY (`uID`) REFERENCES `user` (`uID`),
  ADD CONSTRAINT `chapter_story_sID_fk` FOREIGN KEY (`sID`) REFERENCES `story` (`sID`),
  ADD CONSTRAINT `chapter_user_uID_fk` FOREIGN KEY (`uID`) REFERENCES `user` (`uID`);

--
-- Các ràng buộc cho bảng `comment`
--
ALTER TABLE `comment`
  ADD CONSTRAINT `FK7k38tac8pkcf20qqy1g3g1q5w` FOREIGN KEY (`uID`) REFERENCES `user` (`uID`),
  ADD CONSTRAINT `comment_story_sID_fk` FOREIGN KEY (`sID`) REFERENCES `story` (`sID`),
  ADD CONSTRAINT `comment_user_uID_fk` FOREIGN KEY (`uID`) REFERENCES `user` (`uID`);

--
-- Các ràng buộc cho bảng `pay`
--
ALTER TABLE `pay`
  ADD CONSTRAINT `chdeal_chapter_chID_fk` FOREIGN KEY (`chID`) REFERENCES `chapter` (`chID`),
  ADD CONSTRAINT `chdeal_user_uID_fk` FOREIGN KEY (`payerID`) REFERENCES `user` (`uID`),
  ADD CONSTRAINT `pay_user_uID_fk` FOREIGN KEY (`receiverID`) REFERENCES `user` (`uID`);

--
-- Các ràng buộc cho bảng `story`
--
ALTER TABLE `story`
  ADD CONSTRAINT `FK6ya89n0d9vlpvws2c6aq5bu6t` FOREIGN KEY (`sConverter`) REFERENCES `user` (`uID`),
  ADD CONSTRAINT `story_user_uID_fk` FOREIGN KEY (`sConverter`) REFERENCES `user` (`uID`);

--
-- Các ràng buộc cho bảng `_scategory`
--
ALTER TABLE `_scategory`
  ADD CONSTRAINT `_scategory_cateogry_cID_fk` FOREIGN KEY (`cID`) REFERENCES `category` (`cID`),
  ADD CONSTRAINT `_scategory_story_sID_fk` FOREIGN KEY (`sID`) REFERENCES `story` (`sID`);

--
-- Các ràng buộc cho bảng `_sconverter`
--
ALTER TABLE `_sconverter`
  ADD CONSTRAINT `FK9gs8t382n2th1jwnsjrdkmioh` FOREIGN KEY (`uID`) REFERENCES `user` (`uID`),
  ADD CONSTRAINT `_sconverter_story_sID_fk` FOREIGN KEY (`sID`) REFERENCES `story` (`sID`),
  ADD CONSTRAINT `_sconverter_user_uID_fk` FOREIGN KEY (`uID`) REFERENCES `user` (`uID`);

--
-- Các ràng buộc cho bảng `_seditor`
--
ALTER TABLE `_seditor`
  ADD CONSTRAINT `FK3tvnpnb5jp9fjr6hppsg5xrwl` FOREIGN KEY (`uID`) REFERENCES `user` (`uID`),
  ADD CONSTRAINT `_seditor_story_sID_fk` FOREIGN KEY (`sID`) REFERENCES `story` (`sID`),
  ADD CONSTRAINT `_seditor_user_uID_fk` FOREIGN KEY (`uID`) REFERENCES `user` (`uID`);

--
-- Các ràng buộc cho bảng `_srating`
--
ALTER TABLE `_srating`
  ADD CONSTRAINT `FKl6uc4pj2bg6o8wmos0ihxuu4n` FOREIGN KEY (`uID`) REFERENCES `user` (`uID`),
  ADD CONSTRAINT `_srating_story_sID_fk` FOREIGN KEY (`sID`) REFERENCES `story` (`sID`),
  ADD CONSTRAINT `_srating_user_uID_fk` FOREIGN KEY (`uID`) REFERENCES `user` (`uID`);

--
-- Các ràng buộc cho bảng `_ufavorites`
--
ALTER TABLE `_ufavorites`
  ADD CONSTRAINT `FKsa4pkhu41dl3hk88g1ibv82nh` FOREIGN KEY (`uID`) REFERENCES `user` (`uID`),
  ADD CONSTRAINT `ufavorites_chapter_chID_fk` FOREIGN KEY (`chID`) REFERENCES `chapter` (`chID`),
  ADD CONSTRAINT `ufavorites_user_uID_fk` FOREIGN KEY (`uID`) REFERENCES `user` (`uID`);

--
-- Các ràng buộc cho bảng `_urole`
--
ALTER TABLE `_urole`
  ADD CONSTRAINT `FK6lxlgyqxdujskwtghdho3l5cn` FOREIGN KEY (`uID`) REFERENCES `user` (`uID`),
  ADD CONSTRAINT `_urole_role_rID_fk` FOREIGN KEY (`rID`) REFERENCES `role` (`rID`),
  ADD CONSTRAINT `_urole_user_uID_fk` FOREIGN KEY (`uID`) REFERENCES `user` (`uID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
