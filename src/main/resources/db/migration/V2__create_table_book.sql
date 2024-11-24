CREATE TABLE IF NOT EXISTS `tb_book` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `author` varchar(100) NOT NULL ,
  `launch_date` date NOT NULL,
  `price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`)
);